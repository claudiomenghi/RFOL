package uni.lu.rfol.shifting.intervalshif;

import java.util.HashSet;
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
import uni.lu.rfol.timedterm.InfiniteTerm;
import uni.lu.rfol.timedterm.TimedTermExpression;
import uni.lu.rfol.timedterm.TimedTermNumber;
import uni.lu.rfol.visitors.RSFOLVisitor;

public class GetVariableTimedTerms implements RSFOLVisitor<Set<TimedTermExpression>> {

	@Override
	public Set<TimedTermExpression> visit(SignalID signal) {
		return new HashSet<>();
	}

	@Override
	public Set<TimedTermExpression> visit(Tvariable tvariable) {
		return new HashSet<>();
	}

	@Override
	public Set<TimedTermExpression> visit(Signal signal) {
		Set<TimedTermExpression> term = new HashSet<>();
		term.addAll(signal.getTimedTerm().accept(this));
		return term;
	}

	@Override
	public Set<TimedTermExpression> visit(ForallFormula forallFormula) {
		return forallFormula.getFormula().accept(this);
	}

	@Override
	public Set<TimedTermExpression> visit(NotFormula notFormula) {
		return notFormula.getSubformula().accept(this);
	}

	@Override
	public Set<TimedTermExpression> visit(BinaryFormula binaryFormula) {
		Set<TimedTermExpression> term = new HashSet<>();
		term.addAll(binaryFormula.getSubformula1().accept(this));
		term.addAll(binaryFormula.getSubformula2().accept(this));
		return term;
	}

	@Override
	public Set<TimedTermExpression> visit(SignalConstantComparison signalConstantComparison) {
		return signalConstantComparison.getSignal().accept(this);
	}

	@Override
	public Set<TimedTermExpression> visit(SignalComparison signalComparison) {
		return new HashSet<>();
	}

	@Override
	public Set<TimedTermExpression> visit(TimedTermExpression timedTermExpression) {
		HashSet<TimedTermExpression> t = new HashSet<>();
		t.add(timedTermExpression);
		return t;
	}

	@Override
	public Set<TimedTermExpression> visit(TimedTermNumber timedTermNumber) {
		return new HashSet<>();
	}

	@Override
	public Set<TimedTermExpression> visit(Bound bound) {
		return new HashSet<>();
	}

	@Override
	public Set<TimedTermExpression> visit(InfiniteTerm infiniteTerm) {
		return new HashSet<>();
	}

	@Override
	public Set<TimedTermExpression> visit(BinaryExpression binaryExpression) {
		Set<TimedTermExpression> term = new HashSet<>();
		term.addAll(binaryExpression.getLeftExpression().accept(this));
		term.addAll(binaryExpression.getRightExpression().accept(this));		
		return term;
	}

	@Override
	public Set<TimedTermExpression> visit(AbsoluteExpression modulusExpression) {
		return modulusExpression.getSubformula().accept(this);
	}

	@Override
	public Set<TimedTermExpression> visit(NormExpression normExpression) {
		return normExpression.getSubformula().accept(this);
	}

	@Override
	public Set<TimedTermExpression> visit(SinExpression sinExpression) {
		return sinExpression.getSubformula().accept(this);
	}

	@Override
	public Set<TimedTermExpression> visit(CosExpression cosExpression) {
		return cosExpression.getSubformula().accept(this);
	}

	@Override
	public Set<TimedTermExpression> visit(SQRTExpression sqrtExpression) {
		return sqrtExpression.getSubformula().accept(this);
	}

	@Override
	public Set<TimedTermExpression> visit(SignalVector signalVector) {
		return new HashSet<>();
	}

	@Override
	public Set<TimedTermExpression> visit(SignalMatrix signalMatrix) {
		return new HashSet<>();
	}

	@Override
	public Set<TimedTermExpression> visit(ExpressionComparison expressionComparison) {
		return new HashSet<>();
	}

	@Override
	public Set<TimedTermExpression> visit(Value value) {
		return new HashSet<>();
	}

	@Override
	public Set<TimedTermExpression> visit(SignedExpression signedExpression) {
		return new HashSet<>();
	}

	@Override
	public Set<TimedTermExpression> visit(ExistsFormula existsFormula) {
		return existsFormula.getFormula().accept(this);
	}

}
