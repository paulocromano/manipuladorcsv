package br.com.manipuladorcsv.despesaPublica.service;

import br.com.manipuladorcsv.despesaPublica.model.DespesaPublica;
import br.com.manipuladorcsv.despesaPublica.model.ManipuladorParametroRequestDespesaPublica;
import br.com.manipuladorcsv.despesaPublica.request.BuscaDepesasPublicasService;
import br.com.manipuladorcsv.manipuladores.GeradorOperacoesRequisicao;
import br.com.manipuladorcsv.manipuladores.enums.TipoParametro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class DespesaPublicaService {

    @Value("${url.servico.despesa.publica}")
    private String urlServicoDespesaPublica;

    @Autowired
    private ManipuladorParametroRequestDespesaPublica manipuladorDespesaPublica;


    public ResponseEntity<?> buscarDespesasPublicasDeMinasGerais(Map<String, String> parametros) {
        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl(urlServicoDespesaPublica)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

        BuscaDepesasPublicasService buscaDepesasPublicasService = retrofit.create(BuscaDepesasPublicasService.class);
        Call<List<DespesaPublica>> callDespesasPublicas = buscaDepesasPublicasService.buscarDespesasPublicasDeMinasGerais();

        try {
            Response<List<DespesaPublica>> responseBuscarDespesasPublicasDeMinasGerais = callDespesasPublicas.execute();

            if (responseBuscarDespesasPublicasDeMinasGerais.isSuccessful()) {
                List<DespesaPublica> despesasPublicas = responseBuscarDespesasPublicasDeMinasGerais.body();
                Object response = despesasPublicas;

                if (!parametros.isEmpty()) {
                    GeradorOperacoesRequisicao<DespesaPublica> geradorOperacoesRequisicao = new GeradorOperacoesRequisicao(manipuladorDespesaPublica);
                    geradorOperacoesRequisicao.gerarOperacoesDaRequisicaoConformeParametros(parametros);
                    //Predicate<DespesaPublica> despesaPublicaPredicate = (Predicate<DespesaPublica>) geradorOperacoesRequisicao.getParametrosRequest().get(TipoParametro.FILTER).getOperacao();
                    Function<DespesaPublica, Object> despesaPublicaFunction = (Function<DespesaPublica, Object>) geradorOperacoesRequisicao.getParametrosRequest().get(TipoParametro.MAP).getOperacao();

                    response = despesasPublicas.stream()
                            .map(despesaPublicaFunction)
                            .peek(System.out::println)
                            .collect(Collectors.toList());
                }

                return ResponseEntity.ok(response);
            }

            throw new RuntimeException("A requisição pela busca das despesas de Minas Gerais falhou!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
