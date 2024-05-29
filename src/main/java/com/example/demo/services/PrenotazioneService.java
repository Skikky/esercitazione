package com.example.demo.services;

import com.example.demo.entities.Prenotazione;
import com.example.demo.entities.Ristorante;
import com.example.demo.entities.Utente;
import com.example.demo.repositories.PrenotazioneRepository;
import com.example.demo.request.PrenotazioneRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PrenotazioneService {
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private RistoranteService ristoranteService;

    public Prenotazione createPrenotazione(PrenotazioneRequest prenotazioneRequest) {
        Utente utente = utenteService.getUtenteById(prenotazioneRequest.getIdUtente());
        Ristorante ristorante = ristoranteService.getRistoranteById(prenotazioneRequest.getIdRistorante());

        LocalDateTime prenotazioneTime = prenotazioneRequest.getDataPrenotazione();
        int postiDisponibili = ristorante.getPosti();
        LocalDateTime chiusuraTime = LocalDateTime.of(prenotazioneTime.toLocalDate(), ristorante.getChiusura());

        if (prenotazioneTime.plusHours(2).isAfter(chiusuraTime)) {
            throw new IllegalArgumentException("Prenotazioni non accettate entro 2 ore dalla chiusura.");
        }

        List<Prenotazione> prenotazioni = prenotazioneRepository.findByRistoranteAndDataOraBetween(
                ristorante, prenotazioneTime.toLocalDate().atStartOfDay(), prenotazioneTime.toLocalDate().atTime(23, 59));

        int postiPrenotati = prenotazioni.stream().mapToInt(Prenotazione::getNumeroPosti).sum();
        if (postiPrenotati + prenotazioneRequest.getNumeroPosti() > postiDisponibili) {
            throw new IllegalArgumentException("Posti non disponibili.");
        }

        Prenotazione prenotazione = Prenotazione.builder()
                .utente(utente)
                .ristorante(ristorante)
                .dataOra(prenotazioneRequest.getDataPrenotazione())
                .numeroPosti(prenotazioneRequest.getNumeroPosti())
                .build();

        return prenotazioneRepository.saveAndFlush(prenotazione);
    }

    public Prenotazione getPrenotazioneById(Long id) {
        Optional<Prenotazione> optionalPrenotazione = prenotazioneRepository.findById(id);
        return optionalPrenotazione.orElseThrow(() -> new IllegalArgumentException("prenotazione non trovata con id: "+id));
    }

    public Prenotazione update(Long id, Prenotazione newPrenotazione) {
        Prenotazione prenotazione = getPrenotazioneById(id);
        prenotazione = Prenotazione.builder()
                .id(prenotazione.getId())
                .utente(newPrenotazione.getUtente())
                .ristorante(newPrenotazione.getRistorante())
                .dataOra(newPrenotazione.getDataOra())
                .numeroPosti(newPrenotazione.getNumeroPosti())
                .build();
        return prenotazioneRepository.saveAndFlush(prenotazione);
    }

    public List<Prenotazione> getAllPrenotazioni() {
        return prenotazioneRepository.findAll();
    }

    public void deletePrenotazione(Long id) {
        prenotazioneRepository.deleteById(id);
    }
}
