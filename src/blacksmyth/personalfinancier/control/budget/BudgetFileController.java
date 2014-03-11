package blacksmyth.personalfinancier.control.budget;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import blacksmyth.general.FileUtilities;
import blacksmyth.general.JSonAdapter;
import blacksmyth.personalfinancier.model.PreferencesModel;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.BudgetModelData;
  

public class BudgetFileController implements Observer, IBudgetController, IBudgetObserver {
  private static String currentFileName;
  private static final JFileChooser fileChooser = new JFileChooser();
  
  private BudgetModel model;

  public BudgetFileController (BudgetModel model) {
    setModel(model);
    configureFileChooser();
  }
  
  private void configureFileChooser() {
    FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Files", "json");
    fileChooser.setFileFilter(filter);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
  }
  
  public BudgetModel getBudgetModel() {
    return this.model;
  }

  public void setModel(BudgetModel model) {
    model.addObserver(this);
    this.model = model;
  }

  public void save(JFrame parentFrame) {
    if (currentFileName == null) {
      if (promptForFilename(parentFrame)) {
        save(currentFileName);
        
        PreferencesModel.getInstance().setLastUsedBudgetFilePath(
            fileChooser.getSelectedFile().getPath()
        );
      }
    } else {
      save(currentFileName);
      
      PreferencesModel.getInstance().setLastUsedBudgetFilePath(
          fileChooser.getSelectedFile().getPath()
      );
    }
  }

  public void saveAs(JFrame parentFrame) {

    if (promptForFilename(parentFrame)) {
      save(currentFileName);
      
      PreferencesModel.getInstance().setLastUsedBudgetFilePath(
          fileChooser.getSelectedFile().getPath()
      );
    }
  }

  private void save(String fileName) {
    FileUtilities.saveTextFile(
        fileName, 
        JSonAdapter.getInstance().toJSonFromObject(
            this.getBudgetModel().getState()
        )
    );
  }

  private boolean promptForFilename(JFrame parentFrame) {
    fileChooser.setCurrentDirectory(
        new File(
            PreferencesModel.getInstance().getLastUsedBudgetFilePath()
        )    
    );
      
    int response = fileChooser.showSaveDialog(parentFrame);

    if (response == JFileChooser.APPROVE_OPTION) {
      setCurrentFileName(
          parentFrame,
          fileChooser.getSelectedFile().getAbsolutePath()
      );
      return true;
    }
    return false;
  }
  
  
  public void load(JFrame parentFrame) {
    fileChooser.setCurrentDirectory(
        new File(
            PreferencesModel.getInstance().getLastUsedBudgetFilePath()
        )    
    );

    int response = fileChooser.showOpenDialog(parentFrame);

    if (response == JFileChooser.APPROVE_OPTION) {
      load(fileChooser.getSelectedFile());
      
      PreferencesModel.getInstance().setLastUsedBudgetFilePath(
          fileChooser.getSelectedFile().getPath()
      );

      setCurrentFileName(
          parentFrame,
          fileChooser.getSelectedFile().getAbsolutePath()
      );
    }
  }
  
  private void setCurrentFileName(JFrame frame, String fileName) {
    currentFileName = fileName;
    frame.setTitle(fileName);
  }
  
  public void load(File file) {
    this.getBudgetModel().setState(
        (BudgetModelData) this.loadJSon(file)
    );
  }

  private Object loadJSon(File file) {
    return JSonAdapter.getInstance().toObjectFromJSon(
        FileUtilities.loadTextFile(file)
    );
  }
  
  public void update(Observable o, Object arg) {}
}
