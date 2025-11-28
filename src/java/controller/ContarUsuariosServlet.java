package controller;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.*;
import java.io.IOException;

import model.DAO.UsuarioDAO;

public class ContarUsuariosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");

        UsuarioDAO dao = new UsuarioDAO();

        int qtdProfessores = dao.contarPorTipo("PROFESSOR");
        int qtdAlunos = dao.contarPorTipo("ALUNO");

        // Criando JSON com JSON-P (javax.json)
        JsonObject json = Json.createObjectBuilder()
                .add("professores", qtdProfessores)
                .add("alunos", qtdAlunos)
                .build();

        response.getWriter().write(json.toString());
    }
}
