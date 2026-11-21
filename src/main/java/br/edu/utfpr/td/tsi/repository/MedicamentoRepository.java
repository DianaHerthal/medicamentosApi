package br.edu.utfpr.td.tsi.repository;

import br.edu.utfpr.td.tsi.model.Medicamento;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentoRepository extends MongoRepository<Medicamento, String> {
    List<Medicamento> findByNomeMedicamentoContainingIgnoreCase(String nomeMedicamento);
}