
/**
 * Project 4 Seminar Memory Manager
 * 
 * Compiled using JRE 11 for CS 3114
 * Operating System: Windows 11
 * IDE: Eclipse
 * Date Completed: 05-04-2024
 * Created By: Richard Martinez
 * 
 * In this project, I implemented a seminar database
 * using a memory manager and a hash table. The memory
 * manager uses the buddy system which allows for splitting
 * and merging of memory blocks but can result in internal
 * fragmentation. The closed hash table implements the double
 * hashing technique which minimizes clustering. Both data
 * structure automatically resize when necessary to meet project
 * requirements.
 * 
 * This is the final project for CS 3114.
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
// former student, or any other unauthorized source, either
// modified or unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class SemManager {
    /**
     * @param args
     *            Command line parameters
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // This is the main file for the program.
        // Seminar dum = new Seminar();
        // System.out.println("This is working MemManager!");

        int initMemSize = Integer.parseInt(args[0]);
        int initHashSize = Integer.parseInt(args[1]);
        String commandFileName = args[2];

        File cmdFile = new File(commandFileName);

        SeminarDB database = new SeminarDB(initMemSize, initHashSize);
        CommandProcessor cmdProc = new CommandProcessor(database);

        cmdProc.readCmdFile(cmdFile);
    }
}
