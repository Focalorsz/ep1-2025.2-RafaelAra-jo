import java.util.*;

public class GerenciadorMedicos {
    private Map<String, Medico> medicosPorCRM = new HashMap<>();
    private List<Medico> todosMedicos = new ArrayList<>();

    public void cadastrarMedico(Medico medico) {
        if (medicosPorCRM.containsKey(medico.getCRM())) {
            throw new IllegalArgumentException("Médico com CRM " + medico.getCRM() + " já cadastrado");
        }
        medicosPorCRM.put(medico.getCRM(), medico);
        todosMedicos.add(medico);
        System.out.println("✅ Médico " + medico.getNome() + " cadastrado com sucesso!");
    }

    public Medico buscarPorCRM(String crm) {
        return medicosPorCRM.get(crm);
    }

    public List<Medico> listarMedicos() {
        return new ArrayList<>(todosMedicos);
    }

    public List<Medico> buscarPorEspecialidade(Especialidade especialidade) {
        return todosMedicos.stream()
                .filter(m -> m.getEspecialidade() == especialidade)
                .toList();
    }

    public boolean medicoExiste(String crm) {
        return medicosPorCRM.containsKey(crm);
    }
}