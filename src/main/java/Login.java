import javax.swing.*;
import java.awt.*;

public class Login {

    public void showLogin() {

        JFrame frame = new JFrame("Online Reservation System Login");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("User Login");
        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");

        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();

        JButton loginButton = new JButton("Login");

        panel.add(title);
        panel.add(new JLabel(""));
        panel.add(userLabel);
        panel.add(username);
        panel.add(passLabel);
        panel.add(password);
        panel.add(new JLabel(""));
        panel.add(loginButton);

        frame.add(panel);

        loginButton.addActionListener(e -> {
            String user = username.getText().trim();
            String pass = new String(password.getPassword());

            if (user.equals("admin") && pass.equals("1234")) {
                JOptionPane.showMessageDialog(
                    frame, "Login Successful!"
                );

                frame.dispose();

                Reservation reservation = new Reservation();
                reservation.showReservation();
            } else {
                JOptionPane.showMessageDialog(
                    frame,
                    "Invalid username or password!",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        frame.setVisible(true);
    }
}