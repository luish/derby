package compiler;

public class Var {
	
	public static enum Type { INT, BOOLEAN, CHAR, STRING }

	public String name;
	public Object value;
	public Var.Type type;
	
	public Var(String name, Type type, Object value){
		this.name = name;
		this.type = type;
		this.value = value;
	}
	
	public String toString(){
		return String.format("%s = %s", name, value);
	}
	
}
