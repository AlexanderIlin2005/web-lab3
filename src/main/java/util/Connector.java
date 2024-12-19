package util;

import beans.PointAttempt;
import io.github.cdimascio.dotenv.Dotenv;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.time.ZoneOffset;


public class Connector {

    // Загружаем .env файл из папки resources
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

    // Читаем переменные окружения, с fallback на .env файл
    private static final String DB_URL = getEnvVariable("DB_URL");
    private static final String USER = getEnvVariable("DB_USER");
    private static final String PASS = getEnvVariable("DB_PASS");

    private static final Connector INSTANCE = new Connector();
    private Connection connection;

    // Метод для получения переменной окружения с fallback на .env файл
    private static String getEnvVariable(String key) {
        String value = System.getenv(key); // Читаем из окружения
        if (value == null && dotenv != null) {
            value = dotenv.get(key); // Fallback на .env файл
        }
        return value;
    }

    // Метод для получения экземпляра класса Connector
    public static Connector getInstance() {
        return INSTANCE;
    }

    // Конструктор класса, который устанавливает соединение с базой данных
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

    // Метод для добавления данных в базу
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

    // Метод для инициализации базы данных (создание таблиц и последовательности)
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

    // Метод для получения текущего соединения с базой данных
    public Connection getConnection() {
        return connection;
    }
}
