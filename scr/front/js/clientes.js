let tiposContato = [];
let editandoId = null; // null = cadastrando; numero = editando esse cliente

const form = document.getElementById("form-cliente");
const divContatos = document.getElementById("contatos");
const tbody = document.getElementById("tabela-clientes");
const msg = document.getElementById("mensagem");
const btnCancelar = document.getElementById("btn-cancelar");
const btnSalvar = document.getElementById("btn-salvar");

function mostrarMensagem(texto, erro = false) {
  msg.textContent = texto;
  msg.className = erro ? "msg erro" : "msg ok";
}

// ---------- contatos dinamicos do formulario ----------
function adicionarContato(contato = {}) {
  const linha = document.createElement("div");
  linha.className = "linha-contato";

  const select = document.createElement("select");
  for (const t of tiposContato) {
    const op = document.createElement("option");
    op.value = t.id;
    op.textContent = t.descricao;
    select.appendChild(op);
  }
  if (contato.tipoContatoId) select.value = contato.tipoContatoId;

  const input = document.createElement("input");
  input.type = "text";
  input.placeholder = "Telefone ou e-mail";
  input.maxLength = 120;
  input.value = contato.descricao || "";

  const remover = document.createElement("button");
  remover.type = "button";
  remover.textContent = "✕";
  remover.onclick = () => {
    if (divContatos.children.length > 1) linha.remove();
    else mostrarMensagem("O cliente precisa de pelo menos 1 contato.", true);
  };

  linha.append(select, input, remover);
  divContatos.appendChild(linha);
}

// ---------- ler / limpar formulario ----------
function lerFormulario() {
  return {
    nome: document.getElementById("nome").value,
    cpf: document.getElementById("cpf").value,
    endereco: document.getElementById("endereco").value,
    contatos: [...divContatos.children].map((linha) => ({
      tipoContatoId: Number(linha.querySelector("select").value),
      descricao: linha.querySelector("input").value,
    })),
  };
}

function limparFormulario() {
  form.reset();
  divContatos.innerHTML = "";
  adicionarContato();
  editandoId = null;
  btnSalvar.textContent = "Cadastrar";
  btnCancelar.hidden = true;
}

// ---------- listagem ----------
function celula(texto) {
  const td = document.createElement("td");
  td.textContent = texto; // textContent (nao innerHTML) evita injecao de HTML
  return td;
}

async function carregarClientes() {
  const clientes = await get("/clientes");
  tbody.innerHTML = "";

  for (const c of clientes) {
    const tr = document.createElement("tr");
    tr.append(
      celula(c.nome),
      celula(c.cpf || "—"),
      celula(c.endereco),
      celula(c.contatos.map((ct) => `${ct.tipo}: ${ct.descricao}`).join(" | "))
    );

    const acoes = document.createElement("td");
    const btnEditar = document.createElement("button");
    btnEditar.textContent = "Editar";
    btnEditar.onclick = () => iniciarEdicao(c);
    const btnExcluir = document.createElement("button");
    btnExcluir.textContent = "Excluir";
    btnExcluir.onclick = () => excluirCliente(c);
    acoes.append(btnEditar, btnExcluir);

    tr.appendChild(acoes);
    tbody.appendChild(tr);
  }
}

function iniciarEdicao(c) {
  editandoId = c.id;
  document.getElementById("nome").value = c.nome;
  document.getElementById("cpf").value = c.cpf || "";
  document.getElementById("endereco").value = c.endereco;
  divContatos.innerHTML = "";
  c.contatos.forEach((ct) => adicionarContato(ct));
  btnSalvar.textContent = "Salvar alterações";
  btnCancelar.hidden = false;
  window.scrollTo({ top: 0, behavior: "smooth" });
}

async function excluirCliente(c) {
  if (!confirm(`Excluir o cliente "${c.nome}"?`)) return;
  try {
    await del(`/clientes/${c.id}`);
    mostrarMensagem("Cliente excluído.");
    await carregarClientes();
  } catch (err) {
    mostrarMensagem(err.message, true);
  }
}

// ---------- eventos ----------
form.addEventListener("submit", async (e) => {
  e.preventDefault(); // impede o navegador de recarregar a pagina
  try {
    const dados = lerFormulario();
    if (editandoId) {
      await put(`/clientes/${editandoId}`, dados);
      mostrarMensagem("Cliente atualizado!");
    } else {
      await post("/clientes", dados);
      mostrarMensagem("Cliente cadastrado!");
    }
    limparFormulario();
    await carregarClientes();
  } catch (err) {
    mostrarMensagem(err.message, true);
  }
});

btnCancelar.addEventListener("click", limparFormulario);
document.getElementById("btn-add-contato").addEventListener("click", () => adicionarContato());

// ---------- inicio ----------
(async function iniciar() {
  try {
    tiposContato = await get("/tipos-contato");
    adicionarContato();
    await carregarClientes();
  } catch (err) {
    mostrarMensagem("Não foi possível falar com a API: " + err.message, true);
  }
})();