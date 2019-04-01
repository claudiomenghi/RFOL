package lu.uni.rfol.shifting.intervalshif;

import lu.uni.rfol.formulae.RSFOLFormula;

public class IntervalShifting {

	private IntevalShiftingVisitor shifted;

	public RSFOLFormula perform(RSFOLFormula f) {
		shifted=new IntevalShiftingVisitor(f);
		return f.accept(shifted);
	}
	
	public float getFormulaShift() {
		return shifted.getSimulationShifting();
		
	}

}
