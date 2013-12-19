/**
 * 
 */
package com.islamsamak.aqarmap.model;

import android.os.Parcel;
import android.text.TextUtils;

import com.oneteam.framework.android.io.IJsonNode;
import com.oneteam.framework.android.model.MapModel;

/**
 * @author islam
 * 
 */
public class City implements IJsonNode, MapModel {

	public static final String AQARMAP_API = "http://aqarmap.com/api/1.0/";

	public static final String API_CITIES_REQUEST = "cities.get.json";

	public static final String GET_CITIES_API = AQARMAP_API
			+ API_CITIES_REQUEST;

	public static final String APP_ID_PARAM = "app_id";

	public static final String SIGNATURE_PARAM = "signature";

	public static final String EXPIRES_PARAM = "expires";

	public static final String APP_ID = "52add2b4-841c-44a4-bd9e-0ad50aea22c7";

	public static final String SECRECT_KEY = "aa130d5730b1becd613311ae663bcae9ce094369";

	public static final String TITLE_TAG = "title";

	public static final String SLUG_TAG = "slug";

	public static final String LATITUDE_TAG = "lat";

	public static final String LONGITUDE_TAG = "lng";

	public static final String GEONAME_ID_TAG = "geoname_id";

	public static final String ZOOM_TAG = "zoom";

	public static final String CITY_TAG = "City";

	private String mTitle;

	private String mSlug;

	private double mLatitude;

	private double mLongitude;

	private int mGeonameId;

	private int mZoom;

	public void setTitle(String title) {
		this.mTitle = title;
	}

	@Override
	public String getTitle() {
		return mTitle;
	}

	public String getSlug() {
		return mSlug;
	}

	public void setSlug(String slug) {
		this.mSlug = slug;
	}

	public void setLongitude(double longitude) {
		this.mLongitude = longitude;
	}

	@Override
	public Double getLongitude() {
		return mLongitude;
	}

	public void setLatitude(double latitude) {
		this.mLatitude = latitude;
	}

	@Override
	public Double getLatitude() {
		return mLatitude;
	}

	public int getGeonameId() {
		return mGeonameId;
	}

	public void setGeonameId(int geonameId) {
		this.mGeonameId = geonameId;
	}

	public int getZoom() {
		return mZoom;
	}

	public void setZoom(int zoom) {
		this.mZoom = zoom;
	}

	@Override
	public String getSnippet() {

		StringBuilder snippet = new StringBuilder(200);

		String image = getThumbnail();

		if (TextUtils.isEmpty(image)) {

			snippet.append("");

		} else {

			snippet.append(image);
		}

		snippet.append(";SEP;");

		snippet.append(mTitle);

		snippet.append(";SEP;");

		snippet.trimToSize();

		return snippet.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	@Override
	public String getThumbnail() {
		return null;
	}

}
