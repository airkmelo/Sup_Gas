const API = "http://localhost:8080/api";

// Funcao unica que faz toda chamada HTTP. get/post/put/del so a chamam.
async function request(method, path, body) {
  const opcoes = { method, headers: {} };
  if (body !== undefined) {
    opcoes.headers["Content-Type"] = "application/json";
    opcoes.body = JSON.stringify(body);
  }
  const r = await fetch(`${API}${path}`, opcoes);
  if (!r.ok) throw new Error(await lerErro(r));
  return r.status === 204 ? null : r.json(); // 204 = sem corpo (DELETE)
}

// A API devolve erros como { "erro": "...", "detalhes": [...] }
async function lerErro(r) {
  try {
    const j = await r.json();
    return j.detalhes ? `${j.erro}: ${j.detalhes.join("; ")}` : (j.erro || `Erro ${r.status}`);
  } catch {
    return `Erro ${r.status}`;
  }
}

const get  = (path) => request("GET", path);
const post = (path, body) => request("POST", path, body);
const put  = (path, body) => request("PUT", path, body);
const del  = (path) => request("DELETE", path); // "delete" e palavra reservada em JS