package views.viva.views.Entidades.Relatorio.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import views.viva.views.Entidades.Transacao.Model.transacao;

public class relatorioService {

    public Map<LocalDate, List<transacao>> agruparTransacoesPorDia(List<transacao> transacoes) {
        Map<LocalDate, List<transacao>> transacoesPorDia = new HashMap<>();

        for (transacao transacao : transacoes) {
            LocalDateTime dataHoraTransacao = LocalDateTime.ofInstant(Instant.ofEpochMilli(transacao.getCriadoEm().getTime()), ZoneId.systemDefault());
            LocalDate dataTransacao = dataHoraTransacao.toLocalDate();
            transacoesPorDia.putIfAbsent(dataTransacao, new ArrayList<>());
            transacoesPorDia.get(dataTransacao).add(transacao);
        }

        return transacoesPorDia;
    }

}
