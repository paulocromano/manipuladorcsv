package br.com.manipuladorcsv.manipuladores.parametros;

import br.com.manipuladorcsv.manipuladores.interfaces.ManipuladorParametroRequest;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GroupByParametroRequest<E> implements ParametroRequest<E, Map<Object, List<E>>> {

    private ManipuladorParametroRequest<E> manipuladorParametroRequest;
    private String conteudoParametro;


    @Override
    public void setManipuladorParametroRequest(ManipuladorParametroRequest<E> manipuladorParametroRequest) {
        this.manipuladorParametroRequest = manipuladorParametroRequest;
    }

    @Override
    public void setConteudoParametro(String conteudoParametro) {
        this.conteudoParametro = conteudoParametro;
    }

    @Override
    public Map<Object, List<E>> getOperacao() {
        Objects.requireNonNull(manipuladorParametroRequest);
        Objects.requireNonNull(conteudoParametro);

        return null;
    }
}
