package grisu.gricli.completors;

import java.util.List;

import jline.Completor;
import jline.SimpleCompletor;
import grisu.gricli.GricliEnvironment;

public class VarCompletor implements Completor {
	
	SimpleCompletor sc;
	
	public VarCompletor(){
		sc = new SimpleCompletor(GricliEnvironment.getVariables().toArray(new String[] {}));
	}

	@SuppressWarnings("unchecked")
	public int complete(String s, int i, List l) {
		return sc.complete(s,i,l);
	}

}
