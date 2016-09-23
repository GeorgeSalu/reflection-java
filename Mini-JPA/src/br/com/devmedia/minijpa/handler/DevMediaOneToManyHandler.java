package br.com.devmedia.minijpa.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

import br.com.devmedia.minijpa.persistence.OneToMany;
import br.com.devmedia.minijpa.persistence.exception.DevMediaExceptionMap;
import br.com.devmedia.minijpa.persistence.service.CrudService;
import br.com.devmedia.minijpa.persistence.service.GeneratorList;
import br.com.devmedia.minijpa.persistence.service.QueryGenerator;

public class DevMediaOneToManyHandler implements InvocationHandler {

	
	private QueryGenerator queryGenerator;
	private GeneratorList generatorList;
	private CrudService crudService;
	private OneToMany oneToMany;
	private Class entity;
	private String clazzMapping;
	private Object idValue;
	public DevMediaOneToManyHandler(QueryGenerator queryGenerator,
			GeneratorList generatorList, CrudService crudService, String clazzMapping,
			Class entity,Object idValue, OneToMany oneToMany) {
		this.queryGenerator=queryGenerator;
		this.generatorList=generatorList;
		this.crudService=crudService;
		this.entity=entity;
		this.clazzMapping=clazzMapping;
		this.idValue=idValue;
		this.oneToMany=oneToMany;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		return method.invoke(getList(), args);
	}
	private String getQuery() throws DevMediaExceptionMap{
		return getQueryGenerator().createSelectTheOneToMany(getOneToMany().clazzAssociate(),getOneToMany().fieldFK(),getEntity());
	}
	private QueryGenerator getQueryGenerator() {
		return queryGenerator;
	}
	public List getList() throws ClassNotFoundException, DevMediaExceptionMap, InstantiationException, IllegalAccessException, SQLException{
		return getGeneratorList().
				createListResult(getCrudService().executeSelect(getQuery(),new Object[]{idValue}),
				 true,getClazzMapping(),getQuery());
	}
	private GeneratorList getGeneratorList() {
		return generatorList;
	}
	private CrudService getCrudService() {
		return crudService;
	}
	private OneToMany getOneToMany() {
		return oneToMany;
	}
	private Class getEntity() {
		return entity;
	}
	private String getClazzMapping() {
		return clazzMapping;
	}

}
