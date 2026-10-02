package views.viva.views.Funcionalidades.Buscar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import views.viva.views.Entidades.Evento.Model.eventos;
import views.viva.views.Entidades.Evento.Service.eventoService;
import views.viva.views.Entidades.Operacoes.Model.operacaoEvento;
import views.viva.views.Entidades.Operacoes.Model.operacaoVendedor;
import views.viva.views.Entidades.Operacoes.Model.operacaoVendedorEvento;
import views.viva.views.Entidades.Operacoes.Service.operacaoVendedorService;
import views.viva.views.Entidades.Operacoes.Service.operacaoEventoService;
import views.viva.views.Entidades.Operacoes.Service.operacaoVendedorEventoService;
import views.viva.views.Entidades.Transacao.Model.transacao;
import views.viva.views.Entidades.Transacao.Service.transacaoService;
import views.viva.views.Entidades.Vendedor.Model.vendedor;
import views.viva.views.Entidades.Vendedor.Model.vendedorEvento;
import views.viva.views.Entidades.Vendedor.Service.vendedorService;

import java.sql.SQLException;
import java.text.ParseException;

public class Buscar {

    public ObservableList<eventos> buscaEventosPorNome(TextField txtNomeEvento, int page) throws SQLException, ParseException {
        eventoService eventoService = new eventoService();
        return FXCollections.observableArrayList(eventoService.listaEventos("https://tcc-r46r.onrender.com/evento/selecao/porquantidade/porNome/"+txtNomeEvento.getText()+"/"+page));
    }


    public ObservableList<transacao> buscaTransacoesPorVoucher(TextField txtBusca, int page, int tipo) throws SQLException, ParseException {
        transacaoService transacaoService = new transacaoService();
        if(tipo == 1) {
            String url = "https://tcc-r46r.onrender.com/transacao/selecao/porquantidade/porvoucherportipo/"+txtBusca.getText()+"/C/" + page;
            return FXCollections.observableArrayList(transacaoService.listaTransacao(url));
        }
        if(tipo == 2){
            String url = "https://tcc-r46r.onrender.com/transacao/selecao/porquantidade/porvoucherportipo/"+txtBusca.getText()+"/E/" + page;
            return FXCollections.observableArrayList(transacaoService.listaTransacao(url));
        }
        else return null;
    }

    public ObservableList<vendedor> buscaVendedoresPorNome(TextField txtBusca, int page) throws SQLException, ParseException {
         vendedorService vendedorService = new vendedorService();
         String url = "https://tcc-r46r.onrender.com/vendedor/selecao/porQuantidade/porNome/"+txtBusca.getText()+"/" + page;
         return FXCollections.observableArrayList(vendedorService.listaVendedores(url));
    }
    public ObservableList<vendedorEvento> buscaVendedoresEventosPorNome(TextField txtBusca, int page) throws SQLException, ParseException {
        vendedorService vendedorService = new vendedorService();
        String url = "https://tcc-r46r.onrender.com/vendedorevento/selecao/porquantidade/porloja/"+txtBusca.getText()+"/";
        return FXCollections.observableArrayList(vendedorService.listaVendedorEventos(url, page));
    }

    public ObservableList<operacaoEvento> buscaOperacoesEventosPorNome(TextField txtBusca, int page) throws SQLException, ParseException {
        operacaoEventoService operacaoEventoService = new operacaoEventoService();
        String url = "https://tcc-r46r.onrender.com/operacoes/selecao/pornome/porquantidade/evento/"+txtBusca.getText()+"/";
        return FXCollections.observableArrayList(operacaoEventoService.listaOperacaoEvento(url, page));
    }

    public ObservableList<operacaoVendedorEvento> buscaOperacoesVendedoresEventosPorNome(TextField txtBusca, int page) throws SQLException, ParseException {
        operacaoVendedorEventoService operacaoVendedorEventoService = new operacaoVendedorEventoService();
        String url = "https://tcc-r46r.onrender.com/operacoes/selecao/pornome/porquantidade/vendedorevento/"+txtBusca.getText()+"/";
        return FXCollections.observableArrayList(operacaoVendedorEventoService.listaOperacaoVendEvento(url, page));
    }

    public ObservableList<operacaoVendedor> buscaOperacoesVendedoresPorNome(TextField txtBusca, int page) {
        operacaoVendedorService operacaoVendedorService = new operacaoVendedorService();
        String url = "https://tcc-r46r.onrender.com/operacoes/selecao/pornome/porquantidade/vendedor/"+txtBusca.getText()+"/";
        return FXCollections.observableArrayList(operacaoVendedorService.listaOperacaoVendedor(url, page));
    }
}
