import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class SistemaHospitalar {
    private static Scanner scanner = new Scanner(System.in);
    private static GerenciadorMedicos gerenciadorMedicos = new GerenciadorMedicos();
    private static GerenciadorPacientes gerenciadorPacientes = new GerenciadorPacientes();
    private static GerenciadorQuartos gerenciadorQuartos = new GerenciadorQuartos();
    private static GerenciadorConsultas gerenciadorConsultas = new GerenciadorConsultas();
    private static GerenciadorInternacoes gerenciadorInternacoes = new GerenciadorInternacoes(gerenciadorQuartos);

    public static void main(String[] args) {
        System.out.println("🏥 SISTEMA HOSPITALAR - INICIANDO");
        
        // Cadastro inicial de alguns quartos
        inicializarQuartos();
        
        int opcao;
        do {
            exibirMenu();
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
            
            switch (opcao) {
                case 1 -> cadastrarMedico();
                case 2 -> cadastrarPaciente();
                case 3 -> agendarConsulta();
                case 4 -> realizarConsulta();
                case 5 -> cancelarConsulta();
                case 6 -> listarConsultas();
                case 7 -> internarPaciente();
                case 8 -> darAlta();
                case 9 -> listarMedicos();
                case 10 -> listarPacientes();
                case 11 -> listarQuartosDisponiveis();
                case 0 -> System.out.println("👋 Saindo do sistema...");
                default -> System.out.println("❌ Opção inválida!");
            }
            
            if (opcao != 0) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine();
            }
        } while (opcao != 0);
        
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🏥 SISTEMA HOSPITALAR - MENU PRINCIPAL");
        System.out.println("=".repeat(50));
        System.out.println("1. 👨‍⚕️  Cadastrar Médico");
        System.out.println("2. 👤 Cadastrar Paciente");
        System.out.println("3. 📅 Agendar Consulta");
        System.out.println("4. 🩺 Realizar Consulta (concluir)");
        System.out.println("5. ❌ Cancelar Consulta");
        System.out.println("6. 📋 Listar Consultas");
        System.out.println("7. 🏥 Internar Paciente");
        System.out.println("8. ✅ Dar Alta");
        System.out.println("9. 👨‍⚕️  Listar Médicos");
        System.out.println("10. 👤 Listar Pacientes");
        System.out.println("11. 🚪 Listar Quartos Disponíveis");
        System.out.println("0. 🚪 Sair");
        System.out.println("=".repeat(50));
        System.out.print("Escolha uma opção: ");
    }

    private static void inicializarQuartos() {
        for (int i = 101; i <= 110; i++) {
            gerenciadorQuartos.adicionarQuarto(new Quarto(i));
        }
        System.out.println("✅ 10 quartos cadastrados automaticamente");
    }

    private static void cadastrarMedico() {
        System.out.println("\n--- CADASTRO DE MÉDICO ---");
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        
        System.out.print("Idade: ");
        int idade = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Gênero (MASCULINO/FEMININO): ");
        Genero genero = Genero.valueOf(scanner.nextLine().toUpperCase());
        
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        
        System.out.print("CRM: ");
        String crm = scanner.nextLine();
        
        System.out.print("Custo da Consulta: ");
        double custoConsulta = scanner.nextDouble();
        scanner.nextLine();
        
        // Listar especialidades
        System.out.println("\nEspecialidades disponíveis:");
        Especialidade[] especialidades = Especialidade.values();
        for (int i = 0; i < especialidades.length; i++) {
            System.out.println((i + 1) + ". " + especialidades[i].getNomeExibicao());
        }
        System.out.print("Escolha a especialidade (número): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();
        
        Especialidade especialidade = especialidades[escolha - 1];
        
        Medico medico = new Medico(nome, cpf, idade, genero, telefone, especialidade, crm, custoConsulta);
        gerenciadorMedicos.cadastrarMedico(medico);
    }

    private static void cadastrarPaciente() {
        System.out.println("\n--- CADASTRO DE PACIENTE ---");
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        
        System.out.print("Idade: ");
        int idade = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Gênero (MASCULINO/FEMININO): ");
        Genero genero = Genero.valueOf(scanner.nextLine().toUpperCase());
        
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        
        // Listar planos de saúde
        System.out.println("\nPlanos de saúde disponíveis:");
        PlanoSaude[] planos = PlanoSaude.values();
        for (int i = 0; i < planos.length; i++) {
            System.out.println((i + 1) + ". " + planos[i].getDisplayName());
        }
        System.out.print("Escolha o plano (número): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();
        
        PlanoSaude plano = planos[escolha - 1];
        
        Paciente paciente = new Paciente(nome, cpf, idade, genero, telefone, plano);
        gerenciadorPacientes.cadastrarPaciente(paciente);
    }

    private static void agendarConsulta() {
        System.out.println("\n--- AGENDAR CONSULTA ---");
        
        System.out.print("CPF do Paciente: ");
        String cpfPaciente = scanner.nextLine();
        Paciente paciente = gerenciadorPacientes.buscarPorCPF(cpfPaciente);
        
        if (paciente == null) {
            System.out.println("❌ Paciente não encontrado!");
            return;
        }
        
        System.out.print("CRM do Médico: ");
        String crmMedico = scanner.nextLine();
        Medico medico = gerenciadorMedicos.buscarPorCRM(crmMedico);
        
        if (medico == null) {
            System.out.println("❌ Médico não encontrado!");
            return;
        }
        
        System.out.print("Data e Hora (dd/MM/yyyy HH:mm): ");
        String dataHoraStr = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dataHora = LocalDateTime.parse(dataHoraStr, formatter);
        
        System.out.print("Local: ");
        String local = scanner.nextLine();
        
        try {
            gerenciadorConsultas.agendarConsulta(paciente, medico, dataHora, local);
        } catch (Exception e) {
            System.out.println("❌ Erro ao agendar consulta: " + e.getMessage());
        }
    }

    private static void realizarConsulta() {
        System.out.println("\n--- REALIZAR CONSULTA ---");
        
        System.out.print("ID da Consulta: ");
        int idConsulta = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Diagnóstico: ");
        String diagnostico = scanner.nextLine();
        
        System.out.print("Prescrição: ");
        String prescricao = scanner.nextLine();
        
        try {
            gerenciadorConsultas.concluirConsulta(idConsulta, diagnostico, prescricao);
        } catch (Exception e) {
            System.out.println("❌ Erro ao realizar consulta: " + e.getMessage());
        }
    }

    private static void cancelarConsulta() {
        System.out.println("\n--- CANCELAR CONSULTA ---");
        
        System.out.print("ID da Consulta: ");
        int idConsulta = scanner.nextInt();
        scanner.nextLine();
        
        try {
            gerenciadorConsultas.cancelarConsulta(idConsulta);
        } catch (Exception e) {
            System.out.println("❌ Erro ao cancelar consulta: " + e.getMessage());
        }
    }

    private static void listarConsultas() {
        System.out.println("\n--- LISTA DE CONSULTAS ---");
        List<Consulta> consultas = gerenciadorConsultas.listarConsultas();
        
        if (consultas.isEmpty()) {
            System.out.println("Nenhuma consulta cadastrada.");
        } else {
            consultas.forEach(consulta -> {
                System.out.println("ID: " + consulta.getId() + 
                                 " | Paciente: " + consulta.getPaciente().getNome() +
                                 " | Médico: " + consulta.getMedico().getNome() +
                                 " | Data: " + consulta.getDataHora() +
                                 " | Status: " + consulta.getStatus().getDisplayName());
            });
        }
    }

    private static void internarPaciente() {
        System.out.println("\n--- INTERNAR PACIENTE ---");
        
        System.out.print("CPF do Paciente: ");
        String cpfPaciente = scanner.nextLine();
        Paciente paciente = gerenciadorPacientes.buscarPorCPF(cpfPaciente);
        
        if (paciente == null) {
            System.out.println("❌ Paciente não encontrado!");
            return;
        }
        
        System.out.print("CRM do Médico Responsável: ");
        String crmMedico = scanner.nextLine();
        Medico medico = gerenciadorMedicos.buscarPorCRM(crmMedico);
        
        if (medico == null) {
            System.out.println("❌ Médico não encontrado!");
            return;
        }
        
        System.out.print("Número do Quarto: ");
        int numeroQuarto = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Período Estimado: ");
        String periodo = scanner.nextLine();
        
        try {
            gerenciadorInternacoes.internarPaciente(paciente, medico, numeroQuarto, periodo);
        } catch (Exception e) {
            System.out.println("❌ Erro ao internar paciente: " + e.getMessage());
        }
    }

    private static void darAlta() {
        System.out.println("\n--- DAR ALTA ---");
        
        System.out.print("Número do Quarto: ");
        int numeroQuarto = scanner.nextInt();
        scanner.nextLine();
        
        try {
            gerenciadorInternacoes.darAlta(numeroQuarto);
        } catch (Exception e) {
            System.out.println("❌ Erro ao dar alta: " + e.getMessage());
        }
    }

    private static void listarMedicos() {
        System.out.println("\n--- LISTA DE MÉDICOS ---");
        List<Medico> medicos = gerenciadorMedicos.listarMedicos();
        
        if (medicos.isEmpty()) {
            System.out.println("Nenhum médico cadastrado.");
        } else {
            medicos.forEach(medico -> {
                System.out.println("CRM: " + medico.getCRM() + 
                                 " | Nome: " + medico.getNome() +
                                 " | Especialidade: " + medico.getEspecialidade().getNomeExibicao() +
                                 " | Telefone: " + medico.getTelefone());
            });
        }
    }

    private static void listarPacientes() {
        System.out.println("\n--- LISTA DE PACIENTES ---");
        List<Paciente> pacientes = gerenciadorPacientes.listarPacientes();
        
        if (pacientes.isEmpty()) {
            System.out.println("Nenhum paciente cadastrado.");
        } else {
            pacientes.forEach(paciente -> {
                System.out.println("CPF: " + paciente.getCpf() + 
                                 " | Nome: " + paciente.getNome() +
                                 " | Idade: " + paciente.getIdade() +
                                 " | Plano: " + paciente.getPlano().getDisplayName());
            });
        }
    }

    private static void listarQuartosDisponiveis() {
        System.out.println("\n--- QUARTOS DISPONÍVEIS ---");
        List<Quarto> quartosDisponiveis = gerenciadorQuartos.listarQuartosDisponiveis();
        
        if (quartosDisponiveis.isEmpty()) {
            System.out.println("Nenhum quarto disponível.");
        } else {
            quartosDisponiveis.forEach(quarto -> {
                System.out.println("Quarto " + quarto.getNumero() + " - Disponível");
            });
        }
    }
}