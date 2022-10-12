package br.com.manipuladorcsv.manipuladores.utils;

import br.com.manipuladorcsv.manipuladores.GeradorOperacoesRequisicao;
import br.com.manipuladorcsv.manipuladores.enums.TipoParametro;
import br.com.manipuladorcsv.manipuladores.interfaces.ManipuladorParametroRequest;
import br.com.manipuladorcsv.manipuladores.parametros.ParametroRequest;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamUtils {

    public static <E> Object processarStreamConformeParametrosDaRequisicao(ManipuladorParametroRequest<E> manipuladorParametroRequest,
            Map<String, String> parametros, List<E> dados) {

        GeradorOperacoesRequisicao<E> geradorOperacoesRequisicao = new GeradorOperacoesRequisicao(manipuladorParametroRequest);
        geradorOperacoesRequisicao.gerarOperacoesDaRequisicaoConformeParametros(parametros);

        try {
            return construirOperacoesDaStreamDefinidasPelaRequisicao(geradorOperacoesRequisicao, dados);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static <E> Object construirOperacoesDaStreamDefinidasPelaRequisicao(GeradorOperacoesRequisicao<E> geradorOperacoesRequisicao, List<E> dados) {
        Map<TipoParametro, ParametroRequest<E, ?>> operacoesParametrosRequest = geradorOperacoesRequisicao.getOperacoesParametrosRequest();
        Stream<?> stream = dados.stream();

        if (operacoesParametrosRequest.isEmpty()) throw new IllegalStateException("Operações não geradas!");

        for (Map.Entry<TipoParametro, ParametroRequest<E, ?>> parametroRequest : operacoesParametrosRequest.entrySet()) {
            TipoParametro tipoParametro = parametroRequest.getKey();
            ParametroRequest<E, ?> parametro = parametroRequest.getValue();

            switch (tipoParametro) {
                case FILTER: {
                    Predicate<Object> predicate = (Predicate<Object>) geradorOperacoesRequisicao.getOperacaoParametroRequest(TipoParametro.FILTER);
                    stream = stream.filter(predicate);
                    break;
                }
                case MAP: {
                    Function<Object, Object> function = (Function<Object, Object>) geradorOperacoesRequisicao.getOperacaoParametroRequest(TipoParametro.MAP);
                    stream = stream.map(function);
                    break;
                }
                case GROUPY_BY: {
                    Collector<Object, ?, ?> collector = (Collector<Object, ?, ?>) geradorOperacoesRequisicao.getOperacaoParametroRequest(TipoParametro.GROUPY_BY);
                    return stream.collect(collector);
                }

                default: throw new IllegalStateException("Tipo de operação não tratada!");
            }
        }

        throw new IllegalStateException("Operação terminal não encontrada!");
    }
}
