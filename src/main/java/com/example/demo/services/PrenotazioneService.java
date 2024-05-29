package com.example.demo.services;

import com.example.demo.entities.Prenotazione;
import com.example.demo.entities.Ristorante;
import com.example.demo.entities.Utente;
import com.example.demo.repositories.PrenotazioneRepository;
import com.example.demo.repositories.UtenteRepository;
import com.example.demo.request.PrenotazioneRequest;
import com.example.demo.response.PrenotazioneResponse;
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
    private UtenteRepository utenteRepository;
    @Autowired
    private RistoranteService ristoranteService;

    private PrenotazioneResponse mapToPrenotazioneResponse(Prenotazione prenotazione) {
        return PrenotazioneResponse.builder()
                .idPrenotazione(prenotazione.getId())
                .idUtente(prenotazione.getUtente().getId())
                .idRistorante(prenotazione.getRistorante().getId())
                .dataOra(prenotazione.getDataOra())
                .numeroPosti(prenotazione.getNumeroPosti())
                .build();
    }

    private Prenotazione mapToPrenotazione(PrenotazioneResponse prenotazioneResponse) {
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setId(prenotazioneResponse.getIdPrenotazione());
        prenotazione.setDataOra(prenotazioneResponse.getDataOra());
        prenotazione.setNumeroPosti(prenotazioneResponse.getNumeroPosti());
        return prenotazione;
    }

    public PrenotazioneResponse createPrenotazione(Long idUtente,PrenotazioneRequest prenotazioneRequest) {
        Utente utente = utenteRepository.getReferenceById(idUtente);
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

        Prenotazione savedPrenotazione = prenotazioneRepository.saveAndFlush(prenotazione);
        return mapToPrenotazioneResponse(savedPrenotazione);
    }

/*
    public void chiudiConto(Long prenotazioneId) {
        Prenotazione prenotazione = getPrenotazioneById(prenotazioneId);
        if (prenotazione.isPagata()) {
            throw new IllegalStateException("La prenotazione è già stata pagata.");
        }
        prenotazione.setPagata(true);
        prenotazioneRepository.delete(prenotazione);
    }

 */

    public Prenotazione getPrenotazioneById(Long id) {
        Optional<Prenotazione> optionalPrenotazione = prenotazioneRepository.findById(id);
        return optionalPrenotazione.orElseThrow(() -> new IllegalArgumentException("prenotazione non trovata con id: "+id));
    }

    public List<Prenotazione> getAllPrenotazioni() {
        return prenotazioneRepository.findAll();
    }

    public void deletePrenotazione(Long id) {
        prenotazioneRepository.deleteById(id);
    }
}
