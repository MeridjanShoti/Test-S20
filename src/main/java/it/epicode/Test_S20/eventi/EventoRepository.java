package it.epicode.Test_S20.eventi;


import it.epicode.Test_S20.auth.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    boolean existsByTitolo(String titolo);
    List<Evento> findByPartecipanti(AppUser utenteLoggato);
}