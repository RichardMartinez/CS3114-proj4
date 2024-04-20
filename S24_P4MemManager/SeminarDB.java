import java.io.IOException;

/**
 * Handle the Seminar database. This class processes commands by manipulating
 * the hash table.
 *
 * @author Richard Martinez
 * @version 2024-04-20
 */
public class SeminarDB
{
    // private MemManager myMemman; // Implement Memory Manager class in a separate file
    // private Hash myHashTable; // Implement Hash table class in a separate file
    
    private HashTable table;

    /**
     * Create a new SeminarDB object.
     *
     * @param initMemSize
     *            Initial ize for memory pool
     * @param initHashSize
     *            Initial size for hash tables
     * @throws IOException
     */
    public SeminarDB(int initMemSize, int initHashSize)
        throws IOException
    {
        // myMemman = new MemManager(initMemSize);
        // myHashTable = new Hash(initHashSize);
        
        table = new HashTable(initHashSize);
    }

    // ----------------------------------------------------------
    /**
     * Process insert command, which requires a lot of parsing!
     * @param sID ID value
     * @param stitle title
     * @param sdate date
     * @param slength length
     * @param sx x
     * @param sy y
     * @param scost cost
     * @param skeywords keywords
     * @param sdesc description
     * @throws Exception
     */
    public void insert(int sID, String stitle, String sdate, int slength,
        int sx, int sy, int scost, String[] skeywords, String sdesc)
        throws Exception
    {
        //TODO: Implement this method
        
        // If ID already exists, FAIL
        
        // Construct seminar object using params
        
        // Serialize into byte array
        
        // Insert into memory manager -> Handle
        
        // Insert into hash table -> key = ID, value = Handle
        
        // TEMP: Insert null handles for now
        table.insert(sID, null);
    }

    // ----------------------------------------------------------
    /**
     * Delete the record with the given key
     * @param sID The key to find and remove
     * @throws IOException
     */
    public void delete(int sID)
        throws IOException
    {
        //TODO: Implement this method
        
        // Check hash table -> Handle
        // If ID does not exist, FAIL
        
        // Remove from memory manager if exists
        
        // Remove from hash table
    }

    // ----------------------------------------------------------
    /**
     * Find and return the record that matches the given key
     * @param sID The key to search for
     * @throws IOException
     * @throws Exception
     */
    public void search(int sID)
        throws IOException, Exception
    {
        //TODO: Implement this method
        
        // Check hash table -> Handle
        // If ID does not exist, FAIL
        
        // Get from memory manager -> byte array
        
        // De-serialize to Seminar object
        
        // Print all the stuff
    }

    // ----------------------------------------------------------
    /**
     * Print the hash table
     * @return Number of records in table
     * @throws IOException
     */
    public int hashprint()
        throws IOException
    {
        //TODO: Implement this method
        
        // Print all the stuff in the hash table
        
        // Return number of records in the table
        return 0;
    }

    // ----------------------------------------------------------
    /**
     * Print the memory manager freeblock list
     */
    public void memmanprint()
    {
        //TODO: Implement this method
        
        // Print the freelist
    }
}
