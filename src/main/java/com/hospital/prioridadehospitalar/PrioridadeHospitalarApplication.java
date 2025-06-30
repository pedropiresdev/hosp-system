package com.hospital.prioridadehospitalar;

import com.hospital.prioridadehospitalar.model.Paciente;
import com.hospital.prioridadehospitalar.model.Prioridade;
import com.hospital.prioridadehospitalar.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class PrioridadeHospitalarApplication {

    @Autowired
    private PacienteService pacienteService;

    public static void main(String[] args) {
        SpringApplication.run(PrioridadeHospitalarApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            Scanner scanner = new Scanner(System.in);
            int opcao;

            System.out.println("--- Sistema de Gerenciamento de Prioridade Hospitalar ---");

            do {
                exibirMenu();
                try {
                    opcao = scanner.nextInt();
                    scanner.nextLine(); // Consumir a quebra de linha

                    switch (opcao) {
                        case 1:
                            cadastrarPaciente(scanner);
                            break;
                        case 2:
                            listarPacientes();
                            break;
                        case 3:
                            editarPaciente(scanner);
                            break;
                        case 4:
                            removerPaciente(scanner);
                            break;
                        case 5:
                            System.out.println("Saindo do sistema. Obrigado!");
                            break;
                        default:
                            System.out.println("Opção inválida. Tente novamente.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Por favor, digite um número.");
                    scanner.nextLine(); // Limpa o buffer de entrada
                    opcao = 0; // Para continuar o loop
                } catch (IllegalArgumentException e) {
                    System.out.println("Erro: " + e.getMessage());
                    opcao = 0;
                } catch (Exception e) {
                    System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
                    e.printStackTrace();
                    opcao = 0;
                }
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine(); // Espera o usuário pressionar Enter
            } while (opcao != 5);

            scanner.close();
        };
    }

    private void exibirMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. Cadastrar paciente");
        System.out.println("2. Listar pacientes (Fila de Atendimento)");
        System.out.println("3. Editar paciente");
        System.out.println("4. Remover paciente");
        System.out.println("5. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private void cadastrarPaciente(Scanner scanner) {
        System.out.println("\n--- Cadastro de Paciente ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Idade: ");
        int idade = scanner.nextInt();
        scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        Optional<Paciente> pacienteExistente = pacienteService.buscarPacientePorCpf(cpf);
        if (pacienteExistente.isPresent()) {
            System.out.println("Erro: Paciente com este CPF já cadastrado.");
            return;
        }

        System.out.print("Sintomas: ");
        String sintomas = scanner.nextLine();

        Prioridade prioridade = null;
        boolean prioridadeValida = false;
        while (!prioridadeValida) {
            System.out.print("Classificação de Prioridade (Não Urgência, Pouca Urgência, Muita Urgência, Emergência): ");
            String prioStr = scanner.nextLine();
            try {
                prioridade = Prioridade.fromString(prioStr);
                prioridadeValida = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Por favor, digite uma das opções válidas.");
            }
        }

        System.out.println("\n--- Registro de Sinais Vitais ---");
        System.out.print("Pressão Sistólica (ex: 120.5): ");
        double pressaoSistolica = scanner.nextDouble();
        System.out.print("Pressão Diastólica (ex: 80.2): ");
        double pressaoDiastolica = scanner.nextDouble();
        System.out.print("Batimentos Cardíacos (ex: 75.0): ");
        double batimentosCardiacos = scanner.nextDouble();
        System.out.print("Temperatura (ex: 37.5): ");
        double temperatura = scanner.nextDouble();
        scanner.nextLine(); // Consumir a quebra de linha

        Paciente novoPaciente = new Paciente(nome, idade, cpf, sintomas, prioridade,
                pressaoSistolica, pressaoDiastolica, batimentosCardiacos, temperatura);

        try {
            pacienteService.cadastrarPaciente(novoPaciente);
            System.out.println("Paciente cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar paciente: " + e.getMessage());
        }
    }

    private void listarPacientes() {
        System.out.println("\n--- Fila de Atendimento Hospitalar (Ordenada por Prioridade) ---");
        List<Paciente> pacientes = pacienteService.listarPacientesOrdenadosPorPrioridade();

        if (pacientes.isEmpty()) {
            System.out.println("Nenhum paciente na fila de atendimento.");
            return;
        }

        int medicosDisponiveis = 2;

        System.out.printf("%-5s %-25s %-15s %-20s %-20s %-25s%n",
                "ID", "Nome", "CPF", "Prioridade", "Triagem", "Tempo Espera Estimado");
        System.out.println("------------------------------------------------------------------------------------------------------------------");

        for (Paciente p : pacientes) {
            String tempoEspera = pacienteService.estimarTempoEspera(p, medicosDisponiveis);
            System.out.printf("%-5d %-25s %-15s %-20s %-20s %-25s%n",
                    p.getId(), p.getNome(), p.getCpf(), p.getPrioridade().getDescricao(),
                    p.getDataHoraTriagem().toLocalTime().toString(), tempoEspera);
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------");

        System.out.println("\n--- Painel de Controle: Alertas de Prioridade Máxima ---");
        boolean alertaEmitido = false;
        for (Paciente p : pacientes) {
            if (p.getPrioridade() == Prioridade.EMERGENCIA || p.getPrioridade() == Prioridade.MUITA_URGENCIA) {
                System.out.printf("!!! ALERTA: Paciente %s (CPF: %s) - %s. Sintomas: %s%n",
                        p.getNome(), p.getCpf(), p.getPrioridade().getDescricao(), p.getSintomas());
                alertaEmitido = true;
            }
        }
        if (!alertaEmitido) {
            System.out.println("Nenhum alerta de prioridade máxima no momento.");
        }
    }

    private void editarPaciente(Scanner scanner) {
        System.out.println("\n--- Edição de Paciente ---");
        System.out.print("Digite o CPF do paciente que deseja editar: ");
        String cpf = scanner.nextLine();

        Optional<Paciente> pacienteOpt = pacienteService.buscarPacientePorCpf(cpf);
        if (pacienteOpt.isEmpty()) {
            System.out.println("Paciente com CPF " + cpf + " não encontrado.");
            return;
        }

        Paciente paciente = pacienteOpt.get();
        System.out.println("Paciente encontrado: " + paciente.getNome());

        System.out.print("Novo nome (deixe em branco para manter '" + paciente.getNome() + "'): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isEmpty()) {
            paciente.setNome(novoNome);
        }

        System.out.print("Nova idade (deixe em branco para manter " + paciente.getIdade() + "): ");
        String novaIdadeStr = scanner.nextLine();
        if (!novaIdadeStr.isEmpty()) {
            try {
                paciente.setIdade(Integer.parseInt(novaIdadeStr));
            } catch (NumberFormatException e) {
                System.out.println("Idade inválida. Mantendo a idade anterior.");
            }
        }

        System.out.print("Novos sintomas (deixe em branco para manter '" + paciente.getSintomas() + "'): ");
        String novosSintomas = scanner.nextLine();
        if (!novosSintomas.isEmpty()) {
            paciente.setSintomas(novosSintomas);
        }

        Prioridade novaPrioridade = null;
        boolean prioridadeValida = false;
        while (!prioridadeValida) {
            System.out.print("Nova Prioridade (Não Urgência, Pouca Urgência, Muita Urgência, Emergência - deixe em branco para manter '" + paciente.getPrioridade().getDescricao() + "'): ");
            String novaPrioStr = scanner.nextLine();
            if (novaPrioStr.isEmpty()) {
                prioridadeValida = true; // Mantém a prioridade atual
            } else {
                try {
                    novaPrioridade = Prioridade.fromString(novaPrioStr);
                    paciente.setPrioridade(novaPrioridade);
                    prioridadeValida = true;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage() + " Por favor, digite uma das opções válidas.");
                }
            }
        }

        System.out.println("\n--- Atualização de Sinais Vitais ---");
        System.out.print("Nova Pressão Sistólica (deixe em branco para manter " + paciente.getPressaoSistolica() + "): ");
        String pressaoSistolicaStr = scanner.nextLine();
        if (!pressaoSistolicaStr.isEmpty()) {
            try {
                paciente.setPressaoSistolica(Double.parseDouble(pressaoSistolicaStr));
            } catch (NumberFormatException e) {
                System.out.println("Pressão Sistólica inválida. Mantendo a anterior.");
            }
        }

        System.out.print("Nova Pressão Diastólica (deixe em branco para manter " + paciente.getPressaoDiastolica() + "): ");
        String pressaoDiastolicaStr = scanner.nextLine();
        if (!pressaoDiastolicaStr.isEmpty()) {
            try {
                paciente.setPressaoDiastolica(Double.parseDouble(pressaoDiastolicaStr));
            } catch (NumberFormatException e) {
                System.out.println("Pressão Diastólica inválida. Mantendo a anterior.");
            }
        }

        System.out.print("Novos Batimentos Cardíacos (deixe em branco para manter " + paciente.getBatimentosCardiacos() + "): ");
        String batimentosStr = scanner.nextLine();
        if (!batimentosStr.isEmpty()) {
            try {
                paciente.setBatimentosCardiacos(Double.parseDouble(batimentosStr));
            } catch (NumberFormatException e) {
                System.out.println("Batimentos Cardíacos inválidos. Mantendo os anteriores.");
            }
        }

        System.out.print("Nova Temperatura (deixe em branco para manter " + paciente.getTemperatura() + "): ");
        String temperaturaStr = scanner.nextLine();
        if (!temperaturaStr.isEmpty()) {
            try {
                paciente.setTemperatura(Double.parseDouble(temperaturaStr));
            } catch (NumberFormatException e) {
                System.out.println("Temperatura inválida. Mantendo a anterior.");
            }
        }

        try {
            pacienteService.atualizarPaciente(paciente);
            System.out.println("Paciente atualizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar paciente: " + e.getMessage());
        }
    }

    private void removerPaciente(Scanner scanner) {
        System.out.println("\n--- Remoção de Paciente ---");
        System.out.print("Digite o CPF do paciente que deseja remover: ");
        String cpf = scanner.nextLine();

        try {
            pacienteService.removerPacientePorCpf(cpf);
            System.out.println("Paciente com CPF " + cpf + " removido com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao remover paciente: " + e.getMessage());
        }
    }
}