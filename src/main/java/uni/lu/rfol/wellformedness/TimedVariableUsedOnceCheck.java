package uni.lu.rfol.wellformedness;

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
