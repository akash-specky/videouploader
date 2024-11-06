package com.example.videouploader.utility;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class Constant {

    public static final String UPLOAD_DIR = "c://Users/gaurav.singh/Desktop/videoUploaderTask/videouploader/UploadedVideo";
    public static final String THUMBNAIL_DIR =  "c://Users/gaurav.singh/Desktop/videoUploaderTask/videouploader/Thumbnail";
    public static final String OUTPUT_VIDEO_DIR =  "c://Users/gaurav.singh/Desktop/videoUploaderTask/videouploader/VideosResulition";
    public static final String VIDEO_DIR = "c://Users/gaurav.singh/Pictures/videos";
    public static final String CHUNK_DIR = "c://Users/gaurav.singh/Desktop/videoUploaderTask/videouploader/chunks";
    public static final String COMBINED_VIDEO_DIR = "c://Users/gaurav.singh/Desktop/videoUploaderTask/videouploader/combined";

    public static String[] urls = {"/users/signup"};

    public static final long MAX_FILE_SIZE = 500 * 1024; // 500KB

    // Define thumbnail sizes (small, medium, large)
    public static final int SMALL_WIDTH = 100;
    public static final int SMALL_HEIGHT = 100;

    public static final int MEDIUM_WIDTH = 300;
    public static final int MEDIUM_HEIGHT = 300;

    public static final int LARGE_WIDTH = 600;
    public static final int LARGE_HEIGHT = 600;



}
