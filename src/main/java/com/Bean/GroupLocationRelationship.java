package com.Bean;

/**
 * @author JXS
 * 组之间的关系存储
 */
public class GroupLocationRelationship {
    private int block1Id;
    private int block2Id;
    private String locationRelationship;

    public GroupLocationRelationship(int block1Id, int block2Id, String locationRelationship) {
        this.block1Id = block1Id;
        this.block2Id = block2Id;
        this.locationRelationship = locationRelationship;
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

    public String getLocationRelationship() {
        return locationRelationship;
    }

    public void setLocationRelationship(String locationRelationship) {
        this.locationRelationship = locationRelationship;
    }

    @Override
    public String toString() {
        return "GroupLocationRelationship{" +
                "block1Id=" + block1Id +
                ", block2Id=" + block2Id +
                ", locationRelationship='" + locationRelationship + '\'' +
                '}';
    }
}
