package lu.uni.rfol.shifting.timeshifting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import lu.uni.rfol.Signal;
import lu.uni.rfol.SignalID;
import lu.uni.rfol.SignalMatrix;
import lu.uni.rfol.SignalVector;
import lu.uni.rfol.Tvariable;
import lu.uni.rfol.atoms.ExpressionComparison;
import lu.uni.rfol.atoms.SignalComparison;
import lu.uni.rfol.atoms.SignalConstantComparison;
import lu.uni.rfol.atoms.Value;
import lu.uni.rfol.expression.AbsoluteExpression;
import lu.uni.rfol.expression.BinaryExpression;
import lu.uni.rfol.expression.CosExpression;
import lu.uni.rfol.expression.NormExpression;
import lu.uni.rfol.expression.SQRTExpression;
import lu.uni.rfol.expression.SignedExpression;
import lu.uni.rfol.expression.SinExpression;
import lu.uni.rfol.formulae.BinaryFormula;
import lu.uni.rfol.formulae.Bound;
import lu.uni.rfol.formulae.ExistsFormula;
import lu.uni.rfol.formulae.ForallFormula;
import lu.uni.rfol.formulae.NotFormula;
import lu.uni.rfol.formulae.RSFOLFormula;
import lu.uni.rfol.timedterm.InfiniteTerm;
import lu.uni.rfol.timedterm.TimedTermExpression;
import lu.uni.rfol.timedterm.TimedTermNumber;
import lu.uni.rfol.visitors.RSFOLVisitor;

import java.util.Set;

public class GetFormulaeWithT implements RSFOLVisitor<Map<Tvariable, Set<RSFOLFormula>>> {

	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(SignalID signal) {
		return new HashMap<>();
	}



	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(Tvariable tvariable) {
		return new HashMap<>();

	}

	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(Signal signal) {
		return signal.getTimedTerm().accept(this);
	}

	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(ForallFormula forallFormula) {
		HashMap<Tvariable, Set<RSFOLFormula>> mp = new HashMap<>();
		for (Entry<Tvariable, Set<RSFOLFormula>> e : forallFormula.getBound().accept(this).entrySet()) {
			if (!mp.containsKey(e.getKey())) {
				mp.put(e.getKey(), new HashSet<>());
			}
			mp.get(e.getKey()).addAll(e.getValue());
		}

		for (Entry<Tvariable, Set<RSFOLFormula>> e : forallFormula.getFormula().accept(this).entrySet()) {
			if (!mp.containsKey(e.getKey())) {
				mp.put(e.getKey(), new HashSet<>());
			}
			mp.get(e.getKey()).addAll(e.getValue());
		}

		return mp;
	}


	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(NotFormula notFormula) {
		return notFormula.getSubformula().accept(this);
	}

	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(BinaryFormula binaryFormula) {
		HashMap<Tvariable, Set<RSFOLFormula>> mp = new HashMap<>();

		for (Entry<Tvariable, Set<RSFOLFormula>> e : binaryFormula.getSubformula1().accept(this).entrySet()) {
			if (!mp.containsKey(e.getKey())) {
				mp.put(e.getKey(), new HashSet<>());
			}
			mp.get(e.getKey()).addAll(e.getValue());
		}

		for (Entry<Tvariable, Set<RSFOLFormula>> e : binaryFormula.getSubformula2().accept(this).entrySet()) {
			if (!mp.containsKey(e.getKey())) {
				mp.put(e.getKey(), new HashSet<>());
			}
			mp.get(e.getKey()).addAll(e.getValue());
		}

		return mp;
	}

	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(SignalConstantComparison signalConstantComparison) {
		return signalConstantComparison.getSignal().accept(this);
	}

	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(SignalComparison signalComparison) {

		HashMap<Tvariable, Set<RSFOLFormula>> mp = new HashMap<>();

		for (Entry<Tvariable, Set<RSFOLFormula>> e : signalComparison.getSignal1().accept(this).entrySet()) {
			if (!mp.containsKey(e.getKey())) {
				mp.put(e.getKey(), new HashSet<>());
			}
			mp.get(e.getKey()).addAll(e.getValue());
		}

		for (Entry<Tvariable, Set<RSFOLFormula>> e : signalComparison.getSignal2().accept(this).entrySet()) {
			if (!mp.containsKey(e.getKey())) {
				mp.put(e.getKey(), new HashSet<>());
			}
			mp.get(e.getKey()).addAll(e.getValue());
		}

		return mp;
	}

	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(TimedTermExpression timedTermExpression) {
		HashMap<Tvariable, Set<RSFOLFormula>> mp = new HashMap<>();
		mp.put(timedTermExpression.getTvariable(), new HashSet<>());
		mp.get(timedTermExpression.getTvariable()).add(timedTermExpression);
		return mp;
	}

	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(TimedTermNumber timedTermNumber) {
		return new HashMap<>();
	}

	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(Bound bound) {
		HashMap<Tvariable, Set<RSFOLFormula>> mp = new HashMap<>();
		mp.put(bound.getTvariable(), new HashSet<>());
		mp.get(bound.getTvariable()).add(bound);

		return mp;
	}



	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(InfiniteTerm infiniteTerm) {
		return new HashMap<>();
	}



	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(BinaryExpression binaryExpression) {
		HashMap<Tvariable, Set<RSFOLFormula>> mp = new HashMap<>();

		for (Entry<Tvariable, Set<RSFOLFormula>> e : binaryExpression.getLeftExpression().accept(this).entrySet()) {
			if (!mp.containsKey(e.getKey())) {
				mp.put(e.getKey(), new HashSet<>());
			}
			mp.get(e.getKey()).addAll(e.getValue());
		}

		for (Entry<Tvariable, Set<RSFOLFormula>> e : binaryExpression.getRightExpression().accept(this).entrySet()) {
			if (!mp.containsKey(e.getKey())) {
				mp.put(e.getKey(), new HashSet<>());
			}
			mp.get(e.getKey()).addAll(e.getValue());
		}

		return mp;
	}



	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(AbsoluteExpression modulusExpression) {
		return modulusExpression.getSubformula().accept(this);
	}



	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(NormExpression normExpression) {
		return normExpression.getSubformula().accept(this);
	}

	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(SinExpression sinExpression) {
		return sinExpression.getSubformula().accept(this);
	}



	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(CosExpression cosExpression) {
		return cosExpression.getSubformula().accept(this);
	}

	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(SQRTExpression sqrtExpression) {
		return sqrtExpression.getSubformula().accept(this);
	}



	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(SignalVector signalVector) {
		return signalVector.getTimedTerm().accept(this);
	}



	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(SignalMatrix signalMatrix) {
		return signalMatrix.getTimedTerm().accept(this);
	}



	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(ExpressionComparison expressionComparison) {
		HashMap<Tvariable, Set<RSFOLFormula>> mp = new HashMap<>();

		for (Entry<Tvariable, Set<RSFOLFormula>> e : expressionComparison.getExpression1().accept(this).entrySet()) {
			if (!mp.containsKey(e.getKey())) {
				mp.put(e.getKey(), new HashSet<>());
			}
			mp.get(e.getKey()).addAll(e.getValue());
		}

		for (Entry<Tvariable, Set<RSFOLFormula>> e : expressionComparison.getExpression2().accept(this).entrySet()) {
			if (!mp.containsKey(e.getKey())) {
				mp.put(e.getKey(), new HashSet<>());
			}
			mp.get(e.getKey()).addAll(e.getValue());
		}

		return mp;
	}



	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(Value value) {
		return  new HashMap<>();
	}



	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(SignedExpression signedExpression) {
		return signedExpression.getExp().accept(this);
	}



	@Override
	public Map<Tvariable, Set<RSFOLFormula>> visit(ExistsFormula existsFormula) {
		HashMap<Tvariable, Set<RSFOLFormula>> mp = new HashMap<>();
		for (Entry<Tvariable, Set<RSFOLFormula>> e : existsFormula.getBound().accept(this).entrySet()) {
			if (!mp.containsKey(e.getKey())) {
				mp.put(e.getKey(), new HashSet<>());
			}
			mp.get(e.getKey()).addAll(e.getValue());
		}

		for (Entry<Tvariable, Set<RSFOLFormula>> e : existsFormula.getFormula().accept(this).entrySet()) {
			if (!mp.containsKey(e.getKey())) {
				mp.put(e.getKey(), new HashSet<>());
			}
			mp.get(e.getKey()).addAll(e.getValue());
		}

		return mp;
	}
}
