package com.labs.domain;


import java.util.ArrayList;
import java.util.List;

public class BTree {
    private BTreeNode rootNode;
    private final int treeLevel;
    private static final int LEFT_CHILD_DEFAULT_INDEX = 0;
    private static final int RIGHT_CHILD_DEFAULT_INDEX = 1;

    public BTree(int treeLevel) {
        this.treeLevel = treeLevel;
        this.rootNode = new BTreeNode(this.treeLevel, true);
    }

    public void insertAllEntry(List<TreeNodeEntry> entries) {
        entries.forEach(this::insertEntry);
    }

    public void insertEntry(TreeNodeEntry entry) {
        BTreeNode currentRoot = this.rootNode;
        if (!isUpdatePosition(this.rootNode, entry)) {
            if (currentRoot.isKeysFull()) {
                BTreeNode newNode = new BTreeNode(treeLevel, false);
                this.rootNode = newNode;
                this.rootNode.setChildAtIndex(0, currentRoot);
                splitNode(newNode, 0, currentRoot);
                insertNode(newNode, entry);
            } else {
                insertNode(currentRoot, entry);
            }
        }
    }

    private void splitNode(BTreeNode parentNode, int index, BTreeNode treeNode) {
        BTreeNode newNode = new BTreeNode(treeLevel, treeNode.isLeafNode());
        newNode.setNumberOfKeys(treeLevel - 1);
        copyNodeKeys(newNode, treeNode);
        if (!newNode.isLeafNode()) {
            copyChildNodes(newNode, treeNode);
        }
        treeNode.clearKeyValue();
        treeNode.setNumberOfKeys(treeLevel - 1);

        for (int i = parentNode.getNumberOfKeys(); i >= index + 1; i--) {
            parentNode.setChildAtIndex(i + 1, parentNode.getChildAtIndex(i));
        }
        parentNode.setChildAtIndex(index + 1, newNode);

        for (int i = parentNode.getNumberOfKeys() - 1; i >= index; i--) {
            parentNode.setEntryAtIndex(i + 1, parentNode, i);
        }
        parentNode.setEntryAtIndex(index, treeNode, treeLevel - 1);
        treeNode.clearEntryByIndex(treeLevel - 1);
        parentNode.increaseNumberOfKeys();
    }

    private void copyNodeKeys(BTreeNode nodeTo, BTreeNode nodeFrom) {
        for (int i = 0; i < treeLevel - 1; i++) {
            nodeTo.setEntryAtIndex(i, nodeFrom, i + treeLevel);
        }
    }

    private void copyChildNodes(BTreeNode nodeTo, BTreeNode nodeFrom) {
        for (int i = 0; i < treeLevel; i++) {
            nodeTo.setChildAtIndex(i, nodeFrom.getChildAtIndex(i + treeLevel));
        }
        nodeFrom.clearChild();
    }

    private void insertNode(BTreeNode treeNode, TreeNodeEntry entry) {
        int writeEntryIndex = treeNode.getNumberOfKeys() - 1;
        if (treeNode.isLeafNode()) {
            insertEntryToLeaf(treeNode, entry);
        } else {
            writeEntryIndex = getWritePositionInCurrentNode(entry, treeNode, writeEntryIndex);
            if (treeNode.isChildFull(writeEntryIndex)) {
                splitNode(treeNode, writeEntryIndex, treeNode.getChildAtIndex(writeEntryIndex));
                if (entry.getKey() > treeNode.getKeyAtIndex(writeEntryIndex)) {
                    writeEntryIndex++;
                }
            }
            insertNode(treeNode.getChildAtIndex(writeEntryIndex), entry);
        }
    }

    private void insertEntryToLeaf(BTreeNode treeNode, TreeNodeEntry entry) {
        int writeEntryIndex = treeNode.getNumberOfKeys() - 1;
        while (writeEntryIndex >= 0 && entry.getKey() < treeNode.getKeyAtIndex(writeEntryIndex)) {
            treeNode.setEntryAtIndex(writeEntryIndex + 1, treeNode, writeEntryIndex);
            writeEntryIndex--;
        }
        writeEntryIndex++;
        treeNode.setEntryAtIndex(writeEntryIndex, entry);
        treeNode.increaseNumberOfKeys();
    }

    private int getWritePositionInCurrentNode(TreeNodeEntry entry, BTreeNode treeNode, int writeEntryIndex) {
        while (writeEntryIndex >= 0 && entry.getKey() < treeNode.getKeyAtIndex(writeEntryIndex)) {
            writeEntryIndex--;
        }
        writeEntryIndex++;
        return writeEntryIndex;
    }

    public void removeEntry(int key) {
        removeNode(rootNode, key);
    }

    private void removeNode(BTreeNode treeNode, int key) {
        if (treeNode.isLeafNode()) {
            removeLeaf(treeNode, key);
        } else {
            int i = treeNode.getKeyWithBinarySearch(key);
            findAndRemoveNode(treeNode, key, i);
        }
    }

    private void removeLeaf(BTreeNode treeNode, int key) { //1
        int removeIndex = treeNode.getKeyWithBinarySearch(key);
        if (removeIndex != -1) {
            treeNode.removeChildIndex(removeIndex, LEFT_CHILD_DEFAULT_INDEX);
        }
    }

    private void findAndRemoveNode(BTreeNode treeNode, int key, int i) {
        if (i != -1) {
            findAndReplaceWithPredecessorOrSuccessor(treeNode, key, i); //2
        } else {
            i = treeNode.getSubtreeNodeIndex(key);
            BTreeNode childNode = treeNode.getChildAtIndex(i);
            findAndRemoveWithSiblings(treeNode, i, childNode);
            removeNode(childNode, key);
        }
    }

    private void findAndRemoveWithSiblings(BTreeNode treeNode, int i, BTreeNode childNode) {
        if (childNode.hasLessThanMax()) {
            BTreeNode leftSibling = treeNode.getLeftChildSibling(i);
            BTreeNode rightSibling = treeNode.getRightChildSibling(i);
            if (leftSibling != null && leftSibling.hasMoreThanMax()) {
                replaceWithLeftSibling(treeNode, i, childNode, leftSibling);
            } else if (rightSibling != null && rightSibling.hasMoreThanMax()) {
                replaceWithRightSibling(treeNode, i, childNode, rightSibling);
            } else {
                mergeSiblingsAndMoveRoot(treeNode, i, childNode, leftSibling, rightSibling);
            }
        }
    }

    private void findAndReplaceWithPredecessorOrSuccessor(BTreeNode treeNode, int key, int i) { // 2
        BTreeNode leftChild = treeNode.getChildAtIndex(i);
        BTreeNode rightChild = treeNode.getChildAtIndex(i + 1);
        if (leftChild.hasMoreThanMax()) { //2a predecessor
            movePredecessorOrSuccessor(treeNode, i, false, leftChild);
        } else if (rightChild.hasMoreThanMax()) { // 2b successor
            movePredecessorOrSuccessor(treeNode, i, true, rightChild);
        } else {
            joinChildAndReplaceMedian(leftChild, rightChild, treeNode, i, key);
        }
    }

    private void joinChildAndReplaceMedian(BTreeNode leftChild, BTreeNode rightChild, BTreeNode treeNode, int index, int key) {
        int medianKeyIndex = getMedianIndexAndMergeChildNodes(leftChild, rightChild);
        moveKeyAndInsertIntoMedian(treeNode, index, RIGHT_CHILD_DEFAULT_INDEX, leftChild, medianKeyIndex);
        removeNode(leftChild, key);
    }

    private void replaceWithRightSibling(BTreeNode treeNode, int i, BTreeNode childToNode, BTreeNode childFromNode) {
        childToNode.setEntryAtIndex(childToNode.getNumberOfKeys(), treeNode, i);
        if (!childToNode.isLeafNode()) {
            childToNode.setChildAtIndex(childToNode.getNumberOfKeys() + 1, childFromNode.getChildAtIndex(0));
        }
        childToNode.increaseNumberOfKeys();
        treeNode.setEntryAtIndex(i, childFromNode, 0);
        childFromNode.removeChildIndex(0, LEFT_CHILD_DEFAULT_INDEX);
    }

    private void replaceWithLeftSibling(BTreeNode treeNode, int i, BTreeNode childToNode, BTreeNode childFromNode) {
        childToNode.moveNodesFromSibling();
        childToNode.setEntryAtIndex(0, treeNode, i - 1);
        if (!childToNode.isLeafNode()) {
            childToNode.setChildAtIndex(0, childFromNode.getChildAtIndex(childFromNode.getNumberOfKeys()));
        }
        childToNode.increaseNumberOfKeys();
        treeNode.setEntryAtIndex(i - 1, childFromNode, childFromNode.getNumberOfKeys() - 1);
        childFromNode.removeChildIndex(childFromNode.getNumberOfKeys() - 1, RIGHT_CHILD_DEFAULT_INDEX);
    }

    private void mergeSiblingsAndMoveRoot(BTreeNode treeNode, int i, BTreeNode childNode, BTreeNode leftSibling, BTreeNode rightSibling) {
        if (leftSibling != null) {
            int medianKeyIndex = getMedianIndexAndMergeChildNodes(childNode, leftSibling);
            moveKeyAndInsertIntoMedian(treeNode, i - 1, LEFT_CHILD_DEFAULT_INDEX, childNode, medianKeyIndex);
        } else if (rightSibling != null) {
            int medianKeyIndex = getMedianIndexAndMergeChildNodes(childNode, rightSibling);
            moveKeyAndInsertIntoMedian(treeNode, i, RIGHT_CHILD_DEFAULT_INDEX, childNode, medianKeyIndex);
        }
    }

    private void movePredecessorOrSuccessor(BTreeNode treeNode, int insertIndex, boolean isSuccessor, BTreeNode child) {
        BTreeNode nodeForMove = child;
        BTreeNode nodeForDelete = nodeForMove;
        while (!nodeForMove.isLeafNode()) {
            nodeForDelete = nodeForMove;
            int childIndex = isSuccessor ? 0 : (treeNode.getNumberOfKeys() - 1);
            nodeForMove = nodeForMove.getChildAtIndex(childIndex);
        }
        int entryIndex = isSuccessor ? 0 : (nodeForMove.getNumberOfKeys() - 1);
        treeNode.setEntryAtIndex(insertIndex, nodeForMove, entryIndex);
        removeNode(nodeForDelete, treeNode.getKeyAtIndex(insertIndex));
    }

    private int getMedianIndexAndMergeChildNodes(BTreeNode mergeToNode, BTreeNode mergeFromNode) {
        int medianKeyIndex;
        if (mergeFromNode.getKeyAtIndex(0) < mergeToNode.getKeyAtIndex(mergeToNode.getNumberOfKeys() - 1)) {
            writeChildFromEnd(mergeToNode, mergeFromNode);
            medianKeyIndex = getMedianIndexAndClearMedian(mergeToNode, mergeFromNode);
            writeChildFromStart(mergeToNode, mergeFromNode);
        } else {
            medianKeyIndex = getMedianIndexAndClearMedian(mergeToNode, mergeToNode);
            writeChildFromStartByOne(mergeToNode, mergeFromNode, medianKeyIndex);
        }
        mergeToNode.addKeysLength(mergeFromNode.getNumberOfKeys());
        return medianKeyIndex;
    }

    private void writeChildFromStartByOne(BTreeNode mergeToNode, BTreeNode mergeFromNode, int medianKeyIndex) {
        int medianShift = medianKeyIndex + 1;
        int i;
        for (i = 0; i < mergeFromNode.getNumberOfKeys(); i++) {
            mergeToNode.setEntryAtIndex(medianShift + i, mergeFromNode, i);
            if (!mergeFromNode.isLeafNode()) {
                mergeToNode.setChildAtIndex(medianShift + i, mergeFromNode.getChildAtIndex(i));
            }
        }
        if (!mergeFromNode.isLeafNode()) {
            mergeToNode.setChildAtIndex(medianShift + i, mergeFromNode.getChildAtIndex(i));
        }
    }

    private int getMedianIndexAndClearMedian(BTreeNode mergeToNode, BTreeNode mergeFromNode) {
        int medianKeyIndex;
        medianKeyIndex = mergeFromNode.getNumberOfKeys();
        mergeToNode.clearEntryByIndex(medianKeyIndex);
        return medianKeyIndex;
    }

    private void writeChildFromStart(BTreeNode mergeToNode, BTreeNode mergeFromNode) {
        int i;
        for (i = 0; i < mergeFromNode.getNumberOfKeys(); i++) {
            mergeToNode.setEntryAtIndex(i, mergeFromNode);
            if (!mergeFromNode.isLeafNode()) {
                mergeToNode.setChildAtIndex(i, mergeFromNode.getChildAtIndex(i));
            }
        }
        if (!mergeFromNode.isLeafNode()) {
            mergeToNode.setChildAtIndex(i, mergeFromNode.getChildAtIndex(i));
        }
    }

    private void writeChildFromEnd(BTreeNode mergeToNode, BTreeNode mergeFromNode) {
        if (!mergeToNode.isLeafNode()) {
            int index = mergeFromNode.getNumberOfKeys() + mergeToNode.getNumberOfKeys() + 1;
            mergeToNode.setChildAtIndex(index, mergeToNode.getChildAtIndex(mergeToNode.getNumberOfKeys()));
        }
        for (int i = mergeToNode.getNumberOfKeys(); i > 0; i--) {
            mergeToNode.setEntryAtIndex(mergeFromNode.getNumberOfKeys() + i, mergeToNode, i - 1);
            if (!mergeToNode.isLeafNode()) {
                mergeToNode.setChildAtIndex(mergeFromNode.getNumberOfKeys() + i, mergeToNode.getChildAtIndex(i - 1));
            }
        }
    }

    private void moveKeyAndInsertIntoMedian(BTreeNode fromNode, int fromKeyIndex, int childIndex, BTreeNode toNode, int medianIndex) {
        toNode.setEntryAtIndex(medianIndex, fromNode, fromKeyIndex);
        toNode.increaseNumberOfKeys();
        fromNode.removeChildIndex(fromKeyIndex, childIndex);
        if (fromNode == rootNode && fromNode.isEmpty()) {
            rootNode = toNode;
        }
    }

    public TreeNodeEntry getEntryByKey(int key) {
        return searchEntry(rootNode, key);
    }

    public TreeNodeEntry searchEntry(BTreeNode treeNode, int key) {
        int entryIndex = treeNode.getKeyWithBinarySearch(key);
        if (entryIndex != -1) {
            return treeNode.getEntryAtIndex(entryIndex);
        }
        if (!treeNode.isLeafNode()) {
            int childIndex = treeNode.getNextChildIndex(key);
            return searchEntry(treeNode.getChildAtIndex(childIndex), key);
        } else {
            return null;
        }
    }

    private boolean isUpdatePosition(BTreeNode treeNode, TreeNodeEntry entry) {
        boolean isInserted = false;
        while (treeNode != null) {
            int i = treeNode.getPositionForInsert(entry);
            if (i < treeNode.getNumberOfKeys() && entry.getKey() == treeNode.getKeyAtIndex(i)) {
                treeNode.setValueAtIndex(i, entry.getValue());
                isInserted = true;
                break;
            }
            if (!treeNode.isLeafNode()) {
                treeNode = treeNode.getChildAtIndex(i);
            } else {
                break;
            }
        }
        return isInserted;
    }

    public List<TreeNodeEntry> getEntries() {
        return getEntries(this.rootNode);
    }

    private List<TreeNodeEntry> getEntries(BTreeNode treeNode) {
        List<TreeNodeEntry> entries = new ArrayList<>();
        if (treeNode == null) {
            throw new RuntimeException("Tree node should not be null");
        }
        if (!treeNode.isLeafNode()) {
            int index;
            for (index = 0; index < treeNode.getNumberOfKeys(); index++) {
                entries.addAll(getEntries(treeNode.getChildAtIndex(index)));
                entries.add(treeNode.getEntryAtIndex(index));
            }
            entries.addAll(getEntries(treeNode.getChildAtIndex(index)));
        } else {
            for (int i = 0; i < treeNode.getNumberOfKeys(); i++) {
                entries.add(treeNode.getEntryAtIndex(i));
            }
        }
        return entries;
    }
}
