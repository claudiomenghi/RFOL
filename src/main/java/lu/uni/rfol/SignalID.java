package lu.uni.rfol;

import lu.uni.rfol.formulae.RSFOLFormula;
import lu.uni.rfol.formulae.UIGenerator;
import lu.uni.rfol.visitors.RSFOLVisitor;

public class SignalID implements RSFOLFormula {

	private final String name;

	private final int UI;

	@Override
	public int getUI() {
		return UI;
	}

	public SignalID(String name) {
		UI=UIGenerator.generateUI();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public <S> S accept(RSFOLVisitor<S> v) {
		return v.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		SignalID other = (SignalID) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public RSFOLFormula pushNegations(boolean negate) {
		return this;
	}

}
