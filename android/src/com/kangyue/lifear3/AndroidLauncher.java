package com.kangyue.lifear3;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.kangyue.lifear3.LifeGameAR3;

public class AndroidLauncher extends AndroidApplication {

	private int origWidth;
	private int origHeight;
	private String TAG ="AndroidLauncher";
	int CAMERA_REQUEST_CODE = -1;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		config.r = 8;
		config.g = 8;
		config.a = 8;
		config.b = 8;

		config.useAccelerometer = false;
		config.useCompass = false;

		initialize(new LifeGameAR3(), config);
	}
}
