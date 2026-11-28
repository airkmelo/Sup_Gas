package br.com.supgas.controller;

import org.springframework.web.bind.annotation.RestController;
import br.com.supgas.model.Pessoa;
import br.com.supgas.repository.PessoaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;


@RestController
@RequestMapping("/api/pessoas")
@CrossOrigin // libera o front em outra porta durante o desenvolvimento
public class PessoaController {
    private final PessoaRepository repo;

    public PessoaController(PessoaRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Pessoa> listar() { return repo.findAll(); }

    @PostMapping
    public Pessoa criar(@RequestBody Pessoa p) { return repo.save(p); }
}