public abstract class KuantumNesnesi {
    private final String id;
    private double stabilite;
    private final int tehlikeSeviyesi;

    protected KuantumNesnesi(String id, double stabilite, int tehlikeSeviyesi) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID boş olamaz.");
        }
        if (tehlikeSeviyesi < 1 || tehlikeSeviyesi > 10) {
            throw new IllegalArgumentException("Tehlike seviyesi 1-10 arasında olmalıdır.");
        }
        this.id = id;
        setStabilite(stabilite);
        this.tehlikeSeviyesi = tehlikeSeviyesi;
    }

    public String getId() {
        return id;
    }

    public double getStabilite() {
        return stabilite;
    }

    public int getTehlikeSeviyesi() {
        return tehlikeSeviyesi;
    }

    protected void azaltStabilite(double miktar) {
        if (miktar < 0) {
            throw new IllegalArgumentException("Azaltma miktarı negatif olamaz.");
        }
        double yeniDeger = stabilite - miktar;
        if (yeniDeger <= 0) {
            stabilite = 0;
            throw new KuantumCokusuException("Kuantum çöküşü! Patlayan ID: " + id);
        }
        setStabilite(yeniDeger);
    }

    protected void arttirStabilite(double miktar) {
        if (miktar < 0) {
            throw new IllegalArgumentException("Artış miktarı negatif olamaz.");
        }
        double yeniDeger = Math.min(100.0, stabilite + miktar);
        setStabilite(yeniDeger);
    }

    private void setStabilite(double deger) {
        if (deger < 0 || deger > 100) {
            throw new IllegalArgumentException("Stabilite 0-100 arasında olmalıdır.");
        }
        this.stabilite = deger;
    }

    public String durumBilgisi() {
        return "ID: " + id + " | Stabilite: " + String.format("%.1f", stabilite);
    }

    public abstract void analizEt();
}


