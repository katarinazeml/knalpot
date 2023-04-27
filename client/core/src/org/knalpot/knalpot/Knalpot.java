package org.knalpot.knalpot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import org.knalpot.knalpot.scenes.MainMenuScreen;

import com.badlogic.gdx.Game;

public class Knalpot extends Game {

	private Game game;
	private Music music;
	private boolean isMusicOn = true;
	public float volume;

	public Knalpot() {
		game = this;
	}

	public void create() {
		// Load music file
		music = Gdx.audio.newMusic(Gdx.files.internal("buttons/menusong.mp3"));

		// Set loop and volume for music and play it
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();

		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render();
	}

	public Music getMusic() {
		return music;
	}

	public boolean isMusicOn() {
		return isMusicOn;
	}

	public void setMusicOn(boolean isMusicOn) {
		this.isMusicOn = isMusicOn;
		if (isMusicOn) {
			music.play();
		} else {
			music.stop();
		}
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}
}
