package sample;

import javafx.beans.property.SimpleStringProperty;

public class ModelTable {
    //private final SimpleStringProperty calculation;
    
    String datatime;
    String calculation;
    
    public ModelTable (String datatime, String calculation) {
        this.datatime = datatime;
        this.calculation = calculation;
    }
    
    public ModelTable (String calculation) {
        this.calculation = new String(calculation);
    }
    
    
    public void setDatatime (String datatime) {
        this.datatime = datatime;
    }
    
    public void setCalculation (String calculation) {
        this.calculation = calculation;
    }
    
    public String getDatatime () {
        return datatime;
    }
    
    public String getCalculation () {
        return calculation;
    }
}

