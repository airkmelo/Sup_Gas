package br.com.supgas.repository;

import br.com.supgas.model.ContatoPessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ContatoPessoaRepository extends JpaRepository<ContatoPessoa, Integer> {

    // O Spring le o NOME do metodo e gera o SQL:
    // select ... from contato_pessoa where pessoa_id = ?
    List<ContatoPessoa> findByPessoaId(Integer pessoaId);

    // ... where pessoa_id in (?, ?, ?)
    List<ContatoPessoa> findByPessoaIdIn(Collection<Integer> pessoaIds);

    // DELETE executado na hora (um "delete" derivado so rodaria no fim da transacao,
    // e o INSERT dos novos contatos poderia bater na restricao UNIQUE)
    @Modifying(flushAutomatically = true)
    @Query("delete from ContatoPessoa c where c.pessoa.id = :pessoaId")
    void removerPorPessoaId(@Param("pessoaId") Integer pessoaId);
}