package uni.lu.rfol.shifting.intervalshif;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import uni.lu.rfol.formulae.RSFOLFormula;
import uni.lu.rfol.timedterm.InfiniteTerm;
import uni.lu.rfol.timedterm.TimedTermExpression;
import uni.lu.rfol.timedterm.TimedTermNumber;
import uni.lu.rfol.visitors.RSFOLVisitor;

public class GetParentRelation implements RSFOLVisitor<Map<RSFOLFormula, Set<RSFOLFormula>>> {

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(SignalID signal) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.put(signal, new HashSet<>());
		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(Tvariable tvariable) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.put(tvariable, new HashSet<>());
		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(Signal signal) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.put(signal, new HashSet<>());
		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(ForallFormula forallFormula) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.putAll(forallFormula.getBound().accept(this));
		map.putAll(forallFormula.getFormula().accept(this));

		for (Entry<RSFOLFormula, Set<RSFOLFormula>> e : map.entrySet()) {
			e.getValue().add(forallFormula);
		}

		map.put(forallFormula, new HashSet<>());

		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(NotFormula notFormula) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.putAll(notFormula.getSubformula().accept(this));

		for (Entry<RSFOLFormula, Set<RSFOLFormula>> e : map.entrySet()) {
			e.getValue().add(notFormula);
		}

		map.put(notFormula, new HashSet<>());

		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(BinaryFormula binaryFormula) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.putAll(binaryFormula.getSubformula1().accept(this));
		map.putAll(binaryFormula.getSubformula2().accept(this));

		for (Entry<RSFOLFormula, Set<RSFOLFormula>> e : map.entrySet()) {
			e.getValue().add(binaryFormula);
		}

		map.put(binaryFormula, new HashSet<>());

		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(SignalConstantComparison signalConstantComparison) {

		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.putAll(signalConstantComparison.getSignal().accept(this));

		for (Entry<RSFOLFormula, Set<RSFOLFormula>> e : map.entrySet()) {
			e.getValue().add(signalConstantComparison);
		}

		map.put(signalConstantComparison, new HashSet<>());

		return map;

	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(SignalComparison signalComparison) {

		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.putAll(signalComparison.getSignal1().accept(this));
		map.putAll(signalComparison.getSignal2().accept(this));

		for (Entry<RSFOLFormula, Set<RSFOLFormula>> e : map.entrySet()) {
			e.getValue().add(signalComparison);
		}

		map.put(signalComparison, new HashSet<>());

		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(TimedTermExpression timedTermExpression) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.putAll(timedTermExpression.getTvariable().accept(this));

		for (Entry<RSFOLFormula, Set<RSFOLFormula>> e : map.entrySet()) {
			e.getValue().add(timedTermExpression);
		}

		map.put(timedTermExpression, new HashSet<>());

		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(TimedTermNumber timedTermNumber) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.put(timedTermNumber, new HashSet<>());

		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(Bound bound) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.putAll(bound.getLeftbound().accept(this));
		map.putAll(bound.getRightbound().accept(this));

		for (Entry<RSFOLFormula, Set<RSFOLFormula>> e : map.entrySet()) {
			e.getValue().add(bound);
		}

		map.put(bound, new HashSet<>());

		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(InfiniteTerm infiniteTerm) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.put(infiniteTerm, new HashSet<>());

		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(BinaryExpression binaryExpression) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.putAll(binaryExpression.getLeftExpression().accept(this));
		map.putAll(binaryExpression.getRightExpression().accept(this));

		map.get(binaryExpression.getLeftExpression()).add(binaryExpression);
		map.get(binaryExpression.getRightExpression()).add(binaryExpression);
		map.put(binaryExpression, new HashSet<>());

		return map;

	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(AbsoluteExpression modulusExpression) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.putAll(modulusExpression.getSubformula().accept(this));
		map.get(modulusExpression.getSubformula()).add(modulusExpression);
		map.put(modulusExpression, new HashSet<>());

		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(NormExpression normExpression) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.put(normExpression, new HashSet<>());
		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(SinExpression sinExpression) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.put(sinExpression, new HashSet<>());
		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(CosExpression cosExpression) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.put(cosExpression, new HashSet<>());
		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(SQRTExpression sqrtExpression) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.put(sqrtExpression, new HashSet<>());
		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(SignalVector signalVector) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.put(signalVector, new HashSet<>());
		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(SignalMatrix signalMatrix) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.put(signalMatrix, new HashSet<>());
		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(ExpressionComparison expressionComparison) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.put(expressionComparison, new HashSet<>());
		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(Value value) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.put(value, new HashSet<>());
		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(SignedExpression signedExpression) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.put(signedExpression, new HashSet<>());
		return map;
	}

	@Override
	public Map<RSFOLFormula, Set<RSFOLFormula>> visit(ExistsFormula existsFormula) {
		Map<RSFOLFormula, Set<RSFOLFormula>> map = new HashMap<>();
		map.putAll(existsFormula.getBound().accept(this));
		map.putAll(existsFormula.getFormula().accept(this));

		for (Entry<RSFOLFormula, Set<RSFOLFormula>> e : map.entrySet()) {
			e.getValue().add(existsFormula);
		}

		map.put(existsFormula, new HashSet<>());

		return map;
	}

}
