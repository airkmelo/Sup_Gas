

mr {

    MINIMUNDO & Descrição Relacional

O sistema de lavação 2026 permite o controle de uma lavação de carros. Uma lavação ocorre quando um carro é levado por um cliente para ser lavado. A lavação tem uma hora prevista para entrega do carro lavado.

Sobre um carro serão registrados no sistema a placa, o modelo e a cor. Todo carro está vinculado a um cliente, que é o responsável por aquele carro. 

01. Lavacao
- carro_id
- cliente_id
- data_hora_entrega DateTime

02. Carro
- carro_id: INT [PK]
- cliente_id: INT [FK -> Cliente.cliente_id]
- placa: VARCHAR(10) [UNIQUE]
- modelo: VARCHAR(100) [NOT NULL]
- cor: VARCHAR(40) [NOT NULL]

Sobre um cliente serão registrados o nome e alguns contatos. Um contato pode ser email ou telefone. Um cliente pode ter um ou mais contatos cadastrados, mas precisa ter pelo menos 1 contato cadastrado.

03. Cliente
- cliente_id: INT [PK]
- pessoa_id: INT [FK -> Pessoa.pessoa_id]

04. Pessoa
- pessoa_id: INT [pk]
- nome: VARCHAR(120) [NOT NULL]
- cpf: VARCHAR(14) [UNIQUE]
- endereco: VARCHAR(200) [NOT NULL]

05. Tipo_Contato
- tipo_contato_id
- descricao (exemplo: telefone, email)

06. Contato_da_Pessoa
- pessoa_id (FK Pessoa)
- tipo_contato_id
- descricao (ex: 9 9123 4567; ou joao@gmail.com)
 
Junto à lavação funciona também um pet shop.
Um animal é levado por um cliente para ser atendido no petshot. Em um atendimento podem ser prestados um (mínimo) ou mais serviços. Um serviço é realizado por um funcionário. São registrados no atendimento a hora em que o animal chega e a hora em que o animal é liberado. Quando um atendimento é finalizado e animal é liberado, a hora de retirada é preenchida. Todos os serviços no pet shop são realizados no mesmo dia. Diversos serviços podem ser realizados, como por exemplo Tosa, Banho, Vacina, etc. Quem leva o animal para o atendimento pode ser uma pessoa diferente do "dono" do animal.

07. Atendimento_pet
- id_atendimento: inT [PK]
- cliente_id: INT [FK -> Cliente.cliente_id]
- animal_id: INT [FK -> Animal.animal_id]
- hora_chegada: DATETIME [NOT NULL]
- hora_retirada: dateTIME [NULL]

08. Servico_no_atendimento_pet
- id_atendimento
- servico_id
- preco_cobrado
- funcionario_id

09. Funcionario
- funcionario_id: INT [PK]
- pessoa_id: INT [FK -> Pessoa.pessoa_id]
- comissao: DECIMAL(10,2) [DEFAULT 0]

10. Serviço
- servico_id: INT [PK]
- nome_servico: VARCHAR(100) [NOT NULL] (ex: tosa, banho, 
- descricao: TEXT [NOT NULL]
- preco: DECIMAL(10,2) [NOT NULL]
- tipo_servico: VARCHAR(40) [NOT NULL] (ex: mecânico, pet shop)
    )

11. Tipo_servico
- tipo_servico_id
- descricao (mecânic, pet shop)

Os animais que passam pelo atendimento no petshop pertecem a um cliente, possuem um nome e são pertencentes a uma espécie. A raça e o sexo do animal também é registrados.

12. Animal
- animal_id: INT [PK]
- cliente_id: INT [FK -> Cliente.cliente_id]
- nome: VARCHAR(100) [NOT NULL]
- especie: VARCHAR(50) [NOT NULL]
- raca: VARCHAR(80) [NOT NULL]
- sexo: VARCHAR(15) [NOT NULL]

Durante a estadia do cliente na lavação, ele pode adquirir produtos na loja de conveniência. Um produto possui um nome, um preço e uma quantidade no estoque que fica associada ao próprio produto no cadastro.

13. Produto
- id_produto: INT [PK]
- nome: VARCHAR(100) [NOT NULL]
- preco: DECIMAL(10,2) [NOT NULL]
- qtd_atual: INT [NOT NULL]

Cada venda de produto registra a data e hora da venda, o preço pelo qual o produto foi vendido. A venda pode associar o cliente e o carro, se for oportuno. A venda de um produto deve fazer com que a quantidade desse produto seja diminuída do estoque.

14. Venda
- id_venda: INT [PK]
- cliente_id: INT [FK -> Cliente.cliente_id, NULL]
- carro_id: INT [FK -> Carro.carro_id, NULL]
- data: DATE [NOT NULL]
- hora: TIME [NOT NULL]
- local_id (FK)

15. Item_de_Venda (tabela associativa Venda-Produto)
- id_venda: INT [PK, FK -> Venda.id_venda]
- id_produto: INT [PK, FK -> Produto.id_produto]
- quantidade: INT [NOT NULL]
- preco_unitario: DECIMAL(10,2) [NOT NULL]
- PK composta: (id_venda, id_produto)

No local existe também um guichê de venda de sorvete, no qual, por exemplo, o cliente quando vai embora, passa lá e pega um sorvete. É importante registrar que essa venda foi feita no drive-thru para avaliar qual é a efetividade dessa facilidade.

16. Local_Venda
- local_id
- descricao (exemplo: Loja de Conveniência, Drive Thru)

O abastecimento de produtos é controlado pelas entregas que são realizadas. Um produto em certa quantidade é entregue em uma data, e isso aumenta a quantidade de produtos em estoque (afeta o atributo qtd_atual na tabela Produto).

17. Abastecimento
- id_entrega: INT [PK]
- produto_id: INT [FK -> Produto.id_produto]
- qtd_produto: INT [NOT NULL]
- data_entrega: DATE [NOT NULL]

Antes ou depois de submeter o carro a uma lavação, o cliente pode realizar um ou mais serviços na mecânica anexa à lavação. Quando o veículo entra na oficina, é criada uma ordem de serviço mecânico (OSM), registrando qual é o veículo, a hora de entrada e saída e qual é o cliente que está deixando o carro na oficina. A OSM é finalizada quando é informada a hora de saída.

18. OS_Mecanica
- id_osm: INT [PK]
- carro_id: INT [FK -> Carro.carro_id] [ foreign key (carro_id) references Carro(carro_id) ]
- cliente_id: INT [FK -> Cliente.cliente_id]
- hora_entrada: DATETIME [NOT NULL]
- hora_saida: DATETIME [NULL]

Uma OSM possui um ou mais serviços realizados, e pode incluir um ou mais produtos que foram adquiridos. Toda OSM precisa ter no mínimo um produto adquirido ou um serviço realizado. 

19. Produto_Adquirido
- id_produto_adquirido: INT [PK]
- id_osm: INT [FK -> OS_Mecanica.id_osm]
- produto_id: INT [FK -> Produto.id_produto]
- funcionario_id: INT [FK -> Funcionario.funcionario_id]
- quantidade: INT [NOT NULL]
- preco: DECIMAL(10,2) [NOT NULL]

20. Servico_Realizado
- id_servico_realizado: INT [PK]
- id_osm: INT [FK -> OS_Mecanica.id_osm]
- funcionario_id: INT [FK -> Funcionario.funcionario_id]
- servico_id: INT [FK -> Servço.servico_id]
- preco: DECIMAL(10,2) [NOT NULL]
- hora_inicio: DATETIME [NOT NULL]
- hora_fim: DATETIME [NULL]

}