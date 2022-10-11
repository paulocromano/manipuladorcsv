package br.com.manipuladorcsv.despesaPublica.model;

import br.com.manipuladorcsv.manipuladores.GeradorPredicate;
import br.com.manipuladorcsv.manipuladores.interfaces.Manipulador;
import br.com.manipuladorcsv.manipuladores.enums.OperadorLogico;
import br.com.manipuladorcsv.manipuladores.PreparacaoPredicates;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class ManipuladorDespesaPublica implements Manipulador<DespesaPublica> {

    private static final Map<String, Field> FIELDS_FOR_PREDICATES;

    static {
        FIELDS_FOR_PREDICATES = new HashMap<>();
    }

    @PostConstruct
    @Override
    public void prepararManipulador() {
        if (FIELDS_FOR_PREDICATES.isEmpty()) {
            buscarFields();
        }
    }

    private void buscarFields() {
        FIELDS_FOR_PREDICATES.putAll(PreparacaoPredicates.buscarFieldsDaClasse(DespesaPublica.class));
    }

    @Override
    public List<String> getNomeFields() {
        return FIELDS_FOR_PREDICATES.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    @Override
    public List<Field> getFields() {
        return FIELDS_FOR_PREDICATES.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public Predicate<DespesaPublica> gerarPredicate(Field field, OperadorLogico operadorLogico, Object object) {
        GeradorPredicate<DespesaPublica> geradorPredicate = new GeradorPredicate<>();
        return geradorPredicate.gerarPredicate(field, operadorLogico, object);
    }
}
