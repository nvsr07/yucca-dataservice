package org.csi.yucca.dataservice.metadataapi.util.json;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class JSonHelper {
	private static Gson gson;

	public static Gson getInstance() {
		if (gson == null)
			gson = new GsonBuilder().setExclusionStrategies(new GSONExclusionStrategy()).disableHtmlEscaping().setPrettyPrinting()
					.registerTypeAdapter(Date.class, new DateDeserializer()).registerTypeAdapter(Boolean.class, booleanAsIntAdapter).registerTypeAdapter(boolean.class, booleanAsIntAdapter).create();
		return gson;
	}

	private static final TypeAdapter<Boolean> booleanAsIntAdapter = new TypeAdapter<Boolean>() {
		@Override
		public void write(JsonWriter out, Boolean value) throws IOException {
			if (value == null) {
				out.nullValue();
			} else {
				out.value(value);
			}
		}

		@Override
		public Boolean read(JsonReader in) throws IOException {
			JsonToken peek = in.peek();
			switch (peek) {
			case BOOLEAN:
				return in.nextBoolean();
			case NULL:
				in.nextNull();
				return null;
			case NUMBER:
				return in.nextInt() != 0;
			case STRING:
				return Boolean.parseBoolean(in.nextString());
			default:
				throw new IllegalStateException("Expected BOOLEAN or NUMBER but was " + peek);
			}
		}
	};

	private static final String[] DATE_FORMATS = new String[] { "MMM dd, yyyy HH:mm:ss", "MMM dd, yyyy", "yyyy-MM-dd" };

	private static class DateDeserializer implements JsonDeserializer<Date> {

		@Override
		public Date deserialize(JsonElement jsonElement, Type typeOF, JsonDeserializationContext context) throws JsonParseException {
			for (String format : DATE_FORMATS) {
				try {
					return new SimpleDateFormat(format, Locale.US).parse(jsonElement.getAsString());
				} catch (ParseException e) {
				}
			}
			throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString() + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
		}

	}
}
