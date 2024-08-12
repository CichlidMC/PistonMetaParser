package io.github.cichlidmc.pistonmetaparser.version.logging;

import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class Logging {
	public final LoggingConfig client;

	public Logging(LoggingConfig client) {
		this.client = client;
	}

	public static Logging parse(JsonValue value) {
		JsonObject json = value.asObject();
		LoggingConfig client = LoggingConfig.parse(json.get("client"));
		return new Logging(client);
	}
}
