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

public interface RSFOLVisitor<S> {

	
	/**
	 * returns the output name and the corversion String
	 * @param signal
	 * @return
	 */
	public S visit(SignalID signal);


	public S visit(Tvariable tvariable);

	public S visit(Signal signal);

	public S visit(ForallFormula forallFormula);


	public S visit(NotFormula notFormula);

	public S visit(BinaryFormula binaryFormula);

	public S visit(SignalConstantComparison signalConstantComparison);

	public S visit(SignalComparison signalComparison);

	public S visit(TimedTermExpression timedTermExpression);

	public S visit(TimedTermNumber timedTermNumber);

	public S visit(Bound bound);

	public S visit(InfiniteTerm infiniteTerm);

	public S visit(BinaryExpression binaryExpression);


	public S visit(AbsoluteExpression modulusExpression);


	public S visit(NormExpression normExpression);

	public S visit(SinExpression sinExpression);

	public S visit(CosExpression cosExpression);


	public S visit(SQRTExpression sqrtExpression);


	public S visit(SignalVector signalVector);


	public S visit(SignalMatrix signalMatrix);


	public S visit(ExpressionComparison expressionComparison);


	public S visit(Value value);


	public S visit(SignedExpression signedExpression);


	public S visit(ExistsFormula existsFormula);
}
