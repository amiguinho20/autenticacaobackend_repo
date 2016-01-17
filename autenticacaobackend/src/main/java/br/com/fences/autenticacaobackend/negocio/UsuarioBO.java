package br.com.fences.autenticacaobackend.negocio;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import br.com.fences.autenticacaobackend.config.AppConfig;
import br.com.fences.autenticacaobackend.dao.RdoDelegaciaDAO;
import br.com.fences.autenticacaobackend.dao.RdoUsuarioDAO;
import br.com.fences.autenticacaobackend.dao.UsuarioDAO;
import br.com.fences.autenticacaoentidade.rdo.delegacia.RdoDelegacia;
import br.com.fences.autenticacaoentidade.rdo.usuario.RdoUsuario;
import br.com.fences.autenticacaoentidade.usuario.Usuario;
import br.com.fences.fencesutils.conversor.FencesBase32;
import br.com.fences.fencesutils.verificador.Verificador;

@RequestScoped
public class UsuarioBO {

	@Inject
	private Logger logger;
	
	@Inject
	private AppConfig appConfig;
	
	@Inject
	private UsuarioDAO usuarioDAO;
	
	@Inject 
	private RdoUsuarioDAO rdoUsuarioDAO;
	
	@Inject
	private RdoDelegaciaDAO rdoDelegaciaDAO;

	public void adicionar(Usuario usuario) {
		usuarioDAO.adicionar(usuario);
		enviarEmail(usuario);
		
	}
	
	public String ativarUsuario(String usernameEncoded){
		String mensagem = null;
		if (Verificador.isValorado(usernameEncoded))
		{
			String username = FencesBase32.decode(usernameEncoded);
			Usuario usuario = consultarUsername(username);
			if (usuario != null)
			{
				if (usuario.getAtivo().equalsIgnoreCase("N"))
				{
					usuario.setAtivo("S");
					atualizar(usuario);
					mensagem = "Usuario ativado com sucesso.";
				}
				else
				{
					mensagem = "Usuario ja foi previamente ativado.";
				}
			}
			else
			{
				mensagem = "Nao ha usuario cadastrado com o username: " + username;
			}
		}
		else
		{
			mensagem = "O usernameEncoded esta vazio.";
		}
		
		
		return mensagem;
	}

	public void atualizar(Usuario usuario) {
		usuarioDAO.atualizar(usuario);
	}

	public Usuario consultarId(String id) {
		Usuario usuario = usuarioDAO.consultarId(id);
		popularInformacoesDoRdo(usuario);
		return usuario;
	}

	public Usuario consultarUsername(String username) {
		Usuario usuario = usuarioDAO.consultarUsername(username);
		popularInformacoesDoRdo(usuario);
		return usuario;
	}

	public Usuario consultarRg(String rg) {
		Usuario usuario = usuarioDAO.consultarRg(rg);
		popularInformacoesDoRdo(usuario);
		return usuario;
	}
	
	private void popularInformacoesDoRdo(Usuario usuario)
	{
		if (usuario != null && Verificador.isValorado(usuario.getRg()))
		{
			RdoUsuario usuarioProvenienteDoRdo = rdoUsuarioDAO.consultarRgUsuario(usuario.getRg());
			if (usuarioProvenienteDoRdo != null)
			{
				usuario.setRdoUsuario(usuarioProvenienteDoRdo);
				RdoDelegacia delegaciaProvenienteDoRdo = rdoDelegaciaDAO.consultarIdDelegacia(usuarioProvenienteDoRdo.getIdDelegacia());
				if (delegaciaProvenienteDoRdo != null)
				{
					usuarioProvenienteDoRdo.setRdoDelegacia(delegaciaProvenienteDoRdo);
				}
			}
		}
	}
	
	public List<Usuario> listarUsuarios() {
		List<Usuario> usuarios = usuarioDAO.listarUsuarios();
		if (Verificador.isValorado(usuarios))
		{
			for (Usuario usuario : usuarios)
			{
				popularInformacoesDoRdo(usuario);
			}
		}
		return usuarios;
	}

	public void enviarEmail(Usuario usuario) {
		try {

			String emailDestino = usuario.getUsername();

			if (Verificador.isValorado(emailDestino)) {

				//-- para usar o gmail, a opcao "Less secure apps" deve ser ligada.
				String smtpHost = appConfig.getSmtpHost();
				String smtpPort = appConfig.getSmtpPort();
				String smtpFrom = appConfig.getSmtpFrom();
				String smtpUsername = appConfig.getSmtpUsername();
				String smtpPassword = appConfig.getSmtpPassword();
				String smtpSSL = appConfig.getSmtpSSL();

				if (!Verificador.isValorado(smtpHost, smtpPort, smtpFrom, smtpUsername, smtpPassword, smtpSSL)) {
					StringBuilder msg = new StringBuilder();
					msg.append("Algum valor nao foi informado adequadamente.");
					logger.error(msg.toString());
				} else {
					
					String assunto = "Σ Sigma - Ativação de cadastro";
					String texto1 = "<b>" + emailDestino + "</b>, olá";
					String texto2 = "Seu cadastro foi realizado com sucesso, porém encontra-se desativado.";
					String texto3 = "Guarde as seguintes informações para acessar o &Sigma; Sigma:";
					String texto4 = "Clique no link/botão abaixo para <b>ativar</b> o seu cadastro no &Sigma; Sigma.";
					
					String encode = FencesBase32.encode(emailDestino);
					
					String linkPath = "http://localhost:8080/autenticacaobackend/rest/usuario/ativar/" + encode ;
					String linkStyle = "font-family:Verdana,Arial,sans-serif; border-style: solid; border-width : 2px; text-decoration : none; border-color : #33AFDE; background: #33AFDE; color: white; font-weight: bold; border-radius: 10px; padding: 10px; margin:10px";
					String link =  "<a href='" + linkPath + "' style='"+ linkStyle +"'>Ativar cadastro no &Sigma; Sigma</a>";

					StringBuilder msg = new StringBuilder();
					msg.append("<html>");
					msg.append("<head><title>" + assunto + "</title></head>");
					msg.append("<body>");
					msg.append("<p style='font-family:Verdana,Arial,sans-serif;'>" + texto1 + "</p>");
					msg.append("</br></br>");
					msg.append("<p style='font-family:Verdana,Arial,sans-serif;'>" + texto2 + "</p>");
					msg.append("</br></br>");
					msg.append("<p style='font-family:Verdana,Arial,sans-serif;'>" + texto3 + "</p>");
					msg.append("</br></br>");
					msg.append("<p style='font-family:Verdana,Arial,sans-serif;'>Login: <b>" + emailDestino + "</b></p>");
					msg.append("</br>");
					msg.append("<p style='font-family:Verdana,Arial,sans-serif;'>Senha: <b>" + usuario.getPassword() + "</b></p>");
					msg.append("</br></br>");
					msg.append("<p style='font-family:Verdana,Arial,sans-serif;'>" + texto4 + "</p>");
					msg.append("</br></br>");
					msg.append(link);
					msg.append("</br></br>");
					msg.append("<p style='font-family:Verdana,Arial,sans-serif;'>Atenciosamente,</p>");
					msg.append("<p style='font-family:Verdana,Arial,sans-serif;'>&Sigma; Sigma</p>");
					msg.append("</body>");
					msg.append("</html>");

					boolean sucesso = false;
					int tentativa = 0;
					Exception exception = null;
					for (; tentativa < 5 && !sucesso; tentativa++) {
						try {
							HtmlEmail email = new HtmlEmail();
							email.setHostName(smtpHost);
							email.setSmtpPort(Integer.parseInt(smtpPort));
							email.setAuthenticator(new DefaultAuthenticator(smtpUsername, smtpPassword));
							email.setFrom(smtpFrom, "Σ Sigma");
							email.addTo(emailDestino, emailDestino);
							email.setSubject(assunto);
							if (smtpSSL.equalsIgnoreCase("true")) {
								email.setSSLOnConnect(true);
							} else {
								email.setSSLOnConnect(false);
							}
							email.setHtmlMsg(msg.toString());
							email.send();
							sucesso = true;
						} catch (EmailException e) {
							exception = e;
						}
					}
					if (!sucesso) {
						String msn = "smtp_host[" + smtpHost + "] smtp_port[" + smtpPort + "] usr[" + smtpUsername
								+ "] pwd[" + smtpPassword + "] smtpSSL[" + smtpSSL + "] smtpFrom[" + smtpFrom + "]";
						msn = "Erro no envio do email { " + msn + " }: " + exception.getMessage();
						logger.error(msn, exception);
					}
				}
			}

		} catch (Exception e) {
			logger.error("Erro " + e.getMessage(), e);
			throw new RuntimeException(e);
		}

	}
	
	

	

}
