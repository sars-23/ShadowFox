import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GuiCalculator extends JFrame implements ActionListener {
    private JTextField display;
    private String operator = "";
    private double num1 = 0;

    public GuiCalculator() {
        setTitle("Enhanced GUI Calculator");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        display = new JTextField();
        display.setFont(new Font("Arial", Font.PLAIN, 24));
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        String[] buttons = {
            "7", "8", "9", "/", "√",
            "4", "5", "6", "*", "%",
            "1", "2", "3", "-", "^",
            "0", "C", "=", "+", ""
        };

        JPanel panel = new JPanel(new GridLayout(5, 4, 5, 5));

        for (String text : buttons) {
            if (!text.isEmpty()) {
                JButton button = new JButton(text);
                button.setFont(new Font("Arial", Font.BOLD, 20));
                button.addActionListener(this);
                panel.add(button);
            } else {
                panel.add(new JLabel());
            }
        }

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String input = e.getActionCommand();

        try {
            if ("0123456789.".contains(input)) {
                display.setText(display.getText() + input);
            } else if ("+-*/%^".contains(input)) {
                num1 = Double.parseDouble(display.getText());
                operator = input;
                display.setText("");
            } else if (input.equals("√")) {
                double value = Double.parseDouble(display.getText());
                if (value < 0) throw new ArithmeticException("Cannot sqrt negative");
                display.setText(String.valueOf(Math.sqrt(value)));
            } else if (input.equals("=")) {
                double num2 = Double.parseDouble(display.getText());
                double result = switch (operator) {
                    case "+" -> num1 + num2;
                    case "-" -> num1 - num2;
                    case "*" -> num1 * num2;
                    case "/" -> {
                        if (num2 == 0) throw new ArithmeticException("Divide by zero");
                        yield num1 / num2;
                    }
                    case "%" -> num1 % num2;
                    case "^" -> Math.pow(num1, num2);
                    default -> 0;
                };
                display.setText(String.valueOf(result));
            } else if (input.equals("C")) {
                display.setText("");
                operator = "";
                num1 = 0;
            }
        } catch (NumberFormatException ex) {
            display.setText("Invalid input");
        } catch (ArithmeticException ex) {
            display.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GuiCalculator::new);
    }
}
