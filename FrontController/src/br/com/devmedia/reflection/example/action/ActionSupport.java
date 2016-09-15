package br.com.devmedia.reflection.example.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ActionSupport implements Action{
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Method method) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		setRequest(request);
		setResponse(response);
		return (String) method.invoke(this, new Object[0]);
	}
	protected HttpServletRequest getRequest() {
		return request;
	}
	private void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	protected HttpServletResponse getResponse() {
		return response;
	}
	private void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	
}
