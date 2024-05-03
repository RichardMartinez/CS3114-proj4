
import java.lang.Math;
import java.util.ArrayList;

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
    
    // 2^N = capacity
    private int N;
    
    // The actual memory array
    private byte[] memory;
    
    // The free block list
    private ArrayList<ArrayList<Integer>> freeblocklist;
    
    // Used and free bytes
    private int usedbytes;
    private int freebytes;
    
    /**
     * Constructor for MemoryManager
     * 
     * @param capacity
     *      The initial capacity must be a power of two
     */
    public MemoryManager(int capacity) {
        // Save capacity
        this.capacity = capacity;
        this.N = nextPow2(capacity);
        
        this.usedbytes = 0;
        this.freebytes = capacity;
        
        // Init memory array
        memory = new byte[capacity];
        
        // Init free block list
        freeblocklist = new ArrayList<ArrayList<Integer>>();
        
        for (int i = 0; i <= N; i++) {
            freeblocklist.add(new ArrayList<Integer>());
        }
        
        // Place one block at the end
        // Free block = whole initial array
        // Each node stores starting address of free block
        freeblocklist.get(N).add(0);
    }
    
    /**
     * Insert the space array into memory
     * @param space
     *      The byte array to enter
     * @param size
     *      The size of the byte array
     * @return a Handle representing where it was placed in memory
     */
    public Handle insert(byte[] space, int size) {
        // Handle returned: start address, actual length of record
        // Assume error checking, just do the insert
        
        int blockN = nextPow2(size);
        // int blocksize = raiseToPow2(blockN);

        // TODO: Check size and resize
        
        // Here, we are guaranteed enough space
        
        // Check if we have a free block in blockN
        ArrayList<Integer> sublist = freeblocklist.get(blockN);
        
        if (sublist.size() == 0) {
            // Native block size not available
            // Split here
            split(blockN);
        }
        
        // At this point, native block size is available
        // Insert is smallest position
        // Sorted list -> smallest position is index 0
        int position = sublist.get(0);
        
        // Copy space array into memory array
        for (int i = 0; i < size; i++) {
            memory[position + i] = space[i];
        }
        freebytes -= size;
        usedbytes += size;
        
        // Remove block from FBL
        sublist.remove(0);
        
        // Build and return the handle
        Handle handle = new Handle(position, size);
        return handle;
    }
        
    /**
     * Splits the free block list until a blockN is available
     * @param blockN
     *      The target blockN to make
     */
    public void split(int blockN) {
        // Known sublist at blockN is empty when calling this
        // Known there is space somewhere below blockN
        // because resizing always happens first
        
        // Find sublist below blockN that has space (guaranteed)
        int i;
        for (i = blockN + 1; i <= this.N; i++) {
            ArrayList<Integer> sublist = freeblocklist.get(i);
            if (sublist.size() > 0) {
                break;
            }
        }
        
        // What if it didn't find it?
        // That should never happen
        
        // At this point, i is now the index with next highest size        
        // Keep splitting until blockN size is not zero
        while (freeblocklist.get(blockN).size() == 0) {
            // Split level i, keep sorted order
            ArrayList<Integer> sublist = freeblocklist.get(i);
            
            // Pop front value
            int position = sublist.get(0);
            sublist.remove(0);
            
            // Add two values to previous sublist
            ArrayList<Integer> prevSubList = freeblocklist.get(i - 1);
            int prevBlockSize = raiseToPow2(i - 1);
            
            addToListSorted(prevSubList, position);
            addToListSorted(prevSubList, position + prevBlockSize);
            
            i--;
        }
        // At this point, blockN sublist has at least one available block
    }
    
    
    /**
     * Adds the value to the list to ensure it stays sorted
     * @param list
     *      The list to add to
     * @param value
     *      The value to add
     */
    public void addToListSorted(ArrayList<Integer> list, int value) {
        int i;
        
        for (i = 0; i < list.size(); i++) {
            if (value < list.get(i)) {
                list.add(i, value);
                return;
            }
        }
        
        // It should go at the end
        list.add(value);
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
    
    /**
     * Return 2^N
     * @param N
     *      The exponent
     * @return 2^N
     */
    public int raiseToPow2(int N) {
        return (int)Math.pow(2, N);
    }
    
    /**
     * Print the free block list to output
     */
    public void print() {
        System.out.println("Freeblock List:");
        
        // Check if no free blocks
        if (freebytes <= 0) {
            System.out.println("There are no freeblocks in the memory pool");
            return;
        }
        
        String out;
        
        // We know there are free blocks
        for (int i = 0; i <= N; i++) {
            // For each size block
            ArrayList<Integer> sublist = freeblocklist.get(i);
            
            // Ignore empty sublists
            if (sublist.size() == 0) {
                continue;
            }
            
            // Start printing
            int blocksize = raiseToPow2(i);
            
            out = String.format("%d: ", blocksize);
            System.out.print(out);
            
            // Print all the blocks in here
            for (int j = 0; j < sublist.size(); j++) {
                int position = sublist.get(j);
                
                out = String.format("%d ", position);
                System.out.print(out);
            }
            
            // New line
            System.out.println();
        }
        
    }

}
