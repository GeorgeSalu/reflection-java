package br.com.devmedia.reflection.example.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {
	public String execute(HttpServletRequest request,HttpServletResponse response,Method method)throws IllegalArgumentException, IllegalAccessException, InvocationTargetException ;
}
