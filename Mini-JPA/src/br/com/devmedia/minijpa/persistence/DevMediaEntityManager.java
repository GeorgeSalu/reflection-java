package br.com.devmedia.minijpa.persistence;

import java.sql.SQLException;
import java.util.ResourceBundle;

import br.com.devmedia.minijpa.persistence.exception.DevMediaPersistenceException;
import br.com.devmedia.minijpa.persistence.service.CrudService;
import br.com.devmedia.minijpa.persistence.service.GeneratorList;
import br.com.devmedia.minijpa.persistence.service.QueryGenerator;

public class DevMediaEntityManager {
	ResourceBundle conf =ResourceBundle.getBundle(Persistence.confName);
	private CrudService service;
	private QueryGenerator queryGeneretor;
	private GeneratorList generatorList;
	DevMediaEntityManager (){
		super();
		try {
			service = new CrudService(conf.getString("driver"),conf.getString("url"),conf.getString("login"),conf.getString("password"),conf.getString("showQueries"));
			generatorList = new GeneratorList();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void clear() {
		this.queryGeneretor=null;
	}

	private void init() {
		this.queryGeneretor=new QueryGenerator();
	}

	public void persist(Object o){
		try {
			init();
			service.insert(queryGeneretor.createDmlInsert(o), queryGeneretor.getArgs());
			clear();
		} catch (IllegalArgumentException e) {
			throw new DevMediaPersistenceException(e);
		} catch (SQLException e) {
			throw new DevMediaPersistenceException("Ocorre um erro durante a inserção na base de dados.",e);
		} catch (IllegalAccessException e) {
			throw new DevMediaPersistenceException(e);
		}
		
	}

}
