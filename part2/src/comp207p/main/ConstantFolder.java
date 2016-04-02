package comp207p.main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

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
import org.apache.bcel.generic.IADD;


public class ConstantFolder
{
	ClassParser parser = null;
	ClassGen gen = null;

	JavaClass original = null;
	JavaClass optimized = null;

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
		for(InstructionHandle handle : instList.getInstructionHandles())
    {
      if(handle.getInstruction() instanceof LDC){
        System.out.println(((LDC)handle.getInstruction()).getValue(cpgen));
      } 
      if(h1 != null && h2 != null)
      {
        if(handle.getInstruction() instanceof  IADD &&
            h1.getInstruction() instanceof LDC &&
            h2.getInstruction() instanceof LDC )
        {
          int value = ((Integer)(((LDC)h1.getInstruction()).getValue(cpgen))+ (Integer)(((LDC)h2.getInstruction()).getValue(cpgen)));
          
          instList.insert(handle, new LDC(cpgen.addInteger(value)));
          System.out.println("here");
          try
          {
            // delete the old one
            instList.delete(handle);
            instList.delete(h1);
            instList.delete(h2);  
            System.out.println("here too");
          }
          catch (TargetLostException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
      h2 = h1;
      h1 = handle;
    }
		// setPositions(true) checks whether jump handles 
		// are all within the current method
		instList.setPositions(true);

		// set max stack/local
		methodGen.setMaxStack();
		methodGen.setMaxLocals();

		// generate the new method with replaced iconst
		Method newMethod = methodGen.getMethod();
		// replace the method in the original class
		cgen.replaceMethod(method, newMethod);
    System.out.println("here finally");
  }
	
	public void optimize()
	{
		ClassGen cgen = new ClassGen(original);
		ConstantPoolGen cpgen = cgen.getConstantPool();

		// Implement your optimization here
        
    Method[] methods = cgen.getMethods();

    for(Method m : methods)
    {
      optimizeMethod(cgen, cpgen, m);
    }
		this.optimized = gen.getJavaClass();
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
