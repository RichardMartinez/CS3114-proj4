import student.TestCase;

/**
 * Test the methods of the Handle class.
 * 
 * @author Richard Martinez
 * 
 * @version 2024-04-20
 */
public class HandleTest extends TestCase {

    private Handle handle;
    
    /**
     * Initialize the test object
     */
    public void setUp() {
        handle = new Handle(10, 20);
    }
    
    /**
     * Test the methods of Handle
     */
    public void testHandle() {
        assertEquals(handle.getAddress(), 10);
        assertEquals(handle.getLength(), 20);
    }
    
    // TODO: Test equals(Handle) method??
}
