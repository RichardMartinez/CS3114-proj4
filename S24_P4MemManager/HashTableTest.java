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
        
        assertEquals(table.getSize(), 4);
        assertEquals(table.getCapacity(), 8);
        
        // Try inserting duplicate
        handle = new Handle(10, 10);
        success = table.insert(0, handle);
        assertFalse(success);
        
        assertEquals(table.getSize(), 4);
        assertEquals(table.getCapacity(), 8);
        
        // Test index of
        assertEquals(table.indexOf(0), 0);
        assertEquals(table.indexOf(3), 3);
        assertEquals(table.indexOf(8), 6);
        assertEquals(table.indexOf(11), 1);
        assertEquals(table.indexOf(1), -1);
    }
    
    /**
     * Test the get method
     */
    public void testGet() {
        Handle handle;
        
        assertTrue(table.isEmpty());
        
        handle = table.get(0);
        assertNull(handle);
        assertEquals(table.indexOf(0), -1);
        
        handle = new Handle(0, 0);
        table.insert(0, handle);
        
        handle = new Handle(3, 3);
        table.insert(3, handle);
        
        handle = new Handle(8, 8);
        table.insert(8, handle);
        
        handle = new Handle(11, 11);
        table.insert(11, handle);
        
        handle = table.get(0);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), 0);
        
        handle = table.get(3);
        assertEquals(handle.getAddress(), 3);
        assertEquals(handle.getLength(), 3);
        
        handle = table.get(8);
        assertEquals(handle.getAddress(), 8);
        assertEquals(handle.getLength(), 8);
        
        handle = table.get(11);
        assertEquals(handle.getAddress(), 11);
        assertEquals(handle.getLength(), 11);
        
        // Test contains
        assertTrue(table.contains(0));
        assertTrue(table.contains(3));
        assertTrue(table.contains(8));
        assertTrue(table.contains(11));
        assertFalse(table.contains(1));
        
        // Test size methods
        assertEquals(table.getSize(), 4);
        assertFalse(table.isEmpty());
    }
    
    /**
     * Test the print method
     */
    public void testPrint() {
        Handle handle;
        
        handle = new Handle(0, 0);
        table.insert(0, handle);
        
        handle = new Handle(3, 3);
        table.insert(3, handle);
        
        handle = new Handle(8, 8);
        table.insert(8, handle);
        
        handle = new Handle(11, 11);
        table.insert(11, handle);
        
        // Test print
        systemOut().clearHistory();
        table.print();
        String actual = systemOut().getHistory();
        
        String expected = "Hashtable:\n" +
            "0: 0\n" +
            "1: 11\n" +
            "3: 3\n" +
            "6: 8\n" +
            "total records: 4\n";
        
        assertFuzzyEquals(actual, expected);
    }
    
    
    /**
     * Test the remove method
     */
    public void testRemove() {
        Handle handle;
        int key;
        boolean success;
        
        assertTrue(table.isEmpty());
        
        success = table.remove(0);
        assertFalse(success);
        assertTrue(table.isEmpty());
        
        key = 0;
        handle = new Handle(key, key);
        table.insert(key, handle);
        assertEquals(table.getSize(), 1);
        
        key = 3;
        handle = new Handle(key, key);
        table.insert(key, handle);
        assertEquals(table.getSize(), 2);
        
        key = 8;
        handle = new Handle(key, key);
        table.insert(key, handle);
        assertEquals(table.getSize(), 3);
        
        key = 11;
        handle = new Handle(key, key);
        table.insert(key, handle);
        assertEquals(table.getSize(), 4);
        
        // Test remove simple
        success = table.remove(3);
        assertTrue(success);
        assertEquals(table.getSize(), 3);
        
        // Test double remove
        success = table.remove(3);
        assertFalse(success);
        assertEquals(table.getSize(), 3);
        
        // Test remove non existent
        success = table.remove(1);
        assertFalse(success);
        assertEquals(table.getSize(), 3);
        
        // Test remove simple again
        success = table.remove(11);
        assertTrue(success);
        assertEquals(table.getSize(), 2);
        
        // Verify structure
        systemOut().clearHistory();
        table.print();
        String actual = systemOut().getHistory();
        
        String expected = "Hashtable:\n" +
            "0: 0\n" +
            "1: TOMBSTONE\n" +
            "3: TOMBSTONE\n" +
            "6: 8\n" +
            "total records: 2\n";
        
        assertFuzzyEquals(actual, expected);
    }
    
    /**
     * Test inserting into a spot where a tomb is
     */
    public void testInsertIntoTombstone() {
        Handle handle;
        
        handle = new Handle(0, 0);
        table.insert(0, handle);
        
        handle = new Handle(3, 3);
        table.insert(3, handle);
        
        handle = new Handle(8, 8);
        table.insert(8, handle);
        
        handle = new Handle(11, 11);
        table.insert(11, handle);
        
        int elevenIndex = table.indexOf(11);
        
        // Remove 11 add 1
        table.remove(11);
        
        handle = new Handle(1, 1);
        table.insert(1, handle);
        
        int oneIndex = table.indexOf(1);
        
        assertEquals(elevenIndex, oneIndex);
        
        assertEquals(table.getSize(), 4);
    }
    
    /**
     * Test ignoring tombs in get
     */
    public void testGetIgnoreTombstone() {
        Handle handle;
        
        handle = new Handle(0, 0);
        table.insert(0, handle);
        
        handle = new Handle(3, 3);
        table.insert(3, handle);
        
        handle = new Handle(8, 8);
        table.insert(8, handle);
        
        handle = new Handle(11, 11);
        table.insert(11, handle);
        
        table.remove(8);
        
        handle = table.get(11);
        assertEquals(handle.getAddress(), 11);
        assertEquals(handle.getLength(), 11);
        
        assertEquals(table.getSize(), 3);
    }
    
    /**
     * Test ignoring tombs in indexOf
     */
    public void testIndexOfIgnoreTombstone() {
        Handle handle;
        
        handle = new Handle(0, 0);
        table.insert(0, handle);
        
        handle = new Handle(3, 3);
        table.insert(3, handle);
        
        handle = new Handle(8, 8);
        table.insert(8, handle);
        
        handle = new Handle(11, 11);
        table.insert(11, handle);
        
        table.remove(8);
        
        int index = table.indexOf(11);
        assertEquals(index, 1);
        
        assertEquals(table.getSize(), 3);
    }
    
    /**
     * Test the resize method
     */
    public void testResize() {
        Handle handle;
        
        handle = new Handle(0, 0);
        table.insert(0, handle);
        
        handle = new Handle(3, 3);
        table.insert(3, handle);
        
        handle = new Handle(8, 8);
        table.insert(8, handle);
        
        handle = new Handle(11, 11);
        table.insert(11, handle);
        
        // Verify structure
        assertEquals(table.indexOf(0), 0);
        assertEquals(table.indexOf(3), 3);
        assertEquals(table.indexOf(8), 6);
        assertEquals(table.indexOf(11), 1);
        
        // Do the resize
        assertEquals(table.getCapacity(), 8);
        assertEquals(table.getSize(), 4);
        table.resize();
        assertEquals(table.getCapacity(), 16);
        assertEquals(table.getSize(), 4);
        
        // Verify structure
        assertEquals(table.indexOf(0), 0);
        assertEquals(table.indexOf(3), 3);
        assertEquals(table.indexOf(8), 8);
        assertEquals(table.indexOf(11), 11);
    }
    
    /**
     * Test the resize method with another table
     */
    public void testResize2() {
        Handle handle;
        int key;
        
        key = 100;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        key = 200;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        key = 300;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        key = 400;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        // Verify the structure
        assertEquals(table.indexOf(100), 4);
        assertEquals(table.indexOf(200), 0);
        assertEquals(table.indexOf(300), 7);
        assertEquals(table.indexOf(400), 5);
        
        // Do the resize
        assertEquals(table.getCapacity(), 8);
        assertEquals(table.getSize(), 4);
        table.resize();
        assertEquals(table.getCapacity(), 16);
        assertEquals(table.getSize(), 4);
        
        // Verify the structure
        assertEquals(table.indexOf(100), 4);
        assertEquals(table.indexOf(200), 8);
        assertEquals(table.indexOf(300), 12);
        assertEquals(table.indexOf(400), 0);
    }
    
    /**
     * Test resize with probing in new table
     */
    public void testResize3() {
        Handle handle;
        int key;
        
        key = 0;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        key = 10;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        key = 20;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        key = 32;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        // Verify the structure
        assertEquals(table.indexOf(0), 0);
        assertEquals(table.indexOf(10), 2);
        assertEquals(table.indexOf(20), 4);
        assertEquals(table.indexOf(32), 1);
        
        // Do the resize
        assertEquals(table.getCapacity(), 8);
        assertEquals(table.getSize(), 4);
        table.resize();
        assertEquals(table.getCapacity(), 16);
        assertEquals(table.getSize(), 4);
        
        // Verify the structure
        assertEquals(table.indexOf(0), 0);
        assertEquals(table.indexOf(10), 10);
        assertEquals(table.indexOf(20), 4);
        assertEquals(table.indexOf(32), 5);
    }
    
    /**
     * Test resize ignoring tombstones
     */
    public void testResizeIgnoreTombstone() {
        Handle handle;
        int key;
        
        key = 0;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        key = 10;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        key = 20;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        key = 32;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        table.remove(20);
        
        // Verify the structure
        assertEquals(table.indexOf(0), 0);
        assertEquals(table.indexOf(10), 2);
        assertEquals(table.indexOf(32), 1);
        
        // Do the resize
        assertEquals(table.getCapacity(), 8);
        assertEquals(table.getSize(), 3);
        table.resize();
        assertEquals(table.getCapacity(), 16);
        assertEquals(table.getSize(), 3);
        
        // Verify the structure
        assertEquals(table.indexOf(0), 0);
        assertEquals(table.indexOf(10), 10);
        assertEquals(table.indexOf(32), 5);
    }
    
    /**
     * Test the resizing before strategy
     */
    public void testResizingBefore() {
        Handle handle;
        int key;
        
        key = 0;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        key = 3;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        key = 8;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        key = 12;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        assertEquals(table.getCapacity(), 8);
        
        // Verify Structure
        assertEquals(table.indexOf(0), 0);
        assertEquals(table.indexOf(3), 3);
        assertEquals(table.indexOf(8), 6);
        assertEquals(table.indexOf(12), 4);
        
        // This should cause a resize
        key = 28;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        assertEquals(table.getCapacity(), 16);
        assertEquals(table.getSize(), 5);
        
        // Verify Structure
        assertEquals(table.indexOf(0), 0);
        assertEquals(table.indexOf(3), 3);
        assertEquals(table.indexOf(8), 8);
        assertEquals(table.indexOf(12), 12);
        assertEquals(table.indexOf(28), 15);
    }
    
    /**
     * Test ignoring duplicate insert not affecting capacity
     */
    public void testResizingBeforeIgnoreDuplicate() {
        Handle handle;
        int key;
        
        key = 0;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        key = 3;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        key = 8;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        key = 12;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        assertEquals(table.getCapacity(), 8);
        
        // Verify Structure
        assertEquals(table.indexOf(0), 0);
        assertEquals(table.indexOf(3), 3);
        assertEquals(table.indexOf(8), 6);
        assertEquals(table.indexOf(12), 4);
        
        assertEquals(table.getCapacity(), 8);
        assertEquals(table.getSize(), 4);
        
        // This should NOT cause a resize
        key = 0;
        handle = new Handle(key, key);
        table.insert(key, handle);
        
        assertEquals(table.getCapacity(), 8);
        assertEquals(table.getSize(), 4);
        
        // Verify Structure
        assertEquals(table.indexOf(0), 0);
        assertEquals(table.indexOf(3), 3);
        assertEquals(table.indexOf(8), 6);
        assertEquals(table.indexOf(12), 4);
    }
    
}
