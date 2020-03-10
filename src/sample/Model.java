package sample;

public class Model {
    
    public double calculation (double a, double b, String operator) {
        switch (operator) {
            case "+":
                return a+b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                return a / b;
        }
        System.out.println("Неизвестный оператор" + operator);
        return 0;
    }
}
