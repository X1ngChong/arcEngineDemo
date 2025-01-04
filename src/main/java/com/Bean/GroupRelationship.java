package com.Bean;

public class GroupRelationship {
    private int block1Id;
    private int block2Id;
    private String relationship;

    public GroupRelationship(int block1Id, int block2Id, String relationship) {
        this.block1Id = block1Id;
        this.block2Id = block2Id;
        this.relationship = relationship;
    }

    public int getBlock1Id() {
        return block1Id;
    }

    public void setBlock1Id(int block1Id) {
        this.block1Id = block1Id;
    }

    public int getBlock2Id() {
        return block2Id;
    }

    public void setBlock2Id(int block2Id) {
        this.block2Id = block2Id;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Override
    public String toString() {
        return block1Id + " " + relationship + " " + block2Id;
    }
}
