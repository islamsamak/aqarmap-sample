package com.islamsamak.aqarmap.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.islamsamak.aqarmap.R;
import com.oneteam.framework.android.ui.activity.AbstractSplashActivity;

public class SplashActivity extends AbstractSplashActivity {

	@Override
	protected void onCreateActivity(Bundle savedInstanceState) {

		super.onCreateActivity(savedInstanceState);

		setContentView(R.layout.activity_info);
	}

	@Override
	public void onPreExecute() {
		setTargetClass(HomeActivity.class);
	}

	@Override
	public void doInBackground() {

		try {

			Thread.sleep(2000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onUpdateView(Intent intent) {
		return true;
	}

}
