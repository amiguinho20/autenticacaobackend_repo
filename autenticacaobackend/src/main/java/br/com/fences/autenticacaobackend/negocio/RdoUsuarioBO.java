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
public class RdoUsuarioBO {

	@Inject
	private Logger logger;
	
	@Inject
	private AppConfig appConfig;
	
	@Inject 
	private RdoUsuarioDAO rdoUsuarioDAO;
	
	@Inject
	private RdoDelegaciaDAO rdoDelegaciaDAO;

	public void adicionarOuAtualizar(RdoUsuario rdoUsuario) {
		rdoUsuarioDAO.adicionarOuAtualizar(rdoUsuario);	
	}

	public RdoUsuario consultarRg(String rg)
	{
		RdoUsuario rdoUsuario = null;
		if (Verificador.isValorado(rg))
		{
			rdoUsuario = rdoUsuarioDAO.consultarRgUsuario(rg);
			if (rdoUsuario != null && Verificador.isValorado(rdoUsuario.getIdDelegacia()))
			{
				RdoDelegacia delegaciaProvenienteDoRdo = rdoDelegaciaDAO.consultarIdDelegacia(rdoUsuario.getIdDelegacia());
				if (delegaciaProvenienteDoRdo != null)
				{
					rdoUsuario.setRdoDelegacia(delegaciaProvenienteDoRdo);
				}
			}
		}
		return rdoUsuario;
	}
	
	public List<RdoUsuario> listarRdoUsuarios(){
		List<RdoUsuario> rdoUsuarios = rdoUsuarioDAO.listarRdoUsuarios();
		if (Verificador.isValorado(rdoUsuarios))
		{
			for (RdoUsuario rdoUsuario : rdoUsuarios)
			{
				if (rdoUsuario != null && Verificador.isValorado(rdoUsuario.getIdDelegacia()))
				{
					RdoDelegacia delegaciaProvenienteDoRdo = rdoDelegaciaDAO.consultarIdDelegacia(rdoUsuario.getIdDelegacia());
					if (delegaciaProvenienteDoRdo != null)
					{
						rdoUsuario.setRdoDelegacia(delegaciaProvenienteDoRdo);
					}
				}
			}
		}
		return rdoUsuarios;
	}
	
}
