package uni.lu.rfol.visitors;

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

public class RSFOLGetSignals implements RSFOLVisitor<Set<SignalID>>{

	@Override
	public Set<SignalID> visit(SignalID SignalID) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.add(SignalID);
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(Tvariable tvariable) {
		return new HashSet<>();
	}

	@Override
	public Set<SignalID> visit(Signal signal) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(signal.getSignalID().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(ForallFormula forallFormula) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(forallFormula.getBound().accept(this));
		SignalIDs.addAll(forallFormula.getFormula().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(NotFormula notFormula) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(notFormula.getSubformula().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(BinaryFormula binaryFormula) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(binaryFormula.getSubformula1().accept(this));
		SignalIDs.addAll(binaryFormula.getSubformula2().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(SignalConstantComparison SignalIDConstantComparison) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(SignalIDConstantComparison.getSignal().accept(this));
		SignalIDs.addAll(SignalIDConstantComparison.getValue().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(SignalComparison SignalIDComparison) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(SignalIDComparison.getSignal1().accept(this));
		SignalIDs.addAll(SignalIDComparison.getSignal2().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(TimedTermExpression timedTermExpression) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(timedTermExpression.getTvariable().accept(this));
		SignalIDs.addAll(timedTermExpression.getValue().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(TimedTermNumber timedTermNumber) {
		Set<SignalID> SignalIDs=new HashSet<>();
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(Bound bound) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(bound.getLeftbound().accept(this));
		SignalIDs.addAll(bound.getRightbound().accept(this));
		SignalIDs.addAll(bound.getTvariable().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(InfiniteTerm infiniteTerm) {
		Set<SignalID> SignalIDs=new HashSet<>();
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(BinaryExpression binaryExpression) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(binaryExpression.getLeftExpression().accept(this));
		SignalIDs.addAll(binaryExpression.getRightExpression().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(AbsoluteExpression modulusExpression) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(modulusExpression.getSubformula().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(NormExpression normExpression) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(normExpression.getSubformula().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(SinExpression sinExpression) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(sinExpression.getSubformula().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(CosExpression cosExpression) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(cosExpression.getSubformula().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(SQRTExpression sqrtExpression) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(sqrtExpression.getSubformula().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(SignalVector SignalIDVector) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(SignalIDVector.getSignalID().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(SignalMatrix SignalIDMatrix) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(SignalIDMatrix.getSignalID().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(ExpressionComparison expressionComparison) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(expressionComparison.getExpression1().accept(this));
		SignalIDs.addAll(expressionComparison.getExpression2().accept(this));

		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(Value value) {
		return new HashSet<>();
	}

	@Override
	public Set<SignalID> visit(SignedExpression signedExpression) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(signedExpression.getExp().accept(this));
		return SignalIDs;
	}

	@Override
	public Set<SignalID> visit(ExistsFormula existsFormula) {
		Set<SignalID> SignalIDs=new HashSet<>();
		SignalIDs.addAll(existsFormula.getBound().accept(this));
		SignalIDs.addAll(existsFormula.getFormula().accept(this));
		return SignalIDs;
	}	
}
