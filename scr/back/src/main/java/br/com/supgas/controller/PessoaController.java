package br.com.supgas.controller;

import org.springframework.web.bind.annotation.RestController;

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