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
        return 0;
    }

    // ----------------------------------------------------------
    /**
     * Print the memory manager freeblock list
     */
    public void memmanprint()
    {
        //TODO: Implement this method
    }
}
