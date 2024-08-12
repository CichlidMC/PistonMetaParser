package io.github.cichlidmc.pistonmetaparser.version.logging;

import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class LoggingConfig {
	public final String argument;
	public final LoggingFile file;
	public final LoggingType type;

	public LoggingConfig(String argument, LoggingFile file, LoggingType type) {
		this.argument = argument;
		this.file = file;
		this.type = type;
	}

	public static LoggingConfig parse(JsonValue value) {
		JsonObject json = value.asObject();

		String argument = json.get("argument").asString().value();
		LoggingFile file = LoggingFile.parse(json.get("file"));
		LoggingType type = LoggingType.parse(json.get("type"));

		return new LoggingConfig(argument, file, type);
	}
}
