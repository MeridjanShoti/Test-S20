package it.epicode.Test_S20.eventi;


import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    boolean existsByTitolo(String titolo);
}