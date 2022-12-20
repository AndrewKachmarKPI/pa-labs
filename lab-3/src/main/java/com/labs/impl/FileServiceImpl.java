package com.labs.impl;

import com.labs.domain.TreeNodeEntry;
import com.labs.services.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.labs.services.BTreeService.*;

public class FileServiceImpl implements FileService {
    public void saveAllEntryToFile(String filePath, List<TreeNodeEntry> entry) {
        List<String> entryForSave = entry.stream().map(TreeNodeEntry::getEntryForSave).collect(Collectors.toList());
        try {
            Files.write(Paths.get(filePath), entryForSave);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendEntryToFile(String filePath, TreeNodeEntry entry) {
        checkFileExist(filePath);
        try {
            Files.write(Paths.get(filePath), (entry.getEntryForSave() + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<TreeNodeEntry> getEntriesFromFile(String filePath) {
        checkFileExist(filePath);
        List<TreeNodeEntry> entries = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                String key = line.split(SEPARATOR)[KEY_INDEX];
                String value = line.split(SEPARATOR)[VALUE_INDEX];
                entries.add(new TreeNodeEntry(Integer.parseInt(key), value.trim()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entries;
    }

    public String getEmptyFile() {
        String fileName = UUID.randomUUID() + ".txt";
        try {
            Files.createFile(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create file");
        }
        return fileName;
    }

    public boolean isFileValid(String filePath) {
        boolean isFileValid = true;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                if (line.split(SEPARATOR).length != 2) {
                    isFileValid = false;
                }
                int key = Integer.parseInt(line.split(SEPARATOR)[KEY_INDEX]);
                String value = line.split(SEPARATOR)[VALUE_INDEX];
                if (isKeyValueInvalid(key, value)) {
                    isFileValid = false;
                }
                if (!isFileValid) {
                    break;
                }
            }
        } catch (IOException e) {
            isFileValid = false;
        }
        return isFileValid;
    }

    private boolean isKeyValueInvalid(int key, String value) {
        return key < 0 || value == null || value.isEmpty();
    }

    private void checkFileExist(String filePath) {
        if (!Files.exists(Paths.get(filePath))) {
            throw new RuntimeException("File not found");
        }
    }
}
