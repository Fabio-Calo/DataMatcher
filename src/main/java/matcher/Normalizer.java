package matcher;

import java.util.HashMap;
import java.util.Map;

public class Normalizer {

  private Map<String,String> changes = new HashMap<>();

  /***
   * Save your changes in the order of execution
   * @param changes
   */
  public Normalizer(Map<String, String> changes){

    this.changes = changes;
  }


  public String normalize(String string){

    for (var change : changes.entrySet()){
     string = string.replaceAll(change.getKey(),change.getValue());
    }
    return string.toUpperCase();
  }
}
