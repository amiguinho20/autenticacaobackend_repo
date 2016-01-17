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

import br.com.fences.autenticacaobackend.dao.config.ColecaoUsuario;
import br.com.fences.autenticacaoentidade.usuario.Usuario;
import br.com.fences.fencesutils.conversor.converter.Converter;
import br.com.fences.fencesutils.formatar.FormatarData;


@RequestScoped
public class UsuarioDAO {

	@Inject
	private transient Logger logger;

	@Inject @ColecaoUsuario
	private MongoCollection<Document> colecao;
	
	@Inject
	private Converter<Usuario> converter;
	
	public void adicionar(Usuario usuario) {

		if (usuario != null) {
			if (consultarUsername(usuario.getUsername()) == null)
			{
				usuario.setAtivo("N");
				usuario.setDataCriacao(dataHoraCorrente());
				usuario.setDataAtualizacao(dataHoraCorrente());
				usuario.setRdoUsuario(null); //-- nao deve incluir essa informacao no banco, ela apenas eh relacionada na aplicacao
				try
				{
					Document documento = converter.paraDocumento(usuario);
					colecao.insertOne(documento);
				}
				catch (Exception e)
				{
					String msg = "Erro na inclusao unica. username[" + usuario.getUsername() + "].";
					logger.error(msg, e);
					throw new RuntimeException(msg, e);
				}
			}
			else
			{
				throw new RuntimeException("O username[" + usuario.getUsername() + "] já existe.");	
			}
		}
	}
	
	public void atualizar(Usuario usuario) {
		try
		{
			usuario.setRdoUsuario(null); //-- nao deve incluir essa informacao no banco, ela apenas eh relacionada na aplicacao
			usuario.setDataAtualizacao(dataHoraCorrente());
			Document documento = converter.paraDocumento(usuario);
			colecao.replaceOne(eq("_id", documento.get("_id")), documento);
		}
		catch (Exception e)
		{
			String msg = "Erro na alteracao. usuario[" + usuario.getUsername() + "].";
			logger.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}
	
	public Usuario consultarId(final String id) 
	{
	    Document documento = colecao.find(eq("_id", new ObjectId(id))).first();
	    Usuario usuario = converter.paraObjeto(documento, Usuario.class);
	    return usuario;
	}
	
	public Usuario consultarUsername(final String username) 
	{
		Usuario usuario = null;
	    Document documento = colecao.find(eq("username", username)).first();
	    if (documento != null)
	    {
	    	usuario = converter.paraObjeto(documento, Usuario.class);
	    }
	    return usuario;
	}
	
	public Usuario consultarRg(final String rg) 
	{
		Usuario usuario = null;
	    Document documento = colecao.find(eq("rg", rg)).first();
	    if (documento != null)
	    {
	    	usuario = converter.paraObjeto(documento, Usuario.class);
	    }
	    return usuario;
	}

	public List<Usuario> listarUsuarios() {
		
		List<Usuario> usuarios = new ArrayList<>();
		
	    MongoCursor<Document> cursor = colecao.find().iterator();
		
	    try {
	        while (cursor.hasNext()) {
	        	Document documento = cursor.next();
	        	Usuario usuario = converter.paraObjeto(documento, Usuario.class);
	        	usuarios.add(usuario);
	        }
	    } finally {
	        cursor.close();
	    }
	    
		return usuarios;
	}

	private String dataHoraCorrente()
	{
		String ultimaAtualizacao = FormatarData.getAnoMesDiaHoraMinutoSegundoConcatenados().format(new Date());
		return ultimaAtualizacao; 
	}

	
}
