package br.com.fences.autenticacaobackend.dao;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import br.com.fences.autenticacaobackend.dao.config.ColecaoRdoUsuario;
import br.com.fences.autenticacaoentidade.rdo.usuario.RdoUsuario;
import br.com.fences.autenticacaoentidade.usuario.Usuario;
import br.com.fences.fencesutils.conversor.converter.Converter;
import br.com.fences.fencesutils.formatar.FormatarData;


@RequestScoped
public class RdoUsuarioDAO {

	@Inject
	private transient Logger logger;

	@Inject @ColecaoRdoUsuario
	private MongoCollection<Document> colecao;
	
	@Inject
	private Converter<RdoUsuario> converter;
	
	public void adicionar(RdoUsuario rdoUsuario) {

		if (rdoUsuario != null) {
			boolean existeRegistro = consultarRgUsuario(rdoUsuario.getRgUsuario()) != null ? true : false;
			if (!existeRegistro)
			{
				try
				{
					rdoUsuario.setRdoDelegacia(null); //-- nao deve incluir essa informacao no banco, ela apenas eh relacionada na aplicacao
					Document documento = converter.paraDocumento(rdoUsuario);
					colecao.insertOne(documento);
				}
				catch (Exception e)
				{
					String msg = "Erro na inclusao unica. rgUsuario[" + rdoUsuario.getRgUsuario() + "].";
					logger.error(msg, e);
					throw new RuntimeException(msg, e);
				}
			}
			else
			{
				throw new RuntimeException("O rgUsuario[" + rdoUsuario.getRgUsuario() + "] já existe.");	
			}
		}
	}
	
	public void atualizar(RdoUsuario rdoUsuario) {
		try
		{
			rdoUsuario.setRdoDelegacia(null); //-- nao deve incluir essa informacao no banco, ela apenas eh relacionada na aplicacao
			Document documento = converter.paraDocumento(rdoUsuario);
			colecao.replaceOne(eq("_id", documento.get("_id")), documento);
		}
		catch (Exception e)
		{
			String msg = "Erro na alteracao. rgUsuario[" + rdoUsuario.getRgUsuario() + "].";
			logger.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}
	
	public void adicionarOuAtualizar(RdoUsuario rdoUsuario)
	{
		if (rdoUsuario != null)
		{
			RdoUsuario rdoUsuarioCadastrado = consultarRgUsuario(rdoUsuario.getRgUsuario());
			if (rdoUsuarioCadastrado == null)
			{	//-- incluir
				adicionar(rdoUsuario);
			}
			else
			{	//-- alterar
				String idDoUsuarioCadastrado = rdoUsuarioCadastrado.getId();
				rdoUsuario.setId(idDoUsuarioCadastrado);
				atualizar(rdoUsuario);
			}
		}
	}

	
	public RdoUsuario consultarRgUsuario(final String rgUsuario) 
	{
		RdoUsuario rdoUsuario = null;
	    Document documento = colecao.find(eq("RG_USUARIO", rgUsuario)).first();
	    if (documento != null)
	    {
	    	rdoUsuario = converter.paraObjeto(documento, RdoUsuario.class);
	    }
	    return rdoUsuario;
	}

	public List<RdoUsuario> listarRdoUsuarios() {
		
		List<RdoUsuario> rdoUsuarios = new ArrayList<>();
		
	    MongoCursor<Document> cursor = colecao.find().iterator();
		
	    try {
	        while (cursor.hasNext()) {
	        	Document documento = cursor.next();
	        	RdoUsuario rdoUsuario = converter.paraObjeto(documento, RdoUsuario.class);
	        	rdoUsuarios.add(rdoUsuario);
	        }
	    } finally {
	        cursor.close();
	    }
	    
		return rdoUsuarios;
	}

	
}
