package com.hospital.prioridadehospitalar.repository;

import com.hospital.prioridadehospitalar.model.Paciente;
import com.hospital.prioridadehospitalar.model.Prioridade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByCpf(String cpf);

    List<Paciente> findAllByOrderByPrioridadeDescDataHoraTriagemAsc();
}