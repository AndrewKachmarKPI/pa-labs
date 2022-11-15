package com.labs.runners;

import java.util.Scanner;

import static com.labs.algorithms.OptimizedAlgorithm.sortFiles;
import static com.labs.algorithms.OptimizedAlgorithm.splitFiles;
import static com.labs.algorithms.OptimizedAlgorithm.mergeAllFiles;
import static com.labs.general.FileConstants.*;
import static com.labs.general.GeneralMethods.*;

public class OptimizedRunner {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("ENTER BATCH SIZE:");
        int batch = scanner.nextInt();

        long startGen = System.currentTimeMillis();
        generateInputFile(GB_FILE, GB_FILE_PATH);
        long endGen = System.currentTimeMillis();
        printTime("GENERATE", startGen, endGen);


        long startSplit = System.currentTimeMillis();
        splitFiles(batch, GB_FILE_PATH);
        long endSplit = System.currentTimeMillis();
        printTime("SPLIT", startSplit, endSplit);


        long startSort = System.currentTimeMillis();
        sortFiles(batch, FILE_B);
        long endSort = System.currentTimeMillis();
        printTime("SORT", startSort, endSort);


        long startMerge = System.currentTimeMillis();
        mergeAllFiles(batch);
        long endMerge = System.currentTimeMillis();
        printTime("MERGE", startMerge, endMerge);

        printTime("TOTAL", startGen, endMerge);
    }
}
