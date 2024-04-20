
/**
 * TODO: {Project Description Here}
 */

import java.io.File;
import java.io.IOException;

/**
 * The class containing the main method.
 *
 * @author Richard Martinez
 * @version 2024-04-20
 */

// On my honor:
// - I have not used source code obtained from another current or
//   former student, or any other unauthorized source, either
//   modified or unmodified.
//
// - All source code and documentation used in my program is
//   either my original work, or was derived by me from the
//   source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
//   anyone other than my partner (in the case of a joint
//   submission), instructor, ACM/UPE tutors or the TAs assigned
//   to this course. I understand that I may discuss the concepts
//   of this program with other students, and that another student
//   may help me debug my program so long as neither of us writes
//   anything during the discussion or modifies any computer file
//   during the discussion. I have violated neither the spirit nor
//   letter of this restriction.


public class SemManager {
    /**
     * @param args
     *     Command line parameters
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        // This is the main file for the program.
        // Seminar dum = new Seminar();
        System.out.println("This is working MemManager!");

        int initMemSize = Integer.parseInt(args[0]);
        int initHashSize = Integer.parseInt(args[1]);
        String commandFileName = args[2];
        
        File cmdFile = new File(commandFileName);

        SeminarDB database = new SeminarDB(initMemSize, initHashSize);
        CommandProcessor cmdProc = new CommandProcessor(database);
        
        cmdProc.readCmdFile(cmdFile);
        
//        int x = 1;
    }
}
