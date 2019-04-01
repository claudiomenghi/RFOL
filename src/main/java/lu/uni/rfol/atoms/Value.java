package lu.uni.rfol.atoms;

import lu.uni.rfol.expression.Expression;
import lu.uni.rfol.formulae.RSFOLFormula;
import lu.uni.rfol.visitors.RSFOLVisitor;

public class Value implements Expression {

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(val);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Value other = (Value) obj;
		if (Float.floatToIntBits(val) != Float.floatToIntBits(other.val))
			return false;
		return true;
	}

	private float val;
	
	public Value(float val) {
		this.val=val;
	}
	
	public float getVal() {
		return val;
	}

	public void setVal(float val) {
		this.val = val;
	}

	@Override
	public String toString() {
		return Float.toString(val);
	}

	@Override
	public <S> S accept(RSFOLVisitor<S> v) {
		return v.visit(this);
	}

	@Override
	public float getMaximumAddedValue() {
		return Math.max(0, val);
	}

	@Override
	public RSFOLFormula pushNegations(boolean negate) {
		return this;
	}

	@Override
	public boolean refersToConstantInstant() {
		return false;
	}

}
