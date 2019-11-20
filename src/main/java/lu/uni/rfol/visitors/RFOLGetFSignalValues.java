package lu.uni.rfol.visitors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
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
import lu.uni.rfol.expression.ArithmeticOperator;
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
import lu.uni.rfol.timedterm.InfiniteTerm;
import lu.uni.rfol.timedterm.TimedTermExpression;
import lu.uni.rfol.timedterm.TimedTermNumber;

/**
 * Returns eturns the set of values where used in all the signal terms in the
 * form f(t+n)
 * 
 * @author claudio.menghi
 *
 */
public class RFOLGetFSignalValues implements RSFOLVisitor<Map<Tvariable, Set<Value>>> {

	@Override
	public Map<Tvariable, Set<Value>> visit(SignalID SignalID) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(Tvariable tvariable) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		Set<Value> values = new HashSet<Value>();
		values.add(new Value(0));

		timedVariables.put(tvariable, values);
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(Signal signal) {

		return signal.getTimedTerm().accept(this);
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(ForallFormula forallFormula) {
		return forallFormula.getFormula().accept(this);
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(NotFormula notFormula) {
		return notFormula.getSubformula().accept(this);
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(BinaryFormula binaryFormula) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		timedVariables.putAll(binaryFormula.getSubformula1().accept(this));

		for (Entry<Tvariable, Set<Value>> e : binaryFormula.getSubformula2().accept(this).entrySet()) {
			if (timedVariables.containsKey(e.getKey())) {
				timedVariables.get(e.getKey()).addAll(e.getValue());
			} else {
				timedVariables.put(e.getKey(), e.getValue());
			}
		}
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(SignalConstantComparison signalIDConstantComparison) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		timedVariables.putAll(signalIDConstantComparison.getSignal().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(SignalComparison signalIDConstantComparison) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		timedVariables.putAll(signalIDConstantComparison.getSignal1().accept(this));

		for (Entry<Tvariable, Set<Value>> e : signalIDConstantComparison.getSignal2().accept(this).entrySet()) {
			if (timedVariables.containsKey(e.getKey())) {
				timedVariables.get(e.getKey()).addAll(e.getValue());
			} else {
				timedVariables.put(e.getKey(), e.getValue());
			}
		}
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(TimedTermExpression timedTermExpression) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		Set<Value> values=new HashSet<Value>();
		if(timedTermExpression.getOperator().equals(ArithmeticOperator.PLUS)) {
			values.add(timedTermExpression.getValue());
		}
		timedVariables.put(timedTermExpression.getTvariable(), values);
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(TimedTermNumber timedTermNumber) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(Bound bound) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(InfiniteTerm infiniteTerm) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(BinaryExpression binaryExpression) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		timedVariables.putAll(binaryExpression.getLeftExpression().accept(this));
		for (Entry<Tvariable, Set<Value>> e : binaryExpression.getRightExpression().accept(this).entrySet()) {
			if (timedVariables.containsKey(e.getKey())) {
				timedVariables.get(e.getKey()).addAll(e.getValue());
			} else {
				timedVariables.put(e.getKey(), e.getValue());
			}
		}

		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(AbsoluteExpression modulusExpression) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		timedVariables.putAll(modulusExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(NormExpression normExpression) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		timedVariables.putAll(normExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(SinExpression sinExpression) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		timedVariables.putAll(sinExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(CosExpression cosExpression) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		timedVariables.putAll(cosExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(SQRTExpression sqrtExpression) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		timedVariables.putAll(sqrtExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(SignalVector SignalIDVector) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		timedVariables.putAll(SignalIDVector.getSignalID().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(SignalMatrix SignalIDMatrix) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		timedVariables.putAll(SignalIDMatrix.getSignalID().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(ExpressionComparison expressionComparison) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		timedVariables.putAll(expressionComparison.getExpression1().accept(this));
		timedVariables.putAll(expressionComparison.getExpression2().accept(this));

		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(Value value) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(SignedExpression signedExpression) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();
		timedVariables.putAll(signedExpression.getExp().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<Value>> visit(ExistsFormula existsFormula) {
		Map<Tvariable, Set<Value>> timedVariables = new HashMap<>();

		timedVariables.putAll(existsFormula.getFormula().accept(this));
		return timedVariables;
	}
}
