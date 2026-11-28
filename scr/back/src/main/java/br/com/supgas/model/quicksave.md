Pessoa.java{

    package br.com.supgas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "pessoa")
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pessoa_id")
    private Integer id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(unique = true, length = 14)
    private String cpf;

    @Column(nullable = false, length = 200)
    private String endereco;

    // getters e setters
}


}