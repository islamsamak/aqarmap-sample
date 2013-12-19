package com.islamsamak.aqarmap.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.islamsamak.aqarmap.R;
import com.oneteam.framework.android.ui.impl.activity.DefaultSherlockActivity;

public class AboutActivity extends DefaultSherlockActivity {

	@Override
	protected void onCreateActivity(Bundle savedInstanceState) {

		super.onCreateActivity(savedInstanceState);

		setContentView(R.layout.activity_info);

		findViewById(R.id.lyt_details).setVisibility(View.VISIBLE);
	}

}
