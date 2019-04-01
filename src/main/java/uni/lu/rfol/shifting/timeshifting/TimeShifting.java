package uni.lu.rfol.shifting.timeshifting;

import uni.lu.rfol.formulae.RSFOLFormula;

public class TimeShifting {

	public RSFOLFormula perform(RSFOLFormula f) {
		return f.accept(new TimeShiftVisitor(f));

	}
}
