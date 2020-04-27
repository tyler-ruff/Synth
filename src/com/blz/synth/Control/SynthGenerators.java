package com.blz.synth.Control;

import com.blz.synth.Utils.WaveTable;
/*
Please note: each generator WILL have to implement their own volume system.
 */
public interface SynthGenerators {
    public static final int TONE_OFFSET_LIMIT = 2000;
    public double getToneOffset(); //The offset the the pitch of the generated sound
    public double getKeyFreq(); //The raw pitch (pre offset) of the generated sound
    public void applyToneOffset();
    public void setKeyFreq(double keyFreq);
    public double nextSample(); //The main method called to advance the sample buffer and continue to generate sound
}
