package blacksmyth.general;

import java.io.IOException;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;

/**
 * An adapter through to a 3rd-party JSON Serialization library.
 * All access to JSson functionality should go through this adapter.
 * @author linds
 */
public class JSonFileAdapter<T> implements IPersonalFinancierFileAdapter<T> {
  
  @Override
  public void toFileFromObject(String filePath, T t) {
    try {
      FileUtilities.saveTextFile(
          filePath, 
          JsonWriter.formatJson(
              JsonWriter.objectToJson(t)
          )
      );

    } catch (IOException e) {
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public T toObjectFromFile(String filePath) {
    try {
      return (T) JsonReader.jsonToJava(
          FileUtilities.loadTextFile(filePath)
      );
    } catch (IOException e) {
      return null;
    }
  }
}
