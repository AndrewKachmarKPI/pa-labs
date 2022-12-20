package com.labs.services;

import com.labs.domain.TreeNodeEntry;

import java.util.List;

public interface FileService {
    String SEPARATOR = ";";
    void saveAllEntryToFile(String filePath, List<TreeNodeEntry> entry);
    void appendEntryToFile(String filePath, TreeNodeEntry entry);
    List<TreeNodeEntry> getEntriesFromFile(String filePath);
    String getEmptyFile();
    boolean isFileValid(String filePath);
}
