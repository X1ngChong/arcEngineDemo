package com.Bean;

/**
 * @author JXS
 * 地区下面所包含地物的id与类型
 */
public class GroupMap {
    private Integer id;
    private String type;

    public GroupMap(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "GroupMap{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
