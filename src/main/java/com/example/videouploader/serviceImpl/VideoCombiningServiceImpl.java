package com.example.videouploader.serviceImpl;

import com.example.videouploader.service.VideoCombiningService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import static com.example.videouploader.utility.Constant.COMBINED_VIDEO_DIR;

@Service
public class VideoCombiningServiceImpl implements VideoCombiningService {
    @Override
    public File combineChunks(List<File> chunks, String videoId,String fileName) throws Exception {

        File combinedDir = new File(COMBINED_VIDEO_DIR);
        if (!combinedDir.exists()) {
            combinedDir.mkdirs();
        }

         File outputFile = new File(COMBINED_VIDEO_DIR, fileName + "_combined.mp4");

        String ffmpegCommand = getFFmpegCommand(chunks, videoId, outputFile);

        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", ffmpegCommand);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg command failed with exit code: " + exitCode);
        }
        File fileList = new File(COMBINED_VIDEO_DIR, videoId + "_filelist.txt");
        if (fileList.exists()) {
            fileList.delete();
        }

        return outputFile;
    }

    private static String getFFmpegCommand(List<File> chunks, String videoId, File outputFile) {

        File fileList = new File(COMBINED_VIDEO_DIR, videoId + "_filelist.txt");
        try (PrintWriter writer = new PrintWriter(fileList)) {
            for (File chunk : chunks) {
                writer.println("file '" + chunk.getAbsolutePath() + "'");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error creating file list: " + e.getMessage(), e);
        }

        return String.format(
                "ffmpeg -f concat -safe 0 -i \"%s\" -c copy \"%s\"",
                fileList.getAbsolutePath(),
                outputFile.getAbsolutePath()
        );
    }
}
