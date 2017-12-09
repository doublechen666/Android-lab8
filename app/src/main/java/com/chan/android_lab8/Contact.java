package com.chan.android_lab8;

/**
 * Created by 61915 on 17/12/09.
 */

public class Contact {
    private int id;
    private String name;
    private String birth;
    private String gift;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBirth() {
        return birth;
    }

    public String getGift() {
        return gift;
    }
}
