package lu.uni.rfol.shifting.intervalshif;

import java.util.HashMap;
import java.util.Map;

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
import lu.uni.rfol.visitors.RSFOLVisitor;

public class GetIntervalShiftingDuConstant implements RSFOLVisitor<Map<RSFOLFormula, Float>> {

	@Override
	public Map<RSFOLFormula, Float> visit(SignalID signal) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.put(signal, (float) 0);
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(Tvariable tvariable) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.put(tvariable, (float) 0);
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(Signal signal) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(signal.getTimedTerm().accept(this));
		map.putAll(signal.getSignalID().accept(this));
		// time is not considered in the computation of du
		map.put(signal, new Float(0));
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(ForallFormula forallFormula) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(forallFormula.getFormula().accept(this));
		map.putAll(forallFormula.getBound().accept(this));

		map.put(forallFormula,
				Math.max(
						map.get(forallFormula.getFormula()),
						map.get(forallFormula.getBound())));


		return map;
	}

	

	@Override
	public Map<RSFOLFormula, Float> visit(NotFormula notFormula) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(notFormula.getSubformula().accept(this));
		map.put(notFormula, map.get(notFormula.getSubformula()));

		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(BinaryFormula binaryFormula) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(binaryFormula.getSubformula1().accept(this));
		map.putAll(binaryFormula.getSubformula2().accept(this));
		map.put(binaryFormula,
				Math.max(map.get(binaryFormula.getSubformula1()), map.get(binaryFormula.getSubformula2())));

		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(SignalConstantComparison signalConstantComparison) {

		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(signalConstantComparison.getSignal().accept(this));
		map.putAll(signalConstantComparison.getValue().accept(this));
		map.put(signalConstantComparison, map.get(signalConstantComparison.getSignal()));
		return map;

	}

	@Override
	public Map<RSFOLFormula, Float> visit(SignalComparison signalComparison) {

		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(signalComparison.getSignal1().accept(this));
		map.putAll(signalComparison.getSignal2().accept(this));
		map.put(signalComparison,
				Math.max(map.get(signalComparison.getSignal1()), map.get(signalComparison.getSignal2())));

		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(TimedTermExpression timedTermExpression) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(timedTermExpression.getValue().accept(this));
		map.putAll(timedTermExpression.getTvariable().accept(this));
		
		map.put(timedTermExpression,  new Float(0));
		
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(TimedTermNumber timedTermNumber) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.put(timedTermNumber, timedTermNumber.getNumber());
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(Bound bound) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(bound.getRightbound().accept(this));
		map.putAll(bound.getLeftbound().accept(this));
		
		map.put(bound, Math.max(map.get(bound.getRightbound()), map.get(bound.getLeftbound())));
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(InfiniteTerm infiniteTerm) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.put(infiniteTerm, (float) 0);
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(BinaryExpression binaryExpression) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(binaryExpression.getLeftExpression().accept(this));
		map.putAll(binaryExpression.getRightExpression().accept(this));
		map.put(binaryExpression, 
				Math.max(map.get(binaryExpression.getLeftExpression()), 
						map.get(binaryExpression.getRightExpression()))
				);
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(AbsoluteExpression expression) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(expression.getSubformula().accept(this));
		map.putAll(expression.getSubformula().accept(this));
		map.put(expression, map.get(expression.getSubformula()
				));
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(NormExpression expression) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(expression.getSubformula().accept(this));
		map.putAll(expression.getSubformula().accept(this));
		map.put(expression, map.get(expression.getSubformula()
				));
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(SinExpression expression) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(expression.getSubformula().accept(this));
		map.putAll(expression.getSubformula().accept(this));
		map.put(expression, map.get(expression.getSubformula()
				));
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(CosExpression expression) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(expression.getSubformula().accept(this));
		map.putAll(expression.getSubformula().accept(this));
		map.put(expression, map.get(expression.getSubformula()
				));
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(SQRTExpression expression) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(expression.getSubformula().accept(this));
		map.put(expression, map.get(expression.getSubformula()
				));
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(SignalVector signal) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(signal.getTimedTerm().accept(this));
		map.putAll(signal.getSignalID().accept(this));
		map.put(signal, map.get(signal.getTimedTerm()));
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(SignalMatrix signal) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(signal.getTimedTerm().accept(this));
		map.putAll(signal.getSignalID().accept(this));
		map.put(signal, map.get(signal.getTimedTerm()));
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(ExpressionComparison expression) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(expression.getExpression1().accept(this));
		map.putAll(expression.getExpression2().accept(this));
		map.put(expression, Math.max(map.get(expression.getExpression1()), 
				map.get(expression.getExpression2())));
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(Value value) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.put(value, new Float(0));
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(SignedExpression expression) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(expression.getExp().accept(this));
		map.put(expression, map.get(expression.getExp()
				));
		return map;
	}

	@Override
	public Map<RSFOLFormula, Float> visit(ExistsFormula existsFormula) {
		Map<RSFOLFormula, Float> map = new HashMap<>();
		map.putAll(existsFormula.getFormula().accept(this));
		map.putAll(existsFormula.getBound().accept(this));

		map.put(existsFormula,
				Math.max(
						map.get(existsFormula.getFormula()),
						map.get(existsFormula.getBound())));


		return map;
	}
}