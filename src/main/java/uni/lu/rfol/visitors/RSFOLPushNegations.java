package uni.lu.rfol.visitors;


import uni.lu.rfol.LOGICOP;
import uni.lu.rfol.RELOP;
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
import uni.lu.rfol.formulae.RSFOLFormula;
import uni.lu.rfol.timedterm.InfiniteTerm;
import uni.lu.rfol.timedterm.TimedTermExpression;
import uni.lu.rfol.timedterm.TimedTermNumber;

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
