package br.com.devmedia.reflection.example.frontcontroller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.devmedia.reflection.example.action.Action;

/**
 * Servlet implementation class DevMediaController
 */
@WebServlet("*.action")
public class DevMediaController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private ResourceBundle actionMap=ResourceBundle.getBundle("actionConfig");
	private static final String SEPARATOR="!";
	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String actionMapeada=actionMap.getString(getActionName(request));
		String resultPage;
		try {
			Class actionClass=Class.forName(actionMapeada);
			Method actionMethod=getRealMethod(request,actionClass);
			Object actionInstanciada = actionClass.newInstance();
			if(!(actionInstanciada instanceof Action)){
				throw new ServletException("Classe Mapeada não é uma classe de ação");
			}
			Action action =(Action) actionInstanciada;
			resultPage=action.execute(request, response, actionMethod);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
		request.getRequestDispatcher(resultPage).forward(request, response);
	}
	private Method getRealMethod(HttpServletRequest request, Class actionClass) throws SecurityException, NoSuchMethodException {
		Method method=null;
		try {
			method=actionClass.getMethod(getMethodName(request), new Class[0]);
		} catch (Exception e) {
			e.printStackTrace();
			if(actionClass!=Object.class){
				method=actionClass.getSuperclass().getMethod(getMethodName(request), new Class[0]);
			}
		}
		return method;
	}
// contextoApp/actionName!methodName.action
	private String getActionName(HttpServletRequest req) {
		return req.getRequestURI().replace(req.getContextPath()+"/","").replace(".action", "").split(SEPARATOR)[0];
	}
	
	private String getMethodName(HttpServletRequest req){		
		return req.getRequestURI().replace(req.getContextPath()+"/","").replace(".action", "").split(SEPARATOR)[1];
	}
}
