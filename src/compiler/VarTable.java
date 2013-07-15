package compiler;

import java.util.ArrayList;

public class VarTable {

	ArrayList<Var> table = new ArrayList<Var>();
	
	public VarTable(){
	}
	
	public Var get(String varName){
		for (Var v: table){
			if (v.name.equals(varName))
				return v;
		}
		return null;
	}

	public boolean set(Var v){
		if (exists(v.name)){
			int index = table.indexOf(get(v.name));
			table.set(index, v);
			return true;
		}
		return table.add(v);
	}	
	
	public boolean exists(String varName){
		for (Var v: table){
			if (v.name.equals(varName))
				return true;
		}
		return false;
	}
}
