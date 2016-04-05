package comp207p.main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;
import java.util.ArrayList;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.util.InstructionFinder;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.TargetLostException;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.*;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.ArithmeticInstruction;


public class ConstantFolder
{
	ClassParser parser = null;
	ClassGen gen = null;

	JavaClass original = null;
	JavaClass optimized = null;

	Stack<Number> constantStack = null;
	ArrayList<Number> variables = new ArrayList<Number>(128);
	int offset = 0;

	public ConstantFolder(String classFilePath)
	{
		try{
      System.out.println(classFilePath);
			this.parser = new ClassParser(classFilePath);
			this.original = this.parser.parse();
			this.gen = new ClassGen(this.original);
		} catch(IOException e){
			e.printStackTrace();
		}

		constantStack = new Stack<Number>();


	}

	private void performeArithmetic(InstructionHandle handle)
	{
		Number valueOne = constantStack.pop();
		Number valueTwo = constantStack.pop();

		if(handle.getInstruction() instanceof IADD )
		{
			Number newValue = valueOne.intValue() + valueTwo.intValue();
			constantStack.push(newValue);
		}
		else if (handle.getInstruction() instanceof LADD)
		{
			Number newValue = valueOne.longValue() + valueTwo.longValue();
			constantStack.push(newValue);
		}
		else if (handle.getInstruction() instanceof FADD)
		{
			Number newValue = valueOne.floatValue() + valueTwo.floatValue();
			constantStack.push(newValue);
		}
		else if (handle.getInstruction() instanceof DADD)
		{
			Number newValue = valueOne.doubleValue() + valueTwo.doubleValue();
			constantStack.push(newValue);
		}

		else if(handle.getInstruction() instanceof IMUL )
		{
			Number newValue = valueOne.intValue() * valueTwo.intValue();
			constantStack.push(newValue);
		}
		else if (handle.getInstruction() instanceof LMUL)
		{
			Number newValue = valueOne.longValue() * valueTwo.longValue();
			constantStack.push(newValue);
		}
		else if (handle.getInstruction() instanceof FMUL)
		{
			Number newValue = valueOne.floatValue() * valueTwo.floatValue();
			constantStack.push(newValue);
		}
		else if (handle.getInstruction() instanceof DMUL)
		{
			Number newValue = valueOne.doubleValue() * valueTwo.doubleValue();
			constantStack.push(newValue);
		}

		else if(handle.getInstruction() instanceof ISUB )
		{
			Number newValue = valueOne.intValue() - valueTwo.intValue();
			constantStack.push(newValue);
		}
		else if (handle.getInstruction() instanceof LSUB)
		{
			Number newValue = valueOne.longValue() - valueTwo.longValue();
			constantStack.push(newValue);
		}
		else if (handle.getInstruction() instanceof FSUB)
		{
			Number newValue = valueOne.floatValue() - valueTwo.floatValue();
			constantStack.push(newValue);
		}
		else if (handle.getInstruction() instanceof DSUB)
		{
			Number newValue = valueOne.doubleValue() - valueTwo.doubleValue();
			constantStack.push(newValue);
		}

		else if(handle.getInstruction() instanceof IDIV )
		{
			Number newValue = valueOne.intValue() - valueTwo.intValue();
			constantStack.push(newValue);
		}
		else if (handle.getInstruction() instanceof LDIV)
		{
			Number newValue = valueOne.longValue() - valueTwo.longValue();
			constantStack.push(newValue);
		}
		else if (handle.getInstruction() instanceof FDIV)
		{
			Number newValue = valueOne.floatValue() - valueTwo.floatValue();
			constantStack.push(newValue);
		}
		else if (handle.getInstruction() instanceof DDIV)
		{
			Number newValue = valueOne.doubleValue() - valueTwo.doubleValue();
			constantStack.push(newValue);
		}
	}

  private void optimizeMethod(ClassGen cgen, ConstantPoolGen cpgen, Method method)
  {
    // Get the Code of the method, which is a collection of bytecode instructions
		Code methodCode = method.getCode();

		// Now get the actualy bytecode data in byte array,
		// and use it to initialise an InstructionList
		InstructionList instList = new InstructionList(methodCode.getCode());

		// Initialise a method generator with the original method as the baseline
		MethodGen methodGen = new MethodGen(method.getAccessFlags(), method.getReturnType(), method.getArgumentTypes(), null, method.getName(), cgen.getClassName(), instList, cpgen);
		InstructionHandle h1 = null, h2 = null;
		//InstructionHandle is a wrapper for actual Instructions
		System.out.println("New method started: " + method.getName());
		for(InstructionHandle handle : instList.getInstructionHandles())
    {
			System.out.println(handle.toString());

			boolean isLDC = (handle.getInstruction() instanceof LDC) || (handle.getInstruction() instanceof LDC_W) || (handle.getInstruction() instanceof LDC2_W);
			boolean isArithmeticInst = (handle.getInstruction() instanceof ArithmeticInstruction);
			boolean isPush = (handle.getInstruction() instanceof SIPUSH) || (handle.getInstruction() instanceof BIPUSH);
			boolean isConst = (handle.getInstruction() instanceof ICONST || handle.getInstruction() instanceof FCONST || handle.getInstruction() instanceof LCONST || handle.getInstruction() instanceof DCONST);
			boolean isStore = (handle.getInstruction() instanceof StoreInstruction);
			boolean isLoad = (handle.getInstruction() instanceof LoadInstruction);

			if(isLDC)
			{
				Number value = getConstantValue(handle, cpgen);
				constantStack.push(value);
				try
				{
					instList.delete(handle);
				}
				catch (TargetLostException e)
	      {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      }

			}
			else if(isPush)
			{
				Number value = getConstantValue(handle, cpgen);
				constantStack.push(value);
				try
				{
					instList.delete(handle);
				}
				catch (TargetLostException e)
				{
					// TODO Auto-generated catch block
					InstructionHandle[] targets = e.getTargets();
		    for(int i=0; i < targets.length; i++) {
		      InstructionTargeter[] targeters = targets[i].getTargeters();
		      for(int j=0; j < targeters.length; j++){
		         targeters[j].updateTarget(targets[i], null);}
				 }
					//e.printStackTrace();
				}
			}
			else if(isConst)
			{
				Number value = null;
				if(handle.getInstruction() instanceof ICONST){
					value = (Number)(((ICONST)handle.getInstruction()).getValue());
				}
				else if(handle.getInstruction() instanceof FCONST){
					value = (Number)(((FCONST)handle.getInstruction()).getValue());
				}
				else if(handle.getInstruction() instanceof LCONST){
					value = (Number)(((LCONST)handle.getInstruction()).getValue());
				}
				else if(handle.getInstruction() instanceof DCONST){
					value = (Number)(((DCONST)handle.getInstruction()).getValue());
				}
				constantStack.push(value);
			}
			else if(isArithmeticInst)
			{
					performeArithmetic(handle);
					try
					{
						instList.delete(handle);
					}
					catch (TargetLostException e)
		      {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		      }
/*
					Number topOfStack = constantStack.pop();

					if(topOfStack instanceof Double)
					{
						instList.insert(handle, new LDC2_W(cpgen.addDouble((Double)topOfStack)));
					}
					else if(topOfStack instanceof Long)
					{
						instList.insert(handle, new LDC2_W(cpgen.addLong((Long)topOfStack)));
					}
					else if (topOfStack instanceof Integer)
					{
						instList.insert(handle, new LDC(cpgen.addInteger((Integer)topOfStack)));
					}
					else if (topOfStack instanceof Float)
					{
						instList.insert(handle, new LDC(cpgen.addFloat((Float)topOfStack)));
					}*/

				//	constantStack.push(topOfStack);

			}
			else if(isStore)
			{
				Number value = constantStack.pop();
				int index = ((StoreInstruction)handle.getInstruction()).getIndex();
				try{
				variables.add(index-offset, value);
			}
				catch (Exception e)
				{
					variables.add(value);
					offset = index;
				}
				try
				{
					instList.delete(handle);
				}
				catch (TargetLostException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(isLoad)
			{
				int index = ((LoadInstruction)handle.getInstruction()).getIndex();
				try{
				Number value = variables.get(index-offset);
				constantStack.push(value);
			}
			catch(Exception e)
			{

			}
			try
			{
				instList.delete(handle);
			}
			catch (TargetLostException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}





			//
			//
      // if(h1 != null && h2 != null)
      // {
      //   if(handle.getInstruction() instanceof  IADD &&
      //       h1.getInstruction() instanceof LDC &&
      //       h2.getInstruction() instanceof LDC )
      //   {
      //     //int value = ((Integer)(((LDC)h1.getInstruction()).getValue(cpgen))+ (Integer)(((LDC)h2.getInstruction()).getValue(cpgen)));
			// 		Number value = getConstantValue(handle, cpgen);
      //     instList.insert(handle, new LDC(cpgen.addInteger(value)));
      //     try
      //     {
      //       // delete the old one
      //       instList.delete(handle);
      //       instList.delete(h1);
      //       instList.delete(h2);
      //     }
      //     catch (TargetLostException e)
      //     {
      //       // TODO Auto-generated catch block
      //       e.printStackTrace();
      //     }
      //   }
      // }
      h2 = h1;
      h1 = handle;
			System.out.println("STACK:" + constantStack);
    }
		// setPositions(true) checks whether jump handles
		// are all within the current method
		System.out.println("Before");
		//instList.setPositions(true);
		System.out.println("AFTER");
		System.out.println("Optimised method instructions: " + method.getName());
		for(InstructionHandle handle : instList.getInstructionHandles())
    {
			System.out.println(handle.toString());
		}

		// set max stack/local
		methodGen.setMaxStack();
		methodGen.setMaxLocals();

		// generate the new method with replaced iconst
		Method newMethod = methodGen.getMethod();
		// replace the method in the original class
		cgen.replaceMethod(method, newMethod);
  }

  public Number getConstantValue(InstructionHandle handle, ConstantPoolGen cpgen)
  {
    Instruction instruction = handle.getInstruction();
    if((instruction instanceof LDC) || (instruction instanceof LDC_W))
    {
      return (Number)(((LDC)handle.getInstruction()).getValue(cpgen));
    }
    else if(instruction instanceof LDC2_W)
    {
      return (Number)(((LDC2_W)handle.getInstruction()).getValue(cpgen));
    }
		else if(instruction instanceof BIPUSH)
		{
			return (Number)(((BIPUSH)handle.getInstruction()).getValue());
		}
		else if(instruction instanceof SIPUSH)
		{
			return (Number)(((SIPUSH)handle.getInstruction()).getValue());
		}
    return null;
  }

	public void optimize()
	{
		ClassGen cgen = new ClassGen(original);
		cgen.setMajor(50);
		ConstantPoolGen cpgen = cgen.getConstantPool();

		// Implement your optimization here

    Method[] methods = cgen.getMethods();

    for(Method m : methods)
    {
      optimizeMethod(cgen, cpgen, m);
    }
		this.optimized = cgen.getJavaClass();
	}


	public void write(String optimisedFilePath)
	{
		this.optimize();

		try {
			FileOutputStream out = new FileOutputStream(new File(optimisedFilePath));
			this.optimized.dump(out);
		} catch (FileNotFoundException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}
}
