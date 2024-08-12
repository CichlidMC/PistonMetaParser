package io.github.cichlidmc.pistonmetaparser.version.logging;

import io.github.cichlidmc.tinyjson.JsonException;
import io.github.cichlidmc.tinyjson.value.JsonValue;

public enum LoggingType {
	LOG4J2_XML("log4j2-xml");

	public final String name;

	LoggingType(String name) {
		this.name = name;
	}

	public static LoggingType parse(JsonValue value) {
		String name = value.asString().value();
		if (name.equals(LOG4J2_XML.name)) {
			return LOG4J2_XML;
		} else {
			throw new JsonException(value, "Invalid LoggingType: " + name);
		}
	}
}
