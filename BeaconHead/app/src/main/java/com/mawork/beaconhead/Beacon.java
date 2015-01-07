package com.mawork.beaconhead;

public class Beacon {
    public int id;
    public String uuid;
    public int major;
    public int minor;
    public String url;

    public Beacon(int id, String uuid, int major, int minor, String url ){
        this.id = id;
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.url = url;
    }

    public Beacon(String uuid, int major, int minor, String url ){
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.url = url;
    }

    public Beacon(){

    }
}