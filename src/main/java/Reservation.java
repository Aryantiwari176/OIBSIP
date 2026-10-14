import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class Reservation {

    public void showReservation() {

        JFrame frame = new JFrame("Online Reservation System");
        frame.setSize(550, 470);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField passengerName = new JTextField();
        JTextField trainNumber = new JTextField();
        JTextField trainName = new JTextField();
        JTextField journeyDate = new JTextField();
        JTextField source = new JTextField();
        JTextField destination = new JTextField();

        JComboBox<String> classType = new JComboBox<>(
                new String[]{"Sleeper", "AC", "General"}
        );

        JButton bookButton = new JButton("Book Ticket");
        JButton cancelButton = new JButton("Cancel Ticket");

        panel.add(new JLabel("Passenger Name:"));
        panel.add(passengerName);

        panel.add(new JLabel("Train Number:"));
        panel.add(trainNumber);

        panel.add(new JLabel("Train Name:"));
        panel.add(trainName);

        panel.add(new JLabel("Journey Date (DD-MM-YYYY):"));
        panel.add(journeyDate);

        panel.add(new JLabel("Source Station:"));
        panel.add(source);

        panel.add(new JLabel("Destination Station:"));
        panel.add(destination);

        panel.add(new JLabel("Class Type:"));
        panel.add(classType);

        panel.add(bookButton);
        panel.add(cancelButton);

        bookButton.addActionListener(e -> {

            String name = passengerName.getText().trim();
            String number = trainNumber.getText().trim();
            String train = trainName.getText().trim();
            String date = journeyDate.getText().trim();
            String from = source.getText().trim();
            String to = destination.getText().trim();
            String selectedClass = (String) classType.getSelectedItem();

            if (name.isEmpty() || number.isEmpty()
                    || train.isEmpty() || date.isEmpty()
                    || from.isEmpty() || to.isEmpty()) {

                JOptionPane.showMessageDialog(
                        frame, "Please fill all fields!"
                );
                return;
            }

            if (!number.matches("\\d+")) {
                JOptionPane.showMessageDialog(
                        frame, "Train number must contain digits only!"
                );
                return;
            }

            if (from.equalsIgnoreCase(to)) {
                JOptionPane.showMessageDialog(
                        frame, "Source and destination must be different!"
                );
                return;
            }

            if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
                JOptionPane.showMessageDialog(
                        frame, "Enter date in DD-MM-YYYY format!"
                );
                return;
            }

            String pnr = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 12)
                    .toUpperCase();

            String sql = "INSERT INTO reservations "
                    + "(pnr, passenger_name, train_number, train_name, "
                    + "journey_date, source, destination, class_type, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection con = DatabaseConnection.getConnection()) {

                if (con == null) {
                    JOptionPane.showMessageDialog(
                            frame, "Database connection failed!"
                    );
                    return;
                }

                try (PreparedStatement ps = con.prepareStatement(sql)) {

                    ps.setString(1, pnr);
                    ps.setString(2, name);
                    ps.setString(3, number);
                    ps.setString(4, train);
                    ps.setString(5, date);
                    ps.setString(6, from);
                    ps.setString(7, to);
                    ps.setString(8, selectedClass);
                    ps.setString(9, "BOOKED");

                    ps.executeUpdate();

                    String details = "Booking Successful!\n\n"
                            + "PNR Number: " + pnr + "\n"
                            + "Passenger: " + name + "\n"
                            + "Train: " + train + "\n"
                            + "Train Number: " + number + "\n"
                            + "Date: " + date + "\n"
                            + "From: " + from + "\n"
                            + "To: " + to + "\n"
                            + "Class: " + selectedClass;

                    JOptionPane.showMessageDialog(frame, details);

                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Booking failed!\n" + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        });

        cancelButton.addActionListener(e -> {
            Cancellation cancellation = new Cancellation();
            cancellation.showCancellation();
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}