import java.util.ArrayList;
import student.TestCase;

/**
 * Test the methods of the HashEntry class.
 * 
 * @author Richard Martinez
 * 
 * @version 2024-05-01
 */
public class MemoryManagerTest extends TestCase {

    private MemoryManager memory;
    
    /**
     * Set up the test object
     */
    public void setUp() {
        memory = new MemoryManager(512);
    }
    
    /**
     * Test the nextPow2 method
     */
    public void testNextPow2() {
        int result;
        
        result = memory.nextPow2(1);
        assertEquals(result, 0);
        
        result = memory.nextPow2(2);
        assertEquals(result, 1);
        
        result = memory.nextPow2(3);
        assertEquals(result, 2);
        
        result = memory.nextPow2(4);
        assertEquals(result, 2);
        
        result = memory.nextPow2(5);
        assertEquals(result, 3);
        
        result = memory.nextPow2(9);
        assertEquals(result, 4);
        
        result = memory.nextPow2(17);
        assertEquals(result, 5);
        
        result = memory.nextPow2(33);
        assertEquals(result, 6);
        
        result = memory.nextPow2(127);
        assertEquals(result, 7);
        
        result = memory.nextPow2(200);
        assertEquals(result, 8);
        
        result = memory.nextPow2(500);
        assertEquals(result, 9);
        
        result = memory.nextPow2(1000);
        assertEquals(result, 10);
    }
    
    /**
     * Verify the structure of the initial FBL
     */
    public void testInitialFBL() {
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();
        
        String expected = "Freeblock List:\n" +
            "512: 0\n";
        
        assertFuzzyEquals(actual, expected);
    }
    
    /**
     * Test the sorted add method
     */
    public void testAddToListSorted() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        
        memory.addToListSorted(list, 0);
        memory.addToListSorted(list, 4);
        memory.addToListSorted(list, 3);
        memory.addToListSorted(list, 5);
        memory.addToListSorted(list, 11);
        memory.addToListSorted(list, 7);
        memory.addToListSorted(list, 20);
        memory.addToListSorted(list, 15);
        
        // Assert it is sorted
        for (int i = 0; i < list.size() - 1; i++) {
            assertTrue(list.get(i) < list.get(i+1));
        }
    }
    
    /**
     * Test a simple split
     */
    public void testOneSplit() {
        memory = new MemoryManager(128);
        
        // Do the split
        int targetBlockSize = 64;
        int blockN = memory.nextPow2(targetBlockSize);
        memory.split(blockN);
        
        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();
        
        String expected = "Freeblock List:\n" +
            "64: 0 64\n";
        
        assertFuzzyEquals(actual, expected);
    }
    
    /**
     * Test splitting twice
     */
    public void testTwoSplits() {
        memory = new MemoryManager(128);
        
        // Do the split
        int targetBlockSize = 32;
        int blockN = memory.nextPow2(targetBlockSize);
        memory.split(blockN);
        
        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();
        
        String expected = "Freeblock List:\n" +
            "32: 0 32\n" +
            "64: 64\n";
        
        assertFuzzyEquals(actual, expected);
    }
    
    /**
     * Test splitting all the way to N=0
     */
    public void testSplitAllTheWay() {
        memory = new MemoryManager(128);
        
        // Do the split
        int targetBlockSize = 1;
        int blockN = memory.nextPow2(targetBlockSize);
        memory.split(blockN);
        
//        System.out.println("AFTER SPLIT ALL");
//        memory.print();
        
        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();
        
        String expected = "Freeblock List:\n" +
            "1: 0 1\n" +
            "2: 2\n" +
            "4: 4\n" +
            "8: 8\n" +
            "16: 16\n" +
            "32: 32\n" +
            "64: 64\n";
        
        assertFuzzyEquals(actual, expected);
    }
    
    /**
     * Test inserting and taking up all the space
     */
    public void testBasicInsert() {
        memory = new MemoryManager(128);
        
        byte[] space = new byte[128];
        int size = 128;
        
        Handle handle;
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), 128);
        
        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();
        
        String expected = "Freeblock List:\n" +
            "There are no freeblocks in the memory pool\n";
        
        assertFuzzyEquals(actual, expected);
    }
    
    /**
     * Test inserting half the space
     */
    public void testHalfInsert() {
        memory = new MemoryManager(128);
        
        byte[] space = new byte[64];
        int size = 64;
        
        Handle handle;
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), 64);
        
        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();
        
        String expected = "Freeblock List:\n" +
            "64: 64\n";
        
        assertFuzzyEquals(actual, expected);
    }
    
    /**
     * Test splitting all the way to 1 insert
     */
    public void testAllTheWayInsert() {
        memory = new MemoryManager(128);
        
        byte[] space = new byte[1];
        int size = 1;
        
        Handle handle;
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), 1);
        
        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();
        
        String expected = "Freeblock List:\n" +
            "1: 1\n" +
            "2: 2\n" +
            "4: 4\n" +
            "8: 8\n" +
            "16: 16\n" +
            "32: 32\n" +
            "64: 64\n";
        
        assertFuzzyEquals(actual, expected);
    }
    
    /**
     * Test splitting at a non-zero position
     */
    public void testSplitAtPositionNonZero() {
        memory = new MemoryManager(128);
        
        int size;
        byte[] space;
        Handle handle;
        
        // Allocate 64 bytes
        // Should split once at position 0
        size = 64;
        space = new byte[size];
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), 64);
        
        // Allocate 16 bytes
        // Should split twice at position 64
        size = 16;
        space = new byte[size];
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 64);
        assertEquals(handle.getLength(), 16);
        
        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();
        
        String expected = "Freeblock List:\n" +
            "16: 80\n" +
            "32: 96\n";
        
        assertFuzzyEquals(actual, expected);
    }
    
    /**
     * Test the get buddy pos method
     */
    public void testGetBuddyPos() {
        int position;
        int size;
        int buddyPos;
        
        position = 0;
        size = 64;
        buddyPos = memory.getBuddyPos(position, size);
        assertEquals(buddyPos, 64);
        assertTrue(memory.areBuddies(position, size, buddyPos, size));
        
        position = 64;
        size = 64;
        buddyPos = memory.getBuddyPos(position, size);
        assertEquals(buddyPos, 0);
        assertTrue(memory.areBuddies(position, size, buddyPos, size));
        
        // Situation for basic resize
        position = 0;
        size = 128;
        buddyPos = memory.getBuddyPos(position, size);
        assertEquals(buddyPos, 128);
        assertTrue(memory.areBuddies(position, size, buddyPos, size));
    }
    
    /**
     * Test resizing an empty memory
     */
//    public void testBasicResize() {
//        memory = new MemoryManager(128);
//        
//        System.out.println("BEFORE BASIC RESIZE");
//        memory.print();
//        
//        // Resize
//        memory.resize();
//        
//        System.out.println("AFTER BASIC RESIZE");
//        memory.print();
//    }
    
    
    
}
