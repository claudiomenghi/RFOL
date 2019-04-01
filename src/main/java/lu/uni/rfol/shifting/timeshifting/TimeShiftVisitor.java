package lu.uni.rfol.shifting.timeshifting;

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

public class TimeShiftVisitor implements RSFOLVisitor<RSFOLFormula> {

	private Map<Tvariable, Float> shiftValue;

	public TimeShiftVisitor(RSFOLFormula formula) {
		this.shiftValue = formula.accept(new GetTimeShiftingDt());
	}

	@Override
	public RSFOLFormula visit(SignalID signal) {
		return signal;
	}

	@Override
	public RSFOLFormula visit(Tvariable tvariable) {
		return tvariable;
	}

	@Override
	public RSFOLFormula visit(Signal signal) {

		signal.getTimedTerm().accept(this);

		return signal;
	}

	@Override
	public RSFOLFormula visit(ForallFormula forallFormula) {

		forallFormula.getBound().accept(this);
		forallFormula.getFormula().accept(this);
		return forallFormula;
	}

	@Override
	public RSFOLFormula visit(NotFormula notFormula) {
		notFormula.getSubformula().accept(this);
		return notFormula;
	}

	@Override
	public RSFOLFormula visit(BinaryFormula binaryFormula) {

		binaryFormula.getSubformula1().accept(this);
		binaryFormula.getSubformula2().accept(this);
		return binaryFormula;
	}

	@Override
	public RSFOLFormula visit(SignalConstantComparison signalConstantComparison) {
		signalConstantComparison.getSignal().accept(this);
		signalConstantComparison.getValue().accept(this);
		return signalConstantComparison;
	}

	@Override
	public RSFOLFormula visit(SignalComparison signalComparison) {
		signalComparison.getSignal1().accept(this);
		signalComparison.getSignal2().accept(this);
		return signalComparison;
	}

	@Override
	public RSFOLFormula visit(TimedTermExpression timedTermExpression) {

		if (shiftValue.containsKey(timedTermExpression.getTvariable())) {
			timedTermExpression.shift(-shiftValue.get(timedTermExpression.getTvariable()));
		}
		return null;
	}

	@Override
	public RSFOLFormula visit(TimedTermNumber timedTermNumber) {
		return timedTermNumber;
	}

	@Override
	public RSFOLFormula visit(Bound bound) {
		bound.getLeftbound().accept(this);
		bound.getRightbound().accept(this);
		if (this.shiftValue.containsKey(bound.getTvariable())) {
			bound.getLeftbound().shift(shiftValue.get(bound.getTvariable()));
			bound.getRightbound().shift(shiftValue.get(bound.getTvariable()));
		}
		return bound;
	}

	@Override
	public RSFOLFormula visit(InfiniteTerm infiniteTerm) {
		return infiniteTerm;
	}

	@Override
	public RSFOLFormula visit(BinaryExpression binaryExpression) {
		binaryExpression.getLeftExpression().accept(this);
		binaryExpression.getRightExpression().accept(this);
		return binaryExpression;
	}

	@Override
	public RSFOLFormula visit(AbsoluteExpression modulusExpression) {
		modulusExpression.getExp().accept(this);
		return modulusExpression;
	}

	@Override
	public RSFOLFormula visit(NormExpression normExpression) {
		normExpression.getExp().accept(this);
		return normExpression;
	}

	@Override
	public RSFOLFormula visit(SinExpression sinExpression) {
		sinExpression.getExp().accept(this);
		return sinExpression;
	}

	@Override
	public RSFOLFormula visit(CosExpression cosExpression) {
		cosExpression.getExp().accept(this);
		return cosExpression;
	}

	@Override
	public RSFOLFormula visit(SQRTExpression sqrtExpression) {
		sqrtExpression.getExp().accept(this);
		return sqrtExpression;
	}

	@Override
	public RSFOLFormula visit(SignalVector signalVector) {
		signalVector.getTimedTerm().accept(this);
		return signalVector;
	}

	@Override
	public RSFOLFormula visit(SignalMatrix signalMatrix) {
		signalMatrix.getTimedTerm().accept(this);
		return signalMatrix;
	}

	@Override
	public RSFOLFormula visit(ExpressionComparison expressionComparison) {
		expressionComparison.getExpression1().accept(this);
		expressionComparison.getExpression2().accept(this);
		return expressionComparison;
	}

	@Override
	public RSFOLFormula visit(Value value) {
		return value;
	}

	@Override
	public RSFOLFormula visit(SignedExpression signedExpression) {
		signedExpression.getExp().accept(this);

		return null;
	}

	@Override
	public RSFOLFormula visit(ExistsFormula existsFormula) {
		existsFormula.getBound().accept(this);
		existsFormula.getFormula().accept(this);
		return existsFormula;
	}
}
