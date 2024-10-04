package com.example.videouploader.serviceImpl;

import com.example.videouploader.service.VideoChunkingService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static com.example.videouploader.utility.Constant.CHUNK_DIR;


@Service
public class VideoChunkingServiceImpl implements VideoChunkingService {

    @Override
    public List<File> chunkVideo(File videoFile, String videoId) throws Exception {
        File chunkDir = new File(CHUNK_DIR, videoId);
        if (!chunkDir.exists()) {
            chunkDir.mkdirs();
        }

        String ffmpegCommand = String.format(
                "ffmpeg -i \"%s\" -c copy -map 0 -segment_time 00:00:05 -f segment \"%s/output%%03d.mp4\"",
                videoFile.getAbsolutePath(),
                chunkDir.getAbsolutePath()
        );


        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", ffmpegCommand);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Log FFmpeg output for debugging
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new Exception("FFmpeg command failed with exit code " + exitCode);
        }


        File[] files = chunkDir.listFiles((dir, name) -> name.endsWith(".mp4"));
        if (files == null || files.length == 0) {
            throw new Exception("No video chunks were created by FFmpeg.");
        }

        return Arrays.asList(files);
    }
}
