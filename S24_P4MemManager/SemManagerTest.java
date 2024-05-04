import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import student.TestCase;

/**
 * @author Richard Martinez
 * @version 2024-04-20
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
     * @throws IOException 
     */
    public void testMInitx() throws IOException
    {
        SemManager sem = new SemManager();
        assertNotNull(sem);
//        SemManager.main(null);
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
    
    /**
     * Test simple extension files
     * @throws IOException 
     */
    public void testSimpExtension() throws IOException {
        String[] args = new String[3];
        args[0] = "1024";
        args[1] = "16";
        args[2] = "P1SimpSampleExtension.txt";

        SemManager.main(args);
        String output = systemOut().getHistory();
        String referenceOutput = readFile("P1SimpSampleExtension_out.txt");

        assertFuzzyEquals(referenceOutput, output);
    }
    
    /**
     * Test P4 sample files
     * @throws IOException 
     */
    public void testP4Sample() throws IOException {
        String[] args = new String[3];
        args[0] = "512";
        args[1] = "4";
        args[2] = "P4Sample_input.txt";

        SemManager.main(args);
        String output = systemOut().getHistory();
        String referenceOutput = readFile("P4Sample_output.txt");

        assertFuzzyEquals(referenceOutput, output);
    }
    
    /**
     * Test the capstone files
     * @throws IOException 
     */
    public void testCapstone() throws IOException {
        String[] args = new String[3];
        args[0] = "512";
        args[1] = "8";
        args[2] = "capstone_in.txt";

        SemManager.main(args);
        String output = systemOut().getHistory();
        String referenceOutput = readFile("capstone_out.txt");

        assertFuzzyEquals(referenceOutput, output);
    }

}

