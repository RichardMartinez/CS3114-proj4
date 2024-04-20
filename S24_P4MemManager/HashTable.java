
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
}
