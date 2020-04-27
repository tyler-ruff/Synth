package com.blz.synth.Utils;

import com.blz.synth.SynthGui;

public enum WaveTable {
 Sin, Square, Saw, Triangle;
    public static final int SIZE = 8192; // Ajust to change precision of WaveTable
    // WARNING: using a larger size will produce a "smoother" wave, but will require more memory.
    private final float[] samples = new float[WaveTable.SIZE];
    static{
        //fundamental frequency (the frequency that each wave oscillates at to produce a complete wave.
        final double FUND_FREQ = 1d / (WaveTable.SIZE / (double)SynthGui.AudioInfo.SAMPLE_RATE);
        for(int i = 0; i < WaveTable.SIZE; i++){
            double t = (i / ((double)SynthGui.AudioInfo.SAMPLE_RATE));
            double tDivP = t / (1d / FUND_FREQ);
            Sin.samples[i] = (float)(Math.sin(Utils.Math.frequencyToAngularFreq(FUND_FREQ) * t));
            Square.samples[i] = Math.signum(Sin.samples[i]);
            Saw.samples[i] = (float)(2d * (tDivP - Math.floor((0.5) + tDivP)));
            Triangle.samples[i] = (float)(2d * Math.abs(Saw.samples[i] - 1d));
        }
    }
    public float[] getSamples(){
        return samples;
    }
}
