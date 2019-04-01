package uni.lu.rfol.shifting.intervalshif;

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
import uni.lu.rfol.formulae.RSFOLFormula;
import uni.lu.rfol.timedterm.InfiniteTerm;
import uni.lu.rfol.timedterm.TimedTermExpression;
import uni.lu.rfol.timedterm.TimedTermNumber;
import uni.lu.rfol.visitors.RSFOLVisitor;

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