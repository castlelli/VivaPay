package views.viva.views.Entidades.Cliente.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class clienteEvento {
    private final int idClienteEvento;
    private final int idCliente;
    private final String voucher;
    private final int idEvento;
    private final Timestamp criadoEm;

}
