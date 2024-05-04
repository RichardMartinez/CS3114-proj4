import java.util.ArrayList;
import student.TestCase;

/**
 * Test the methods of the HashEntry class.
 * 
 * @author Richard Martinez
 * 
 * @version 2024-05-01
 */
public class MemoryManagerTest extends TestCase {

    private MemoryManager memory;

    /**
     * Set up the test object
     */
    public void setUp() {
        memory = new MemoryManager(512);
    }


    /**
     * Test the nextPow2 method
     */
    public void testNextPow2() {
        int result;

        result = memory.nextPow2(1);
        assertEquals(result, 0);

        result = memory.nextPow2(2);
        assertEquals(result, 1);

        result = memory.nextPow2(3);
        assertEquals(result, 2);

        result = memory.nextPow2(4);
        assertEquals(result, 2);

        result = memory.nextPow2(5);
        assertEquals(result, 3);

        result = memory.nextPow2(9);
        assertEquals(result, 4);

        result = memory.nextPow2(17);
        assertEquals(result, 5);

        result = memory.nextPow2(33);
        assertEquals(result, 6);

        result = memory.nextPow2(127);
        assertEquals(result, 7);

        result = memory.nextPow2(200);
        assertEquals(result, 8);

        result = memory.nextPow2(500);
        assertEquals(result, 9);

        result = memory.nextPow2(1000);
        assertEquals(result, 10);
    }


    /**
     * Verify the structure of the initial FBL
     */
    public void testInitialFBL() {
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();

        String expected = "Freeblock List:\n" + "512: 0\n";

        assertFuzzyEquals(actual, expected);
    }


    /**
     * Test the sorted add method
     */
    public void testAddToListSorted() {
        ArrayList<Integer> list = new ArrayList<Integer>();

        memory.addToListSorted(list, 0);
        memory.addToListSorted(list, 4);
        memory.addToListSorted(list, 3);
        memory.addToListSorted(list, 5);
        memory.addToListSorted(list, 11);
        memory.addToListSorted(list, 7);
        memory.addToListSorted(list, 20);
        memory.addToListSorted(list, 15);

        // Assert it is sorted
        for (int i = 0; i < list.size() - 1; i++) {
            assertTrue(list.get(i) < list.get(i + 1));
        }
    }


    /**
     * Test a simple split
     */
    public void testOneSplit() {
        memory = new MemoryManager(128);

        // Do the split
        int targetBlockSize = 64;
        int blockN = memory.nextPow2(targetBlockSize);
        memory.split(blockN);

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();

        String expected = "Freeblock List:\n" + "64: 0 64\n";

        assertFuzzyEquals(actual, expected);
    }


    /**
     * Test splitting twice
     */
    public void testTwoSplits() {
        memory = new MemoryManager(128);

        // Do the split
        int targetBlockSize = 32;
        int blockN = memory.nextPow2(targetBlockSize);
        memory.split(blockN);

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();

        String expected = "Freeblock List:\n" + "32: 0 32\n" + "64: 64\n";

        assertFuzzyEquals(actual, expected);
    }


    /**
     * Test splitting all the way to N=0
     */
    public void testSplitAllTheWay() {
        memory = new MemoryManager(128);

        // Do the split
        int targetBlockSize = 1;
        int blockN = memory.nextPow2(targetBlockSize);
        memory.split(blockN);

// System.out.println("AFTER SPLIT ALL");
// memory.print();

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();

        String expected = "Freeblock List:\n" + "1: 0 1\n" + "2: 2\n" + "4: 4\n"
            + "8: 8\n" + "16: 16\n" + "32: 32\n" + "64: 64\n";

        assertFuzzyEquals(actual, expected);
    }


    /**
     * Test inserting and taking up all the space
     */
    public void testBasicInsert() {
        memory = new MemoryManager(128);

        byte[] space = new byte[128];
        int size = 128;

        Handle handle;
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), 128);

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();

        String expected = "Freeblock List:\n"
            + "There are no freeblocks in the memory pool\n";

        assertFuzzyEquals(actual, expected);
    }


    /**
     * Test inserting half the space
     */
    public void testHalfInsert() {
        memory = new MemoryManager(128);

        byte[] space = new byte[64];
        int size = 64;

        Handle handle;
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), 64);

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();

        String expected = "Freeblock List:\n" + "64: 64\n";

        assertFuzzyEquals(actual, expected);
    }


    /**
     * Test splitting all the way to 1 insert
     */
    public void testAllTheWayInsert() {
        memory = new MemoryManager(128);

        byte[] space = new byte[1];
        int size = 1;

        Handle handle;
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), 1);

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();

        String expected = "Freeblock List:\n" + "1: 1\n" + "2: 2\n" + "4: 4\n"
            + "8: 8\n" + "16: 16\n" + "32: 32\n" + "64: 64\n";

        assertFuzzyEquals(actual, expected);
    }


    /**
     * Test splitting at a non-zero position
     */
    public void testSplitAtPositionNonZero() {
        memory = new MemoryManager(128);

        int size;
        byte[] space;
        Handle handle;

        // Allocate 64 bytes
        // Should split once at position 0
        size = 64;
        space = new byte[size];
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), 64);

        // Allocate 16 bytes
        // Should split twice at position 64
        size = 16;
        space = new byte[size];
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 64);
        assertEquals(handle.getLength(), 16);

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();

        String expected = "Freeblock List:\n" + "16: 80\n" + "32: 96\n";

        assertFuzzyEquals(actual, expected);
    }


    /**
     * Test the get buddy pos method
     */
    public void testGetBuddyPos() {
        int position;
        int size;
        int buddyPos;

        position = 0;
        size = 64;
        buddyPos = memory.getBuddyPos(position, size);
        assertEquals(buddyPos, 64);
        assertTrue(memory.areBuddies(position, size, buddyPos, size));

        position = 64;
        size = 64;
        buddyPos = memory.getBuddyPos(position, size);
        assertEquals(buddyPos, 0);
        assertTrue(memory.areBuddies(position, size, buddyPos, size));

        // Situation for basic resize
        position = 0;
        size = 128;
        buddyPos = memory.getBuddyPos(position, size);
        assertEquals(buddyPos, 128);
        assertTrue(memory.areBuddies(position, size, buddyPos, size));
    }


    /**
     * Test the are buddies method
     */
    public void testAreBuddies() {
        int b1start;
        int b1size;
        int b2start;
        int b2size;

        b1start = 0;
        b1size = 32;
        b2start = 32;
        b2size = 32;
        assertTrue(memory.areBuddies(b1start, b1size, b2start, b2size));

        // Different sizes are not buddies
        b1start = 0;
        b1size = 32;
        b2start = 32;
        b2size = 16;
        assertFalse(memory.areBuddies(b1start, b1size, b2start, b2size));

        // Tests from piazza
        b1start = 0;
        b1size = 4;
        b2start = 4;
        b2size = 4;
        assertTrue(memory.areBuddies(b1start, b1size, b2start, b2size));

        b1start = 8;
        b1size = 4;
        b2start = 12;
        b2size = 4;
        assertTrue(memory.areBuddies(b1start, b1size, b2start, b2size));

        b1start = 4;
        b1size = 4;
        b2start = 8;
        b2size = 4;
        assertFalse(memory.areBuddies(b1start, b1size, b2start, b2size));
    }


    /**
     * Test resizing an empty memory
     */
    public void testEmptyResize() {
        memory = new MemoryManager(128);

        // Resize and Merge
        memory.resize();

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();

        String expected = "Freeblock List:\n" + "256: 0\n";

        assertFuzzyEquals(actual, expected);
    }


    /**
     * Test adding whole memory, remove whole memory
     */
    public void testBasicRemove() {
        memory = new MemoryManager(256);

        int size;
        byte[] space;
        Handle handle;

        String actual;
        String expected;

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();

        expected = "Freeblock List:\n" + "256: 0\n";

        assertFuzzyEquals(actual, expected);

        assertEquals(memory.numFreeBytes(), 256);

        // Insert 256 bytes
        size = 256;
        space = new byte[size];
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), size);

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();

        expected = "Freeblock List:\n"
            + "There are no freeblocks in the memory pool\n";

        assertFuzzyEquals(actual, expected);

        assertEquals(memory.numFreeBytes(), 0);

        // Remove it
        memory.remove(handle);

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();

        expected = "Freeblock List:\n" + "256: 0\n";

        assertFuzzyEquals(actual, expected);

        assertEquals(memory.numFreeBytes(), 256);
    }


    /**
     * Same thing, only half memory
     */
    public void testHalfRemove() {
        memory = new MemoryManager(256);

        int size;
        byte[] space;
        Handle handle;

        String actual;
        String expected;

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();

        expected = "Freeblock List:\n" + "256: 0\n";

        assertFuzzyEquals(actual, expected);

        assertEquals(memory.numFreeBytes(), 256);

        // Insert 128 bytes
        size = 128;
        space = new byte[size];
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), size);

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();

        expected = "Freeblock List:\n" + "128: 128\n";

        assertFuzzyEquals(actual, expected);

        assertEquals(memory.numFreeBytes(), 128);

        // Remove it
        memory.remove(handle);

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();

        expected = "Freeblock List:\n" + "256: 0\n";

        assertFuzzyEquals(actual, expected);

        assertEquals(memory.numFreeBytes(), 256);
    }


    /**
     * Test removing with a bunch of merges
     */
    public void testRemoveAllTheWay() {
        memory = new MemoryManager(256);

        int size;
        byte[] space;
        Handle handle;

        String actual;
        String expected;

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();

        expected = "Freeblock List:\n" + "256: 0\n";

        assertFuzzyEquals(actual, expected);

        assertEquals(memory.numFreeBytes(), 256);

        // Insert 1 byte
        size = 1;
        space = new byte[size];
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), size);

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();

        expected = "Freeblock List:\n" + "1: 1\n" + "2: 2\n" + "4: 4\n"
            + "8: 8\n" + "16: 16\n" + "32: 32\n" + "64: 64\n" + "128: 128\n";

        assertFuzzyEquals(actual, expected);

        assertEquals(memory.numFreeBytes(), 255);

        // Remove it
        memory.remove(handle);

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();

        expected = "Freeblock List:\n" + "256: 0\n";

        assertFuzzyEquals(actual, expected);

        assertEquals(memory.numFreeBytes(), 256);
    }


    /**
     * Test inserting and removing non power of 2 sizes
     */
    public void testNonPow2Sizes() {
        memory = new MemoryManager(256);

        int size;
        byte[] space;
        Handle handle;

        String actual;
        String expected;

        // Insert 10 bytes -> blocksize 16
        size = 10;
        space = new byte[size];
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), size);
        assertEquals(memory.numFreeBytes(), 240);

        // Insert 30 bytes -> blocksize 32
        size = 30;
        space = new byte[size];
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 32);
        assertEquals(handle.getLength(), size);
        assertEquals(memory.numFreeBytes(), 208);

        // Insert 60 bytes -> blocksize 64
        size = 60;
        space = new byte[size];
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 64);
        assertEquals(handle.getLength(), size);
        assertEquals(memory.numFreeBytes(), 144);

        // Insert 61 bytes -> blocksize 64
        size = 61;
        space = new byte[size];
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 128);
        assertEquals(handle.getLength(), size);
        assertEquals(memory.numFreeBytes(), 80);

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();

        expected = "Freeblock List:\n" + "16: 16\n" + "64: 192\n";

        assertFuzzyEquals(actual, expected);

        // Start removing stuff

        // Remove 10
        handle = new Handle(0, 10);
        memory.remove(handle);
        assertEquals(memory.numFreeBytes(), 96);

        // Verify Structure
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();
        expected = "Freeblock List:\n" + "32: 0\n" + "64: 192\n";
        assertFuzzyEquals(actual, expected);

        // Remove 60
        handle = new Handle(64, 60);
        memory.remove(handle);
        assertEquals(memory.numFreeBytes(), 160);

        // Verify Structure
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();
        expected = "Freeblock List:\n" + "32: 0\n" + "64: 64 192\n";
        assertFuzzyEquals(actual, expected);

        // Remove 61
        handle = new Handle(128, 61);
        memory.remove(handle);
        assertEquals(memory.numFreeBytes(), 224);

        // Verify Structure
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();
        expected = "Freeblock List:\n" + "32: 0\n" + "64: 64\n" + "128: 128\n";
        assertFuzzyEquals(actual, expected);

        // Remove 30
        handle = new Handle(32, 30);
        memory.remove(handle);
        assertEquals(memory.numFreeBytes(), 256);

        // Verify Structure
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();
        expected = "Freeblock List:\n" + "256: 0\n";
        assertFuzzyEquals(actual, expected);
    }


    /**
     * Place a 10 byte array into memory
     * Then get it back
     */
    public void testBasicGet() {
        memory = new MemoryManager(32);

        int size;
        byte[] space;
        Handle handle;
        boolean success;

        size = 10;
        space = new byte[size];
        for (int i = 0; i < size; i++) {
            space[i] = (byte)i;
        }

        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), 10);
        assertEquals(memory.numFreeBytes(), 16);

        // Try getting it back
        byte[] space2 = new byte[10];
        success = memory.get(space2, handle, size);
        assertTrue(success);

        // Verify the arrays are the same
        for (int i = 0; i < size; i++) {
            assertEquals(space[i], space2[i]);
        }
    }


    /**
     * Test failed get
     */
    public void testFailedGet() {
        memory = new MemoryManager(32);

        int size;
        byte[] space;
        Handle handle;
        boolean success;

        size = 10;
        space = new byte[size];
        for (int i = 0; i < size; i++) {
            space[i] = (byte)i;
        }

        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), 10);
        assertEquals(memory.numFreeBytes(), 16);

        // Try getting it back
        size = 15;
        byte[] space2 = new byte[size];
        success = memory.get(space2, handle, size);
        assertFalse(success);
    }


    /**
     * Test the can insert method
     */
    public void testCanInsert() {
        memory = new MemoryManager(32);

        assertEquals(memory.numFreeBytes(), 32);

        int size;
        int blockN;
        byte[] space;
        Handle handle;

        // Empty Memory
        size = 8;
        blockN = memory.nextPow2(size);
        assertTrue(memory.canInsert(blockN));

        size = 16;
        blockN = memory.nextPow2(size);
        assertTrue(memory.canInsert(blockN));

        size = 32;
        blockN = memory.nextPow2(size);
        assertTrue(memory.canInsert(blockN));

        size = 64;
        blockN = memory.nextPow2(size);
        assertFalse(memory.canInsert(blockN));

        // Half Memory
        size = 16;
        space = new byte[size];
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), 16);
        assertEquals(memory.numFreeBytes(), 16);

        size = 8;
        blockN = memory.nextPow2(size);
        assertTrue(memory.canInsert(blockN));

        size = 16;
        blockN = memory.nextPow2(size);
        assertTrue(memory.canInsert(blockN));

        size = 32;
        blockN = memory.nextPow2(size);
        assertFalse(memory.canInsert(blockN));

        size = 64;
        blockN = memory.nextPow2(size);
        assertFalse(memory.canInsert(blockN));

        memory.remove(handle); // Clear memory
        assertEquals(memory.numFreeBytes(), 32);

        // Split Memory
        // Insert 3 8's, clear 2nd 8
        size = 8;
        space = new byte[size];
        memory.insert(space, size);
        handle = memory.insert(space, size);
        memory.insert(space, size);
        memory.remove(handle);
        assertEquals(memory.numFreeBytes(), 16);

        size = 8;
        blockN = memory.nextPow2(size);
        assertTrue(memory.canInsert(blockN));

        size = 16;
        blockN = memory.nextPow2(size);
        assertFalse(memory.canInsert(blockN));

        size = 32;
        blockN = memory.nextPow2(size);
        assertFalse(memory.canInsert(blockN));

        size = 64;
        blockN = memory.nextPow2(size);
        assertFalse(memory.canInsert(blockN));

        // Clear memory
        handle = new Handle(0, 8);
        memory.remove(handle);
        handle = new Handle(16, 8);
        memory.remove(handle);
        assertEquals(memory.numFreeBytes(), 32);

        // Full memory
        size = 32;
        space = new byte[size];
        handle = memory.insert(space, size);
        assertEquals(memory.numFreeBytes(), 0);

        size = 8;
        blockN = memory.nextPow2(size);
        assertFalse(memory.canInsert(blockN));

        size = 16;
        blockN = memory.nextPow2(size);
        assertFalse(memory.canInsert(blockN));

        size = 32;
        blockN = memory.nextPow2(size);
        assertFalse(memory.canInsert(blockN));

        size = 64;
        blockN = memory.nextPow2(size);
        assertFalse(memory.canInsert(blockN));
    }


    /**
     * Test auto resize when inserting blockN > this.N
     */
    public void testAutoResize1() {
        memory = new MemoryManager(32);

        int size;
        byte[] space;
        Handle handle;

        assertEquals(memory.getCapacity(), 32);

        // Try to insert 64
        size = 64;
        space = new byte[size];
        handle = memory.insert(space, size); // Should resize
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), 64);

        assertEquals(memory.getCapacity(), 64);
        assertEquals(memory.numFreeBytes(), 0);

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();

        String expected = "Freeblock List:\n"
            + "There are no freeblocks in the memory pool\n";

        assertFuzzyEquals(actual, expected);
    }


    /**
     * Test auto resize when inserting blockN > this.N by 2
     */
    public void testAutoResize2() {
        memory = new MemoryManager(32);

        int size;
        byte[] space;
        Handle handle;

        assertEquals(memory.getCapacity(), 32);

        // Try to insert 128
        size = 128;
        space = new byte[size];
        handle = memory.insert(space, size); // Should resize
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), 128);

        assertEquals(memory.getCapacity(), 128);
        assertEquals(memory.numFreeBytes(), 0);

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();

        String expected = "Freeblock List:\n"
            + "There are no freeblocks in the memory pool\n";

        assertFuzzyEquals(actual, expected);
    }


    /**
     * Test auto resize when inserting blockN as a non power of 2
     */
    public void testAutoResizeNonPow2() {
        memory = new MemoryManager(32);

        int size;
        byte[] space;
        Handle handle;

        assertEquals(memory.getCapacity(), 32);

        // Try to insert 60 -> 64
        size = 60;
        space = new byte[size];
        handle = memory.insert(space, size); // Should resize
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), 60);

        assertEquals(memory.getCapacity(), 64);
        assertEquals(memory.numFreeBytes(), 0);

        // Verify the structure
        systemOut().clearHistory();
        memory.print();
        String actual = systemOut().getHistory();

        String expected = "Freeblock List:\n"
            + "There are no freeblocks in the memory pool\n";

        assertFuzzyEquals(actual, expected);
    }


    /**
     * Test resize when we have enough bytes, but no space
     */
    public void testAutoResizeFrag() {
        memory = new MemoryManager(32);

        int size;
        byte[] space;
        Handle handle;

        // Insert 3 8's, remove 2nd 8
        size = 8;
        space = new byte[size];
        memory.insert(space, size);
        handle = memory.insert(space, size);
        memory.insert(space, size);
        memory.remove(handle);

        // We have enough bytes
        // Try to insert 16
        // But no space, must resize
        assertEquals(memory.numFreeBytes(), 16);
        assertEquals(memory.getCapacity(), 32);

        size = 16;
        space = new byte[size];
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 32);
        assertEquals(handle.getLength(), 16);
        assertEquals(memory.numFreeBytes(), 32);
        assertEquals(memory.getCapacity(), 64);

        // Verify Structure
        String actual;
        String expected;
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();
        expected = "Freeblock List:\n" + "8: 8 24\n" + "16: 48\n";
        assertFuzzyEquals(actual, expected);
    }


    /**
     * Test resizing on full mem
     */
    public void testAutoResizeFullMem() {
        memory = new MemoryManager(32);

        int size;
        byte[] space;
        Handle handle;

        // Fill memory
        size = 32;
        space = new byte[size];
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 0);
        assertEquals(handle.getLength(), 32);

        assertEquals(memory.numFreeBytes(), 0);
        assertEquals(memory.getCapacity(), 32);

        // Insert 16
        size = 16;
        space = new byte[size];
        handle = memory.insert(space, size);
        assertEquals(handle.getAddress(), 32);
        assertEquals(handle.getLength(), 16);

        assertEquals(memory.numFreeBytes(), 16);
        assertEquals(memory.getCapacity(), 64);

        // Verify Structure
        String actual;
        String expected;
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();
        expected = "Freeblock List:\n" + "16: 48\n";
        assertFuzzyEquals(actual, expected);
    }


    /**
     * Make sure resizes keeps old memory
     */
    public void testResizeKeepMemContents() {
        memory = new MemoryManager(32);

        int size = 8;
        byte[] space = new byte[size];
        Handle handle1;
        Handle handle2;

        // Build record arrays
        byte[] record1 = new byte[size];
        byte[] record2 = new byte[size];
        for (int i = 0; i < size; i++) {
            record1[i] = (byte)(i + 1);
            record2[i] = (byte)(8 - i);
        }

        // Insert 8 array into Handle(8, 8)
        size = 8;
        memory.insert(space, size);
        handle1 = memory.insert(record1, size);
        memory.insert(space, size);
        memory.insert(space, size);

        // Insert to cause resize
        handle2 = memory.insert(record2, size);

        // Make sure you can get both records back
        memory.get(space, handle1, size);
        for (int i = 0; i < size; i++) {
            assertEquals(space[i], (byte)(i + 1));
        }

        memory.get(space, handle2, size);
        for (int i = 0; i < size; i++) {
            assertEquals(space[i], (byte)(8 - i));
        }
    }


    /**
     * Capstone test for MemoryManager
     */
    public void testCapstone() {
        memory = new MemoryManager(128);

        int size;
        byte[] space;

        // Generate records
        size = 7;
        byte[] record7 = new byte[size];
        for (int i = 0; i < size; i++) {
            record7[i] = (byte)(i + 1);
        }
        Handle handle7;

        size = 20;
        byte[] record20 = new byte[size];
        for (int i = 0; i < size; i++) {
            record20[i] = (byte)(i + 1);
        }
        Handle handle20;

        size = 15;
        byte[] record15 = new byte[size];
        for (int i = 0; i < size; i++) {
            record15[i] = (byte)(i + 1);
        }
        Handle handle15;

        size = 23;
        byte[] record23 = new byte[size];
        for (int i = 0; i < size; i++) {
            record23[i] = (byte)(i + 1);
        }
        Handle handle23;

        size = 40;
        byte[] record40 = new byte[size];
        for (int i = 0; i < size; i++) {
            record40[i] = (byte)(i + 1);
        }
        Handle handle40;

        size = 30;
        byte[] record30 = new byte[size];
        for (int i = 0; i < size; i++) {
            record30[i] = (byte)(i + 1);
        }
        Handle handle30;

        size = 29;
        byte[] record29 = new byte[size];
        for (int i = 0; i < size; i++) {
            record29[i] = (byte)(i + 1);
        }
        Handle handle29;

        size = 100;
        byte[] record100 = new byte[size];
        for (int i = 0; i < size; i++) {
            record100[i] = (byte)(i + 1);
        }
        Handle handle100;

        size = 8;
        byte[] record8 = new byte[size];
        for (int i = 0; i < size; i++) {
            record8[i] = (byte)(i + 1);
        }
        Handle handle8;

        // Insert 4 Records
        // Sizes: 7, 20, 15, 23
        handle7 = memory.insert(record7, 7);
        assertEquals(handle7.getAddress(), 0);
        assertEquals(handle7.getLength(), 7);

        handle20 = memory.insert(record20, 20);
        assertEquals(handle20.getAddress(), 32);
        assertEquals(handle20.getLength(), 20);

        handle15 = memory.insert(record15, 15);
        assertEquals(handle15.getAddress(), 16);
        assertEquals(handle15.getLength(), 15);

        handle23 = memory.insert(record23, 23);
        assertEquals(handle23.getAddress(), 64);
        assertEquals(handle23.getLength(), 23);

        // Verify structure, capacity, freebytes
        assertEquals(memory.getCapacity(), 128);
        assertEquals(memory.numFreeBytes(), 40);

        // Verify Structure
        String actual;
        String expected;
        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();
        expected = "Freeblock List:\n" + "8: 8\n" + "32: 96\n";
        assertFuzzyEquals(actual, expected);

        // Insert to force resize (40 -> 64)
        handle40 = memory.insert(record40, 40);
        assertEquals(handle40.getAddress(), 128);
        assertEquals(handle40.getLength(), 40);

        // Verify structure, capacity, freebytes
        assertEquals(memory.getCapacity(), 256);
        assertEquals(memory.numFreeBytes(), 104);

        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();
        expected = "Freeblock List:\n" + "8: 8\n" + "32: 96\n" + "64: 192\n";
        assertFuzzyEquals(actual, expected);

        // Get all those records back
        size = 7;
        space = new byte[size];
        memory.get(space, handle7, size);
        for (int i = 0; i < size; i++) {
            assertEquals(space[i], (byte)(i + 1));
        }

        size = 15;
        space = new byte[size];
        memory.get(space, handle15, size);
        for (int i = 0; i < size; i++) {
            assertEquals(space[i], (byte)(i + 1));
        }

        size = 20;
        space = new byte[size];
        memory.get(space, handle20, size);
        for (int i = 0; i < size; i++) {
            assertEquals(space[i], (byte)(i + 1));
        }

        size = 23;
        space = new byte[size];
        memory.get(space, handle23, size);
        for (int i = 0; i < size; i++) {
            assertEquals(space[i], (byte)(i + 1));
        }

        size = 40;
        space = new byte[size];
        memory.get(space, handle40, size);
        for (int i = 0; i < size; i++) {
            assertEquals(space[i], (byte)(i + 1));
        }

        // Remove 23 and 40
        memory.remove(handle23);
        memory.remove(handle40);

        // Verify structure, capacity, freebytes
        assertEquals(memory.getCapacity(), 256);
        assertEquals(memory.numFreeBytes(), 200);

        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();
        expected = "Freeblock List:\n" + "8: 8\n" + "64: 64\n" + "128: 128\n";
        assertFuzzyEquals(actual, expected);

        // Insert 30, 29, 100
        handle30 = memory.insert(record30, 30);
        assertEquals(handle30.getAddress(), 64);
        assertEquals(handle30.getLength(), 30);

        handle29 = memory.insert(record29, 29);
        assertEquals(handle29.getAddress(), 96);
        assertEquals(handle29.getLength(), 29);

        handle100 = memory.insert(record100, 100);
        assertEquals(handle100.getAddress(), 128);
        assertEquals(handle100.getLength(), 100);

        // Verify structure, capacity, freebytes
        assertEquals(memory.getCapacity(), 256);
        assertEquals(memory.numFreeBytes(), 8);

        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();
        expected = "Freeblock List:\n" + "8: 8\n";
        assertFuzzyEquals(actual, expected);

        // Get all records still in memory
        size = 7;
        space = new byte[size];
        memory.get(space, handle7, size);
        for (int i = 0; i < size; i++) {
            assertEquals(space[i], (byte)(i + 1));
        }

        size = 15;
        space = new byte[size];
        memory.get(space, handle15, size);
        for (int i = 0; i < size; i++) {
            assertEquals(space[i], (byte)(i + 1));
        }

        size = 20;
        space = new byte[size];
        memory.get(space, handle20, size);
        for (int i = 0; i < size; i++) {
            assertEquals(space[i], (byte)(i + 1));
        }

        size = 30;
        space = new byte[size];
        memory.get(space, handle30, size);
        for (int i = 0; i < size; i++) {
            assertEquals(space[i], (byte)(i + 1));
        }

        size = 29;
        space = new byte[size];
        memory.get(space, handle29, size);
        for (int i = 0; i < size; i++) {
            assertEquals(space[i], (byte)(i + 1));
        }

        size = 100;
        space = new byte[size];
        memory.get(space, handle100, size);
        for (int i = 0; i < size; i++) {
            assertEquals(space[i], (byte)(i + 1));
        }

        // Insert 8 to make it full
        handle8 = memory.insert(record8, 8);
        assertEquals(handle8.getAddress(), 8);
        assertEquals(handle8.getLength(), 8);

        // Verify final structure, capacity, freebytes
        assertEquals(memory.getCapacity(), 256);
        assertEquals(memory.numFreeBytes(), 0);

        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();
        expected = "Freeblock List:\n"
            + "There are no freeblocks in the memory pool\n";
        assertFuzzyEquals(actual, expected);

        // Remove all
        memory.remove(handle7);
        memory.remove(handle8);
        memory.remove(handle15);
        memory.remove(handle20);
        memory.remove(handle30);
        memory.remove(handle29);
        memory.remove(handle100);

        // Verify empty
        assertEquals(memory.getCapacity(), 256);
        assertEquals(memory.numFreeBytes(), 256);

        systemOut().clearHistory();
        memory.print();
        actual = systemOut().getHistory();
        expected = "Freeblock List:\n" + "256: 0\n";
        assertFuzzyEquals(actual, expected);
    }

}
