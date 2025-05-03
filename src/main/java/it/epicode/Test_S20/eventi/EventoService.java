package it.epicode.Test_S20.eventi;

import it.epicode.Test_S20.auth.AppUser;
import it.epicode.Test_S20.auth.Role;
import it.epicode.Test_S20.common.CommonResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@Validated
public class EventoService {
    @Autowired
    EventoRepository eventoRepository;


    Evento saveEvento(EventoRequest request, AppUser utenteLoggato) {
        if (utenteLoggato.getRoles().contains(Role.ROLE_ORGANIZZATORE_EVENTI)) {
            Evento evento = new Evento();
            BeanUtils.copyProperties(request, evento);
            evento.setOrganizzatore(utenteLoggato);
            if (evento.getTitolo() == null || evento.getTitolo().isEmpty()) {
                throw new IllegalArgumentException("Il titolo dell'evento non può essere vuoto");
            }
            if (eventoRepository.existsByTitolo(evento.getTitolo())) {
                throw new IllegalArgumentException("Esiste già un evento con lo stesso titolo");
            }
            return eventoRepository.save(evento);
        } else {
            throw new IllegalArgumentException("Solo gli organizzatori possono creare eventi");
        }
    }

    public void deleteEvento(Long id, AppUser utenteLoggato) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con ID: " + id));

        if (!utenteLoggato.getRoles().contains(Role.ROLE_ORGANIZZATORE_EVENTI) && !utenteLoggato.equals(evento.getOrganizzatore())) {
            throw new IllegalArgumentException("Solo gli organizzatori possono eliminare eventi");
        }
        eventoRepository.deleteById(id);
    }

    public Evento updateEvento(Long id, EventoRequest request, AppUser utenteLoggato) {
        Evento existingEvento = eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con ID: " + id));
        if (!utenteLoggato.getRoles().contains(Role.ROLE_ORGANIZZATORE_EVENTI) && !utenteLoggato.equals(existingEvento.getOrganizzatore())) {
            throw new IllegalArgumentException("Solo gli organizzatori possono modificare eventi");
        }
        existingEvento.setTitolo(request.getTitolo());
        existingEvento.setDescrizione(request.getDescrizione());
        existingEvento.setData(request.getData());
        existingEvento.setNumeroPostiDisponibili(request.getNumeroPostiDisponibili());
        return eventoRepository.save(existingEvento);
    }
    public Evento updateEventoPatch(Long id, EventoRequest request, AppUser utenteLoggato) {
        Evento existingEvento = eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con ID: " + id));
        if (!utenteLoggato.getRoles().contains(Role.ROLE_ORGANIZZATORE_EVENTI) && !utenteLoggato.equals(existingEvento.getOrganizzatore())) {
            throw new IllegalArgumentException("Solo gli organizzatori possono modificare eventi");
        }
        if (request.getTitolo() != null) {
            existingEvento.setTitolo(request.getTitolo());
        }
        if (request.getDescrizione() != null) {
            existingEvento.setDescrizione(request.getDescrizione());
        }
        if(request.getData() != null) {
            existingEvento.setData(request.getData());
        }
        if (request.getNumeroPostiDisponibili() != null){
            existingEvento.setNumeroPostiDisponibili(request.getNumeroPostiDisponibili());
        }
        return eventoRepository.save(existingEvento);
    }

    public Evento getEventoById(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con ID: " + id));
    }

    public List<Evento> getAllEventi() {
        return eventoRepository.findAll();
    }

    public CommonResponse prenotaPartecipazione(Long id, AppUser utenteLoggato) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con ID: " + id));

        if (evento.getPartecipanti().contains(utenteLoggato)) {
            throw new IllegalArgumentException("L'utente ha gia' prenotato questo evento");
        } else if (evento.getPartecipanti().size() >= evento.getNumeroPostiDisponibili() ) {
            throw new IllegalArgumentException("Non ci sono piu' posti disponibili per questo evento");
        } else {
            evento.getPartecipanti().add(utenteLoggato);
            eventoRepository.save(evento);
            return new CommonResponse(evento.getId());
        }
    }
    public Evento annullaPartecipazione(Long id, AppUser utenteLoggato) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con ID: " + id));

        if (!evento.getPartecipanti().contains(utenteLoggato)) {
            throw new IllegalArgumentException("L'utente non ha prenotato questo evento");
        } else {
            evento.getPartecipanti().remove(utenteLoggato);
            return eventoRepository.save(evento);
        }
    }
    public List<Evento> getEventiByUtenteLoggato(AppUser utenteLoggato) {
        List<Evento> eventi = eventoRepository.findByPartecipanti(utenteLoggato);
        if (eventi.isEmpty()) {
            throw new IllegalArgumentException("L'utente non ha prenotato nessun evento");
        } else {
            return eventi;
        }
    }
}