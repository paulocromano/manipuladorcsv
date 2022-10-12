package br.com.manipuladorcsv.manipuladores.utils;

import br.com.manipuladorcsv.manipuladores.GeradorOperacoesRequisicao;
import br.com.manipuladorcsv.manipuladores.enums.TipoParametro;
import br.com.manipuladorcsv.manipuladores.interfaces.ManipuladorParametroRequest;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StreamUtils {

    public static <E> Object processarStreamConformeParametrosDaRequisicao(ManipuladorParametroRequest<E> manipuladorParametroRequest,
                                                                           Map<String, String> parametros, List<E> dados) {

        GeradorOperacoesRequisicao<E> geradorOperacoesRequisicao = new GeradorOperacoesRequisicao(manipuladorParametroRequest);
        geradorOperacoesRequisicao.gerarOperacoesDaRequisicaoConformeParametros(parametros);
        Predicate<E> predicate = (Predicate<E>) geradorOperacoesRequisicao.getParametrosRequest().get(TipoParametro.FILTER).getOperacao();
        Function<E, Object> function = (Function<E, Object>) geradorOperacoesRequisicao.getParametrosRequest().get(TipoParametro.MAP).getOperacao();

        return dados.stream()
                .filter(predicate)
                .map(function)
                .peek(System.out::println)
                .collect(Collectors.toList());
    }
}
