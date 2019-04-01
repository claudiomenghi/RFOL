package lu.uni.rfol.preprocessing;

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
import lu.uni.rfol.expression.Expression;
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
import lu.uni.rfol.timedterm.TimedTerm;
import lu.uni.rfol.timedterm.TimedTermExpression;
import lu.uni.rfol.timedterm.TimedTermNumber;
import lu.uni.rfol.visitors.RSFOLVisitor;

import java.util.Set;

public class Preprocess implements RSFOLVisitor<RSFOLFormula> {

	int newvariablecounter = 0;

	@Override
	public RSFOLFormula visit(SignalID signal) {
		return signal;
	}

	@Override
	public RSFOLFormula visit(Tvariable tvariable) {
		return tvariable;
	}

	@Override
	public RSFOLFormula visit(Signal signal) {
		return new Signal(signal.getSignalID(), (TimedTerm) signal.getTimedTerm().accept(this));
	}

	@Override
	public RSFOLFormula visit(ForallFormula forallFormula) {
		return new ForallFormula((Bound) forallFormula.getBound().accept(this),
				forallFormula.getFormula().accept(this));
	}

	@Override
	public RSFOLFormula visit(NotFormula notFormula) {
		return new NotFormula(notFormula.getSubformula().accept(this));
	}

	@Override
	public RSFOLFormula visit(BinaryFormula binaryFormula) {
		return new BinaryFormula(binaryFormula.getSubformula1().accept(this), binaryFormula.getOp(),
				binaryFormula.getSubformula2().accept(this));
	}

	@Override
	public RSFOLFormula visit(SignalConstantComparison signalConstantComparison) {
		if (signalConstantComparison.refersToConstantInstant()) {

			Set<Entry<String, Float>> res = signalConstantComparison.accept(new PreprocessVariableCreation());
			RSFOLFormula resf = signalConstantComparison;

			for (Entry<String, Float> e : res) {
				resf = new ForallFormula(new Bound(new Tvariable(e.getKey()), new TimedTermNumber(e.getValue()),
						new TimedTermNumber(e.getValue()), false, false), resf);
			}
			return resf;
		} else {
			return signalConstantComparison;
		}
	}

	@Override
	public RSFOLFormula visit(SignalComparison signalComparison) {
		if (signalComparison.refersToConstantInstant()) {

			Set<Entry<String, Float>> res = signalComparison.accept(new PreprocessVariableCreation());
			RSFOLFormula resf = signalComparison;

			for (Entry<String, Float> e : res) {
				resf = new ForallFormula(new Bound(new Tvariable(e.getKey()), new TimedTermNumber(e.getValue()),
						new TimedTermNumber(e.getValue()), false, false), resf);
			}
			return resf;
		} else {
			return signalComparison;
		}
	}

	@Override
	public RSFOLFormula visit(TimedTermExpression timedTermExpression) {
		return timedTermExpression;
	}

	@Override
	public RSFOLFormula visit(TimedTermNumber timedTermNumber) {
		return timedTermNumber;
	}

	@Override
	public RSFOLFormula visit(Bound bound) {
		return bound;
	}

	@Override
	public RSFOLFormula visit(InfiniteTerm infiniteTerm) {
		return infiniteTerm;
	}

	@Override
	public RSFOLFormula visit(BinaryExpression binaryExpression) {
		binaryExpression.getLeftExpression().accept(this);
		binaryExpression.getRightExpression().accept(this);
		return binaryExpression;
	}

	@Override
	public RSFOLFormula visit(AbsoluteExpression modulusExpression) {
		modulusExpression.getExp().accept(this);
		return modulusExpression;
	}

	@Override
	public RSFOLFormula visit(NormExpression normExpression) {
		normExpression.getExp().accept(this);
		return normExpression;
	}

	@Override
	public RSFOLFormula visit(SinExpression sinExpression) {
		sinExpression.getExp().accept(this);
		return sinExpression;
	}

	@Override
	public RSFOLFormula visit(CosExpression cosExpression) {
		cosExpression.getExp().accept(this);
		return cosExpression;
	}

	@Override
	public RSFOLFormula visit(SQRTExpression sqrtExpression) {
		sqrtExpression.getExp().accept(this);
		return sqrtExpression;
	}

	@Override
	public RSFOLFormula visit(SignalVector signalVector) {
		signalVector.getTimedTerm().accept(this);
		return signalVector;
	}

	@Override
	public RSFOLFormula visit(SignalMatrix signalMatrix) {
		return new SignalMatrix((SignalID) signalMatrix.getSignalID().accept(this),
				(TimedTerm) signalMatrix.getTimedTerm().accept(this), (int) signalMatrix.getIndex1(),
				(int) signalMatrix.getIndex2());
	}

	@Override
	public RSFOLFormula visit(ExpressionComparison expressionComparison) {
		
		if (expressionComparison.refersToConstantInstant()) {

			Set<Entry<String, Float>> res = expressionComparison.accept(new PreprocessVariableCreation());
			RSFOLFormula resf = expressionComparison;

			for (Entry<String, Float> e : res) {
				resf = new ForallFormula(new Bound(new Tvariable(e.getKey()), new TimedTermNumber(e.getValue()),
						new TimedTermNumber(e.getValue()), false, false), resf);
			}
			return resf;
		} else {
			return expressionComparison;
		}
	}

	@Override
	public RSFOLFormula visit(Value value) {
		return value;
	}

	@Override
	public RSFOLFormula visit(SignedExpression signedExpression) {
		return new SignedExpression((Expression) signedExpression.getExp().accept(this), signedExpression.getOp());
	}

	@Override
	public RSFOLFormula visit(ExistsFormula existsFormula) {
		return new ExistsFormula((Bound) existsFormula.getBound().accept(this),
				existsFormula.getFormula().accept(this));
	}
}
