package lu.uni.rfol.timedterm;

import lu.uni.rfol.expression.ArithmeticOperator;
import lu.uni.rfol.expression.Expression;
import lu.uni.rfol.formulae.RSFOLFormula;
import lu.uni.rfol.visitors.RSFOLVisitor;

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
