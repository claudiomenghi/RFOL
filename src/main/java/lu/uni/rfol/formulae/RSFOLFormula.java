package lu.uni.rfol.formulae;

import lu.uni.rfol.visitors.RSFOLVisitor;

public interface RSFOLFormula {
	
	
	
	
	public <S> S accept(RSFOLVisitor<S> v);
	
	public RSFOLFormula pushNegations(boolean negate);

	public int getUI();
	
}


