package lu.uni.rfol.shifting.intervalshif;

import lu.uni.rfol.formulae.RSFOLFormula;

public class IntervalShifting {

	
	public RSFOLFormula perform(RSFOLFormula f) {
		return f.accept(new IntevalShiftingVisitor());
	}
	

}
