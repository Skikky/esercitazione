package com.example.demo.services;

import com.example.demo.entities.Prenotazione;
import com.example.demo.entities.Ristorante;
import com.example.demo.entities.Utente;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.repositories.PrenotazioneRepository;
import com.example.demo.repositories.RistoranteRepository;
import com.example.demo.repositories.UtenteRepository;
import com.example.demo.request.PrenotazioneRequest;
import com.example.demo.response.PrenotazioneResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrenotazioneService {
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private RistoranteRepository ristoranteRepository;
    @Autowired
    private ContoService contoService;

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

    public PrenotazioneResponse createPrenotazione(Long idUtente,PrenotazioneRequest prenotazioneRequest) throws EntityNotFoundException {
        Utente utente = utenteRepository.findById(idUtente)
                .orElseThrow(() -> new EntityNotFoundException(idUtente, "Utente"));

        Ristorante ristorante = ristoranteRepository.findById(prenotazioneRequest.getIdRistorante())
                .orElseThrow(() -> new EntityNotFoundException(prenotazioneRequest.getIdRistorante(), "Ristorante"));


        LocalDateTime prenotazioneTime = prenotazioneRequest.getDataPrenotazione();
        int postiDisponibili = ristorante.getPosti();
        LocalDateTime chiusuraTime = LocalDateTime.of(prenotazioneTime.toLocalDate(), ristorante.getChiusura());

        if (prenotazioneTime.plusHours(2).isAfter(chiusuraTime)) {
            throw new IllegalArgumentException("Prenotazioni non accettate entro 2 ore dalla chiusura.");
        }

        if (prenotazioneRequest.getNumeroPosti() > postiDisponibili) {
            throw new IllegalArgumentException("Posti non disponibili.");
        }
        ristorante.setPosti(postiDisponibili-prenotazioneRequest.getNumeroPosti());
        Ristorante savedRistorante = ristoranteRepository.saveAndFlush(ristorante);

        Prenotazione prenotazione = Prenotazione.builder()
                .utente(utente)
                .ristorante(savedRistorante)
                .dataOra(prenotazioneRequest.getDataPrenotazione())
                .numeroPosti(prenotazioneRequest.getNumeroPosti())
                .build();

        Prenotazione savedPrenotazione = prenotazioneRepository.saveAndFlush(prenotazione);

        contoService.createConto(savedPrenotazione);

        return mapToPrenotazioneResponse(savedPrenotazione);
    }

    public Prenotazione getPrenotazioneById(Long id) throws EntityNotFoundException {
        Optional<Prenotazione> optionalPrenotazione = prenotazioneRepository.findById(id);
        return optionalPrenotazione.orElseThrow(() -> new EntityNotFoundException(id,"Prenotazione"));
    }

    public PrenotazioneResponse getPrenotazioneResponseById(Long id) throws EntityNotFoundException {
        Prenotazione prenotazione = getPrenotazioneById(id);
        return mapToPrenotazioneResponse(prenotazione);
    }

    public List<PrenotazioneResponse> getAllPrenotazioniResponse() {
        return prenotazioneRepository.findAll().stream()
                .map(this::mapToPrenotazioneResponse)
                .collect(Collectors.toList());
    }

    public void deletePrenotazione(Long id) throws EntityNotFoundException {
        Prenotazione prenotazione = getPrenotazioneById(id);
        Ristorante ristorante = prenotazione.getRistorante();
        int postiLiberati = prenotazione.getNumeroPosti();
        ristorante.setPosti(ristorante.getPosti() + postiLiberati);
        ristoranteRepository.saveAndFlush(ristorante);
        prenotazioneRepository.deleteById(id);

        contoService.deleteContoByPrenotazione(prenotazione);
    }
}
