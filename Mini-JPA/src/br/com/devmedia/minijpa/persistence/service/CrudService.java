package br.com.devmedia.minijpa.persistence.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudService {

	private Connection con;
	private PreparedStatement psmt;
	private ResultSet set;
	private boolean showQuery;
	
	public CrudService(String driver, String url, String login,
			String senha, String showQueries) throws ClassNotFoundException, SQLException {
		 Class.forName(driver);
		 con=DriverManager.getConnection(url, login, senha);
	     this.showQuery=showQueries.contains("ok");
	}
	public int insert(final String query,Object [] args) throws SQLException{
		return executeDML(query,args);
	}
	public int update(final String query,Object [] args) throws SQLException{
		return executeDML(query,args);
	}
	public int remove(final String query,Object [] args) throws SQLException{
		return executeDML(query,args);
	}
	private int executeDML(String query, Object[] args) throws SQLException {
		showQuery(query);
		psmt = con.prepareStatement(query);		
	    setParamiters(args);
	    return psmt.executeUpdate();
	}
	
	private void showQuery(String query) {
		if(showQuery)
			System.out.println(query);
		
	}
	private void setParamiters(Object[] args) throws SQLException{
		int i=1;
		if(args!=null){
			for (Object arg : args) {
				psmt.setObject(i, arg);
				i++;
			}
		}
	}
	public ResultSet executeSelect(final String query,Object [] args) throws SQLException{
		showQuery(query);
		psmt = con.prepareStatement(query);		
	    setParamiters(args);
	    set=psmt.executeQuery();
	    return set;
	}
	public void close() throws SQLException{
		con.close();
		psmt.close();
	}
}
