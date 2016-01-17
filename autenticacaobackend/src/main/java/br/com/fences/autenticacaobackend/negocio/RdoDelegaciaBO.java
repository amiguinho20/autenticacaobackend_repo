package br.com.fences.autenticacaobackend.negocio;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.com.fences.autenticacaobackend.dao.RdoDelegaciaDAO;
import br.com.fences.autenticacaoentidade.rdo.delegacia.RdoDelegacia;

@RequestScoped
public class RdoDelegaciaBO {

	@Inject
	private transient Logger logger;
	
	
	@Inject
	private RdoDelegaciaDAO rdoDelegaciaDAO;

	public void adicionarOuAtualizar(RdoDelegacia rdoDelegacia) {
		rdoDelegaciaDAO.adicionarOuAtualizar(rdoDelegacia);	
	}
	
	public RdoDelegacia consultarIdDelegacia(String id)
	{
		RdoDelegacia rdoDelegacia = rdoDelegaciaDAO.consultarIdDelegacia(id);
		return rdoDelegacia;
	}

	public List<RdoDelegacia> listarRdoDelegacias(){
		List<RdoDelegacia> rdoDelegacias = rdoDelegaciaDAO.listarRdoDelegacias();
		return rdoDelegacias;
	}
	
}
