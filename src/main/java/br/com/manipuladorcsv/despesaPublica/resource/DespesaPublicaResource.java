package br.com.manipuladorcsv.despesaPublica.resource;

import br.com.manipuladorcsv.despesaPublica.service.DespesaPublicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

@RestController
@RequestMapping(path = "/despesa-publica")
public class DespesaPublicaResource {

    @Autowired
    private DespesaPublicaService despesaPublicaService;


    @GetMapping(path = "/despesas-minas-gerais")
    public ResponseEntity<Object> buscarDespesasPublicasDeMinasGerais(@RequestParam(required = false) LinkedHashMap<String, String> parametros) {
        return despesaPublicaService.buscarDespesasPublicasDeMinasGerais(parametros);
    }
}
