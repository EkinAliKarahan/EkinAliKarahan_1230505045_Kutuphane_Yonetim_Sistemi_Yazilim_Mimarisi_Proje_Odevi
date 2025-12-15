package com.yourname.library.pattern.observer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InventoryUIObserver implements IInventoryObserver {

    @Override
    public void update() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("[LOG - " + timeStamp + "] BİLDİRİM: Kütüphane envanterinde değişiklik yapıldı! (Observer Tetiklendi)");
    }
}