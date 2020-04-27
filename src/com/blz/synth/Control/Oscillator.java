package com.blz.synth.Control;
/*
    An Oscillator is used to synthesize sound. The oscillator can be tweaked in a variety of ways to produce distinct sound.
 */
import com.blz.synth.Control.SynthControlContainer;
import com.blz.synth.SynthGui;
import com.blz.synth.Utils.RefWrapper;
import com.blz.synth.Utils.Utils;
import com.blz.synth.Utils.WaveTable;
import javax.swing.*;
import java.awt.event.*;

public class Oscillator extends SynthControlContainer implements SynthGenerators {
    private double keyFreq; private int waveTableStepSize; private int waveTableIndex;
    private static final double TONE_OFFSET_PRECISION = 100d;
    private WaveTable waveTable = WaveTable.Sin;
    //These ref wrappers are used to store the value for parameter boxes
    private RefWrapper<Integer> toneOffset = new RefWrapper<Integer>(0);
    private RefWrapper<Integer> volume = new RefWrapper<Integer>(100);
    //Each oscillator has a wave table which is a tabular representation of the sound wave (basically a sound array)
    public Oscillator(SynthGui gui){
        super(gui); JLabel toneText = new JLabel("Tone: "); JLabel volumeText = new JLabel("Volume: ");
        JComboBox<WaveTable> comboBox = new JComboBox<WaveTable>(WaveTable.values());
        JLabel toneParameter = new JLabel("x0.00"); JLabel volumeParameter = new JLabel("100%");
        comboBox.setSelectedItem(this.waveTable);
        volumeParameter.setBorder(Utils.WindowDesign.LINE_BORDER);
        toneParameter.setBorder(Utils.WindowDesign.LINE_BORDER);
        comboBox.setBounds(10, 10, 75, 25);
        volumeParameter.setBounds(222, 65, 50, 25);
        toneParameter.setBounds(165, 65, 50, 25);
        volumeText.setBounds(225,40, 75, 25);
        toneText.setBounds(172, 40, 75, 25);
        setSize(279, 100); setLayout(null);
        setBorder(Utils.WindowDesign.LINE_BORDER);
        /* Add listeners */
        comboBox.addItemListener(l -> {
            if(l.getStateChange() == ItemEvent.SELECTED) {
                // changes the waveform of the wave table based on the item selected
                waveTable = (WaveTable) l.getItem();
            }
        });
        Utils.ParameterHandling.addParameterMouseListeners(volumeParameter, this, 0, 100, 1, volume, () -> { volumeParameter.setText(volume.val + "%"); });
        Utils.ParameterHandling.addParameterMouseListeners(toneParameter, this, -TONE_OFFSET_LIMIT, TONE_OFFSET_LIMIT, 1, toneOffset, () -> { applyToneOffset(); toneParameter.setText("x" + String.format("%.3f", this.getToneOffset())); });
        /* Register components */
        this.add(comboBox); this.add(volumeText);
        this.add(toneParameter); this.add(toneText);
        this.add(volumeParameter);
    }
    /* Getters */
    public double getToneOffset(){
        return toneOffset.val / Oscillator.TONE_OFFSET_PRECISION;
    }
    public double getKeyFreq(){
        return keyFreq;
    }
    private double getVolumeMultiplier(){ return volume.val / 100; }
    /* Setters / Applyers */
    public void applyToneOffset(){
        waveTableStepSize = (int) ((WaveTable.SIZE * keyFreq * Math.pow(2, getToneOffset())) / SynthGui.AudioInfo.SAMPLE_RATE);
    }
    public void setKeyFreq(double keyFreq){
        this.keyFreq = keyFreq; applyToneOffset();
    }
    public double nextSample(){
        double sample = waveTable.getSamples()[waveTableIndex] * getVolumeMultiplier();
        waveTableIndex = (waveTableIndex + waveTableStepSize) % WaveTable.SIZE;
        return sample;
    }
}