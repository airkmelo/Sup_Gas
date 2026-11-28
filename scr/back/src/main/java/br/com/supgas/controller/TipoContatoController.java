package br.com.supgas.controller;

import br.com.supgas.model.TipoContato;
import br.com.supgas.repository.TipoContatoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Alimenta o <select> de tipo de contato no front (telefone, email)
@RestController
@RequestMapping("/api/tipos-contato")
public class TipoContatoController {

    private final TipoContatoRepository repo;

    public TipoContatoController(TipoContatoRepository repo) { this.repo = repo; }

    @GetMapping
    public List<TipoContato> listar() { return repo.findAll(); }
}