package com.yourname.library.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        // Field yerine yerel değişken yapıldı (Sadece burada kullanılıyorlar)
        String url = "jdbc:mysql://localhost:3306/library_db?useUnicode=true&characterEncoding=utf8";
        String username = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Veritabanına başarıyla bağlanıldı.");
        } catch (ClassNotFoundException e) {
            System.err.println("HATA: MySQL JDBC sürücüsü bulunamadı. Lütfen kütüphaneyi (JAR) eklediğinizden emin olun.");
            System.err.println("Detay: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("HATA: Veritabanına bağlanılamadı. XAMPP/MySQL açık mı?");
            System.err.println("Hata Detayı: " + e.getMessage());
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null || isConnectionClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private static boolean isConnectionClosed() {
        try {
            return instance.connection == null || instance.connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }
}