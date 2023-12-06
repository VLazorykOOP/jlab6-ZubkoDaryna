import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LabSixTwo {
    private static final int MAX_N = 20;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("LabSixTwo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);

            JPanel panel = new JPanel();
            frame.add(panel);
            placeComponents(panel);

            frame.setVisible(true);
        });
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(new GridLayout(3, 2));

        JLabel label = new JLabel("Enter the value of n (<20):");
        panel.add(label);

        JTextField textField = new JTextField();
        panel.add(textField);

        JButton submitButton = new JButton("Submit");
        panel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int n = Integer.parseInt(textField.getText());
                    processInput(n);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid input. Please enter a valid integer.");
                } catch (FileFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid file format. Please check the input file.");
                } catch (MyCustomException ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "An unexpected error occurred.");
                }
            }
        });
    }

    private static void processInput(int n) throws FileFormatException, MyCustomException, IOException {
        if (n < MAX_N) {
            int[][] X = doubleArray(n);
            boolean[] Y = new boolean[n];
            for (int i = 0; i < n; i++) {
                Y[i] = isUnique(X[i]);
            }
            System.out.print("Vector Y: ");
            for (int i = 0; i < n; i++) {
                System.out.print(Y[i] + " ");
            }
        } else {
            throw new IllegalArgumentException("Invalid value (n >= 20)");
        }
    }

    static int[][] doubleArray(int n) throws FileFormatException, MyCustomException, IOException {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (Scanner in = new Scanner(file)) {
                int[][] X = new int[n][n];
                for (int i = 0; i < n; i++) {
                    System.out.print("Add value for " + (i) + " row ");
                    for (int j = 0; j < n; j++) {
                        if (in.hasNextInt()) {
                            int value = in.nextInt();
                            if (value == 5) {
                                throw new MyCustomException("Custom exception: Found the number 5 in the file");
                            }
                            X[i][j] = value;
                        } else {
                            throw new FileFormatException("Invalid file format");
                        }
                    }
                }
                return X;
            }
        } else {
            throw new IOException("File not selected");
        }
    }

    static boolean isUnique(int[] X) {
        for (int i = 0; i < X.length; i++) {
            for (int j = i + 1; j < X.length; j++) {
                if (X[i] == X[j]) {
                    return false;
                }
            }
        }
        return true;
    }

    static class FileFormatException extends Exception {
        public FileFormatException(String message) {
            super(message);
        }
    }

    static class MyCustomException extends Exception {
        public MyCustomException(String message) {
            super(message);
        }
    }
}
