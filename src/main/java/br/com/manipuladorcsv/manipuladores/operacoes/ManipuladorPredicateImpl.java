package br.com.manipuladorcsv.manipuladores.operacoes;


import br.com.manipuladorcsv.manipuladores.enums.OperadorLogico;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.function.Predicate;

public class ManipuladorPredicateImpl<E> implements ManipuladorPredicate<E> {

    @Override
    public Predicate<E> gerarPredicate(Field field, OperadorLogico operadorLogico, Object object) {
        return objectPredicate -> {
            try {
                switch (operadorLogico) {
                    case IGUALDADE: return field.get(objectPredicate).equals(object);
                    case NEGACAO: return !field.get(objectPredicate).equals(object);
                    case MENOR: return gerarPredicateParaOperadorLogicoMenor(field, object).test(objectPredicate);
                    case MENOR_IGUAL: return gerarPredicateParaOperadorLogicoMenorIgual(field, object).test(objectPredicate);
                    case MAIOR: return gerarPredicateParaOperadorLogicoMaior(field, object).test(objectPredicate);
                    case MAIOR_IGUAL: return gerarPredicateParaOperadorLogicoMaiorIgual(field, object).test(objectPredicate);
                    default: throw new RuntimeException("Operador lógico não tratado -> " + operadorLogico);
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Predicate<E> gerarPredicateParaOperadorLogicoMenor(Field field, Object object) {
        return predicate -> {
            try {
                if (field.getType().getSuperclass().equals(Number.class)) {
                    if (field.getType().equals(Float.class)) return ((Float) field.get(predicate)) < ((Float) object);
                    if (field.getType().equals(Double.class)) return ((Double) field.get(predicate)) < ((Double) object);
                    if (field.getType().equals(Integer.class)) return ((Integer) field.get(predicate)) < ((Integer) object);
                    if (field.getType().equals(Long.class)) return ((Long) field.get(predicate)) < ((Long) object);
                }
                else if (field.getType().getSuperclass().equals(Temporal.class)) {
                    if (field.getType().equals(LocalDate.class)) return ((LocalDate) field.get(predicate)).isBefore((LocalDate) object);
                    if (field.getType().equals(LocalTime.class)) return ((LocalTime) field.get(predicate)).isBefore((LocalTime) object);
                    if (field.getType().equals(LocalDateTime.class)) return ((LocalDateTime) field.get(predicate)).isBefore((LocalDateTime) object);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            throw new RuntimeException("Geração do Predicate com Operador -> '" + OperadorLogico.MENOR.getValor() + "' não tratado ou inválido!");
        };
    }

    private Predicate<E> gerarPredicateParaOperadorLogicoMenorIgual(Field field, Object object) {
        return predicate -> {
            try {
                if (field.getType().getSuperclass().equals(Number.class)) {
                    if (field.getType().equals(Float.class)) return !(((Float) field.get(predicate)) > ((Float) object));
                    if (field.getType().equals(Double.class)) return !(((Double) field.get(predicate)) > ((Double) object));
                    if (field.getType().equals(Integer.class)) return !(((Integer) field.get(predicate)) > ((Integer) object));
                    if (field.getType().equals(Long.class)) return !(((Long) field.get(predicate)) > ((Long) object));
                }
                else if (field.getType().getSuperclass().equals(Temporal.class)) {
                    if (field.getType().equals(LocalDate.class)) return !((LocalDate) field.get(predicate)).isAfter((LocalDate) object);
                    if (field.getType().equals(LocalTime.class)) return !((LocalTime) field.get(predicate)).isAfter((LocalTime) object);
                    if (field.getType().equals(LocalDateTime.class)) return !((LocalDateTime) field.get(predicate)).isAfter((LocalDateTime) object);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            throw new RuntimeException("Geração do Predicate com Operador -> '" + OperadorLogico.MENOR_IGUAL.getValor() + "' não tratado!");
        };
    }

    private Predicate<E> gerarPredicateParaOperadorLogicoMaior(Field field, Object object) {
        return predicate -> {
            try {
                if (field.getType().getSuperclass().equals(Number.class)) {
                    if (field.getType().equals(Float.class)) return ((Float) field.get(predicate)) > ((Float) object);
                    if (field.getType().equals(Double.class)) return ((Double) field.get(predicate)) > ((Double) object);
                    if (field.getType().equals(Integer.class)) return ((Integer) field.get(predicate)) > ((Integer) object);
                    if (field.getType().equals(Long.class)) return ((Long) field.get(predicate)) > ((Long) object);
                }
                else if (field.getType().getSuperclass().equals(Temporal.class)) {
                    if (field.getType().equals(LocalDate.class)) return ((LocalDate) field.get(predicate)).isAfter((LocalDate) object);
                    if (field.getType().equals(LocalTime.class)) return ((LocalTime) field.get(predicate)).isAfter((LocalTime) object);
                    if (field.getType().equals(LocalDateTime.class)) return ((LocalDateTime) field.get(predicate)).isAfter((LocalDateTime) object);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            throw new RuntimeException("Geração do Predicate com Operador -> '" + OperadorLogico.MAIOR.getValor() + "' não tratado ou inválido!");
        };
    }

    private Predicate<E> gerarPredicateParaOperadorLogicoMaiorIgual(Field field, Object object) {
        return predicate -> {
            try {
                if (field.getType().getSuperclass().equals(Number.class)) {
                    if (field.getType().equals(Float.class)) return !(((Float) field.get(predicate)) < ((Float) object));
                    if (field.getType().equals(Double.class)) return !(((Double) field.get(predicate)) < ((Double) object));
                    if (field.getType().equals(Integer.class)) return !(((Integer) field.get(predicate)) < ((Integer) object));
                    if (field.getType().equals(Long.class)) return !(((Long) field.get(predicate)) < ((Long) object));
                }
                else if (field.getType().getSuperclass().equals(Temporal.class)) {
                    if (field.getType().equals(LocalDate.class)) return !((LocalDate) field.get(predicate)).isBefore((LocalDate) object);
                    if (field.getType().equals(LocalTime.class)) return !((LocalTime) field.get(predicate)).isBefore((LocalTime) object);
                    if (field.getType().equals(LocalDateTime.class)) return !((LocalDateTime) field.get(predicate)).isBefore((LocalDateTime) object);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            throw new RuntimeException("Geração do Predicate com Operador -> '" + OperadorLogico.MAIOR_IGUAL.getValor() + "' não tratado!");
        };
    }
}
