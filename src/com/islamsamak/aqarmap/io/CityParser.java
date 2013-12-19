/**
 * 
 */
package com.islamsamak.aqarmap.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.util.JsonReader;
import android.util.JsonToken;

import com.islamsamak.aqarmap.model.City;
import com.oneteam.framework.android.io.Parser;
import com.oneteam.framework.android.utils.Logger;

/**
 * @author islam
 * 
 */
public class CityParser implements Parser<CityStatus> {

	public CityParser() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.oneteam.framework.android.pattern.Command#execute()
	 */
	@Override
	public void execute() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.oneteam.framework.android.io.Parser#execute(android.util.JsonReader)
	 */
	@Override
	public List<CityStatus> execute(JsonReader reader) {

		CityStatus node;

		try {

			node = parseObject(reader);

		} catch (IOException e) {

			Logger.w("Failed to read the action status");

			e.printStackTrace();

			return null;
		}

		List<CityStatus> nodes = new ArrayList<CityStatus>();

		nodes.add(node);

		return nodes;
	}

	/**
	 * @param reader
	 * @param parser
	 * @throws IOException
	 */
	protected CityStatus parseObject(JsonReader reader) throws IOException {

		CityStatus node = new CityStatus();

		reader.beginObject();

		while (reader.hasNext()) {

			String name = reader.nextName();

			if (reader.peek() == JsonToken.NULL) {

				reader.skipValue();

				continue;
			}

			if (TextUtils.isEmpty(name)) {
				continue;
			}

			if (CityStatus.RESPONSE_TAG.toString().equals(name)) {

				node = readResponse(reader);

			} else if (CityStatus.MESSAGE_TAG.toString().equals(name)) {

				String msg = readMessage(reader);

				node.setMessage(msg);

			} else {

				reader.skipValue();

				continue;
			}
		}

		reader.endObject();

		return node;
	}

	/**
	 * @param reader
	 * @throws IOException
	 */
	protected CityStatus readResponse(JsonReader reader) throws IOException {

		CityStatus node = new CityStatus();

		reader.beginObject();

		while (reader.hasNext()) {

			String name = reader.nextName();

			if (reader.peek() == JsonToken.NULL) {
				reader.skipValue();

				continue;
			}

			if (TextUtils.isEmpty(name)) {
				continue;
			}

			if (CityStatus.STATUS_TAG.toString().equals(name)) {

				String state = reader.nextString();

				boolean status = "OK".equalsIgnoreCase(state);

				node.setStatus(status);

			} else if (CityStatus.RESULTS_TAG.toString().equals(name)) {

				JsonToken token = reader.peek();

				if (token == JsonToken.BEGIN_ARRAY) {

					List<City> stores = parseResultsArray(reader);

					node.setResults(stores);
				}

			} else {

				reader.skipValue();

				continue;
			}
		}

		reader.endObject();

		return node;
	}

	/**
	 * @param reader
	 * @throws IOException
	 */
	protected List<City> parseResultsArray(JsonReader reader)
			throws IOException {

		List<City> nodes = new ArrayList<City>();

		reader.beginArray();

		while (reader.hasNext()) {

			City node = parseCity(reader);

			nodes.add(node);
		}

		reader.endArray();

		return nodes;
	}

	private City parseCity(JsonReader reader) throws IOException {

		City node = new City();

		reader.beginObject();

		while (reader.hasNext()) {

			String name = reader.nextName();

			if (reader.peek() == JsonToken.NULL) {

				reader.skipValue();

				continue;
			}

			if (City.CITY_TAG.toString().equals(name)) {

				node = readCity(reader);

			} else {

				reader.skipValue();

				continue;
			}
		}

		reader.endObject();

		return node;
	}

	private City readCity(JsonReader reader) throws IOException {

		City node = new City();

		reader.beginObject();

		while (reader.hasNext()) {

			String name = reader.nextName();

			if (reader.peek() == JsonToken.NULL) {

				reader.skipValue();

				continue;
			}

			if (City.TITLE_TAG.toString().equals(name)) {

				node.setTitle(reader.nextString());

			} else if (City.SLUG_TAG.toString().equals(name)) {

				node.setSlug(reader.nextString());

			} else if (City.LATITUDE_TAG.toString().equals(name)) {

				node.setLatitude(reader.nextDouble());

			} else if (City.LONGITUDE_TAG.toString().equals(name)) {

				node.setLongitude(reader.nextDouble());

			} else if (City.GEONAME_ID_TAG.toString().equals(name)) {

				node.setGeonameId(reader.nextInt());

			} else if (City.ZOOM_TAG.toString().equals(name)) {

				node.setZoom(reader.nextInt());

			}
		}

		reader.endObject();

		return node;
	}

	/**
	 * @param reader
	 * @throws IOException
	 */
	protected String readMessage(JsonReader reader) throws IOException {

		StringBuilder message = new StringBuilder(200);

		JsonToken token = reader.peek();

		if (token == JsonToken.BEGIN_ARRAY) {

			reader.beginArray();

			int count = 0;

			while (reader.hasNext()) {

				String msg = reader.nextString();

				if (count > 0) {

					message.append("\n");
				}

				message.append(msg);

				count++;
			}

			reader.endArray();

		} else {

			message.append(reader.nextString());
		}

		return message.toString();
	}

}
