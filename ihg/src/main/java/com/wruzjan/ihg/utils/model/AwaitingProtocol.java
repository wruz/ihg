package com.wruzjan.ihg.utils.model;

public class AwaitingProtocol {

    private final int id;
    private final String protocolPdfUrl;

    public AwaitingProtocol(String protocolPdfUrl) {
        this(-1, protocolPdfUrl);
    }

    public AwaitingProtocol(int id, String protocolPdfUrl) {
        this.id = id;
        this.protocolPdfUrl = protocolPdfUrl;
    }

    public int getId() {
        return id;
    }

    public String getProtocolPdfUrl() {
        return protocolPdfUrl;
    }
}
