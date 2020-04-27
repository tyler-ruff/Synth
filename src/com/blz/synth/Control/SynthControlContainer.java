package com.blz.synth.Control;
/*
    The basic container extended by all synth control containers.
 */
import com.blz.synth.SynthGui;
import javax.swing.*;
import java.awt.*;

public class SynthControlContainer extends JPanel {
    private Point mouseClickLocation;
    protected boolean on;
    private SynthGui gui;

    public SynthControlContainer(SynthGui gui){
        this.gui = gui;
    }
    public boolean isOn(){ return on; }
    public Point getMouseClickLocation(){ return this.mouseClickLocation; }
    public void setMouseClickLocation(Point newLoc){ this.mouseClickLocation = newLoc; }
    public void setOn(boolean on){ this.on = on; }
    @Override
    public Component add(Component c){
        c.addKeyListener(gui.getKeyAdapter());
        return super.add(c);
    }
    @Override
    public Component add(Component c, int index){
        c.addKeyListener(gui.getKeyAdapter());
        return super.add(c, index);
    }
    @Override
    public Component add(String name, Component c){
        c.addKeyListener(gui.getKeyAdapter());
        return super.add(name, c);
    }
    @Override
    public void add(Component c, Object constraints){
        c.addKeyListener(gui.getKeyAdapter());
        super.add(c, constraints);
    }
    @Override
    public void add(Component c, Object constraints, int index){
        c.addKeyListener(gui.getKeyAdapter());
        super.add(c, constraints, index);
    }

}
