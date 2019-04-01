package lu.uni.rfol.visitors;


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
