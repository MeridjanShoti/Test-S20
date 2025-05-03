package it.epicode.Test_S20.eventi;

import it.epicode.Test_S20.auth.AppUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class EventoService {
    @Autowired
    EventoRepository eventoRepository;

    Evento saveEvento(EventoRequest request, AppUser utenteLoggato) {
        if (utenteLoggato.getRoles().contains("ROLE_ORGANIZZATORE_EVENTI")) {
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

        if (!utenteLoggato.getRoles().contains("ROLE_ORGANIZZATORE_EVENTI")&& !utenteLoggato.equals(evento.getOrganizzatore())) {
            throw new IllegalArgumentException("Solo gli organizzatori possono eliminare eventi");
        }
        eventoRepository.deleteById(id);
    }
    public Evento updateEvento(Long id, EventoRequest request, AppUser utenteLoggato) {
        Evento existingEvento = eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con ID: " + id));
        if (!utenteLoggato.getRoles().contains("ROLE_ORGANIZZATORE_EVENTI") && !utenteLoggato.equals(existingEvento.getOrganizzatore())) {
            throw new IllegalArgumentException("Solo gli organizzatori possono modificare eventi");
        }
        Evento evento = new Evento();
        BeanUtils.copyProperties(request, evento);
        return eventoRepository.save(evento);
    }
    public Evento getEventoById(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con ID: " + id));
    }
    public List<Evento> getAllEventi() {
        return eventoRepository.findAll();
    }

}
