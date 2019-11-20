package lu.uni.rfol.visitors;

import java.util.HashMap;
import java.util.Map;

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
import lu.uni.rfol.formulae.RSFOLFormula;
import lu.uni.rfol.timedterm.InfiniteTerm;
import lu.uni.rfol.timedterm.TimedTermExpression;
import lu.uni.rfol.timedterm.TimedTermNumber;

/**
 * Returns eturns the set of values where used in all the signal terms in the
 * form f(t+n)
 * 
 * @author claudio.menghi
 *
 */
public class RFOL2GetQuantifiedFormulae implements RSFOLVisitor<Map<Tvariable, RSFOLFormula>> {

	@Override
	public Map<Tvariable, RSFOLFormula> visit(SignalID SignalID) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(Tvariable tvariable) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(Signal signal) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(ForallFormula forallFormula) {
		
		Map<Tvariable, RSFOLFormula> map=new HashMap<Tvariable, RSFOLFormula>();
		map.putAll(forallFormula.getFormula().accept(this));
		map.put(forallFormula.getBound().getTvariable(), forallFormula);
		return map;
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(NotFormula notFormula) {
		return notFormula.getSubformula().accept(this);
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(BinaryFormula binaryFormula) {
		Map<Tvariable, RSFOLFormula> timedVariables = new HashMap<>();
		timedVariables.putAll(binaryFormula.getSubformula1().accept(this));
		timedVariables.putAll(binaryFormula.getSubformula2().accept(this));

		
		return timedVariables;
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(SignalConstantComparison signalIDConstantComparison) {
		return new HashMap<Tvariable, RSFOLFormula>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(SignalComparison signalIDConstantComparison) {
		
		return new HashMap<Tvariable, RSFOLFormula>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(TimedTermExpression timedTermExpression) {
		return new HashMap<Tvariable, RSFOLFormula>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(TimedTermNumber timedTermNumber) {
		return new HashMap<Tvariable, RSFOLFormula>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(Bound bound) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(InfiniteTerm infiniteTerm) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(BinaryExpression binaryExpression) {
		return new HashMap<Tvariable, RSFOLFormula>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(AbsoluteExpression modulusExpression) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(NormExpression normExpression) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(SinExpression sinExpression) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(CosExpression cosExpression) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(SQRTExpression sqrtExpression) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(SignalVector SignalIDVector) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(SignalMatrix SignalIDMatrix) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(ExpressionComparison expressionComparison) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(Value value) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(SignedExpression signedExpression) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, RSFOLFormula> visit(ExistsFormula existsFormula) {
		Map<Tvariable, RSFOLFormula> map=new HashMap<Tvariable, RSFOLFormula>();
		map.putAll(existsFormula.getFormula().accept(this));
		map.put(existsFormula.getBound().getTvariable(), existsFormula);
		return map;
	}
}
