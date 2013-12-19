package com.islamsamak.aqarmap.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.MenuItem;
import com.islamsamak.aqarmap.R;
import com.islamsamak.aqarmap.ui.fragment.CitiesListFragment;
import com.islamsamak.aqarmap.ui.fragment.CitiesMapFragment;
import com.oneteam.framework.android.ui.impl.activity.DefaultSherlockActivity;
import com.oneteam.framework.android.ui.impl.viewmodel.SectionPagerAdapter;

public class HomeActivity extends DefaultSherlockActivity implements
		ActionBar.TabListener {

	private final static String EXTRA_SELECTED_TAB = "extra_selected_tab";

	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);

		setupNetworkStateReceiver();

		final ActionBar actionBar = getSupportActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		initTabs(actionBar);

		if (savedInstanceState != null) {

			actionBar.setSelectedNavigationItem(savedInstanceState
					.getInt(EXTRA_SELECTED_TAB));
		}
	}

	private void initTabs(final ActionBar actionBar) {

		List<Class<?>> classes = new ArrayList<Class<?>>(3);

		List<String> titles = new ArrayList<String>(3);

		classes.add(CitiesListFragment.class);

		titles.add(getString(R.string.tab_title_list));

		classes.add(CitiesMapFragment.class);

		titles.add(getString(R.string.tab_title_map));

		SectionPagerAdapter pagerAdapter = new SectionPagerAdapter(this,
				getSupportFragmentManager(), classes);

		pagerAdapter.setFragmentTitles(titles);

		mViewPager = (ViewPager) findViewById(R.id.pager);

		mViewPager.setAdapter(pagerAdapter);

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < pagerAdapter.getCount(); i++) {

			actionBar
					.addTab(actionBar.newTab()
							.setText(pagerAdapter.getPageTitle(i))
							.setTabListener(this));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		final ActionBar actionBar = getSupportActionBar();

		outState.putInt(EXTRA_SELECTED_TAB,
				actionBar.getSelectedNavigationIndex());

		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

		getSupportMenuInflater().inflate(R.menu.home, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		switch (id) {

		case R.id.action_about:

			onClickAbout();

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void onClickAbout() {

		Intent intent = new Intent(getApplicationContext(), AboutActivity.class);

		startActivity(intent);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {

		if (mViewPager != null) {
			mViewPager.setCurrentItem(tab.getPosition());
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// Do nothing
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// Do nothing
	}

}
