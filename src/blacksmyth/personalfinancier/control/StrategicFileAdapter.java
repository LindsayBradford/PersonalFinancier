package blacksmyth.personalfinancier.control;

import java.util.HashMap;

import blacksmyth.general.file.IObjectFileConverter;

public class StrategicFileAdapter<T> implements IObjectFileConverter<T> {
  
  private HashMap<String, IObjectFileConverter<T>> adapterMap = new HashMap<String, IObjectFileConverter<T>>();

  @Override
  public T toObjectFromFile(String filePath) {
    IObjectFileConverter<T> adapter = getMatchingAdapter(filePath);
    if (adapter != null) {
      return adapter.toObjectFromFile(filePath);
    }
    return null;
  }

  @Override
  public void toFileFromObject(String filePath, T t) {
    IObjectFileConverter<T> adapter = getMatchingAdapter(filePath);
    if (adapter != null) {
      adapter.toFileFromObject(filePath, t);
    }
  }
  
  public void add(String fileExtension, IObjectFileConverter<T> adapter) {
    adapterMap.put(fileExtension, adapter);
  }
  
  private String getFileExtension(String filePath) {
    int i = filePath.lastIndexOf('.');
    if (i >= 0) {
        return filePath.substring(i+1);
    }
    return null;
  }
  
  private IObjectFileConverter<T> getMatchingAdapter(String filePath) {
    String fileExt = getFileExtension(filePath);
    
    IObjectFileConverter<T> adapter = adapterMap.get(fileExt);
    
    return adapter;
  }

}
