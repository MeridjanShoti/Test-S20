package it.epicode.Test_S20.eventi;

import it.epicode.Test_S20.auth.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Eventi")

public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private  Long id;
    @Column(name = "titolo" , nullable = false, unique = true, length = 20)
    private String titolo;
    @Column(name = "descrizione", length = 100)
    private String descrizione;
    @Column(name = "luogo", length = 20)
    private String luogo;
    private LocalDate data;
    private int numeroPostiDisponibili;
    @ManyToOne
    @JoinColumn(name = "organizzatore_id")
    private AppUser organizzatore;
    @ManyToMany
    @JoinTable(
            name = "partecipazioni",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "utente_id")
    )
    private Set<AppUser> partecipanti;


}