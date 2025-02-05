import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame implements ActionListener {
    private JTextField display;
    private JPanel panel;
    private String[] buttons = {
        "7", "8", "9", "/", 
        "4", "5", "6", "*", 
        "1", "2", "3", "-", 
        "0", ".", "=", "+", 
        "C"
    };
    private String operand1 = "";
    private String operand2 = "";
    private String operator = "";

    public Calculator() {
        display = new JTextField();
        display.setEditable(false);
        display.setPreferredSize(new Dimension(200, 50));
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 5, 5));

        for (String button : buttons) {
            JButton btn = new JButton(button);
            btn.addActionListener(this);
            panel.add(btn);
        }

        setLayout(new BorderLayout());
        add(display, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        setTitle("Calculator");
        setSize(250, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.charAt(0) >= '0' && command.charAt(0) <= '9' || command.equals(".")) {
            if (!operator.isEmpty()) {
                operand2 += command;
            } else {
                operand1 += command;
            }
            display.setText(operand1 + operator + operand2);
        } else if (command.equals("=")) {
            double result = calculate(Double.parseDouble(operand1), Double.parseDouble(operand2), operator);
            display.setText(operand1 + operator + operand2 + "=" + formatResult(result));
            operand1 = Double.toString(result);
            operand2 = "";
            operator = "";
        } else if (command.equals("C")) {
            operand1 = "";
            operand2 = "";
            operator = "";
            display.setText("");
        } else {
            if (operator.isEmpty() || operand2.isEmpty()) {
                operator = command;
            } else {
                double result = calculate(Double.parseDouble(operand1), Double.parseDouble(operand2), operator);
                operand1 = Double.toString(result);
                operator = command;
                operand2 = "";
            }
            display.setText(operand1 + operator + operand2);
        }
    }

    private double calculate(double op1, double op2, String operator) {
        switch (operator) {
            case "+":
                return op1 + op2;
            case "-":
                return op1 - op2;
            case "*":
                return op1 * op2;
            case "/":
                if (op2 != 0) {
                    return op1 / op2;
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot divide by zero");
                    return 0;
                }
            default:
                return 0;
        }
    }

    private String formatResult(double result) {
        if (result == (long) result) {
            return String.format("%d", (long) result);
        } else {
            return String.format("%s", result);
        }
    }

    public static void main(String[] args) {
        new Calculator();
    }
}