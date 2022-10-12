package br.com.manipuladorcsv.manipuladores;

import br.com.manipuladorcsv.manipuladores.enums.TipoParametro;
import br.com.manipuladorcsv.manipuladores.interfaces.ManipuladorParametroRequest;
import br.com.manipuladorcsv.manipuladores.parametros.FilterParametroRequest;
import br.com.manipuladorcsv.manipuladores.parametros.GroupByParametroRequest;
import br.com.manipuladorcsv.manipuladores.parametros.MapParametroRequest;
import br.com.manipuladorcsv.manipuladores.parametros.ParametroRequest;
import lombok.Getter;

import java.util.*;
import java.util.function.Predicate;


public class GeradorOperacoesRequisicao<E> {

    @Getter
    private Optional<Predicate<E>> predicateFilterOptional;


    @Getter
    private Map<TipoParametro, ParametroRequest<E, ?>> parametrosRequest;
    private ManipuladorParametroRequest<E> manipuladorParametroRequest;


    public GeradorOperacoesRequisicao(ManipuladorParametroRequest<E> manipuladorParametroRequest) {
        this.manipuladorParametroRequest = manipuladorParametroRequest;
        this.predicateFilterOptional = Optional.empty();
        this.parametrosRequest = new HashMap<>();
    }

    public void gerarOperacoesDaRequisicaoConformeParametros(Map<String, String> parametros) {
        for (TipoParametro tipoParametro : TipoParametro.values()) {
            if (parametros.containsKey(tipoParametro.getValor())) {
                String parametro = parametros.get(tipoParametro.getValor());
                ParametroRequest<E, ?> parametroRequest = null;

                switch (tipoParametro) {
                    case FILTER: {
                        parametroRequest = new FilterParametroRequest<E>();
                        break;
                    }
                    case MAP: {
                        parametroRequest = new MapParametroRequest<E>();
                        break;
                    }
                    case GROUPY_BY: {
                        parametroRequest = new GroupByParametroRequest<E>();
                        break;
                    }
                    default: throw new RuntimeException("Tipo de operação não implementada!");
                }

                parametroRequest.setManipuladorParametroRequest(this.manipuladorParametroRequest);
                parametroRequest.setConteudoParametro(parametro);
                this.parametrosRequest.put(tipoParametro, parametroRequest);
            }
        }
    }
}
