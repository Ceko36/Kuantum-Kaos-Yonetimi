'use strict';
const readline = require('readline');

class KuantumCokusuException extends Error {
  constructor(id) {
    super(`Quantum Collapse: ${id}`);
    this.name = 'KuantumCokusuException';
  }
}

class KuantumNesnesi {
  constructor(id, stabilite, tehlikeSeviyesi) {
    if (new.target === KuantumNesnesi) throw new Error('Soyut sınıf.');
    this.id = id;
    this.stabilite = stabilite;
    this.tehlikeSeviyesi = tehlikeSeviyesi;
  }

  get stabilite() {
    return this._stabilite;
  }

  set stabilite(deger) {
    const temiz = Math.max(0, Math.min(100, Number(deger)));
    this._stabilite = Number.isNaN(temiz) ? 0 : temiz;
  }

  _azalt(miktar) {
    this.stabilite = this.stabilite - miktar;
    if (this.stabilite <= 0) throw new KuantumCokusuException(this.id);
  }

  durumBilgisi() {
    return `${this.id} | Stabilite: ${this.stabilite.toFixed(1)} | Tehlike: ${this.tehlikeSeviyesi}`;
  }

  analizEt() {
    throw new Error('AnalizEt uygulanmalı.');
  }
}

class VeriPaketi extends KuantumNesnesi {
  analizEt() {
    console.log('Veri içeriği okundu.');
    this._azalt(5);
  }
}

class KaranlikMadde extends KuantumNesnesi {
  analizEt() {
    this._azalt(15);
  }

  acilDurumSogutmasi() {
    this.stabilite = Math.min(100, this.stabilite + 50);
  }
}

class AntiMadde extends KuantumNesnesi {
  analizEt() {
    console.log('Evrenin dokusu titriyor...');
    this._azalt(25);
  }

  acilDurumSogutmasi() {
    this.stabilite = Math.min(100, this.stabilite + 50);
  }
}

const rl = readline.createInterface({ input: process.stdin, output: process.stdout });
const sor = (yazi) => new Promise((res) => rl.question(yazi, res));
const envanter = [];

const rastgeleNesne = () => {
  const tipler = [
    { cls: VeriPaketi, stab: 90, tehlike: 2 },
    { cls: KaranlikMadde, stab: 70, tehlike: 7 },
    { cls: AntiMadde, stab: 60, tehlike: 10 },
  ];
  const secim = tipler[Math.floor(Math.random() * tipler.length)];
  const id = `Q-${Math.random().toString(36).slice(2, 6).toUpperCase()}`;
  return new secim.cls(id, secim.stab, secim.tehlike);
};

const kritikMi = (nesne) => typeof nesne.acilDurumSogutmasi === 'function';

const fatal = (err) => {
  if (err instanceof KuantumCokusuException) {
    console.log('SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...');
    rl.close();
    process.exit(0);
  }
  throw err;
};

const menu = `
KUANTUM AMBARI KONTROL PANELİ
1. Yeni Nesne Ekle
2. Tüm Envanteri Listele
3. Nesneyi Analiz Et
4. Acil Durum Soğutması Yap
5. Çıkış
`;

async function main() {
  while (true) {
    console.log(menu);
    const secim = (await sor('Seçiminiz: ')).trim();
    try {
      if (secim === '1') {
        const nesne = rastgeleNesne();
        envanter.push(nesne);
        console.log(`${nesne.id} eklendi.`);
      } else if (secim === '2') {
        if (!envanter.length) console.log('Envanter boş.');
        envanter.forEach((n) => console.log(n.durumBilgisi()));
      } else if (secim === '3') {
        const id = await sor('Analiz edilecek ID: ');
        const nesne = envanter.find((n) => n.id === id.trim());
        if (!nesne) console.log('Nesne yok.');
        else nesne.analizEt();
      } else if (secim === '4') {
        const id = await sor('Soğutulacak ID: ');
        const nesne = envanter.find((n) => n.id === id.trim());
        if (!nesne) console.log('Nesne yok.');
        else if (!kritikMi(nesne)) console.log('Bu nesne soğutulamaz!');
        else {
          nesne.acilDurumSogutmasi();
          console.log('Soğutma tamamlandı.');
        }
      } else if (secim === '5') {
        console.log('Vardiya sona erdi.');
        rl.close();
        break;
      } else {
        console.log('Geçersiz seçim.');
      }
    } catch (err) {
      fatal(err);
    }
  }
}

main().catch((err) => {
  console.error(err);
  rl.close();
});