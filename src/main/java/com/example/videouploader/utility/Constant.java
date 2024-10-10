package com.example.videouploader.utility;

import java.io.File;
import java.nio.file.Paths;

public class Constant {
//    public static final String UPLOAD_DIR = "C:/Users/akash/OneDrive/Desktop/Uploaded Video";
//    public static final String CHUNK_DIR = "C:/Users/akash/OneDrive/Desktop/Uploaded Video/chunks";
//    public static final String COMBINED_VIDEO_DIR = "C:/Users/akash/OneDrive/Desktop/Uploaded Video/combined";
public static final String BASE_DIR = System.getProperty("java.io.tmpdir");

    public static final String UPLOAD_DIR = Paths.get(BASE_DIR, "UploadedVideo").toString();
    public static final String CHUNK_DIR = Paths.get(UPLOAD_DIR, "chunks").toString();
    public static final String COMBINED_VIDEO_DIR = Paths.get(UPLOAD_DIR, "combined").toString();



}
