import java.util.*;

public class GerenciadorInternacoes {
    private List<Internacao> internacoes = new ArrayList<>();
    private GerenciadorQuartos gerenciadorQuartos;

    public GerenciadorInternacoes(GerenciadorQuartos gerenciadorQuartos) {
        this.gerenciadorQuartos = gerenciadorQuartos;
    }

    public Internacao internarPaciente(Paciente paciente, Medico medico, int numeroQuarto, String periodoEstimado) {
        Quarto quarto = gerenciadorQuartos.buscarQuartoDisponivel(numeroQuarto);
        
        if (quarto == null) {
            throw new IllegalStateException("Quarto " + numeroQuarto + " não disponível");
        }

        Internacao internacao = new Internacao(paciente, medico, quarto, periodoEstimado);
        internacoes.add(internacao);
        
        // Adicionar ao histórico do paciente
        paciente.getHistoricoInternacoes().add(internacao);
        
        System.out.println("✅ Paciente " + paciente.getNome() + " internado no quarto " + numeroQuarto);
        return internacao;
    }

    public void darAlta(int numeroQuarto) {
        Internacao internacao = buscarInternacaoAtivaPorQuarto(numeroQuarto);
        if (internacao != null) {
            internacao.darAlta();
            System.out.println("✅ Alta concedida para paciente do quarto " + numeroQuarto);
        } else {
            throw new IllegalArgumentException("Nenhuma internação ativa encontrada para o quarto " + numeroQuarto);
        }
    }

    private Internacao buscarInternacaoAtivaPorQuarto(int numeroQuarto) {
        return internacoes.stream()
                .filter(i -> i.isAtiva() && i.getQuarto().getNumero() == numeroQuarto)
                .findFirst()
                .orElse(null);
    }

    public List<Internacao> listarInternacoesAtivas() {
        return internacoes.stream()
                .filter(Internacao::isAtiva)
                .toList();
    }
}