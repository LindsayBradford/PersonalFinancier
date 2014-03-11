package blacksmyth.general;

import java.io.IOException;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;

/**
 * An adapter through to a 3rd-party JSON Serialization library.
 * All access to JSson functionality should go through this adapter.
 * @author linds
 */
public class JSonAdapter {
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
  }

  /**
   * Supplies a JSON string encoding of the supplied <Object>.
   * @param object
   * @return
   */
  public String toJSonFromObject(Object object) {
    try {
      return JsonWriter.formatJson(
          JsonWriter.objectToJson(object)
      );
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Returns an instance of type <tt>objectType</tt> from
   * its JSon serialiation in <tt>jsonData</tt>.
   * @param jsonData
   * @param objectType
   * @return
   * @throws JsonSyntaxException
   */
  public Object toObjectFromJSon(String jsonData) {
    try {
      return JsonReader.jsonToJava(jsonData);
    } catch (IOException e) {
      return null;
    }
  }
}
