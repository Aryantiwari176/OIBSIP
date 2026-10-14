import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Cancellation {

    public void showCancellation() {

        JFrame frame = new JFrame("Cancel Reservation");
        frame.setSize(400, 220);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField pnrField = new JTextField();
        JButton searchButton = new JButton("Search Ticket");
        JButton cancelButton = new JButton("Cancel Ticket");

        panel.add(new JLabel("Enter PNR Number:"));
        panel.add(pnrField);
        panel.add(searchButton);
        panel.add(cancelButton);

        cancelButton.setEnabled(false);

        final String[] currentPnr = {null};

        searchButton.addActionListener(e -> {

            String pnr = pnrField.getText().trim();

            if (pnr.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame, "Please enter PNR number!"
                );
                return;
            }

            String sql = "SELECT * FROM reservations WHERE pnr = ?";

            try (Connection con = DatabaseConnection.getConnection()) {

                if (con == null) {
                    JOptionPane.showMessageDialog(
                            frame, "Database connection failed!"
                    );
                    return;
                }

                try (PreparedStatement ps = con.prepareStatement(sql)) {

                    ps.setString(1, pnr);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (rs.next()) {

                            String status = rs.getString("status");

                            String details =
                                    "Passenger: " + rs.getString("passenger_name")
                                    + "\nTrain: " + rs.getString("train_name")
                                    + "\nDate: " + rs.getString("journey_date")
                                    + "\nFrom: " + rs.getString("source")
                                    + "\nTo: " + rs.getString("destination")
                                    + "\nStatus: " + status;

                            JOptionPane.showMessageDialog(frame, details);

                            if ("BOOKED".equalsIgnoreCase(status)) {
                                currentPnr[0] = pnr;
                                cancelButton.setEnabled(true);
                            } else {
                                currentPnr[0] = null;
                                cancelButton.setEnabled(false);
                            }

                        } else {
                            currentPnr[0] = null;
                            cancelButton.setEnabled(false);

                            JOptionPane.showMessageDialog(
                                    frame, "Ticket not found!"
                            );
                        }
                    }
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        frame, "Error: " + ex.getMessage()
                );
            }
        });

        cancelButton.addActionListener(e -> {

            if (currentPnr[0] == null) {
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to cancel this ticket?",
                    "Confirm Cancellation",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            String sql =
                    "UPDATE reservations SET status = ? "
                    + "WHERE pnr = ? AND status = ?";

            try (Connection con = DatabaseConnection.getConnection()) {

                if (con == null) {
                    JOptionPane.showMessageDialog(
                            frame, "Database connection failed!"
                    );
                    return;
                }

                try (PreparedStatement ps = con.prepareStatement(sql)) {

                    ps.setString(1, "CANCELLED");
                    ps.setString(2, currentPnr[0]);
                    ps.setString(3, "BOOKED");

                    int rows = ps.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(
                                frame, "Ticket cancelled successfully!"
                        );

                        currentPnr[0] = null;
                        cancelButton.setEnabled(false);
                        pnrField.setText("");

                    } else {
                        JOptionPane.showMessageDialog(
                                frame, "Ticket is already cancelled or not found!"
                        );
                    }
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        frame, "Cancellation failed: " + ex.getMessage()
                );
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}