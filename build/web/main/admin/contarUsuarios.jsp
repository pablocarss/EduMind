<%@ page import="java.sql.*"%>
<%@ page import="config.ConectaDB;"%>
<%@ page contentType="application/json; charset=UTF-8" %>

<%
    int totalAlunos = 0;
    int totalProfessores = 0;

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        conn = ConectaDB.conectar();

        // Conta Alunos
        stmt = conn.prepareStatement("SELECT COUNT(*) FROM usuario WHERE tipo = 'ALUNO'");
        rs = stmt.executeQuery();
        if (rs.next()) totalAlunos = rs.getInt(1);

        // Conta Professores
        stmt = conn.prepareStatement("SELECT COUNT(*) FROM usuario WHERE tipo = 'PROFESSOR'");
        rs = stmt.executeQuery();
        if (rs.next()) totalProfessores = rs.getInt(1);
        
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
    }

    out.print("{");
    out.print("\"alunos\": " + totalAlunos + ",");
    out.print("\"professores\": " + totalProfessores);
    out.print("}");
%>
