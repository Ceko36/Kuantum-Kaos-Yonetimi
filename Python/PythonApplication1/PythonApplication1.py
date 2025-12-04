import random
import sys
import uuid
from abc import ABC, abstractmethod
from typing import List, Optional


class KuantumCokusuException(Exception):
    """Raised when a quantum object's stability reaches zero."""

    def __init__(self, nesne_id: str):
        super().__init__(f"Kuantum çöküşü! Patlayan nesne: {nesne_id}")
        self.nesne_id = nesne_id


class KuantumNesnesi(ABC):
    def __init__(self, nesne_id: str, stabilite: float, tehlike_seviyesi: int):
        self._id = nesne_id
        self.stabilite = stabilite
        self._tehlike_seviyesi = tehlike_seviyesi

    @property
    def id(self) -> str:
        return self._id

    @property
    def stabilite(self) -> float:
        return self._stabilite

    @stabilite.setter
    def stabilite(self, value: float) -> None:
        if not 0 <= value <= 100:
            raise ValueError("Stabilite 0 ile 100 arasında olmalıdır.")
        self._stabilite = value

    @property
    def tehlike_seviyesi(self) -> int:
        return self._tehlike_seviyesi

    def _azalt_stabilite(self, miktar: float) -> None:
        yeni_deger = max(self.stabilite - miktar, 0)
        self._stabilite = yeni_deger
        if yeni_deger <= 0:
            raise KuantumCokusuException(self.id)

    def durum_bilgisi(self) -> str:
        return f"ID: {self.id} | Stabilite: {self.stabilite:.1f}"

    @abstractmethod
    def analiz_et(self) -> None:
        ...


class IKritik(ABC):
    @abstractmethod
    def acil_durum_sogutmasi(self) -> None:
        ...


class VeriPaketi(KuantumNesnesi):
    def analiz_et(self) -> None:
        self._azalt_stabilite(5)
        print("Veri içeriği okundu.")


class KaranlikMadde(KuantumNesnesi, IKritik):
    def analiz_et(self) -> None:
        self._azalt_stabilite(15)

    def acil_durum_sogutmasi(self) -> None:
        self.stabilite = min(self.stabilite + 50, 100)
        print("Karanlık madde soğutuldu.")


class AntiMadde(KuantumNesnesi, IKritik):
    def analiz_et(self) -> None:
        print("Evrenin dokusu titriyor...")
        self._azalt_stabilite(25)

    def acil_durum_sogutmasi(self) -> None:
        self.stabilite = min(self.stabilite + 50, 100)
        print("Anti madde soğutuldu.")


def _rastgele_id() -> str:
    return uuid.uuid4().hex[:8].upper()


def _rastgele_stabilite() -> float:
    return random.uniform(50, 100)


def _rastgele_tehlike() -> int:
    return random.randint(1, 10)


def yeni_nesne_uret() -> KuantumNesnesi:
    secenekler = [VeriPaketi, KaranlikMadde, AntiMadde]
    sinif = random.choice(secenekler)
    return sinif(_rastgele_id(), _rastgele_stabilite(), _rastgele_tehlike())


def envanteri_listele(envanter: List[KuantumNesnesi]) -> None:
    if not envanter:
        print("Envanter boş.")
        return
    for nesne in envanter:
        print(nesne.durum_bilgisi())


def nesne_bul(
    envanter: List[KuantumNesnesi], nesne_id: str
) -> Optional[KuantumNesnesi]:
    for nesne in envanter:
        if nesne.id == nesne_id:
            return nesne
    return None


def menuyu_yazdir() -> None:
    print(
        "\nKUANTUM AMBARI KONTROL PANELİ\n"
        "1. Yeni Nesne Ekle\n"
        "2. Tüm Envanteri Listele\n"
        "3. Nesneyi Analiz Et\n"
        "4. Acil Durum Soğutması Yap\n"
        "5. Çıkış"
    )


def main() -> None:
    random.seed()
    envanter: List[KuantumNesnesi] = []
    while True:
        menuyu_yazdir()
        try:
            secim = input("Seçiminiz: ").strip()
        except EOFError:
            print("\nGirdi alınamadı, program sonlandırılıyor...")
            break

        try:
            if secim == "1":
                nesne = yeni_nesne_uret()
                envanter.append(nesne)
                print(f"Yeni nesne eklendi: {nesne.durum_bilgisi()}")
            elif secim == "2":
                envanteri_listele(envanter)
            elif secim == "3":
                hedef_id = input("Analiz edilecek nesnenin ID'si: ").strip().upper()
                hedef = nesne_bul(envanter, hedef_id)
                if not hedef:
                    print("Nesne bulunamadı.")
                    continue
                hedef.analiz_et()
                print(hedef.durum_bilgisi())
            elif secim == "4":
                hedef_id = input("Soğutulacak nesnenin ID'si: ").strip().upper()
                hedef = nesne_bul(envanter, hedef_id)
                if not hedef:
                    print("Nesne bulunamadı.")
                    continue
                if isinstance(hedef, IKritik):
                    hedef.acil_durum_sogutmasi()
                    print(hedef.durum_bilgisi())
                else:
                    print("Bu nesne soğutulamaz!")
            elif secim == "5":
                print("Program sonlandırılıyor...")
                break
            else:
                print("Geçersiz seçim.")
        except KuantumCokusuException as hata:
            print(str(hata))
            print("SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...")
            sys.exit(1)
        except ValueError as hata:
            print(f"Hatalı veri: {hata}")


if __name__ == "__main__":
    main()

