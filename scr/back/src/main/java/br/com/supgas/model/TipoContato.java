package br.com.supgas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_contato")
public class TipoContato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipo_contato_id")
    private Integer id;

    @Column(nullable = false, length = 40, unique = true)
    private String descricao;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}