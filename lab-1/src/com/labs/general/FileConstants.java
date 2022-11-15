package com.labs.general;



public interface FileConstants {
    String FILE_DIR = "numbers/";

    String FILE_B = "file_b.txt";
    String FILE_C = "file_c.txt";

    Long SMALL_FILE = Long.valueOf("1100000");
    Long GB_FILE = (long) (11 * Math.pow(10, 7));
    Long BIG_FILE = (long) (11 * Math.pow(10, 7));

    String SMALL_FILE_PATH = "numbers/input-sm.txt";
    String GB_FILE_PATH = "numbers/input-gb.txt";
    String BIG_FILE_PATH = "numbers/input-bg.txt";
}
