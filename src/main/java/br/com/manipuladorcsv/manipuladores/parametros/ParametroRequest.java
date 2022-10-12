package br.com.manipuladorcsv.manipuladores.parametros;

import br.com.manipuladorcsv.manipuladores.interfaces.ManipuladorParametroRequest;

public interface ParametroRequest<E, R> {

    void setManipuladorParametroRequest(ManipuladorParametroRequest<E> manipuladorParametroRequest);

    void setConteudoParametro(String conteudoParametro);

    R getOperacao();
}
