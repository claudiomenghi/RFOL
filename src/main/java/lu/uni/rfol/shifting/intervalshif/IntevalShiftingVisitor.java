package lu.uni.rfol.shifting.intervalshif;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lu.uni.rfol.Signal;
import lu.uni.rfol.SignalID;
import lu.uni.rfol.SignalMatrix;
import lu.uni.rfol.SignalVector;
import lu.uni.rfol.Tvariable;
import lu.uni.rfol.atoms.ExpressionComparison;
import lu.uni.rfol.atoms.SignalComparison;
import lu.uni.rfol.atoms.SignalConstantComparison;
import lu.uni.rfol.atoms.Value;
import lu.uni.rfol.expression.AbsoluteExpression;
import lu.uni.rfol.expression.BinaryExpression;
import lu.uni.rfol.expression.CosExpression;
import lu.uni.rfol.expression.NormExpression;
import lu.uni.rfol.expression.SQRTExpression;
import lu.uni.rfol.expression.SignedExpression;
import lu.uni.rfol.expression.SinExpression;
import lu.uni.rfol.formulae.BinaryFormula;
import lu.uni.rfol.formulae.Bound;
import lu.uni.rfol.formulae.ExistsFormula;
import lu.uni.rfol.formulae.ForallFormula;
import lu.uni.rfol.formulae.NotFormula;
import lu.uni.rfol.formulae.RSFOLFormula;
import lu.uni.rfol.timedterm.InfiniteTerm;
import lu.uni.rfol.timedterm.TimedTermExpression;
import lu.uni.rfol.timedterm.TimedTermNumber;
import lu.uni.rfol.visitors.RFOL2GetFSignalTerms;
import lu.uni.rfol.visitors.RSFOLVisitor;

public class IntevalShiftingVisitor implements RSFOLVisitor<RSFOLFormula> {

	private Map<Tvariable, Set<TimedTermExpression>> maptvariableExpressions = null;

	private Map<Integer, Value> forall = new HashMap<Integer, Value>();

	private Map<Integer, Value> exists = new HashMap<Integer, Value>();
	private boolean computed = false;

	private void computeF(RSFOLFormula formula) {
		if (computed == false) {
			maptvariableExpressions = formula.accept(new RFOL2GetFSignalTerms());
		}
		computed = true;
	}

	@Override
	public RSFOLFormula visit(SignalID signal) {
		computeF(signal);
		return signal;
	}

	@Override
	public RSFOLFormula visit(Tvariable tvariable) {
		computeF(tvariable);
		return tvariable;
	}

	@Override
	public RSFOLFormula visit(Signal signal) {
		computeF(signal);
		return signal;
	}

	@Override
	public RSFOLFormula visit(BinaryFormula binaryFormula) {
		computeF(binaryFormula);
		binaryFormula.getSubformula1().accept(this);
		binaryFormula.getSubformula2().accept(this);
		forall.put(binaryFormula.getUI(),
				Value.valueComparator.compare(forall.get(binaryFormula.getSubformula1().getUI()),
						forall.get(binaryFormula.getSubformula1().getUI())) > 0 ? forall.get(binaryFormula.getSubformula1().getUI())
								: forall.get(binaryFormula.getSubformula2().getUI()));

		exists.put(binaryFormula.getUI(),
				Value.valueComparator.compare(exists.get(binaryFormula.getSubformula1().getUI()),
						exists.get(binaryFormula.getSubformula1().getUI())) < 0 ? exists.get(binaryFormula.getSubformula1().getUI())
								: exists.get(binaryFormula.getSubformula2().getUI()));

		return binaryFormula;
	}

	@Override
	public RSFOLFormula visit(ForallFormula forallFormula) {
		computeF(forallFormula);
		forallFormula.getFormula().accept(this);
		if (forallFormula.getBound().getLeftbound() instanceof TimedTermNumber) {

			Value tau1 = new Value(((TimedTermNumber) forallFormula.getBound().getLeftbound()).getNumber());
			float c1 = 0;
			float c2 = 0;
			if (Value.valueComparator.compare(tau1, exists.get(forallFormula.getFormula().getUI())) < 0) {
				c1 = exists.get(forallFormula.getFormula().getUI()).getVal() - tau1.getVal();
				forallFormula.getBound().getLeftbound().shift(c1);
				forallFormula.getBound().getRightbound().shift(c1);
			}
			Value tau2 = new Value(((TimedTermNumber) forallFormula.getBound().getRightbound()).getNumber());
			if (Value.valueComparator.compare(tau2, forall.get(forallFormula.getFormula().getUI())) < 0) {
				c2 = forall.get(forallFormula.getFormula().getUI()).getVal() - tau2.getVal();
				forallFormula.getBound().getLeftbound().shift(c2);
				forallFormula.getBound().getRightbound().shift(c2);
			}
			
			for (TimedTermExpression texp : this.maptvariableExpressions.get(forallFormula.getBound().getTvariable())) {
				texp.shift((-c1 - c2));
			}
			
			Value existsforallvalue=forall.get(forallFormula.getFormula().getUI());
			Value maxval = Value.valueComparator.compare(
					tau2, 
					existsforallvalue) > 0 ? tau2
					: existsforallvalue;
			forall.put(forallFormula.getUI(), maxval);
			exists.put(forallFormula.getUI(), exists.get(forallFormula.getFormula().getUI()));
		}

		return forallFormula;
	}

	@Override
	public RSFOLFormula visit(NotFormula notFormula) {
		computeF(notFormula);
		notFormula.getSubformula().accept(this);
		forall.put(notFormula.getUI(), forall.get(notFormula.getUI()));
		exists.put(notFormula.getUI(), exists.get(notFormula.getUI()));
		return notFormula;
	}

	@Override
	public RSFOLFormula visit(SignalConstantComparison signalConstantComparison) {
		computeF(signalConstantComparison);
		forall.put(signalConstantComparison.getUI(), new Value(0));
		exists.put(signalConstantComparison.getUI(), new Value(0));
		return signalConstantComparison;
	}

	@Override
	public RSFOLFormula visit(SignalComparison signalComparison) {
		computeF(signalComparison);
		forall.put(signalComparison.getUI(), new Value(0));
		exists.put(signalComparison.getUI(), new Value(0));
		return signalComparison;
	}

	@Override
	public RSFOLFormula visit(TimedTermExpression timedTermExpression) {
		computeF(timedTermExpression);
		forall.put(timedTermExpression.getUI(), new Value(0));
		exists.put(timedTermExpression.getUI(), new Value(0));
		return timedTermExpression;
	}

	@Override
	public RSFOLFormula visit(TimedTermNumber timedTermNumber) {
		computeF(timedTermNumber);
		forall.put(timedTermNumber.getUI(), new Value(0));
		exists.put(timedTermNumber.getUI(), new Value(0));
		return timedTermNumber;
	}

	@Override
	public RSFOLFormula visit(Bound bound) {
		computeF(bound);
		forall.put(bound.getUI(), new Value(0));
		exists.put(bound.getUI(), new Value(0));
		return bound;
	}

	
	@Override
	public RSFOLFormula visit(InfiniteTerm infiniteTerm) {
		computeF(infiniteTerm);
		forall.put(infiniteTerm.getUI(), new Value(0));
		exists.put(infiniteTerm.getUI(), new Value(0));
		return infiniteTerm;
	}

	@Override
	public RSFOLFormula visit(BinaryExpression binaryExpression) {
		computeF(binaryExpression);
		forall.put(binaryExpression.getUI(), new Value(0));
		exists.put(binaryExpression.getUI(), new Value(0));
		return binaryExpression;
	}

	@Override
	public RSFOLFormula visit(AbsoluteExpression modulusExpression) {
		computeF(modulusExpression);
		forall.put(modulusExpression.getUI(), new Value(0));
		exists.put(modulusExpression.getUI(), new Value(0));
		return modulusExpression;
	}

	@Override
	public RSFOLFormula visit(NormExpression normExpression) {
		computeF(normExpression);
		forall.put(normExpression.getUI(), new Value(0));
		exists.put(normExpression.getUI(), new Value(0));
		return normExpression;
	}

	@Override
	public RSFOLFormula visit(SinExpression sinExpression) {
		computeF(sinExpression);
		forall.put(sinExpression.getUI(), new Value(0));
		exists.put(sinExpression.getUI(), new Value(0));
		return sinExpression;
	}

	@Override
	public RSFOLFormula visit(CosExpression cosExpression) {
		computeF(cosExpression);
		forall.put(cosExpression.getUI(), new Value(0));
		exists.put(cosExpression.getUI(), new Value(0));
		return cosExpression;
	}

	@Override
	public RSFOLFormula visit(SQRTExpression sqrtExpression) {
		computeF(sqrtExpression);
		forall.put(sqrtExpression.getUI(), new Value(0));
		exists.put(sqrtExpression.getUI(), new Value(0));
		return sqrtExpression;
	}

	@Override
	public RSFOLFormula visit(SignalVector signalVector) {
		computeF(signalVector);
		forall.put(signalVector.getUI(), new Value(0));
		exists.put(signalVector.getUI(), new Value(0));
		return signalVector;
	}

	@Override
	public RSFOLFormula visit(SignalMatrix signalMatrix) {
		computeF(signalMatrix);
		forall.put(signalMatrix.getUI(), new Value(0));
		exists.put(signalMatrix.getUI(), new Value(0));
		return signalMatrix;
	}

	@Override
	public RSFOLFormula visit(ExpressionComparison expressionComparison) {
		computeF(expressionComparison);
		forall.put(expressionComparison.getUI(), new Value(0));
		exists.put(expressionComparison.getUI(), new Value(0));
		return expressionComparison;
	}

	@Override
	public RSFOLFormula visit(Value value) {
		computeF(value);
		return value;
	}

	@Override
	public RSFOLFormula visit(SignedExpression signedExpression) {
		computeF(signedExpression);
		forall.put(signedExpression.getUI(), new Value(0));
		exists.put(signedExpression.getUI(), new Value(0));
		return signedExpression;
	}

	@Override
	public RSFOLFormula visit(ExistsFormula existsFormula) {
		computeF(existsFormula);
		existsFormula.getFormula().accept(this);
		if (existsFormula.getBound().getLeftbound() instanceof TimedTermNumber) {

			Value tau1 = new Value(((TimedTermNumber) existsFormula.getBound().getLeftbound()).getNumber());
			float c1 = 0;
			float c2 = 0;
			if (Value.valueComparator.compare(tau1, forall.get(existsFormula.getFormula().getUI())) < 0) {
				c1 = forall.get(existsFormula.getFormula().getUI()).getVal() - tau1.getVal();
				existsFormula.getBound().getLeftbound().shift(c1);
				existsFormula.getBound().getRightbound().shift(c1);
			}
			Value tau2 = new Value(((TimedTermNumber) existsFormula.getBound().getRightbound()).getNumber());
			if (Value.valueComparator.compare(tau2, exists.get(existsFormula.getFormula().getUI())) < 0) {
				c2 = exists.get(existsFormula.getFormula().getUI()).getVal() - tau2.getVal();
				existsFormula.getBound().getLeftbound().shift(c2);
				existsFormula.getBound().getRightbound().shift(c2);
			}
			for (TimedTermExpression texp : this.maptvariableExpressions.get(existsFormula.getBound().getTvariable())) {
				texp.shift((-c1 - c2));
			}
			Value maxval = Value.valueComparator.compare(tau2, exists.get(existsFormula.getFormula().getUI())) > 0 ? tau2
					: exists.get(existsFormula.getFormula().getUI());
			forall.put(existsFormula.getUI(), forall.get(existsFormula.getFormula().getUI()));
			exists.put(existsFormula.getUI(), maxval);
		}

		return existsFormula;
	}
}
