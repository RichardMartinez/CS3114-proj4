
/**
 * This class represents a memory handle.
 * It stores the information used to
 * retrieve a record from memory
 * 
 * @author Richard Martinez
 * 
 * @version 2024-04-20
 */
public class Handle {

    // The starting byte address in memory
    private int address;

    // The length of the record in bytes
    private int length;

    /**
     * Constructor for Handle
     * 
     * @param address
     *            The starting byte address
     * @param length
     *            The length of the record in bytes
     */
    public Handle(int address, int length) {
        this.address = address;
        this.length = length;
    }


    /**
     * Default constructor for Handle
     */
    public Handle() {
        this(-1, -1);
    }


    /**
     * Returns the address
     * 
     * @return the address
     */
    public int getAddress() {
        return address;
    }


    /**
     * Returns the length
     * 
     * @return the length
     */
    public int getLength() {
        return length;
    }

}
