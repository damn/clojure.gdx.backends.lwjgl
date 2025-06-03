
package com.badlogic.gdx.backends.lwjgl3;

import com.badlogic.gdx.Application;

public interface Lwjgl3ApplicationBase extends Application {

	Lwjgl3Input createInput (Lwjgl3Window window);
}
