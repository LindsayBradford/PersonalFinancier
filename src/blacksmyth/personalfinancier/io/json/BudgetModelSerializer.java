package blacksmyth.personalfinancier.io.json;

import java.lang.reflect.Type;

import blacksmyth.personalfinancier.model.budget.BudgetModel;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class BudgetModelSerializer implements JsonSerializer<BudgetModel> {
  
  public JsonElement serialize(BudgetModel src, Type typeOfSrc, JsonSerializationContext context) {

    JsonObject obj = new JsonObject();
    
    obj.add(
        "budgetItems", 
        context.serialize(src.getExpsnesItems())
    );

    return obj;
  }
}