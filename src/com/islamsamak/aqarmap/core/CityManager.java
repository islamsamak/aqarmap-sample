/**
 * 
 */
package com.islamsamak.aqarmap.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.islamsamak.aqarmap.io.CityParser;
import com.islamsamak.aqarmap.io.CityStatus;
import com.islamsamak.aqarmap.model.City;
import com.oneteam.framework.android.connection.AbstractManager;
import com.oneteam.framework.android.io.JsonParserInvoker;
import com.oneteam.framework.android.net.HttpPostConnection;
import com.oneteam.framework.android.utils.Logger;

/**
 * @author islam
 * 
 */
public class CityManager extends AbstractManager<CityStatus> {

	@Override
	protected void handleActionStatus(CityStatus actionStatus) {
	}

	@Override
	protected CityStatus readResponse(InputStream in) {

		CityParser offerParser = new CityParser();

		JsonParserInvoker<CityStatus> parser = new JsonParserInvoker<CityStatus>();

		try {

			List<CityStatus> nodes = parser.parseNodes(in, offerParser);

			if (nodes != null && nodes.size() > 0) {

				CityStatus status = nodes.get(0);

				return status;
			}
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return null;
	}

	public boolean requestCitiesInfo() {

		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.MINUTE, 5);

		String expires = new SimpleDateFormat("MMM dd yyyy HH:mm:ss")
				.format(calendar.getTime());

		String rawSignature = City.APP_ID + City.SECRECT_KEY + expires
				+ City.API_CITIES_REQUEST;

		String signature = "";

		try {

			signature = sha1(rawSignature);

		} catch (InvalidKeyException e) {
			Logger.w("InvalidKeyException");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			Logger.w("UnsupportedEncodingException");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			Logger.w("NoSuchAlgorithmException");
			e.printStackTrace();
		}

		Logger.w(signature);

		Logger.w(expires);

		HttpPostConnection connection = new HttpPostConnection(
				City.GET_CITIES_API);

		connection.addParam(City.APP_ID_PARAM, City.APP_ID);

		connection.addParam(City.SIGNATURE_PARAM, signature);

		connection.addParam(City.EXPIRES_PARAM, expires);

		return executeConnection(connection);
	}

	private String sha1(String text) throws UnsupportedEncodingException,
			NoSuchAlgorithmException, InvalidKeyException {

		MessageDigest digest = null;

		digest = MessageDigest.getInstance("SHA-1");

		digest.reset();

		byte[] data = digest.digest(text.getBytes("UTF8"));

		StringBuffer hash = new StringBuffer();

		for (int i = 0; i < data.length; i++) {

			String h = Integer.toHexString(0xFF & data[i]);

			while (h.length() < 2) {
				h = "0" + h;
			}

			hash.append(h);
		}

		return hash.toString();
	}

}
