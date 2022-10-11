package br.com.manipuladorcsv.manipuladores;


import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.function.Predicate;

public class GeradorPredicate<E> implements ManipuladorPredicate<E> {

    @Override
    public Predicate<E> gerarPredicate(Field field, OperadorLogico operadorLogico, Object object) {
        return predicate -> {
            try {
                switch (operadorLogico) {
                    case IGUALDADE: return field.get(predicate).equals(object);
                    case NEGACAO: return !field.get(predicate).equals(object);
                    case MENOR: return gerarPredicateParaOperadorLogicoMenor(field, object).test(predicate);
                    case MENOR_IGUAL: return gerarPredicateParaOperadorLogicoMenorIgual(field, object).test(predicate);
                    case MAIOR: return gerarPredicateParaOperadorLogicoMaior(field, object).test(predicate);
                    case MAIOR_IGUAL: return gerarPredicateParaOperadorLogicoMaiorIgual(field, object).test(predicate);
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

            throw new RuntimeException("Geração do Predicate com Operador -> '" + OperadorLogico.MENOR.getValor() + "' não tratado!");
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

            throw new RuntimeException("Geração do Predicate com Operador -> '" + OperadorLogico.MENOR.getValor() + "' não tratado ou inválido!");
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

            throw new RuntimeException("Geração do Predicate com Operador -> '" + OperadorLogico.MENOR.getValor() + "' não tratado!");
        };
    }
}
