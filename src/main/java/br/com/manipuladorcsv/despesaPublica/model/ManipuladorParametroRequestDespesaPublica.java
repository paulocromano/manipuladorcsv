package br.com.manipuladorcsv.despesaPublica.model;

import br.com.manipuladorcsv.manipuladores.utils.ClassUtils;
import br.com.manipuladorcsv.manipuladores.interfaces.ManipuladorParametroRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ManipuladorParametroRequestDespesaPublica implements ManipuladorParametroRequest<DespesaPublica> {

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
        FIELDS_FOR_PREDICATES.putAll(ClassUtils.buscarFieldsDaClasse(DespesaPublica.class));
    }

    @Override
    public List<String> getNomeFields() {
        return FIELDS_FOR_PREDICATES.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    @Override
    public List<Field> getFields() {
        return FIELDS_FOR_PREDICATES.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }
}
