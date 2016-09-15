package br.com.devmedia.reflection.example.frontcontroller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("*.action")
public class DevMediaController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String SEPARATOR = "!";
	private ResourceBundle actionMap = ResourceBundle.getBundle("actionConfig");
	
	
    public DevMediaController() {
        super();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			Class actionClass = Class.forName(actionMap.getString(getActionName(request)));
			Method actionMethod = getRealMethod(request,actionClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ServletException();
		}
		
	}
	
	private Method getRealMethod(HttpServletRequest request, Class actionClass) {

		Method method = null;
		try{
			method = actionClass.getMethod(getMethodName(request), new Class[0]);
		}catch(Exception e){
			e.printStackTrace();
			if(actionClass != Object.class){
				method=getRealMethod(request, actionClass.getSuperclass());
			}
		}
		
		return method;
	}

	private String getActionName(HttpServletRequest req){
		
		return req.getRequestURI().replace(req.getContextPath()+"/","" ).replace(".action", "").split(SEPARATOR)[0];
		
	}

	private String getMethodName(HttpServletRequest req){
		
		return req.getRequestURI().replace(req.getContextPath()+"/","" ).replace(".action", "").split(SEPARATOR)[1];
		
	}
	
}
