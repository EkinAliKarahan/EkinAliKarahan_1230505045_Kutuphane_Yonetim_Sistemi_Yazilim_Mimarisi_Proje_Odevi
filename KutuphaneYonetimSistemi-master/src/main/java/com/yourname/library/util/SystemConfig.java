package com.yourname.library.util;

import java.io.*;
import java.util.Properties;

public class SystemConfig {
    private static SystemConfig instance;
    private final Properties properties;
    private final String CONFIG_FILE = "config.properties";

    private int maxBorrowLimit = 3;
    private int loanPeriodDays = 15;
    private double dailyFine = 1.5;

    private SystemConfig() {
        properties = new Properties();
        loadConfig();
    }

    public static synchronized SystemConfig getInstance() {
        if (instance == null) {
            instance = new SystemConfig();
        }
        return instance;
    }

    private void loadConfig() {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);

            maxBorrowLimit = Integer.parseInt(properties.getProperty("maxBorrowLimit", "3"));
            loanPeriodDays = Integer.parseInt(properties.getProperty("loanPeriodDays", "15"));
            dailyFine = Double.parseDouble(properties.getProperty("dailyFine", "1.5"));

        } catch (IOException | NumberFormatException e) {
            System.out.println("Ayarlar dosyası bulunamadı. Varsayılanlar oluşturuluyor...");
            saveConfig();
        }
    }

    private void saveConfig() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {

            properties.setProperty("maxBorrowLimit", String.valueOf(maxBorrowLimit));
            properties.setProperty("loanPeriodDays", String.valueOf(loanPeriodDays));
            properties.setProperty("dailyFine", String.valueOf(dailyFine));
            properties.store(output, "Kutuphane Sistemi Ayarlari");

        } catch (IOException e) {
            System.err.println("HATA: Ayarlar kaydedilemedi: " + e.getMessage());
        }
    }

    public int getMaxBorrowLimit() {
        return maxBorrowLimit;
    }

    public void setMaxBorrowLimit(int maxBorrowLimit) {
        this.maxBorrowLimit = maxBorrowLimit;
        saveConfig();
    }

    public int getLoanPeriodDays() {
        return loanPeriodDays;
    }

    public void setLoanPeriodDays(int loanPeriodDays) {
        this.loanPeriodDays = loanPeriodDays;
        saveConfig();
    }

    public double getDailyFine() {
        return dailyFine;
    }

    public void setDailyFine(double dailyFine) {
        this.dailyFine = dailyFine;
        saveConfig();
    }
}