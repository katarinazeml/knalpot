package org.knalpot.knalpot;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	private static int WW = 1280;
	private static int WH = 720;
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Knalpot");
		config.setWindowedMode(WW, WH);
		config.setResizable(false);
		new Lwjgl3Application(new Knalpot(), config);
	}
}
