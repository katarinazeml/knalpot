package org.knalpot.knalpot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import org.knalpot.knalpot.scenes.GameOverScreen;
import org.knalpot.knalpot.scenes.MainMenuScreen;

import com.badlogic.gdx.Game;

public class Knalpot extends Game {

	private Game game;
	private Music music;
	public float volume;

	public Knalpot() {
		game = this;
	}

	public void create() {
		// play music in MainMenuScreen and SettingsMenuScreen
		music();

		this.setScreen(new MainMenuScreen((Knalpot) game));
		//this.setScreen(new MainMenuScreen());
	}

	public void render() {
		super.render();
	}

	public Music getMusic() {
		return music;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public void music() {
		// Load music file
		music = Gdx.audio.newMusic(Gdx.files.internal("menu-music.mp3"));

		// Set loop and volume for music and play it
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();
	}
}
