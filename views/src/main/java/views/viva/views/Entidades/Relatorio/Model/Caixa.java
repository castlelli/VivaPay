package views.viva.views.Entidades.Relatorio.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)

public class Caixa {
    private String voucher;
    private Timestamp criadoEm;
    private double valorDepositos;
    private double valorEstorno;
    private double valorPagamento;
    private String valorDepositosString;
    private String valorSaidaString;
}
