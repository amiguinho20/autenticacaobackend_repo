package br.com.fences.autenticacaobackend.dao.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;

import br.com.fences.autenticacaobackend.config.AppConfig;
import br.com.fences.fencesutils.verificador.Verificador;

@ApplicationScoped
public class MongoProvider {  

	private static final String COLECAO_USUARIO = "usuario01";   
	private static final String COLECAO_RDO_USUARIO = "rdo_usuario01";
	private static final String COLECAO_RDO_DELEGACIA = "rdo_delegacia01";
	
	private MongoClient conexao;
	private MongoDatabase banco;
	private MongoCollection<Document> colecaoUsuario;
	private MongoCollection<Document> colecaoRdoUsuario;
	private MongoCollection<Document> colecaoRdoDelegacia;
	
	@Inject
	private AppConfig appConfig;
	
	@PostConstruct
	public void abrirConexao() 
	{
		String dbMongoHost = appConfig.getDbMongoHost();
		String dbMongoPort = appConfig.getDbMongoPort();
		String dbMongoDatabase = appConfig.getDbMongoDatabase();
		String dbMongoUser = appConfig.getDbMongoUser();
		String dbMongoPass = appConfig.getDbMongoPass();
		
		if (Verificador.isValorado(dbMongoUser))
		{
			String uriConexao = String.format("mongodb://%s:%s@%s:%s/%s", dbMongoUser, dbMongoPass, dbMongoHost, dbMongoPort, dbMongoDatabase);
			MongoClientURI uri  = new MongoClientURI(uriConexao); 
			conexao = new MongoClient(uri);
		}
		else
		{
			conexao = new MongoClient(dbMongoHost, Integer.parseInt(dbMongoPort));
		}
		banco = conexao.getDatabase(dbMongoDatabase);
		
		colecaoUsuario = banco.getCollection(COLECAO_USUARIO);
		if (colecaoUsuario == null)
		{
			banco.createCollection(COLECAO_USUARIO);
			colecaoUsuario = banco.getCollection(COLECAO_USUARIO);
			   
			BasicDBObject campos = new BasicDBObject();
			campos.append("username", 1);
			
			IndexOptions opcoes =  new IndexOptions();
			opcoes.unique(true);
			
			colecaoUsuario.createIndex(campos, opcoes);
			
			BasicDBObject campos2 = new BasicDBObject();
			campos2.append("rg", 1);
			
			colecaoUsuario.createIndex(campos2, opcoes);
		}
		
		colecaoRdoUsuario = banco.getCollection(COLECAO_RDO_USUARIO);
		if (colecaoRdoUsuario == null)
		{
			banco.createCollection(COLECAO_RDO_USUARIO);
			colecaoRdoUsuario = banco.getCollection(COLECAO_RDO_USUARIO);
			   
			BasicDBObject campos = new BasicDBObject();
			campos.append("RG_USUARIO", 1);
			
			IndexOptions opcoes =  new IndexOptions();
			opcoes.unique(true);
			
			colecaoRdoUsuario.createIndex(campos, opcoes);
			
		}
		
		colecaoRdoDelegacia = banco.getCollection(COLECAO_RDO_DELEGACIA);
		if (colecaoRdoDelegacia == null)
		{
			banco.createCollection(COLECAO_RDO_DELEGACIA);
			colecaoRdoDelegacia = banco.getCollection(COLECAO_RDO_DELEGACIA);
			   
			BasicDBObject campos = new BasicDBObject();
			campos.append("ID_DELEGACIA", 1);
			
			IndexOptions opcoes =  new IndexOptions();
			opcoes.unique(true);
			
			colecaoRdoDelegacia.createIndex(campos, opcoes);
			
		}
		
	}
	
	/**
	 * Fechar a conexao com o banco quando o objeto for destruido.
	 */
	@PreDestroy
	public void fecharConecao()
	{
		conexao.close();
	}
	
	@Produces @ColecaoUsuario
	public MongoCollection<Document> getColecaoUsuario()
	{
		return colecaoUsuario;
	}
	
	@Produces @ColecaoRdoUsuario
	public MongoCollection<Document> getColecaoRdoUsuario()
	{
		return colecaoRdoUsuario;
	}
	
	@Produces @ColecaoRdoDelegacia
	public MongoCollection<Document> getColecaoRdoDelegacia()
	{
		return colecaoRdoDelegacia;
	}
	
}
