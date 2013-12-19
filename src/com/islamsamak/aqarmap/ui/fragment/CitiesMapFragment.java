/**
 * 
 */
package com.islamsamak.aqarmap.ui.fragment;

import java.util.List;
import java.util.Random;

import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.islamsamak.aqarmap.R;
import com.islamsamak.aqarmap.core.CityManager;
import com.islamsamak.aqarmap.io.CityStatus;
import com.islamsamak.aqarmap.model.City;
import com.oneteam.framework.android.task.DefaultAsyncTask;
import com.oneteam.framework.android.task.TaskCallback;
import com.oneteam.framework.android.ui.impl.fragment.AbstractMapFragment;
import com.oneteam.framework.android.utils.Logger;

/**
 * @author islam
 * 
 */
public class CitiesMapFragment extends AbstractMapFragment<City> {

	private TaskCallback<CityManager> mCityCallback;

	private DefaultAsyncTask<CityManager, Void, CityManager> mCitiesTask;

	public CitiesMapFragment() {

		setFragmentLayoutId(R.layout.fragment_cities_map);

		setMapFragmentId(R.id.map);
	}

	@Override
	public void onStart() {
		super.onStart();

		initVars();

		loadCitiesLocations();
	}

	private void initVars() {

		mCityCallback = new TaskCallback<CityManager>() {

			@Override
			public void onRequestSuccess(CityManager cityManger) {

				if (cityManger == null) {

					hideProgress();

					return;
				}

				CityStatus cityStatus = cityManger.getActionStatus();

				if (cityStatus == null || !cityStatus.getStatus()) {
					return;
				}

				List<City> citiesList = cityStatus.getResults();

				setItemsList(citiesList);

				LatLng loc = getRandomLocation(citiesList);

				GoogleMap map = getGoogleMap();

				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(loc).zoom(7).tilt(30).build();

				map.animateCamera(
						CameraUpdateFactory.newCameraPosition(cameraPosition),
						2000, null);
				clearMap();

				drawMarkers();

				hideProgress();
			}

			@Override
			public void onRequestFailure(String error) {
				hideProgress();
			}
		};

		mCitiesTask = new DefaultAsyncTask<CityManager, Void, CityManager>() {

			@Override
			protected CityManager doInBackground(CityManager... params) {

				showProgress();

				if (params == null || params.length == 0) {
					if (mCallback != null) {
						mCallback
								.onRequestFailure("No params are sent to get cities");
					}

					return null;
				}

				CityManager citiesManager = params[0];

				boolean status = citiesManager.requestCitiesInfo();

				Logger.d("Requesting cities info status = " + status);

				if (!status) {

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {

							Toast.makeText(getActivity(),
									R.string.msg_init_cities_error,
									Toast.LENGTH_SHORT).show();
						}
					});

					return null;
				}

				return citiesManager;
			}
		};
	}

	protected LatLng getRandomLocation(List<City> citiesList) {

		if (citiesList == null || citiesList.isEmpty()) {
			return new LatLng(30.048889D, 31.197706D);
		}

		Random random = new Random();

		int index = random.nextInt(citiesList.size());

		City city = citiesList.get(index);

		LatLng loc = new LatLng(city.getLatitude(), city.getLongitude());

		return loc;
	}

	private void loadCitiesLocations() {

		if (mCitiesTask == null || mCitiesTask == null) {
			return;
		}

		CityManager cityManager = new CityManager();

		mCitiesTask.setTaskCallback(mCityCallback);

		mCitiesTask.execute(cityManager);
	}

	@Override
	public void onDetach() {

		if (mCitiesTask != null) {
			mCitiesTask.cancel(true);
		}

		super.onDetach();
	}

	@Override
	public void onDestroyView() {

		if (mCitiesTask != null) {
			mCitiesTask.cancel(true);
		}

		super.onDestroyView();
	}

	@Override
	public void onInfoWindowClick(String[] items) {
	}
}
