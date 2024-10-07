package com.example.videouploader.utility;

import java.io.File;

public class Constant {
    public static final String UPLOAD_DIR = "C:/Users/akash/OneDrive/Desktop/Uploaded Video";
    public static final String CHUNK_DIR = "C:/Users/akash/OneDrive/Desktop/Uploaded Video/chunks";
    public static final String COMBINED_VIDEO_DIR = "C:/Users/akash/OneDrive/Desktop/Uploaded Video/combined";


    public static void deleteFileOrDirectory(File file) {

        if (file.isDirectory()) {
            File[] allContents = file.listFiles();
            if (allContents != null) {
                for (File f : allContents) {
                    deleteFileOrDirectory(f);
                }
            }
        }
        file.delete();
    }

}
