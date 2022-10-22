package br.com.manipuladorcsv.manipuladores.parametros;

import br.com.manipuladorcsv.manipuladores.enums.ModoOrdenacao;
import br.com.manipuladorcsv.manipuladores.interfaces.ManipuladorParametroRequest;
import br.com.manipuladorcsv.manipuladores.model.OrdenacaoField;
import br.com.manipuladorcsv.manipuladores.operacoes.ManipuladorComparator;
import br.com.manipuladorcsv.manipuladores.operacoes.ManipuladorComparatorImpl;

import java.lang.reflect.Field;
import java.util.*;

public class OrderByParametroRequest<E> implements ParametroRequest<E, Comparator<Object>> {

    private ManipuladorParametroRequest<E> manipuladorParametroRequest;
    private String conteudoParametro;
    private ManipuladorComparator manipuladorComparator;

    private static final String ATRIBUICAO = "=";
    private static final String SEPARADOR_EXPRESSOES = ";";


    public OrderByParametroRequest() {
        this.manipuladorComparator = new ManipuladorComparatorImpl();
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
    public Comparator<Object> getOperacao() {
        Objects.requireNonNull(manipuladorParametroRequest);
        Objects.requireNonNull(conteudoParametro);
        Optional<ModoOrdenacao> modoOrdenacaoOptional = conteudoParametroPossuiApenasModoDeOrdenacao();

        if (modoOrdenacaoOptional.isPresent())
            return this.manipuladorComparator.getComparatorDepoisDaOperacaoMap(modoOrdenacaoOptional.get());

        List<OrdenacaoField> ordenacaoFields = gerarComparatorQuandoConteudoDoParametroConterFields();
        return this.manipuladorComparator.getComparatorAntesDaOperacaoMap(ordenacaoFields);
    }

    private Optional<ModoOrdenacao> conteudoParametroPossuiApenasModoDeOrdenacao() {
        return Arrays.stream(ModoOrdenacao.values())
                .filter(modoOrdenacao -> modoOrdenacao.getValor().equals(this.conteudoParametro))
                .findFirst();
    }

    private List<OrdenacaoField> gerarComparatorQuandoConteudoDoParametroConterFields() {
        List<OrdenacaoField> ordenacaoFields = new ArrayList<>();

        for (String expressao : this.conteudoParametro.split(SEPARADOR_EXPRESSOES)) {
            String[] partesDaExpressao = expressao.split(ATRIBUICAO);
            Field field = validarField(partesDaExpressao[0]);
            ModoOrdenacao modoOrdenacao = validarModoOrdenacao(partesDaExpressao);
            ordenacaoFields.add(new OrdenacaoField(field, modoOrdenacao));
        }

        return ordenacaoFields;
    }

    private Field validarField(String nomeField) {
        final Optional<Field> fieldOptional = this.manipuladorParametroRequest.getFields()
                .stream()
                .filter(field -> field.getName().equals(nomeField))
                .findFirst();

        if (fieldOptional.isEmpty()) throw new RuntimeException("Nome do field não encontrado -> " + nomeField);

        return fieldOptional.get();
    }

    private ModoOrdenacao validarModoOrdenacao(String[] partesDaExpressao) {
        if (partesDaExpressao.length < 2) throw new IllegalArgumentException("A expressão contem sintáxe inválida -> " + Arrays.toString(partesDaExpressao));

        String modoOrdenacaoParametro = partesDaExpressao[1];

        Optional<ModoOrdenacao> modoOrdenacaoOptional = Arrays.stream(ModoOrdenacao.values())
                .filter(modoOrdenacao -> modoOrdenacao.getValor().equals(modoOrdenacaoParametro))
                .findFirst();

        if (modoOrdenacaoOptional.isEmpty()) throw new IllegalArgumentException("Modo de ordenação inválido! -> " + modoOrdenacaoParametro);
        return modoOrdenacaoOptional.get();
    }
}
