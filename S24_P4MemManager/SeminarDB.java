import java.io.IOException;

/**
 * Handle the Seminar database. This class processes commands by manipulating
 * the hash table.
 *
 * @author Richard Martinez
 * @version 2024-04-20
 */
public class SeminarDB {
    // The hash table storing key (ID), value (Handle) pairs
    private HashTable table;

    // The memory manager using a free block list
    private MemoryManager memory;

    /**
     * Create a new SeminarDB object.
     *
     * @param initMemSize
     *            Initial ize for memory pool
     * @param initHashSize
     *            Initial size for hash tables
     * @throws IOException
     */
    public SeminarDB(int initMemSize, int initHashSize) throws IOException {
        // Init both table and memory
        table = new HashTable(initHashSize);
        memory = new MemoryManager(initMemSize);
    }


    // ----------------------------------------------------------
    /**
     * Process insert command, which requires a lot of parsing!
     * 
     * @param sID
     *            ID value
     * @param stitle
     *            title
     * @param sdate
     *            date
     * @param slength
     *            length
     * @param sx
     *            x
     * @param sy
     *            y
     * @param scost
     *            cost
     * @param skeywords
     *            keywords
     * @param sdesc
     *            description
     * @throws Exception
     */
    public void insert(
        int sID,
        String stitle,
        String sdate,
        int slength,
        int sx,
        int sy,
        int scost,
        String[] skeywords,
        String sdesc)
        throws Exception {
        String out;

        // If ID already exists, FAIL
        if (table.contains(sID)) {
            // FAIL
            out = String.format(
                "Insert FAILED - There is already a record with ID %d", sID);
            System.out.println(out);
            return;
        }

        // Construct seminar object using params
        // Serialize to byte array
        Seminar sem = new Seminar(sID, stitle, sdate, slength, (short)sx,
            (short)sy, scost, skeywords, sdesc);
        byte[] serial = sem.serialize();

        // Insert into memory manager -> Handle
        int size = serial.length;
        Handle handle = memory.insert(serial, size);

        // Insert into hash table -> key = ID, value = Handle
        table.insert(sID, handle);

        // SUCCESS
        out = String.format("Successfully inserted record with ID %d", sID);
        System.out.println(out);
        System.out.println(sem.toString());
        out = String.format("Size: %d", size);
        System.out.println(out);
    }


    // ----------------------------------------------------------
    /**
     * Delete the record with the given key
     * 
     * @param sID
     *            The key to find and remove
     * @throws IOException
     */
    public void delete(int sID) throws IOException {
        String out;

        // If ID does not exist, FAIL
        if (!table.contains(sID)) {
            // FAIL
            out = String.format(
                "Delete FAILED -- There is no record with ID %d", sID);
            System.out.println(out);
            return;
        }

        // Get handle from table
        Handle handle = table.get(sID);

        // Remove from memory
        memory.remove(handle);

        // Remove from table
        table.remove(sID);

        // SUCCESS
        out = String.format(
            "Record with ID %d successfully deleted from the database", sID);
        System.out.println(out);
    }


    // ----------------------------------------------------------
    /**
     * Find and return the record that matches the given key
     * 
     * @param sID
     *            The key to search for
     * @throws IOException
     * @throws Exception
     */
    public void search(int sID) throws IOException, Exception {
        String out;

        // If ID does not exist -> FAIL
        if (!table.contains(sID)) {
            // FAIL
            out = String.format(
                "Search FAILED -- There is no record with ID %d", sID);
            System.out.println(out);
            return;
        }

        // Get from table -> Handle
        Handle handle = table.get(sID);
        int size = handle.getLength();

        // Get from memory manager -> byte array
        byte[] record = new byte[size];
        memory.get(record, handle, size);

        // Record now contains byte array
        // De-serialize to seminar object
        Seminar sem = Seminar.deserialize(record);

        // SUCCESS
        // Print all the stuff
        out = String.format("Found record with ID %d:", sID);
        System.out.println(out);
        System.out.println(sem.toString());
    }


    // ----------------------------------------------------------
    /**
     * Print the hash table
     * 
     * @return Number of records in table
     * @throws IOException
     */
    public int hashprint() throws IOException {
        // Print all the stuff in the hash table
        table.print();

        // Return number of records in the table
        return table.getSize();
    }


    // ----------------------------------------------------------
    /**
     * Print the memory manager freeblock list
     */
    public void memmanprint() {
        // Print the freelist
        memory.print();
    }
}
