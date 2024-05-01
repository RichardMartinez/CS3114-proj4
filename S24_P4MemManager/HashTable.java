
/**
 * This class represents a hash table.
 * The hash functions used are provided in
 * the project spec.
 * 
 * @author Richard Martinez
 * 
 * @version 2024-04-20
 */
public class HashTable {

    // The capacity of the table
    private int capacity;
    
    // The number of items currently in the table
    private int size;
    
    // The table array
    private HashEntry[] table;
    
    /**
     * Constructor for HashTable
     * @param capacity
     *      The initial capacity must be power of two
     */
    public HashTable(int capacity) {
        // TODO: Check that capacity is power of two
        this.capacity = capacity;
        
        // Init array
        table = new HashEntry[capacity];
        
        // Fill array with default constructed entries
        for (int i = 0; i < capacity; i++) {
            table[i] = new HashEntry();
        }
    }
    
    /**
     * First hash function h1(k, M)
     * @param k
     *      The integer to hash
     * @param M
     *      The size of the hash table
     * @return the hashed integer
     */
    public int h1(int k, int M) {
        return k % M;
    }
    
    /**
     * Second hash function h2(k, M)
     * @param k
     *      The integer to hash
     * @param M
     *      The size of the hash table
     * @return the hashed integer
     */
    public int h2(int k, int M) {
        return (((k / M) % (M / 2)) * 2) + 1;
    }
    
    /**
     * Returns true if empty
     * @return true if empty
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Return the current size
     * @return the size
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Return the current capacity
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * Insert into the hash table
     * @param key
     *      The key to insert
     * @param value
     *      The value to insert
     * @return true on success
     */
    public boolean insert(int key, Handle value) {
        // Assume error checking has been done,
        // Just do the insert
        
        // TODO: Check size and resize
        
        // Keep track of index and step
        int index = h1(key, capacity);
        int step = h2(key, capacity);
        HashEntryState state = table[index].getState();
        
        // Keep going until you find a valid spot
        while (state != HashEntryState.EMPTY) {
            // Either full or tombstone
            if (state == HashEntryState.TOMBSTONE) {
                // We should be able to insert here
                break;
            }
            
            // This index is full
            // Is there a duplicate key?
            if (table[index].getKey() == key) {
                // ERROR: Duplicate Key
                return false;
            }
            
            // No duplicate key, but need to move on
            index = (index + step) % capacity;
            state = table[index].getState();
        }
        
        // Here is a valid index
        HashEntry entry = new HashEntry(key, value);
        entry.setState(HashEntryState.FULL);
        table[index] = entry;
        size++;
        
        return true;
    }
    
    /**
     * Returns the value associated with key if it exists
     * @param key
     *      The key to retrieve
     * @return the value
     */
    public Handle get(int key) {
        // Assume error checking, just do the get
        
        // Get the home index
        int home_index = h1(key, capacity);
        
        // Check if its there
        if (table[home_index].getState() == HashEntryState.FULL) {
            if (table[home_index].getKey() == key) {
                // Found it!!
                return table[home_index].getValue();
            }
        }
        
        // Either it was empty, tombstone, or the key didn't match
        // Do probing
        int step_size = h2(key, capacity);
        int probing_index = home_index;  // Need to check home_index for tombstone
        
        while (table[probing_index].getState() != HashEntryState.EMPTY) {
            // Either full or tombstone
//            if (table[probing_index].getState() == HashEntryState.TOMBSTONE) {
//                // Skip over it
//                probing_index = (probing_index + step_size) % capacity;
//                continue;
//            }
            
            // Here it must be full
            // Does the key match?
            if (table[probing_index].getKey() == key) {
                return table[probing_index].getValue();
            }
            
            probing_index = (probing_index + step_size) % capacity;
        }
        
        // At this point, we never found it
        return null;
    }
    
    /**
     * Returns true if contains key
     * @param key
     *      The key to check
     * @return true if contains
     */
    public boolean contains(int key) {
        return get(key) != null;
    }
    
}
