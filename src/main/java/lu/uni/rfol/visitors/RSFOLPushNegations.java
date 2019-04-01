package lu.uni.rfol.visitors;


import lu.uni.rfol.LOGICOP;
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
import lu.uni.rfol.formulae.RSFOLFormula;
import lu.uni.rfol.timedterm.InfiniteTerm;
import lu.uni.rfol.timedterm.TimedTermExpression;
import lu.uni.rfol.timedterm.TimedTermNumber;

public class RSFOLPushNegations implements RSFOLVisitor<RSFOLFormula>{

	private boolean negate;
	
	public RSFOLPushNegations(boolean negate){
		this.negate=negate;
	}

	public RSFOLFormula visit(SignalID signal) {
		return signal;
	}

	public RSFOLFormula visit(Tvariable tvariable) {
		return tvariable;
	}

	public RSFOLFormula visit(Signal signal) {
		return signal;
	}
	public RSFOLFormula visit(SignalVector signalVector) {
		return signalVector;
	}

	public RSFOLFormula visit(SignalMatrix signalMatrix) {
		return signalMatrix;
	}

	public RSFOLFormula visit(ForallFormula forallFormula) {
		if(negate) {
			return new ExistsFormula(forallFormula.getBound(),
					forallFormula.getFormula().accept(new RSFOLPushNegations(true)));
		}
		return 
				new ForallFormula(forallFormula.getBound(), 
				forallFormula.getFormula().accept(new RSFOLPushNegations(false)));
	}

	@Override
	public RSFOLFormula visit(NotFormula notFormula) {
		if (negate) {
			return notFormula.getSubformula().accept(new RSFOLPushNegations(false));
		}
		return notFormula.getSubformula().accept(new RSFOLPushNegations(true));
	}

	@Override
	public RSFOLFormula visit(BinaryFormula binaryFormula) {
		if (negate) {
			
			LOGICOP op2=LOGICOP.CONJ;
			if(binaryFormula.getOp().equals(LOGICOP.CONJ)) {
				op2=LOGICOP.DISJ;
			}
			if(binaryFormula.getOp().equals(LOGICOP.DISJ)) {
				op2=LOGICOP.CONJ;
			}
			return new BinaryFormula(binaryFormula.getSubformula1().accept(new RSFOLPushNegations(true)), op2, binaryFormula.getSubformula2().accept(new RSFOLPushNegations(true)));
		}
		return 
			new BinaryFormula(binaryFormula.getSubformula1().accept(new RSFOLPushNegations(false)), binaryFormula.getOp(), binaryFormula.getSubformula2().accept(new RSFOLPushNegations(false)));
			
	}

	@Override
	public RSFOLFormula visit(SignalConstantComparison signalConstantComparison) {
		if (negate) {
			return new SignalConstantComparison(signalConstantComparison.getSignal(), 
					RELOP.negate(signalConstantComparison.getOp())
					, signalConstantComparison.getValue());
		}
		return signalConstantComparison;
	}

	@Override
	public RSFOLFormula visit(SignalComparison signalComparison) {

		if (negate) {
			return new SignalComparison(signalComparison.getSignal1(), 
					RELOP.negate(signalComparison.getOp())
					, signalComparison.getSignal2());
		}
		return signalComparison;
	}
	
	@Override
	public RSFOLFormula visit(ExpressionComparison expressionComparison) {
		if (negate) {
			return new ExpressionComparison(expressionComparison.getExpression1(), 
					RELOP.negate(expressionComparison.getOp())
					, expressionComparison.getExpression2());
		}
		return expressionComparison;
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

		return  bound;
	}

	@Override
	public RSFOLFormula visit(InfiniteTerm infiniteTerm) {
		return infiniteTerm;
	}

	@Override
	public RSFOLFormula visit(BinaryExpression binaryExpression) {
		return binaryExpression;
	}

	@Override
	public RSFOLFormula visit(AbsoluteExpression modulusExpression) {
		return modulusExpression;
	}

	@Override
	public RSFOLFormula visit(NormExpression normExpression) {
		return normExpression;
	}

	@Override
	public RSFOLFormula visit(SinExpression sinExpression) {
		return sinExpression;
	}

	@Override
	public RSFOLFormula visit(CosExpression cosExpression) {		
		return cosExpression;
	}

	@Override
	public RSFOLFormula visit(SQRTExpression sqrtExpression) {
		return sqrtExpression;
	}

	

	

	@Override
	public RSFOLFormula visit(Value value) {
		return value;
	}

	@Override
	public RSFOLFormula visit(SignedExpression signedExpression) {
		return signedExpression;
	}

	@Override
	public RSFOLFormula visit(ExistsFormula existsFormula) {
		if(negate) {
			return new ForallFormula(existsFormula.getBound(),
					existsFormula.getFormula().accept(new RSFOLPushNegations(true)));
		}
		return new ExistsFormula(existsFormula.getBound(), 
				existsFormula.getFormula().accept(new RSFOLPushNegations(false)));
	}

}
