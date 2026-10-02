package views.viva.views.Entidades.Operacoes.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class operacaoEvento {
    private int idOperacaoEvento;
    private String tipo;
    private int responsavel;
    private int idEvento;
    private Timestamp dataOperacao;
    private String dataHora;
    private String nomeResponsavel;
    private String nomeEvento;
}
