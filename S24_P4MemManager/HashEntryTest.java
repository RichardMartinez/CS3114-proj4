import student.TestCase;

/**
 * Test the methods of the HashEntry class.
 * 
 * @author Richard Martinez
 * 
 * @version 2024-04-20
 */
public class HashEntryTest extends TestCase {

    private HashEntry entry;
    
    private Handle handle;
    
    /**
     * Initialize the test object
     */
    public void setUp() {
        handle = new Handle(15, 25);
        entry = new HashEntry(5, handle);
    }
    
    /**
     * Test the methods of HashEntry
     */
    public void testHashEntry() {
        assertEquals(entry.getKey(), 5);
        assertEquals(entry.getValue().getAddress(), 15);
        assertEquals(entry.getValue().getLength(), 25);
        assertEquals(entry.getState(), HashEntryState.EMPTY);
        
        entry.setState(HashEntryState.FULL);
        assertEquals(entry.getState(), HashEntryState.FULL);
    }
    
    /**
     * Test the default HashEntry
     */
    public void testDefaultHashEntry() {
        entry = new HashEntry();
        assertEquals(entry.getKey(), -1);
        assertEquals(entry.getValue().getAddress(), -1);
        assertEquals(entry.getValue().getLength(), -1);
        assertEquals(entry.getState(), HashEntryState.EMPTY);
    }
}
