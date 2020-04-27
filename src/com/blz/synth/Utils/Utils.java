package com.blz.synth.Utils;

import com.blz.synth.Control.SynthControlContainer;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

public class Utils {
    public static void handleProcedure(Procedure procedure, boolean printStackTrace)
    {
        try{
            procedure.invoke();
        } catch (Exception e) {
            if(printStackTrace){
                e.printStackTrace();
            }
        }
    }
    public static class ParameterHandling{
        public static final Robot parameterRobot;
            static{
                try{
                    parameterRobot = new Robot();
                } catch(Exception e){
                    throw new ExceptionInInitializerError("Cannot construct a robot instance");
                }
            }
            public static void addParameterMouseListeners(Component addTo, SynthControlContainer scc, int minVal, int maxVal, int valStep, RefWrapper<Integer> Parameter, Procedure onChange){
                addTo.addMouseListener(new MouseAdapter(){
                @Override
                public void mousePressed (MouseEvent mouseEvent){
                    final Cursor BLANK_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank_mouse");
                    addTo.setCursor(BLANK_CURSOR);
                    scc.setMouseClickLocation(mouseEvent.getLocationOnScreen());
                }

                @Override
                public void mouseReleased (MouseEvent mouseEvent){
                    addTo.setCursor(Cursor.getDefaultCursor());
                }
                });
                addTo.addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if(scc.getMouseClickLocation().y != e.getYOnScreen()){
                            boolean mouseMovingUp = ((scc.getMouseClickLocation().y - e.getYOnScreen()) > 0);
                            if(mouseMovingUp && (Parameter.val < maxVal)){
                                Parameter.val += valStep;
                            } else if(!mouseMovingUp && (Parameter.val > (minVal))){
                                Parameter.val -= valStep;
                            }
                        }
                        if(onChange != null){
                            handleProcedure(onChange, true);
                        }
                        Utils.ParameterHandling.parameterRobot.mouseMove(scc.getMouseClickLocation().x, scc.getMouseClickLocation().y);
                    }
                });
            }
    }
    public static class WindowDesign{
        public static final Border LINE_BORDER = BorderFactory.createLineBorder(Color.BLACK);
    }
    public static class Math{
        public static double frequencyToAngularFreq(double freq){
            return 2d * (double)java.lang.Math.PI * freq;
        }
        public static double getKeyFrequency(int keyNum){
            return java.lang.Math.pow(root(2, 12), keyNum - 49) * 440;
        }
        public static double root(double num, double root){
            return java.lang.Math.pow(java.lang.Math.E, (java.lang.Math.log(num) / root));
        }
    }
}
