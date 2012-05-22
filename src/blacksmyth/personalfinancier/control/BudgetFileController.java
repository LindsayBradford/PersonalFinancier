package blacksmyth.personalfinancier.control;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.gson.reflect.TypeToken;

import blacksmyth.general.FileUtilities;
import blacksmyth.general.JSonAdapter;
import blacksmyth.personalfinancier.model.PreferencesModel;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
  

public class BudgetFileController implements Observer, IBudgetController, IBudgetObserver {
  private static String currentFileName;
  private static final JFileChooser fileChooser = new JFileChooser();
  
  private BudgetModel model;

  public BudgetFileController (BudgetModel model) {
    setModel(model);
    configureFileChooser();
  }
  
  private void configureFileChooser() {
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "JSON Files", "json");
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
      
      fileChooser.setCurrentDirectory(
          new File(
              PreferencesModel.getInstance().getLastUsedFilePath()
          )    
      );
      
      int response = fileChooser.showSaveDialog(parentFrame);

      if (response == JFileChooser.APPROVE_OPTION) {
        setCurrentFileName(
            parentFrame,
            fileChooser.getSelectedFile().getAbsolutePath()
        );
      }
    }

    save(currentFileName);
    
    PreferencesModel.getInstance().setLastUsedFilePath(
        fileChooser.getSelectedFile().getPath()
    );

  }
  
  private void save(String fileName) {
    FileUtilities.saveTextFile(
        fileName, 
        JSonAdapter.getInstance().toJSonFromObject(
            this.getBudgetModel().getState()
        )
    );
  }
  
  public void load(JFrame parentFrame) {
    fileChooser.setCurrentDirectory(
        new File(
            PreferencesModel.getInstance().getLastUsedFilePath()
        )    
    );

    int response = fileChooser.showOpenDialog(parentFrame);

    if (response == JFileChooser.APPROVE_OPTION) {
      load(fileChooser.getSelectedFile());
      
      PreferencesModel.getInstance().setLastUsedFilePath(
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
    Type budgetItemsType = new TypeToken<BudgetModel.SerializableState>() {}.getType();
    this.getBudgetModel().setState(
        (BudgetModel.SerializableState) this.loadJSon(file, budgetItemsType)
    );
  }

  private Object loadJSon(File file, Type typeToExpect) {
    return JSonAdapter.getInstance().toObjectFromJSon(
        FileUtilities.loadTextFile(file), 
        typeToExpect
    );
  }
  
  public void update(Observable o, Object arg) {}
}
