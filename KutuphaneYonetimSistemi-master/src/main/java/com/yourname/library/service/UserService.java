package com.yourname.library.service;

import com.yourname.library.dao.UserDAO;
import com.yourname.library.model.AbstractUser;
import java.util.List;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public boolean registerUser(AbstractUser user) {
        if (userDAO.isEmailTaken(user.getEmail())) throw new IllegalArgumentException("Bu E-posta adresi zaten kullanımda!");
        if (userDAO.isTcNoTaken(user.getTcNo())) throw new IllegalArgumentException("Bu T.C. Kimlik Numarası zaten kayıtlı!");
        if (userDAO.isPhoneTaken(user.getPhone())) throw new IllegalArgumentException("Bu Telefon numarası zaten kullanımda!");
        if (userDAO.isNumberTaken(user.getNumber())) throw new IllegalArgumentException("Bu Okul/Personel numarası zaten kullanımda!");

        boolean result = userDAO.addUser(user);
        if (result) {
            System.out.println("Kullanıcı başarıyla eklendi: " + user.getEmail());
            return true;
        }
        return false;
    }

    public AbstractUser login(String credential, String password) {
        AbstractUser user = userDAO.getUserByCredential(credential);
        if (user != null) {
            if (user.getPassword().trim().equals(password.trim())) {
                System.out.println("Giriş başarılı: " + user.getEmail());
                return user;
            } else {
                System.out.println("Şifre eşleşmedi.");
            }
        } else {
            System.out.println("Kullanıcı bulunamadı: " + credential);
        }
        return null;
    }

    public boolean changeUserRole(int userId, String newRole) {
        if (!newRole.equalsIgnoreCase("Student") && !newRole.equalsIgnoreCase("Staff") &&
                !newRole.equalsIgnoreCase("Admin")) {
            throw new IllegalArgumentException("Geçersiz rol tipi!");
        }
        return userDAO.updateUserRole(userId, newRole);
    }

    public boolean updateUser(AbstractUser user) {
        return userDAO.updateUser(user);
    }

    public void deleteUser(int userId) {
        userDAO.deleteUser(userId);
    }

    public List<AbstractUser> getAllUsers() {
        return userDAO.getAllUsers();
    }
}