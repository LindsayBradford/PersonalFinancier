package blacksmyth.personalfinancier.io.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import blacksmyth.personalfinancier.model.budget.BudgetModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GSonAdapter {
  private Gson gsonHandle;
  private static GSonAdapter instance;
  
  public static GSonAdapter getInstance() {
    if (instance == null) {
      instance = new GSonAdapter();
    }
    return instance;
  }
  
  protected GSonAdapter() {
    gsonHandle = new GsonBuilder()
                      .setPrettyPrinting()
                      .registerTypeAdapter(
                         BudgetModel.class, 
                         new BudgetModelSerializer()
                       )
                       .create();
  }
  
  public void save(Object object, String fileName) {
    String objectAsJSonString = gsonHandle.toJson(object);

    File fileHandle = new File(fileName);

    BufferedWriter output = null;

    try {
      output = new BufferedWriter(
          new FileWriter(fileHandle)
      );
      output.write(objectAsJSonString);
      output.close();
    } catch (IOException ioe) {
      System.out.println(ioe);
    }
    
    System.out.println(fileHandle.getAbsolutePath() + " saved.");
  }
  
  public String toJSon(Object object) {
    return gsonHandle.toJson(object);
  }

  public Object load(String fileName, Type typeToExpect) {
    File fileHandle = new File(fileName);
    
    BufferedReader input = null;

    StringBuffer jsonData = new StringBuffer();
    String currentLine;
    try {
      input = new BufferedReader(
          new FileReader(fileHandle)
      );
      
      while ((currentLine = input.readLine()) != null) {
        jsonData.append(currentLine);
      }
      input.close();
    } catch (IOException ioe) {
      System.out.println(ioe);
    }
    
    System.out.println(fileHandle.getAbsolutePath() + " loaded.");

    
    return gsonHandle.fromJson(
        jsonData.toString(), 
        typeToExpect
    );
  }
}
