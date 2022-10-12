package br.com.manipuladorcsv.manipuladores.parametros;

import br.com.manipuladorcsv.manipuladores.operacoes.ManipuladorPredicateImpl;
import br.com.manipuladorcsv.manipuladores.enums.OperadorLogico;
import br.com.manipuladorcsv.manipuladores.interfaces.ManipuladorParametroRequest;
import br.com.manipuladorcsv.manipuladores.operacoes.ManipuladorPredicate;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterParametroRequest<E> implements ParametroRequest<E, Predicate<E>> {

    private ManipuladorParametroRequest<E> manipuladorParametroRequest;

    private ManipuladorPredicate<E> manipuladorPredicate;
    private String conteudoParametro;

    private static final String ATRIBUICAO = "=";

    public FilterParametroRequest() {
        this.manipuladorPredicate = new ManipuladorPredicateImpl<>();
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
    public Predicate<E> getOperacao() {
        Objects.requireNonNull(manipuladorParametroRequest);
        Objects.requireNonNull(conteudoParametro);
        boolean expressaoContemOperadorLogicoAndOr = expressaoContemOperadorLogicoAndOr(conteudoParametro);

        return expressaoContemOperadorLogicoAndOr
                ? gerarPredicateQuandoHouverOperadorLogicoAndOr(conteudoParametro).get()
                : construirPredicateDeAcordoComParametrosDaRequisicao(conteudoParametro).get();
    }

    private boolean expressaoContemOperadorLogicoAndOr(String expressaoCompleta) {
        return expressaoCompleta.contains(OperadorLogico.AND.getValor()) || expressaoCompleta.contains(OperadorLogico.OR.getValor());
    }

    private Optional<Predicate<E>> gerarPredicateQuandoHouverOperadorLogicoAndOr(String expressaoCompleta) {
        String[] expressaoSeparadaPorAndOr = expressaoCompleta.split(OperadorLogico.AND.getValor() + "|" + OperadorLogico.OR.getValor());
        List<OperadorLogico> operadoresLogicosAndOr = buscarOperadoresLogicosAndOrDaExpressao(expressaoCompleta, expressaoSeparadaPorAndOr);

        if (expressaoSeparadaPorAndOr.length - 1 != operadoresLogicosAndOr.size()) throw new RuntimeException("A expressão com Operadores AND e/ou OR é inválida!");

        Predicate<E> predicateExpressaoAnterior;
        Optional<Predicate<E>> predicateCompletoOptional = Optional.empty();

        for (int index = 1; index < expressaoSeparadaPorAndOr.length; index++) {
            predicateExpressaoAnterior = construirPredicateDeAcordoComParametrosDaRequisicao(expressaoSeparadaPorAndOr[index - 1]).get();
            Predicate<E> predicateExpressaoAtual = construirPredicateDeAcordoComParametrosDaRequisicao(expressaoSeparadaPorAndOr[index]).get();
            OperadorLogico operadorLogico = operadoresLogicosAndOr.get(index - 1);

            switch (operadorLogico) {
                case AND: {
                    predicateCompletoOptional = Optional.of(predicateCompletoOptional.isEmpty()
                            ? predicateExpressaoAnterior.and(predicateExpressaoAtual)
                            : predicateCompletoOptional.get().and(predicateExpressaoAnterior));
                    break;
                }
                case OR: {
                    predicateCompletoOptional = Optional.of(predicateCompletoOptional.isEmpty()
                            ? predicateExpressaoAnterior.or(predicateExpressaoAtual)
                            : predicateCompletoOptional.get().and(predicateExpressaoAnterior));
                    break;
                }
                default: throw new RuntimeException("Operador lógico não permitido nesta etapa do processamento!");
            }
        }

        return predicateCompletoOptional;
    }

    private List<OperadorLogico> buscarOperadoresLogicosAndOrDaExpressao(String expressaoCompleta, String[] expressaoSeparadaPorAndOr) {
        List<OperadorLogico> operadoresLogicosAndOr = new ArrayList<>();
        int inicioSubstring = expressaoSeparadaPorAndOr[0].length();
        int fimSubstring = expressaoSeparadaPorAndOr[0].length() + 2;

        for (int index = 0; index < expressaoSeparadaPorAndOr.length - 1; index++) {
            String operadorLogico = expressaoCompleta.substring(inicioSubstring, fimSubstring);
            inicioSubstring += expressaoSeparadaPorAndOr[index + 1].length() + 2;
            fimSubstring = inicioSubstring + 2;

            if (operadorLogico.equals(OperadorLogico.AND.getValor())) operadoresLogicosAndOr.add(OperadorLogico.AND);
            else if (operadorLogico.equals(OperadorLogico.OR.getValor())) operadoresLogicosAndOr.add(OperadorLogico.OR);
        }

        return operadoresLogicosAndOr;
    }

    private Optional<Predicate<E>> construirPredicateDeAcordoComParametrosDaRequisicao(String expressaoCompleta) {
        Optional<OperadorLogico> optionalOperadorLogicoExcetoAndOr = buscarOperadorLogicoDaExpressaoExcetoAndOr(expressaoCompleta);

        if (optionalOperadorLogicoExcetoAndOr.isPresent()) {
            String[] partesExpressoes = expressaoCompleta.split(optionalOperadorLogicoExcetoAndOr.get().getValor());

            for (String parteExpressao : partesExpressoes) {
                if (!parteExpressao.contains(ATRIBUICAO)) throw new RuntimeException("Erro de sintaxe! Falta o token de Atribuição -> " + ATRIBUICAO);
                if (parteExpressao.split(ATRIBUICAO).length == 0) throw new RuntimeException("A expressão não contem uma chave e valor");

                String nomeFieldRequisicao = parteExpressao.split(ATRIBUICAO)[0];
                Optional<Field> fieldParametroRequisicaoOptional = filtrarFieldRequisicaoReferenteAoFieldDaClasse(nomeFieldRequisicao);

                if (fieldParametroRequisicaoOptional.isPresent()) {
                    if (parteExpressao.split(ATRIBUICAO).length == 1) throw new RuntimeException("O campo '" +  nomeFieldRequisicao + "' não possui um valor!");

                    return gerarValorDaExpressaoReferenteAoTipoDoFieldDaClasse(fieldParametroRequisicaoOptional,
                            parteExpressao, optionalOperadorLogicoExcetoAndOr.get());
                }
            }
        }

        throw new RuntimeException("Operador lógico não encontrado!");
    }

    private Optional<OperadorLogico> buscarOperadorLogicoDaExpressaoExcetoAndOr(String expressaoCompleta) {
        final List<OperadorLogico> OPERADORES_LOGICOS_RESTRINGIDOS = Arrays.asList(OperadorLogico.AND, OperadorLogico.OR);
        final List<OperadorLogico> OPERADORES_LOGICOS = Arrays.stream(OperadorLogico.values())
                .filter(operadorLogico -> !OPERADORES_LOGICOS_RESTRINGIDOS.contains(operadorLogico))
                .collect(Collectors.toList());

        return OPERADORES_LOGICOS
                .stream()
                .filter(operadorLogico -> expressaoCompleta.contains(operadorLogico.getValor()))
                .findFirst();
    }

    private Optional<Field> filtrarFieldRequisicaoReferenteAoFieldDaClasse(String nomeFieldRequisicao) {
        return manipuladorParametroRequest.getFields()
                .stream()
                .filter(field -> field.getName().equals(nomeFieldRequisicao))
                .findFirst();
    }

    private Optional<Predicate<E>> gerarValorDaExpressaoReferenteAoTipoDoFieldDaClasse(Optional<Field> fieldParametroRequisicaoOptional,
                                                                                       String parteExpressao, OperadorLogico operadorLogico) {

        Field fieldParametroRequisicao = fieldParametroRequisicaoOptional.get();
        String valorFieldDaRequisicao = parteExpressao.split(ATRIBUICAO)[1];
        Class<?> fieldClass = fieldParametroRequisicao.getType();
        Object valorObjectRequisicao = identificarClasseDoField(fieldClass, valorFieldDaRequisicao);

        return Optional.of(manipuladorPredicate.gerarPredicate(fieldParametroRequisicao,
                operadorLogico, valorObjectRequisicao));
    }

    private Object identificarClasseDoField(Class<?> fieldClass, String valorFieldDaRequisicao) {
        if (fieldClass.equals(Integer.class)) return Integer.valueOf(valorFieldDaRequisicao);
        else if (fieldClass.equals(Long.class)) return Long.valueOf(valorFieldDaRequisicao);
        else if (fieldClass.equals(Float.class)) return Float.valueOf(valorFieldDaRequisicao);
        else if (fieldClass.equals(Double.class)) return Double.valueOf(valorFieldDaRequisicao);
        else if (fieldClass.equals(String.class)) return valorFieldDaRequisicao;

        throw new RuntimeException("Tipo de Classe não tratada para geração do Predicate!");
    }
}
