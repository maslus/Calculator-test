package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Controller {
    
    @FXML
    private Text   output;
    private double num1 = 0;
    
    private boolean start = true;
    
    private String operator = "";
    private Model  model    = new Model();
    
    private String toDataBase = "";
    
    @FXML
    private void processNum (ActionEvent event) {
        if (start) {
            output.setText("");
            start = false;
        }
        String value = ((Button) event.getSource()).getText();
        output.setText(output.getText() + value);
        toDataBase = toDataBase + output.getText();
    }
    
    @FXML
    private void processOperator (ActionEvent event) {
        String value = ((Button) event.getSource()).getText();
        if (("+").equals(value) || ("-").equals(value) || ("*").equals(value) || ("/").equals(value)) {
            if (!operator.isEmpty()) return;
            operator = value;
            num1 = Double.parseDouble(output.getText());
            toDataBase = output.getText() + " " + operator + " ";
            output.setText("");
        } else if (("=").equals(value)) {
            if (operator.isEmpty()) return;
    
            DecimalFormat df = new DecimalFormat("#.#########");
            df.setRoundingMode(RoundingMode.HALF_EVEN);
            
            output.setText(df.format(model.calculation(num1, Double.parseDouble(output.getText()), operator)));
            operator = "";
            toDataBase = toDataBase + " = " + output.getText();
            DataBaseHandler dataBaseHandler = new DataBaseHandler();
            dataBaseHandler.calcWrite(toDataBase);
            start = true;
            
        } else if (("C").equals(value)) {
            output.setText("");
            operator = "";
            start = true;
        } else if (("<--").equals(value)) {
            if (output.getText().length() > 0) {
                StringBuilder back = new StringBuilder(output.getText());
                back.deleteCharAt(output.getText().length() - 1);
                output.setText(back.toString());
                StringBuilder backToDataBase = new StringBuilder(toDataBase);
                toDataBase = backToDataBase.deleteCharAt(toDataBase.length() - 1).toString();
                
            }
        }
    }
}
