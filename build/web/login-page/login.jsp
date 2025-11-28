<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="model.DAO.UsuarioDAO" %>
<%@ page import="model.Usuario" %>

<%

    // Captura o parâmetro 'erro' passado pela URL (ex: ?erro=Acesso Negado.)
    String erro = request.getParameter("erro");
    // Se o parâmetro não existir, 'erro' será null.

    // Processa o login se houver POST
    if("POST".equals(request.getMethod())) {

        String rgm = request.getParameter("rgm");
        String senha = request.getParameter("senha");

        if(rgm != null && senha != null && !rgm.trim().isEmpty() && !senha.trim().isEmpty()) {

            UsuarioDAO dao = new UsuarioDAO();
            Usuario u = dao.autenticar(rgm, senha);

            if(u != null){
                session.setAttribute("usuarioLogado", u);

                String tipo = u.getTipo() != null ? u.getTipo() : "";

                switch(tipo.toUpperCase()){
                    case "ADMIN":
                        response.sendRedirect("../main/admin/portalAdmin.jsp");
                        return;
                    case "PROFESSOR":
                        response.sendRedirect("../main/professor/main-page/portalProfessor.jsp");
                        return;
                    default:
                        response.sendRedirect("../main/aluno/main-page/portalAluno.jsp");
                        return;
                }
            } else {
                response.sendRedirect("login.jsp?erro=RGM ou senha incorretos");
                return;
            }

        } else {
            response.sendRedirect("login.jsp?erro=Preencha todos os campos");
            return;
        }
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="style.css">
  <title>Login</title>
</head>
<body>

<div class="wrapper active" id="loginWrapper">
    <div class="logo">
      <img src="#">
    </div>
    <div class="buttons">
      <button id="loginBtn">Login</button>
      <button id="primeiroBtn">Primeiro Acesso</button>
    </div>
</div>

<div class="wrapper" id="secondWrapper">
    <div class="logo">
      <img src="#">
    </div>

    <!-- FORMULÁRIO DE LOGIN CORRETO -->
    <form method="POST" class="inputs">
      <input type="text" name="rgm" placeholder="RGM">
      <input type="password" name="senha" placeholder="Senha">

      <button type="submit" id="btnEntrar">Entrar</button>
      <button type="button" class="back-button">Voltar</button>
    </form>

    <% if(erro != null){ %>
        <p style="color:red; margin-top:10px;"><%= erro %></p>
    <% } %>

</div>

<div class="wrapper" id="thirdWrapper">
    <div class="logo">
      <img src="#">
    </div>

    <form method="POST" action="mailto:contatoeduweb@gmail.com" enctype="text/plain" class="inputs">

      <input type="text" name="nome" placeholder="Nome" required>
      <input type="text" name="sobrenome" placeholder="Sobrenome" required>
      <input type="email" name="email" placeholder="Email" required>
      <input type="date" name="nascimento" placeholder="Data de Nascimento" required>

      <input type="text" id="cep" name="cep" placeholder="CEP" required>
      <input type="text" id="endereco" name="endereco" placeholder="Endereço" required>
      <input type="text" id="bairro" name="bairro" placeholder="Bairro" required>

      <button type="submit">Enviar Dados</button>
      <button type="button" class="back-button">Voltar</button>
    </form>
</div>

<section>
    <div class="onda ondas1"></div>
    <div class="onda ondas2"></div>
    <div class="onda ondas3"></div>
    <div class="onda ondas4"></div>
</section>

<script src="script.js"></script>

</body>
</html>
