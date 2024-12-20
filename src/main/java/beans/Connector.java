package beans;

import io.github.cdimascio.dotenv.Dotenv;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.enterprise.inject.spi.CDI;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Named
@ApplicationScoped
@Singleton
public class Connector implements Serializable {

    private static final Dotenv dotenv;

    static {
        InputStream dotenvStream = Connector.class.getClassLoader().getResourceAsStream(".env");
        if (dotenvStream == null) {
            System.err.println("Не удалось найти файл .env в resources");
            dotenv = null;
        } else {
            dotenv = Dotenv.configure().ignoreIfMissing().load();
        }
    }

    private static final String DB_URL = getEnvVariable("DB_URL");
    private static final String USER = getEnvVariable("DB_USER");
    private static final String PASS = getEnvVariable("DB_PASS");

    private Connection connection;

    private static String getEnvVariable(String key) {
        String value = System.getenv(key);
        if (value == null && dotenv != null) {
            value = dotenv.get(key);
        }
        return value;
    }

    @PostConstruct
    private void init() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connect success!");
            initDB();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error on creating database connection");
            System.err.println(e.getMessage());
            throw new RuntimeException("Failed to initialize Connector", e);
        }
    }

    public void makeBigAdd(PointAttempt attempt) {
        try (PreparedStatement statement = connection.prepareStatement(
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
        try (Statement sm = connection.createStatement()) {
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



    @PreDestroy
    private void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Failed to close database connection");
            System.err.println(e.getMessage());
        }
    }

    public static Connector getInstance() {
        return CDI.current().select(Connector.class).get();
    }
}
