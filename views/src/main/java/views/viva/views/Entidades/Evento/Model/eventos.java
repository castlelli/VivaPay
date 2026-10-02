package views.viva.views.Entidades.Evento.Model;

import lombok.*;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

//@ToString(onlyExplicitlyIncluded = true)
@Data
@AllArgsConstructor
public class eventos {
    private final int idEvento;
    private final long idInstituicao;
    private final long responsavel;
    private boolean ativo;
    private final String nomeEvento;
    private final String abreviacaoEvento;
    private final String descricaoEvento;
    private final Timestamp horaInicio;
    private final Timestamp horaFinal;
    private final Timestamp criadoEm;
    private String dataHoraCriaEm;
    private String dataHoraInicio;
    private String dataHoraFinal;
    private String nomeResponsavel;

    @Override
    public String toString() {
        return nomeEvento;
    }
}
