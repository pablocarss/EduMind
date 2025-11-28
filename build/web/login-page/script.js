// WRAPPERS
const loginWrapper = document.getElementById("loginWrapper");
const secondWrapper = document.getElementById("secondWrapper");
const thirdWrapper = document.getElementById("thirdWrapper");

// BOTÕES PRINCIPAIS
const loginBtn = document.getElementById("loginBtn");
const primeiroBtn = document.getElementById("primeiroBtn");

/* ============================
   TROCA ENTRE AS TELAS
============================ */
function switchWrapper(hide, show) {
  hide.classList.remove("active");
  setTimeout(() => show.classList.add("active"), 300);
}

// Mostrar tela de login (RGM/senha)
loginBtn.addEventListener("click", () => {
  switchWrapper(loginWrapper, secondWrapper);
});

// Mostrar tela de primeiro acesso (form completo)
primeiroBtn.addEventListener("click", () => {
  switchWrapper(loginWrapper, thirdWrapper);
});

// Botões de voltar
document.querySelectorAll(".back-button").forEach(btn => {
  btn.addEventListener("click", () => {
    switchWrapper(btn.closest(".wrapper"), loginWrapper);
  });
});

/* ============================
   LOGIN: IR PARA PORTAL (TESTE)
============================ */
document.getElementById("btnEntrar").addEventListener("click", () => {
  window.location.href = "../main/aluno/main-page/index.html";
});

/* ============================
   PRIMEIRO ACESSO – FORM COMPLETO
============================ */

// FORM DO TERCEIRO WRAPPER
const primeiroAcessoForm = thirdWrapper.querySelector("form");

// Evento de envio
primeiroAcessoForm.addEventListener("submit", (e) => {
  e.preventDefault();

  const nome = primeiroAcessoForm.nome.value.trim();
  const sobrenome = primeiroAcessoForm.sobrenome.value.trim();
  const email = primeiroAcessoForm.email.value.trim();
  const nascimento = primeiroAcessoForm.nascimento.value.trim();
  const cep = primeiroAcessoForm.cep.value.trim();
  const endereco = primeiroAcessoForm.endereco.value.trim();
  const bairro = primeiroAcessoForm.bairro.value.trim();

  if (!nome || !sobrenome || !email || !nascimento || !cep || !endereco || !bairro) {
    alert("Por favor, preencha todos os campos.");
    return;
  }

  alert("Seus dados foram enviados! Você receberá um e-mail em breve.");

  // Aqui você pode adicionar envio real via backend se quiser.
});

/* ============================
   AUTOCOMPLETAR ENDEREÇO (VIACEP)
============================ */
const cepInput = document.getElementById("cep");
const enderecoInput = document.getElementById("endereco");
const bairroInput = document.getElementById("bairro");

cepInput.addEventListener("blur", async () => {
  const cep = cepInput.value.replace(/\D/g, "");

  if (cep.length !== 8) return;

  try {
    const response = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
    const data = await response.json();

    if (!data.erro) {
      enderecoInput.value = data.logradouro || "";
      bairroInput.value = data.bairro || "";
    }
  } catch (e) {
    console.log("Erro ao buscar CEP:", e);
  }
});

// Tela inicial
loginWrapper.classList.add("active");


emailButton.addEventListener("click", () => {
  const emailInput = document.querySelector("#email").value;

  fetch("http://localhost:8080/SEU_PROJETO/enviarEmail", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: "email=" + encodeURIComponent(emailInput)
  })
  .then(res => res.text())
  .then(alert)
  .catch(err => alert("Erro: " + err));
});