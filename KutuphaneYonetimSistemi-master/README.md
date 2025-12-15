### Kütüphane Yönetim Sistemi Projesi

---

#### 📂 Proje Genel Bakış

Bu proje, Java kullanılarak Swing arayüzü ile geliştirilmiş kapsamlı bir **Kütüphane Yönetim Sistemi** uygulamasıdır. Uygulama; kitap envanter takibi, detaylı kullanıcı yönetimi (öğrenci ve personel), ödünç/iade döngüsü ve ceza hesaplama gibi özellikleri içerir.

Yazılım mimarisinde sürdürülebilirlik ve esneklik sağlamak amacıyla **Singleton**, **Factory**, **Strategy**, **Decorator**, **Observer** ve **State** tasarım desenleri aktif olarak kullanılmıştır.

---

#### ✨ Temel Özellikler

1. **Kullanıcı Yönetimi**:
    - **Öğrenci ve Personel/Admin** rolleri ile ayrıştırılmış yetkilendirme.
    - Kayıt olma, güvenli giriş yapma ve profil (bilgi/şifre) güncelleme.
    - Personel paneli üzerinden tüm üyelerin yönetimi.

2. **Kitap ve Envanter Yönetimi**:
    - Kitap ekleme, güncelleme ve silme (CRUD) işlemleri.
    - Kitapların anlık durum takibi (Mevcut veya Ödünçte).
    - **Observer Deseni** sayesinde envanter güncellemelerinin arayüze anlık yansıması.

3. **Ödünç ve İade Sistemi**:
    - Kitap ödünç alma ve iade etme süreçleri.
    - Gecikmiş kitaplar için gün bazlı otomatik **ceza hesaplama**.
    - Kullanıcıların kendi ödünç geçmişlerini ve cezalarını görüntülemesi.

4. **Gelişmiş Arama**:
    - **Strategy Deseni** ile Başlık, Yazar, ISBN veya Kategoriye göre dinamik filtreleme.

5. **Sistem Ayarları**:
    - Ödünç alma limiti, gün süresi ve gecikme cezası tutarının arayüzden yönetilmesi (**Singleton Deseni** ile merkezi yapı).

---

#### 🏗️ Uygulanan Tasarım Desenleri

Projede kullanılan desenler ve görevleri:

- **Singleton Deseni**: Veritabanı bağlantısı (`DatabaseConnection`) ve sistem konfigürasyonlarının (`SystemConfig`) tek bir merkezden yönetilmesi için.
- **Factory Deseni**: Kullanıcı tiplerinin (`Student`, `Staff`, `Admin`) oluşturulma sürecini soyutlamak için.
- **Strategy Deseni**: Arama motorunda farklı algoritmaların (Yazar, Başlık vb.) çalışma zamanında seçilmesi için.
- **State Deseni**: Kitapların durum geçişlerini (`Available` $\leftrightarrow$ `Borrowed`) yönetmek için.
- **Decorator Deseni**: Kitap nesnelerine çalışma zamanında ek özellikler (örneğin "Puanlama/Rating" bilgisi) kazandırmak için.
- **Observer Deseni**: Kitap ekleme/silme işlemlerinde ilgili arayüzlerin otomatik olarak yenilenmesi için.

---

#### 📂 Proje Yapısı

Proje, sorumlulukların ayrıldığı (Separation of Concerns) modüler bir yapıya sahiptir:

- **`dao`** (Data Access Object): Veritabanı ile iletişim kuran katman.
    - `BookDAO`: Kitap veritabanı işlemleri.
    - `UserDAO`: Kullanıcı CRUD işlemleri.
    - `LoanDAO`: Ödünç, iade ve geçmiş takibi işlemleri.

- **`model`**: Veri varlıkları.
    - `AbstractUser`: Kullanıcılar için temel sınıf.
    - `AbstractBook`: Kitaplar için temel sınıf.
    - `Student` / `Staff` / `Admin`: Özelleşmiş kullanıcı sınıfları.
    - `Book`: Kitap nesnesi.
    - `Loan`: Ödünç alma kaydı.

- **`pattern`**: Tasarım desenlerinin uygulandığı paketler.
    - `factory`: `UserFactory` sınıfı.
    - `state`: `BookState` arayüzü ve `AvailableState`, `BorrowedState` sınıfları.
    - `strategy`: Arama stratejileri (`TitleSearch`, `AuthorSearch` vb.).
    - `decorator`: `BookComponent` ve `RateableBookDecorator`.
    - `observer`: `IInventoryObserver` ve `InventoryUIObserver`.

- **`service`**: İş mantığı (Business Logic) katmanı.
    - `UserService`: Yetkilendirme ve kullanıcı işlemleri.
    - `BookService`: Kitap durumu ve envanter yönetimi.
    - `LoanService`: Ödünç kuralları ve ceza hesaplama.

- **`ui`**: Swing kullanıcı arayüzü.
    - `LoginView` / `RegisterView`: Giriş ve Kayıt ekranları.
    - `StudentView`: Öğrenci paneli (Kitaplar, Ödünçlerim, Profil).
    - `StaffView`: Personel paneli ana menüsü.
    - `ManageBooksView` / `ManageUsersView`: Yönetim panelleri.
    - `ManageConfigView`: Sistem ayarları.
    - `SearchBooksView`: Detaylı arama ekranı.
    - `UserDetailView`: Personel için üye detay ekranı.

- **`util`**: Yardımcı araçlar.
    - `DatabaseConnection`: JDBC bağlantı yönetimi.
    - `SystemConfig`: Ayarların dosyadan okunup yazılması (`config.properties`).

---

#### 🛠️ Kullanılan Teknolojiler

- **Dil:** Java (JDK 17 veya üzeri önerilir)
- **Arayüz:** Java Swing
- **Veritabanı:** MySQL
- **Bağlantı:** JDBC (MySQL Connector)

---

#### 🚀 Kurulum Talimatları

1. **Gereksinimler**:
    - Java Development Kit (JDK) 17+
    - MySQL Server ve bir veritabanı yönetim aracı (örn. phpMyAdmin, Workbench).

2. **Veritabanı Kurulumu**:
    - Proje klasöründeki `library_db.sql` dosyasını MySQL sunucunuza "Import" edin.
    - `DatabaseConnection.java` dosyasını açın ve yerel ayarlarınızı girin:
      ```java
      // src/com/yourname/library/util/DatabaseConnection.java
      String url = "jdbc:mysql://localhost:3306/library_db?useUnicode=true&characterEncoding=utf8";
      String username = "root"; // Kendi kullanıcı adınız
      String password = "";     // Kendi şifreniz
      ```

3. **Çalıştır**:
    - Projeyi IDE (IntelliJ IDEA önerilir) ile açın.
    - `org.example.Main` sınıfını çalıştırın.

---