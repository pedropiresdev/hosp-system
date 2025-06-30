package com.hospital.prioridadehospitalar.service;

import com.hospital.prioridadehospitalar.model.Paciente;
import com.hospital.prioridadehospitalar.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Transactional
    public Paciente cadastrarPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public List<Paciente> listarTodosPacientes() {
        return pacienteRepository.findAll();
    }

    public List<Paciente> listarPacientesOrdenadosPorPrioridade() {
        return pacienteRepository.findAllByOrderByPrioridadeDescDataHoraTriagemAsc();
    }

    public Optional<Paciente> buscarPacientePorCpf(String cpf) {
        return pacienteRepository.findByCpf(cpf);
    }

    @Transactional
    public Paciente atualizarPaciente(Paciente paciente) {
        if (paciente.getId() == null || !pacienteRepository.existsById(paciente.getId())) {
            throw new IllegalArgumentException("Paciente não encontrado para atualização.");
        }
        return pacienteRepository.save(paciente);
    }

    @Transactional
    public void removerPacientePorCpf(String cpf) {
        Optional<Paciente> pacienteOpt = pacienteRepository.findByCpf(cpf);
        if (pacienteOpt.isPresent()) {
            pacienteRepository.delete(pacienteOpt.get());
        } else {
            throw new IllegalArgumentException("Paciente com CPF " + cpf + " não encontrado.");
        }
    }


    public String estimarTempoEspera(Paciente paciente, int medicosDisponiveis) {
        List<Paciente> filaOrdenada = listarPacientesOrdenadosPorPrioridade();
        int posicaoNaFila = -1;

        for (int i = 0; i < filaOrdenada.size(); i++) {
            if (filaOrdenada.get(i).getCpf().equals(paciente.getCpf())) {
                posicaoNaFila = i;
                break;
            }
        }

        if (posicaoNaFila == -1) {
            return "Paciente não está na fila de espera (ou já foi atendido/não cadastrado).";
        }

        double tempoPorPaciente = 15.0; // 15 minutos por paciente
        double tempoTotal = 0;

        for (int i = 0; i < posicaoNaFila; i++) {
            Paciente pacienteAFrente = filaOrdenada.get(i);
            tempoTotal += tempoPorPaciente * pacienteAFrente.getPrioridade().getNivel();
        }

        if (medicosDisponiveis > 0) {
            tempoTotal /= medicosDisponiveis;
        } else {
            return "Nenhum médico disponível, tempo de espera indefinido.";
        }

        long horas = (long) (tempoTotal / 60);
        long minutos = (long) (tempoTotal % 60);

        return String.format("Tempo estimado de espera para %s: %d horas e %d minutos.", paciente.getNome(), horas, minutos);
    }
}