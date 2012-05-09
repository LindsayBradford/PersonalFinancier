package blacksmyth.general;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * An adapter through to a 3rd-party JSON Serialization library.
 * All access to JSson functionality should go through this adapter.
 * @author linds
 */
public class JSonAdapter {
  private Gson gsonHandle;
  private static JSonAdapter instance;
  
  /**
   * Provide the single instance of this class available to interface 
   * with a JSon serialization library. 
   * @return
   */
  public static JSonAdapter getInstance() {
    if (instance == null) {
      instance = new JSonAdapter();
    }
    return instance;
  }
  
  protected JSonAdapter() {
    gsonHandle = new GsonBuilder()
                      .setPrettyPrinting()
                      .create();
  }

  /**
   * Saves a JSon serialisation encoding of the supplied <tt>object</tt> 
   * to the file specified by <tt>fileName</tt>
   * @param object
   * @param fileName
   */
  public void save(Object object, String fileName) {
    String objectAsJSonString = gsonHandle.toJson(object);

    File fileHandle = new File(fileName);

    BufferedWriter output = null;

    try {
      output = new BufferedWriter(
          new FileWriter(fileHandle)
      );
      output.write(objectAsJSonString);
      output.close();
    } catch (IOException ioe) {
      System.out.println(ioe);
    }
    
    System.out.println(fileHandle.getAbsolutePath() + " saved.");
  }

  /**
   * Supplies a JSON string encoding of the supplied <Object>.
   * @param object
   * @return
   */
  public String toJSon(Object object) {
    return gsonHandle.toJson(object);
  }

  /**
   * Loads a JSON encoding of an object from the file identified
   * in <tt>fileName</tt> into a new instance of an object identified
   * by <tt>typeToExtract</tt>
   * @param fileName
   * @param typeToExpect
   * @return
   */
  public Object load(String fileName, Type typeToExpect) {
    File fileHandle = new File(fileName);
    
    BufferedReader input = null;

    StringBuffer jsonData = new StringBuffer();
    String currentLine;
    try {
      input = new BufferedReader(
          new FileReader(fileHandle)
      );
      
      while ((currentLine = input.readLine()) != null) {
        jsonData.append(currentLine);
      }
      input.close();
    } catch (IOException ioe) {
      System.out.println(ioe);
    }
    
    System.out.println(fileHandle.getAbsolutePath() + " loaded.");
    
    return gsonHandle.fromJson(
        jsonData.toString(), 
        typeToExpect
    );
  }
}
