/**
 * (c) 2013, Lindsay Bradford. licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 
 * Unported License.
 */

package blacksmyth.personalfinancier.view;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class DocumentFilterFactory {
  
  private static final BasicDocumentFilter DECIMAL_FILTER = new BasicDocumentFilter("0123456789.,");
  private static final BasicDocumentFilter DATE_FILTER = new BasicDocumentFilter("0123456789/");
  private static final BasicDocumentFilter PERCENT_FILTER = new BasicDocumentFilter("0123456789.,%");
  
  public static BasicDocumentFilter getDecimalFilter() {
    return DECIMAL_FILTER;
  }

  public static BasicDocumentFilter getDateFilter() {
    return DATE_FILTER;
  }

  public static BasicDocumentFilter getPercentFilter() {
    return PERCENT_FILTER;
  }
}

class BasicDocumentFilter extends DocumentFilter {
  private String validChars;
  
  public BasicDocumentFilter(String validCharList) {
    super();
    validChars = validCharList;
  }
  
  public void insertString(DocumentFilter.FilterBypass bypass, int offset, String string, AttributeSet attr) 
      throws BadLocationException {
    if (isValidText(string)) bypass.insertString(offset, string,attr);
  }
  
  public void replace(DocumentFilter.FilterBypass bypass, int offset, int length, String text, AttributeSet attributes) 
      throws BadLocationException {
    if (isValidText(text)) bypass.replace(offset,length,text,attributes);
  }

  protected boolean isValidText(String text) {
    for (int i = 0; i < text.length(); i++) {
      if (validChars.indexOf(text.charAt(i)) == -1) {
        return false;
      }
    }
    return true;
  }
}
