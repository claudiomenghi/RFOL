package uni.lu.rfol.shifting.intervalshif;

import uni.lu.rfol.formulae.RSFOLFormula;

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
