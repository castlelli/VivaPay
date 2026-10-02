package views.viva.views.Entidades.Vendedor.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@AllArgsConstructor
@Data
public class vendedor {
        private final int idVendedor;
        private String loja;
        private final long idUsuario;
        private final long responsavel;
        private String ativo;
        private final Timestamp criadoEm;
        private String criadoEmString;
        private String nomeResponsavel;

        @Override
        public String toString() {
                return loja;
        }
}