public class VeriPaketi extends KuantumNesnesi {
    public VeriPaketi(String id, double stabilite) {
        super(id, stabilite, 2);
    }

    @Override
    public void analizEt() {
        System.out.println("Veri içeriği okundu.");
        azaltStabilite(5);
    }
}


