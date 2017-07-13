/*
 * 
 */
package com.pooja.carepack.volly;

import java.lang.reflect.Type;
import java.sql.Timestamp;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

// TODO: Auto-generated Javadoc
/**
 * The Class TimestampDeserializer.
 */
public class TimestampDeserializer implements JsonDeserializer<Timestamp> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		long time = Long.parseLong(json.getAsString());
		return new Timestamp(time * 1000);
	}
}