
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
    
    // Number of free bytes
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
        int blocksize = raiseToPow2(blockN);

        // Check size and resize
        // If can't insert -> resize
        // Keep resizing until done
        while (!canInsert(blockN)) {
            resize();
        }
        
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
        freebytes -= blocksize;
        
        // Remove block from FBL
        sublist.remove(0);
        
        // Build and return the handle
        Handle handle = new Handle(position, size);
        return handle;
    }
    
    /**
     * Frees the specified block in memory
     * @param handle
     *      The handle representing the block
     */
    public void remove(Handle handle) {
        // Assume error checking, just do the remove
        
        // Give the block back to FBL
        int position = handle.getAddress();
        int length = handle.getLength();
        int blockN = nextPow2(length);
        int blocksize = raiseToPow2(blockN);
        
        ArrayList<Integer> sublist = freeblocklist.get(blockN);
        addToListSorted(sublist, position);
        freebytes += blocksize;
        
        // Call merge
        merge();
    }
    
    // TODO: Implement get here
    
    /**
     * Get a record from memory and put it into space
     * @param space
     *      The array to place record into
     * @param handle
     *      The handle pointing to the record in memory
     * @param size
     *      The size of the space array
     * @return true if successful
     */
    public boolean get(byte[] space, Handle handle, int size) {
        // Copy "size" bytes into "space" from position specified
        // by "handle"
        
        // Assume error checking, just do the get
        int position = handle.getAddress();
        int length = handle.getLength();
        
        if (length != size) {
            // Space array and length of record do not line up
            return false;
        }
        
        for (int i = 0; i < size; i++) {
            space[i] = memory[position + i];
        }
        return true;
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
     * Merge all buddies together and propagate through all sizes
     */
    public void merge() {
        // For each size in FBL
        // No need to try to merge last level
        for (int i = 0; i <= this.N - 1; i++) {
            ArrayList<Integer> sublist = freeblocklist.get(i);
            
            // Ignore empty sublists
            if (sublist.size() == 0) {
                continue;
            }
            
            // Call helper function
            mergehelp(i);
        }
    }
    
    /**
     * This is a helper method factored out of merge
     * to help readability
     * @param i
     *      The current loop iteration inside merge
     */
    public void mergehelp(int i) {
        // If buddies in sublist, merge to i+1
        // Can never have more than one pair of buddies in a single pass
        
        int blocksize = raiseToPow2(i);
        ArrayList<Integer> sublist = freeblocklist.get(i);
        
        // For each block in sublist
        // No need to try to merge last block
        // Insight: Two buddies will ALWAYS be next to each other
        // because the array is sorted (b2Index = b1Index + 1)
        for (int j = 0; j < sublist.size() - 1; j++) {
            int position = sublist.get(j);
            int buddyPos = getBuddyPos(position, blocksize);
            
            int b1index = j;
            int b2index = j + 1;
            
            // BuddyPos should be in j+1
            if (sublist.get(b2index) == buddyPos) {
                // Found the buddy, let's merge
                ArrayList<Integer> nextList = freeblocklist.get(i + 1);
                
                // Remove b2
                sublist.remove(b2index);
                
                // Move b1 to next list
                addToListSorted(nextList, position);
                sublist.remove(b1index);
                
                // Only one merge to handle max, so return to save time
                return;
            }
        }
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
     * Returns the buddy position give a free block
     * Only the position is returned, size is implied the same
     * @param position
     *      The position of a free block
     * @param size
     *      The size of the free block
     * @return The position of the buddy
     */
    public int getBuddyPos(int position, int size) {
        // This is from OpenDSA 11.09
        // Flip the k'th bit with bitwise XOR
        return position ^ size;
    }
    
    /**
     * Return true if the blocks are buddies
     * @param b1start
     *      The starting position of b1
     * @param b1size
     *      The size of b1
     * @param b2start
     *      The starting position of b2
     * @param b2size
     *      The size of b2
     * @return true if b1 and b2 are buddies
     */
    public boolean areBuddies(int b1start, int b1size, int b2start, int b2size) {
        // This is from OpenDSA 11.09
        return (b1start | b1size) == (b2start | b2size);
    }
    
    
    /**
     * Returns the number of free bytes
     * @return freebytes
     */
    public int numFreeBytes() {
        return freebytes;
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
    
    /**
     * Return true if can insert a blockN
     * @param blockN
     *      The blockN to check
     * @return true if can insert
     */
    public boolean canInsert(int blockN) {
        // True if current memory can insert a blockN
        // False if not -> Resize before anything else

        // Can't insert if requested blockN > this.N
        // This is covered in for loop
//        if (blockN > this.N) {
//            return false;
//        }
        
        // Can insert if any sublist blockN -> this.N
        // has length > 0
        // See if any sublist has space
        for (int i = blockN; i <= this.N; i++) {
            ArrayList<Integer> sublist = freeblocklist.get(i);
            if (sublist.size() > 0) {
                return true;
            }
        }
        
        // No sublist found
        return false;
    }

    /**
     * Resizes the memory pool
     */
    public void resize() {
        // Double capacity
        int new_capacity = capacity * 2;
        
        // Create new size array
        byte[] new_memory = new byte[new_capacity];
        
        // Copy over old array
        for (int i = 0; i < capacity; i++) {
            new_memory[i] = memory[i];
        }
        
        // Install new array and update stats vars
        memory = new_memory;
        freebytes += capacity;
        
        // Update FBL
        freeblocklist.add(new ArrayList<Integer>());
        freeblocklist.get(N).add(capacity);
        
        // Install new capacity
        capacity = new_capacity;
        N += 1;
        
        // Merge here
        merge();
    }
    
    /**
     * Return the capacity
     * @return capacity
     */
    public int getCapacity() {
        return capacity;
    }
    
}
