package blacksmyth.general;

public interface IPersonalFinancierFileAdapter<T> {
  void toFileFromObject(String filePath, T t); 
  public T toObjectFromFile(String filePath);
}
