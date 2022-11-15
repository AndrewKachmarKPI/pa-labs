package com.labs.algorithms;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.labs.general.FileConstants.*;
import static com.labs.general.GeneralMethods.*;

public class OptimizedAlgorithm {
    public static void splitFiles(int batch, String filePath) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            createFiles(batch, FILE_B);
            List<BufferedWriter> writers = createWriters(batch, FILE_B);

            int currentFile = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                BufferedWriter currentWriter = writers.get(currentFile);
                currentWriter.write(line + "\n");
                currentFile++;
                if (currentFile >= batch) {
                    currentFile = 0;
                }
            }
            closeWriters(writers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sortFiles(int batch, String filePath) {
        try {
            for (int i = 0; i < batch; i++) {
                String path = FILE_DIR + i + filePath;
                BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
                List<String> lines = bufferedReader.lines().map(Integer::parseInt).sorted().map(String::valueOf).collect(Collectors.toList());
                rewriteToFile(lines, path);
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mergeAllFiles(int batch) {
        try {
            mergeFiles(batch, FILE_B, FILE_C);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void mergeFiles(int batch, String readFrom, String writeTo) throws IOException {
        List<BufferedReader> readers = createReaders(batch, readFrom);
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_DIR + 0 + writeTo, true));

        int finishedBuffers = 0;
        while (finishedBuffers != readers.size()) {
            int min = Integer.MAX_VALUE;
            Integer minIndex = null;
            for (BufferedReader reader : readers) {
                String line = readLine(reader);
                if (line != null && Integer.parseInt(line) < min) {
                    min = Integer.parseInt(line);
                    minIndex = readers.indexOf(reader);
                }
                if(line == null){
                    finishedBuffers++;
                }
            }
            if (minIndex != null) {
                writer.write(min + "\n");
                readers.get(minIndex).readLine();
            }
        }
        closeReaders(readers);
        writer.close();
    }

}
