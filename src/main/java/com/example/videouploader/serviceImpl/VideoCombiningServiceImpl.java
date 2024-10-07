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
    public File combineChunks(List<File> chunks, String videoId) throws Exception {

        File combinedDir = new File(COMBINED_VIDEO_DIR);
        if (!combinedDir.exists()) {
            combinedDir.mkdirs();
        }

        File outputFile = new File(COMBINED_VIDEO_DIR, videoId + "_combined.mp4");

        String ffmpegCommand = getString(chunks, videoId, outputFile);

        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", ffmpegCommand);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        process.waitFor();

        return outputFile;
    }

    private static String getString(List<File> chunks, String videoId, File outputFile) {
        File fileList = new File(COMBINED_VIDEO_DIR, videoId + "_filelist.txt");
        try (PrintWriter writer = new PrintWriter(fileList)) {
            for (File chunk : chunks) {
                writer.println("file '" + chunk.getAbsolutePath() + "'");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        String ffmpegCommand = String.format(
                "ffmpeg -f concat -safe 0 -i %s -c copy %s",
                fileList.getAbsolutePath(),
                outputFile.getAbsolutePath()
        );
        return ffmpegCommand;
    }
}
