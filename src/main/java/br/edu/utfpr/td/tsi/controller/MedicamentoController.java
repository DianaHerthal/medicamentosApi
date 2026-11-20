package br.edu.utfpr.td.tsi.controller;

import br.edu.utfpr.td.tsi.model.Medicamento;
import br.edu.utfpr.td.tsi.service.MedicamentoService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/medicamentos")
@CrossOrigin(origins = "*")
public class MedicamentoController {

    private static final Logger log = LoggerFactory.getLogger(MedicamentoController.class);

    private final MedicamentoService service;

    public MedicamentoController(MedicamentoService service) {
        this.service = service;
    }

    @PostMapping("/cadastrar")
    public List<Medicamento> cadastrar(@Valid @RequestBody List<Medicamento> medicamentos) {
        log.info("POST /medicamentos/cadastrar — {} medicamento(s)", medicamentos.size());
        return service.cadastrar(medicamentos);
    }

    @GetMapping
    public List<Medicamento> pesquisar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String crm,
            @RequestParam(required = false) String especialidade) {

        log.debug("GET /medicamentos?nome={}&crm={}&especialidade={}",
                nome, crm, especialidade);

        if (vazio(nome) && vazio(crm) && vazio(especialidade)) {
            return List.of();
        }

        return service.pesquisar(nome, crm, especialidade);
    }

    @GetMapping("/{id}")
    public Medicamento buscar(@PathVariable String id) {
        log.debug("GET /medicamentos/{}", id);
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Medicamento atualizar(
            @PathVariable String id,
            @RequestBody Medicamento medicamento) {
        log.info("PUT /medicamentos/{}", id);
        return service.atualizar(id, medicamento);
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable String id) {
        log.info("DELETE /medicamentos/{}", id);
        service.deletar(id);
        return "Medicamento deletado com sucesso!";
    }

    @PostMapping("/importar")
    public String uploadCSVFile(Model model) {
        

        return "file-upload-status";
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}