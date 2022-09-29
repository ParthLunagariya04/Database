package com.parth.sqldatabas.model;

public class RoomModelRecyclerView {
    private String name;
    private String moNumber;
    private byte[] bufferByte;

    public RoomModelRecyclerView(String name, String moNumber, byte[] bufferByte) {
        this.name = name;
        this.moNumber = moNumber;
        this.bufferByte = bufferByte;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoNumber() {
        return moNumber;
    }

    public void setMoNumber(String moNumber) {
        this.moNumber = moNumber;
    }

    public byte[] getBufferByte() {
        return bufferByte;
    }

    public void setBufferByte(byte[] bufferByte) {
        this.bufferByte = bufferByte;
    }
}
