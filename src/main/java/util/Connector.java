package util;

import beans.PointAttempt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Connector {

    private static final String DB_URL = "jdbc:postgresql://pg:5432/studs";
    private static final String USER = "s338879";
    private static final String PASS = "7emn2ZsjgX2BaLq4";
    private static final Connector INSTANCE = new Connector();

    Connection connection;

    public static Connector getInstance() {
        return INSTANCE;
    }

    private Connector() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connect success!");
            initDB();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error on creating database connection");
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void makeBigAdd(PointAttempt attempt) {
        try(PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO checks(id, x, y, r, date, working_time, status) VALUES (" +
                        "(SELECT nextval('id_generator')), ?,?,?,?,?,?" +
                        ")"
        )) {
            statement.setDouble(1, attempt.getPoint().getX());
            statement.setDouble(2, attempt.getPoint().getY());
            statement.setDouble(3, attempt.getPoint().getR());
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.ofInstant(attempt.getAttemptTime(), ZoneOffset.UTC)));
            statement.setDouble(5, attempt.getProcessTime());
            statement.setBoolean(6, attempt.isSuccess());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void initDB() throws SQLException {
        try(Statement sm = connection.createStatement();) {
            sm.execute("CREATE TABLE IF NOT EXISTS checks\n" +
                    "(\n" +
                    "    id     INTEGER PRIMARY KEY,\n" +
                    "    x      DOUBLE PRECISION,\n" +
                    "    y      DOUBLE PRECISION,\n" +
                    "    r      DOUBLE PRECISION,\n" +
                    "    date   TIMESTAMP,\n" +
                    "    working_time DOUBLE PRECISION,\n" +
                    "    status BOOLEAN\n" +
                    ");");
            sm.execute("CREATE SEQUENCE IF NOT EXISTS id_generator START 1 MINVALUE 1 MAXVALUE 2147483647;");
        }
    }

}
