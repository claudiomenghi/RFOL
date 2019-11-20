package lu.uni.rfol.visitors;

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

/**
 * This visitor returns all the timed variables used in the formula
 * @author claudio.menghi
 *
 */
public class RSFOLGetTimedVariables implements RSFOLVisitor<Set<Tvariable>>{

	@Override
	public Set<Tvariable> visit(SignalID SignalID) {
		return new HashSet<>();
	}

	@Override
	public Set<Tvariable> visit(Tvariable tvariable) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.add(tvariable);
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(Signal signal) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(signal.getTimedTerm().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(ForallFormula forallFormula) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(forallFormula.getBound().accept(this));
		timedVariables.addAll(forallFormula.getFormula().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(NotFormula notFormula) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(notFormula.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(BinaryFormula binaryFormula) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(binaryFormula.getSubformula1().accept(this));
		timedVariables.addAll(binaryFormula.getSubformula2().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(SignalConstantComparison signalIDConstantComparison) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(signalIDConstantComparison.getSignal().accept(this));
		timedVariables.addAll(signalIDConstantComparison.getValue().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(SignalComparison signalIDConstantComparison) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(signalIDConstantComparison.getSignal1().accept(this));
		timedVariables.addAll(signalIDConstantComparison.getSignal2().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>visit(TimedTermExpression timedTermExpression) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(timedTermExpression.getTvariable().accept(this));
		timedVariables.addAll(timedTermExpression.getValue().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(TimedTermNumber timedTermNumber) {
		Set<Tvariable> timedVariables=new HashSet<>();
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(Bound bound) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(bound.getLeftbound().accept(this));
		timedVariables.addAll(bound.getRightbound().accept(this));
		timedVariables.addAll(bound.getTvariable().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(InfiniteTerm infiniteTerm) {
		Set<Tvariable> timedVariables=new HashSet<>();
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(BinaryExpression binaryExpression) {
		Set<Tvariable>timedVariables=new HashSet<>();
		timedVariables.addAll(binaryExpression.getLeftExpression().accept(this));
		timedVariables.addAll(binaryExpression.getRightExpression().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(AbsoluteExpression modulusExpression) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(modulusExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(NormExpression normExpression) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(normExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(SinExpression sinExpression) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(sinExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(CosExpression cosExpression) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(cosExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(SQRTExpression sqrtExpression) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(sqrtExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(SignalVector SignalIDVector) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(SignalIDVector.getSignalID().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(SignalMatrix SignalIDMatrix) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(SignalIDMatrix.getSignalID().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(ExpressionComparison expressionComparison) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(expressionComparison.getExpression1().accept(this));
		timedVariables.addAll(expressionComparison.getExpression2().accept(this));

		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(Value value) {
		return new HashSet<>();
	}

	@Override
	public Set<Tvariable> visit(SignedExpression signedExpression) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(signedExpression.getExp().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Tvariable>  visit(ExistsFormula existsFormula) {
		Set<Tvariable> timedVariables=new HashSet<>();
		timedVariables.addAll(existsFormula.getBound().accept(this));
		timedVariables.addAll(existsFormula.getFormula().accept(this));
		return timedVariables;
	}	
}
