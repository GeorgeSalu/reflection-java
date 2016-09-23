package br.com.devmedia.minijpa.persistence.service;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.devmedia.minijpa.persistence.Column;
import br.com.devmedia.minijpa.persistence.Entity;
import br.com.devmedia.minijpa.persistence.Id;
import br.com.devmedia.minijpa.persistence.ManyToOne;
import br.com.devmedia.minijpa.persistence.Table;
import br.com.devmedia.minijpa.persistence.exception.DevMediaExceptionMap;
import br.com.devmedia.minijpa.persistence.util.ReflectionUtil;

public class QueryGenerator {
	List<Object> fields;
	private static final String DML_INSERT_PARTS[]={"INSERT INTO ","VALUES"};
	private static final String DML_UPDATE_PARTS[]={"UPDATE "," SET "," WHERE "};
	private static final String DML_DELETE_PARTS[]={"DELETE FROM "," WHERE "};
	private static final String SELECT_BY_ID[]={"SELECT p.* FROM "," WHERE "};
	public QueryGenerator(){
		fields= new ArrayList<Object>();
	}
	public String createDmlInsert(Object objNaoPersistido) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder initDml = new StringBuilder(DML_INSERT_PARTS[0]);
		StringBuilder values= new StringBuilder();
		initDml.append(objNaoPersistido.getClass().getAnnotation(Table.class).name()).append(" (");
		List<Field> fields=ReflectionUtil.getFieldsWhithAnnotation(objNaoPersistido.getClass().getDeclaredFields(), Column.class);
		for (Field field : fields) {
			initDml.append(field.getAnnotation(Column.class).name()).append(",");
			this.fields.add(isData(field.get(objNaoPersistido)));
			values.append("?,");
		}
		fields=ReflectionUtil.getFieldsWhithAnnotation(objNaoPersistido.getClass().getDeclaredFields(), ManyToOne.class);
		for (Field field : fields) {
			Object valueFK=field.get(objNaoPersistido);
			List<Field> fieldsRelacionado=ReflectionUtil.getFieldsWhithAnnotation(valueFK.getClass().getDeclaredFields(), Id.class);
			if(fieldsRelacionado!=null && !fieldsRelacionado.isEmpty()){
				Field fieldIdFK=fieldsRelacionado.get(0);
				Object valueFieldIdFK=fieldIdFK.get(valueFK);
				if(valueFieldIdFK!=null){
					initDml.append(field.getAnnotation(ManyToOne.class).fieldFK()).append(",");
					this.fields.add(isData(valueFieldIdFK));
					values.append("?,");
				}
			}
		}
		initDml = extractLastIndexString(initDml.toString(),",");
		initDml.append(")").append(DML_INSERT_PARTS[1]).append("(");
		initDml.append(values);
		return initDml.toString().substring(0, initDml.toString().lastIndexOf(","))+")";
	}
	private StringBuilder extractLastIndexString(String initDml,String str) {
		return new StringBuilder(initDml.substring(0, initDml.lastIndexOf(str)));
	}
	private Object isData(Object value){
		if(value instanceof Date){
			return new Timestamp(((Date)value).getTime());
		}
		return value;
	}
	public Object[] getArgs() {
		return fields.toArray();
	}
	
	public String createDmlUpdate(Object objAtualizando, Object objOriginal) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder initDml = new StringBuilder(DML_UPDATE_PARTS[0]);
		initDml.append(objAtualizando.getClass().getAnnotation(Table.class).name());
		initDml.append(DML_UPDATE_PARTS[1]);
		List<Field> fieldsForUpdate=getFieldsForUpdate(objAtualizando,objOriginal);
		for (Field field : fieldsForUpdate) {
			initDml.append(field.getAnnotation(Column.class).name()+" = ?,");
			this.fields.add(isData(field.get(objAtualizando)));
		}
		initDml = extractLastIndexString(initDml.toString(),",");
		initDml.append(DML_UPDATE_PARTS[2]);
		Field id= ReflectionUtil.getFieldsWhithAnnotation(objAtualizando.getClass().getDeclaredFields(), Id.class).get(0);
		initDml.append(id.getAnnotation(Column.class).name()+"= ?");
		this.fields.add(isData(id.get(objAtualizando)));
		return initDml.toString();
	}

	private List<Field> getFieldsForUpdate(Object objAtualizando,
			Object objOriginal) throws IllegalArgumentException, IllegalAccessException {
		List<Field> fieldsForUpdate=new ArrayList<Field>();
		List<Field> fieldsOriginal=ReflectionUtil.getFieldsWhithAnnotation(objOriginal.getClass().getDeclaredFields(), Column.class);
		List<Field> fieldsAtualizando=ReflectionUtil.getFieldsWhithAnnotation(objAtualizando.getClass().getDeclaredFields(), Column.class);
		for (Field fieldOriginal : fieldsOriginal) {
			for (Field fieldAtualizando : fieldsAtualizando) {
				if(fieldOriginal.getName().equals(fieldAtualizando.getName()) && !fieldOriginal.get(objOriginal).equals(fieldAtualizando.get(objAtualizando))){
					fieldsForUpdate.add(fieldAtualizando);
				}
			}
		}
		return fieldsForUpdate;
	}
	

	public String createDmlDelete(Object o) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder initDml = new StringBuilder(DML_DELETE_PARTS[0]);
		initDml.append(o.getClass().getAnnotation(Table.class).name());
		initDml.append(DML_DELETE_PARTS[1]);
		Field field = ReflectionUtil.getFieldsWhithAnnotation(o.getClass().getDeclaredFields(), Id.class).get(0);
		initDml.append(field.getAnnotation(Column.class).name()+"=? ");
		fields.add(isData(field.get(o)));
		return initDml.toString();
	}

	public String createSelectById( Class clazz,Object o) throws DevMediaExceptionMap{
		StringBuilder selectById = new StringBuilder(SELECT_BY_ID[0]);
		if(!clazz.isAnnotationPresent(Table.class)&& !clazz.isAnnotationPresent(Entity.class)){
			throw new DevMediaExceptionMap("A classe usada não está anotada da maneira correta");
		}
		selectById.append(((Table)clazz.getAnnotation(Table.class)).name()).append(" p").append(SELECT_BY_ID[1]);
		List<Field> fields=ReflectionUtil.getFieldsWhithAnnotation(clazz.getDeclaredFields(), Id.class);
		if(fields.isEmpty()|| fields.size()!=1){
			throw new DevMediaExceptionMap("A classe usada não está anotada da maneira correta");
		}
		Field field = fields.get(0);
		selectById.append("p.").append(field.getAnnotation(Column.class).name()+"=?");
		this.fields.add(isData(o));
		return selectById.toString();
	}
	public String createSelectByFk(Class<?> clazzAssociate, String fieldFK,
			Class<?> clazzReturn) throws DevMediaExceptionMap {
		StringBuilder queryFK= createQuerywhitiJoin(clazzAssociate, clazzReturn);
		Field fieldPK=getFieldPK(clazzAssociate);
		queryFK.append(" p.").append(fieldFK).append("=").append("s."+fieldPK.getAnnotation(Column.class).name());
		queryFK.append(" AND p."+getFieldPK(clazzReturn).getAnnotation(Column.class).name()+"=?");
		return queryFK.toString();
	}
	private Field getFieldPK(Class<?> clazz) throws DevMediaExceptionMap {
		List<Field> fields=ReflectionUtil.getFieldsWhithAnnotation(clazz.getDeclaredFields(), Id.class);
		if(fields.isEmpty()|| fields.size()!=1){
			throw new DevMediaExceptionMap("A classe usada não está anotada da maneira correta");
		}
		return fields.get(0);
	}
	public String createSelectTheOneToMany(Class<?> clazzAssociate, String fieldFK,
			Class<?> clazzReturn) throws DevMediaExceptionMap {
		StringBuilder queryOneToMany=createQuerywhitiJoin(clazzAssociate, clazzReturn);
		Field fieldFoK=getFieldPK(clazzReturn);
		queryOneToMany.append(" s.").append(fieldFK).append("=").append("p."+fieldFoK.getAnnotation(Column.class).name());
		queryOneToMany.append(" AND p."+fieldFoK.getAnnotation(Column.class).name()+"=?");
		return queryOneToMany.toString();
	}
	private StringBuilder createQuerywhitiJoin(Class<?> clazzAssociate,
			Class<?> clazzReturn) {
		StringBuilder queryJoinTwoTables= new StringBuilder("SELECT s.* FROM ");
		queryJoinTwoTables.append(((Table)clazzAssociate.getAnnotation(Table.class)).name()).append(" s, ");
		queryJoinTwoTables.append(((Table)clazzReturn.getAnnotation(Table.class)).name()).append(" p WHERE");
		return queryJoinTwoTables;
	}
}
