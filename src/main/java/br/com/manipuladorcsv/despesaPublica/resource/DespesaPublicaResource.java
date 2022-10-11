package br.com.manipuladorcsv.despesaPublica.resource;

import br.com.manipuladorcsv.despesaPublica.model.DespesaPublica;
import br.com.manipuladorcsv.despesaPublica.service.DespesaPublicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/despesa-publica")
public class DespesaPublicaResource {

    @Autowired
    private DespesaPublicaService despesaPublicaService;


    @GetMapping(path = "/despesas-minas-gerais")
    public ResponseEntity<List<DespesaPublica>> buscarDespesasPublicasDeMinasGerais(@RequestParam(required = false) Map<String, String> parametros) {
        return despesaPublicaService.buscarDespesasPublicasDeMinasGerais(parametros);
    }

    @GetMapping
    public ResponseEntity<Void> teste(@RequestParam Map<String, String> parametros) {
        parametros.forEach((a, b) -> System.out.println(a + " -> " + b));
        System.out.println(Arrays.toString("nbjisdji!!sdasd".split("!!")));
        return ResponseEntity.ok().build();
    }
}
