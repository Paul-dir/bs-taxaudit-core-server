package com.mor.itas.planning.application.port;

public interface DmsPort {
    String uploadDocument(String folder, String filename, byte[] content);
}
