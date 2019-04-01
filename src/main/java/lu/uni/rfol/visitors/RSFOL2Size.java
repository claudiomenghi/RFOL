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
