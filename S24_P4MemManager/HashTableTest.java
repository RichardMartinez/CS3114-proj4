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
    
    /**
     * Test basic insert method
     */
    public void testBasicInsert() {
        Handle handle;
        boolean success;
        
        assertTrue(table.isEmpty());
        
        handle = new Handle(0, 0);
        success = table.insert(0, handle);
        assertTrue(success);
        
        handle = new Handle(3, 3);
        success = table.insert(3, handle);
        assertTrue(success);
        
        handle = new Handle(8, 8);
        success = table.insert(8, handle);
        assertTrue(success);
        
        handle = new Handle(11, 11);
        success = table.insert(11, handle);
        assertTrue(success);
        
        // Try inserting duplicate
        handle = new Handle(10, 10);
        success = table.insert(0, handle);
        assertFalse(success);
        
        // Test get
        handle = table.get(0);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getAddress(), 0);
        
        handle = table.get(3);
        assertEquals(handle.getAddress(), 3);
        assertEquals(handle.getAddress(), 3);
        
        handle = table.get(8);
        assertEquals(handle.getAddress(), 8);
        assertEquals(handle.getAddress(), 8);
        
        handle = table.get(11);
        assertEquals(handle.getAddress(), 11);
        assertEquals(handle.getAddress(), 11);
        
        handle = table.get(21);
        assertNull(handle);
        
        handle = table.get(40);
        assertNull(handle);
        
        // Test contains
        assertTrue(table.contains(0));
        assertTrue(table.contains(3));
        assertTrue(table.contains(8));
        assertTrue(table.contains(11));
        assertFalse(table.contains(21));
        assertFalse(table.contains(40));
        
        // Size methods
        assertEquals(table.getSize(), 4);
        assertFalse(table.isEmpty());

    }
}
