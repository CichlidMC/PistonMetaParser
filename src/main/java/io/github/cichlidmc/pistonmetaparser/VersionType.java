package io.github.cichlidmc.pistonmetaparser;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.github.cichlidmc.tinyjson.JsonException;
import io.github.cichlidmc.tinyjson.value.JsonValue;

public enum VersionType {
	SNAPSHOT, RELEASE, OLD_ALPHA, OLD_BETA;

	public static final Map<String, VersionType> BY_NAME = Arrays.stream(VersionType.values())
			.collect(Collectors.toMap(type -> type.name, Function.identity()));

	public final String name = this.name().toLowerCase(Locale.ROOT);

	public static VersionType parse(JsonValue value) {
		String name = value.asString().value();
		if (!BY_NAME.containsKey(name)) {
			throw new JsonException(value, "Invalid VersionType: " + name);
		}
		return BY_NAME.get(name);
	}
}
