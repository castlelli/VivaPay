package views.viva.views.Entidades.Vendedor.Model;

import lombok.*;

import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class vendedorEvento {
    private int idVendedorEvento;
    private int idVendedor;
    private int idEvento;
    private int responsavel;
    private Timestamp criadoEm;
    private Boolean ativo;
    private String ativoString;
    private String nomeVendedor;
    private String nomeResponsavel;
    private String nomeEvento;
    private String criadoEmString;
}