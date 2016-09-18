package br.com.devmedia.reflection.example.frontcontroller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.devmedia.reflection.example.action.Action;

/**
 * Servlet implementation class DevMediaController
 */
@WebServlet(urlPatterns={"*.action"},initParams={@WebInitParam(name="actions",value="br.com.devmedia.reflection.example.action")})
public class DevMediaController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String SEPARATOR="!"; 
    private ResourceBundle actionMap=ResourceBundle.getBundle("actionConfig");
    private static Map<String,Class<? extends Action>> actions=null;
    public DevMediaController() {
        super();
       
    }
    @Override
    public void init() throws ServletException {
    	System.out.println("Controladora Inicializada");
    	String act=getServletConfig().getInitParameter("actions");
    	List<String> actionArray = new ArrayList<String>();
		act=act.replace('.', '/');
		if(act.contains(",")){
			String[] acts=act.split(",");
			for (String string : acts) {
				actionArray.add(string);
			}
		}else{
			actionArray.add(act);
		}
		try {
			actions=getClasses(actionArray);
		} catch (Exception e) {
			throw new ServletException("Erro ao Mapear as Actions no web.xml",e);
		}
    }
	private Map<String, Class<? extends Action>> getClasses(
			List<String> packages) throws IOException, ClassNotFoundException {
		Map<String, Class<? extends Action>> classe= new HashMap<String, Class<? extends Action>>();
		for(String pack:packages){
			ClassLoader loader= Thread.currentThread().getContextClassLoader();
			Enumeration<URL> urls;
			urls=loader.getResources(pack);	
			while (urls.hasMoreElements()) {
	                String urlPath = urls.nextElement().getFile();
	                urlPath = URLDecoder.decode(urlPath, "UTF-8");
	                File file = new File(urlPath);
	                if (file.isDirectory()) {
	                    loadImplementationsInDirectory(pack, file,classe);
	                }
			}
		}
		return classe;
	}
	private void loadImplementationsInDirectory(String pack, File file,
			Map<String, Class<? extends Action>> classe) throws ClassNotFoundException {
		File[] files = file.listFiles();
		pack=pack.replace('/', '.');    	 
   	 	List<String> classes = new LinkedList<String>();
   	 	for (File file2 : files) {
			String fqn= file2.getAbsolutePath();
			if(fqn.contains(".class")){
				String externalName = fqn.replace('\\', '.').substring(0, fqn.indexOf("."));
				externalName=fqn.substring(fqn.indexOf(pack.substring(0,pack.indexOf('.'))), fqn.length()).replace('\\', '.');
				externalName=externalName.substring(0, externalName.indexOf(".class"));
				classes.add(externalName);
			}
		}
   	 	System.out.println("Actions localizadas");
   	 	for (String clazz : classes) {
   	 		Class<? extends Action> class1=(Class<? extends Action>) Class.forName(clazz);
			if(class1.isAnnotationPresent(br.com.devmedia.reflection.example.action.annotation.Action.class)){
				System.out.println(class1.toString());
				classe.put(class1.getAnnotation(br.com.devmedia.reflection.example.action.annotation.Action.class).actionName(), class1);
			}
		}
		
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String result;
		if(actions!=null && !actions.isEmpty()){
			result=executeActionInMap(request,response);
		}else{
			result=executeActionInProperties(request,response);
		}
		request.getRequestDispatcher(result).forward(request, response);
	}
	private String executeActionInMap(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		String result;
		try {
			Class actionClass=actions.get(getActionName(request));
			Method actionMethod =getRealMethod(request,actionClass);
			Object actionInstanciada =actionClass.newInstance();
			if(!(actionInstanciada instanceof Action)){
				throw new ServletException("Classe Informada não é uma classe de ação");
			}
			Action action =(Action) actionInstanciada;
			result=action.execute(request, response, actionMethod);
		} catch (Exception e) {
				e.printStackTrace();
				throw new ServletException(e);
		}
		return result;
	}
	private String executeActionInProperties(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		String result;
		try {
			Class actionClass=Class.forName(actionMap.getString(getActionName(request)));
			Method actionMethod =getRealMethod(request,actionClass);
			Object actionInstanciada =actionClass.newInstance();
			if(!(actionInstanciada instanceof Action)){
				throw new ServletException("Classe Informada não é uma classe de ação");
			}
			Action action =(Action) actionInstanciada;
			result=action.execute(request, response, actionMethod);
		} catch (Exception e) {
				e.printStackTrace();
				throw new ServletException(e);
		}
		return result;
	}
	private Method getRealMethod(HttpServletRequest request, Class actionClass) {
		Method method=null;
		try {
			method=actionClass.getMethod(getMethodName(request), new Class[0]);
		} catch (Exception e) {
			e.printStackTrace();
			if(actionClass!=Object.class){
				method=getRealMethod(request, actionClass.getSuperclass());
			}
		}
		return method;
	}

	// actionName!methodName	
	private String getActionName(HttpServletRequest req) {
		return req.getRequestURI().replace(req.getContextPath()+"/","").replace(".action", "").split(SEPARATOR)[0];
	}
	
	private String getMethodName(HttpServletRequest req){		
		return req.getRequestURI().replace(req.getContextPath()+"/","").replace(".action", "").split(SEPARATOR)[1];
	}
}
