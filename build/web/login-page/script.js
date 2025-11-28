// WRAPPERS
const loginWrapper = document.getElementById("loginWrapper");
const secondWrapper = document.getElementById("secondWrapper");
const thirdWrapper = document.getElementById("thirdWrapper");
const modalRegister = document.getElementById("thirdWrapperRegister");
const overlay = document.getElementById("overlay");

// BOTÕES PRINCIPAIS
const loginBtn = document.getElementById("loginBtn");
const primeiroBtn = document.getElementById("primeiroBtn");
const btnCadastrar = document.getElementById("btnCadastrar");

// Função auxiliar para trocar wrappers (fade)
function switchWrapper(hide, show) {
  if (!hide || !show) return;
  hide.classList.remove("active");
  setTimeout(() => show.classList.add("active"), 450);
}

/* ============================
   Navegação entre telas
============================ */
// Mostrar tela de login (RGM/senha)
loginBtn.addEventListener("click", () => {
  switchWrapper(loginWrapper, secondWrapper);
});

// Mostrar tela "Primeiro Acesso" (a tela com o botão Cadastrar)
primeiroBtn.addEventListener("click", () => {
  switchWrapper(loginWrapper, thirdWrapper);
});

// Botões de voltar (para wrappers e modal)
document.querySelectorAll(".back-button").forEach((btn) => {
  btn.addEventListener("click", () => {
    const current = btn.closest(".wrapper, .wrapperRegister");

    // Se for o modal, fecha-o e volta para thirdWrapper
    if (current === modalRegister) {
      modalRegister.classList.remove("active");
      overlay.classList.remove("active");
      thirdWrapper.classList.add("active");
      return;
    }

    // Se for qualquer wrapper normal, volta pra login
    switchWrapper(current, loginWrapper);
  });
});

// Entrar (teste)
document.getElementById("btnEntrar").addEventListener("click", () => {
  window.location.href = "../main/aluno/main-page/index.html";
});

/* ============================
   ABRIR O MODAL PELO BOTÃO "Cadastrar"
============================ */
btnCadastrar.addEventListener("click", () => {
  // Esconder a tela atual (thirdWrapper) e abrir o modal + overlay
  thirdWrapper.classList.remove("active");
  modalRegister.classList.add("active");
  overlay.classList.add("active");
});

/* ============================
   FECHAR O MODAL QUANDO CLICAR NO OVERLAY
============================ */
overlay.addEventListener("click", () => {
  modalRegister.classList.remove("active");
  overlay.classList.remove("active");
  thirdWrapper.classList.add("active"); // volta pra tela anterior
});

/* ============================
   FORMULÁRIO DO MODAL (validação simples)
============================ */
const primeiroAcessoForm = modalRegister.querySelector("form");
if (primeiroAcessoForm) {
  primeiroAcessoForm.addEventListener("submit", (e) => {
    e.preventDefault();

    const nome = primeiroAcessoForm.nome.value.trim();
    const sobrenome = primeiroAcessoForm.sobrenome.value.trim();
    const email = primeiroAcessoForm.email.value.trim();
    const nascimento = primeiroAcessoForm.nascimento.value.trim();
    const cep = primeiroAcessoForm.cep.value.trim();
    const endereco = primeiroAcessoForm.endereco.value.trim();
    const bairro = primeiroAcessoForm.bairro.value.trim();

    if (
      !nome ||
      !sobrenome ||
      !email ||
      !nascimento ||
      !cep ||
      !endereco ||
      !bairro
    ) {
      alert("Por favor, preencha todos os campos.");
      return;
    }

    alert("Seus dados foram enviados! Você receberá um e-mail em breve.");
    // fecha o modal depois do submit (opcional)
    modalRegister.classList.remove("active");
    overlay.classList.remove("active");
    thirdWrapper.classList.add("active");
  });
}

/* ============================
   AUTOCOMPLETAR VIA CEP (VIACEP)
============================ */
const cepInput = document.getElementById("cep");
const enderecoInput = document.getElementById("endereco");
const bairroInput = document.getElementById("bairro");

if (cepInput) {
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
}

// Tela inicial ativa
loginWrapper.classList.add("active");
