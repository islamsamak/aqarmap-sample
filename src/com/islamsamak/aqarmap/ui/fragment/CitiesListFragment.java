/**
 * 
 */
package com.islamsamak.aqarmap.ui.fragment;

import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.islamsamak.aqarmap.R;
import com.islamsamak.aqarmap.core.CityManager;
import com.islamsamak.aqarmap.io.CityStatus;
import com.islamsamak.aqarmap.model.City;
import com.islamsamak.aqarmap.ui.viewmodel.ProductAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.oneteam.framework.android.task.DefaultAsyncTask;
import com.oneteam.framework.android.task.TaskCallback;

/**
 * @author islam
 * 
 */
public class CitiesListFragment extends SherlockFragment {

	private TaskCallback<CityManager> mCityCallback;

	private DefaultAsyncTask<CityManager, Void, CityManager> mCityTask;

	private ProgressDialog mProgressDialog;

	private ImageLoader mImageLoader = null;

	protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";

	protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";

	protected AbsListView mListView;

	private ProductAdapter<City> mAdapter;

	protected boolean pauseOnScroll = false;

	protected boolean pauseOnFling = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			pauseOnScroll = savedInstanceState.getBoolean(
					STATE_PAUSE_ON_SCROLL, false);

			pauseOnFling = savedInstanceState.getBoolean(STATE_PAUSE_ON_FLING,
					true);
		}

		initImageLoader();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_cities_list, container,
				false);

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);

		mListView = (ListView) view.findViewById(android.R.id.list);

		initVars();

		initImageLoader();

		startLoadOffers();
	}

	/**
	 * 
	 */
	protected void initImageLoader() {

		mImageLoader = ImageLoader.getInstance();

		ImageLoaderConfiguration configs = new ImageLoaderConfiguration.Builder(
				getActivity()).memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(5 * 1024 * 1024).build();

		mImageLoader.init(configs);
	}

	private void initVars() {

		mCityCallback = new TaskCallback<CityManager>() {

			@Override
			public void onRequestSuccess(CityManager cityManger) {

				if (cityManger == null) {

					if (mProgressDialog != null) {
						mProgressDialog.dismiss();
					}

					return;
				}

				final CityStatus cityStatus = cityManger.getActionStatus();

				if (cityStatus == null) {
					return;
				}

				if (!cityStatus.getStatus()) {

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {

							Toast.makeText(getActivity(),
									cityStatus.getMessage(), Toast.LENGTH_SHORT)
									.show();
						}
					});

					return;
				}

				List<City> itemsList = cityStatus.getResults();

				displayItems(itemsList);

				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
			}

			@Override
			public void onRequestFailure(String error) {

				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
			}
		};

		mCityTask = new DefaultAsyncTask<CityManager, Void, CityManager>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

				showProgressDialog();
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();

				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
			}

			@Override
			protected CityManager doInBackground(CityManager... params) {

				if (params == null || params.length == 0) {

					if (mCallback != null) {
						mCallback
								.onRequestFailure("Failed to load offers items");
					}

					return null;
				}

				CityManager cityManager = params[0];

				cityManager.requestCitiesInfo();

				return cityManager;
			}

			@Override
			protected void onPostExecute(CityManager result) {
				super.onPostExecute(result);

				if (mProgressDialog != null) {

					mProgressDialog.dismiss();
				}
			}
		};
	}

	protected void displayItems(final List<City> itemsList) {

		TextView emptyView = (TextView) getView().findViewById(
				android.R.id.empty);

		if (itemsList == null || itemsList.size() == 0) {

			emptyView.setVisibility(View.VISIBLE);

			mListView.setVisibility(View.GONE);

			return;
		}

		emptyView.setVisibility(View.GONE);

		mListView.setVisibility(View.VISIBLE);

		mAdapter = new ProductAdapter<City>(getActivity(), itemsList,
				mImageLoader);

		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Do nothing
			}

		});

		hideProgress();
	}

	private void startLoadOffers() {

		CityManager cityManager = new CityManager();

		mCityTask.setTaskCallback(mCityCallback);

		mCityTask.execute(cityManager);
	}

	protected void showProgressDialog() {

		if (mProgressDialog != null) {

			mProgressDialog.dismiss();
		}

		mProgressDialog = ProgressDialog.show(getActivity(), "",
				getString(R.string.msg_loading), true, true);

		mProgressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {

				if (mCityTask != null) {

					mCityTask.cancel(true);
				}

				getActivity().finish();
			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);

		outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
	}

	public void showProgress() {

		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {

				View progress = getView().findViewById(R.id.progress);

				progress.setVisibility(View.VISIBLE);
			}
		});
	}

	public void hideProgress() {

		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {

				View progress = getView().findViewById(R.id.progress);

				progress.setVisibility(View.GONE);
			}
		});
	}
}
