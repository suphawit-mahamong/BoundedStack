import java.util.*;

//น.ส.สุกฤตา วงษ์กาวิน 6821601551
//นายศุภวิชญ์ มหามงคลชัย 6821601518

/**
 * 
 * Boundedstack — ADT แทนรายการหนังที่ผู้ใช้จัดลำดับไว้
 * ค่านามธรรม (A): ลำดับของเพลง เช่น [หนังA, หนังB, หนังC]
 * 
 */
public class Boundedstack {

    public static final int MAX_MOVIES = 100;

    // ===== representation =====
    private final List<String> movies;
    
    //AF(movies,capacity) =
    //RI
    //
    //
    // TODO 1: เขียน Abstraction Function ตรงนี้
    // Abstraction Function:
    //   AF(movies) = เพลย์ลิสต์ที่เล่นเพลง movies.get(0), movies.get(1), ... ตามลำดับ

    // TODO 2: เขียน Representation Invariant ตรงนี้ (4 ข้อ)
    // Representation Invariant:
    //   song != null
    //   ไม่มีเพลงใดเป็น null
    //   ไม่มีชื่อเพลงที่เป็นสตริงว่าง
    //   ชื่อเพลงห้ามซ้ำกัน
    //   MAX_SONGS <= 100

    // TODO 3: เขียน Safety from rep exposure ตรงนี้
    // Safety from rep exposure:
    //  ให้ song เป็น final
    //  คัดลอกทั้งขาเข้าและขาขาออก

    /**
     * TODO 4: เขียน checkRep()
     * แปลง RI ทุกข้อเป็น assert หนึ่งบรรทัด พร้อมข้อความอธิบาย
    */
    private void checkRep(){
        assert movies != null : "songs not null!!";
        assert movies.size() <= MAX_MOVIES ;
        Set<String> seen = new HashSet<>();
        for (String s : movies) {
            assert s != null;
            assert !(s=="");
            assert seen.add(s) : "duplicate: " + s;
        }
    }
    // ===== Creator =====
    /**
     * สร้างเพลย์ลิสต์ว่าง
     * @param capacity
     */
    public Boundedstack() {
        this.movies = new ArrayList<>();
        checkRep();
    }
     /**
     * TODO 5: Creator ตัวที่สอง
     * สร้างเพลย์ลิสต์จากรายชื่อหนังที่ให้มา
     *
     * ระวัง: ห้ามเก็บ reference ของ initial ตรง ๆ (rep exposure!)
     *
     * @param initial รายชื่อหนังเริ่มต้น ต้องไม่ซ้ำและไม่เกิน MAX_MOVIES
     * @throws IllegalArgumentException ถ้า initial ผิดเงื่อนไข
     */
    
    public Boundedstack(List<String> initial) {
        this.movies = new ArrayList<>(initial);   // แก้บรรทัดนี้
        // เขียนโค้ดตรงนี้
    }

    // ===== Mutators =====
    
    /**
     * 
     * 
     * @param S
     */
    public void push(String S){
    //ตรงนี้กลับมาดูคืออะไร
    }

     /**
     * TODO 6: เพิ่มชื่อหนังลงในเพลย์ลิสต์
     *
     * @param movie ชื่อหนัง ต้องไม่เป็น null และไม่เป็นสตริงว่าง
     * @return true ถ้าเพิ่มสำเร็จ, false ถ้ามีหนังนี้อยู่แล้วหรือเต็มแล้ว
     * @throws IllegalArgumentException ถ้า movie เป็น null หรือสตริงว่าง
     */
    public boolean add(String movie) {
        if (movie == null || movie =="") throw new IllegalArgumentException();
        if (movies.contains(movie) || movies.size()==MAX_MOVIES) return false ;
        movies.add(movie);
        checkRep();
        return true;   // แก้บรรทัดนี้
    }

    /**
     * TODO 7: ลบชื่อหนังออกจากเพลย์ลิสต์
     *
     * @param movie ชื่อหนังที่ต้องการลบ
     * @return true ถ้าลบสำเร็จ, false ถ้าไม่พบหนังเรื่องนี้
     */
    public boolean remove(String movie) {
        if (   !movies.contains(movie) ) return false; 
        movies.remove(movie);
        checkRep();
        return true;   // แก้บรรทัดนี้

    }

    // ===== Observers =====

    /**
     * TODO 8: คืนค่ารายชื่อหนังทั้งหมด
     */
    public int size() {
        return movies.size();   // แก้บรรทัดนี้
    }

    /**
     * TODO 9: ตรวจว่ามีหนังเรื่องนี้อยู่หรือไม่
     */
    public boolean contains(String movie) {

        return movies.contains(movie);   // แก้บรรทัดนี้
    }
      /**
     * TODO 10: คืนรายชื่อเพลงทั้งหมดตามลำดับ
     *
     * ระวัง: ห้ามคืน reference ของ songs ตรง ๆ (rep exposure!)
     */
    public List<String> movies() {
        
        return new ArrayList<>(movies); // แก้บรรทัดนี้
    }

    // ===== Producer =====

        /**
     * TODO 11: คืนเพลย์ลิสต์ใหม่ที่มีหนังเดียวกันแต่สลับลำดับ
     *
     * ระวัง: ห้ามแก้เพลย์ลิสต์เดิม (this) เด็ดขาด
     *
     * @return เพลย์ลิสต์ใหม่ที่สลับลำดับแล้ว
     */
    public Boundedstack shuffled() {
        List<String> copy = new ArrayList<>(movies);
        Collections.shuffle(copy);
        return new Boundedstack(copy);   // แก้บรรทัดนี้
    }

    @Override
    public String toString() {
        return movies.toString();
    }
}
    