public class KaranlikMadde extends KuantumNesnesi implements IKritik {
    public KaranlikMadde(String id, double stabilite) {
        super(id, stabilite, 7);
    }

    @Override
    public void analizEt() {
        System.out.println("Karanlık madde dalgalanması tespit edildi.");
        azaltStabilite(15);
    }

    @Override
    public void acilDurumSogutmasi() {
        System.out.println("Karanlık madde acil soğutması devrede.");
        arttirStabilite(50);
    }
}


