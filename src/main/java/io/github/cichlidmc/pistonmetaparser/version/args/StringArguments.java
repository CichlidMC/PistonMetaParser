package io.github.cichlidmc.pistonmetaparser.version.args;

import io.github.cichlidmc.tinyjson.value.JsonValue;

public class StringArguments {
	public final String value;

	public StringArguments(String value) {
		this.value = value;
	}

	public static StringArguments parse(JsonValue value) {
		return new StringArguments(value.asString().value());
	}
}
