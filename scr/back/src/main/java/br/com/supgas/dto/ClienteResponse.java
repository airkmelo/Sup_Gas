package br.com.supgas.dto;

import java.util.List;

// O que a API DEVOLVE ao front.
public record ClienteResponse(
        Integer id,
        Integer pessoaId,
        String nome,
        String cpf,
        String endereco,
        List<ContatoResponse> contatos
) {
    public record ContatoResponse(Integer id, Integer tipoContatoId, String tipo, String descricao) { }
}