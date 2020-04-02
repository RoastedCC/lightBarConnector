package com.lightBar;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import com.lightBar.jssc.*;

import java.io.IOException;

@Slf4j
@PluginDescriptor(
	name = "Light Bar Plugin"
)
public class LightBarPlugin extends Plugin
{

	@Inject LightBarConfig config;
	public SerialPort serialPort = new SerialPort(config.COMP());
	public String comboString;
	public Actor myActor;
	public Widget widget;
	private String isPoison;
	private String isVenomed;
	public String prevString;
	@Inject
	private Client client;
	@Inject
	@Override
	protected void startUp() throws Exception
	{String[] portNames = SerialPortList.getPortNames();

		if (portNames.length == 0) {
			System.out.println("There are no serial-ports :( You can use an emulator, such ad VSPE, to create a virtual serial port.");
			System.out.println("Press Enter to exit...");
			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		for (int i = 0; i < portNames.length; i++){
			System.out.println(portNames[i]);}


		log.info("LightBarController started!");

        try {
            serialPort.openPort();

            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);

            serialPort.writeString("connected to osrs");
        }
        catch (SerialPortException ex) {
            System.out.println("There are an error on writing string to port т: " + ex);
        }


	}


	@Override
	protected void shutDown() throws Exception
	{
		log.info("Lightbar	 stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
		}
	}





	@Provides
	LightBarConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(LightBarConfig.class);
	}


	public void data(){
		comboString = myActor.getHealthRatio() + isPoison + isVenomed;
		if(comboString != prevString){send();}
	}

	public void checkPoison() {
		if (widget.createChild(160, 6).getSpriteId() == SpriteID.MINIMAP_ORB_HITPOINTS_POISON) {
			isPoison = "+";
		} else if (widget.createChild(160, 6).getSpriteId() == SpriteID.MINIMAP_ORB_HITPOINTS_VENOM) {
			isVenomed = "+";
		} else {
			isPoison = "-";
			isVenomed = "+";
		}
	}

	public void send(){
		prevString = comboString;
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


