package controller;

import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/enviarEmail")
public class PrimeiroAcessoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String emailDestino = request.getParameter("email");

        if (emailDestino == null || emailDestino.trim().isEmpty()) {
            response.getWriter().write("Erro: nenhum email foi enviado ao servlet.");
            return;
        }

        // EMAIL DO REMETENTE
        String remetente = "SEU_EMAIL"; // Coloque aqui o email do remetente

        // SENHA DE APLICATIVO DO GMAIL
        String senha = "SUA_SENHA_AQUI"; // Coloque aqui a senha do aplicativo

        // CONFIGURAÇÕES SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // AUTENTICAÇÃO
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetente, senha);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestino));
            message.setSubject("Registro da conta");
            message.setText("Seu e-mail foi registrado com sucesso!");

            Transport.send(message);

            response.getWriter().write("Email enviado com sucesso!");

        } catch (MessagingException e) {
            e.printStackTrace();
            response.getWriter().write("Erro ao enviar email: " + e.getMessage());
        }
    }
}
