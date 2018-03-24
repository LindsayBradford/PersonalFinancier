package blacksmyth.general.file;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NullObjectFileConverter<T> implements IObjectFileConverter<T> {
  
  private static final Logger LOG = LogManager.getLogger(NullObjectFileConverter.class);

  @Override
  public void toFileFromObject(String filePath, T t) {
    LOG.warn("No converter found to process file [{}]. ", filePath);
}

  @Override
  public T toObjectFromFile(String filePath) {
    LOG.warn("No converter found to process file [{}]. ", filePath);
    return null;
  }  
}
