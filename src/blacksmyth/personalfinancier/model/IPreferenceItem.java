package blacksmyth.personalfinancier.model;

public interface IPreferenceItem<T> {
  void setPreference(T t);
  T getPreference();
}
