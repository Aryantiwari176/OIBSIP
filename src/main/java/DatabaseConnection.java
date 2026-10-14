import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:h2:file:./database/reservationdb";

    public static Connection getConnection() {
        try {
            new File("database").mkdirs();

            Connection con =
                    DriverManager.getConnection(URL, "sa", "");

            Statement stmt = con.createStatement();

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS reservations (" +
                "pnr VARCHAR(20) PRIMARY KEY, " +
                "passenger_name VARCHAR(100) NOT NULL, " +
                "train_number VARCHAR(20) NOT NULL, " +
                "train_name VARCHAR(100) NOT NULL, " +
                "journey_date VARCHAR(20) NOT NULL, " +
                "source VARCHAR(100) NOT NULL, " +
                "destination VARCHAR(100) NOT NULL, " +
                "class_type VARCHAR(30) NOT NULL, " +
                "status VARCHAR(20) DEFAULT 'BOOKED')"
            );

            stmt.close();
            return con;

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try (Connection con = getConnection()) {

            if (con != null) {
                System.out.println("Database connected successfully!");
                System.out.println("Reservation table is ready!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}