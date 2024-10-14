package com.patrick;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class VideoConverter {
        protected static void convertToMp4(String inputFile, int crf){
        String outputFile = inputFile.substring(0, inputFile.lastIndexOf('.')) + ".mp4";
        try(FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile, 0)){
                // Start the grabber
                grabber.start();

                // Set recorder parameters
                recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                recorder.setFormat("mp4");

                // Set width and height for the output video
                recorder.setImageWidth(grabber.getImageWidth());
                recorder.setImageHeight(grabber.getImageHeight());

                // Set the frame rate
                recorder.setFrameRate(grabber.getFrameRate());
                // Set the bitrate to 0 so that CRF value is used instead
                recorder.setVideoBitrate(0);
                // Set the CRF value (using bitrate approximation)
                recorder.setOption("crf", String.valueOf(crf)); // Set CRF for quality

                // Setup the Audio to be converted along with the video as well
                // Set the same Sample Rate as the input file.
                recorder.setSampleRate(grabber.getSampleRate());
                // Select codec for the conversion
                recorder.setAudioCodec(avcodec.AV_CODEC_ID_FLAC);
                // Set the propper amount of audio channels based on the original
                recorder.setAudioChannels(grabber.getAudioChannels());
                
                // Start recording
                recorder.start();


                for(int i = 0; i < grabber.getLengthInFrames(); i++){
                    Frame frame = grabber.grab();
                    if (frame == null){
                        break;
                    }
                    recorder.record(frame);
                }
                recorder.stop();
                grabber.stop();

                System.out.println("File " + inputFile + " converted successfully to " + outputFile);
            } catch (Exception e){
                System.err.println("Error coverting file: " + e.getMessage());
                //e.printStackTrace();
            }
    }

}
