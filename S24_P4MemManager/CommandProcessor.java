import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * Command file parser
 *
 * @author {Your Name Here}
 * @version {Put Something Here}
 */
public class CommandProcessor
{
    private SeminarDB myWorld;

    /**
     * Create a new CommandProcessor object.
     *
     * @param inWorld
     *            The world database we are processing commands for
     * @throws IOException
     */
    public CommandProcessor(SeminarDB inWorld)
        throws IOException
    {
        myWorld = inWorld;
    }



    /**
     * Read in command file line by line
     *
     * @param theFile
     *            The file that we are reading
     * @throws FileNotFoundException
     * @throws IOException
     * @return Always true
     */
    public boolean readCmdFile(File theFile)
        throws IOException
    {
        int sID;
        String stitle;
        String sdate;
        int slength;
        int sx;
        int sy;
        int scost;
        String skeystring;
        String[] skeywords;
        String sdescription;
        String ptype;
        int cnt;
        try (Scanner sc = new Scanner(theFile))
        { // Create our new scanner
            while (sc.hasNext())
            { // While the scanner has information to read
                String cmd = sc.next(); // Read the next term
                switch (cmd)
                {
                    case "insert":
                        sID = sc.nextInt();
                        sc.nextLine(); // Flush the end-of-line
                        stitle = sc.nextLine().trim();
                        sdate = sc.next().trim();
                        slength = sc.nextInt();
                        sx = sc.nextInt();
                        sy = sc.nextInt();
                        scost = sc.nextInt();
                        sc.nextLine(); // Flush the end-of-line
                        skeystring = sc.nextLine().trim();
                        List<String> temp = new ArrayList<String>();
                        try (Scanner keysc = new Scanner(skeystring))
                        { // Create our new scanner
                            while (keysc.hasNext())
                            {
                                String dum = keysc.next().trim();
                                temp.add(dum);
                            }
                        }
                        skeywords = temp.toArray(new String[0]);
                        sdescription = sc.nextLine().trim();
                        myWorld.insert(sID, stitle, sdate, slength, sx, sy,
                            scost, skeywords, sdescription);
                        break;
                    case "delete":// Found a delete command
                        sID = sc.nextInt();
                        myWorld.delete(sID);
                        break;
                    case "search": // Found a search command
                        sID = sc.nextInt();
                        myWorld.search(sID);
                        break;
                    case "print": // Found an intersections command
                        ptype = sc.next();
                        if (ptype.equals("hashtable"))
                        {
                            cnt = myWorld.hashprint();
                            System.out.println("total records: " + cnt);
                        }
                        else if (ptype.equals("blocks"))
                        {
                            myWorld.memmanprint();
                        }
                        else
                        {
                            System.out.println(
                                "Print command has bad parameter");
                        }
                        break;
                    default:// Found an unrecognized command
                        System.out.println("Unrecognized input: |" + cmd + "|");
                        break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }
}
