package blacksmyth.personalfinancier.control.inflation;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import blacksmyth.general.FileUtilities;
import blacksmyth.general.JSonAdapter;
import blacksmyth.personalfinancier.model.PreferencesModel;
import blacksmyth.personalfinancier.model.inflation.InflationModel;
  

public class InflationFileController implements Observer, IInflationController, IInfllationObserver {
  private static String currentFileName;
  private static final JFileChooser fileChooser = new JFileChooser();
  
  private InflationModel model;

  public InflationFileController (InflationModel model) {
    setModel(model);
    configureFileChooser();
  }
  
  private void configureFileChooser() {
    FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Files", "json");
    fileChooser.setFileFilter(filter);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
  }
  
  public InflationModel getInflationModel() {
    return this.model;
  }

  public void setModel(InflationModel model) {
    model.addObserver(this);
    this.model = model;
  }

  public void save(JFrame parentFrame) {
    if (currentFileName == null) {
      if (promptForFilename(parentFrame)) {
        save(currentFileName);
        
        PreferencesModel.getInstance().setLastUsedInflationFilePath(
            fileChooser.getSelectedFile().getPath()
        );
      }
    } else {
      save(currentFileName);

      PreferencesModel.getInstance().setLastUsedInflationFilePath(
          fileChooser.getSelectedFile().getPath()
      );
    }
  }

  public void saveAs(JFrame parentFrame) {

    if (promptForFilename(parentFrame)) {
      save(currentFileName);
      
      PreferencesModel.getInstance().setLastUsedInflationFilePath(
          fileChooser.getSelectedFile().getPath()
      );
    }
  }

  private void save(String fileName) {
    FileUtilities.saveTextFile(
        fileName, 
        JSonAdapter.getInstance().toJSonFromObject(
            this.getInflationModel().getState()
        )
    );
  }

  private boolean promptForFilename(JFrame parentFrame) {
    fileChooser.setCurrentDirectory(
        new File(
            PreferencesModel.getInstance().getLastUsedInflationFilePath()
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
            PreferencesModel.getInstance().getLastUsedInflationFilePath()
        )    
    );

    int response = fileChooser.showOpenDialog(parentFrame);

    if (response == JFileChooser.APPROVE_OPTION) {
      load(fileChooser.getSelectedFile());
      
      PreferencesModel.getInstance().setLastUsedInflationFilePath(
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
  }
  
  public void load(File file) {
    this.getInflationModel().setState(
        (InflationModel.SerializableState) this.loadJSon(file)
    );
  }

  private Object loadJSon(File file) {
    return JSonAdapter.getInstance().toObjectFromJSon(
        FileUtilities.loadTextFile(file)
    );
  }
  
  public void update(Observable o, Object arg) {}
}
