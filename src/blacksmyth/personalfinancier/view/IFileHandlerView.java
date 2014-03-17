package blacksmyth.personalfinancier.view;

public interface IFileHandlerView {

  public void setPromptDirectory(String directory);

  public boolean promptForSaveFilename();
  public boolean promptForLoadFilename();
  
  public String getFilename();

}
