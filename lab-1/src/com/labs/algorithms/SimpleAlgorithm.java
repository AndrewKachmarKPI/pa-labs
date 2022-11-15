package com.labs.algorithms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.labs.general.FileConstants.*;
import static com.labs.general.GeneralMethods.*;

public class SimpleAlgorithm {
    private static Long inputSize;

    static {
        try {
            inputSize = Files.size(Path.of(SMALL_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void splitFiles(int batch) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(SMALL_FILE_PATH))) {
            createFiles(batch, FILE_B);

            int currentFile = 0;
            List<String> series = new ArrayList<>();

            String currentLine = bufferedReader.readLine();
            series.add(currentLine);
            String previous = currentLine;

            while ((currentLine = bufferedReader.readLine()) != null) {
                if (series.size() > 1 && !comparePrevious(previous, currentLine)) {
                    writeToFileBulk(series, FILE_DIR + currentFile + FILE_B);
                    series = new ArrayList<>();
                    currentFile++;
                }
                if (currentFile >= batch) {
                    currentFile = 0;
                }
                series.add(currentLine);
                previous = currentLine;
            }
            writeToFileBulk(series, FILE_DIR + currentFile + FILE_B);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mergeAllFiles(int batch) {
        try {
            boolean direction = true;
            createFiles(batch, FILE_C);
            for (int i = 0; i < batch; i++) {
                System.out.println("FILE->"+i);
                printFile(FILE_DIR + i + FILE_B);
            }
            while (!isFullyMerged(batch, FILE_B, inputSize) || !isFullyMerged(batch, FILE_C, inputSize)) {
                String readFrom = direction ? FILE_B : FILE_C;
                String writeTo = direction ? FILE_C : FILE_B;
                mergeFiles(batch, readFrom, writeTo);

                for (int i = 0; i < batch; i++) {
                    System.out.println("FILE->"+i);
                    printFile(FILE_DIR + i + writeTo);
                }
                if (isFullyMerged(batch, writeTo, inputSize)) {
                    clearFiles(batch, readFrom);
                    break;
                }
                direction = !direction;
            }
            removeEmptyFiles(batch, FILE_B);
            removeEmptyFiles(batch, FILE_C);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printFile(String filePath) throws IOException {
        System.out.println(filePath + "-" + Files.readAllLines(Path.of(filePath)));
    }


    public static void mergeFiles(int batch, String readFrom, String writeTo) throws IOException {
        List<BufferedReader> readers = createReaders(batch, readFrom);
        clearFiles(batch, writeTo);

        List<String> elements = new ArrayList<>();
        int writeToIndex = 0;
        while (!isFilesRead(readers)) {
            Integer minElement = Integer.MIN_VALUE;
            Integer readerIndex = null;

            for (BufferedReader reader : readers) {
                String line = readLine(reader);
                if (line != null) {
                    int current = Integer.parseInt(line);
                    Integer last = elements.isEmpty() ? null : Integer.parseInt(elements.get(elements.size() - 1));
                    if ((elements.isEmpty() || current <= last) && (current > minElement)) {
                        minElement = current;
                        readerIndex = readers.indexOf(reader);
                    }
                }
            }

            if (readerIndex != null) {
                elements.add(String.valueOf(minElement));
                readers.get(readerIndex).readLine();
            } else {
                writeToFileBulk(elements, FILE_DIR + writeToIndex + writeTo);
                elements.clear();
                writeToIndex = (writeToIndex + 1) % batch;
            }
        }
        System.out.println("END WRITE");
        writeToFileBulk(elements, FILE_DIR + writeToIndex + writeTo);
        closeReaders(readers);
    }

}
