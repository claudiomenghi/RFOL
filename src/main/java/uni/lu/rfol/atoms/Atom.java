package uni.lu.rfol.atoms;

import uni.lu.rfol.formulae.RSFOLFormula;

public interface Atom  extends RSFOLFormula{

	/**
	 * returns true if and only if the atom predicates on a signal whose time term is a constant 
	 * @return true if and only if the atom predicates on a signal whose time term is a constant 
	 */
	public boolean refersToConstantInstant();
	

	
}
