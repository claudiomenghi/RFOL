package uni.lu.rfol.shifting.timeshifting;

import java.util.HashMap;
import java.util.Map;

import uni.lu.rfol.Signal;
import uni.lu.rfol.SignalID;
import uni.lu.rfol.SignalMatrix;
import uni.lu.rfol.SignalVector;
import uni.lu.rfol.Tvariable;
import uni.lu.rfol.atoms.ExpressionComparison;
import uni.lu.rfol.atoms.SignalComparison;
import uni.lu.rfol.atoms.SignalConstantComparison;
import uni.lu.rfol.atoms.Value;
import uni.lu.rfol.expression.AbsoluteExpression;
import uni.lu.rfol.expression.BinaryExpression;
import uni.lu.rfol.expression.CosExpression;
import uni.lu.rfol.expression.NormExpression;
import uni.lu.rfol.expression.SQRTExpression;
import uni.lu.rfol.expression.SignedExpression;
import uni.lu.rfol.expression.SinExpression;
import uni.lu.rfol.formulae.BinaryFormula;
import uni.lu.rfol.formulae.Bound;
import uni.lu.rfol.formulae.ExistsFormula;
import uni.lu.rfol.formulae.ForallFormula;
import uni.lu.rfol.formulae.NotFormula;
import uni.lu.rfol.timedterm.InfiniteTerm;
import uni.lu.rfol.timedterm.TimedTermExpression;
import uni.lu.rfol.timedterm.TimedTermNumber;
import uni.lu.rfol.visitors.RSFOLVisitor;

public class GetTimeShiftingDt implements RSFOLVisitor<Map<Tvariable, Float>> {

	@Override
	public Map<Tvariable, Float> visit(SignalID signal) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, Float> visit(Tvariable tvariable) {
		Map<Tvariable, Float> map = new HashMap<>();
		if (!map.containsKey(tvariable)) {
			map.put(tvariable, (float) 0);
		}
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(Signal signal) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(signal.getTimedTerm().accept(this));
		map.putAll(signal.getSignalID().accept(this));
		return map;
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

		Map<Tvariable, Float> map1 = binaryFormula.getSubformula1().accept(this);
		for (Tvariable f : map1.keySet()) {
			if (map.containsKey(f)) {
				map.put(f, Math.max(map.get(f), map1.get(f)));
			} else {
				map.put(f, map1.get(f));
			}
		}
		Map<Tvariable, Float> map2 = binaryFormula.getSubformula2().accept(this);
		for (Tvariable f : map2.keySet()) {
			if (map.containsKey(f)) {
				map.put(f, Math.max(map.get(f), map2.get(f)));
			} else {
				map.put(f, map2.get(f));
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
		Map<Tvariable, Float> map1 = signalComparison.getSignal1().accept(this);
		for (Tvariable f : map1.keySet()) {
			if (map.containsKey(f)) {
				map.put(f, Math.max(map.get(f), map1.get(f)));
			} else {
				map.put(f, map1.get(f));
			}
		}
		Map<Tvariable, Float> map2 = signalComparison.getSignal2().accept(this);
		for (Tvariable f : map2.keySet()) {
			if (map.containsKey(f)) {
				map.put(f, Math.max(map.get(f), map2.get(f)));
			} else {
				map.put(f, map2.get(f));
			}
		}

		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(TimedTermExpression timedTermExpression) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(timedTermExpression.getValue().accept(this));

		if (map.containsKey(timedTermExpression.getTvariable())) {
			map.put(timedTermExpression.getTvariable(), Math.max(map.get(timedTermExpression.getTvariable()),
					((Value) timedTermExpression.getValue()).getVal()));
		} else {
			map.put(timedTermExpression.getTvariable(), ((Value) timedTermExpression.getValue()).getVal());
		}
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(TimedTermNumber timedTermNumber) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, Float> visit(Bound bound) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(bound.getRightbound().accept(this));
		map.putAll(bound.getLeftbound().accept(this));
		// the bounds are not considered in the computation of dt
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(InfiniteTerm infiniteTerm) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, Float> visit(BinaryExpression binaryExpression) {
		Map<Tvariable, Float> map = new HashMap<>();

		Map<Tvariable, Float> map1 = binaryExpression.getLeftExpression().accept(this);
		for (Tvariable f : map1.keySet()) {
			if (map.containsKey(f)) {
				map.put(f, Math.max(map.get(f), map1.get(f)));
			} else {
				map.put(f, map1.get(f));
			}
		}
		Map<Tvariable, Float> map2 = binaryExpression.getRightExpression().accept(this);
		for (Tvariable f : map2.keySet()) {
			if (map.containsKey(f)) {
				map.put(f, Math.max(map.get(f), map2.get(f)));
			} else {
				map.put(f, map2.get(f));
			}
		}

		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(AbsoluteExpression expression) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(expression.getSubformula().accept(this));
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(NormExpression expression) {
		Map<Tvariable, Float> map = new HashMap<>();
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
	public Map<Tvariable, Float> visit(SignalVector signal) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(signal.getTimedTerm().accept(this));
		map.putAll(signal.getSignalID().accept(this));
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(SignalMatrix signal) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(signal.getTimedTerm().accept(this));
		map.putAll(signal.getSignalID().accept(this));
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(ExpressionComparison expression) {
		Map<Tvariable, Float> map = new HashMap<>();

		Map<Tvariable, Float> map1 = expression.getExpression1().accept(this);
		for (Tvariable f : map1.keySet()) {
			if (map.containsKey(f)) {
				map.put(f, Math.max(map.get(f), map1.get(f)));
			} else {
				map.put(f, map1.get(f));
			}
		}
		Map<Tvariable, Float> map2 = expression.getExpression2().accept(this);
		for (Tvariable f : map2.keySet()) {
			if (map.containsKey(f)) {
				map.put(f, Math.max(map.get(f), map2.get(f)));
			} else {
				map.put(f, map2.get(f));
			}
		}
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(Value value) {
		Map<Tvariable, Float> map = new HashMap<>();
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(SignedExpression expression) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(expression.getExp().accept(this));
		return map;
	}

	@Override
	public Map<Tvariable, Float> visit(ExistsFormula existsFormula) {
		Map<Tvariable, Float> map = new HashMap<>();
		map.putAll(existsFormula.getFormula().accept(this));
		map.putAll(existsFormula.getBound().accept(this));

		return map;
	}
}