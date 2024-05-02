
import java.lang.Math;

/**
 * This class represents a memory manager.
 * It maintains a free block list using the buddy method.
 * This leads to internal fragmentation, but efficient searching.
 * 
 * @author Richard Martinez
 * 
 * @version 2024-05-01
 */
public class MemoryManager {
    
    // The capacity of the memory array
    private int capacity;
    
    /**
     * Constructor for MemoryManager
     * 
     * @param capacity
     *      The initial capacity must be a power of two
     */
    public MemoryManager(int capacity) {
        this.capacity = capacity;
    }
    
    /**
     * Get the next power of 2 from value
     * Returns n where 2^n is the next power of 2
     * @param value
     *      The value to use
     * @return ceiling(log2(n))
     */
    public int nextPow2(int value) {
        double log2n = Math.log(value) / Math.log(2);
        
        int result = (int)Math.ceil(log2n);
        
        return result;
    }

}
