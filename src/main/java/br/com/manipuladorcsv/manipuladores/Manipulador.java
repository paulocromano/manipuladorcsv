package br.com.manipuladorcsv.manipuladores;

import java.lang.reflect.Field;
import java.util.List;

public interface Manipulador<E> extends ManipuladorPredicate<E> {

    void prepararManipulador();

    List<String> getNomeFields();

    List<Field> getFields();
}
