import java.util.*;

//น.ส.สุกฤตา วงษ์กาวิน 6821601551
//นายศุภวิชญ์ มหามงคลชัย 6821601518

/**
 * Boundedstack — ADT แทนรายการหนังที่ผู้ใช้จัดลำดับไว้
 * ค่านามธรรม (A): ลำดับของหนัง เช่น [หนังA, หนังB, หนังC]
 */
public class Boundedstack {
    public static final int MAX_MOVIES = 100;
    // ===== representation =====
    private final List<String> movies;
    // เขียน Abstraction Function 
    // Abstraction Function: AF(movies) = เพลย์ลิสต์ที่เล่นหนัง movies.get(0), movies.get(1), ... ตามลำดับ
    // เขียน Representation Invariant (4 ข้อ)
    // Representation Invariant: song != null , ไม่มีหนังใดเป็น null , ไม่มีชื่อหนังที่เป็นสตริงว่าง , ชื่อหนังห้ามซ้ำกัน , MAX_MOVIES <= 100
    // เขียน Safety from rep exposure 
    // Safety from rep exposure: ให้ movie เป็น final คัดลอกทั้งขาเข้าและขาขาออก
    /**
     * เขียน checkRep()
     * แปลง RI ทุกข้อเป็น assert หนึ่งบรรทัด พร้อมข้อความอธิบาย
    */
    private void checkRep(){
        assert movies != null : "movies not null!!";
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
     */
    public Boundedstack() {
        this.movies = new ArrayList<>();
        checkRep();
    }
    
    /**
    * Creator ตัวที่สอง
    * สร้างเพลย์ลิสต์จากรายชื่อหนังที่ให้มา
    * @param initial รายชื่อหนังเริ่มต้น ต้องไม่ซ้ำและไม่เกิน MAX_MOVIES
    * @throws IllegalArgumentException ถ้า initial ผิดเงื่อนไข
    */
    public Boundedstack(List<String> initial) {
        if(initial == null ) throw new IllegalArgumentException();
        if (initial.size() > MAX_MOVIES) throw new IllegalArgumentException();
        Set<String> seen = new HashSet<>();   
        for (String s : initial) {
            if (s == null || s == "") throw new IllegalArgumentException();
            if (!seen.add(s)) throw new IllegalArgumentException();
        }

        this.movies = new ArrayList<>(initial); 
        checkRep();
    }
    // ===== Mutators =====
    /**
    * เพิ่มชื่อหนังลงในเพลย์ลิสต์
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
        return true;   
    }
    /**
    * ลบชื่อหนังออกจากเพลย์ลิสต์
    * @param movie ชื่อหนังที่ต้องการลบ
    * @return true ถ้าลบสำเร็จ, false ถ้าไม่พบหนังเรื่องนี้
    */
    public boolean remove(String movie) {
        if (   !movies.contains(movie) ) return false; 
        movies.remove(movie);
        checkRep();
        return true;   
    }
    // ===== Observers =====
    /**
     * คืนค่าจำนวนหนังทั้งหมดที่อยู่ใน Boundedstack
     *
     * @return จำนวนหนังทั้งหมด
     */

    public int size() {
        return movies.size();   
    }
    /**
    * ตรวจสอบว่ามีหนังเรื่องที่ระบุอยู่ใน Boundedstack หรือไม่
    *
    * @param movie ชื่อหนังที่ต้องการตรวจสอบ
    * @return true ถ้ามีหนังเรื่องนั้นอยู่, false หากไม่มี
     */
    public boolean contains(String movie) {
        return movies.contains(movie);   
    }
    /**
     * คืนค่ารายชื่อหนังทั้งหมดตามลำดับที่เก็บอยู่
     * โดยคืนเป็นรายการใหม่เพื่อไม่ให้ข้อมูลภายในถูกแก้ไข
     *
     * @return รายชื่อหนังทั้งหมด
     */
    public List<String> movies() {
        return new ArrayList<>(movies); 
    }
    // ===== Producer =====
    /**
     * คืนเพลย์ลิสต์ใหม่ที่มีหนังเดียวกันแต่สลับลำดับ
     * @return เพลย์ลิสต์ใหม่ที่สลับลำดับแล้ว
     */
    public Boundedstack shuffled() {
        List<String> copy = new ArrayList<>(movies);
        Collections.shuffle(copy);
        return new Boundedstack(copy);   
    }
    /**
     * คืนค่า Boundedstack ใหม่ที่มีข้อมูลเหมือนเดิมทุกตัว
     * แต่เรียงลำดับชื่อหนังจาก(A-Z), (ก-ฮ)
     * โดยไม่แก้ไขข้อมูลใน Boundedstack เดิม
     * @return Boundedstack ใหม่ที่เรียงลำดับข้อมูลแล้ว
     */
    public Boundedstack sorted(){
        List<String> copy = new ArrayList<>(movies);
        Collections.sort(copy);
        return new Boundedstack(copy);
    }
    @Override
    public String toString() {
        return movies.toString();
    }
}