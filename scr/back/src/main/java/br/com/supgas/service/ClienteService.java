package br.com.supgas.service;

import br.com.supgas.dto.ClienteRequest;
import br.com.supgas.dto.ClienteResponse;
import br.com.supgas.model.Cliente;
import br.com.supgas.model.ContatoPessoa;
import br.com.supgas.model.Pessoa;
import br.com.supgas.model.TipoContato;
import br.com.supgas.repository.ClienteRepository;
import br.com.supgas.repository.ContatoPessoaRepository;
import br.com.supgas.repository.PessoaRepository;
import br.com.supgas.repository.TipoContatoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepo;
    private final PessoaRepository pessoaRepo;
    private final TipoContatoRepository tipoRepo;
    private final ContatoPessoaRepository contatoRepo;

    // O Spring injeta os repositories sozinho (injecao de dependencia)
    public ClienteService(ClienteRepository clienteRepo, PessoaRepository pessoaRepo,
                          TipoContatoRepository tipoRepo, ContatoPessoaRepository contatoRepo) {
        this.clienteRepo = clienteRepo;
        this.pessoaRepo = pessoaRepo;
        this.tipoRepo = tipoRepo;
        this.contatoRepo = contatoRepo;
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        List<Cliente> clientes = clienteRepo.findAll();
        if (clientes.isEmpty()) return List.of();

        // 1 consulta so para trazer os contatos de todos (evita 1 consulta por cliente)
        List<Integer> pessoaIds = clientes.stream().map(c -> c.getPessoa().getId()).toList();
        Map<Integer, List<ContatoPessoa>> contatosPorPessoa = contatoRepo.findByPessoaIdIn(pessoaIds)
                .stream().collect(Collectors.groupingBy(ct -> ct.getPessoa().getId()));

        return clientes.stream()
                .map(c -> toResponse(c, contatosPorPessoa.getOrDefault(c.getPessoa().getId(), List.of())))
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscar(Integer id) {
        Cliente cliente = buscarEntidade(id);
        return toResponse(cliente, contatoRepo.findByPessoaId(cliente.getPessoa().getId()));
    }

    // @Transactional: tudo abaixo acontece junto ou nao acontece.
    // Se salvar um contato falhar, a pessoa e o cliente tambem sao desfeitos.
    @Transactional
    public ClienteResponse criar(ClienteRequest req) {
        validarContatos(req.contatos());

        Pessoa pessoa = new Pessoa();
        preencher(pessoa, req);
        pessoaRepo.save(pessoa);

        Cliente cliente = new Cliente();
        cliente.setPessoa(pessoa);
        clienteRepo.save(cliente);

        List<ContatoPessoa> contatos = salvarContatos(pessoa, req.contatos());
        return toResponse(cliente, contatos);
    }

    @Transactional
    public ClienteResponse atualizar(Integer id, ClienteRequest req) {
        validarContatos(req.contatos());

        Cliente cliente = buscarEntidade(id);
        Pessoa pessoa = cliente.getPessoa();
        preencher(pessoa, req);
        pessoaRepo.save(pessoa);

        // Estrategia simples: apaga os contatos antigos e grava os novos
        contatoRepo.removerPorPessoaId(pessoa.getId());
        List<ContatoPessoa> contatos = salvarContatos(pessoa, req.contatos());
        return toResponse(cliente, contatos);
    }

    @Transactional
    public void excluir(Integer id) {
        Cliente cliente = buscarEntidade(id);
        Pessoa pessoa = cliente.getPessoa();

        contatoRepo.removerPorPessoaId(pessoa.getId());
        clienteRepo.delete(cliente);
        pessoaRepo.delete(pessoa);
        // flush forca o SQL agora: se o cliente tiver carro/animal/venda,
        // o erro de chave estrangeira aparece aqui e vira resposta 409
        pessoaRepo.flush();
    }

    // ---------------- regras e auxiliares ----------------

    // REGRA DE NEGOCIO: cliente precisa de pelo menos 1 contato (e sem repetidos)
    private void validarContatos(List<ClienteRequest.Contato> contatos) {
        if (contatos == null || contatos.isEmpty()) {
            throw erro("O cliente precisa ter pelo menos 1 contato.");
        }
        Set<String> vistos = new HashSet<>();
        for (ClienteRequest.Contato c : contatos) {
            String chave = c.tipoContatoId() + "|" + c.descricao().trim().toLowerCase();
            if (!vistos.add(chave)) {
                throw erro("Contato repetido: " + c.descricao().trim());
            }
        }
    }

    private List<ContatoPessoa> salvarContatos(Pessoa pessoa, List<ClienteRequest.Contato> contatos) {
        List<ContatoPessoa> novos = new ArrayList<>();
        for (ClienteRequest.Contato c : contatos) {
            TipoContato tipo = tipoRepo.findById(c.tipoContatoId())
                    .orElseThrow(() -> erro("Tipo de contato inválido: " + c.tipoContatoId()));
            novos.add(new ContatoPessoa(pessoa, tipo, c.descricao().trim()));
        }
        return contatoRepo.saveAll(novos);
    }

    private void preencher(Pessoa pessoa, ClienteRequest req) {
        pessoa.setNome(req.nome().trim());
        // CPF vazio vira null: a coluna e UNIQUE e dois "" iguais dariam conflito
        pessoa.setCpf(req.cpf() == null || req.cpf().isBlank() ? null : req.cpf().trim());
        pessoa.setEndereco(req.endereco().trim());
    }

    private Cliente buscarEntidade(Integer id) {
        return clienteRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
    }

    private ResponseStatusException erro(String mensagem) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
    }

    private ClienteResponse toResponse(Cliente c, List<ContatoPessoa> contatos) {
        Pessoa p = c.getPessoa();
        List<ClienteResponse.ContatoResponse> cs = contatos.stream()
                .map(ct -> new ClienteResponse.ContatoResponse(
                        ct.getId(), ct.getTipoContato().getId(),
                        ct.getTipoContato().getDescricao(), ct.getDescricao()))
                .toList();
        return new ClienteResponse(c.getId(), p.getId(), p.getNome(), p.getCpf(), p.getEndereco(), cs);
    }
}