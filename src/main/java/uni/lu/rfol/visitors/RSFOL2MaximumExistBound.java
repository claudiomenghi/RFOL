package uni.lu.rfol.visitors;


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

public class RSFOL2MaximumExistBound implements RSFOLVisitor<Float> {

	
	@Override
	public Float visit(SignalID signal) {
		return 0f;
	}

	@Override
	public Float visit(Tvariable tvariable) {
		return 0f;
	}

	@Override
	public Float visit(Signal signal) {
		return 0f;
	}
	@Override
	public Float visit(SignalVector signalVector) {
		return 0f;
	}

	@Override
	public Float visit(SignalMatrix signalMatrix) {
		return 0f;
	}

	@Override
	public Float visit(ForallFormula forallFormula) {
		return forallFormula.getFormula().accept(this);
	}

	@Override
	public Float visit(NotFormula notFormula) {
		return notFormula.getSubformula().accept(this);
	}

	@Override
	public Float visit(BinaryFormula binaryFormula) {
		return Math.max(binaryFormula.getSubformula1().accept(this), binaryFormula.getSubformula2().accept(this));
	}

	@Override
	public Float visit(SignalConstantComparison signalConstantComparison) {
		return 0f;
	}

	@Override
	public Float visit(SignalComparison signalComparison) {
		return 0f;
	}

	@Override
	public Float visit(TimedTermExpression timedTermExpression) {
		return timedTermExpression.getValue().getVal();
	}

	@Override
	public Float visit(TimedTermNumber timedTermNumber) {
		return timedTermNumber.getNumber();
	}

	@Override
	public Float visit(Bound bound) {
		return bound.getRightbound().accept(this);
	}

	@Override
	public Float visit(InfiniteTerm infiniteTerm) {
		
		return infiniteTerm.getMaximumAddedValue();
	}

	@Override
	public Float visit(BinaryExpression binaryExpression) {
		return 0f;

	}

	@Override
	public Float visit(AbsoluteExpression modulusExpression) {
		return 0f;

	}

	@Override
	public Float visit(NormExpression normExpression) {
		return 0f;

	}

	@Override
	public Float visit(SinExpression sinExpression) {
		return 0f;
	}

	@Override
	public Float visit(CosExpression cosExpression) {
		return 0f;
	}

	@Override
	public Float visit(SQRTExpression sqrtExpression) {
		return 0f;
	}

	

	@Override
	public Float visit(ExpressionComparison expressionComparison) {
		return 0f;
	}

	@Override
	public Float visit(Value value) {
		return 0f;
	}

	@Override
	public Float visit(SignedExpression signedExpression) {
		return 0f;
	}

	@Override
	public Float visit(ExistsFormula existsFormula) {
		return Math.max(existsFormula.getBound().accept(this), existsFormula.getFormula().accept(this));
	}

}
