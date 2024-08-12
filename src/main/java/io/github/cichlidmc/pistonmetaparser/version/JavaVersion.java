package io.github.cichlidmc.pistonmetaparser.version;

import io.github.cichlidmc.pistonmetaparser.util.JsonUtils;
import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class JavaVersion {
	public final String component;
	public final int majorVersion;

	public JavaVersion(String component, int majorVersion) {
		this.component = component;
		this.majorVersion = majorVersion;
	}

	public static JavaVersion parse(JsonValue value) {
		JsonObject json = value.asObject();
		String component = json.get("component").asString().value();
		int majorVersion = JsonUtils.parseInt(json.get("majorVersion"));
		return new JavaVersion(component, majorVersion);
	}
}
