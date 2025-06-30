package com.hospital.prioridadehospitalar.controller;

import com.hospital.prioridadehospitalar.model.Paciente;
import com.hospital.prioridadehospitalar.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "http://localhost:3000")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @PostMapping
    public ResponseEntity<Paciente> cadastrarPaciente(@RequestBody Paciente paciente) {
        try {
            Paciente novoPaciente = pacienteService.cadastrarPaciente(paciente);
            return new ResponseEntity<>(novoPaciente, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // CPF duplicado ou dados inválidos
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientesOrdenados() {
        List<Paciente> pacientes = pacienteService.listarPacientesOrdenadosPorPrioridade();
        return new ResponseEntity<>(pacientes, HttpStatus.OK);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Paciente> buscarPacientePorCpf(@PathVariable String cpf) {
        Optional<Paciente> paciente = pacienteService.buscarPacientePorCpf(cpf);
        return paciente.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> atualizarPaciente(@PathVariable Long id, @RequestBody Paciente paciente) {
        if (!id.equals(paciente.getId())) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try {
            Paciente pacienteAtualizado = pacienteService.atualizarPaciente(paciente);
            return new ResponseEntity<>(pacienteAtualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // --- Endpoint para Remover Paciente por CPF ---
    // (Pode-se usar ID também, mas o requisito pedia por CPF)
    @DeleteMapping("/cpf/{cpf}")
    public ResponseEntity<HttpStatus> removerPaciente(@PathVariable String cpf) {
        try {
            pacienteService.removerPacientePorCpf(cpf);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/estimativa/{cpf}/{medicosDisponiveis}")
    public ResponseEntity<String> getEstimativaTempoEspera(@PathVariable String cpf, @PathVariable int medicosDisponiveis) {
        Optional<Paciente> pacienteOpt = pacienteService.buscarPacientePorCpf(cpf);
        if (pacienteOpt.isPresent()) {
            String estimativa = pacienteService.estimarTempoEspera(pacienteOpt.get(), medicosDisponiveis);
            return new ResponseEntity<>(estimativa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Paciente não encontrado.", HttpStatus.NOT_FOUND);
        }
    }
}