package lu.uni.rfol.preprocessing;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

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
import lu.uni.rfol.timedterm.TimedTermVariable;
import lu.uni.rfol.visitors.RSFOLVisitor;

import java.util.Set;

public class PreprocessVariableCreation implements RSFOLVisitor<Set<Entry<String, Float>>> {

	int newvariablecounter = 0;

	private static String variablename = "newtimedvariable";

	private static String currentnewVariableName = "";

	private void createNewVariable() {
		currentnewVariableName = variablename + newvariablecounter;
		newvariablecounter++;
	}

	@Override
	public Set<Entry<String, Float>> visit(SignalID signal) {
		return new HashSet<Entry<String, Float>>();
	}

	@Override
	public Set<Entry<String, Float>> visit(Tvariable tvariable) {
		return new HashSet<Entry<String, Float>>();
	}

	@Override
	public Set<Entry<String, Float>> visit(Signal signal) {
		if(signal.getTimedTerm() instanceof TimedTermNumber) {
			createNewVariable();
			Set<Entry<String, Float>> values = new HashSet<Entry<String, Float>>();
			values.add(new AbstractMap.SimpleEntry<String, Float>(currentnewVariableName, ((TimedTermNumber) signal.getTimedTerm()).getNumber()));
			signal.setTimedTerm(new TimedTermVariable(new Tvariable(currentnewVariableName)));
			return values;
		}
		return new HashSet<Map.Entry<String,Float>>();
	}

	@Override
	public Set<Entry<String, Float>> visit(ForallFormula forallFormula) {
		throw new Error("Not defined for this operator");
	}

	@Override
	public Set<Entry<String, Float>> visit(ExistsFormula existsFormula) {
		throw new Error("Not defined for this operator");
	}

	@Override
	public Set<Entry<String, Float>> visit(NotFormula notFormula) {
		throw new Error("Not defined for this operator");
	}

	@Override
	public Set<Entry<String, Float>> visit(BinaryFormula binaryFormula) {
		throw new Error("Not defined for this operator");
	}

	@Override
	public Set<Entry<String, Float>> visit(SignalConstantComparison signalConstantComparison) {
		return signalConstantComparison.getSignal().accept(this);
	}

	@Override
	public Set<Entry<String, Float>> visit(SignalComparison signalComparison) {
		Set<Entry<String, Float>> set = new HashSet<Entry<String, Float>>();
		set.addAll(signalComparison.getSignal1().accept(this));
		set.addAll(signalComparison.getSignal2().accept(this));
		return set;
	}

	@Override
	public Set<Entry<String, Float>> visit(TimedTermExpression timedTermExpression) {
		return new HashSet<Map.Entry<String,Float>>();
	}

	@Override
	public Set<Entry<String, Float>> visit(TimedTermNumber timedTermNumber) {
		createNewVariable();
		Set<Entry<String, Float>> values = new HashSet<Entry<String, Float>>();
		values.add(new AbstractMap.SimpleEntry<String, Float>(currentnewVariableName, timedTermNumber.getNumber()));
		
		return values;
	}

	@Override
	public Set<Entry<String, Float>> visit(Bound bound) {
		return new HashSet<Map.Entry<String,Float>>();
	}

	@Override
	public Set<Entry<String, Float>> visit(InfiniteTerm infiniteTerm) {
		return new HashSet<Entry<String, Float>>();
	}

	@Override
	public Set<Entry<String, Float>> visit(BinaryExpression binaryExpression) {
		
		Set<Entry<String, Float>> values=new HashSet<Map.Entry<String,Float>>();
		values.addAll(binaryExpression.getLeftExpression().accept(this));
		values.addAll(binaryExpression.getRightExpression().accept(this));
		return values;
	}

	@Override
	public Set<Entry<String, Float>> visit(AbsoluteExpression modulusExpression) {
		return modulusExpression.getExp().accept(this);
	}

	@Override
	public Set<Entry<String, Float>> visit(NormExpression normExpression) {
		return normExpression.getExp().accept(this);
	}

	@Override
	public Set<Entry<String, Float>> visit(SinExpression sinExpression) {
		return sinExpression.getExp().accept(this);
	}

	@Override
	public Set<Entry<String, Float>> visit(CosExpression cosExpression) {
		return cosExpression.getExp().accept(this);
	}

	@Override
	public Set<Entry<String, Float>> visit(SQRTExpression sqrtExpression) {
		return sqrtExpression.getExp().accept(this);
	}

	@Override
	public Set<Entry<String, Float>> visit(SignalVector signalVector) {
		return signalVector.getTimedTerm().accept(this);
	}

	@Override
	public Set<Entry<String, Float>> visit(SignalMatrix signalMatrix) {
		return signalMatrix.getTimedTerm().accept(this);
	}

	@Override
	public Set<Entry<String, Float>> visit(ExpressionComparison expressionComparison) {
		Set<Entry<String, Float>> set = new HashSet<Entry<String, Float>>();
		set.addAll(expressionComparison.getExpression1().accept(this));
		set.addAll(expressionComparison.getExpression2().accept(this));
		return set;
	}

	@Override
	public Set<Entry<String, Float>> visit(Value value) {
		return new HashSet<Entry<String, Float>>();
	}

	@Override
	public Set<Entry<String, Float>> visit(SignedExpression signedExpression) {
		return signedExpression.getExp().accept(this);
	}

}
