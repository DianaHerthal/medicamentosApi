package br.edu.utfpr.td.tsi.exception;

public class MedicamentoNaoEncontradoException extends RuntimeException {

    public MedicamentoNaoEncontradoException() {
        super("Medicamento não encontrado.");
    }

    public MedicamentoNaoEncontradoException(String id) {
        super("Medicamento não encontrado com ID: " + id);
    }
}