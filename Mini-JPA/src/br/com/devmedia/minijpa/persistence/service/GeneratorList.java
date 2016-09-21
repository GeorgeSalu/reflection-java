package br.com.devmedia.minijpa.persistence.service;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.devmedia.minijpa.persistence.Column;
import br.com.devmedia.minijpa.persistence.Entity;
import br.com.devmedia.minijpa.persistence.Table;
import br.com.devmedia.minijpa.persistence.exception.DevMediaExceptionMap;
import br.com.devmedia.minijpa.persistence.util.ReflectionUtil;

public class GeneratorList {

	public GeneratorList() {
		super();
	}

	public List createListResult(ResultSet resultSelect,boolean isUpperCase,String clazzMapping,
			String query) throws ClassNotFoundException, DevMediaExceptionMap, InstantiationException, IllegalAccessException, SQLException{
		List result=new ArrayList();
		Class clazzReturn=this.findClass(clazzMapping,isUpperCase,query);
		result=createList(resultSelect,clazzReturn);
		return result;
	}

	private List createList(ResultSet resultSelect, Class clazzResult) throws InstantiationException, IllegalAccessException, SQLException {
		List result = new ArrayList();
		List<Field> fields=ReflectionUtil.getFieldsWhithAnnotation(clazzResult.getDeclaredFields(), Column.class);
		while(resultSelect.next()){
			Object instanceResult=clazzResult.newInstance();
			for (Field field : fields) {
				field.set(instanceResult, resultSelect.getObject(field.getAnnotation(Column.class).name()));
			}
			result.add(instanceResult);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private Class findClass(String clazzMapping, boolean isUpperCase, String query) throws ClassNotFoundException, DevMediaExceptionMap {
		String [] clazzes=null;
		String tablesConsultadas=null;
		String camposConsultados=null;
		Class clazzResult=null;
		if(clazzMapping.contains(",")){
			 clazzes =clazzMapping.split(",");
		} 
		int possuiWhere =-1; 
		if(isUpperCase){
			camposConsultados=query.substring(0,query.indexOf("FROM")).trim().replace("SELECT","");
			possuiWhere=query.toUpperCase().indexOf("WHERE");
			tablesConsultadas=query.substring(query.indexOf("FROM"), possuiWhere!= -1? possuiWhere:query.length()).trim().replace("FROM","");
		}else{
			camposConsultados=query.substring(0,query.indexOf("from")).trim().replace("select","");
			possuiWhere=query.toUpperCase().indexOf("where");
			tablesConsultadas=query.substring(query.indexOf("from"), possuiWhere!= -1? possuiWhere:query.length()).trim().replace("from","");
		}
		String tableResult=findTableResult(camposConsultados,tablesConsultadas);
		if(clazzes!=null){
			for (String clazz : clazzes) {			
				clazzResult=Class.forName(clazz);
				if(isTableAndClassMapping(tableResult, clazzResult)){
					return clazzResult;
				}
			}
		}
		clazzResult=Class.forName(clazzMapping);
		if(isTableAndClassMapping(tableResult, clazzResult)){
			return clazzResult;
		}
		throw new DevMediaExceptionMap("Consulta Feita com Sucesso porém não ha classe mapeada para os resultados");
	}

	private String findTableResult(String camposConsultados,
			String tablesConsultadas) {
		String alias=camposConsultados.substring(0,camposConsultados.indexOf(".*")).trim();
		if(tablesConsultadas.contains(",")){
			for(String table:tablesConsultadas.split(",")){
				if(table.trim().contains(alias)){
					return table.trim().substring(0, table.trim().lastIndexOf(alias));
				}
			}
		}
		if(tablesConsultadas.trim().contains(alias)){
			return tablesConsultadas.trim().substring(0, tablesConsultadas.trim().lastIndexOf(alias));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private boolean isTableAndClassMapping(String tablesConsultadas,
			Class clazzResult) {
		return clazzResult.isAnnotationPresent(Entity.class) && clazzResult.isAnnotationPresent(Table.class) &&
				 ((Table)clazzResult.getAnnotation(Table.class)).name().trim().equals(tablesConsultadas.trim());
	}
}
