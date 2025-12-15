package com.yourname.library.ui;

import com.yourname.library.util.SystemConfig;
import javax.swing.*;

public class ManageConfigView {

    public ManageConfigView() {
        initialize();
    }

    private void initialize() {

        JFrame frame = new JFrame("Sistem Ayarları");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        frame.add(panel);

        int y = 30;
        int gap = 40;

        JLabel lblLimit = new JLabel("Maks. Kitap Limiti:");
        lblLimit.setBounds(30, y, 150, 25);
        panel.add(lblLimit);

        JTextField limitField = new JTextField(String.valueOf(SystemConfig.getInstance().getMaxBorrowLimit()));
        limitField.setBounds(180, y, 150, 25);
        panel.add(limitField);
        y += gap;

        JLabel lblDays = new JLabel("Ödünç Süresi (Gün):");
        lblDays.setBounds(30, y, 150, 25);
        panel.add(lblDays);

        JTextField daysField = new JTextField(String.valueOf(SystemConfig.getInstance().getLoanPeriodDays()));
        daysField.setBounds(180, y, 150, 25);
        panel.add(daysField);
        y += gap;

        JLabel lblFine = new JLabel("Günlük Ceza (TL):");
        lblFine.setBounds(30, y, 150, 25);
        panel.add(lblFine);

        JTextField fineField = new JTextField(String.valueOf(SystemConfig.getInstance().getDailyFine()));
        fineField.setBounds(180, y, 150, 25);
        panel.add(fineField);
        y += 50;

        JButton saveBtn = new JButton("Ayarları Kaydet");
        saveBtn.setBounds(100, y, 200, 35);
        panel.add(saveBtn);

        saveBtn.addActionListener(e -> {
            try {
                int limit = Integer.parseInt(limitField.getText());
                int days = Integer.parseInt(daysField.getText());
                double fine = Double.parseDouble(fineField.getText());

                if (limit < 1 || days < 1 || fine < 0) {
                    JOptionPane.showMessageDialog(frame, "Lütfen mantıklı pozitif değerler girin!", "Hata", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                SystemConfig.getInstance().setMaxBorrowLimit(limit);
                SystemConfig.getInstance().setLoanPeriodDays(days);
                SystemConfig.getInstance().setDailyFine(fine);

                JOptionPane.showMessageDialog(frame, "Ayarlar başarıyla güncellendi!\nYeni işlemler bu ayarlara göre yapılacak.");
                frame.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Lütfen geçerli sayısal değerler girin.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }
}