/**
 * 
 */
package com.islamsamak.aqarmap.io;

import java.util.List;

import com.islamsamak.aqarmap.model.City;
import com.oneteam.framework.android.connection.status.ActionStatus;

/**
 * @author islam
 * 
 */
public class CityStatus extends ActionStatus<List<City>, String> {

	public static final String RESPONSE_TAG = "response";

	public static final String STATUS_TAG = "status";

	public static final String RESULTS_TAG = "Cities";

	public static final String CODE_TAG = "code";

	public static final String NAME_TAG = "name";

	public static final String MESSAGE_TAG = "message";

	public CityStatus() {
	}

}
