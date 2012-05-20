package blacksmyth.general;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

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
   * Supplies a JSON string encoding of the supplied <Object>.
   * @param object
   * @return
   */
  public String toJSonFromObject(Object object) {
    return gsonHandle.toJson(object);
  }

  /**
   * Returns an instance of type <tt>objectType</tt> from
   * its JSon serialiation in <tt>jsonData</tt>.
   * @param jsonData
   * @param objectType
   * @return
   * @throws JsonSyntaxException
   */
  public Object toObjectFromJSon(String jsonData, Type objectType) throws JsonSyntaxException {
    return gsonHandle.fromJson(
        jsonData.toString(), 
        objectType
    );
  }
}
