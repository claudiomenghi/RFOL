package lu.uni.rfol.expression;

import lu.uni.rfol.formulae.RSFOLFormula;
import lu.uni.rfol.formulae.UIGenerator;
import lu.uni.rfol.visitors.RSFOLVisitor;

public class CosExpression implements Expression {

	private final int UI;

	@Override
	public int getUI() {
		return UI;
	}

	@Override
	public String toString() {
		return "cos(" + exp + ")";
	}

	private final Expression exp;

	public CosExpression(Expression exp) {
		UI=UIGenerator.generateUI();
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
