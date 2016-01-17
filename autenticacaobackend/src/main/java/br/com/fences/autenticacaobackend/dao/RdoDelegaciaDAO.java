package br.com.fences.autenticacaobackend.dao;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import br.com.fences.autenticacaobackend.dao.config.ColecaoRdoDelegacia;
import br.com.fences.autenticacaoentidade.rdo.delegacia.RdoDelegacia;
import br.com.fences.fencesutils.conversor.converter.Converter;


@RequestScoped
public class RdoDelegaciaDAO {

	@Inject
	private transient Logger logger;

	@Inject @ColecaoRdoDelegacia
	private MongoCollection<Document> colecao;
	
	@Inject
	private Converter<RdoDelegacia> converter;
	
	public void adicionar(RdoDelegacia rdoDelegacia) {

		if (rdoDelegacia != null) {
			boolean existeRegistro = consultarIdDelegacia(rdoDelegacia.getIdDelegacia()) != null ? true : false;
			if (!existeRegistro)
			{
				try
				{
					Document documento = converter.paraDocumento(rdoDelegacia);
					colecao.insertOne(documento);
				}
				catch (Exception e)
				{
					String msg = "Erro na inclusao unica. idDelegacia[" + rdoDelegacia.getIdDelegacia() + "].";
					logger.error(msg, e);
					throw new RuntimeException(msg, e);
				}
			}
			else
			{
				throw new RuntimeException("O idDelegacia[" + rdoDelegacia.getIdDelegacia() + "] já existe.");	
			}
		}
	}
	
	public void atualizar(RdoDelegacia rdoDelegacia) {
		try
		{
			Document documento = converter.paraDocumento(rdoDelegacia);
			colecao.replaceOne(eq("_id", documento.get("_id")), documento);
		}
		catch (Exception e)
		{
			String msg = "Erro na alteracao. idDelegacia[" + rdoDelegacia.getIdDelegacia() + "].";
			logger.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	public void adicionarOuAtualizar(RdoDelegacia rdoDelegacia)
	{
		if (rdoDelegacia != null)
		{
			RdoDelegacia rdoDelegaciaCadastrada = consultarIdDelegacia(rdoDelegacia.getIdDelegacia());
			if (rdoDelegaciaCadastrada == null)
			{	//-- incluir
				adicionar(rdoDelegacia);
			}
			else
			{	//-- alterar
				String idDaDelegaciaCadastrada = rdoDelegaciaCadastrada.getId();
				rdoDelegacia.setId(idDaDelegaciaCadastrada);
				atualizar(rdoDelegacia);
			}
		}
	}
	
	public RdoDelegacia consultarIdDelegacia(final String idDelegacia) 
	{
		RdoDelegacia rdoDelegacia = null;
	    Document documento = colecao.find(eq("ID_DELEGACIA", idDelegacia)).first();
	    if (documento != null)
	    {
	    	rdoDelegacia = converter.paraObjeto(documento, RdoDelegacia.class);
	    }
	    return rdoDelegacia;
	}

	public List<RdoDelegacia> listarRdoDelegacias() {
		
		List<RdoDelegacia> rdoDelegacias = new ArrayList<>();
		
	    MongoCursor<Document> cursor = colecao.find().iterator();
		
	    try {
	        while (cursor.hasNext()) {
	        	Document documento = cursor.next();
	        	RdoDelegacia rdoDelegacia = converter.paraObjeto(documento, RdoDelegacia.class);
	        	rdoDelegacias.add(rdoDelegacia);
	        }
	    } finally {
	        cursor.close();
	    }
	    
		return rdoDelegacias;
	}

	
}
