package io.github.cichlidmc.pistonmetaparser.util.system;

import java.util.Locale;

import io.github.cichlidmc.tinyjson.JsonException;
import io.github.cichlidmc.tinyjson.value.JsonValue;

public enum OperatingSystem {
	WINDOWS, MACOS, LINUX;

	public static final OperatingSystem CURRENT;
	public static final String VERSION = System.getProperty("os.version");

	static {
		String string = System.getProperty("os.name").toLowerCase(Locale.ROOT);
		if (string.contains("win")) {
			CURRENT = WINDOWS;
		} else if (string.contains("mac")) {
			CURRENT = MACOS;
		} else {
			CURRENT = LINUX; // best effort guess
		}
	}

	public static OperatingSystem parse(JsonValue value) {
		String string = value.asString().value();
		switch (string) {
			case "windows": return WINDOWS;
			case "osx": return MACOS;
			case "linux": return LINUX;
			default: throw new JsonException(value, "Invalid OperatingSystem: " + string);
		}
	}
}
