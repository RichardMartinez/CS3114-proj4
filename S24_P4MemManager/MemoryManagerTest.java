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
    
}
