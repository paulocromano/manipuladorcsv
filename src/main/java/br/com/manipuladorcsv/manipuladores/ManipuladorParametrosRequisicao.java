package br.com.manipuladorcsv.manipuladores;

import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class ManipuladorParametrosRequisicao<E> {

    @Getter
    private Optional<Predicate<E>> predicateFilterOptional;
    private Manipulador<E> manipulador;

    private static final String SEPARADOR_CONDICIONAIS = "_";
    private static final String ATRIBUICAO = "=";


    public ManipuladorParametrosRequisicao(Manipulador<E> manipulador) {
        this.manipulador = manipulador;
        this.predicateFilterOptional = Optional.empty();
    }

    public void formatarParametrosDaRequisicao(Map<String, String> parametros) {
        for (TipoParametro tipoParametro : TipoParametro.values()) {
            if (parametros.containsKey(tipoParametro.getValor())) {
                String condicionais = parametros.get(tipoParametro.getValor());

                switch (tipoParametro) {
                    case FILTER: {
                        separarCondicionais(condicionais);
                        break;
                    }
                    default: throw new RuntimeException("Não tratado!");
                }

            }
        }
    }


    private void separarCondicionais(String condicionais) {
        String[] expressoesCompletas = condicionais.split(SEPARADOR_CONDICIONAIS);

        for (String expressaoCompleta : expressoesCompletas) {
            Optional<OperadorLogico> optionalOperadorLogico = buscarOperadorLogicoDaExpressao(expressaoCompleta);

            if (optionalOperadorLogico.isPresent()) {
                String[] partesExpressoes = expressaoCompleta.split(optionalOperadorLogico.get().getValor());

                for (String parteExpressao : partesExpressoes) {
                    if (!parteExpressao.contains(ATRIBUICAO)) throw new RuntimeException("Erro de sintaxe! Falta o token  de Atribuição -> " + ATRIBUICAO);
                    if (parteExpressao.split(ATRIBUICAO).length == 0) throw new RuntimeException("A expressão não contem uma chave e valor");

                    String nomeFieldRequisicao = parteExpressao.split(ATRIBUICAO)[0];
                    Optional<Field> fieldParametroRequisicaoOptional = filtrarFieldRequisicaoReferenteAoFieldDaClasse(nomeFieldRequisicao);

                    if (fieldParametroRequisicaoOptional.isPresent()) {
                        if (parteExpressao.split(ATRIBUICAO).length == 1) throw new RuntimeException("O campo '" +  nomeFieldRequisicao + "' não possui um valor!");

                        gerarValorDaExpressaoReferenteAoTipoDoFieldDaClasse(fieldParametroRequisicaoOptional,
                                parteExpressao, optionalOperadorLogico.get());
                    }
                }
            }
        }
    }

    private Optional<OperadorLogico> buscarOperadorLogicoDaExpressao(String expressaoCompleta) {
        final List<OperadorLogico> OPERADORES_LOGICOS = Arrays.stream(OperadorLogico.values()).collect(Collectors.toList());

        return OPERADORES_LOGICOS
                .stream()
                .filter(operadorLogico -> expressaoCompleta.contains(operadorLogico.getValor()))
                .findFirst();
    }

    private Optional<Field> filtrarFieldRequisicaoReferenteAoFieldDaClasse(String nomeFieldRequisicao) {
        return manipulador.getFields()
                .stream()
                .filter(field -> field.getName().equals(nomeFieldRequisicao))
                .findFirst();
    }

    private void gerarValorDaExpressaoReferenteAoTipoDoFieldDaClasse(Optional<Field> fieldParametroRequisicaoOptional,
               String parteExpressao, OperadorLogico operadorLogico) {

        Field fieldParametroRequisicao = fieldParametroRequisicaoOptional.get();
        String valor = parteExpressao.split(ATRIBUICAO)[1];
        Class<?> fieldClass = fieldParametroRequisicao.getType();
        Optional<Object> valorObjectRequisicaoOptional = Optional.empty();

        if (fieldClass.equals(Integer.class)) valorObjectRequisicaoOptional = Optional.of(Integer.valueOf(valor));
        if (fieldClass.equals(Long.class)) valorObjectRequisicaoOptional = Optional.of(Long.valueOf(valor));
        if (fieldClass.equals(Float.class)) valorObjectRequisicaoOptional = Optional.of(Float.valueOf(valor));
        if (fieldClass.equals(Double.class)) valorObjectRequisicaoOptional = Optional.of(Double.valueOf(valor));
        else if (fieldClass.equals(String.class)) valorObjectRequisicaoOptional = Optional.of(valor);

        if (valorObjectRequisicaoOptional.isEmpty()) throw new RuntimeException("Tipo de Classe não tratada para geração do Predicate!");

        predicateFilterOptional = Optional.of(manipulador.gerarPredicate(fieldParametroRequisicao,
                operadorLogico, valorObjectRequisicaoOptional.get()));
    }
}
