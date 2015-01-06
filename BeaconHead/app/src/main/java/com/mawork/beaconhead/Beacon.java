package com.mawork.beaconhead;

public class Beacon {
    public int id;
    public byte[] uuid;
    public int major;
    public int minor;
    public String url;

    public Beacon(int id, byte[] uuid, int major, int minor, String url ){
        this.id = id;
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.url = url;
    }

    public Beacon(byte[] uuid, int major, int minor, String url ){
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.url = url;
    }

    public Beacon(){

    }
}