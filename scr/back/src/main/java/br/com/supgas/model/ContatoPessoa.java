package br.com.supgas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contato_pessoa")
public class ContatoPessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contato_id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_contato_id", nullable = false)
    private TipoContato tipoContato;

    @Column(nullable = false, length = 120)
    private String descricao;

    // O JPA exige construtor sem argumentos
    public ContatoPessoa() { }

    public ContatoPessoa(Pessoa pessoa, TipoContato tipoContato, String descricao) {
        this.pessoa = pessoa;
        this.tipoContato = tipoContato;
        this.descricao = descricao;
    }

    public Integer getId() { return id; }
    public Pessoa getPessoa() { return pessoa; }
    public TipoContato getTipoContato() { return tipoContato; }
    public String getDescricao() { return descricao; }
}