package uni.lu.rfol.formulae;

import uni.lu.rfol.visitors.RSFOLVisitor;

public interface RSFOLFormula {
	
	public <S> S accept(RSFOLVisitor<S> v);
	
	public RSFOLFormula pushNegations(boolean negate);

	
}


