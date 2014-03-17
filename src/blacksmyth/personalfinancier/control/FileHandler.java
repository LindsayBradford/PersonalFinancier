package blacksmyth.personalfinancier.control;

import java.io.File;

import blacksmyth.general.FileUtilities;
import blacksmyth.general.JSonAdapter;
import blacksmyth.personalfinancier.model.IFileHandlerModel;
import blacksmyth.personalfinancier.model.PreferencesModel;
import blacksmyth.personalfinancier.view.IFileHandlerView;

public class FileHandler<T> {
  
  private IFileHandlerModel<T> model;
  private IFileHandlerView     view;

  public FileHandler (IFileHandlerModel<T> model, IFileHandlerView view) {
    setModel(model);
    setView(view);
  }
  
  private void setView(IFileHandlerView view) {
    this.view = view;
  }

  public IFileHandlerModel<T> gettModel() {
    return this.model;
  }

  public void setModel(IFileHandlerModel<T> model) {
    this.model = model;
  }

  public void save() {
    if (view.getFilename() == null) {
      if (view.promptForSaveFilename()) {
        save(view.getFilename());
        
        PreferencesModel.getInstance().setLastUsedBudgetFilePath(
            view.getFilename()
        );
      }
    } else {
      save(view.getFilename());
      
      PreferencesModel.getInstance().setLastUsedBudgetFilePath(
          view.getFilename()
      );
    }
  }

  public void saveAs() {
    if (view.promptForSaveFilename()) {
      save(view.getFilename());
      
      PreferencesModel.getInstance().setLastUsedBudgetFilePath(
          view.getFilename()
      );
    }
  }

  private void save(String fileName) {
    FileUtilities.saveTextFile(
        fileName, 
        JSonAdapter.getInstance().toJSonFromObject(
            this.gettModel().toSerializable()
        )
    );
  }
  
  public void load() {
    view.setPromptDirectory(
        PreferencesModel.getInstance().getLastUsedBudgetFilePath()
    );

    if (view.promptForLoadFilename()) {
      load(view.getFilename());
      
      PreferencesModel.getInstance().setLastUsedBudgetFilePath(
          view.getFilename()
      );
    }
  }
  
  public void load(String filename) {
    this.gettModel().fromSerializable(
        this.loadJSon(
            new File(filename)
        )
    );
  }

  @SuppressWarnings("unchecked")
  private T loadJSon(File file) {
    return (T) JSonAdapter.getInstance().toObjectFromJSon(
        FileUtilities.loadTextFile(file)
    );
  }
}
