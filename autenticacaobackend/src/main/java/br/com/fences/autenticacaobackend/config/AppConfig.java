package br.com.fences.autenticacaobackend.config;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppConfig implements Serializable{

	private static final long serialVersionUID = 9159464655935948935L;

	private String logConsole;
	private String logLevel;
	private String logDiretorio;
	private String serverBackendHost;
	private String serverBackendPort;
	private String dbMongoHost;
	private String dbMongoPort;
	private String dbMongoDatabase;
	private String dbMongoUser;
	private String dbMongoPass;
	private String smtpHost;
	private String smtpPort;
	private String smtpFrom;
	private String smtpUsername;
	private String smtpPassword;
	private String smtpSSL;
	private String urlAtivacaoUsuario;
	

	
	public String getLogConsole() {
		return logConsole;
	}
	public void setLogConsole(String logConsole) {
		this.logConsole = logConsole;
	}
	public String getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	public String getLogDiretorio() {
		return logDiretorio;
	}
	public void setLogDiretorio(String logDiretorio) {
		this.logDiretorio = logDiretorio;
	}
	public String getServerBackendHost() {
		return serverBackendHost;
	}
	public void setServerBackendHost(String serverBackendHost) {
		this.serverBackendHost = serverBackendHost;
	}
	public String getServerBackendPort() {
		return serverBackendPort;
	}
	public void setServerBackendPort(String serverBackendPort) {
		this.serverBackendPort = serverBackendPort;
	}
	public String getDbMongoHost() {
		return dbMongoHost;
	}
	public void setDbMongoHost(String dbMongoHost) {
		this.dbMongoHost = dbMongoHost;
	}
	public String getDbMongoPort() {
		return dbMongoPort;
	}
	public void setDbMongoPort(String dbMongoPort) {
		this.dbMongoPort = dbMongoPort;
	}
	public String getDbMongoDatabase() {
		return dbMongoDatabase;
	}
	public void setDbMongoDatabase(String dbMongoDatabase) {
		this.dbMongoDatabase = dbMongoDatabase;
	}
	public String getDbMongoUser() {
		return dbMongoUser;
	}
	public void setDbMongoUser(String dbMongoUser) {
		this.dbMongoUser = dbMongoUser;
	}
	public String getDbMongoPass() {
		return dbMongoPass;
	}
	public void setDbMongoPass(String dbMongoPass) {
		this.dbMongoPass = dbMongoPass;
	}
	public String getSmtpHost() {
		return smtpHost;
	}
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	public String getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}
	public String getSmtpFrom() {
		return smtpFrom;
	}
	public void setSmtpFrom(String smtpFrom) {
		this.smtpFrom = smtpFrom;
	}
	public String getSmtpUsername() {
		return smtpUsername;
	}
	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}
	public String getSmtpPassword() {
		return smtpPassword;
	}
	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}
	public String getSmtpSSL() {
		return smtpSSL;
	}
	public void setSmtpSSL(String smtpSSL) {
		this.smtpSSL = smtpSSL;
	}
	public String getUrlAtivacaoUsuario() {
		return urlAtivacaoUsuario;
	}
	public void setUrlAtivacaoUsuario(String urlAtivacaoUsuario) {
		this.urlAtivacaoUsuario = urlAtivacaoUsuario;
	}

	
}
