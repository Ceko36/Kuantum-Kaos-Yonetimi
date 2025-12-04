public class AntiMadde extends KuantumNesnesi implements IKritik {
    public AntiMadde(String id, double stabilite) {
        super(id, stabilite, 10);
    }

    @Override
    public void analizEt() {
        System.out.println("Evrenin dokusu titriyor...");
        azaltStabilite(25);
    }

    @Override
    public void acilDurumSogutmasi() {
        System.out.println("Anti madde soğutma kalkanı devreye alındı!");
        arttirStabilite(50);
    }
}


