package com.hospital.prioridadehospitalar.model;

import jakarta.persistence.*;
import lombok.Data; // Do Lombok, gera getters, setters, toString, equals e hashCode
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private int idade;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(length = 500)
    private String sintomas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridade prioridade;

    // Sinais vitais
    private Double pressaoSistolica;
    private Double pressaoDiastolica;
    private Double batimentosCardiacos;
    private Double temperatura;

    @Column(nullable = false)
    private LocalDateTime dataHoraTriagem;

    public Paciente(String nome, int idade, String cpf, String sintomas, Prioridade prioridade,
                    Double pressaoSistolica, Double pressaoDiastolica, Double batimentosCardiacos, Double temperatura) {
        this.nome = nome;
        this.idade = idade;
        this.cpf = cpf;
        this.sintomas = sintomas;
        this.prioridade = prioridade;
        this.pressaoSistolica = pressaoSistolica;
        this.pressaoDiastolica = pressaoDiastolica;
        this.batimentosCardiacos = batimentosCardiacos;
        this.temperatura = temperatura;
        this.dataHoraTriagem = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (dataHoraTriagem == null) {
            dataHoraTriagem = LocalDateTime.now();
        }
    }
}