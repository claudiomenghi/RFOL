package lu.uni.rfol.visitors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import lu.uni.rfol.formulae.Quantification;
import lu.uni.rfol.timedterm.InfiniteTerm;
import lu.uni.rfol.timedterm.TimedTermExpression;
import lu.uni.rfol.timedterm.TimedTermNumber;

/**
 * Returns a maps that relates the quantification (forall and exists) to the
 * constant time instant values used within these quantification
 * 
 * @author claudio.menghi
 *
 */
public class RFOL2GetQuantificatorConstants implements RSFOLVisitor<Set<Value>> {

	public Map<Quantification, Set<Value>> map = new HashMap<Quantification, Set<Value>>();

	@Override
	public Set<Value> visit(SignalID SignalID) {
		return new HashSet<>();
	}

	@Override
	public Set<Value> visit(Tvariable tvariable) {
		return new HashSet<>();
	}

	@Override
	public Set<Value> visit(Signal signal) {

		return signal.getTimedTerm().accept(this);
	}

	@Override
	public Set<Value> visit(ForallFormula forallFormula) {
		Set<Value> values = forallFormula.getFormula().accept(this);
		this.map.put(forallFormula, values);
		return values;
	}

	@Override
	public Set<Value> visit(ExistsFormula existsFormula) {
		Set<Value> values = existsFormula.getFormula().accept(this);
		this.map.put(existsFormula, values);
		return values;
	}

	@Override
	public Set<Value> visit(NotFormula notFormula) {
		return notFormula.getSubformula().accept(this);
	}

	@Override
	public Set<Value> visit(BinaryFormula binaryFormula) {
		Set<Value> timedVariables = new HashSet<>();
		timedVariables.addAll(binaryFormula.getSubformula1().accept(this));
		timedVariables.addAll(binaryFormula.getSubformula2().accept(this));

		return timedVariables;
	}

	@Override
	public Set<Value> visit(SignalConstantComparison signalIDConstantComparison) {
		Set<Value> timedVariables = new HashSet<>();
		timedVariables.addAll(signalIDConstantComparison.getSignal().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Value> visit(SignalComparison signalIDConstantComparison) {
		Set<Value> timedVariables = new HashSet<>();

		timedVariables.addAll(signalIDConstantComparison.getSignal1().accept(this));

		timedVariables.addAll(signalIDConstantComparison.getSignal2().accept(this));

		return timedVariables;
	}

	@Override
	public Set<Value> visit(TimedTermExpression timedTermExpression) {
		Set<Value> timedVariables = new HashSet<>();
		
		
		return timedVariables;
	}

	@Override
	public Set<Value> visit(TimedTermNumber timedTermNumber) {
		Set<Value> timedVariables = new HashSet<>();
		timedVariables.add(new Value(timedTermNumber.getNumber()));
		return timedVariables;
	}

	@Override
	public Set<Value> visit(Bound bound) {
		return new HashSet<>();
	}

	@Override
	public Set<Value> visit(InfiniteTerm infiniteTerm) {
		return new HashSet<>();
	}

	@Override
	public Set<Value> visit(BinaryExpression binaryExpression) {
		Set<Value> timedVariables = new HashSet<>();
		timedVariables.addAll(binaryExpression.getLeftExpression().accept(this));
		timedVariables.addAll(binaryExpression.getRightExpression().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Value> visit(AbsoluteExpression modulusExpression) {
		Set<Value> timedVariables = new HashSet<>();
		timedVariables.addAll(modulusExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Value> visit(NormExpression normExpression) {
		Set<Value> timedVariables = new HashSet<>();
		timedVariables.addAll(normExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Value> visit(SinExpression sinExpression) {
		Set<Value> timedVariables = new HashSet<>();
		timedVariables.addAll(sinExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Value> visit(CosExpression cosExpression) {
		Set<Value> timedVariables = new HashSet<>();
		timedVariables.addAll(cosExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Value> visit(SQRTExpression sqrtExpression) {
		Set<Value> timedVariables = new HashSet<>();
		timedVariables.addAll(sqrtExpression.getSubformula().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Value> visit(SignalVector SignalIDVector) {
		Set<Value> timedVariables = new HashSet<>();
		timedVariables.addAll(SignalIDVector.getSignalID().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Value> visit(SignalMatrix SignalIDMatrix) {
		Set<Value> timedVariables = new HashSet<>();
		timedVariables.addAll(SignalIDMatrix.getSignalID().accept(this));
		return timedVariables;
	}

	@Override
	public Set<Value> visit(ExpressionComparison expressionComparison) {
		Set<Value> timedVariables = new HashSet<>();
		timedVariables.addAll(expressionComparison.getExpression1().accept(this));
		timedVariables.addAll(expressionComparison.getExpression2().accept(this));

		return timedVariables;
	}

	@Override
	public Set<Value> visit(Value value) {
		return new HashSet<>();
	}

	@Override
	public Set<Value> visit(SignedExpression signedExpression) {
		Set<Value> timedVariables = new HashSet<>();
		timedVariables.addAll(signedExpression.getExp().accept(this));
		return timedVariables;
	}

}
