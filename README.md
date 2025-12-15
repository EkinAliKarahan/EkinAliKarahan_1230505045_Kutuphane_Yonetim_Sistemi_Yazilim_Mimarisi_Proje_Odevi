### Kütüphane Yönetim Sistemi Projesi İçin README

---

#### Proje Genel Bakış

Bu proje, Java kullanılarak Swing ile geliştirilmiş bir **Kütüphane Yönetim Sistemi** uygulamasıdır. Uygulama, kitap yönetimi, kullanıcı yönetimi (öğrenci ve personel), ödünç alma ve iade işlemleri, kullanıcı giriş doğrulama gibi özellikleri destekler. Projede **Singleton**, **Factory**, **Strategy**, **Decorator**, **Observer** ve **State** tasarım desenleri, modülerlik ve ölçeklenebilirlik sağlamak amacıyla kullanılmıştır.

---

#### Temel Özellikler

1. **Kullanıcı Yönetimi**:
   - Öğrenci ve personel rolleri desteklenir.
   - Kullanıcı kaydı, giriş yapma ve profil yönetimi sağlanır.
   - Kullanıcılar, kişisel bilgilerini güncelleyebilir ve şifrelerini değiştirebilir.

2. **Kitap Yönetimi**:
   - Kitap ekleme, güncelleme ve silme işlemleri yapılabilir.
   - Mevcut, ödünç alınmış ve kayıp kitaplar görüntülenebilir.
   - Kitap ödünç alma ve iade etme işlemleri, durum yönetimi ile sağlanır.

3. **Arama Fonksiyonu**:
   - Kitapları başlık, yazar veya konuya göre arama desteği sağlanır (**Strategy Deseni** kullanılmıştır).

4. **Uygulanan Tasarım Desenleri**:
   - **Singleton Deseni**: MySQL veri tabanına bağlanmak ve sistem ayarlarını kontrol etmek için.
   - **Factory Deseni**: Kullanıcı oluşturma işlemleri için.
   - **Strategy Deseni**: Farklı arama stratejilerini uygulamak için.
   - **State Deseni**: Kitap durumlarının (Mevcut, Ödünç Alınmış, Kayıp) yönetimi için.
   - **Decorator Deseni**: Kitaplara ekstra özellikler (örneğin, puanlama) eklemek için.
   - **Observer Deseni**: Kütüphane envanterindeki değişiklikleri kullanıcıya bildirmek için.

---

#### Proje Yapısı

Proje, aşağıdaki modüler yapıya sahiptir:

- **`dao`**: Veritabanı işlemleri için Veri Erişim Nesneleri.
  - `BookDAO`: Kitaplarla ilgili CRUD işlemlerini yönetir.
  - `UserDAO`: Kullanıcı verilerini yönetir.
  - `BorrowLogDAO`: Ödünç alma ve iade işlemlerini takip eder.
  - `LoanDAO`: Ödünç alma ve iade işlemleri ile ilgili CRUD işlemlerini yönetir.

- **`model`**: Kütüphane sisteminin temel varlıklarını temsil eder.
  - `AbstractUser`: `Admin`, `Student` ve `Staff` sınıflarının temel özelliklerini içeren soyut sınıf.
  - `AbstractBook`: `Book` temel özelliklerini içeren soyut sınıf.
  - `Admin`: `AbstractUser` üst sınıfı ile admin kullanıcı oluşturur.
  - `Book`: `AbstractBook` ile kitap oluşturur.
  - `Loan`: Ödünç bilgilerini ve işlemlerini içerir.
  - `Staff`: `AbstractUser` üst sınıfı ile yetkili kullanıcı oluşturur.
  - `Student`: `AbstractUser` üst sınıfı ile öğrenci kullanıcı oluşturur

- **`pattern`**: Tasarım desenlerinin implementasyonları.
  - `factory`: Kullanıcı oluşturmak için **Factory Deseni**.
  - `state`: Kitap durum yönetimi (Mevcut, Ödünç Alınmış, Kayıp).
  - `strategy`: Arama stratejileri (Başlığa göre, Yazara göre).
  - `decorator`: Kitaplara ek işlevler eklemek için (örneğin, puanlama).
  - `Observer`: Kütüphane Envanterindeki değişimleri izler.
  - `Singleton`: Veri tabanı bağlantısı ve Global sistem ayarları.

- **`service`**: İş mantığı katmanı.
  - `UserService`: Kullanıcıyla ilgili işlemleri yönetir.
  - `BookService`: Kitap işlemlerini ve durum geçişlerini yönetir.
  - `LoanService`: Ödünç işlemlerini ve durumlarını yönetir.

- **`ui`**: Swing ile geliştirilmiş kullanıcı arayüzü bileşenleri.
  - `BorrowReturnView`: Ödünç alma ve iade ekranı.
  - `LoginView`: Kullanıcı giriş ekranı.
  - `MainView`:  Giriş sonrası ekranı.
  - `ManageUsersView`: Kullanıcı yönetim ekranı.
  - `ManageConfigView`: Sistem ayarları yönetim ekranı.
  - `ManageBooksView`: Kitap yönetim ekranı.
  - `RegisterView`: Kayıt ekranı.
  - `SearchBooksView`: Kitap arama ekranı.
  - `StudentView`: Öğrenciler için kullanıcı arayüzü.
  - `StaffView`: Personel için kullanıcı arayüzü.
  - `UserDetailView`: Kullanıcı detayları için kullanıcı arayüzü.

- **`util`**: Yardımcı sınıflar.
  - `DatabaseConnection`: Veritabanı bağlantısını yönetir.
  - `SystemConfig`: Sistem ayarlarını yönetir.

---

#### Kullanılan Teknolojiler

- **Java**: Programlama dili.
- **Swing**: Kullanıcı arayüzü bileşenleri.
- **MySQL**: Veritabanı yönetim sistemi.
- **Maven**: Derleme otomasyon aracı.

---

#### Kurulum Talimatları

1. **Gereksinimler**:
   - [Java 17+](https://www.oracle.com/java/technologies/javase-downloads.html)'yı yükleyin.
   - [MySQL](https://dev.mysql.com/downloads/)'i yükleyin.
   - Bir IDE kurun (örn. IntelliJ IDEA, Eclipse).

2. **Veritabanı Kurulumu**:
   - `library_db.sql` dosyasını MySQL'e aktarın.
   - `DatabaseConnection.java` dosyasındaki veritabanı bağlantı bilgilerini düzenleyin:
     ```java
     private final String url = "jdbc:mysql://localhost:3306/library_db";
     private final String username = "kendi_kullanıcı_adınız";
     private final String password = "kendi_şifreniz";
     ```

3. **Projeyi Derleme**:
   - Projeyi IDE'nizde açın.
   - Maven kullanarak projeyi derleyin.

4. **Uygulamayı Çalıştırma**:
   - `org.example` paketindeki `Main.java` dosyasını çalıştırın.

---


Daha fazla bilgi eklemek veya düzenlemek isterseniz bana bildirin!
