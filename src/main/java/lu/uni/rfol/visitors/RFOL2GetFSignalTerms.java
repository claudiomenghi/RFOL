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
public class RFOL2GetFSignalTerms implements RSFOLVisitor<Map<Tvariable, Set<TimedTermExpression>>> {

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(SignalID SignalID) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(Tvariable tvariable) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(Signal signal) {

		return signal.getTimedTerm().accept(this);
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(ForallFormula forallFormula) {
		return forallFormula.getFormula().accept(this);
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(NotFormula notFormula) {
		return notFormula.getSubformula().accept(this);
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(BinaryFormula binaryFormula) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();
		timedVariables.putAll(binaryFormula.getSubformula1().accept(this));

		for (Entry<Tvariable, Set<TimedTermExpression>> e : binaryFormula.getSubformula2().accept(this).entrySet()) {
			if (timedVariables.containsKey(e.getKey())) {
				timedVariables.get(e.getKey()).addAll(e.getValue());
			} else {
				timedVariables.put(e.getKey(), e.getValue());
			}
		}
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(SignalConstantComparison signalIDConstantComparison) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();
		timedVariables.putAll(signalIDConstantComparison.getSignal().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(SignalComparison signalIDConstantComparison) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();
		timedVariables.putAll(signalIDConstantComparison.getSignal1().accept(this));

		for (Entry<Tvariable, Set<TimedTermExpression>> e : signalIDConstantComparison.getSignal2().accept(this).entrySet()) {
			if (timedVariables.containsKey(e.getKey())) {
				timedVariables.get(e.getKey()).addAll(e.getValue());
			} else {
				timedVariables.put(e.getKey(), e.getValue());
			}
		}
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(TimedTermExpression timedTermExpression) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();
		Set<TimedTermExpression> values=new HashSet<TimedTermExpression>();
		values.add(timedTermExpression);
		timedVariables.put(timedTermExpression.getTvariable(), values);
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(TimedTermNumber timedTermNumber) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(Bound bound) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(InfiniteTerm infiniteTerm) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(BinaryExpression binaryExpression) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();
		timedVariables.putAll(binaryExpression.getLeftExpression().accept(this));
		for (Entry<Tvariable, Set<TimedTermExpression>> e : binaryExpression.getRightExpression().accept(this).entrySet()) {
			if (timedVariables.containsKey(e.getKey())) {
				timedVariables.get(e.getKey()).addAll(e.getValue());
			} else {
				timedVariables.put(e.getKey(), e.getValue());
			}
		}

		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(AbsoluteExpression modulusExpression) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();
		timedVariables.putAll(modulusExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(NormExpression normExpression) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();
		timedVariables.putAll(normExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(SinExpression sinExpression) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();
		timedVariables.putAll(sinExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(CosExpression cosExpression) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();
		timedVariables.putAll(cosExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(SQRTExpression sqrtExpression) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();
		timedVariables.putAll(sqrtExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(SignalVector SignalIDVector) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();
		timedVariables.putAll(SignalIDVector.getSignalID().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(SignalMatrix SignalIDMatrix) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();
		timedVariables.putAll(SignalIDMatrix.getSignalID().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(ExpressionComparison expressionComparison) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();
		timedVariables.putAll(expressionComparison.getExpression1().accept(this));
		timedVariables.putAll(expressionComparison.getExpression2().accept(this));

		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(Value value) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(SignedExpression signedExpression) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();
		timedVariables.putAll(signedExpression.getExp().accept(this));
		return timedVariables;
	}

	@Override
	public Map<Tvariable, Set<TimedTermExpression>> visit(ExistsFormula existsFormula) {
		Map<Tvariable, Set<TimedTermExpression>> timedVariables = new HashMap<>();

		timedVariables.putAll(existsFormula.getFormula().accept(this));
		return timedVariables;
	}
}
