package uni.lu.rfol.expression;

import uni.lu.rfol.formulae.RSFOLFormula;
import uni.lu.rfol.visitors.RSFOLVisitor;

public class CosExpression implements Expression {

	@Override
	public String toString() {
		return "cos(" + exp + ")";
	}

	private final Expression exp;

	public CosExpression(Expression exp) {

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
	public RSFOLFormula pushNegations(boolean negate) {
		return this;
	}
	@Override
	public boolean refersToConstantInstant() {
		return this.exp.refersToConstantInstant();
	}

}
