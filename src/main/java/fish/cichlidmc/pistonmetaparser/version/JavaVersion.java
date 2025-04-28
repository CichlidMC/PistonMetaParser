package fish.cichlidmc.pistonmetaparser.version;

import fish.cichlidmc.pistonmetaparser.util.JsonUtils;
import fish.cichlidmc.tinyjson.value.JsonValue;
import fish.cichlidmc.tinyjson.value.composite.JsonObject;

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
