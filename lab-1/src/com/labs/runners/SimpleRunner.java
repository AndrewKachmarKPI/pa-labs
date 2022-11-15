package com.labs.runners;

import java.util.Scanner;
import static com.labs.general.FileConstants.*;
import static com.labs.general.GeneralMethods.*;
import static com.labs.algorithms.SimpleAlgorithm.mergeAllFiles;
import static com.labs.algorithms.SimpleAlgorithm.splitFiles;

public class SimpleRunner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("ENTER BATCH SIZE:");
        int batch = scanner.nextInt();

        long startGen = System.currentTimeMillis();
        generateInputFile(SMALL_FILE, SMALL_FILE_PATH);
        long endGen = System.currentTimeMillis();
        printTime("GENERATE", startGen, endGen);


        long startSplit = System.currentTimeMillis();
        splitFiles(batch);
        long endSplit = System.currentTimeMillis();
        printTime("SPLIT", startSplit, endSplit);


        long startMerge = System.currentTimeMillis();
        mergeAllFiles(batch);
        long endMerge = System.currentTimeMillis();
        printTime("MERGE", startMerge, endMerge);
    }
}
