package views.viva.views.Entidades.Operacoes.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class operacaoVendedorEvento {
    private int idOperacaoVendedorEvento;
    private String tipo;
    private int responsavel;
    private int idVendedorEvento;
    private Timestamp dataOperacao;
    private String dataHora;
    private String nomeResponsavel;
    private String nomeVendedor;
}
