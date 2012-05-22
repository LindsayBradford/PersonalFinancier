package blacksmyth.personalfinancier.control.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JToggleButton;

import blacksmyth.general.FontIconProvider;

public class JToggleEyeButton extends JToggleButton {
  public JToggleEyeButton() {
    super();
    init();
  }
  
  public JToggleEyeButton(Action toggleAction) {
    super(toggleAction);
    init();
  }
  
  private void init() {
    this.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            setIconToSelected();
          }
        }
    );
    
    this.setSelected(true);
    setIconToSelected();
  }
  
  private void setIconToSelected() {
    if (this.isSelected()) {
      FontIconProvider.getInstance().setGlyphAsText(
          this, 
          FontIconProvider.icon_eye_open
      );
    } else {
      FontIconProvider.getInstance().setGlyphAsText(
          this, 
          FontIconProvider.icon_eye_close
      );
    }
  }
}
