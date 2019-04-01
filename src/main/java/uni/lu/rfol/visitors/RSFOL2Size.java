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

public class RSFOL2Size implements RSFOLVisitor<Integer> {

	@Override
	public Integer visit(SignalID signal) {
		return 0;
	}

	@Override
	public Integer visit(Tvariable tvariable) {
		return 0;
	}

	@Override
	public Integer visit(Signal signal) {
		return signal.getSignalID().accept(this)+signal.getTimedTerm().accept(this);
	}

	@Override
	public Integer visit(ForallFormula forallFormula) {
		return 1+forallFormula.getBound().accept(this)+forallFormula.getFormula().accept(this);
	}

	@Override
	public Integer visit(NotFormula notFormula) {
		return 1+notFormula.getSubformula().accept(this);
	}

	@Override
	public Integer visit(BinaryFormula binaryFormula) {
		return 1+binaryFormula.getSubformula1().accept(this)+binaryFormula.getSubformula2().accept(this);
	}

	@Override
	public Integer visit(SignalConstantComparison signalConstantComparison) {
		return 1+signalConstantComparison.getSignal().accept(this);
	}

	@Override
	public Integer visit(SignalComparison signalComparison) {
		return 1+signalComparison.getSignal1().accept(this)+signalComparison.getSignal2().accept(this);
	}

	@Override
	public Integer visit(TimedTermExpression timedTermExpression) {
		return 1+timedTermExpression.getTvariable().accept(this);
	}

	@Override
	public Integer visit(TimedTermNumber timedTermNumber) {
		return 0;
	}

	@Override
	public Integer visit(Bound bound) {
		return bound.getLeftbound().accept(this)+bound.getRightbound().accept(this);
	}

	@Override
	public Integer visit(InfiniteTerm infiniteTerm) {
		return 0;
	}

	@Override
	public Integer visit(BinaryExpression binaryExpression) {
		return 1+binaryExpression.getLeftExpression().accept(this)+binaryExpression.getRightExpression().accept(this);
	}

	@Override
	public Integer visit(AbsoluteExpression modulusExpression) {
		return 1+modulusExpression.getSubformula().accept(this);
	}

	@Override
	public Integer visit(NormExpression normExpression) {
		return 1+normExpression.getSubformula().accept(this);
	}

	@Override
	public Integer visit(SinExpression sinExpression) {
		return 1+sinExpression.getSubformula().accept(this);
	}

	@Override
	public Integer visit(CosExpression cosExpression) {
		return 1+cosExpression.getSubformula().accept(this);
	}

	@Override
	public Integer visit(SQRTExpression sqrtExpression) {
		return 1+sqrtExpression.getSubformula().accept(this);
	}

	@Override
	public Integer visit(SignalVector signalVector) {
		return 0;
	}

	@Override
	public Integer visit(SignalMatrix signalMatrix) {
		return 0;
	}

	@Override
	public Integer visit(ExpressionComparison expressionComparison) {
		return 1+expressionComparison.getExpression1().accept(this)+expressionComparison.getExpression2().accept(this);
	}

	@Override
	public Integer visit(Value value) {
		return 0;
	}

	@Override
	public Integer visit(SignedExpression signedExpression) {
		return 1+signedExpression.getExp().accept(this);
	}

	@Override
	public Integer visit(ExistsFormula existsFormula) {
		return 1+existsFormula.getBound().accept(this)+existsFormula.getFormula().accept(this);
	}
}
