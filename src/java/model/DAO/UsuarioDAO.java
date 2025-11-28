package model.DAO;

import config.ConectaDB;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Usuario;
import model.Util;

public class UsuarioDAO {

    /**
     * Cadastrar usuário (professor/aluno)
     */
    public boolean cadastrar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nomeCompleto, rgm, email, senha, tipo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = ConectaDB.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNomeCompleto());
            stmt.setString(2, usuario.getRgm());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getTipo());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inserir com senha já criptografada
     */
    public boolean inserir(Usuario u) {
        String sql = "INSERT INTO usuario (nomeCompleto, rgm, email, tipo, senha) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConectaDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNomeCompleto());
            stmt.setString(2, u.getRgm());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getTipo());
            stmt.setString(5, u.getSenha());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica se uma coluna existe
     */
    private boolean verificarColunaExiste(String tabela, String coluna) {
        try (Connection conn = ConectaDB.conectar()) {

            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getColumns(null, null, tabela, coluna)) {
                if (rs.next()) return true;
            }
            try (ResultSet rs2 = meta.getColumns(null, null, tabela, coluna.toUpperCase())) {
                return rs2.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cria usuário a partir de ResultSet
     */
    private Usuario criarUsuarioDoResultSet(
            ResultSet rs,
            boolean temIdCurso,
            boolean temIdTurma,
            boolean temTelefone,
            boolean temFoto
    ) throws SQLException {

        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNomeCompleto(rs.getString("nomeCompleto"));
        u.setRgm(rs.getString("rgm"));
        u.setEmail(rs.getString("email"));
        u.setTipo(rs.getString("tipo"));

        String senhaBD = rs.getString("senha");
        if (senhaBD != null && senhaBD.length() == 32) u.setSenha("HASH_MD5");
        else u.setSenha(senhaBD);

        if (temIdCurso) u.setIdCurso(rs.getInt("idCurso"));
        if (temIdTurma) u.setIdTurma(rs.getInt("idTurma"));
        if (temTelefone) u.setTelefone(rs.getString("telefone"));
        if (temFoto) u.setFoto(rs.getString("foto"));

        return u;
    }

    /**
     * Autenticar por RGM
     */
    public Usuario autenticar(String rgm, String senhaDigitada) {

        String sql = "SELECT * FROM usuario WHERE rgm = ? AND senha = ?";

        try (Connection conn = ConectaDB.conectar()) {

            boolean temIdCurso = verificarColunaExiste("usuario", "idCurso");
            boolean temIdTurma = verificarColunaExiste("usuario", "idTurma");
            boolean temTelefone = verificarColunaExiste("usuario", "telefone");
            boolean temFoto = verificarColunaExiste("usuario", "foto");

            // Tentar senha normal
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, rgm);
                stmt.setString(2, senhaDigitada);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return criarUsuarioDoResultSet(rs, temIdCurso, temIdTurma, temTelefone, temFoto);
                    }
                }
            }

            // Tentar senha em MD5
            String senhaMD5 = Util.md5(senhaDigitada);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, rgm);
                stmt.setString(2, senhaMD5);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return criarUsuarioDoResultSet(rs, temIdCurso, temIdTurma, temTelefone, temFoto);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * AUTENTICAR POR EMAIL — para admin
     */
    public Usuario autenticarPorEmail(String email, String senhaDigitada) {

        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";

        try (Connection conn = ConectaDB.conectar()) {

            boolean temIdCurso = verificarColunaExiste("usuario", "idCurso");
            boolean temIdTurma = verificarColunaExiste("usuario", "idTurma");
            boolean temTelefone = verificarColunaExiste("usuario", "telefone");
            boolean temFoto = verificarColunaExiste("usuario", "foto");

            // Senha normal
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, senhaDigitada);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return criarUsuarioDoResultSet(rs, temIdCurso, temIdTurma, temTelefone, temFoto);
                    }
                }
            }

            // Senha MD5
            String senhaMD5 = Util.md5(senhaDigitada);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, senhaMD5);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return criarUsuarioDoResultSet(rs, temIdCurso, temIdTurma, temTelefone, temFoto);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * CONTAR USUÁRIOS POR TIPO (professor, aluno, admin)
     */
    public int contarPorTipo(String tipo) {
    String sql = "SELECT COUNT(*) FROM usuario WHERE tipo = ?";
    
    try (Connection conn = ConectaDB.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, tipo);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;

    }
}
