package com.labs.services;


import com.labs.domain.TreeNodeEntry;

import java.util.List;

public interface BTreeService {
    Integer TREE_LEVEL = 10;
    Integer KEY_INDEX = 0;
    Integer VALUE_INDEX = 1;
    Integer DEFAULT_LENGTH = 10000;

    void insertEntry(String filePath, TreeNodeEntry entry);
    void deleteEntry(String filePath, Integer key);
    TreeNodeEntry getEntryByKey(Integer key);
    List<TreeNodeEntry> getAllEntries(String filePath);
    void loadTree(String filePath);
    String getRandomTree(int length);
}
