package lu.uni.rfol.shifting.intervalshif;

import java.util.HashSet;
import java.util.Set;

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
import lu.uni.rfol.visitors.RSFOLVisitor;

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
