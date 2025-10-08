import java.time.LocalDateTime;
import java.util.*;

public class GerenciadorConsultas {
    private List<Consulta> consultas = new ArrayList<>();
    private int proximoId = 1;

    public Consulta agendarConsulta(Paciente paciente, Medico medico, LocalDateTime dataHora, String local) {
        // Verificar se médico está disponível nesse horário
        boolean medicoOcupado = consultas.stream()
                .anyMatch(c -> c.getMedico().equals(medico) && 
                              c.getDataHora().equals(dataHora) && 
                              c.getStatus() != StatusConsulta.CANCELADA);
        
        if (medicoOcupado) {
            throw new IllegalStateException("Médico já possui consulta nesse horário");
        }

        // Verificar se especialidade é adequada para o paciente
        if (!medico.getEspecialidade().isAdequadaPara(paciente.getIdade(), paciente.getGenero())) {
            throw new IllegalStateException("Especialidade " + medico.getEspecialidade().getNomeExibicao() + 
                                          " não é adequada para o paciente");
        }

        Consulta consulta = new Consulta(proximoId++, paciente, medico, dataHora, local);
        consultas.add(consulta);
        
        // Adicionar ao histórico do paciente
        paciente.getHistoricoConsultas().add(consulta);
        
        System.out.println("✅ Consulta agendada com sucesso! ID: " + consulta.getId());
        return consulta;
    }

    public void cancelarConsulta(int idConsulta) {
        Consulta consulta = buscarPorId(idConsulta);
        if (consulta != null) {
            consulta.cancelar();
            System.out.println("✅ Consulta #" + idConsulta + " cancelada com sucesso!");
        } else {
            throw new IllegalArgumentException("Consulta não encontrada");
        }
    }

    public void concluirConsulta(int idConsulta, String diagnostico, String prescricao) {
        Consulta consulta = buscarPorId(idConsulta);
        if (consulta != null) {
            consulta.concluir(diagnostico, prescricao);
            System.out.println("✅ Consulta #" + idConsulta + " concluída com sucesso!");
        } else {
            throw new IllegalArgumentException("Consulta não encontrada");
        }
    }

    public Consulta buscarPorId(int id) {
        return consultas.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Consulta> listarConsultas() {
        return new ArrayList<>(consultas);
    }

    public List<Consulta> consultasPorMedico(String crm) {
        return consultas.stream()
                .filter(c -> c.getMedico().getCRM().equals(crm))
                .toList();
    }

    public List<Consulta> consultasPorPaciente(String cpf) {
        return consultas.stream()
                .filter(c -> c.getPaciente().getCpf().equals(cpf))
                .toList();
    }
}