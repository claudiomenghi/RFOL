package uni.lu.rfol.timedterm;


import uni.lu.rfol.Tvariable;
import uni.lu.rfol.atoms.Value;
import uni.lu.rfol.expression.ArithmeticOperator;

public class TimedTermVariable extends TimedTermExpression implements TimedTerm {

	public TimedTermVariable(Tvariable tvariable) {
		super(tvariable, ArithmeticOperator.PLUS, 
				new Value(0));
	}


}
