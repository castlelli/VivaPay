package views.viva.views.Funcionalidades.Relatorio;

import java.io.*;

import javafx.scene.Node;
import javafx.stage.FileChooser;

import com.itextpdf.text.Paragraph;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import views.viva.views.Entidades.Relatorio.Model.Caixa;
import views.viva.views.Entidades.Transacao.Model.transacao;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class GerarPDF {
    private String verificaMetodo(String metodo) {
        return metodo.equals("C") ? "Cartão" : (metodo.equals("P") ? "Pix" : "Dinheiro");
    }


    public void gerarPDF(String tipo, String dataInicio, String dataFinal, Map<LocalDate, List<transacao>> transacoesPorDia, Stage stage) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        Path caminhoAtual = Paths.get("").toAbsolutePath();
        double saldo = 0;
        double saldoTotal = 0;

        String dataRelatorio;
        if (Objects.equals(dataInicio, dataFinal)){
            dataRelatorio = dataInicio;
        }
        else{
            dataRelatorio = dataInicio + " - " + dataFinal;
        }

        String html =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<style>" +
                        "table, th, td {" +
                        "border: 1px black;" +
                        "border-collapse: collapse;" +
                        "padding: 10px;" +
                        "}" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<h1 align=\"center\"><img src='" + caminhoAtual + "/src/main/resources/imagens/logo horizontal preta.png' height=\"40\"/></h1>" +
                        "<h3>Relatório de Caixa " + dataRelatorio + "</h3>" +
                        "<h3>Tipo de operação: " + tipo + "</h3>";


        for (Map.Entry<LocalDate, List<transacao>> entry : transacoesPorDia.entrySet()) {
            LocalDate data = entry.getKey();
            List<transacao> listaTransacoes = entry.getValue();
            String saldoMoeda = null;

            html += "<h4 align=\"center\">" + data + "</h4>" +
                    "<table align=\"center\">" +
                    "<tr>" +
                    "<th>Voucher</th>" +
                    "<th>Método</th>" +
                    "<th>Data</th>" +
                    "<th>Valor</th>" +
                    "</tr>";
            for (transacao t : listaTransacoes) {
                saldo += t.getValor();
                NumberFormat formatMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                saldoMoeda = formatMoeda.format(saldo);

                String timestamp = String.valueOf(t.getCriadoEm());
                DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
                        .appendPattern("yyyy-MM-dd HH:mm:ss")
                        .optionalStart()
                        .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, true)
                        .optionalEnd()
                        .toFormatter();
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDateTime dataHora = LocalDateTime.parse(timestamp, inputFormatter);
                String dataFormatada = dataHora.format(outputFormatter);

                html += "<tr>" +
                        "<td>" + t.getVoucher() + "</td>" +
                        "<td>" + verificaMetodo(t.getMetodo()) + "</td>" +
                        "<td>" + dataFormatada + "</td>" +
                        "<td>" + t.getValorString() + "</td>" +
                        "</tr>";
            }
            saldoTotal += saldo;
            html +=
                    "<tr>" +
                            "<th>Saldo</th>" +
                            "<th colspan='3'>" + saldoMoeda + "</th>" +
                            "</tr>" +
                            "</table>";
            saldo = 0;
        }
        NumberFormat formatMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String saldoTotalMoeda = formatMoeda.format(saldoTotal);
        html += "<table align=\"center\">" +
                "<tr>" +
                "<th>Saldo total: </th>" +
                "<th colspan='3'>" + saldoTotalMoeda + "</th>" +
                "</tr>" +
                "</table>" +
                "</body>" +
                "</html>";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(html));

            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            } finally {
                document.close();
            }
        } else {
        }
    }

    public void gerarPDFCaixa(String tipo, String dataInicio, String dataFinal, Map<LocalDate, List<transacao>> transacoesPorDia, Stage stage) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        Path caminhoAtual = Paths.get("").toAbsolutePath();
        double saldo = 0;
        double saldoTotal = 0;

        String dataRelatorio;
        if (Objects.equals(dataInicio, dataFinal)){
            dataRelatorio = dataInicio;
        }
        else{
            dataRelatorio = dataInicio + " - " + dataFinal;
        }

        String html =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<style>" +
                        "table, th, td {" +
                        "border: 1px black;" +
                        "border-collapse: collapse;" +
                        "padding: 10px;" +
                        "}" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<h1 align=\"center\"><img src='" + caminhoAtual + "/src/main/resources/imagens/logo horizontal preta.png' height=\"40\"/></h1>" +
                        "<h3>Relatório de Caixa " + dataRelatorio + "</h3>" +
                        "<h3>Tipo de operação: " + tipo + "</h3>";


        for (Map.Entry<LocalDate, List<transacao>> entry : transacoesPorDia.entrySet()) {
            LocalDate data = entry.getKey();
            List<transacao> listaTransacoes = entry.getValue();
            String saldoMoeda = null;

            html += "<h4 align=\"center\">" + data + "</h4>" +
                    "<table align=\"center\">" +
                    "<tr>" +
                    "<th>Gerado em</th>" +
                    "<th>Voucher</th>" +
                    "<th>Entrada</th>" +
                    "<th>Saída</th>" +
                    "</tr>";
            for (transacao t : listaTransacoes) {
                saldo += t.getValorDepositos() - t.getValorEstorno();
                NumberFormat formatMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                saldoMoeda = formatMoeda.format(saldo);

                String timestamp = String.valueOf(t.getCriadoEmVoucher());
                DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
                        .appendPattern("yyyy-MM-dd HH:mm:ss")
                        .optionalStart()
                        .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, true)
                        .optionalEnd()
                        .toFormatter();
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDateTime dataHora = LocalDateTime.parse(timestamp, inputFormatter);
                String dataFormatada = dataHora.format(outputFormatter);

                html += "<tr>" +
                        "<td>" + dataFormatada + "</td>" +
                        "<td>" + t.getVoucher() + "</td>" +
                        "<td>" + t.getValorDepositosString() + "</td>" +
                        "<td>" + t.getValorSaidaString() + "</td>" +
                        "</tr>";
            }
            saldoTotal += saldo;
            html +=
                    "<tr>" +
                            "<th>Saldo</th>" +
                            "<th colspan='3'>" + saldoMoeda + "</th>" +
                            "</tr>" +
                            "</table>";
            saldo = 0;
        }
        NumberFormat formatMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String saldoTotalMoeda = formatMoeda.format(saldoTotal);
        html += "<table align=\"center\">" +
                "<tr>" +
                "<th>Saldo total: </th>" +
                "<th colspan='3'>" + saldoTotalMoeda + "</th>" +
                "</tr>" +
                "</table>" +
                "</body>" +
                "</html>";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(html));

            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            } finally {
                document.close();
            }
        } else {
        }
    }
}
