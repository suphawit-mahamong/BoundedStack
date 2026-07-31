import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test runner 
 */
public class Boundedstacktest {

    private static int passed = 0;
    private static int failed = 0;

    /** helper กลาง — พิมพ์ PASS/FAIL และนับผลให้เอง */
    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("[PASS] " + name);
        } else {
            failed++;
            System.out.println("[FAIL] " + name);
        }
    }

    public static void main(String[] args) {
        boolean assertsOn = false;
        assert assertsOn = true;
        if (!assertsOn) {
            System.out.println("WARNING: assertions disabled"
                    + " - re-run with: java -ea BoundedstackTest\n");
        }

        System.out.println("=== Boundedstack Test Suite ===\n");

        testCreators();
        testAdd();
        testRemove();
        testObservers();
        testProducer();
        testExposure();

        System.out.println("\n=== Summary ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total : " + (passed + failed));
        System.out.println(failed == 0 ? "ALL TESTS PASSED" : "SOME TESTS FAILED");

        if (failed > 0) {
            System.exit(1);
        }
    }

    // --- Partition: ว่าง / มีหนัง / input ที่ผิดเงื่อนไข ---
    private static void testCreators() {
        System.out.println("-- Creators --");

        Boundedstack c = new Boundedstack();
        check("new() -> empty", c.size() == 0);
        check("new() -> contains nothing", !c.contains("anything"));

        Boundedstack p = new Boundedstack(Arrays.asList("Inception", "Titanic", "ธี่หยด"));
        check("new(list) -> size 3", p.size() == 3);
        check("new(list) -> contains B", p.contains("Titanic"));
        check("new(list) -> preserves order",
            p.movies().equals(Arrays.asList("Inception", "Titanic", "ธี่หยด")));
    }

    // --- Mutator: add ต้องรักษาลำดับและกันหนังซ้ำ ---
    private static void testAdd() {
        System.out.println("\n-- Add --");

        Boundedstack ad = new Boundedstack();
        check("add(A) -> size 1", ad.size() == 1);
        check("add(Car 3) -> returns true", ad.add("Car 3"));
        check("add(ธี่หยด) -> returns true" , ad.add("ธี่หยด"));

        // เพลงซ้ำไม่ใช่ error — คืน false เฉย ๆ
        check("add duplicate -> returns false", !ad.add("Inception"));
        check("failed add leaves size unchanged", ad.size() == 3);

        // input ที่ผิดเงื่อนไขต้องโยน exception
        boolean threwEmpty = false;
        try {
            ad.add("");
        } catch (IllegalArgumentException e) {
            threwEmpty = true;
        }
        check("add(empty string) -> throws IllegalArgumentException", threwEmpty);

        boolean threwNull = false;
        try {
            ad.add(null);
        } catch (IllegalArgumentException e) {
            threwNull = true;
        }
        check("add(null) -> throws IllegalArgumentException", threwNull);

        check("failed adds leave Boundedstack unchanged", ad.size() == 3);

        // boundary: เติมจนเต็มพอดีแล้วเติมเพิ่ม
        Boundedstack full = new Boundedstack();
        for (int i = 0; i < Boundedstack.MAX_MOVIES; i++) {
            full.add("song" + i);
        }
        check("can fill up to MAX_MOVIES", full.size() == Boundedstack.MAX_MOVIES);
        check("add when full -> returns false", !full.add("one more"));
        check("full Boundedstack stays at MAX_MOVIES",
                full.size() == Boundedstack.MAX_MOVIES);
    }

    // --- Mutator: remove ทั้งกรณีพบและไม่พบ ---
    private static void testRemove() {
        System.out.println("\n-- Remove --");

        Boundedstack re = new Boundedstack(Arrays.asList("Inception", "Titanic", "ธี่หยด"));
        check("remove(Titanic) -> returns true", re.remove("Titanic"));
        check("remove -> size decreases", re.size() == 2);
        check("remove -> song is gone", !re.contains("B"));
        check("remove keeps the others in order",
                re.movies().equals(Arrays.asList("Inception", "ธี่หยด")));
        
        // ลบเพลงที่ไม่มีไม่ใช่ error — คืน false เฉย ๆ
        check("remove missing song -> returns false", !re.remove("nope"));
        check("failed remove leaves size unchanged", re.size() == 2);

        // boundary: ลบจนหมด
        re.remove("Inception");
        re.remove("ธี่หยด");
        check("remove all -> empty", re.size() == 0);
        check("remove on empty Boundedstack -> returns false", !re.remove("Inception"));
    }

    // --- Observer ต้องไม่มี side effect ---
    private static void testObservers() {
        System.out.println("\n-- Observers --");

        Boundedstack s = new Boundedstack(Arrays.asList("Inception", "Titanic"));
        check("size reports 2", s.size() == 2);
        check("contains finds an existing movie", s.contains("Inception"));
        check("contains rejects a missing movie", !s.contains("Z"));
        check("movies returns the full list in order",
                s.movies().equals(Arrays.asList("Inception", "Titanic")));

        int before = s.size();
        s.size();
        s.contains("A");
        s.movies();
        check("observers have no side effects", s.size() == before);
    }

    // --- Producer ต้องคืนตัวใหม่ ไม่แก้ตัวเดิม ---
    private static void testProducer() {
        System.out.println("\n-- Producer (shuffled) --");

        Boundedstack original = new Boundedstack(Arrays.asList("Inception", "Titanic", "ธี่หยด", "Car 3"));
        Boundedstack shuffled = original.shuffled();

        check("shuffled has the same size", shuffled.size() == original.size());

        List<String> a = new ArrayList<String>(original.movies());
        List<String> b = new ArrayList<String>(shuffled.movies());
        Collections.sort(a);
        Collections.sort(b);
        check("shuffled contains exactly the same movies", a.equals(b));

        check("shuffled does not mutate the original",
                original.movies().equals(Arrays.asList("Inception", "Titanic", "ธี่หยด", "Car 3")));

        // mutate ตัวใหม่ต้องไม่กระทบตัวเดิม
        shuffled.add("E");
        check("mutating the result does not affect the original",
                original.size() == 4);

        // boundary: shuffle เพลย์ลิสต์ว่างต้องไม่พัง
        Boundedstack emptyShuffled = new Boundedstack();
        check("shuffling an empty Boundedstack is safe", emptyShuffled.size() == 0);

        System.out.println("\n-- Producer (sorted) --");
        //เคสปกติ: หนังไม่เรียงมาแต่แรก ต้องเรียงให้ถูก
        Boundedstack originaled = new Boundedstack(Arrays.asList("Inception", "Titanic", "ธี่หยด", "Car 3"));
        Boundedstack sorted = originaled.sorted();
        check("sorted returns movies in ascending order",
            sorted.movies().equals(Arrays.asList("Inception", "Titanic", "ธี่หยด")));
            // 2. ขนาดต้องเท่าเดิม
        check("sorted has the same size", sorted.size() == original.size());

        // 3. ต้องมีหนังครบชุดเดิม ไม่ขาดไม่เกิน
        List<String> as = new ArrayList<String>(original.movies());
        List<String> bs = new ArrayList<String>(sorted.movies());
        Collections.sort(as);
        Collections.sort(bs);
        check("sorted contains exactly the same movies", a.equals(b));

        // 4. ต้นฉบับต้องไม่ถูกแก้ไข (ยังเป็นลำดับเดิมก่อนเรียก sorted())
        check("sorted does not mutate the original",
            original.movies().equals(Arrays.asList("ธี่หยด", "Inception", "Titanic")));

        // 5. แก้ตัวใหม่ (ผลลัพธ์จาก sorted) ต้องไม่กระทบตัวเดิม
        sorted.add("Zootopia");
        check("mutating the result does not affect the original", original.size() == 3);

        // 6. เพลย์ลิสต์ที่เรียงอยู่แล้ว -> ผลลัพธ์ต้องเหมือนเดิม
        Boundedstack alreadySorted = new Boundedstack(Arrays.asList("Avatar", "Batman", "Zootopia"));
        check("sorted on already-sorted list stays the same",
            alreadySorted.sorted().movies().equals(Arrays.asList("Avatar", "Batman", "Zootopia")));

        // 7. เพลย์ลิสต์เรียงกลับด้าน (Z-A) -> ต้องกลับมาเป็น A-Z
        Boundedstack reverseOrder = new Boundedstack(Arrays.asList("Zootopia", "Batman", "Avatar"));
        check("sorted reverses a descending list",
            reverseOrder.sorted().movies().equals(Arrays.asList("Avatar", "Batman", "Zootopia")));

        // 8. boundary: เพลย์ลิสต์ว่าง -> ไม่พัง คืนค่าว่าง
        Boundedstack empty = new Boundedstack();
        check("sorting an empty Boundedstack is safe", empty.sorted().size() == 0);

        // 9. boundary: มีหนังเรื่องเดียว -> คืนค่าเดิม ไม่พัง
        Boundedstack single = new Boundedstack(Arrays.asList("Inception"));
        check("sorting a single-movie Boundedstack returns itself unchanged",
            single.sorted().movies().equals(Arrays.asList("Inception")));

        // 10. Representation exposure: ผลลัพธ์ต้องเป็น object คนละตัวจากต้นฉบับ
        check("sorted returns a different object from the original",
            sorted != original);

        // 11. แก้ list ที่ได้จาก sorted().movies() ต้องไม่กระทบ Boundedstack ใหม่
        List<String> gotFromSorted = sorted.movies();
        gotFromSorted.clear();
        check("clearing result of sorted().movies() does not affect sorted",
            sorted.size() != 0);    
    }

    // --- ทดสอบว่าไม่เกิด representation exposure ---
    private static void testExposure() {
        System.out.println("\n-- Representation Exposure --");

        // ขาออก: แก้ list ที่ได้จาก movies() ต้องไม่กระทบ rep
        Boundedstack s = new Boundedstack();
        s.add("Inception");

        List<String> got = s.movies();
        got.clear();
        check("clearing result of movies() does not affect Boundedstack",
                s.size() == 1);

        got = s.movies();
        got.add("injected");
        check("adding to result of movies() does not affect Boundedstack",
                s.size() == 1 && !s.contains("injected"));

        // สองครั้งต้องเป็นคนละ object
        check("movies() returns a fresh list each call",
                s.movies() != s.movies());

        // ขาเข้า: แก้ list ที่ส่งให้ constructor ต้องไม่กระทบ rep
        List<String> input = new ArrayList<String>(Arrays.asList("Inception", "Titanic"));
        Boundedstack p = new Boundedstack(input);

        input.clear();
        check("clearing constructor argument does not affect Boundedstack",
                p.size() == 2);

        input.add("injected");
        check("adding to constructor argument does not affect Boundedstack",
                !p.contains("injected"));
    }
}
