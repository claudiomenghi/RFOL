package lu.uni.rfol.visitors;

import java.util.HashSet;
import java.util.Set;

import lu.uni.rfol.RELOP;
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

public class RSFOL2NumberOfConnections implements RSFOLVisitor<Integer> {

	private Set<Integer> componentsAlreadyAdded;


	public RSFOL2NumberOfConnections() {
		this.componentsAlreadyAdded = new HashSet<>();
	}
	
	@Override
	public Integer visit(SignalID signal) {
		if (!componentsAlreadyAdded.contains(signal.hashCode())) {
			componentsAlreadyAdded.add(signal.hashCode());
			return 1;
		}
		return 0;
	}

	@Override
	public Integer visit(Tvariable tvariable) {
		if (!componentsAlreadyAdded.contains(tvariable.hashCode())) {
			componentsAlreadyAdded.add(tvariable.hashCode());

			return 1;
		}
		return 0;
	}

	@Override
	public Integer visit(Signal signal) {
		if (!componentsAlreadyAdded.contains(signal.hashCode())) {
			componentsAlreadyAdded.add(signal.hashCode());
			int res1 = signal.getSignalID().accept(this);
			int res2 = signal.getTimedTerm().accept(this);
			return res1 + res2 + 1;
		}
		return 0;
	}

	@Override
	public Integer visit(SignalVector signalVector) {
		if (!componentsAlreadyAdded.contains(signalVector.hashCode())) {
			componentsAlreadyAdded.add(signalVector.hashCode());
			int res1 = signalVector.getSignalID().accept(this);
			int res2 = signalVector.getTimedTerm().accept(this);
			return res1 + res2 + 2;
		}
		return 0;
	}

	@Override
	public Integer visit(SignalMatrix signalMatrix) {
		if (!componentsAlreadyAdded.contains(signalMatrix.hashCode())) {
			componentsAlreadyAdded.add(signalMatrix.hashCode());
			int res1 = signalMatrix.getSignalID().accept(this);
			int res2 = signalMatrix.getTimedTerm().accept(this);
			return res1 + res2 + 2;
		}
		return 0;
	}
	
	
	@Override
	public Integer visit(ForallFormula forallFormula) {

		if (!componentsAlreadyAdded.contains(forallFormula.hashCode())) {
			componentsAlreadyAdded.add(forallFormula.hashCode());

			int res1 = forallFormula.getBound().accept(this);
			int res2 = forallFormula.getFormula().accept(this);
			return res1 + res2 + 6;
		}
		return 0;
	}

	@Override
	public Integer visit(NotFormula notFormula) {
		if (!componentsAlreadyAdded.contains(notFormula.hashCode())) {
			componentsAlreadyAdded.add(notFormula.hashCode());

			int res1 = notFormula.getSubformula().accept(this);
			return res1 + 1;

		}
		return 0;

	}

	@Override
	public Integer visit(BinaryFormula binaryFormula) {
		if (!componentsAlreadyAdded.contains(binaryFormula.hashCode())) {
			componentsAlreadyAdded.add(binaryFormula.hashCode());

			int res1 = binaryFormula.getSubformula1().accept(this);
			int res2 = binaryFormula.getSubformula2().accept(this);
			return res1 + res2 + 2;
		}
		return 0;
	}

	@Override
	public Integer visit(SignalConstantComparison signalConstantComparison) {
		if (!componentsAlreadyAdded.contains(signalConstantComparison.hashCode())) {
			componentsAlreadyAdded.add(signalConstantComparison.hashCode());

			
			int res = signalConstantComparison.getSignal().accept(this);

			if (signalConstantComparison.getOp() == RELOP.LEQ || signalConstantComparison.getOp() == RELOP.GEQ || signalConstantComparison.getOp() == RELOP.EQ) {
				return res + 9;
			}
			else {
				return res + 12;
							
			}
		}
		return 0;
	}

	@Override
	public Integer visit(SignalComparison signalComparison) {
		if (!componentsAlreadyAdded.contains(signalComparison.hashCode())) {
			componentsAlreadyAdded.add(signalComparison.hashCode());

			int res1 = signalComparison.getSignal1().accept(this);
			int res2 = signalComparison.getSignal2().accept(this);
			
			if (signalComparison.getOp() == RELOP.LEQ || signalComparison.getOp() == RELOP.GEQ || signalComparison.getOp() == RELOP.EQ) {
				return res1 + res2 +9;
			}
			else {
				return res1 + res2 +12;
			}					
		}
		return 0;
	}

	@Override
	public Integer visit(TimedTermExpression timedTermExpression) {
		if (!componentsAlreadyAdded.contains(timedTermExpression.hashCode())) {
			componentsAlreadyAdded.add(timedTermExpression.hashCode());

				return timedTermExpression.getTvariable().accept(this)
						+
						timedTermExpression.getValue().accept(this)
						+2;
			
		}
		return 0;
	}

	@Override
	public Integer visit(TimedTermNumber timedTermNumber) {
		return 0;
	}

	@Override
	public Integer visit(Bound bound) {
		if (!componentsAlreadyAdded.contains(bound.hashCode())) {
			componentsAlreadyAdded.add(bound.hashCode());

			int r1 = bound.getTvariable().accept(this);
			int r2 = bound.getLeftbound().accept(this);
			int r3 = bound.getRightbound().accept(this);
			return r1 + r2 + r3 + 6;
		}
		return 0;
	}

	@Override
	public Integer visit(InfiniteTerm infiniteTerm) {
		return 0;
	}

	@Override
	public Integer visit(BinaryExpression binaryExpression) {
		if (!componentsAlreadyAdded.contains(binaryExpression.hashCode())) {
			componentsAlreadyAdded.add(binaryExpression.hashCode());

			int res1 = binaryExpression.getLeftExpression().accept(this);
			int res2 = binaryExpression.getLeftExpression().accept(this);
			return 2 + res1 + res2;
		}
		return 0;
	}

	@Override
	public Integer visit(AbsoluteExpression modulusExpression) {
		if (!componentsAlreadyAdded.contains(modulusExpression.hashCode())) {
			componentsAlreadyAdded.add(modulusExpression.hashCode());

			int res1 = modulusExpression.getExp().accept(this);
			int res2 = modulusExpression.getSubformula().accept(this);
			return 1 + res1 + res2;
		}
		return 0;
	}

	@Override
	public Integer visit(NormExpression normExpression) {
		if (!componentsAlreadyAdded.contains(normExpression.hashCode())) {
			componentsAlreadyAdded.add(normExpression.hashCode());

			int res1 = normExpression.getExp().accept(this);
			int res2 = normExpression.getSubformula().accept(this);

			return 1 + res1 + res2;
		}
		return 0;
	}

	@Override
	public Integer visit(SinExpression sinExpression) {
		if (!componentsAlreadyAdded.contains(sinExpression.hashCode())) {
			componentsAlreadyAdded.add(sinExpression.hashCode());

			int res1 = sinExpression.getSubformula().accept(this);
			int res2 = sinExpression.getExp().accept(this);

			return 1 + res1 + res2;
		}
		return 0;
	}

	@Override
	public Integer visit(CosExpression cosExpression) {
		if (!componentsAlreadyAdded.contains(cosExpression.hashCode())) {
			componentsAlreadyAdded.add(cosExpression.hashCode());

			int res1 = cosExpression.getSubformula().accept(this);
			int res2 = cosExpression.getExp().accept(this);

			return 1 + res1 + res2;
		}
		return 0;
	}

	@Override
	public Integer visit(SQRTExpression sqrtExpression) {
		if (!componentsAlreadyAdded.contains(sqrtExpression.hashCode())) {
			componentsAlreadyAdded.add(sqrtExpression.hashCode());

			int res1 = sqrtExpression.getSubformula().accept(this);
			int res2 = sqrtExpression.getExp().accept(this);

			return 1 + res1 + res2;
		}
		return 0;
	}


	@Override
	public Integer visit(ExpressionComparison expressionComparison) {
		if (!componentsAlreadyAdded.contains(expressionComparison.hashCode())) {
			componentsAlreadyAdded.add(expressionComparison.hashCode());
			
			int res1 = expressionComparison.getExpression1().accept(this);
			int res2 = expressionComparison.getExpression2().accept(this);
			if (expressionComparison.getOp() == RELOP.LEQ || expressionComparison.getOp() == RELOP.GEQ || expressionComparison.getOp() == RELOP.EQ) {
				return res1 + res2 +9;
			}
			else {
				return res1 + res2 +12;
			}		
		}
		return 0;
	}

	@Override
	public Integer visit(Value value) {
		return 0;
	}
	
	@Override
	public Integer visit(SignedExpression signedExpression) {
		return 1+signedExpression.getExp().accept(this);
	}

	@Override
	public Integer visit(ExistsFormula existsFormula) {
		if (!componentsAlreadyAdded.contains(existsFormula.hashCode())) {
			componentsAlreadyAdded.add(existsFormula.hashCode());

			int res1 = existsFormula.getBound().accept(this);
			int res2 = existsFormula.getFormula().accept(this);
			return res1 + res2 + 6;
		}
		return 0;
	}
}
