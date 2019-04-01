package lu.uni.rfol.shifting.timeshifting;

import lu.uni.rfol.formulae.RSFOLFormula;

public class TimeShifting {

	public RSFOLFormula perform(RSFOLFormula f) {
		return f.accept(new TimeShiftVisitor(f));

	}
}
