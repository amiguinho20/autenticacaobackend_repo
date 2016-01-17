package br.com.fences.autenticacaobackend.rest;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.fences.autenticacaobackend.config.Log;
import br.com.fences.autenticacaobackend.negocio.RdoDelegaciaBO;
import br.com.fences.autenticacaobackend.negocio.RdoUsuarioBO;
import br.com.fences.autenticacaobackend.negocio.UsuarioBO;
import br.com.fences.autenticacaoentidade.rdo.delegacia.RdoDelegacia;
import br.com.fences.autenticacaoentidade.rdo.usuario.RdoUsuario;
import br.com.fences.autenticacaoentidade.usuario.Usuario;
import br.com.fences.fencesutils.conversor.InputStreamParaJson;
import br.com.fences.fencesutils.conversor.converter.ColecaoJsonAdapter;

@Log
@RequestScoped
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AutenticacaoResource {
	
	@Inject
	private transient Logger logger;

	@Inject
	private UsuarioBO usuarioBO;
	
	@Inject
	private RdoUsuarioBO rdoUsuarioBO;
	
	@Inject
	private RdoDelegaciaBO rdoDelegaciaBO;
	
	private Gson gson = new GsonBuilder()
			.registerTypeHierarchyAdapter(Collection.class, new ColecaoJsonAdapter())
			.create();

    @GET
    @Path("usuario/consultarId/{id}")
    public String usuarioConsultarId(@PathParam("id") String id) 
    {
    	Usuario usuario = usuarioBO.consultarId(id);
    	String json = gson.toJson(usuario);
    	return json;
    }
    
    @GET
    @Path("usuario/consultarUsername/{username}")
    public String usuarioConsultarUsername(@PathParam("username") String username) 
    {
    	Usuario usuario = usuarioBO.consultarUsername(username);
    	String json = gson.toJson(usuario);
    	return json;
    }
    
    @GET
    @Path("usuario/consultarRg/{rg}")
    public String usuarioConsultarRg(@PathParam("rg") String rg) 
    {
    	Usuario usuario = usuarioBO.consultarRg(rg);
    	String json = gson.toJson(usuario);
    	return json;
    }
    
    @GET
    @Path("usuario/listarUsuarios")
    public String usuarioListarUsuarios() 
    {
    	List<Usuario> usuarios = usuarioBO.listarUsuarios();
    	String json = gson.toJson(usuarios);
    	return json;
    }
    
    
    @PUT
    @Path("usuario/adicionar")
    public void usuarioAdicionar(InputStream ipFiltros) 
    {
    	String json = InputStreamParaJson.converter(ipFiltros);
    	Usuario usuario = gson.fromJson(json, Usuario.class);
    	usuarioBO.adicionar(usuario);
    }
    
    @GET
    @Path("usuario/ativar/{usernameEncoded}")
    public String usuarioAtivar(@PathParam("usernameEncoded") String usernameEncoded) 
    {
    	String retorno = usuarioBO.ativarUsuario(usernameEncoded);
    	return retorno;
    }

    @POST
    @Path("usuario/atualizar")
    public void usuarioAtualizar(InputStream ipFiltros) 
    {
    	String json = InputStreamParaJson.converter(ipFiltros);
    	Usuario usuario = gson.fromJson(json, Usuario.class);
    	usuarioBO.atualizar(usuario);
    }
    
    //-- RdoDelegacia  --------------------------------------------------------------------------
    @GET
    @Path("rdoDelegacia/listarRdoDelegacias")
    public String rdoDelegaciaListarRdoDelegacias() 
    {
    	List<RdoDelegacia> rdoDelegacias = rdoDelegaciaBO.listarRdoDelegacias();
    	String json = gson.toJson(rdoDelegacias);
    	return json;
    }
    
    @GET
    @Path("rdoDelegacia/consultarIdDelegacia/{idDelegacia}")
    public String rdoDelegaciaConsultarIdDelegacia(@PathParam("idDelegacia") String idDelegacia) 
    {
    	RdoDelegacia rdoDelegacia = rdoDelegaciaBO.consultarIdDelegacia(idDelegacia);
    	String json = gson.toJson(rdoDelegacia, RdoDelegacia.class);
    	return json;
    }
    
    @POST
    @Path("rdoDelegacia/adicionarOuAtualizar")
    public void rdoDelegaciaAdicionarOuAtualizar(InputStream ipFiltros)
    {
    	String json = InputStreamParaJson.converter(ipFiltros);
    	RdoDelegacia rdoDelegacia = gson.fromJson(json, RdoDelegacia.class);
    	rdoDelegaciaBO.adicionarOuAtualizar(rdoDelegacia);
    }
    
    //-- RdoUsuario  --------------------------------------------------------------------------
    @GET
    @Path("rdoUsuario/listarRdoUsuarios")
    public String rdoUsuarioListarRdoUsuarios() 
    {
    	List<RdoUsuario> rdoUsuarios = rdoUsuarioBO.listarRdoUsuarios();
    	String json = gson.toJson(rdoUsuarios);
    	return json;
    }
    
    @GET
    @Path("rdoUsuario/consultarRg/{rg}")
    public String rdoUsuarioConsultarRg(@PathParam("rg") String rg) 
    {
    	RdoUsuario rdoUsuario = rdoUsuarioBO.consultarRg(rg);
    	String json = gson.toJson(rdoUsuario, RdoUsuario.class);
    	return json;
    }
    
    @POST
    @Path("rdoUsuario/adicionarOuAtualizar")
    public void rdoUsuarioAdicionarOuAtualizar(InputStream ipFiltros)
    {
    	String json = InputStreamParaJson.converter(ipFiltros);
    	RdoUsuario rdoUsuario = gson.fromJson(json, RdoUsuario.class);
    	rdoUsuarioBO.adicionarOuAtualizar(rdoUsuario);
    }

}
