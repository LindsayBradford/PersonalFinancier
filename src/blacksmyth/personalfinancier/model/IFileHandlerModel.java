package blacksmyth.personalfinancier.model;

public interface IFileHandlerModel<T> {
  public void fromSerializable(T t);
  
  public T toSerializable();
}
