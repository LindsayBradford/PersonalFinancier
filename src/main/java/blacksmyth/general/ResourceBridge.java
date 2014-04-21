/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.general;

import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;

/**
 * This class acts as a bridge through to the application's resource bundle. 
 * Freeing the developer from having to know anything more than the
 * filename and type of resource they want to retrieve.
 * 
 * This is an implementation of the bridge pattern.
 */

public class ResourceBridge {

  private static final int MENU_ICON_BUFFER_SIZE = 1024;  // 1k

  private static final String RESOURCE_PATH = "/resources";
  private static final String MENU_ICON_PATH = RESOURCE_PATH + "/menuIcons";
  private static final String FONT_PATH = RESOURCE_PATH + "/fonts";
  
  /**
   * Supplies an {@link ImageIcon} from the set of menu icons available from 
   * the application's resource bundle.
   * @param iconImageFileName The filename of the file within this resource bundle.
   * @return {@link ImageIcon}
   */
   public static ImageIcon getMenuIcon(String iconImageFileName) {
     String iconPath = MENU_ICON_PATH + "/" + iconImageFileName;
     byte[] iconByteArray;

     try {
       InputStream in = ResourceBridge.class.getResourceAsStream(iconPath);
       iconByteArray = toByteArray(in, MENU_ICON_BUFFER_SIZE);
       in.close();
     } catch (Exception e) {
       return null;
     }

     return new ImageIcon(iconByteArray);
   }

   /**
    * Converts the supplied <tt>InputStream<tt> to a byte array.
    * @param is
    * @param bufferSize
    * @return
    * @throws IOException
    */
   private static byte[] toByteArray(final InputStream is, final int bufferSize) throws IOException {

     BufferedInputStream inStream = new BufferedInputStream(is);
     ByteArrayOutputStream outStream = new ByteArrayOutputStream(bufferSize);
     
     final byte[] byteBuffer = new byte[bufferSize];

     int posn = 0;
     while ((posn = inStream.read(byteBuffer, 0, bufferSize)) > 0) {
         outStream.write(
             byteBuffer, 
             0, 
             posn
         );
     }
     
     outStream.flush();
     return outStream.toByteArray();
 }
   
   /**
    * Supplies a <Font> from the set of true-type fonts available from the 
    * application's resource bundle.
    * @param fontFilename The filename of the font within this resource bundle
    * @return
    */
   public static Font getFont(String fontFilename) {
     String fontPath = FONT_PATH + "/" + fontFilename;

     try {
       InputStream inStream = ResourceBridge.class.getResourceAsStream(fontPath);
       return Font.createFont(Font.TRUETYPE_FONT, inStream);
     } catch (Exception e) {
       e.printStackTrace();
       return null;
     }
   }
}
