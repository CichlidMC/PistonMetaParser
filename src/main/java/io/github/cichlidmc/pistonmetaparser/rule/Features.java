package io.github.cichlidmc.pistonmetaparser.rule;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class Features {
	public static final Features EMPTY = new Features(Collections.emptyMap());

	public final Map<String, Boolean> map;

	public Features(Map<String, Boolean> map) {
		this.map = map;
	}

	public boolean matches(Features present) {
		for (Map.Entry<String, Boolean> entry : this.map.entrySet()) {
			String key = entry.getKey();
			boolean required = entry.getValue();

			if (!present.map.containsKey(key)) {
				// key is not present, if it's required to be true then fail
				if (required) {
					return false;
				}
			} else {
				// key is present, require that it matches
				boolean actual = present.map.get(key);
				if (required != actual)
					return false;
			}
		}

		return true;
	}

	public static Features parse(JsonValue value) {
		JsonObject json = value.asObject();

		Map<String, Boolean> map = new HashMap<>();
		json.forEach((feature, val) -> map.put(feature, val.asBoolean().value()));

		return new Features(Collections.unmodifiableMap(map));
	}
}
