package com.islamsamak.aqarmap;

import com.oneteam.framework.android.app.AbstractManager;

public class AppManager extends AbstractManager {

	private static AppManager sInstance;

	public static AppManager getInstance() {

		if (sInstance == null) {

			sInstance = new AppManager();
		}

		return sInstance;
	}

}
