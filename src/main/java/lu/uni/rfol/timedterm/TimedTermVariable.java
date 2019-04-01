package lu.uni.rfol.timedterm;


import lu.uni.rfol.Tvariable;
import lu.uni.rfol.atoms.Value;
import lu.uni.rfol.expression.ArithmeticOperator;

public class TimedTermVariable extends TimedTermExpression implements TimedTerm {

	public TimedTermVariable(Tvariable tvariable) {
		super(tvariable, ArithmeticOperator.PLUS, 
				new Value(0));
	}


}
