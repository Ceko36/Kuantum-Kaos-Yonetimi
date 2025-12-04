using System;
using System.Collections.Generic;

abstract class KuantumNesnesi
{
    private double stabilite;

    protected KuantumNesnesi(string id, double baslangicStabilite, int tehlikeSeviyesi)
    {
        Id = id ?? throw new ArgumentNullException(nameof(id));
        TehlikeSeviyesi = tehlikeSeviyesi;
        Stabilite = baslangicStabilite;
    }

    public string Id { get; }

    public double Stabilite
    {
        get => stabilite;
        protected set
        {
            if (value < 0)
            {
                stabilite = 0;
                throw new KuantumCokusuException(Id);
            }

            stabilite = value > 100 ? 100 : value;
        }
    }

    public int TehlikeSeviyesi { get; protected set; }

    public abstract void AnalizEt();

    public virtual string DurumBilgisi()
    {
        return $"{Id} | Stabilite: {Stabilite:F1} | Tehlike: {TehlikeSeviyesi}";
    }

    protected void StabiliteAzalt(double miktar)
    {
        if (miktar < 0)
        {
            throw new ArgumentException("Azaltma miktarı negatif olamaz.", nameof(miktar));
        }

        Stabilite -= miktar;
    }

    protected void StabiliteArtir(double miktar)
    {
        if (miktar < 0)
        {
            throw new ArgumentException("Artırma miktarı negatif olamaz.", nameof(miktar));
        }

        Stabilite += miktar;
    }
}

interface IKritik
{
    void AcilDurumSogutmasi();
}

class VeriPaketi : KuantumNesnesi
{
    public VeriPaketi(string id)
        : base(id, 100, 1)
    {
    }

    public override void AnalizEt()
    {
        StabiliteAzalt(5);
        Console.WriteLine("Veri içeriği okundu.");
    }
}

class KaranlikMadde : KuantumNesnesi, IKritik
{
    public KaranlikMadde(string id)
        : base(id, 85, 7)
    {
    }

    public override void AnalizEt()
    {
        StabiliteAzalt(15);
        Console.WriteLine("Karanlık madde dalgalanmaları analiz edildi.");
    }

    public void AcilDurumSogutmasi()
    {
        StabiliteArtir(50);
        Console.WriteLine("Karanlık madde soğutuldu.");
    }
}

class AntiMadde : KuantumNesnesi, IKritik
{
    public AntiMadde(string id)
        : base(id, 70, 10)
    {
    }

    public override void AnalizEt()
    {
        Console.WriteLine("Evrenin dokusu titriyor...");
        StabiliteAzalt(25);
    }

    public void AcilDurumSogutmasi()
    {
        StabiliteArtir(50);
        Console.WriteLine("Anti madde karantina alanı soğutuldu.");
    }
}

class KuantumCokusuException : Exception
{
    public KuantumCokusuException(string nesneId)
        : base($"Kuantum çöküşü! Patlayan nesne: {nesneId}")
    {
    }
}

class Program
{
    private static readonly Random Random = new();
    private static readonly List<KuantumNesnesi> Envanter = new();
    private static int sayac = 1;

    static void Main()
    {
        try
        {
            while (true)
            {
                MenuGoster();
                Console.Write("Seçiminiz: ");
                var secim = Console.ReadLine();

                Console.WriteLine();

                switch (secim)
                {
                    case "1":
                        YeniNesneEkle();
                        break;
                    case "2":
                        EnvanteriListele();
                        break;
                    case "3":
                        NesneAnalizEt();
                        break;
                    case "4":
                        AcilDurumSogutmasi();
                        break;
                    case "5":
                        Console.WriteLine("Vardiya sonlandırılıyor...");
                        return;
                    default:
                        Console.WriteLine("Geçersiz seçim, tekrar deneyin.");
                        break;
                }

                Console.WriteLine();
            }
        }
        catch (KuantumCokusuException ex)
        {
            Console.WriteLine(ex.Message);
            Console.WriteLine("SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...");
        }
    }

    private static void MenuGoster()
    {
        Console.WriteLine("KUANTUM AMBARI KONTROL PANELİ");
        Console.WriteLine("1. Yeni Nesne Ekle");
        Console.WriteLine("2. Tüm Envanteri Listele");
        Console.WriteLine("3. Nesneyi Analiz Et");
        Console.WriteLine("4. Acil Durum Soğutması Yap");
        Console.WriteLine("5. Çıkış");
    }

    private static void YeniNesneEkle()
    {
        int tur = Random.Next(0, 3);
        string id = $"Q-{sayac++:D4}";

        KuantumNesnesi nesne = tur switch
        {
            0 => new VeriPaketi(id),
            1 => new KaranlikMadde(id),
            _ => new AntiMadde(id)
        };

        Envanter.Add(nesne);
        Console.WriteLine($"{nesne.GetType().Name} ({nesne.Id}) envantere eklendi.");
    }

    private static void EnvanteriListele()
    {
        if (Envanter.Count == 0)
        {
            Console.WriteLine("Envanter boş.");
            return;
        }

        Console.WriteLine("=== DURUM RAPORU ===");
        foreach (var nesne in Envanter)
        {
            Console.WriteLine(nesne.DurumBilgisi());
        }
    }

    private static void NesneAnalizEt()
    {
        var nesne = NesneSec("Analiz edilecek nesnenin ID'si: ");
        if (nesne == null)
        {
            return;
        }

        nesne.AnalizEt();
    }

    private static void AcilDurumSogutmasi()
    {
        var nesne = NesneSec("Soğutulacak nesnenin ID'si: ");
        if (nesne == null)
        {
            return;
        }

        if (nesne is IKritik kritikNesne)
        {
            kritikNesne.AcilDurumSogutmasi();
        }
        else
        {
            Console.WriteLine("Bu nesne soğutulamaz!");
        }
    }

    private static KuantumNesnesi? NesneSec(string mesaj)
    {
        if (Envanter.Count == 0)
        {
            Console.WriteLine("Envanter boş.");
            return null;
        }

        Console.WriteLine("Aktif Nesneler:");
        foreach (var aktifNesne in Envanter)
        {
            Console.WriteLine($"- {aktifNesne.DurumBilgisi()}");
        }

        Console.Write(mesaj);
        var id = Console.ReadLine();
        var nesne = Envanter.Find(n => n.Id.Equals(id, StringComparison.OrdinalIgnoreCase));

        if (nesne == null)
        {
            Console.WriteLine("Nesne bulunamadı.");
        }

        return nesne;
    }
}
