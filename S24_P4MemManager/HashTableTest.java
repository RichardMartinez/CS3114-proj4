import student.TestCase;

/**
 * Test the methods of the HashTable class.
 * 
 * @author Richard Martinez
 * 
 * @version 2024-04-20
 */
public class HashTableTest extends TestCase {

    private HashTable table;
    
    /**
     * Initialize the test object
     */
    public void setUp() {
        table = new HashTable(8);
    }
    
    /**
     * Test the constructor
     */
    public void testConstructor() {
        assertEquals(table.getCapacity(), 8);
        assertTrue(table.isEmpty());
        assertEquals(table.getSize(), 0);
    }
    
    /**
     * Test h1 hash
     */
    public void testH1() {
        assertEquals(table.h1(2, 8), 2);
        assertEquals(table.h1(4, 8), 4);
        assertEquals(table.h1(6, 8), 6);
        assertEquals(table.h1(8, 8), 0);
        assertEquals(table.h1(10, 8), 2);
    }
    
    /**
     * Test h2 hash
     */
    public void testH2() {
        assertEquals(table.h2(2, 8), 1);
        assertEquals(table.h2(6, 8), 1);
        assertEquals(table.h2(7, 8), 1);
        assertEquals(table.h2(8, 8), 3);
        assertEquals(table.h2(16, 8), 5);
        assertEquals(table.h2(24, 8), 7);
    }
}
