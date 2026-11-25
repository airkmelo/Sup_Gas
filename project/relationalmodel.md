# --Modelo relacional--

# Tabelas feitas no quadro:

01. OS_Mecanica
02. Carro
03. Produto_adquirido
04. Servicos_realizados
05. Lavação
06. Compra
07. Produto
08. Venda
09. Venda_Sorvete
10. Entrega(abastecimento)
11. Servico
12. Petshop
13. Cliente
14. Servico_id
15. Animal
16. Pessoa
17. Funcionario

modelo_relacional {
    
# [Mecanica](
01. OS_Mecanica
- id_osm: INT [PK]
- carro_id: INT [FK -> Carro.carro_id] [ foreign key (carro_id) references Carro(carro_id) ]
- cliente_id: INT [FK -> Cliente.cliente_id]
- hora_entrada: DATETIME [NOT NULL]
- hora_saida: DATETIME [NOT NULL]
- status: VARCHAR(30) [NOT NULL]

02. Carro
- carro_id: INT [PK]
- cliente_id: INT [FK -> Cliente.cliente_id]
- placa: VARCHAR(10) [UNIQUE]
- modelo: VARCHAR(100) [NOT NULL]
- cor: VARCHAR(40) [NOT NULL]

03. Produtos_Adquiridos
- id_produtos_adquiridos: INT [PK]
- id_osm: INT [FK -> OS_Mecanica.id_osm]
- produto_id: INT [FK -> Produto.id_produto]
- funcionario_id: INT [FK -> Funcionario.funcionario_id]
- quantidade: INT [NOT NULL]
- preco: DECIMAL(10,2) [NOT NULL]

04. Servicos_Realizados
- id_servico_realizado: INT [PK]
- id_osm: INT [FK -> OS_Mecanica.id_osm]
- funcionario_id: INT [FK -> Funcionario.funcionario_id]
- servico_id: INT [FK -> Serviço.servico_id]
- preco: DECIMAL(10,2) [NOT NULL]
- hora_inicio: DATETIME [NOT NULL]
- hora_fim: DATETIME [NOT NULL]

05. Serviço
- servico_id: INT [PK]
- nome_servico: VARCHAR(100) [NOT NULL]
- descricao: TEXT [NOT NULL]
- preco: DECIMAL(10,2) [NOT NULL]
- tipo_servico: VARCHAR(40) [NOT NULL]
    )
# [Lavação?](
01. Produto
- id_produto: INT [PK]
- nome: VARCHAR(100) [NOT NULL]
- preco: DECIMAL(10,2) [NOT NULL]
- qtd_atual: INT [NOT NULL]

02. Venda
- id_venda: INT [PK]
- cliente_id: INT [FK -> Cliente.cliente_id]
- carro_id: INT [FK -> Carro.carro_id, NULL]
- data: DATE [NOT NULL]
- hora: TIME [NOT NULL]
- preco_total: DECIMAL(10,2) [NOT NULL]

03. Compra (tabela associativa Venda-Produto)
- id_venda: INT [PK, FK -> Venda.id_venda]
- id_produto: INT [PK, FK -> Produto.id_produto]
- quantidade: INT [NOT NULL]
- preco_unitario: DECIMAL(10,2) [NOT NULL]
- PK composta: (id_venda, id_produto)

04. Venda_Sorvete (especialização de Venda, proposta)
- id_venda: INT [PK, FK -> Venda.id_venda]

05. Entrega (Abastecimento)
- id_entrega: INT [PK]
- produto_id: INT [FK -> Produto.id_produto]
- qtd_produto: INT [NOT NULL]
- data_entrega: DATE [NOT NULL]

06. Lavacao
- id_lavacao: INT [PK]
- cliente_id: INT [FK -> Cliente.cliente_id]
- carro_id: INT [FK -> Carro.carro_id]
- data_hora_entrega: DATETIME [NOT NULL]
- servico_id: INT [FK -> Serviço.servico_id]
- preco: DECIMAL(10,2) [NOT NULL]
    )

# [Petshop](
01. Pessoa
- pessoa_id: INT [PK]
- nome: VARCHAR(120) [NOT NULL]
- cpf: VARCHAR(14) [UNIQUE]
- telefone: VARCHAR(20) [NOT NULL]
- email: VARCHAR(120) [NOT NULL]
- endereco: VARCHAR(200) [NOT NULL]

02. Cliente
- cliente_id: INT [PK]
- pessoa_id: INT [FK -> Pessoa.pessoa_id]
- tipo_contato_id: INT [FK -> Tipo_Contato.tipo_contato_id]
- contato: VARCHAR(100) [NULL]

03. Funcionario
- funcionario_id: INT [PK]
- pessoa_id: INT [FK -> Pessoa.pessoa_id]
- comissao: DECIMAL(10,2) [DEFAULT 0]

04. Animal
- animal_id: INT [PK]
- cliente_id: INT [FK -> Cliente.cliente_id]
- nome: VARCHAR(100) [NOT NULL]
- especie: VARCHAR(50) [NOT NULL]
- raca: VARCHAR(80) [NOT NULL]
- sexo: VARCHAR(15) [NOT NULL]

05. PetShop (atendimento)
- id_atendimento: INT [PK]
- cliente_id: INT [FK -> Cliente.cliente_id]
- animal_id: INT [FK -> Animal.animal_id]
- servico_id: INT [FK -> Serviço.servico_id]
- hora_chegada: DATETIME [NOT NULL]
- hora_retirada: DATETIME [NOT NULL]
- status: VARCHAR(30) [NOT NULL]

06. Tipo_Contato
- tipo_contato_id: INT [PK]
- descricao: VARCHAR(50) [NOT NULL]
    )

}

