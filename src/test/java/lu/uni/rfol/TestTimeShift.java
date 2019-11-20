package lu.uni.rfol;

import static org.junit.Assert.*;

import java.security.cert.PKIXRevocationChecker.Option;

import org.junit.Test;

import lu.uni.rfol.atoms.SignalComparison;
import lu.uni.rfol.atoms.SignalConstantComparison;
import lu.uni.rfol.atoms.Value;
import lu.uni.rfol.expression.ArithmeticOperator;
import lu.uni.rfol.formulae.BinaryFormula;
import lu.uni.rfol.formulae.Bound;
import lu.uni.rfol.formulae.ForallFormula;
import lu.uni.rfol.formulae.RSFOLFormula;
import lu.uni.rfol.shifting.timeshifting.TimeShifting;
import lu.uni.rfol.timedterm.TimedTermExpression;
import lu.uni.rfol.timedterm.TimedTermNumber;
import lu.uni.rfol.timedterm.TimedTermVariable;

public class TestTimeShift {

	@Test
	public void testNoTimeShiftingAppliedIfNotNeeded() {
		RSFOLFormula f = new ForallFormula(
				new Bound(new Tvariable("t"), new TimedTermNumber(3), new TimedTermNumber(5), true, true),
				new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermVariable(new Tvariable("t"))),
						RELOP.GEQ, new Value(2)));

		RSFOLFormula f1 = new TimeShifting().perform(f);

		assertEquals(f1.toString(), "(for all t:(3.0,5.0).(s(t+0.0)>=2.0))");
	}

	@Test
	public void testShiftsTheValueWhenAdded() {
		RSFOLFormula f = new ForallFormula(
				new Bound(new Tvariable("t"), new TimedTermNumber(3), new TimedTermNumber(5), true, true),
				new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermExpression(

						new Tvariable("t"), ArithmeticOperator.PLUS, new Value(2))), RELOP.GEQ, new Value(2)));

		RSFOLFormula f1 = new TimeShifting().perform(f);

		assertEquals(f1.toString(), "(for all t:(5.0,7.0).(s(t+0.0)>=2.0))");
	}
	
	@Test
	public void testShiftsTheValueWhenAdded2() {
		RSFOLFormula f = new ForallFormula(
				new Bound(new Tvariable("t"), new TimedTermNumber(3), new TimedTermNumber(5), true, true),
				new BinaryFormula(
				new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermExpression(

						new Tvariable("t"), ArithmeticOperator.PLUS, new Value(2))), RELOP.GEQ, new Value(2)),
				LOGICOP.CONJ,
				new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermExpression(

						new Tvariable("t"), ArithmeticOperator.PLUS, new Value(1))), RELOP.GEQ, new Value(2))
				)
				);

		RSFOLFormula f1 = new TimeShifting().perform(f);
		assertEquals(f1.toString(), "(for all t:(5.0,7.0).((s(t+0.0)>=2.0)and(s(t+-1.0)>=2.0)))");
	}
	
	
	@Test
	public void testShiftsTheValueWhenAdded3() {
		RSFOLFormula f = new ForallFormula(
				new Bound(new Tvariable("t"), new TimedTermNumber(3), new TimedTermNumber(5), true, true),
				new BinaryFormula(
				new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermExpression(

						new Tvariable("t"), ArithmeticOperator.PLUS, new Value(2))), RELOP.GEQ, new Value(2)),
				LOGICOP.CONJ,
				new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermNumber(10)), RELOP.GEQ, new Value(2))
				)
				);

		RSFOLFormula f1 = new TimeShifting().perform(f);
		System.out.println(f1.toString());
		assertEquals(f1.toString(), "(for all t:(15.0,17.0).((s(t+-10.0)>=2.0)and(s(10.0)>=2.0)))");
	}

	@Test
	public void testDoesNotShiftValueWhenSubstracted() {
		RSFOLFormula f = new ForallFormula(
				new Bound(new Tvariable("t"), new TimedTermNumber(3), new TimedTermNumber(5), true, true),
				new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermExpression(

						new Tvariable("t"), ArithmeticOperator.MINUS, new Value(2))), RELOP.GEQ, new Value(2)));

		RSFOLFormula f1 = new TimeShifting().perform(f);

		assertEquals(f1.toString(), "(for all t:(3.0,5.0).(s(t-2.0)>=2.0))");
	}

	@Test
	public void testShiftsTheValueWhenAddedToDifferentTerms() {
		RSFOLFormula f = new BinaryFormula(

				new ForallFormula(
						new Bound(new Tvariable("t"), new TimedTermNumber(3), new TimedTermNumber(5), true, true),
						new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermExpression(

								new Tvariable("t"), ArithmeticOperator.PLUS, new Value(2))), RELOP.GEQ, new Value(2)))

				, LOGICOP.CONJ,
				new ForallFormula(
						new Bound(new Tvariable("t1"), new TimedTermNumber(3), new TimedTermNumber(5), true, true),
						new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermExpression(

								new Tvariable("t1"), ArithmeticOperator.PLUS, new Value(5))), RELOP.GEQ,
								new Value(2))));

		RSFOLFormula f1 = new TimeShifting().perform(f);
		assertEquals(f1.toString(),
				"((for all t:(5.0,7.0).(s(t+0.0)>=2.0))and(for all t1:(8.0,10.0).(s(t1+0.0)>=2.0)))");

	}
	
	
	@Test
	public void testShiftsTheValueWhenAddedToDifferentTerms2() {
		RSFOLFormula f = new BinaryFormula(

				new ForallFormula(
						new Bound(new Tvariable("t"), new TimedTermNumber(3), new TimedTermNumber(5), true, true),
						new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermExpression(

								new Tvariable("t"), ArithmeticOperator.MINUS, new Value(2))), RELOP.GEQ, new Value(2)))

				, LOGICOP.CONJ,
				new ForallFormula(
						new Bound(new Tvariable("t1"), new TimedTermNumber(3), new TimedTermNumber(5), true, true),
						new SignalConstantComparison(new Signal(new SignalID("s"), new TimedTermExpression(

								new Tvariable("t1"), ArithmeticOperator.PLUS, new Value(5))), RELOP.GEQ,
								new Value(2))));

		RSFOLFormula f1 = new TimeShifting().perform(f);
		
		assertEquals(f1.toString(),
				"((for all t:(3.0,5.0).(s(t-2.0)>=2.0))and(for all t1:(8.0,10.0).(s(t1+0.0)>=2.0)))");

	}

}
