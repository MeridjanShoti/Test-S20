package it.epicode.Test_S20.eventi;

import it.epicode.Test_S20.auth.AppUser;
import it.epicode.Test_S20.common.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/eventi")
public class EventoController {
    @Autowired
    private EventoService eventoService;
    @GetMapping("")
    public List<Evento> getAllEventi() {
        return eventoService.getAllEventi();
    }
    @GetMapping("/{id}")
    public Evento getEventoById(@PathVariable Long id) {
        return eventoService.getEventoById(id);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public List<Evento> getEventiByUtenteLoggato(@AuthenticationPrincipal AppUser utenteLoggato) {
        return eventoService.getEventiByUtenteLoggato(utenteLoggato);
    }
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/prenota/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse prenotaPartecipazione(@AuthenticationPrincipal AppUser utenteLoggato, @PathVariable Long id) {
        return eventoService.prenotaPartecipazione(id, utenteLoggato);
    }
    @PreAuthorize("hasRole('ROLE_ORGANIZZATORE_EVENTI')")
    @GetMapping("/annulla/{id}")
    public Evento annullaPartecipazione(@AuthenticationPrincipal AppUser utenteLoggato, @PathVariable Long id) {
        return eventoService.annullaPartecipazione(id, utenteLoggato);
    }
    @PreAuthorize("hasRole('ROLE_ORGANIZZATORE_EVENTI')")
    @PostMapping("/eventi")
    @ResponseStatus(HttpStatus.CREATED)
    public Evento createEvento(@RequestBody EventoRequest request , @AuthenticationPrincipal AppUser utenteLoggato) {
        return eventoService.saveEvento(request, utenteLoggato);
    }
    @PreAuthorize("hasRole('ROLE_ORGANIZZATORE_EVENTI')")
    @PutMapping("/eventi/{id}")

    public Evento updateEvento(@PathVariable Long id, @RequestBody EventoRequest request, @AuthenticationPrincipal AppUser utenteLoggato) {
        return eventoService.updateEvento(id, request, utenteLoggato);
    }
    @PreAuthorize("hasRole('ROLE_ORGANIZZATORE_EVENTI')")
    @DeleteMapping("/eventi/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvento(@PathVariable Long id, @AuthenticationPrincipal AppUser utenteLoggato) {
        eventoService.deleteEvento(id, utenteLoggato);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/eventi/{id}")
    public Evento getEventoById(@PathVariable Long id, @AuthenticationPrincipal AppUser utenteLoggato) {
        return eventoService.getEventoById(id);
    }

}
