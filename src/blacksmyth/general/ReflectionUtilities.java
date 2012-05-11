package blacksmyth.general;

import java.util.ArrayList;

/**
 * A collection of utilities that use reflection to determine useful things about the 
 * running code. Being a library, the class is not able to be instantiated.
 * @author linds
 *
 */
public final class ReflectionUtilities {
  
  /**
   * Checks to see if the method calling whatever method that invokes
   * <tt>callerImplements</tt> implements the supplied interface.
   * @param theInterface
   * @return
   */
  public static boolean callerImplements(Class<?> theInterface) {
    
    StackTraceElement[] elements = new Throwable().getStackTrace(); 

    String callerClassName = elements[2].getClassName(); 
    Class<?> callerClass = null;
    try {
      callerClass = Class.forName(callerClassName);
    } catch (Exception e) {
       return false;  
    }
    return classImplements(callerClass, theInterface);
  }
  
  /**
   * Returns the class of the method calling whatever method that invokes <tt>getCaller</tt>
   */
  public static Class<?> getCaller() {
    StackTraceElement[] elements = new Throwable().getStackTrace(); 

    String callerClassName = elements[2].getClassName(); 
    Class<?> callerClass = null;
    try {
      callerClass = Class.forName(callerClassName);
    } catch (Exception e) {
       return null;  
    }
    return callerClass;
  }
  
  /**
   * Checks to see if the supplied class implements the supplied interface.
   * @param theClass
   * @param theInterface
   * @return
   */
  public static boolean classImplements(Class<?> theClass, Class<?> theInterface) {
    ArrayList<Class<?>> interfaceArray = getInterfacesForClass(theClass);
    if (interfaceArray.contains(theInterface)) {
      return true;
    }
    return false;
  }

  /**
   * Recursively build and returns a list of all interfaces implemented by the 
   * supplied class and all its superclasses.
   * @param theClass
   * @return
   */
  private static ArrayList<Class<?>> getInterfacesForClass(Class<?> theClass) {
    
    if (theClass == null) {
      return null;
    }
    
    Class<?>[] interfaceArray = theClass.getInterfaces();
    Class<?> theSuperclass = theClass.getSuperclass();
    
    ArrayList<Class<?>> totalInterfaces = new ArrayList<Class<?>>();
    for (Class<?> currInterface : interfaceArray) {
      totalInterfaces.add(currInterface);
      totalInterfaces.addAll(getInterfacesForClass(currInterface));
    }
    ArrayList<Class<?>> superClassInterfaces = getInterfacesForClass(theSuperclass);
    if (superClassInterfaces != null) {
      totalInterfaces.addAll(superClassInterfaces);
    }
    
    return totalInterfaces;
  }
}
