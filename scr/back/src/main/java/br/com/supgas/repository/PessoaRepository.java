package br.com.supgas.repository;
import br.com.supgas.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PessoaRepository extends JpaRepository<Pessoa, Integer> { }