package com.lightBar;


import jssc.SerialPort;
import jssc.SerialPortException;
import net.runelite.api.Actor;
import net.runelite.api.SpriteID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import org.pushingpixels.substance.internal.utils.WidgetUtilities;

public class infoScSe{

public String comboString;
public Actor myActor;
public Widget widget;
private String isPoison;
private String isVenomed;
public String prevString;
    public void data(){

        comboString = myActor.getHealthRatio() + isPoison + isVenomed;
           if(comboString != prevString){send();}
    }

    public void checkPoison(){

        if(widget.createChild(160,6).getSpriteId()==SpriteID.MINIMAP_ORB_HITPOINTS_POISON){
            isPoison = "+";
        } else if(widget.createChild(160,6).getSpriteId()==SpriteID.MINIMAP_ORB_HITPOINTS_VENOM){
            isVenomed = "+";
        } else {
            isPoison = "-";
            isVenomed = "+";
        }

    }

    public void send(){
        prevString = comboString;
        SerialPort serialPort = new SerialPort("COM1");
        try {
            serialPort.openPort();

            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);


            serialPort.writeString(comboString);

            data();
        }
        catch (SerialPortException ex) {
            System.out.println("There are an error on writing string to port т: " + ex);
            data();
        }

    }

}
