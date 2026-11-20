package br.edu.utfpr.td.tsi.repository;

import br.edu.utfpr.td.tsi.model.Medicamento;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentoRepository extends MongoRepository<Medicamento, String> {
    Optional<Medicamento> findByNomeMedicamentoIgnoreCase(String nomeMedicamento);

}