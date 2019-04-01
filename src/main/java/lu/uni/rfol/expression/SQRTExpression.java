package lu.uni.rfol.expression;

import lu.uni.rfol.formulae.RSFOLFormula;
import lu.uni.rfol.visitors.RSFOLVisitor;

public class SQRTExpression implements Expression {

	private final Expression exp;

	public SQRTExpression(Expression exp) {

		this.exp = exp;
	}

	@Override
	public <S> S accept(RSFOLVisitor<S> v) {
		return v.visit(this);
	}

	public Expression getExp() {
		return exp;
	}

	public Expression getSubformula() {
		return exp;
	}

	@Override
	public float getMaximumAddedValue() {
		return exp.getMaximumAddedValue();
	}

	@Override
	public String toString() {
		return "sqrt(" + exp + ")";
	}

	@Override
	public RSFOLFormula pushNegations(boolean negate) {
		return this;
	}
	
	@Override
	public boolean refersToConstantInstant() {
		return this.exp.refersToConstantInstant();
	}
}
