package views.viva.views.Entidades.Transacao.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class transacao {
    private int idTransacao;
    private double valor;
    private String tipo;
    private String metodo;
    private int idClienteEvento;
    private Timestamp criadoEm;
    private String voucher;
    private String dia;
    private String hora;
    private String valorString;

    private Timestamp criadoEmVoucher;
    private double valorDepositos;
    private double valorEstorno;
    private double valorPagamento;
    private String valorDepositosString;
    private String valorSaidaString;
    private String valorPagamentoString;

    private String valorTipoString;
    private double valorTipo;


}
