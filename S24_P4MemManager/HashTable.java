
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
     * 
     * @param capacity
     *            The initial capacity must be power of two
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
     * 
     * @param k
     *            The integer to hash
     * @param M
     *            The size of the hash table
     * @return the hashed integer
     */
    public int h1(int k, int M) {
        return k % M;
    }


    /**
     * Second hash function h2(k, M)
     * 
     * @param k
     *            The integer to hash
     * @param M
     *            The size of the hash table
     * @return the hashed integer
     */
    public int h2(int k, int M) {
        return (((k / M) % (M / 2)) * 2) + 1;
    }


    /**
     * Returns true if empty
     * 
     * @return true if empty
     */
    public boolean isEmpty() {
        return size == 0;
    }


    /**
     * Return the current size
     * 
     * @return the size
     */
    public int getSize() {
        return size;
    }


    /**
     * Return the current capacity
     * 
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }


    /**
     * Insert into the hash table
     * 
     * @param key
     *            The key to insert
     * @param value
     *            The value to insert
     * @return true on success
     */
    public boolean insert(int key, Handle value) {
        // Assume error checking has been done,
        // Just do the insert

        // THIS IS RESIZING BEFORE
        double ratio = (double)size / (double)capacity;
        if (ratio >= 0.5) {
            // Put this contains call here
            // so it only needs to be called when checking
            // for resizing (much less often than calling
            // for every single insert)
            if (!contains(key)) {
                // Resize here
                resize();
            }
            else {
                // It contained it
                // No duplicate values allowed
                return false;
            }
        }

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

        // Maybe resize here?
        // No because then the value of index is now wrong

        // Here is a valid index
        HashEntry entry = new HashEntry(key, value);
        entry.setState(HashEntryState.FULL);
        table[index] = entry;
        size++;
        return true;
    }


    /**
     * Removes the key if it exists
     * 
     * @param key
     *            The key to remove
     * @return true if successful
     */
    public boolean remove(int key) {
        // Assume error checking, just do the remove
        // Keep track of index and step
        int index = h1(key, capacity);
        int step = h2(key, capacity);
        HashEntryState state = table[index].getState();

        while (state != HashEntryState.EMPTY) {
            // Either full or tomb
            if (state == HashEntryState.TOMBSTONE) {
                // Ignore it
                index = (index + step) % capacity;
                state = table[index].getState();
                continue;
            }

            // Full
            if (table[index].getKey() == key) {
                // Found it!
                table[index].setState(HashEntryState.TOMBSTONE);
                size--;
                return true;
            }

            // Haven't found it yet
            index = (index + step) % capacity;
            state = table[index].getState();
        }

        return false;
    }


    /**
     * Returns the value associated with key if it exists
     * 
     * @param key
     *            The key to retrieve
     * @return the value
     */
    public Handle get(int key) {
        // Assume error checking, just do the get
        // Keep track of index and step
        int index = h1(key, capacity);
        int step = h2(key, capacity);
        HashEntryState state = table[index].getState();

        while (state != HashEntryState.EMPTY) {
            // Full or Tombstone
            if (state == HashEntryState.TOMBSTONE) {
                // Ignore it
                index = (index + step) % capacity;
                state = table[index].getState();
                continue;
            }

            // Full
            if (table[index].getKey() == key) {
                // Found it!
                return table[index].getValue();
            }

            // Did not find it yet
            index = (index + step) % capacity;
            state = table[index].getState();
        }

        // Did not find it
        return null;
    }


    /**
     * Returns true if contains key
     * 
     * @param key
     *            The key to check
     * @return true if contains
     */
    public boolean contains(int key) {
        return get(key) != null;
    }


    /**
     * Returns the index of the key if it exists
     * Returns -1 if not found
     * 
     * @param key
     *            The key to search for
     * @return the index or -1
     */
    public int indexOf(int key) {
        // Keep track of index and step
        int index = h1(key, capacity);
        int step = h2(key, capacity);
        HashEntryState state = table[index].getState();

        while (state != HashEntryState.EMPTY) {
            // Full or Tombstone
            if (state == HashEntryState.TOMBSTONE) {
                // Ignore it
                index = (index + step) % capacity;
                state = table[index].getState();
                continue;
            }

            // Full
            if (table[index].getKey() == key) {
                // Found it!
                return index;
            }

            // Did not find it yet
            index = (index + step) % capacity;
            state = table[index].getState();
        }

        // Did not find it
        return -1;
    }


    /**
     * Prints the hashtable to standard out
     */
    public void print() {
        System.out.println("Hashtable:");

        String out;
        HashEntry entry;
        HashEntryState state;

        // For loop through table array
        // Print fulls and tombstones
        for (int i = 0; i < capacity; i++) {
            entry = table[i];
            state = entry.getState();

            if (state == HashEntryState.FULL) {
                out = String.format("%d: %d", i, entry.getKey());
                System.out.println(out);
            }
            else if (state == HashEntryState.TOMBSTONE) {
                out = String.format("%d: TOMBSTONE", i);
                System.out.println(out);
            }
            // Ignore empty
        }

        out = String.format("total records: %d", size);
        System.out.println(out);
    }


    /**
     * Resize the table by doubling the size
     * Rehash everything into new capacity
     */
    public void resize() {
        // Double capacity
        int new_capacity = capacity * 2;
        
        // Announce to console out
        String out;
        out = String.format("Hash Table expanded to %d records", new_capacity);
        System.out.println(out);

        // Create new size table
        HashEntry[] new_table = new HashEntry[new_capacity];
        for (int i = 0; i < new_capacity; i++) {
            new_table[i] = new HashEntry();
        }

        // Go through old table
        // For loop
        // Re-hash -> Probing -> Insert
        for (int i = 0; i < capacity; i++) {
            HashEntry entry = table[i];
            int key = entry.getKey();
            HashEntryState state = entry.getState();

            if (state == HashEntryState.FULL) {
                // Insert into new table
                int index = h1(key, new_capacity);
                int step = h2(key, new_capacity);
                HashEntryState state2 = new_table[index].getState();

                while (state2 != HashEntryState.EMPTY) {
                    // Never tombstones here, only fulls
                    // Never duplicates here
                    index = (index + step) % new_capacity;
                    state2 = new_table[index].getState();
                }

                // Found a valid spot here
                new_table[index] = entry;
            }
            // Ignore tombs and empty
        }

        // Install changes
        // Size doesn't change
        table = new_table;
        capacity = new_capacity;
    }

}
