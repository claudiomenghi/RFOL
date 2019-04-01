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
