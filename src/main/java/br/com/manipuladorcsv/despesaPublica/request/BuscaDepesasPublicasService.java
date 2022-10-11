package br.com.manipuladorcsv.despesaPublica.request;

import br.com.manipuladorcsv.despesaPublica.model.DespesaPublica;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface BuscaDepesasPublicasService {

    @GET("/despesa-publica/despesas-minas-gerais")
    public Call<List<DespesaPublica>> buscarDespesasPublicasDeMinasGerais();
}
