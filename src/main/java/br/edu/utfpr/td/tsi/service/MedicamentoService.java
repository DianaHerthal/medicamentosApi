package br.edu.utfpr.td.tsi.service;

import br.edu.utfpr.td.tsi.exception.MedicamentoNaoEncontradoException;
import br.edu.utfpr.td.tsi.model.Medicamento;
import br.edu.utfpr.td.tsi.repository.MedicamentoRepository;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class MedicamentoService {

    private static final Logger log = LoggerFactory.getLogger(MedicamentoService.class);

    private final MedicamentoRepository repository;
    private final MongoTemplate mongoTemplate;

    public MedicamentoService(MedicamentoRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }


    public List<Medicamento> cadastrar(List<Medicamento> medicamentos) {
        if (medicamentos == null || medicamentos.isEmpty()) {
            throw new IllegalArgumentException("Nenhum medicamento informado.");
        }

        log.debug("Recebido pedido de cadastro para {} medicamento(s)", medicamentos.size());

        for (Medicamento medicamento : medicamentos) {
             medicamento.setId(null);

            List<Medicamento> existente = repository.findByNomeMedicamentoContainingIgnoreCase(
                    medicamento.getNomeMedicamento());
            if (!existente.isEmpty()) {
                log.warn("Nome duplicado: {}", medicamento.getNomeMedicamento());
                throw new IllegalArgumentException(
                        "Nome " + medicamento.getNomeMedicamento() + " já cadastrado.");
            }
        }

        List<Medicamento> salvos = repository.saveAll(medicamentos);
        log.info("Cadastrados {} medicamento(s) com sucesso", salvos.size());
        return salvos;
    }

    public List<Medicamento> pesquisar(String nome) {
        List<Criteria> filtros = new ArrayList<>();

        if (temValor(nome)) {
            String termo = nome.trim().toUpperCase();
            filtros.add(Criteria.where("NOME_PRODUTO").regex(Pattern.quote(termo), "i"));
        }

        if (filtros.isEmpty()) {
            log.debug("Pesquisa sem filtros — retornando lista vazia");
            return List.of();
        }

        Criteria criteria = new Criteria().andOperator(filtros.toArray(new Criteria[0]));
        List<Medicamento> resultado = mongoTemplate.find(new Query(criteria), Medicamento.class);

        log.debug("Pesquisa [nome={}] → {} resultado(s)",
                nome, resultado.size());

        return resultado;
    }

    public Medicamento buscarPorId(String id) {
        log.debug("Buscando medicamento por ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Medicamento não encontrado: {}", id);
                    return new MedicamentoNaoEncontradoException(id);
                });
    }

    public Medicamento atualizar(String id, Medicamento novosDados) {
        log.debug("Atualizando medicamento ID: {}", id);

        if (repository.existsById(id)) {
            return repository.save(novosDados);
        } else {
            throw new MedicamentoNaoEncontradoException(id);
        }
    }

    public void deletar(String id) {
        log.debug("Deletando médico ID: {}", id);
        Medicamento medicamento = buscarPorId(id);
        repository.delete(medicamento);
        log.info("Médico deletado: {} ({})", medicamento.getNomeMedicamento(), id);
    }

    public long contar() {
        return repository.count();
    }

    public void importarArquivo(String nomeArquivo) {
        Path filePath = Paths.get("src/main/resources", nomeArquivo);

        if (!Files.exists(filePath)) {
            log.error("Arquivo não encontrado: {}", filePath.toAbsolutePath());
            return;
        }

        try (Reader reader = new BufferedReader(new FileReader(filePath.toFile(), StandardCharsets.ISO_8859_1))) {

            CsvToBean<Medicamento> csvToBean = new CsvToBeanBuilder<Medicamento>(reader)
                    .withType(Medicamento.class)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Medicamento> medicamentosLidos = csvToBean.parse();
            log.info("Lidos {} registros do arquivo {}", medicamentosLidos.size(), nomeArquivo);

            List<Medicamento> paraSalvar = new ArrayList<>();
            java.util.Set<String> nomesJaProcessados = new java.util.HashSet<>();

            for (Medicamento med : medicamentosLidos) {
                if (!temValor(med.getNomeMedicamento())) {
                    continue;
                }

                String nomeNormalizado = med.getNomeMedicamento().trim().toUpperCase();
                
                if (!nomesJaProcessados.contains(nomeNormalizado) 
                        && repository.findByNomeMedicamentoContainingIgnoreCase(nomeNormalizado).isEmpty()) {
                    
                    med.setId(null);
                    nomesJaProcessados.add(nomeNormalizado);
                    paraSalvar.add(med);
                }
            }

            if (!paraSalvar.isEmpty()) {
                repository.saveAll(paraSalvar);
                log.info("Importação concluída: {} novos medicamentos salvos no MongoDB.", paraSalvar.size());
            } else {
                log.info("Nenhum medicamento novo para importar.");
            }

        } catch (Exception ex) {
            log.error("Erro ao processar o CSV {}: {}", nomeArquivo, ex.getMessage(), ex);
        }
    }

    private boolean temValor(String valor) {
        return valor != null && !valor.isBlank();
    }

}