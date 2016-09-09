package br.com.devmedia.reflection.example.field;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;

public class MainManipulaField {
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		
		Veiculo veiculo = new Veiculo();
		veiculo.setMarca("Fiat");
		veiculo.setPeso(555.0);
		veiculo.setPotencia(1.2);
		veiculo.setQuantidadeMaximaPassageiros(5);
		veiculo.setValor(new BigDecimal("20450.0"));
		
		Class clazz = veiculo.getClass();
		
		Field[] fields = clazz.getFields();
		System.out.println("Quantidade de fields : "+fields.length);
		
		fields = clazz.getDeclaredFields();
		
		System.out.println("Quantidade de fields : "+fields.length);
		 
		showValuesInField(fields,veiculo);
		
		System.out.println("Valor apos o set via reflexao : ");
		System.out.println("Marca nova : "+veiculo.getMarca());
		
	}

	private static void showValuesInField(Field[] fields, Object obj) throws IllegalArgumentException, IllegalAccessException {

		for(Field field : fields){
			
			if(Modifier.isPrivate(field.getModifiers())){
				field.setAccessible(true);
			}
			
			System.out.println(field.getName() + " Valor : "+field.get(obj));
			if(field.getName().equals("marca")){
				field.set(obj, "GM");
			}
		}
		
	}
}