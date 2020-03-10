package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.math.RoundingMode;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    
    
    @FXML
    private Text   output;
    private double num1 = 0;
    
    private boolean start = true;
    
    private String operator = "";
    private Model  model    = new Model();
    
    private String toDataBase = "";
    
    @FXML
    private TableView<ModelTable> table;
    
//    @FXML
//    private TableColumn<ModelTable, String> column_datetime;
    
    @FXML
    private TableColumn<ModelTable, String> column_calc;
    
    @FXML
    private static ObservableList<ModelTable> data;
    
    @FXML
    public void loadDataFromDatabase () throws SQLException, ClassNotFoundException, NullPointerException {
        String fromSqlToTable = "SELECT * FROM history  ORDER BY datetime DESC LIMIT 0,20";

        data = FXCollections.observableArrayList();
        Connection connection = DataBaseHandler.getDbConnection();
        ResultSet  rs         = connection.createStatement().executeQuery(fromSqlToTable);
        while (rs.next()) {
            data.add(new ModelTable(rs.getString(2)));
        }
        column_calc.setCellValueFactory(new PropertyValueFactory<>("calculations"));  ////
        table.setItems(null);
        table.setItems(data);
    }
    
    @FXML
    private void processNum (ActionEvent event) {
        if (start) {
            output.setText("");
            start = false;
        }
        String value = ((Button) event.getSource()).getText();
        if (output.getText().equals(Const.ERROR)){
            output.setText(value);
        } else {
            output.setText(output.getText() + value);
            toDataBase = toDataBase + value;
        }
    }
    
    @FXML
    private void processPoint (ActionEvent event) {
        if (start) {
            output.setText("");
            start = false;
        }
        String point = ".";
        if (output.getText().equals("")){
            output.setText("0.");
            toDataBase = "0.";
        }else if (output.getText().contains(".")){
            return;
        } else if (output.getText().equals(Const.ERROR)){
            output.setText("0.");
        } else {
        output.setText(output.getText() + point);
        toDataBase = toDataBase + point;
        }
    }
    
    @FXML
    private void processOperator (ActionEvent event) throws SQLException, ClassNotFoundException {
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
            
            if (Double.parseDouble(output.getText())==0&&operator.equals("/")){
                output.setText(Const.ERROR);
                operator = "";
                num1 = 0;
                toDataBase ="";
                start = true;
                return;
            }
            
            output.setText(df.format(model.calculation(num1, Double.parseDouble(output.getText()), operator)));
            operator = "";
            toDataBase = toDataBase + " = " + output.getText();
            DataBaseHandler dataBaseHandler = new DataBaseHandler();
            dataBaseHandler.calcWrite(toDataBase);
            table.getItems().clear();
            
            try {
                Connection connection = DataBaseHandler.getDbConnection();
                ResultSet  rs         = connection.createStatement().executeQuery("SELECT * FROM history  ORDER BY datetime DESC LIMIT 0,20");
                while (rs.next()) {
                    oblist.add(new ModelTable(rs.getString("calculations")));
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            column_calc.setCellValueFactory(new PropertyValueFactory<>("calculation"));
            table.setItems(oblist);
            
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
    
    private ObservableList<ModelTable> oblist = FXCollections.observableArrayList();
    
    @Override
    public void initialize (URL location, ResourceBundle resources) {
        
           table.getItems().clear();
        try {
            Connection connection = DataBaseHandler.getDbConnection();
            ResultSet  rs         = connection.createStatement().executeQuery("SELECT * FROM history  ORDER BY datetime DESC LIMIT 0,20");
            while (rs.next()) {
                oblist.add(new ModelTable(rs.getString("calculations")));
                
            } connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    
        column_calc.setCellValueFactory(new PropertyValueFactory<>("calculation"));
        table.setItems(oblist);
    }
}
