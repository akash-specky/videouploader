package com.example.videouploader.model;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class VideoProperties {

    @Id
    Long size;
    String codec;
    String format;
    Float fps;

    public VideoProperties() {
    }

    public VideoProperties(Long size, String codec, String format, Float fps) {
        this.size = size;
        this.codec = codec;
        this.format = format;
        this.fps = fps;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Float getFps() {
        return fps;
    }

    public void setFps(Float fps) {
        this.fps = fps;
    }
}
