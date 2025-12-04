import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public class KuantumAmbariApp {
    private static final Random RANDOM = new Random();
    private static int nesneSayaci = 1;

    public static void main(String[] args) {
        List<KuantumNesnesi> envanter = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        try {
            boolean devam = true;
            while (devam) {
                menuYazdir();
                String secim = scanner.nextLine().trim();

                switch (secim) {
                    case "1":
                        KuantumNesnesi yeniNesne = rastgeleNesneUret();
                        envanter.add(yeniNesne);
                        System.out.println("Yeni nesne eklendi: " + yeniNesne.durumBilgisi());
                        break;
                    case "2":
                        envanteriListele(envanter);
                        break;
                    case "3":
                        nesneyiAnalizEt(scanner, envanter);
                        break;
                    case "4":
                        sogutmaIslemi(scanner, envanter);
                        break;
                    case "5":
                        devam = false;
                        System.out.println("Programdan çıkılıyor. İyi şanslar amir!");
                        break;
                    default:
                        System.out.println("Geçersiz seçim. Lütfen 1-5 arası değer girin.");
                }

                System.out.println();
            }
        } catch (KuantumCokusuException ex) {
            System.out.println("SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...");
            System.out.println(ex.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void menuYazdir() {
        System.out.println("KUANTUM AMBARI KONTROL PANELİ");
        System.out.println("1. Yeni Nesne Ekle");
        System.out.println("2. Tüm Envanteri Listele");
        System.out.println("3. Nesneyi Analiz Et");
        System.out.println("4. Acil Durum Soğutması Yap");
        System.out.println("5. Çıkış");
        System.out.print("Seçiminiz: ");
    }

    private static KuantumNesnesi rastgeleNesneUret() {
        double stabilite = 40 + RANDOM.nextDouble() * 60; // 40-100
        int tip = RANDOM.nextInt(3);
        String id = "OBJ-" + String.format("%03d", nesneSayaci++);

        switch (tip) {
            case 0:
                return new VeriPaketi(id, stabilite);
            case 1:
                return new KaranlikMadde(id, stabilite);
            default:
                return new AntiMadde(id, stabilite);
        }
    }

    private static void envanteriListele(List<KuantumNesnesi> envanter) {
        if (envanter.isEmpty()) {
            System.out.println("Envanterde nesne yok.");
            return;
        }

        System.out.println("Aktif envanter:");
        for (KuantumNesnesi nesne : envanter) {
            System.out.println(nesne.durumBilgisi());
        }
    }

    private static void nesneyiAnalizEt(Scanner scanner, List<KuantumNesnesi> envanter) {
        System.out.print("Analiz edilecek nesnenin ID'si: ");
        String id = scanner.nextLine().trim();
        Optional<KuantumNesnesi> hedef = envanter.stream()
                .filter(n -> n.getId().equalsIgnoreCase(id))
                .findFirst();

        if (hedef.isEmpty()) {
            System.out.println("Bu ID ile nesne bulunamadı.");
            return;
        }

        hedef.get().analizEt();
        System.out.println("Analiz tamamlandı. Güncel durum: " + hedef.get().durumBilgisi());
    }

    private static void sogutmaIslemi(Scanner scanner, List<KuantumNesnesi> envanter) {
        System.out.print("Soğutulacak nesnenin ID'si: ");
        String id = scanner.nextLine().trim();
        Optional<KuantumNesnesi> hedef = envanter.stream()
                .filter(n -> n.getId().equalsIgnoreCase(id))
                .findFirst();

        if (hedef.isEmpty()) {
            System.out.println("Bu ID ile nesne bulunamadı.");
            return;
        }

        KuantumNesnesi nesne = hedef.get();
        if (nesne instanceof IKritik kritik) {
            kritik.acilDurumSogutmasi();
            System.out.println("Soğutma tamamlandı. Güncel durum: " + nesne.durumBilgisi());
        } else {
            System.out.println("Bu nesne soğutulamaz!");
        }
    }
}


