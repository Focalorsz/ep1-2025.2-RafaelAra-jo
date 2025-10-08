import java.util.*;

public class GerenciadorPacientes {
    private Map<String, Paciente> pacientesPorCPF = new HashMap<>();
    private List<Paciente> todosPacientes = new ArrayList<>();

    public void cadastrarPaciente(Paciente paciente) {
        if (pacientesPorCPF.containsKey(paciente.getCpf())) {
            throw new IllegalArgumentException("Paciente com CPF " + paciente.getCpf() + " já cadastrado");
        }
        pacientesPorCPF.put(paciente.getCpf(), paciente);
        todosPacientes.add(paciente);
        System.out.println("✅ Paciente " + paciente.getNome() + " cadastrado com sucesso!");
    }

    public Paciente buscarPorCPF(String cpf) {
        return pacientesPorCPF.get(cpf);
    }

    public List<Paciente> listarPacientes() {
        return new ArrayList<>(todosPacientes);
    }

    public boolean pacienteExiste(String cpf) {
        return pacientesPorCPF.containsKey(cpf);
    }
}