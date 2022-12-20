package com.labs.impl;

import com.labs.domain.BTree;
import com.labs.domain.TreeNodeEntry;
import com.labs.services.BTreeService;
import com.labs.services.FileService;

import java.util.*;

public class BtreeServiceImpl implements BTreeService {
    private BTree bTree;
    private FileService fileService;

    public BtreeServiceImpl() {
        this.bTree = new BTree(TREE_LEVEL);
        this.fileService = new FileServiceImpl();
    }

    public void insertEntry(String filePath, TreeNodeEntry entry) {
        fileService.appendEntryToFile(filePath, entry);
        bTree.insertEntry(entry);
    }

    public void deleteEntry(String filePath, Integer key) {
        bTree.removeEntry(key);
        fileService.saveAllEntryToFile(filePath, bTree.getEntries());
    }

    public TreeNodeEntry getEntryByKey(Integer key) {
        return bTree.getEntryByKey(key);
    }

    public List<TreeNodeEntry> getAllEntries(String filePath) {
        return bTree.getEntries();
    }

    public void loadTree(String filePath) {
        loadTreeFromFile(filePath);
    }

    public String getRandomTree(int length) {
        String fileName = fileService.getEmptyFile();
        List<TreeNodeEntry> treeNodeEntries = new ArrayList<>();
        Set<Integer> keys = getRandomKeys(length);
        for (Integer key : keys) {
            treeNodeEntries.add(new TreeNodeEntry(key, UUID.randomUUID().toString()));
        }
        fileService.saveAllEntryToFile(fileName, treeNodeEntries);
        return fileName;
    }

    private Set<Integer> getRandomKeys(int length) {
        Random random = new Random();
        Set<Integer> keys = new LinkedHashSet<>();
        while (keys.size() < length) {
            Integer next = random.nextInt(99999) + 1;
            keys.add(next);
        }
        return keys;
    }

    private void loadTreeFromFile(String filePath) {
        List<TreeNodeEntry> entries = fileService.getEntriesFromFile(filePath);
        bTree = new BTree(TREE_LEVEL);
        bTree.insertAllEntry(entries);
    }
}
