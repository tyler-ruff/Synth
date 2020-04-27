package com.blz.synth;

import com.blz.synth.Control.Oscillator;
import com.blz.synth.Utils.Utils;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class SynthGui {
    private static final HashMap<Character,Double> KEY_FREQUENCIES = new HashMap();
    private boolean shouldGenerate; private int oscNum;
    //TODO: Add/remove oscillators
    private ArrayList<Oscillator> osc;
    private final JFrame frame = new JFrame("Synthesizer");
    private final AudioThread audioThread = new AudioThread(() ->
    {
        if(!shouldGenerate){
            return null;
        }
        short[] s = new short[AudioThread.BUFFER_SIZE];
        for(int i = 0; i < AudioThread.BUFFER_SIZE; ++i){
            double d = 0;
            for(Oscillator o : osc){
                d += o.nextSample() / osc.size();
            }
            s[i] = (short)(Short.MAX_VALUE * d);
        }
        return s;
    });
    private final KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            //Failsafe in case an unmapped key is pressed
            if(!KEY_FREQUENCIES.containsKey(e.getKeyChar())){ return; }
            else {
                if (!audioThread.isRunning()) {
                    for (Oscillator o : osc) {
                        o.setKeyFreq(KEY_FREQUENCIES.get(e.getKeyChar()));
                    }
                    shouldGenerate = true;
                    audioThread.triggerPlayback();
                }
            }
        }
        @Override
        public void keyReleased(KeyEvent e){
            shouldGenerate = false;
        }
    };
    static {
        final int STARTING_KEY = 16;
        final int KEY_FREQ_INC = 2;
        final char[] KEYS = "1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik,9ol.0p;/-['".toCharArray();
        for(int i = STARTING_KEY, key = 0; i < KEYS.length * KEY_FREQ_INC + STARTING_KEY; i += KEY_FREQ_INC, ++key){
            KEY_FREQUENCIES.put(KEYS[key], Utils.Math.getKeyFrequency(i));
        }
    }
    SynthGui(int oscNum){
        this.oscNum = oscNum;
        this.osc = new ArrayList<Oscillator>();
        for(int i = 0, y = 0; i < this.oscNum; i++, y += 105){
            //Add Oscillators
            osc.add(new Oscillator(this));
            osc.get(i).setLocation(0, y);
            frame.add(osc.get(i));
        }
        frame.addKeyListener(this.keyAdapter);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                audioThread.close();
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(613, 357);
        frame.setResizable(false); frame.setLocationRelativeTo(null);
        frame.setLayout(null); frame.setVisible(true);

    }

    public KeyAdapter getKeyAdapter() {
        return keyAdapter;
    }

    public static class AudioInfo{
       public static final int SAMPLE_RATE = 44100;
    }
}
