package br.com.manipuladorcsv.manipuladores.parametros;

import br.com.manipuladorcsv.manipuladores.interfaces.ManipuladorFunction;
import br.com.manipuladorcsv.manipuladores.interfaces.ManipuladorFunctionImpl;
import br.com.manipuladorcsv.manipuladores.interfaces.ManipuladorParametroRequest;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MapParametroRequest<E> implements ParametroRequest<E, Function<E, Object>> {

    private ManipuladorParametroRequest<E> manipuladorParametroRequest;
    private String conteudoParametro = null;
    private ManipuladorFunction<E> manipuladorFunction;

    private static final String ATRIBUICAO = "=";

    public MapParametroRequest() {
        this.manipuladorFunction = new ManipuladorFunctionImpl<>();
    }

    @Override
    public void setManipuladorParametroRequest(ManipuladorParametroRequest<E> manipuladorParametroRequest) {
        this.manipuladorParametroRequest = manipuladorParametroRequest;
    }

    @Override
    public void setConteudoParametro(String conteudoParametro) {
        this.conteudoParametro = conteudoParametro;
    }

    @Override
    public Function<E, Object> getOperacao() {
        Field field = validarField();
        return this.manipuladorFunction.gerarFunction(field);
    }

    private Field validarField() {
        final Optional<Field> fieldOptional = this.manipuladorParametroRequest.getFields()
                .stream()
                .filter(field -> field.getName().equals(this.conteudoParametro))
                .findFirst();

        if (fieldOptional.isEmpty()) throw new RuntimeException("Nome do field não encontrado -> " + this.conteudoParametro);

        return fieldOptional.get();
    }
}
