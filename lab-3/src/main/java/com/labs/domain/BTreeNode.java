package com.labs.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BTreeNode {
    private int treeLevel;
    private int numberOfKeys = 0;
    private int[] key;
    private Object[] values;
    private BTreeNode[] childNodes;
    private boolean isLeafNode;

    public BTreeNode(int treeLevel, boolean isLeafNode) {
        this.treeLevel = treeLevel;
        this.key = new int[2 * this.treeLevel - 1];
        this.values = new Object[2 * this.treeLevel - 1];
        this.childNodes = new BTreeNode[2 * this.treeLevel];
        this.isLeafNode = isLeafNode;
    }

    public int getKeyWithBinarySearch(int key) {
        int firstPos = 0;
        int lastPos = numberOfKeys - 1;
        int resultPos = -1;
        while (firstPos <= lastPos) {
            int middlePos = firstPos + ((lastPos - firstPos) / 2);
            if (this.key[middlePos] < key) {
                firstPos = middlePos + 1;
            } else if (this.key[middlePos] > key) {
                lastPos = middlePos - 1;
            } else {
                resultPos = middlePos;
                break;
            }
        }
        return resultPos;
    }

    public boolean hasMoreThanMax() {
        return getNumberOfKeys() >= treeLevel;
    }

    public boolean hasLessThanMax() {
        return getNumberOfKeys() == treeLevel - 1;
    }

    public void removeChildIndex(int index, int childIndex) {
        if (index >= 0) {
            int i;
            for (i = index; i < numberOfKeys - 1; i++) {
                setEntryAtIndex(i, this, i + 1);
                if (!isLeafNode && i >= index + childIndex) {
                    childNodes[i] = childNodes[i + 1];
                }
            }
            clearEntryByIndex(i);
            if (!isLeafNode) {
                if (i >= index + childIndex) {
                    childNodes[i] = childNodes[i + 1];
                }
                childNodes[i + 1] = null;
            }
            numberOfKeys--;
        }
    }

    public void moveNodesFromSibling() {
        if (!isLeafNode) {
            childNodes[numberOfKeys + 1] = childNodes[numberOfKeys];
        }
        for (int i = numberOfKeys - 1; i >= 0; i--) {
            this.key[i + 1] = this.key[i];
            this.values[i + 1] = this.values[i];
            if (!isLeafNode) {
                childNodes[i + 1] = childNodes[i];
            }
        }
    }

    public int getSubtreeNodeIndex(int key) {
        for (int i = 0; i < numberOfKeys; i++) {
            if (key < this.key[i]) {
                return i;
            }
        }
        return numberOfKeys;
    }

    public boolean isKeysFull() {
        return this.numberOfKeys == (2 * treeLevel - 1);
    }

    public boolean isEmpty() {
        return this.numberOfKeys == 0;
    }

    public boolean isChildFull(int childIndex) {
        return this.childNodes[childIndex].numberOfKeys == (2 * treeLevel - 1);
    }

    public int getKeyAtIndex(int index) {
        return this.key[index];
    }

    public TreeNodeEntry getEntryAtIndex(int index) {
        return new TreeNodeEntry(key[index], (String) values[index]);
    }

    public BTreeNode getChildAtIndex(int index) {
        return this.childNodes[index];
    }

    public void setValueAtIndex(int index, String value) {
        this.values[index] = value;
    }

    public void setChildAtIndex(int index, BTreeNode treeNode) {
        this.childNodes[index] = treeNode;
    }

    public void clearKeyValue() {
        for (int i = treeLevel; i < numberOfKeys; i++) {
            clearEntryByIndex(i);
        }
    }

    public void clearChild() {
        for (int i = treeLevel; i <= numberOfKeys; i++) {
            this.childNodes[i] = null;
        }
    }

    public int getNextChildIndex(int key) {
        int i = 0;
        while (i < getNumberOfKeys() && key > getKeyAtIndex(i)) {
            i++;
        }
        return i;
    }

    public void setEntryAtIndex(int index, BTreeNode treeNode) {
        this.key[index] = treeNode.key[index];
        this.values[index] = treeNode.values[index];
    }

    public void setEntryAtIndex(int index, TreeNodeEntry entry) {
        this.key[index] = entry.getKey();
        this.values[index] = entry.getValue();
    }

    public void setEntryAtIndex(int index, BTreeNode treeNode, int nodeIndex) {
        this.key[index] = treeNode.key[nodeIndex];
        this.values[index] = treeNode.values[nodeIndex];
    }

    public void clearEntryByIndex(int index) {
        this.key[index] = 0;
        this.values[index] = null;
    }

    public void increaseNumberOfKeys() {
        this.numberOfKeys++;
    }

    public void addKeysLength(int keysLength) {
        this.numberOfKeys += keysLength;
    }

    public int getPositionForInsert(TreeNodeEntry entry) {
        int pos = 0;
        while (pos < this.getNumberOfKeys() && entry.getKey() > this.getKeyAtIndex(pos)) {
            pos++;
        }
        return pos;
    }

    public BTreeNode getLeftChildSibling(int index) {
        return (index - 1 >= 0) ? getChildAtIndex(index - 1) : null;
    }

    public BTreeNode getRightChildSibling(int index) {
        return (index + 1 <= getNumberOfKeys()) ? getChildAtIndex(index + 1) : null;
    }
}
