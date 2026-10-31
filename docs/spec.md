# Házifeladat Specifikáció: Állatkert Nyilvántartó Rendszer
### Vadoba Hunor (RF50UI)

## 1. Feladat rövid szöveges ismertetése

Ez a program egy asztali Java Swing alapú alkalmazás, amely egy virtuális állatkert lakóinak nyilvántartását, állapotkezelését és raktárkészletét mutatja be. A szoftver célja, hogy áttekinthető, grafikus felületen biztosítsa a különböző állattípusok adatainak rögzítését, módosítását, törlését, szűrését, valamint az állatok éhségi állapotának és etetésének kezelését.

A felhasználók különböző kategóriájú állatokat (emlősök, madarak, hüllők) vehetnek fel a rendszerbe. Minden állat rendelkezik alapvető attribútumokkal (név, kor, súly, állapot, utolsó etetés időpontja), míg a leszármazott osztályok saját, egyedi tulajdonságokkal és speciális táplálkozási igénnyel bírnak (pl. szőrzet típusa, szárnyfesztávolság, mérgező-e). Az alkalmazás része egy Raktár modul is, amely nyilvántartja a különböző állatfajtáknak megfelelő eledeleket.
---

## 2. Use-case-ek felsorolása és kifejtése

- **Új állat rögzítése**
  - A felhasználó a menüből vagy gombnyomással megnyit egy űrlapot, kiválasztja az állat típusát (Emlős, Madár, Hüllő), kitölti az adatait, majd menti. Az elem bekerül a memóriába és a táblázatba.
- **Állatok listázása, szűrése és állapotának megtekintése**
  - A főablak táblázatában látható az összes állat, beleértve azok aktuális állapotát (`BOLDOG`, `ÉHES`, `MEGHALT`). A felhasználó szűrhet a listára típus vagy állapot alapján.
- **Állat etetése és állapotfrissítése**
  - A felhasználó kiválaszt egy állatot a táblázatból és az „Etetés” gombra kattint. A rendszer ellenőrzi, hogy a Raktárban van-e az adott állatfajtának megfelelő eledel. Sikeres etetés esetén a raktárkészlet csökken, az állat állapota `BOLDOG`-ra vált, az utolsó etetés időbélyege pedig frissül az aktuális időre.
- **Raktárkezelés és feltöltés**
  - A felhasználó megtekintheti az elérhető eledelek mennyiségét (Hús/Tej emlősöknek, Magvak madaraknak, Élő táplálék hüllőknek), és gombnyomással feltöltheti a készleteket.
- **Adatok mentése fájlba (Szerializáció)**
  - A menüből (`Fájl -> Mentés`) a rendszer a teljes objektum-grafot (állatok listája és a raktár állapota) kiírja bináris fájlba (`allatok.dat`) a Java `ObjectOutputStream` segítségével.
- **Adatok betöltése fájlból**
  - Korábban elmentett bináris fájl megnyitásával (`Fájl -> Betöltés`) az adatok visszatölthetők a memóriába a `ObjectInputStream` használatával.

---

## 3. Megoldási ötlet vázlatos ismertetése

### 3.1. Technológiai megoldások
* **Nyelv és keretrendszer:** Java 17+, Swing (`JFrame`, `JTable`, `JComboBox`, `JMenuBar`, `JOptionPane`).
* **Adatszerkezet:** `ArrayList<Allat>` a memóriában történő tárolásra.
* **Fájlkezelés:** Beépített Java bináris szerializáció (`java.io.Serializable`, `ObjectOutputStream`, `ObjectInputStream`). Fájl kiterjesztése: `.dat`.
* **Időkezelés:** `java.time.LocalDateTime` az etetési időbélyegek eltelt idejének mérésére.
* **Tesztelés:** JUnit 5 keretrendszer.

### 3.2. Osztályhierarchia és funkcionális működés (OOP)

* **`Allapot` (Enum):** `BOLDOG`, `ÉHES`, `MEGHALT`.
* **`EledelTipus` (Enum):** `HUS` (Emlős), `MAG` (Madár), `ROVAR` (Hüllő).
* **`Allat` (Absztrakt ősosztály):** 
  * Mezők: `nev`, `kor`, `suly`, `utolsoEtetesIdopont`, `allapot`.
  * Metódusok: `frissitAllapot()` (az aktuális idő és az utolsó etetés közötti időkülönbség alapján frissíti az állapotot `BOLDOG` -> `ÉHES` -> `MEGHALT`), `getSzuksegesEledel()`, `etetes(Raktar raktar)`.
* **`Emlos` / `Madar` / `Hullo` (Leszármazottak):**
  * Egyedi mezők: pl. szőrtípus, szárnyfesztávolság, mérgező-e.
  * Egyedi viselkedés: Megvalósítják a `getSzuksegesEledel()` metódust (a saját eledeltípusukat adják vissza) és felüldefiniálják az etetés visszajelzését/logikáját.
* **`Raktar`:**
  * Felelős az eledelek mennyiségének nyilvántartásáért (`HashMap<EledelTipus, Integer>`). Műveletek: `csokkentKeszlet()`, `feltoltKeszlet()`, `vanElegEledel()`.
* **`MenhelyModell`:** Üzleti logika, hozzáadás, törlés, szűrés, időalapú állapotfrissítések, fájlkezelés (szerializáció/deszerializáció).
* **`AllatGUI`:** Swing alapú grafikus felület, táblázatos nézettel, raktárellenőrző panellel és etetés gombbal.

### 3.3. Szerializált fájlstruktúra és működés
A mentés során a Java objektum-szerializációját használjuk, így bináris adatfolyamként íródnak ki az objektumok.