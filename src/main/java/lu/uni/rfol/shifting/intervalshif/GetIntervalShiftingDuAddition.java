package lu.uni.rfol.shifting.intervalshif;

import java.util.HashMap;
import java.util.Map;

import lu.uni.rfol.Signal;
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
import lu.uni.rfol.visitors.RSFOLVisitor;

public class GetIntervalShiftingDuAddition implements RSFOLVisitor<Map<Tvariable, Float>> {


	@Override
	public Map<Tvariable, Float> visit(Tvariable tvariable) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, Float> visit(Signal signal) {
		return  new HashMap<>();
	}

	@Override
	public Map<Tvariable, Float> visit(SignalVector signal) {
		return  new HashMap<>();
	}

	@Override
	public Map<Tvariable, Float> visit(SignalMatrix signal) {
		return  new HashMap<>();
	}

	@Override
	public Map<Tvariable, Float> visit(ForallFormula forallFormula) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(forallFormula.getFormula().accept(this));
		map.putAll(forallFormula.getBound().accept(this));

		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(NotFormula notFormula) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(notFormula.getSubformula().accept(this));

		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(BinaryFormula binaryFormula) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(binaryFormula.getSubformula1().accept(this));

		Map<Tvariable, Float> tmp = binaryFormula.getSubformula2().accept(this);
		for (Tvariable id : tmp.keySet()) {
			if(!map.containsKey(id)) {
				map.put(id, tmp.get(id));
			}
			else {
				map.put(id, Math.max(tmp.get(id), map.get(id)));
			}
		}
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(SignalConstantComparison signalConstantComparison) {

		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(signalConstantComparison.getSignal().accept(this));
		map.putAll(signalConstantComparison.getValue().accept(this));
		return map;

	}

	@Override
	public Map<Tvariable, Float> visit(SignalComparison signalComparison) {

		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(signalComparison.getSignal1().accept(this));
		map.putAll(signalComparison.getSignal2().accept(this));

		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(TimedTermExpression timedTermExpression) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.put(timedTermExpression.getTvariable(), timedTermExpression.getMaximumAddedValue());
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(TimedTermNumber timedTermNumber) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, Float> visit(Bound bound) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(bound.getLeftbound().accept(this));

		Map<Tvariable, Float> tmp = bound.getRightbound().accept(this);
		for (Tvariable id : tmp.keySet()) {
			if(!map.containsKey(id)) {
				map.put(id, tmp.get(id));
			}
			else {
				map.put(id, Math.max(tmp.get(id), map.get(id)));
			}
		}
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(InfiniteTerm infiniteTerm) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, Float> visit(BinaryExpression binaryExpression) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(binaryExpression.getLeftExpression().accept(this));
		map.putAll(binaryExpression.getRightExpression().accept(this));
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(AbsoluteExpression expression) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(expression.getSubformula().accept(this));
		map.putAll(expression.getSubformula().accept(this));
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(NormExpression expression) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(expression.getSubformula().accept(this));
		map.putAll(expression.getSubformula().accept(this));
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(SinExpression expression) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(expression.getSubformula().accept(this));
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(CosExpression expression) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(expression.getSubformula().accept(this));
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(SQRTExpression expression) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(expression.getSubformula().accept(this));
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(ExpressionComparison expression) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(expression.getExpression1().accept(this));
		map.putAll(expression.getExpression2().accept(this));
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(Value value) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, Float> visit(SignedExpression expression) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, Float> visit(lu.uni.rfol.SignalID signal) {
		return new HashMap<>();

	}

	@Override
	public Map<Tvariable, Float> visit(ExistsFormula existsFormula) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(existsFormula.getFormula().accept(this));
		map.putAll(existsFormula.getBound().accept(this));

		return map;
	}
}