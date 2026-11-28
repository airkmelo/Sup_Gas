package br.com.supgas.repository;

import br.com.supgas.model.TipoContato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoContatoRepository extends JpaRepository<TipoContato, Integer> { }