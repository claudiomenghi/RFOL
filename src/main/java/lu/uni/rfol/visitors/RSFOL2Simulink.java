package lu.uni.rfol.visitors;

import java.util.HashSet;
import java.util.Set;

import lu.uni.rfol.LOGICOP;
import lu.uni.rfol.RELOP;
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
import lu.uni.rfol.expression.ArithmeticOperator;
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
import lu.uni.rfol.timedterm.InfiniteTerm;
import lu.uni.rfol.timedterm.TimedTermExpression;
import lu.uni.rfol.timedterm.TimedTermNumber;
import lu.uni.rfol.timedterm.TimedTermVariable;

public class RSFOL2Simulink implements RSFOLVisitor<String> {

	
	
	private final String subcomponentname;

	private Set<Integer> componentsAlreadyAdded;

	/**
	 * if set to 1 it uses a delay Simulink/Block to encode the forall/exists operator
	 * if set to 0 it uses a memory Simulink/Block to encode the forall/exists operator
	 */
	private static int MEMORY_DELAY_SELECTOR=0;

	/**
	 * Allows choosing whether a memory or a delay is used in the encoding of the forall/exists temporal operators
	 * 
	 * @param value if set to 1 it uses a delay Simulink/Block to encode the forall/exists operator
	 * if set to 0 it uses a memory Simulink/Block to encode the forall/exists operator
	 * 
	 * @throws IllegalArgumentException if the value is different from 0 and 1
	 */
	public static void setMemoryDelaySelector(int value) {
		if(value!=0 && value!=1) {
			throw new IllegalArgumentException("The MemoryDelaySelector accept a value that is either 0 or 1");
		}
		MEMORY_DELAY_SELECTOR=value;
	}
	
	public RSFOL2Simulink(String modelName, String name) {
		this.subcomponentname = modelName + "/" + name;
		this.componentsAlreadyAdded = new HashSet<>();
	}

	@Override
	public String visit(SignalID signal) {
		StringBuilder builder = new StringBuilder();

		if (!componentsAlreadyAdded.contains(signal.hashCode())) {

			builder.append("%" + signal.toString() + "\n");
			builder.append("add_block('simulink/Ports & Subsystems/In1', '" + subcomponentname + "/" + signal.getName()
					+ "')\n");
			componentsAlreadyAdded.add(signal.hashCode());
			builder.append("add_block('simulink/Signal Attributes/Rate Transition', '" + subcomponentname + "/"
					+ signal.hashCode() + "')\n");
			builder.append("add_line('" + subcomponentname + "', '" + signal.getName() + "/1', '" + signal.hashCode()
					+ "/1')\n");

			componentsAlreadyAdded.add(signal.hashCode());
		}

		return builder.toString();
	}

	@Override
	public String visit(Tvariable tvariable) {

		StringBuilder builder = new StringBuilder();

		if (!componentsAlreadyAdded.contains(tvariable.hashCode())) {

			builder.append("%" + tvariable.toString() + "\n");

			String name = subcomponentname + "/" + tvariable.hashCode();

			builder.append("add_block('simulink/Commonly Used Blocks/Constant', '" + name + "c')\n");
			builder.append("add_block('simulink/Commonly Used Blocks/Integrator', '" + name + "')\n");
			builder.append("add_line('" + subcomponentname + "', '" + tvariable.hashCode() + "c/1', '"
					+ tvariable.hashCode() + "/1')\n");
			componentsAlreadyAdded.add(tvariable.hashCode());
		}

		return builder.toString();
	}

	@Override
	public String visit(Signal signal) {
		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(signal.hashCode())) {

			String v1 = signal.getSignalID().accept(this);

			b.append(v1);

			b.append("%" + signal.toString() + "\n");

			float delayValue = 0;
			if (signal.getTimedTerm() instanceof TimedTermNumber) {
				System.out.println("Ci passo");
				TimedTermNumber t=(TimedTermNumber) signal.getTimedTerm();
				b.append("%" + signal.toString() + "\n");

				String name = subcomponentname + "/" + signal.hashCode();

				b.append("add_block('simulink/Commonly Used Blocks/Constant', '" + name + "c')\n");
				b.append("set_param('" + name+ "c', 'Value', '1')\n");
				
				b.append("add_block('simulink/Commonly Used Blocks/Integrator', '" + name + "integrator')\n");
				b.append("add_line('" + subcomponentname + "','"+signal.hashCode()+"c/1', '"+signal.hashCode()+"integrator/1')\n");
				

				b.append("add_block('simulink/Commonly Used Blocks/Constant', '" + name + "timevalue')\n");
				b.append("set_param('" + name+ "timevalue', 'Value', '"+t.getNumber()+"')\n");

				b.append("add_block('simulink/Logic and Bit Operations/Relational Operator', '" + name + "timecheck')\n");
				b.append("set_param('"+ name + "timecheck', 'Operator', '==')\n");
				
				b.append("add_line('" + subcomponentname + "','"+signal.hashCode()+"integrator/1', '"+signal.hashCode()+"timecheck/1')\n");
				b.append("add_line('" + subcomponentname + "','"+signal.hashCode()+"timevalue/1', '"+signal.hashCode()+"timecheck/2')\n");
				
				
				
				b.append("add_block('simulink/Signal Routing/Switch', '" + name + "Switch')\n");
				b.append("set_param('" + name + "Switch', 'Criteria', 'u2 ~= 0')\n");
				
				b.append("add_line('" + subcomponentname +"','"+signal.hashCode()+"timecheck/1', '"+signal.hashCode()+"Switch/2')\n");
				
				b.append("add_line('" + subcomponentname+"','" + signal.getSignalID().hashCode() + "/1', '"+signal.hashCode()
						+ "Switch/1')\n");
				
				b.append("add_block('simulink/Commonly Used Blocks/Constant', '" + name + "Dummy')\n");
				b.append("set_param('" + name+ "Dummy', 'Value', '2.225073858507201e-308')\n");
				b.append("add_line('" + subcomponentname +"','"+signal.hashCode()+  "Dummy/1','"+signal.hashCode()
						+ "Switch/3')\n");				
				
				
				b.append("add_block('simulink/Math Operations/MinMax', '" + name + "max')\n");
				
				b.append("set_param('" + name + "max', 'Function', 'max')\n");
				b.append("set_param('" + name + "max', 'Inputs', '2')\n");
				
				b.append("add_line('" + subcomponentname + "','"+signal.hashCode()+ "Switch/1','"+signal.hashCode()
						+ "max/1')\n");				
				
				
				b.append("add_block('simulink/Discrete/Memory', '" + name +  "')\n");
				
				b.append("add_line('" + subcomponentname + "','"+signal.hashCode()+ "max/1', '"+signal.hashCode()
						+  "/1')\n");				
				
				b.append("add_line('" + subcomponentname +"','"+signal.hashCode()+  "/1','"+signal.hashCode()
						+ "max/2')\n");		

				
			}
			else {
				if (signal.getTimedTerm() instanceof TimedTermVariable) {
					delayValue = -((TimedTermVariable) signal.getTimedTerm()).getValue().getVal();
					if (((TimedTermVariable) signal.getTimedTerm()).getOperator().equals(ArithmeticOperator.MINUS)) {
						delayValue = -delayValue;
					}
				}
				if (signal.getTimedTerm() instanceof TimedTermExpression) {

					delayValue = -((TimedTermExpression) signal.getTimedTerm()).getValue().getVal();
					if (((TimedTermExpression) signal.getTimedTerm()).getOperator().equals(ArithmeticOperator.MINUS)) {
						delayValue = -delayValue;
					}
				}

				if (delayValue != 0) {
					b.append("add_block('simulink/Continuous/Transport Delay', '" + subcomponentname + "/"
							+ signal.hashCode() + "');\n");
					b.append("set_param('" + subcomponentname + "/" + signal.hashCode() + "', 'DelayTime', '"
							+ delayValue + "')\n");
				} else {
					b.append("add_block('simulink/Math Operations/Gain', '" + subcomponentname + "/" + signal.hashCode()
							+ "')\n");
				}

				b.append("add_line('" + subcomponentname + "', '" + signal.getSignalID().hashCode() + "/1', '"
						+ signal.hashCode() + "/1')\n");
			}

			componentsAlreadyAdded.add(signal.hashCode());
		}

		return b.toString();
	}

	

	@Override
	public String visit(SignalVector signalVector) {

		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(signalVector.hashCode())) {

			String v1 = signalVector.getSignalID().accept(this);

			b.append(v1);

			b.append("%" + signalVector.toString() + "\n");

			float delayValue = 0;
			if (signalVector.getTimedTerm() instanceof TimedTermVariable) {
				delayValue = -((TimedTermVariable) signalVector.getTimedTerm()).getValue().getVal();
				if (((TimedTermVariable) signalVector.getTimedTerm()).getOperator().equals(ArithmeticOperator.MINUS)) {
					delayValue = -delayValue;
				}
			}
			if (signalVector.getTimedTerm() instanceof TimedTermExpression) {
				delayValue = -((TimedTermExpression) signalVector.getTimedTerm()).getValue().getVal();
				if (((TimedTermExpression) signalVector.getTimedTerm()).getOperator()
						.equals(ArithmeticOperator.MINUS)) {
					delayValue = -delayValue;
				}
			}

			if (delayValue != 0) {
				b.append("add_block('simulink/Continuous/Transport Delay', '" + subcomponentname + "/"
						+ signalVector.hashCode() + "');\n");
				b.append("set_param('" + subcomponentname + "/" + signalVector.hashCode() + "', 'DelayTime', '"
						+ delayValue + "')\n");
			} else {
				b.append("add_block('simulink/Math Operations/Gain', '" + subcomponentname + "/"
						+ signalVector.hashCode() + "')\n");
			}


			b.append("add_block('dspindex/Selector', '" + subcomponentname + "/" + signalVector.hashCode()
					+ "extractor')\n");
			b.append("set_param('" + subcomponentname + "/" + signalVector.hashCode()
					+ "extractor', 'InputPortWidth','-1')\n");
			b.append("set_param('" + subcomponentname + "/" + signalVector.hashCode() + "extractor', 'Indices','"
					+ signalVector.getIndex() + "')\n");

			b.append("add_line('" + subcomponentname + "', '" + signalVector.getSignalID().hashCode() + "/1','"
					+ signalVector.hashCode() + "extractor/1')\n");

			b.append("add_line('" + subcomponentname + "', '" + signalVector.hashCode() + "extractor/1','"
					+ signalVector.hashCode() + "/1')\n");

			componentsAlreadyAdded.add(signalVector.hashCode());
		}

		return b.toString();

	}

	@Override
	public String visit(SignalMatrix signalMatrix) {
		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(signalMatrix.hashCode())) {

			String v1 = signalMatrix.getSignalID().accept(this);
			b.append(v1);

			b.append("%" + signalMatrix.toString() + "\n");

			float delayValue = 0;
			if (signalMatrix.getTimedTerm() instanceof TimedTermVariable) {
				delayValue = -((TimedTermVariable) signalMatrix.getTimedTerm()).getValue().getVal();
				if (((TimedTermVariable) signalMatrix.getTimedTerm()).getOperator().equals(ArithmeticOperator.MINUS)) {
					delayValue = -delayValue;
				}
			}
			if (signalMatrix.getTimedTerm() instanceof TimedTermExpression) {
				delayValue = -((TimedTermExpression) signalMatrix.getTimedTerm()).getValue().getVal();
				if (((TimedTermExpression) signalMatrix.getTimedTerm()).getOperator()
						.equals(ArithmeticOperator.MINUS)) {
					delayValue = -delayValue;
				}
			}

			if (delayValue != 0) {
				b.append("add_block('simulink/Continuous/Transport Delay', '" + subcomponentname + "/"
						+ signalMatrix.hashCode() + "');\n");
				b.append("set_param('" + subcomponentname + "/" + signalMatrix.hashCode() + "', 'DelayTime', '"
						+ delayValue + "')\n");
			} else {
				b.append("add_block('simulink/Math Operations/Gain', '" + subcomponentname + "/"
						+ signalMatrix.hashCode() + "')\n");
			}

			b.append("add_block('dspindex/Selector', '" + subcomponentname + "/" + signalMatrix.hashCode()
					+ "extractor')\n");
			b.append("set_param('" + subcomponentname + "/" + signalMatrix.hashCode()
					+ "extractor', 'InputPortWidth','-1')\n");
			b.append("set_param('" + subcomponentname + "/" + signalMatrix.hashCode()
					+ "extractor', 'NumberOfDimensions','2',"
					+ "'IndexOptionArray',{'Index vector (dialog)','Index vector (dialog)'}," + "'IndexParamArray',{'"
					+ signalMatrix.getIndex1() + "','" + signalMatrix.getIndex2() + "'})\n");

			b.append("add_line('" + subcomponentname + "', '" + signalMatrix.getSignalID().hashCode() + "/1','"
					+ signalMatrix.hashCode() + "extractor/1')\n");

			b.append("add_line('" + subcomponentname + "', '" + signalMatrix.hashCode() + "extractor/1','"
					+ signalMatrix.hashCode() + "/1')\n");
			componentsAlreadyAdded.add(signalMatrix.hashCode());
		}

		return b.toString();
	}
	
	@Override
	public String visit(ExistsFormula existsFormula) {
		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(existsFormula.hashCode())) {

			b.append(existsFormula.getBound().accept(this));
			b.append(existsFormula.getFormula().accept(this));

			b.append("%" + existsFormula.toString() + "\n");

			b.append("add_block('simulink/Commonly Used Blocks/Constant', '" + subcomponentname + "/"
					+ existsFormula.hashCode() + "Constant')\n");
			b.append("set_param('" + subcomponentname + "/" + existsFormula.hashCode() + "Constant', 'Value', '-1')\n");

			b.append("add_block('simulink/Signal Routing/Switch', '" + subcomponentname + "/" + existsFormula.hashCode()
					+ "Switch')\n");
			b.append("set_param('" + subcomponentname + "/" + existsFormula.hashCode()
					+ "Switch', 'Criteria', 'u2 > Threshold')\n");

			b.append("add_line('" + subcomponentname + "', '" + existsFormula.hashCode() + "Constant/1', '"
					+ existsFormula.hashCode() + "Switch/3')\n");
			b.append("add_line('" + subcomponentname + "', '" + existsFormula.getBound().hashCode() + "/1', '"
					+ existsFormula.hashCode() + "Switch/2')\n");
			b.append("add_line('" + subcomponentname + "', '" + existsFormula.getFormula().hashCode() + "/1', '"
					+ existsFormula.hashCode() + "Switch/1')\n");

			b.append("add_block('simulink/Math Operations/MinMax', '" + subcomponentname + "/"
					+ existsFormula.hashCode() + "')\n");
			b.append("set_param('" + subcomponentname + "/" + existsFormula.hashCode() + "', 'Function', 'max')\n");

			b.append("add_line('" + subcomponentname + "', '" + existsFormula.hashCode() + "Switch/1', '"
					+ existsFormula.hashCode() + "/1')\n");

			b.append("set_param('" + subcomponentname + "/" + existsFormula.hashCode() + "', 'Inputs', '2')\n");

			
			
			if(MEMORY_DELAY_SELECTOR==1) {
				b.append("add_block('simulink/Discrete/Unit Delay', '" + subcomponentname + "/" + existsFormula.hashCode()
					+ "Delay" + "')\n");

				b.append("set_param('" + subcomponentname + "/" + existsFormula.hashCode() + "Delay', 'X0', '-1')\n");
			}
			if(MEMORY_DELAY_SELECTOR==0) {
				b.append("add_block('simulink/Discrete/Memory', '" + subcomponentname + "/" + existsFormula.hashCode()
				+ "Delay" + "')\n");
				
				b.append("set_param('" + subcomponentname + "/" + existsFormula.hashCode() + "Delay', 'InitialCondition', '-1')\n");
			}
			
			b.append("add_line('" + subcomponentname + "', '" + existsFormula.hashCode() + "/1', '"
					+ existsFormula.hashCode() + "Delay/1')\n");

			b.append("add_line('" + subcomponentname + "', '" + existsFormula.hashCode() + "Delay/1', '"
					+ existsFormula.hashCode() + "/2')\n");


			componentsAlreadyAdded.add(existsFormula.hashCode());
		}
		return b.toString();
	}

	@Override
	public String visit(ForallFormula forallFormula) {
		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(forallFormula.hashCode())) {

			b.append(forallFormula.getBound().accept(this));
			b.append(forallFormula.getFormula().accept(this));

			b.append("%" + forallFormula.toString() + "\n");

			b.append("add_block('simulink/Commonly Used Blocks/Constant', '" + subcomponentname + "/"
					+ forallFormula.hashCode() + "Constant')\n");
			b.append("set_param('" + subcomponentname + "/" + forallFormula.hashCode() + "Constant', 'Value', '1')\n");

			b.append("add_block('simulink/Signal Routing/Switch', '" + subcomponentname + "/" + forallFormula.hashCode()
					+ "Switch')\n");
			b.append("set_param('" + subcomponentname + "/" + forallFormula.hashCode()
					+ "Switch', 'Criteria', 'u2 > Threshold')\n");

			b.append("add_line('" + subcomponentname + "', '" + forallFormula.hashCode() + "Constant/1', '"
					+ forallFormula.hashCode() + "Switch/3')\n");
			b.append("add_line('" + subcomponentname + "', '" + forallFormula.getBound().hashCode() + "/1', '"
					+ forallFormula.hashCode() + "Switch/2')\n");
			b.append("add_line('" + subcomponentname + "', '" + forallFormula.getFormula().hashCode() + "/1', '"
					+ forallFormula.hashCode() + "Switch/1')\n");

			b.append("add_block('simulink/Math Operations/MinMax', '" + subcomponentname + "/"
					+ forallFormula.hashCode() + "')\n");
			b.append("set_param('" + subcomponentname + "/" + forallFormula.hashCode() + "', 'Function', 'min')\n");

			b.append("add_line('" + subcomponentname + "', '" + forallFormula.hashCode() + "Switch/1', '"
					+ forallFormula.hashCode() + "/1')\n");

			b.append("set_param('" + subcomponentname + "/" + forallFormula.hashCode() + "', 'Inputs', '2')\n");

			
			if(MEMORY_DELAY_SELECTOR==1) {
				b.append("add_block('simulink/Discrete/Unit Delay', '" + subcomponentname + "/" + forallFormula.hashCode()
					+ "Delay" + "')\n");

				b.append("set_param('" + subcomponentname + "/" + forallFormula.hashCode() + "Delay', 'X0', '1')\n");
			}
			if(MEMORY_DELAY_SELECTOR==0) {
				b.append("add_block('simulink/Discrete/Memory', '" + subcomponentname + "/" + forallFormula.hashCode()
				+ "Delay" + "')\n");
				
				b.append("set_param('" + subcomponentname + "/" + forallFormula.hashCode() + "Delay', 'InitialCondition', '1')\n");
			}

			b.append("add_line('" + subcomponentname + "', '" + forallFormula.hashCode() + "/1', '"
					+ forallFormula.hashCode() + "Delay/1')\n");

			b.append("add_line('" + subcomponentname + "', '" + forallFormula.hashCode() + "Delay/1', '"
					+ forallFormula.hashCode() + "/2')\n");

			componentsAlreadyAdded.add(forallFormula.hashCode());
		}
		return b.toString();
	}

	@Override
	public String visit(NotFormula notFormula) {
		throw new InternalError("Not supported formula");
	}

	@Override
	public String visit(BinaryFormula binaryFormula) {
		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(binaryFormula.hashCode())) {

			if (binaryFormula.getOp() == LOGICOP.CONJ) {
				String v1 = binaryFormula.getSubformula1().accept(this);
				String v2 = binaryFormula.getSubformula2().accept(this);

				b.append(v1);
				b.append(v2);

				b.append("%" + binaryFormula.toString() + "\n");

				b.append("add_block('simulink/Math Operations/MinMax','" + subcomponentname + "/"
						+ binaryFormula.hashCode() + "')\n");
				b.append("set_param('" + subcomponentname + "/" + binaryFormula.hashCode() + "', 'Inputs', '2')\n");
				b.append("add_line('" + subcomponentname + "', '" + binaryFormula.getSubformula1().hashCode() + "/1', '"
						+ binaryFormula.hashCode() + "/2')\n");
				b.append("add_line('" + subcomponentname + "', '" + binaryFormula.getSubformula2().hashCode() + "/1', '"
						+ binaryFormula.hashCode() + "/1')\n");
				componentsAlreadyAdded.add(binaryFormula.hashCode());
			}
			if (binaryFormula.getOp() == LOGICOP.DISJ) {
				String v1 = binaryFormula.getSubformula1().accept(this);
				String v2 = binaryFormula.getSubformula2().accept(this);

				b.append(v1);
				b.append(v2);

				b.append("%" + binaryFormula.toString() + "\n");

				b.append("add_block('simulink/Math Operations/MinMax','" + subcomponentname + "/"
						+ binaryFormula.hashCode() + "')\n");
				b.append("set_param('" + subcomponentname + "/" + binaryFormula.hashCode() + "', 'Inputs', '2')\n");

				b.append("set_param('" + subcomponentname + "/" + binaryFormula.hashCode() + "', 'function', 'max')\n");
				b.append("add_line('" + subcomponentname + "', '" + binaryFormula.getSubformula1().hashCode() + "/1', '"
						+ binaryFormula.hashCode() + "/2')\n");
				b.append("add_line('" + subcomponentname + "', '" + binaryFormula.getSubformula2().hashCode() + "/1', '"
						+ binaryFormula.hashCode() + "/1')\n");
				componentsAlreadyAdded.add(binaryFormula.hashCode());
			}
			if (binaryFormula.getOp() == LOGICOP.BICOND) {
				throw new InternalError("The operator: " + LOGICOP.BICOND + " cannot be processed");
			}
			if (binaryFormula.getOp() == LOGICOP.IMPL) {
				throw new InternalError("The operator: " + LOGICOP.IMPL + " cannot be processed");
			}
		}
		return b.toString();
	}

	@Override
	public String visit(SignalConstantComparison signalConstantComparison) {

		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(signalConstantComparison.hashCode())) {

			b.append(signalConstantComparison.getSignal().accept(this));
			b.append(signalConstantComparison.getValue().accept(this));

			b.append("% SignalConstantComparison\n");

			b.append("%" + signalConstantComparison.toString() + "\n");

			this.getString(signalConstantComparison.hashCode(), signalConstantComparison.getSignal().hashCode(),
					signalConstantComparison.getValue().hashCode(), b, signalConstantComparison.getOp());
			componentsAlreadyAdded.add(signalConstantComparison.hashCode());

		}
		return b.toString();

	}

	@Override
	public String visit(SignalComparison signalComparison) {
		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(signalComparison.hashCode())) {

			b.append(signalComparison.getSignal1().accept(this));
			b.append(signalComparison.getSignal2().accept(this));

			b.append("% signalComparison\n");
			b.append("%" + signalComparison.toString() + "\n");

			this.getString(signalComparison.hashCode(), signalComparison.getSignal1().hashCode(),
					signalComparison.getSignal2().hashCode(), b, signalComparison.getOp());
			componentsAlreadyAdded.add(signalComparison.hashCode());

		}
		return b.toString();
	}

	@Override
	public String visit(TimedTermExpression timedTermExpression) {
		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(timedTermExpression.hashCode())) {

			b.append(timedTermExpression.getTvariable().accept(this));
			b.append(timedTermExpression.getValue().accept(this));
			b.append("% TimedTermExpression \n");
			b.append("%" + timedTermExpression.toString() + "\n");

			if (timedTermExpression.getOperator().equals(ArithmeticOperator.PLUS)
					|| timedTermExpression.getOperator().equals(ArithmeticOperator.MINUS)) {
				b.append("add_block('simulink/Math Operations/Add','" + subcomponentname + "/"
						+ timedTermExpression.hashCode() + "')\n");

				b.append("set_param('" + subcomponentname + "/" + timedTermExpression.hashCode() + "', 'Inputs',"
						+ (timedTermExpression.getOperator().equals(ArithmeticOperator.PLUS) ? "'++'" : "'+-'")
						+ ")\n");
			}
			if (timedTermExpression.getOperator().equals(ArithmeticOperator.DIV)) {
				b.append("add_block('simulink/Math Operations/Divide','" + subcomponentname + "/"
						+ timedTermExpression.hashCode() + "')\n");
			}
			if (timedTermExpression.getOperator().equals(ArithmeticOperator.PROD)) {
				b.append("add_block('simulink/Math Operations/Product','" + subcomponentname + "/"
						+ timedTermExpression.hashCode() + "')\n");

			}
			b.append("add_line('" + subcomponentname + "', '" + timedTermExpression.getTvariable().hashCode() + "/1', '"
					+ timedTermExpression.hashCode() + "/1')\n");
			b.append("add_line('" + subcomponentname + "', '" + timedTermExpression.getValue().hashCode() + "/1', '"
					+ timedTermExpression.hashCode() + "/2')\n");

			componentsAlreadyAdded.add(timedTermExpression.hashCode());
		}

		return b.toString();
	}

	@Override
	public String visit(TimedTermNumber timedTermNumber) {
		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(timedTermNumber.hashCode())) {
			b.append("%" + timedTermNumber.toString() + "\n");

			String timedTermName = subcomponentname + "/" + timedTermNumber.hashCode();
			b.append("add_block('simulink/Commonly Used Blocks/Constant', '" + timedTermName + "')\n");
			b.append("set_param('" + timedTermName + "', 'Value', '"
					+ Double.toString((double) timedTermNumber.getNumber()) + "')\n");

			componentsAlreadyAdded.add(timedTermNumber.hashCode());

		}
		return b.toString();
	}

	@Override
	public String visit(Bound bound) {
		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(bound.hashCode())) {

			b.append(bound.getTvariable().accept(this));
			b.append(bound.getLeftbound().accept(this));
			b.append(bound.getRightbound().accept(this));
			b.append("%" + bound.toString() + "\n");

			b.append("add_block('simulink/Logic and Bit Operations/Relational Operator', '" + subcomponentname + "/"
					+ bound.hashCode() + "1')\n");

			if (bound.isLopen()) {
				b.append("set_param('" + subcomponentname + "/" + bound.hashCode() + "1', 'Operator', '<')\n");
			}

			b.append("add_block('simulink/Logic and Bit Operations/Relational Operator', '" + subcomponentname + "/"
					+ bound.hashCode() + "2')\n");
			if (bound.isRopen()) {
				b.append("set_param('" + subcomponentname + "/" + bound.hashCode() + "2', 'Operator', '<')\n");
			}

			b.append("add_block('simulink/Logic and Bit Operations/Logical Operator', '" + subcomponentname + "/"
					+ bound.hashCode() + "')\n");

			b.append("add_line('" + subcomponentname + "', '" + bound.getLeftbound().hashCode() + "/1', '"
					+ bound.hashCode() + "1/1')\n");
			b.append("add_line('" + subcomponentname + "', '" + bound.getTvariable().hashCode() + "/1', '"
					+ bound.hashCode() + "1/2')\n");

			b.append("add_line('" + subcomponentname + "', '" + bound.getTvariable().hashCode() + "/1', '"
					+ bound.hashCode() + "2/1')\n");
			b.append("add_line('" + subcomponentname + "', '" + bound.getRightbound().hashCode() + "/1', '"
					+ bound.hashCode() + "2/2')\n");

			b.append("add_line('" + subcomponentname + "', '" + bound.hashCode() + "1/1', '" + bound.hashCode()
					+ "/1')\n");
			b.append("add_line('" + subcomponentname + "', '" + bound.hashCode() + "2/1', '" + bound.hashCode()
					+ "/2')\n");
			componentsAlreadyAdded.add(bound.hashCode());

		}
		return b.toString();
	}

	@Override
	public String visit(InfiniteTerm infiniteTerm) {
		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(infiniteTerm.hashCode())) {
			b.append("%" + infiniteTerm.toString() + "\n");

			String timedTermName = subcomponentname + "/" + infiniteTerm.hashCode();
			b.append("add_block('simulink/Commonly Used Blocks/Constant', '" + timedTermName + "')\n");
			b.append("set_param('" + timedTermName + "', 'Value', 't_fin')\n");
			componentsAlreadyAdded.add(infiniteTerm.hashCode());

		}
		return b.toString();
	}

	@Override
	public String visit(BinaryExpression binaryExpression) {
		StringBuilder b = new StringBuilder();
		if (!componentsAlreadyAdded.contains(binaryExpression.hashCode())) {
			if (binaryExpression.getOp() == ArithmeticOperator.MINUS) {
				String v1 = binaryExpression.getLeftExpression().accept(this);
				String v2 = binaryExpression.getRightExpression().accept(this);

				b.append(v1);
				b.append(v2);

				b.append("%" + binaryExpression.toString() + "\n");

				b.append("add_block('simulink/Math Operations/Add','" + subcomponentname + "/"
						+ binaryExpression.hashCode() + "')\n");

				b.append("set_param('" + subcomponentname + "/" + binaryExpression.hashCode() + "', 'Inputs', '+-')\n");

				b.append("add_line('" + subcomponentname + "', '" + binaryExpression.getLeftExpression().hashCode()
						+ "/1', '" + binaryExpression.hashCode() + "/1')\n");
				b.append("add_line('" + subcomponentname + "', '" + binaryExpression.getRightExpression().hashCode()
						+ "/1', '" + binaryExpression.hashCode() + "/2')\n");
				componentsAlreadyAdded.add(binaryExpression.hashCode());
			}
			if (binaryExpression.getOp() == ArithmeticOperator.PLUS) {
				String v1 = binaryExpression.getLeftExpression().accept(this);
				String v2 = binaryExpression.getRightExpression().accept(this);

				b.append(v1);
				b.append(v2);

				b.append("%" + binaryExpression.toString() + "\n");

				b.append("add_block('simulink/Math Operations/Add','" + subcomponentname + "/"
						+ binaryExpression.hashCode() + "')\n");
				b.append("set_param('" + subcomponentname + "/" + binaryExpression.hashCode() + "', 'Inputs', '++')\n");

				b.append("add_line('" + subcomponentname + "', '" + binaryExpression.getLeftExpression().hashCode()
						+ "/1', '" + binaryExpression.hashCode() + "/2')\n");
				b.append("add_line('" + subcomponentname + "', '" + binaryExpression.getRightExpression().hashCode()
						+ "/1', '" + binaryExpression.hashCode() + "/1')\n");
				componentsAlreadyAdded.add(binaryExpression.hashCode());
			}
			if (binaryExpression.getOp() == ArithmeticOperator.PROD) {
				String v1 = binaryExpression.getLeftExpression().accept(this);
				String v2 = binaryExpression.getRightExpression().accept(this);

				b.append(v1);
				b.append(v2);

				b.append("%" + binaryExpression.toString() + "\n");

				b.append("add_block('simulink/Math Operations/Product','" + subcomponentname + "/"
						+ binaryExpression.hashCode() + "')\n");

				b.append("add_line('" + subcomponentname + "', '" + binaryExpression.getLeftExpression().hashCode()
						+ "/1', '" + binaryExpression.hashCode() + "/1')\n");
				b.append("add_line('" + subcomponentname + "', '" + binaryExpression.getRightExpression().hashCode()
						+ "/1', '" + binaryExpression.hashCode() + "/2')\n");
				componentsAlreadyAdded.add(binaryExpression.hashCode());
			}
			if (binaryExpression.getOp() == ArithmeticOperator.DIV) {
				String v1 = binaryExpression.getLeftExpression().accept(this);
				String v2 = binaryExpression.getRightExpression().accept(this);

				b.append(v1);
				b.append(v2);

				b.append("%" + binaryExpression.toString() + "\n");

				b.append("add_block('simulink/Math Operations/Divide','" + subcomponentname + "/"
						+ binaryExpression.hashCode() + "')\n");

				b.append("add_line('" + subcomponentname + "', '" + binaryExpression.getLeftExpression().hashCode()
						+ "/1', '" + binaryExpression.hashCode() + "/1')\n");
				b.append("add_line('" + subcomponentname + "', '" + binaryExpression.getRightExpression().hashCode()
						+ "/1', '" + binaryExpression.hashCode() + "/2')\n");
				componentsAlreadyAdded.add(binaryExpression.hashCode());
			}
		}

		return b.toString();
	}

	@Override
	public String visit(AbsoluteExpression modulusExpression) {

		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(modulusExpression.hashCode())) {
			b.append(modulusExpression.getSubformula().accept(this));

			b.append("%" + modulusExpression.toString() + "\n");

			String constantName = subcomponentname + "/" + modulusExpression.hashCode();
			b.append("add_block('simulink/Math Operations/Abs','" + constantName + "')\n");

			b.append("add_line('" + subcomponentname + "', '" + modulusExpression.getSubformula().hashCode() + "/1', '"
					+ modulusExpression.hashCode() + "/1')\n");

			componentsAlreadyAdded.add(modulusExpression.hashCode());
		}
		return b.toString();
	}

	@Override
	public String visit(NormExpression normExpression) {

		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(normExpression.hashCode())) {
			b.append(normExpression.getSubformula().accept(this));

			b.append("%" + normExpression.toString() + "\n");

			String constantName = subcomponentname + "/" + normExpression.hashCode();
			// b.append("add_block('simulink/Math Operations/Normalization','" +
			// constantName + "')\n");
			b.append("add_block('simulink/User-Defined Functions/MATLAB Fcn','" + constantName + "')\n");

			b.append("set_param('" + constantName + "', 'MATLABFcn', 'norm')\n");
			b.append("add_line('" + subcomponentname + "', '" + normExpression.getSubformula().hashCode() + "/1', '"
					+ normExpression.hashCode() + "/1')\n");

			componentsAlreadyAdded.add(normExpression.hashCode());
		}
		return b.toString();
	}

	@Override
	public String visit(SinExpression sinExpression) {
		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(sinExpression.hashCode())) {
			b.append(sinExpression.getSubformula().accept(this));

			b.append("%" + sinExpression.toString() + "\n");

			String constantName = subcomponentname + "/" + sinExpression.hashCode();
			b.append("add_block('simulink/Lookup Tables/Sine','" + constantName + "')\n");

			b.append("add_line('" + subcomponentname + "', '" + sinExpression.getSubformula().hashCode() + "/1', '"
					+ sinExpression.hashCode() + "/1')\n");

			componentsAlreadyAdded.add(sinExpression.hashCode());
		}
		return b.toString();
	}

	@Override
	public String visit(CosExpression cosExpression) {
		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(cosExpression.hashCode())) {
			b.append(cosExpression.getSubformula().accept(this));

			b.append("%" + cosExpression.toString() + "\n");

			String constantName = subcomponentname + "/" + cosExpression.hashCode();
			b.append("add_block('simulink/Lookup Tables/Cosine','" + constantName + "')\n");

			b.append("add_line('" + subcomponentname + "', '" + cosExpression.getSubformula().hashCode() + "/1', '"
					+ cosExpression.hashCode() + "/1')\n");

			componentsAlreadyAdded.add(cosExpression.hashCode());
		}
		return b.toString();
	}

	@Override
	public String visit(SQRTExpression sqrtExpression) {
		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(sqrtExpression.hashCode())) {
			b.append(sqrtExpression.getSubformula().accept(this));

			b.append("%" + sqrtExpression.toString() + "\n");

			String constantName = subcomponentname + "/" + sqrtExpression.hashCode();
			b.append("add_block('simulink/Math Operations/Sqrt','" + constantName + "')\n");

			b.append("add_line('" + subcomponentname + "', '" + sqrtExpression.getSubformula().hashCode() + "/1', '"
					+ sqrtExpression.hashCode() + "/1')\n");

			componentsAlreadyAdded.add(sqrtExpression.hashCode());
		}
		return b.toString();
	}

	

	private void getString(int currehashcode, int hascodeChild1, int hashcoldChild2, StringBuilder b, RELOP op) {

		if (op == RELOP.LEQ || op == RELOP.GEQ || op == RELOP.EQ) {
			getEQString(currehashcode, hascodeChild1, hashcoldChild2, b, op);
		}

		if (op == RELOP.GE || op == RELOP.NEQ || op == RELOP.LE) {
			getNEQString(currehashcode, hascodeChild1, hashcoldChild2, b, op);
		}
	}

	private void getEQString(int currehashcode, int hascodeChild1, int hashcoldChild2, StringBuilder b, RELOP op) {

		b.append("add_block('simulink/Math Operations/Add','" + subcomponentname + "/" + currehashcode + "mu')\n");
		b.append("set_param('" + subcomponentname + "/" + currehashcode + "mu', 'Inputs', '+-')\n");
		b.append("add_line('" + subcomponentname + "', '" + hascodeChild1 + "/1', '" + currehashcode + "mu/1')\n");
		b.append("add_line('" + subcomponentname + "', '" + hashcoldChild2 + "/1', '" + currehashcode + "mu/2')\n");

		b.append("add_block('simulink/Math Operations/Abs','" + subcomponentname + "/" + currehashcode + "muabs')\n");
		b.append("add_line('" + subcomponentname + "', '" + currehashcode + "mu/1', '" + currehashcode + "muabs/1')\n");

		b.append("add_block('simulink/Commonly Used Blocks/Constant', '" + subcomponentname + "/" + currehashcode
				+ "one')\n");
		b.append("set_param('" + subcomponentname + "/" + currehashcode + "one', 'Value', '1')\n");

		b.append("add_block('simulink/Math Operations/Add','" + subcomponentname + "/" + currehashcode + "den')\n");

		b.append(
				"add_line('" + subcomponentname + "', '" + currehashcode + "muabs/1', '" + currehashcode + "den/1')\n");
		b.append("add_line('" + subcomponentname + "', '" + currehashcode + "one/1', '" + currehashcode + "den/2')\n");

		
		b.append("add_block('simulink/Math Operations/Divide', '" + subcomponentname + "/" + currehashcode + "div')\n");
		if (op == RELOP.EQ) {
			b.append("add_line('" + subcomponentname + "', '" + currehashcode + "muabs/1', '" + currehashcode
					+ "div/1')\n");
		}
		if (op == RELOP.GEQ || op == RELOP.LEQ) {
			b.append("add_line('" + subcomponentname + "', '" + currehashcode + "mu/1', '" + currehashcode
					+ "div/1')\n");
		}
		b.append("add_line('" + subcomponentname + "', '" + currehashcode + "den/1', '" + currehashcode + "div/2')\n");

		b.append("add_block('simulink/Math Operations/Gain', '" + subcomponentname + "/" + currehashcode + "gain')\n");
		
		if(op==RELOP.EQ || op==RELOP.LEQ) {
			b.append("set_param('" + subcomponentname + "/" + currehashcode + "gain', 'Gain', '-1')\n");
		}
		b.append("add_line('" + subcomponentname + "', '" + currehashcode + "div/1', '" + currehashcode + "gain/1')\n");


		b.append("add_block('simulink/Signal Attributes/Data Type Conversion', '" + subcomponentname + "/"
				+ currehashcode + "')\n");

		b.append("add_line('" + subcomponentname + "', '" + currehashcode + "gain/1', '" + currehashcode + "/1')\n");

		componentsAlreadyAdded.add(currehashcode);
	}

	private void getNEQString(int currehashcode, int hascodeChild1, int hashcoldChild2, StringBuilder b, RELOP op) {

		b.append("add_block('simulink/Math Operations/Add','" + subcomponentname + "/" + currehashcode + "mu')\n");

		b.append("set_param('" + subcomponentname + "/" + currehashcode + "mu', 'Inputs', '+-')\n");

		b.append("add_line('" + subcomponentname + "', '" + hascodeChild1 + "/1', '" + currehashcode + "mu/1')\n");

		b.append("add_line('" + subcomponentname + "', '" + hashcoldChild2 + "/1', '" + currehashcode + "mu/2')\n");

		b.append("add_block('simulink/Math Operations/Abs','" + subcomponentname + "/" + currehashcode + "muabs')\n");
		b.append("add_line('" + subcomponentname + "', '" + currehashcode + "mu/1', '" + currehashcode + "muabs/1')\n");

		b.append("add_block('simulink/Math Operations/Add','" + subcomponentname + "/" + currehashcode + "den')\n");

		b.append(
				"add_line('" + subcomponentname + "', '" + currehashcode + "muabs/1', '" + currehashcode + "den/1')\n");

		b.append("add_block('simulink/Commonly Used Blocks/Constant', '" + subcomponentname + "/" + currehashcode
				+ "one')\n");
		b.append("set_param('" + subcomponentname + "/" + currehashcode + "one', 'Value', '1')\n");
		b.append("add_line('" + subcomponentname + "', '" + currehashcode + "one/1', '" + currehashcode + "den/2')\n");

		b.append("add_block('simulink/Math Operations/Divide', '" + subcomponentname + "/" + currehashcode + "div')\n");

		if (op == RELOP.NEQ) {

			b.append("add_line('" + subcomponentname + "', '" + currehashcode + "muabs/1', '" + currehashcode
					+ "div/1')\n");
		}
		if (op == RELOP.GE || op == RELOP.LE) {
			b.append("add_line('" + subcomponentname + "', '" + currehashcode + "mu/1', '" + currehashcode
					+ "div/1')\n");
		}

		b.append("add_line('" + subcomponentname + "', '" + currehashcode + "den/1', '" + currehashcode + "div/2')\n");

		b.append("add_block('simulink/Math Operations/Gain', '" + subcomponentname + "/" + currehashcode + "gain')\n");
		b.append("add_line('" + subcomponentname + "', '" + currehashcode + "div/1', '" + currehashcode + "gain/1')\n");
		if (op == RELOP.LE) {
			b.append("set_param('" + subcomponentname + "/" + currehashcode + "gain', 'Gain', '-1')\n");
		} else {
			b.append("set_param('" + subcomponentname + "/" + currehashcode + "gain', 'Gain', '1')\n");
		}

		b.append("add_block('simulink/Signal Routing/Switch', '" + subcomponentname + "/" + currehashcode
				+ "switch')\n");

		b.append("add_block('simulink/Commonly Used Blocks/Constant', '" + subcomponentname + "/" + currehashcode
				+ "epsilon')\n");

		b.append("set_param('" + subcomponentname + "/" + currehashcode + "epsilon', 'Value', '-0.01')\n");

		b.append("set_param('" + subcomponentname + "/" + currehashcode + "switch','Criteria', 'u2 ~= 0')\n");


		b.append("add_line('" + subcomponentname + "', '" + currehashcode + "gain/1', '" + currehashcode
				+ "switch/1')\n");
		b.append("add_line('" + subcomponentname + "', '" + currehashcode + "mu/1', '" + currehashcode
				+ "switch/2')\n");
		b.append("add_line('" + subcomponentname + "', '" + currehashcode + "epsilon/1', '" + currehashcode
				+ "switch/3')\n");
		
		b.append("add_block('simulink/Signal Attributes/Data Type Conversion', '" + subcomponentname + "/"
				+ currehashcode + "')\n");

		b.append("add_line('" + subcomponentname + "', '" + currehashcode + "switch/1', '" + currehashcode + "/1')\n");

		componentsAlreadyAdded.add(currehashcode);

	}

	@Override
	public String visit(ExpressionComparison expressionComparison) {
		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(expressionComparison.hashCode())) {

			b.append(expressionComparison.getExpression1().accept(this));
			b.append(expressionComparison.getExpression2().accept(this));

			b.append("% ExpressionComparison\n");

			b.append("%" + expressionComparison.toString() + "\n");

			this.getString(expressionComparison.hashCode(), expressionComparison.getExpression1().hashCode(),
					expressionComparison.getExpression2().hashCode(), b, expressionComparison.getOp());

			b.append("% ExpressionComparisonEnd\n");
			componentsAlreadyAdded.add(expressionComparison.hashCode());
		}
		return b.toString();
	}

	@Override
	public String visit(Value value) {
		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(value.hashCode())) {
			b.append("%" + value.toString() + "\n");
			b.append("add_block('simulink/Commonly Used Blocks/Constant', '" + subcomponentname + "/" + value.hashCode()
					+ "')\n");
			b.append("set_param('" + subcomponentname + "/" + value.hashCode() + "', 'Value', '" + value.getVal()
					+ "')\n");
			componentsAlreadyAdded.add(value.hashCode());
		}
		return b.toString();
	}

	@Override
	public String visit(SignedExpression signedExpression) {
		StringBuilder b = new StringBuilder();

		if (!componentsAlreadyAdded.contains(signedExpression.hashCode())) {
			b.append(signedExpression.getExp().accept(this));

			b.append("%" + signedExpression.toString() + "\n");

			String constantName = subcomponentname + "/" + signedExpression.hashCode();
			b.append("add_block('simulink/Math Operations/Gain','" + constantName + "')\n");
			b.append("set_param('" + subcomponentname + "/" + signedExpression.hashCode() + "', 'Gain', '-1')\n");

			b.append("add_line('" + subcomponentname + "', '" + signedExpression.getExp().hashCode() + "/1', '"
					+ signedExpression.hashCode() + "/1')\n");

			componentsAlreadyAdded.add(signedExpression.hashCode());
		}
		return b.toString();
	}

	
}
