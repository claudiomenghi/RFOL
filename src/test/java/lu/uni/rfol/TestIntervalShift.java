package lu.uni.rfol;

import static org.junit.Assert.*;

import org.junit.Test;

import lu.uni.rfol.atoms.SignalConstantComparison;
import lu.uni.rfol.atoms.Value;
import lu.uni.rfol.formulae.BinaryFormula;
import lu.uni.rfol.formulae.Bound;
import lu.uni.rfol.formulae.ExistsFormula;
import lu.uni.rfol.formulae.ForallFormula;
import lu.uni.rfol.formulae.RSFOLFormula;
import lu.uni.rfol.shifting.intervalshif.IntervalShifting;
import lu.uni.rfol.shifting.timeshifting.TimeShifting;
import lu.uni.rfol.timedterm.TimedTermNumber;
import lu.uni.rfol.timedterm.TimedTermVariable;

public class TestIntervalShift {

	@Test
	public void testNoTimeShiftingAppliedIfNotNeeded() {
		RSFOLFormula f = new ForallFormula(
				new Bound(new Tvariable("t"), new TimedTermNumber(3), new TimedTermNumber(5), true, true),
				new BinaryFormula(
				
						new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermVariable(new Tvariable("t"))),
								RELOP.GEQ, new Value(2)),LOGICOP.CONJ,
				new ForallFormula(
						new Bound(new Tvariable("t1"), new TimedTermNumber(2), new TimedTermNumber(4), true, true),
						new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermVariable(new Tvariable("t1"))),
								RELOP.GEQ, new Value(2)))
						
						));

		RSFOLFormula f1 = new IntervalShifting().perform(f);

		assertEquals(f1.toString(), "(for all t:(3.0,5.0).((s(t+0.0)>=2.0)and(for all t1:(2.0,4.0).(s(t1+0.0)>=2.0))))");
	}
	
	
	@Test
	public void testNoTimeShiftingAppliedIfNotNeeded2() {
		RSFOLFormula f = new ForallFormula(
				new Bound(new Tvariable("t"), new TimedTermNumber(3), new TimedTermNumber(5), true, true),
				new BinaryFormula(
				
						new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermVariable(new Tvariable("t"))),
								RELOP.GEQ, new Value(2)),LOGICOP.CONJ,
				new ForallFormula(
						new Bound(new Tvariable("t1"), new TimedTermNumber(2), new TimedTermNumber(6), true, true),
						new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermVariable(new Tvariable("t1"))),
								RELOP.GEQ, new Value(2)))
						
						));

		RSFOLFormula f1 = new IntervalShifting().perform(f);

		assertEquals(f1.toString(), "(for all t:(4.0,6.0).((s(t+-1.0)>=2.0)and(for all t1:(2.0,6.0).(s(t1+0.0)>=2.0))))");
		
	}
	
	@Test
	public void testNoTimeShiftingAppliedIfNotNeeded3() {
		RSFOLFormula f = new ForallFormula(
				new Bound(new Tvariable("t"), new TimedTermNumber(3), new TimedTermNumber(5), true, true),
				new BinaryFormula(
				
						new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermVariable(new Tvariable("t"))),
								RELOP.GEQ, new Value(2)),LOGICOP.CONJ,
				new ExistsFormula(
						new Bound(new Tvariable("t1"), new TimedTermNumber(2), new TimedTermNumber(6), true, true),
						new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermVariable(new Tvariable("t1"))),
								RELOP.GEQ, new Value(2)))
						
						));
		System.out.println(f);
		RSFOLFormula f1 = new IntervalShifting().perform(f);

		
		System.out.println(f1);
		assertEquals(f1.toString(), "(for all t:(6.0,8.0).((s(t+-3.0)>=2.0)and(exists t1:(2.0,6.0).(s(t1+0.0)>=2.0))))");
		
	}


}
