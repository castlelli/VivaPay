package views.viva.views.Funcionalidades.Impressora;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.json.JSONObject;
import views.viva.views.Funcionalidades.Converter.DateTime.dateTimeConverter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class Impressao {

    public static final views.viva.views.Funcionalidades.Converter.DateTime.dateTimeConverter dateTimeConverter = new dateTimeConverter();

    public static void ObterImpressoras(){
        try {
            String[] impresoras = ConectorPluginV3.obtenerImpresoras();
            for (String impresora : impresoras) {
            }
        } catch (IOException | InterruptedException e) {
        }
    }

    public static String formatarData(String dataIso) {
        try {
            // Formato da data ISO que vem no JSON
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date data = isoFormat.parse(dataIso);

            // Formato desejado (dd/MM/yyyy)
            SimpleDateFormat formatoDesejado = new SimpleDateFormat("dd/MM/yyyy");
            return formatoDesejado.format(data);
        } catch (ParseException e) {
            // Tratar o erro e retornar uma string de erro ou data padrão
            return "";  // ou retorne uma string padrão, como "Data inválida"
        }
    }

    public static String verificaMetodo(String metodo){
        if (Objects.equals(metodo, "P")) {
            return "PIX";
        } else if (Objects.equals(metodo, "D")) {
            return "DINHEIRO";
        }
        else{
            return"CARTÃO";
        }
    }

    public static void ImprimirComVoucher(JSONObject jsonResponse, String txtVoucher) {
        ObterImpressoras();
        JSONObject transacao = jsonResponse.getJSONObject("transacao");
        Double valor = transacao.getDouble("valor");
        String voucher= txtVoucher;
        String data = transacao.getString("criadoEm");
        String dataFormatada = formatarData(data);
        String metodo = verificaMetodo(transacao.getString("metodo"));
        Imprimir(voucher,valor.toString(),metodo, dataFormatada);
    }

    public static void ImprimirSemVoucher(JSONObject jsonResponse, String valorVoucher, String pagamento)  {
        ObterImpressoras();
        JSONObject transacao = jsonResponse.getJSONObject("clienteEvento");
        Integer voucher = transacao.getInt("voucher");
        String data = transacao.getString("criadoEm");
        String dataFormatada = formatarData(data);
        String valor = valorVoucher;
        String metodo = verificaMetodo(pagamento);
        Imprimir(voucher.toString(),valor,metodo,dataFormatada);
    }

    public static void ImprimirOffline( String valorVoucher, String pagamento){
        ObterImpressoras();
        String valor = valorVoucher;
        String metodo = verificaMetodo(pagamento);
        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatar = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = hoje.format(formatar);
        ImprimirOff(valor,metodo,dataFormatada);
    }

    public static void Imprimir(  String voucher, String valor, String metodo, String data){
        final String serial = "COM6"; //sempre mudar de acordo com a porta
        ConectorPluginV3 conectorPluginV3 = new ConectorPluginV3(ConectorPluginV3.URL_PLUGIN_POR_DEFECTO, serial);
        conectorPluginV3.Iniciar()
                .HabilitarCaracteresPersonalizados()
                .DeshabilitarElModoDeCaracteresChinos()
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                .DescargarImagenDeInternetEImprimir("https://projetoscti.com.br/projetoscti25/img/logoviva.png", 100, 216)
                .EstablecerEnfatizado(true)
                .EstablecerTamanoFuente(2, 1)
                .Feed(2)
                .EscribirTexto("VOUCHER\n" +
                        "Semana do Colegio \n" +
                        data)
                .EstablecerEnfatizado(false)
                .EstablecerTamanoFuente(2, 1)
                .Feed(2)
                .ImprimirCodigoQr(voucher,
                        320,
                        ConectorPluginV3.RECUPERACION_QR_MEJOR,
                        ConectorPluginV3.TAMANO_IMAGEN_NORMAL)
                .EscribirTexto(voucher)
                .Feed(2)
                .EscribirTexto("R$" + valor + " - " + metodo)/*"R$ 00.00 - PIX"*/
                .Feed(2)
                .EstablecerTamanoFuente(2, 1)
                .EstablecerEnfatizado(true)
                .EscribirTexto("AVISO!\n")
                .EstablecerEnfatizado(false)
                .Feed(1)
                .EstablecerTamanoFuente(1, 1)
                .EscribirTexto("EM CASO DE PERDA DO VOUCHER, O ESTORNO \nNAO SERA REALIZADO! \nNAO PERCA SEU VOUCHER! ")
                .Feed(3)
                .Corte(1);
        try {
            conectorPluginV3.imprimirEn("MP-4200 TH");// sempre colocar o mesmo nome da instalação/compartilhamento da impressora
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro de impressão: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public static void ImprimirOff( String valor, String metodo, String data){
        final String serial = "COM6"; //sempre mudar de acordo com a porta
        ConectorPluginV3 conectorPluginV3 = new ConectorPluginV3(ConectorPluginV3.URL_PLUGIN_POR_DEFECTO, serial);
        conectorPluginV3.Iniciar()
                .HabilitarCaracteresPersonalizados()
                .DeshabilitarElModoDeCaracteresChinos()
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                .DescargarImagenDeInternetEImprimir("https://projetoscti.com.br/projetoscti25/img/logoviva.png", 100, 216)
                .EstablecerEnfatizado(true)
                .EstablecerTamanoFuente(2, 1)
                .Feed(2)
                .EscribirTexto("VOUCHER\n" +
                        "Semana do Colegio \n" +
                        data)
                .EstablecerEnfatizado(false)
                .EstablecerTamanoFuente(2, 1)
                .Feed(2)
                .EstablecerTamanoFuente(5, 2)
                .EscribirTexto("R$ " + valor + ",00")
                .Feed(1)
                .EscribirTexto(metodo)/*"R$ 00.00 - PIX"*/
                .Feed(2)
                .EstablecerTamanoFuente(2, 1)
                .EstablecerEnfatizado(true)
                .EscribirTexto("AVISO!\n")
                .EstablecerEnfatizado(false)
                .Feed(1)
                .EstablecerTamanoFuente(1, 1)
                .EscribirTexto("EM CASO DE PERDA DO VOUCHER, O ESTORNO \nNAO SERA REALIZADO! \nNAO PERCA SEU VOUCHER! ")
                .Feed(3)
                .Corte(1);
        try {
            conectorPluginV3.imprimirEn("MP-4200 TH");// sempre colocar o mesmo nome da instalação/compartilhamento da impressora
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro de impressão: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }




}