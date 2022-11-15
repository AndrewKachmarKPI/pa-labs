package com.labs.general;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.labs.general.FileConstants.*;

public class GeneralMethods {

    public static void generateInputFile(long length, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            for (int i = 0; i < length; i++) {
                String num = randomNumber();
                writer.append(num);
                if (i + 1 < length) {
                    writer.append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<BufferedReader> createReaders(int count, String filePath) throws FileNotFoundException {
        List<BufferedReader> bufferedReaders = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            bufferedReaders.add(new BufferedReader(new FileReader(FILE_DIR + i + filePath)));
        }
        return bufferedReaders;
    }
    public static List<BufferedWriter> createWriters(int count, String filePath) throws IOException {
        List<BufferedWriter> bufferedWriters = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            bufferedWriters.add(new BufferedWriter(new FileWriter(FILE_DIR + i + filePath, true)));
        }
        return bufferedWriters;
    }
    public static void closeReaders(List<BufferedReader> readers) {
        readers.forEach(reader -> {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public static void closeWriters(List<BufferedWriter> writers) {
        writers.forEach(reader -> {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public static void clearFiles(int count, String filePath) throws FileNotFoundException {
        for (int i = 0; i < count; i++) {
            PrintWriter writer = new PrintWriter(FILE_DIR + i + filePath);
            writer.print("");
            writer.close();
        }
    }
    public static boolean isFilesRead(List<BufferedReader> readers) throws IOException {
        for (BufferedReader reader : readers) {
            if (readLine(reader) != null) {
                return false;
            }
        }
        return true;
    }
    public static String readLine(BufferedReader reader) throws IOException {
        reader.mark(100);
        String line = reader.readLine();
        reader.reset();
        return line;
    }
    public static boolean comparePrevious(String previous, String current) {
        return Integer.parseInt(previous) > Integer.parseInt(current);
    }

    public static void quickSort(int[] arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);
            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    private static int partition(int[] arr, int begin, int end) {
        int pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;

                int swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }

        int swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

    public static void createFiles(int batch, String filePath) throws IOException {
        for (int i = 0; i < batch; i++) {
            File file = new File(FILE_DIR + i + filePath);
            file.createNewFile();
        }
    }

    public static String randomNumber() {
        return String.valueOf(new Random().nextInt());
    }


    public static void writeToFileBulk(List<String> numbers, String filePath) throws IOException {
        Files.write(Path.of(filePath), numbers, StandardOpenOption.APPEND);
    }

    public static void rewriteToFile(List<String> numbers, String filePath) throws IOException {
        Files.write(Path.of(filePath), numbers);
    }

    public static void removeEmptyFiles(int batch, String filePath) throws IOException {
        for (int i = 0; i < batch; i++) {
            Path path = Path.of(FILE_DIR + i + filePath);
            if (Files.size(path) == 0) {
                Files.delete(path);
            }
        }
    }

    public static boolean isFullyMerged(int batch, String filePath, long inputSize) throws IOException {
        List<Long> sizes = new ArrayList<>();
        for (int i = 0; i < batch; i++) {
            long fileSize = Files.size(Path.of(FILE_DIR + i + filePath));
            if (fileSize >= inputSize && !sizesZero(sizes)) {
                return true;
            }
            sizes.add(fileSize);
        }
        return false;
    }

    public static boolean sizesZero(List<Long> numbers) {
        return numbers.stream().anyMatch(aLong -> aLong > 0);
    }

    public static void printTime(String method, long start, long end) {
        long millis = end - start;
        System.out.println(method + " TIME -> " + millis + " ms");
    }
}
