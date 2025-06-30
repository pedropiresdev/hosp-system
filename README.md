-----

# Sistema de Gerenciamento de Prioridade Hospitalar

Este projeto é um **Sistema de Gerenciamento de Prioridade Hospitalar** desenvolvido em **Java com Spring Boot** para o backend e **React** para o frontend. Seu objetivo principal é automatizar a organização da fila de atendimento médico com base na prioridade definida durante a triagem, garantindo que casos de maior urgência sejam atendidos primeiro.

-----

## Funcionalidades

O sistema oferece as seguintes funcionalidades principais:

  * **Triagem por Enfermeiros:**
      * Cadastro inicial do paciente (nome, idade, CPF, sintomas).
      * Classificação de prioridade: "Não Urgência", "Pouca Urgência", "Muita Urgência", "Emergência".
      * Registro de sinais vitais (pressão, batimentos, temperatura).
  * **Fila de Atendimento Inteligente:**
      * Ordenação automática dos pacientes por nível de prioridade (da mais alta para a mais baixa). Em caso de empate na prioridade, a ordem é definida pela data e hora da triagem (o mais antigo primeiro).
      * Exibição de tempo estimado de espera (simplificado, baseado na complexidade dos casos e médicos disponíveis).
  * **CRUD Completo (Backend e Frontend):**
      * Operações de **C**adastrar, **L**er (Listar), **A**tualizar e **R**emover pacientes.
      * Persistência dos dados utilizando **Hibernate** (JPA) e um banco de dados relacional.
  * **Painel de Controle:**
      * Visualização em tempo real da fila de espera (via interface web).
      * Alertas para casos de prioridade máxima.
  * **Interface Web (React):** Uma interface amigável para gerenciamento e visualização dos pacientes.
  * **Gerenciamento de Erros:** Tratamento básico de erros na comunicação e nas operações.

-----

## Tecnologias Utilizadas

### Backend (Spring Boot - Java)

  * **Java 17+**: Linguagem de programação.
  * **Spring Boot 3.x**: Framework para desenvolvimento rápido de aplicações Java.
  * **Spring Data JPA**: Simplifica o acesso a dados, construído sobre JPA.
  * **Hibernate**: Implementação da JPA para mapeamento Objeto-Relacional (ORM).
  * **Lombok**: Biblioteca para reduzir o código boilerplate (getters, setters, construtores).
  * **H2 Database (Desenvolvimento)**: Banco de dados em memória, ideal para desenvolvimento e testes. Pode ser facilmente configurado para PostgreSQL, MySQL, etc. em produção.
  * **Spring Web**: Para criar endpoints RESTful.
  * **Jackson**: Para serialização/desserialização de JSON.

### Frontend (React)

  * **React 18+**: Biblioteca JavaScript para construção de interfaces de usuário.
  * **HTML5, CSS3, JavaScript (ES6+)**: Padrões da web.
  * **Create React App**: Ferramenta para configurar um projeto React rapidamente.
  * **Fetch API**: Para fazer requisições HTTP para o backend.

-----

## Como Rodar o Projeto

Para colocar o sistema em funcionamento, você precisará configurar e rodar tanto o backend Spring Boot quanto o frontend React.

### 1\. Pré-requisitos

  * **Java Development Kit (JDK) 17 ou superior**
  * **Maven** (ferramenta de build do Java)
  * **Node.js e npm (ou Yarn)** (para o frontend React)
  * Uma IDE como IntelliJ IDEA, Eclipse ou VS Code.

### 2\. Configuração do Backend (Spring Boot)

1.  **Navegue até a Pasta do Backend:**

    ```bash
    cd prioridade-hospitalar
    ```

2.  **Verifique as Dependências:** Abra o arquivo `pom.xml` e certifique-se de que todas as dependências estão corretas.

    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>3.2.5</version> <relativePath/> </parent>
        <groupId>com.hospital</groupId>
        <artifactId>prioridade-hospitalar</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <name>prioridade-hospitalar</name>
        <description>Sistema de Gerenciamento de Prioridade Hospitalar</description>

        <properties>
            <java.version>17</java.version>
        </properties>

        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-jpa</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <dependency>
                <groupId>com.h2database</groupId>
                <artifactId>h2</artifactId>
                <scope>runtime</scope>
            </dependency>
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <optional>true</optional>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
            </dependency>
        </dependencies>

        <build>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <configuration>
                        <excludes>
                            <exclude>
                                <groupId>org.projectlombok</groupId>
                                <artifactId>lombok</artifactId>
                            </exclude>
                        </excludes>
                    </configuration>
                </plugin>
            </plugins>
        </build>
    </project>
    ```

3.  **Configuração do Banco de Dados:** Edite o arquivo `src/main/resources/application.properties`.

    ```properties
    # prioridade-hospitalar/src/main/resources/application.properties
    spring.h2.console.enabled=true
    spring.h2.console.path=/h2-console
    spring.datasource.url=jdbc:h2:mem:hospitaldb
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=

    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    ```

4.  **Model Paciente:**

    ```java
    // prioridade-hospitalar/src/main/java/com/hospital/prioridadehospitalar/model/Paciente.java
    package com.hospital.prioridadehospitalar.model;

    import jakarta.persistence.*;
    import lombok.Data;
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
    ```

5.  **Enum `Prioridade` (com anotações Jackson para JSON):**

    ```java
    // prioridade-hospitalar/src/main/java/com/hospital/prioridadehospitalar/model/Prioridade.java
    package com.hospital.prioridadehospitalar.model;

    import com.fasterxml.jackson.annotation.JsonCreator;
    import com.fasterxml.jackson.annotation.JsonValue;

    public enum Prioridade {
        EMERGENCIA(4, "Emergência"),
        MUITA_URGENCIA(3, "Muita Urgência"),
        POUCA_URGENCIA(2, "Pouca Urgência"),
        NAO_URGENCIA(1, "Não Urgência");

        private final int nivel;
        private final String descricao;

        Prioridade(int nivel, String descricao) {
            this.nivel = nivel;
            this.descricao = descricao;
        }

        public int getNivel() {
            return nivel;
        }

        @JsonValue
        public String getDescricao() {
            return descricao;
        }

        @JsonCreator
        public static Prioridade fromDescricao(String text) {
            if (text == null) {
                throw new IllegalArgumentException("Descrição de prioridade não pode ser nula.");
            }
            for (Prioridade p : Prioridade.values()) {
                if (p.descricao.equalsIgnoreCase(text.trim())) {
                    return p;
                }
            }
            throw new IllegalArgumentException("Prioridade inválida: '" + text + "' Não é uma das descrições aceitas: " +
                    "['Emergência', 'Muita Urgência', 'Pouca Urgência', 'Não Urgência']");
        }

        public static Prioridade fromString(String text) {
             return fromDescricao(text);
        }
    }
    ```

6.  **Repository Paciente:**

    ```java
    // prioridade-hospitalar/src/main/java/com/hospital/prioridadehospitalar/repository/PacienteRepository.java
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
    ```

7.  **Service Paciente:**

    ```java
    // prioridade-hospitalar/src/main/java/com/hospital/prioridadehospitalar/service/PacienteService.java
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
            if (pacienteRepository.findByCpf(paciente.getCpf()).isPresent()) {
                throw new IllegalArgumentException("Já existe um paciente cadastrado com este CPF.");
            }
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
            Optional<Paciente> pacienteComMesmoCpf = pacienteRepository.findByCpf(paciente.getCpf());
            if (pacienteComMesmoCpf.isPresent() && !pacienteComMesmoCpf.get().getId().equals(paciente.getId())) {
                throw new IllegalArgumentException("Outro paciente já está cadastrado com este CPF.");
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
    ```

8.  **Controller REST Paciente:**

    ```java
    // prioridade-hospitalar/src/main/java/com/hospital/prioridadehospitalar/controller/PacienteController.java
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
    @CrossOrigin(origins = "http://localhost:3000") // Permite requisições do seu frontend React (porta 3000)
    public class PacienteController {

        @Autowired
        private PacienteService pacienteService;

        @PostMapping
        public ResponseEntity<Paciente> cadastrarPaciente(@RequestBody Paciente paciente) {
            try {
                Paciente novoPaciente = pacienteService.cadastrarPaciente(paciente);
                return new ResponseEntity<>(novoPaciente, HttpStatus.CREATED);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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
    ```

9.  **Classe Principal da Aplicação:** O `CommandLineRunner` é opcional agora que você tem uma interface web. Você pode removê-lo ou comentá-lo.

    ```java
    // prioridade-hospitalar/src/main/java/com/hospital/prioridadehospitalar/PrioridadeHospitalarApplication.java
    package com.hospital.prioridadehospitalar;

    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    // import org.springframework.boot.CommandLineRunner; // Remova ou comente se não quiser mais o console
    // import org.springframework.context.annotation.Bean; // Remova ou comente

    @SpringBootApplication
    public class PrioridadeHospitalarApplication {

        public static void main(String[] args) {
            SpringApplication.run(PrioridadeHospitalarApplication.class, args);
        }

        // Se você não quiser mais o menu de console, pode comentar ou remover este bloco:
        /*
        @Autowired
        private PacienteService pacienteService;

        @Bean
        public CommandLineRunner run() {
            return args -> {
                // Seu código do menu de console aqui
            };
        }
        */
    }
    ```

10. **Compilar e Rodar:**

      * Abra um terminal na raiz do projeto backend (`prioridade-hospitalar`).
      * Execute o comando Maven para construir o projeto:
        ```bash
        mvn clean install
        ```
      * Execute a aplicação Spring Boot:
        ```bash
        mvn spring-boot:run
        ```
      * O backend estará rodando na porta `8080` (por padrão). Você pode acessar o console do H2 em `http://localhost:8080/h2-console` usando a URL JDBC `jdbc:h2:mem:hospitaldb` para verificar o banco de dados.

### 3\. Configuração do Frontend (React)

1.  **Navegue até a Pasta do Frontend:**

    ```bash
    cd hospital-frontend
    ```

2.  **Instale as Dependências:**

    ```bash
    npm install
    # OU
    yarn install
    ```

3.  **Serviço de API:**

    ```javascript
    // hospital-frontend/src/services/pacienteService.js
    const API_BASE_URL = 'http://localhost:8080/api/pacientes'; // URL do seu backend Spring Boot

    const pacienteService = {
      createPaciente: async (pacienteData) => {
        try {
          const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(pacienteData),
          });
          if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Erro ao cadastrar paciente.');
          }
          return await response.json();
        } catch (error) {
          console.error('Erro na requisição de cadastro:', error);
          throw error;
        }
      },

      getAllPacientes: async () => {
        try {
          const response = await fetch(API_BASE_URL);
          if (!response.ok) {
            throw new Error('Erro ao buscar pacientes.');
          }
          return await response.json();
        } catch (error) {
          console.error('Erro na requisição de listagem:', error);
          throw error;
        }
      },

      getPacienteByCpf: async (cpf) => {
        try {
          const response = await fetch(`${API_BASE_URL}/cpf/${cpf}`);
          if (!response.ok) {
            if (response.status === 404) {
              return null;
            }
            throw new Error('Erro ao buscar paciente por CPF.');
          }
          return await response.json();
        } catch (error) {
          console.error('Erro na requisição de busca por CPF:', error);
          throw error;
        }
      },

      updatePaciente: async (id, pacienteData) => {
        try {
          const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(pacienteData),
          });
          if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Erro ao atualizar paciente.');
          }
          return await response.json();
        } catch (error) {
          console.error('Erro na requisição de atualização:', error);
          throw error;
        }
      },

      deletePacienteByCpf: async (cpf) => {
        try {
          const response = await fetch(`${API_BASE_URL}/cpf/${cpf}`, {
            method: 'DELETE',
          });
          if (!response.ok) {
            if (response.status === 404) {
                throw new Error('Paciente não encontrado para exclusão.');
            }
            throw new Error('Erro ao remover paciente.');
          }
        } catch (error) {
          console.error('Erro na requisição de exclusão:', error);
          throw error;
        }
      },
    };

    export default pacienteService;
    ```

4.  **Componente de Formulário:**

    ```javascript
    // hospital-frontend/src/components/PacienteForm.js
    import React, { useState, useEffect } from 'react';
    import './PacienteForm.css';

    const PrioridadeOptions = [
      "Não Urgência", "Pouca Urgência", "Muita Urgência", "Emergência"
    ];

    function PacienteForm({ paciente, onSubmit, onCancel }) {
      const [formData, setFormData] = useState({
        id: paciente ? paciente.id : null,
        nome: paciente ? paciente.nome : '',
        idade: paciente ? paciente.idade : '',
        cpf: paciente ? paciente.cpf : '',
        sintomas: paciente ? paciente.sintomas : '',
        prioridade: paciente ? paciente.prioridade : PrioridadeOptions[0],
        pressaoSistolica: paciente ? paciente.pressaoSistolica : '',
        pressaoDiastolica: paciente ? paciente.pressaoDiastolica : '',
        batimentosCardiacos: paciente ? paciente.batimentosCardiacos : '',
        temperatura: paciente ? paciente.temperatura : '',
      });

      useEffect(() => {
        if (paciente) {
          setFormData({
            id: paciente.id,
            nome: paciente.nome,
            idade: paciente.idade,
            cpf: paciente.cpf,
            sintomas: paciente.sintomas,
            prioridade: paciente.prioridade,
            pressaoSistolica: paciente.pressaoSistolica,
            pressaoDiastolica: paciente.pressaoDiastolica,
            batimentosCardiacos: paciente.batimentosCardiacos,
            temperatura: paciente.temperatura,
          });
        } else {
          setFormData({
            id: null, nome: '', idade: '', cpf: '', sintomas: '', prioridade: PrioridadeOptions[0],
            pressaoSistolica: '', pressaoDiastolica: '', batimentosCardiacos: '', temperatura: ''
          });
        }
      }, [paciente]);

      const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
          ...formData,
          [name]: value,
        });
      };

      const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(formData);
      };

      return (
        <form onSubmit={handleSubmit} className="paciente-form">
          <h2>{paciente ? 'Editar Paciente' : 'Cadastrar Paciente'}</h2>
          <div className="form-group">
            <label htmlFor="nome">Nome:</label>
            <input type="text" id="nome" name="nome" value={formData.nome} onChange={handleChange} required />
          </div>
          <div className="form-group">
            <label htmlFor="idade">Idade:</label>
            <input type="number" id="idade" name="idade" value={formData.idade} onChange={handleChange} required />
          </div>
          <div className="form-group">
            <label htmlFor="cpf">CPF:</label>
            <input type="text" id="cpf" name="cpf" value={formData.cpf} onChange={handleChange} required />
          </div>
          <div className="form-group">
            <label htmlFor="sintomas">Sintomas:</label>
            <textarea id="sintomas" name="sintomas" value={formData.sintomas} onChange={handleChange}></textarea>
          </div>
          <div className="form-group">
            <label htmlFor="prioridade">Prioridade:</label>
            <select id="prioridade" name="prioridade" value={formData.prioridade} onChange={handleChange} required>
              {PrioridadeOptions.map(option => (
                <option key={option} value={option}>{option}</option>
              ))}
            </select>
          </div>

          <h3>Sinais Vitais</h3>
          <div className="form-group">
            <label htmlFor="pressaoSistolica">Pressão Sistólica:</label>
            <input type="number" step="0.1" id="pressaoSistolica" name="pressaoSistolica" value={formData.pressaoSistolica} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label htmlFor="pressaoDiastolica">Pressão Diastólica:</label>
            <input type="number" step="0.1" id="pressaoDiastolica" name="pressaoDiastolica" value={formData.pressaoDiastolica} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label htmlFor="batimentosCardiacos">Batimentos Cardíacos:</label>
            <input type="number" step="0.1" id="batimentosCardiacos" name="batimentosCardiacos" value={formData.batimentosCardiacos} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label htmlFor="temperatura">Temperatura:</label>
            <input type="number" step="0.1" id="temperatura" name="temperatura" value={formData.temperatura} onChange={handleChange} />
          </div>

          <div className="form-actions">
            <button type="submit">{paciente ? 'Atualizar' : 'Cadastrar'}</button>
            {onCancel && <button type="button" onClick={onCancel} className="cancel-button">Cancelar</button>}
          </div>
        </form>
      );
    }

    export default PacienteForm;
    ```

    ```css
    /* hospital-frontend/src/components/PacienteForm.css */
    .paciente-form {
      background-color: #f9f9f9;
      padding: 25px;
      border-radius: 8px;
      box-shadow: 0 2px 5px rgba(0,0,0,0.05);
      margin-bottom: 30px;
    }

    .paciente-form .form-group {
      margin-bottom: 15px;
    }

    .paciente-form label {
      display: block;
      margin-bottom: 5px;
      font-weight: bold;
      color: #555;
    }

    .paciente-form input[type="text"],
    .paciente-form input[type="number"],
    .paciente-form textarea,
    .paciente-form select {
      width: calc(100% - 22px);
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 4px;
      font-size: 1em;
    }

    .paciente-form textarea {
      resize: vertical;
      min-height: 80px;
    }

    .paciente-form .form-actions {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
      margin-top: 20px;
    }

    .paciente-form button {
      padding: 10px 20px;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      font-size: 1em;
      transition: background-color 0.3s ease;
    }

    .paciente-form button[type="submit"] {
      background-color: #28a745;
      color: white;
    }

    .paciente-form button[type="submit"]:hover {
      background-color: #218838;
    }

    .paciente-form .cancel-button {
      background-color: #6c757d;
      color: white;
    }

    .paciente-form .cancel-button:hover {
      background-color: #5a6268;
    }
    ```

5.  **Componente de Lista:**

    ```javascript
    // hospital-frontend/src/components/PacienteList.js
    import React, { useState } from 'react';
    import './PacienteList.css';

    function PacienteList({ pacientes, onEdit, onDelete }) {
      const [searchTerm, setSearchTerm] = useState('');

      const filteredPacientes = pacientes.filter(paciente =>
        paciente.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
        paciente.cpf.includes(searchTerm)
      );

      return (
        <div className="paciente-list-container">
          <h2>Fila de Atendimento</h2>
          <div className="search-bar">
            <input
              type="text"
              placeholder="Buscar por nome ou CPF..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          {filteredPacientes.length === 0 ? (
            <p>Nenhum paciente na fila de atendimento.</p>
          ) : (
            <table className="paciente-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Nome</th>
                  <th>Idade</th>
                  <th>CPF</th>
                  <th>Prioridade</th>
                  <th>Sintomas</th>
                  <th>Sinais Vitais</th>
                  <th>Triagem</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {filteredPacientes.map((paciente) => (
                  <tr key={paciente.id}>
                    <td>{paciente.id}</td>
                    <td>{paciente.nome}</td>
                    <td>{paciente.idade}</td>
                    <td>{paciente.cpf}</td>
                    <td>{paciente.prioridade}</td>
                    <td>{paciente.sintomas}</td>
                    <td>
                      PS: {paciente.pressaoSistolica || 'N/A'}, PD: {paciente.pressaoDiastolica || 'N/A'}<br/>
                      BPM: {paciente.batimentosCardiacos || 'N/A'}, Temp: {paciente.temperatura || 'N/A'}
                    </td>
                    <td>{new Date(paciente.dataHoraTriagem).toLocaleString()}</td>
                    <td className="actions-column">
                      <button onClick={() => onEdit(paciente)}>Editar</button>
                      <button onClick={() => onDelete(paciente.cpf)} className="delete-button">Remover</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      );
    }

    export default PacienteList;
    ```

    ```css
    /* hospital-frontend/src/components/PacienteList.css */
    .paciente-list-container {
      overflow-x: auto;
    }

    .search-bar {
      margin-bottom: 20px;
      text-align: center;
    }

    .search-bar input {
      width: 50%;
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 5px;
      font-size: 1em;
    }

    .paciente-table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 20px;
    }

    .paciente-table th,
    .paciente-table td {
      border: 1px solid #ddd;
      padding: 12px 15px;
      text-align: left;
    }

    .paciente-table th {
      background-color: #e9ecef;
      font-weight: bold;
      color: #495057;
    }

    .paciente-table tr:nth-child(even) {
      background-color: #f8f9fa;
    }

    .paciente-table tr:hover {
      background-color: #e2f2ff;
    }

    .paciente-table .actions-column button {
      background-color: #ffc107;
      color: #333;
      border: none;
      padding: 6px 10px;
      border-radius: 4px;
      cursor: pointer;
      font-size: 0.85em;
      margin-right: 5px;
      transition: background-color 0.3s ease;
    }

    .paciente-table .actions-column button:hover {
      background-color: #e0a800;
    }

    .paciente-table .actions-column .delete-button {
      background-color: #dc3545;
      color: white;
    }

    .paciente-table .actions-column .delete-button:hover {
      background-color: #c82333;
    }
    ```

6.  **Componente Principal da Aplicação React:**

    ```javascript
    // hospital-frontend/src/App.js
    import React, { useState, useEffect } from 'react';
    import PacienteForm from './components/PacienteForm';
    import PacienteList from './components/PacienteList';
    import pacienteService from './services/pacienteService';
    import './App.css';

    function App() {
      const [pacientes, setPacientes] = useState([]);
      const [editingPaciente, setEditingPaciente] = useState(null);
      const [showForm, setShowForm] = useState(false);
      const [message, setMessage] = useState('');

      const fetchPacientes = async () => {
        try {
          const data = await pacienteService.getAllPacientes();
          setPacientes(data);
        } catch (error) {
          setMessage(`Erro ao carregar pacientes: ${error.message}`);
          console.error('Erro ao carregar pacientes:', error);
        }
      };

      useEffect(() => {
        fetchPacientes();
      }, []);

      const handleCreateOrUpdatePaciente = async (pacienteData) => {
        try {
          if (pacienteData.id) {
            await pacienteService.updatePaciente(pacienteData.id, pacienteData);
            setMessage('Paciente atualizado com sucesso!');
          } else {
            await pacienteService.createPaciente(pacienteData);
            setMessage('Paciente cadastrado com sucesso!');
          }
          setShowForm(false);
          setEditingPaciente(null);
          fetchPacientes();
        } catch (error) {
          setMessage(`Erro: ${error.message}`);
          console.error('Erro ao salvar paciente:', error);
        }
      };

      const handleEdit = (paciente) => {
        setEditingPaciente(paciente);
        setShowForm(true);
      };

      const handleDelete = async (cpf) => {
        if (window.confirm(`Tem certeza que deseja remover o paciente com CPF: ${cpf}?`)) {
          try {
            await pacienteService.deletePacienteByCpf(cpf);
            setMessage('Paciente removido com sucesso!');
            fetchPacientes();
          } catch (error) {
            setMessage(`Erro ao remover paciente: ${error.message}`);
            console.error('Erro ao remover paciente:', error);
          }
        }
      };

      return (
        <div className="App">
          <header className="App-header">
            <h1>Sistema de Gerenciamento de Prioridade Hospitalar</h1>
          </header>
          <main>
            {message && <p className="message">{message}</p>}

            <div className="toolbar">
              <button onClick={() => { setShowForm(true); setEditingPaciente(null); }}>
                + Cadastrar Novo Paciente
              </button>
            </div>

            {showForm && (
              <div className="form-section">
                <PacienteForm
                  paciente={editingPaciente}
                  onSubmit={handleCreateOrUpdatePaciente}
                  onCancel={() => { setShowForm(false); setEditingPaciente(null); }}
                />
              </div>
            )}

            <div className="list-section">
              <PacienteList
                pacientes={pacientes}
                onEdit={handleEdit}
                onDelete={handleDelete}
              />
            </div>
          </main>
        </div>
      );
    }

    export default App;
    ```

    ```css
    /* hospital-frontend/src/App.css */
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      margin: 0;
      padding: 0;
      background-color: #f4f7f6;
      color: #333;
    }

    .App {
      max-width: 1200px;
      margin: 20px auto;
      background-color: #ffffff;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      overflow: hidden;
    }

    .App-header {
      background-color: #28a745;
      color: white;
      padding: 20px;
      text-align: center;
      border-bottom: 5px solid #218838;
    }

    h1, h2 {
      color: #333;
      margin-top: 20px;
      margin-bottom: 15px;
    }

    .toolbar {
      padding: 20px;
      text-align: right;
      border-bottom: 1px solid #eee;
    }

    .toolbar button {
      background-color: #007bff;
      color: white;
      border: none;
      padding: 10px 20px;
      border-radius: 5px;
      cursor: pointer;
      font-size: 1em;
      transition: background-color 0.3s ease;
    }

    .toolbar button:hover {
      background-color: #0056b3;
    }

    .message {
      padding: 10px 20px;
      margin: 10px 20px;
      border-radius: 5px;
      background-color: #d4edda;
      color: #155724;
      border: 1px solid #c3e6cb;
      text-align: center;
    }

    .message.error {
      background-color: #f8d7da;
      color: #721c24;
      border: 1px solid #f5c6cb;
    }

    .form-section, .list-section {
      padding: 20px;
    }
    ```

7.  **Ponto de Entrada do React:**

    ```javascript
    // hospital-frontend/src/index.js
    import React from 'react';
    import ReactDOM from 'react-dom/client';
    import './index.css';
    import App from './App';
    import reportWebVitals from './reportWebVitals';

    const root = ReactDOM.createRoot(document.getElementById('root'));
    root.render(
      <React.StrictMode>
        <App />
      </React.StrictMode>
    );

    reportWebVitals();
    ```

    ```css
    /* hospital-frontend/src/index.css */
    body {
      margin: 0;
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
        'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue',
        sans-serif;
      -webkit-font-smoothing: antialiased;
      -moz-osx-rendering: grayscale;
    }

    code {
      font-family: source-code-pro, Menlo, Monaco, Consolas, 'Courier New',
        monospace;
    }
    ```

8.  **Inicie a Aplicação React:**

    ```bash
    npm start
    # OU
    yarn start
    ```

      * O frontend será aberto automaticamente no seu navegador, geralmente em `http://localhost:3000`.

-----

## Utilização

Com o backend e o frontend em execução:

  * Acesse `http://localhost:3000` no seu navegador.
  * Use o botão **"Cadastrar Novo Paciente"** para abrir o formulário de triagem e cadastro.
  * Após cadastrar, os pacientes aparecerão na **"Fila de Atendimento"**, ordenados automaticamente por prioridade.
  * Use os botões **"Editar"** e **"Remover"** para gerenciar os cadastros existentes.
  * A barra de busca permite filtrar pacientes por nome ou CPF.

-----

## Próximos Passos e Melhorias

Este projeto serve como uma base sólida para um sistema de gerenciamento hospitalar. Possíveis melhorias futuras incluem:

  * **Autenticação e Autorização:** Implementar Spring Security no backend e integração no frontend para diferentes perfis de usuário (enfermeiro, médico, administrador).
  * **Otimização do Tempo de Espera:** Desenvolver um algoritmo mais sofisticado para estimar o tempo de espera, considerando múltiplos fatores (especialidade, recursos, complexidade do caso).
  * **Painel de Controle Gráfico:** Criar um painel visual mais rico com gráficos e atualizações em tempo real (utilizando WebSockets, por exemplo).
  * **Notificações:** Sistema de notificação para médicos sobre novos pacientes de alta prioridade.
  * **Relatórios Avançados:** Gerar relatórios detalhados sobre o fluxo de pacientes, tempo médio de atendimento, etc.
  * **Testes Abrangentes:** Adicionar testes unitários, de integração e end-to-end para garantir a robustez do sistema.
  * **Containerização:** Empacotar a aplicação em Docker para facilitar o deployment.

-----
