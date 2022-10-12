package br.com.manipuladorcsv.manipuladores.interfaces;

import java.lang.reflect.Field;
import java.util.List;

public interface ManipuladorParametroRequest<E> {

    void prepararManipulador();

    List<String> getNomeFields();

    List<Field> getFields();
}
