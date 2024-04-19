import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import student.TestCase;

/**
 * @author {Your Name Here}
 * @version {Put Something Here}
 */
public class SemManagerTest extends TestCase {
    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp() {
        // Nothing here
    }

    /**
     * Read contents of a file into a string
     * @param path File name
     * @return the string
     * @throws IOException
     */
    static String readFile(String path)
        throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }


    /**
     * This method is simply to get coverage of the class declaration.
     */
    public void testMInitx()
    {
        SemManager sem = new SemManager();
        assertNotNull(sem);
        SemManager.main(null);
    }

    /**
     * Full parser test
     * @throws IOException
     */
    public void testparserfull()
        throws IOException
    {
        String[] args = new String[3];
        args[0] = "512";
        args[1] = "4";
        args[2] = "P1Sample_inputX.txt";

        SemManager.main(args);
        String output = systemOut().getHistory();
        String referenceOutput = readFile("P1Sample_outputX.txt");
        assertFuzzyEquals(referenceOutput, output);
    }
    
    /**
     * Simple parser test (input only)
     * @throws IOException
     */
    public void testparserinput()
        throws IOException
    {
        String[] args = new String[3];
        args[0] = "2048";
        args[1] = "16";
        args[2] = "P1SimpSample_inputX.txt";

        SemManager.main(args);
        String output = systemOut().getHistory();
        String referenceOutput = readFile("P1SimpSample_outputX.txt");
        assertFuzzyEquals(referenceOutput, output);
    }
}

