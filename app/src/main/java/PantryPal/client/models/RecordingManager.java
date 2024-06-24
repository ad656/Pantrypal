package PantryPal.client.models;

import java.io.*;
import javax.sound.sampled.*;

public class RecordingManager {
    
    private String filename = "recording.wav";
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Thread myThread;
  
    /**
     * Constructor for RecordingManager.
     */
    public RecordingManager(){
        this.audioFormat = getAudioFormat();
    }
    
    public void setFilePath(String filename){
        this.filename = filename;
    }

    /**
     * Private method to get audio format 
     * @return AudioFormat object
     */
    private AudioFormat getAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 2;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        return new AudioFormat(
            sampleRate,
            sampleSizeInBits,
            channels,
            signed,
            bigEndian);
    }

    /**
     * Begins the recording.
     * Will store wav file at recording.wav
     * @return true if the recording was successfuly started
     */
    public boolean startRecording() {
        targetDataLine = null;
        // the format of the TargetDataLine
        DataLine.Info dataLineInfo = new DataLine.Info(
                TargetDataLine.class,
                audioFormat);
        // the TargetDataLine used to capture audio data from the microphone
        try{
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }

        myThread = new Thread(
            new Runnable(){

                @Override
                public void run(){
                    try {
                        targetDataLine.start();

                        // the AudioInputStream that will be used to write the audio data to a file
                        AudioInputStream audioInputStream = new AudioInputStream(
                            targetDataLine);
                        

                        // the file that will contain the audio data
                        File audioFile = new File(filename);
                        AudioSystem.write(
                            audioInputStream,
                            AudioFileFormat.Type.WAVE,
                            audioFile);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
        }});
        myThread.start();
        return true;
    }

    /**
     * Stops the recording.
     * @return true if successfuly stopped the recording (or if there was no recording).
     */
    public boolean stopRecording() {
        if (myThread == null){
            return true;
        }
        if (myThread.isAlive()){
            targetDataLine.stop();
            targetDataLine.close();
            return true;
        }
        return false;
    }
}
