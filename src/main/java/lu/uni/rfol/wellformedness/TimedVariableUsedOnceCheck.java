package lu.uni.rfol.wellformedness;

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

public class TimedVariableUsedOnceCheck implements RSFOLVisitor<Boolean> {

	private Set<Tvariable> variablesUsedInOneBound;
	
	public TimedVariableUsedOnceCheck() {
		this.variablesUsedInOneBound=new HashSet<>();
	}

	@Override
	public Boolean visit(SignalID signal) {
		return true;
	}

	@Override
	public Boolean visit(Tvariable tvariable) {
		return true;
	}

	@Override
	public Boolean visit(Signal signal) {
		return true;
	}

	@Override
	public Boolean visit(ForallFormula forallFormula) {
		return forallFormula.getBound().accept(this)&& forallFormula.getFormula().accept(this);
	}

	@Override
	public Boolean visit(NotFormula notFormula) {
		return notFormula.getSubformula().accept(this);
	}

	@Override
	public Boolean visit(BinaryFormula binaryFormula) {
		return binaryFormula.getSubformula1().accept(this) && binaryFormula.getSubformula2().accept(this);
	}

	@Override
	public Boolean visit(SignalConstantComparison signalConstantComparison) {
		return true;
	}

	@Override
	public Boolean visit(SignalComparison signalComparison) {
		return true;
	}

	@Override
	public Boolean visit(TimedTermExpression timedTermExpression) {
		return true;
	}

	@Override
	public Boolean visit(TimedTermNumber timedTermNumber) {
		return true;
	}

	@Override
	public Boolean visit(Bound bound) {
		if(this.variablesUsedInOneBound.contains(bound.getTvariable())) {
			return false;
		}
		else {
			this.variablesUsedInOneBound.add(bound.getTvariable());
			return true;
		}
	}

	@Override
	public Boolean visit(InfiniteTerm infiniteTerm) {
		return true;
	}

	@Override
	public Boolean visit(BinaryExpression binaryExpression) {
		return true;
	}

	@Override
	public Boolean visit(AbsoluteExpression modulusExpression) {
		return true;
	}

	@Override
	public Boolean visit(NormExpression normExpression) {
		return true;
	}

	@Override
	public Boolean visit(SinExpression sinExpression) {
		return true;
	}

	@Override
	public Boolean visit(CosExpression cosExpression) {
		return true;
	}

	@Override
	public Boolean visit(SQRTExpression sqrtExpression) {
		return true;
	}

	@Override
	public Boolean visit(SignalVector signalVector) {
		return true;
	}

	@Override
	public Boolean visit(SignalMatrix signalMatrix) {
		return true;
	}

	@Override
	public Boolean visit(ExpressionComparison expressionComparison) {
		return true;
	}

	@Override
	public Boolean visit(Value value) {
		return true;
	}

	@Override
	public Boolean visit(SignedExpression signedExpression) {
		return true;
	}

	@Override
	public Boolean visit(ExistsFormula existsFormula) {
		return existsFormula.getBound().accept(this)&& existsFormula.getFormula().accept(this);
	}
	
	

}
