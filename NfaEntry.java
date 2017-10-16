import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class NfaEntry {
   private final SimpleStringProperty sourceName;
   private final SimpleStringProperty destName;
   private final SimpleIntegerProperty weightName;
   
   public NfaEntry(String sName, Integer wName, String dName){
      this.sourceName = new SimpleStringProperty(sName);
      this.weightName = new SimpleIntegerProperty(wName);
      this.destName = new SimpleStringProperty(dName);
   }
   //Getter methods
   public String getSourceName(){
      return sourceName.get();
   }
   
   public String getDestName(){
      return destName.get();
   }
   
   public Integer getWeightName(){
      return weightName.get();
   }
   
   //Setter methods
   public void setSourceName(String sName){
      sourceName.set(sName);
   }
   public void setDestName(String dName){
      destName.set(dName);
   }
   public void setWeightName(Integer wName){
      weightName.set(wName);
   }
}