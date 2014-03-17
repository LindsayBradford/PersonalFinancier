package blacksmyth.personalfinancier.view;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileHandlerView implements IFileHandlerView {

  private static final JFileChooser fileChooser = new JFileChooser();
  private String currentFileName;
  
  private JFrame parentFrame;
  
  public FileHandlerView(JFrame parentFrame, String description, String... extensions) {
    this.parentFrame = parentFrame;
    configureFileChooser(description, extensions);
  }

  private void configureFileChooser(String description, String... extensions) {
    FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extensions);
    fileChooser.setFileFilter(filter);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
  }
  
  @Override
  public boolean promptForSaveFilename() {
      
    int response = fileChooser.showSaveDialog(parentFrame);

    if (response == JFileChooser.APPROVE_OPTION) {
      setCurrentFileName(
          fileChooser.getSelectedFile().getAbsolutePath()
      );
      return true;
    }
    return false;
  }

  private void setCurrentFileName(String filename) {
    this.currentFileName = filename;
    parentFrame.setTitle(filename);
  }

  @Override
  public String getFilename() {
    return this.currentFileName;
  }

  @Override
  public void setPromptDirectory(String directory) {
    fileChooser.setCurrentDirectory(
        new File(
            directory
        )    
    );
  }

  @Override
  public boolean promptForLoadFilename() {
    int response = fileChooser.showOpenDialog(parentFrame);
    if (response == JFileChooser.APPROVE_OPTION) {
      setCurrentFileName(
          fileChooser.getSelectedFile().getAbsolutePath()
      );
      return true;
    }
    return false;
  }
}
