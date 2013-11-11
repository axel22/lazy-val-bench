package example;
import java.lang.invoke.*;


public final class LazyValsHm extends SwitchPoint{
 
    public LazyValsHm(int value) {}
    private int value__;

    private final MethodHandle accessor = guardWithTest(initter, getter);;
    public int value() throws Throwable {return (int) accessor.invoke(this);}

    static private final MethodHandles.Lookup lookup = MethodHandles.lookup();
    static private final MethodType getterMT = MethodType.methodType(int.class, LazyValsHm.class);
    static private final MethodHandle initter = getInitter();
    static private final MethodHandle getter = getGetter();

    static private MethodHandle getGetter() {
	try {
	    return lookup.findStatic(LazyValsHm.class, "_get", getterMT);
	}
	catch (IllegalAccessException|NoSuchMethodException e) {}
	return null;
    }

    static private MethodHandle getInitter() {
	try {
	    return lookup.findStatic(LazyValsHm.class, "_init", getterMT);
	}
	catch (IllegalAccessException|NoSuchMethodException e) {}
	return null;
    }
    
    static private final int _get(LazyValsHm who) {
	return who.value__;
    }
    


    static private final int _init(LazyValsHm who) throws Throwable {
/*          synchronized(who.accessor) {
	    if(!who.hasBeenInvalidated())
	    {*/
		    who.value__=0;
		    SwitchPoint.invalidateAll(new SwitchPoint[]{ who });
/*		    accessor.notifyAll();
		    		}
	    else {
	         who.accessor.wait();
	    }
	    }*/
	return who.value__;
    }

}
