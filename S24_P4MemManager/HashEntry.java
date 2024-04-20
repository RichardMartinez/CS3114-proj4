
/**
 * An enum to represent the state of a HashEntry
 * 
 * @author Richard Martinez
 * 
 * @version 2024-04-20
 */
enum HashEntryState {
    EMPTY,
    FULL,
    TOMBSTONE
};

/**
 * This class represents an entry in
 * the hash table.
 * It stores a key, value, and a state
 * 
 * @author Richard Martinez
 * 
 * @version 2024-04-20
 */
public class HashEntry {

    // The key for the entry
    private int key;
    
    // The value for the entry
    private Handle value;
    
    // The state of the entry
    private HashEntryState state;
    
    /**
     * Constructor for HashEntry
     * @param key
     *      The key for the entry
     * @param value
     *      The value for the entry
     */
    public HashEntry(int key, Handle value) {
        this.key = key;
        this.value = value;
        this.state = HashEntryState.EMPTY;
    }
    
    /**
     * Default constructor for HashEntry
     */
    public HashEntry() {
        this(-1, new Handle());
    }
    
    /**
     * Set the state of this entry
     * @param state
     *      The new state
     */
    public void setState(HashEntryState state) {
        this.state = state;
    }
    
    /**
     * Return the current state
     * @return the state
     */
    public HashEntryState getState() {
        return state;
    }
    
    /**
     * Return the key
     * @return the key
     */
    public int getKey() {
        return key;
    }
    
    /**
     * Return the value
     * @return the value
     */
    public Handle getValue() {
        return value;
    }
    
    // TODO: equals(HashEntry) ??
    
    // No need for setKey, setValue
    // Create a new Entry and replace it in table
}
