package lu.uni.rfol.shifting.timeshifting;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lu.uni.rfol.Tvariable;
import lu.uni.rfol.atoms.Value;
import lu.uni.rfol.formulae.ExistsFormula;
import lu.uni.rfol.formulae.ForallFormula;
import lu.uni.rfol.formulae.Quantification;
import lu.uni.rfol.formulae.RSFOLFormula;
import lu.uni.rfol.timedterm.TimedTermExpression;
import lu.uni.rfol.visitors.RFOL2GetFSignalTerms;
import lu.uni.rfol.visitors.RFOL2GetQuantificatorConstants;
import lu.uni.rfol.visitors.RFOL2GetQuantifiedFormulae;
import lu.uni.rfol.visitors.RFOLGetFSignalValues;
import lu.uni.rfol.visitors.RSFOLGetTimedVariables;

/** 
 * This procedure implements Algorithm 1 of our paper and contains the time Shifting procedure.
 * 
 * @author claudio.menghi
 *
 */
public class TimeShifting {

	
 
	public RSFOLFormula perform(RSFOLFormula f) {
		
		 		        
		Map<Tvariable,Set<Value>> map=f.accept(new RFOLGetFSignalValues());
		Map<Tvariable,Set<TimedTermExpression>> maptvariableExpressions=f.accept(new RFOL2GetFSignalTerms());
		Map<Tvariable, RSFOLFormula> mapgetTquantifiedFormula=f.accept(new RFOL2GetQuantifiedFormulae());
		
		
		for(Tvariable t: f.accept(new RSFOLGetTimedVariables())) {
			Value max=map.get(t).stream().max(Value.valueComparator).orElse(new Value(0));
			
			for(TimedTermExpression exp: maptvariableExpressions.get(t)) {
				exp.shift(-max.getVal());
			}
			
			RSFOLFormula formula=mapgetTquantifiedFormula.get(t);
			
			if(formula instanceof ForallFormula) {
				ForallFormula forallFormula=(ForallFormula) formula;
				forallFormula.getBound().getLeftbound().shift(max.getVal());
				forallFormula.getBound().getRightbound().shift(max.getVal());
			}
			if(formula instanceof ExistsFormula) {
				ExistsFormula forallFormula=(ExistsFormula) formula;
				forallFormula.getBound().getLeftbound().shift(max.getVal());
				forallFormula.getBound().getRightbound().shift(max.getVal());
			}
		}
		
		RFOL2GetQuantificatorConstants visitor=new RFOL2GetQuantificatorConstants();
		f.accept(visitor);
		Map<Quantification, Set<Value>> quantificationConstantMap=visitor.map;
		
		for(Entry<Quantification, Set<Value>> entry: quantificationConstantMap.entrySet()) {
			
			Tvariable t=entry.getKey().getBound().getTvariable();
			Value max=entry.getValue().stream().max(Value.valueComparator).orElse(new Value(0));
			for(TimedTermExpression exp: maptvariableExpressions.get(t)) {
				exp.shift(-max.getVal());
			}
			entry.getKey().getBound().getLeftbound().shift(max.getVal());
			entry.getKey().getBound().getRightbound().shift(max.getVal());
			
		}
		
		
		
		return f;
	}
		
}
