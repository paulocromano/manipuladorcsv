package br.com.manipuladorcsv.manipuladores;

import br.com.manipuladorcsv.manipuladores.enums.TipoParametro;
import br.com.manipuladorcsv.manipuladores.interfaces.ManipuladorParametroRequest;
import br.com.manipuladorcsv.manipuladores.parametros.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


public class GeradorOperacoesRequisicao<E> {

    @Getter
    private Map<TipoParametro, ParametroRequest<E, ?>> operacoesParametrosRequest;
    private ManipuladorParametroRequest<E> manipuladorParametroRequest;


    public GeradorOperacoesRequisicao(ManipuladorParametroRequest<E> manipuladorParametroRequest) {
        this.manipuladorParametroRequest = manipuladorParametroRequest;
        this.operacoesParametrosRequest = new HashMap<>();
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
                    case ORDER_BY: {
                        parametroRequest = new OrderByParametroRequest<E>();
                        break;
                    }
                    case GROUPY_BY: {
                        parametroRequest = new GroupByParametroRequest<E>();
                        break;
                    }
                    default: throw new IllegalStateException("Tipo de operação não implementada!");
                }

                parametroRequest.setManipuladorParametroRequest(this.manipuladorParametroRequest);
                parametroRequest.setConteudoParametro(parametro);
                this.operacoesParametrosRequest.put(tipoParametro, parametroRequest);
            }
        }
    }

    public Object getOperacaoParametroRequest(TipoParametro tipoParametro) {
        if (!operacoesParametrosRequest.containsKey(tipoParametro))  throw new IllegalArgumentException("Tipo de parâmetro não encontrado!");
        return operacoesParametrosRequest.get(tipoParametro).getOperacao();
    }
}
