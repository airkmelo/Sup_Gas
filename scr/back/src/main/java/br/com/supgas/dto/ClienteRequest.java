package br.com.supgas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

// O que o front ENVIA ao cadastrar/editar um cliente.
// "record" = classe imutavel com construtor e getters automaticos.
public record ClienteRequest(
        @NotBlank @Size(max = 120) String nome,
        @Size(max = 14) String cpf,
        @NotBlank @Size(max = 200) String endereco,
        @NotEmpty(message = "O cliente precisa ter pelo menos 1 contato") @Valid List<Contato> contatos
) {
    public record Contato(
            @NotNull Integer tipoContatoId,
            @NotBlank @Size(max = 120) String descricao
    ) { }
}