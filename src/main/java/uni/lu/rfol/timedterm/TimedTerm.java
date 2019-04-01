package uni.lu.rfol.timedterm;

import uni.lu.rfol.expression.ArithmeticOperator;
import uni.lu.rfol.expression.Expression;
import uni.lu.rfol.formulae.RSFOLFormula;
import uni.lu.rfol.visitors.RSFOLVisitor;

public interface TimedTerm extends RSFOLFormula {

	public <S> S accept(RSFOLVisitor<S> v);
	
	public void compose(Expression value, ArithmeticOperator op);
	
	public float getMaximumAddedValue();
	
	public void shift(float value);
	
	/**
	 * returns true if and only if the timed term is a constant 
	 * @return true if and only if the  timed term is a constant 
	 */
	public boolean refersToConstantInstant();
}
