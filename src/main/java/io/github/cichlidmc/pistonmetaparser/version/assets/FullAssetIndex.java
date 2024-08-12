package io.github.cichlidmc.pistonmetaparser.version.assets;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;
import io.github.cichlidmc.tinyjson.value.primitive.JsonBool;

public class FullAssetIndex {
	/**
	 * Indicates that this index needs to be extracted.
	 * This is only present in the 'legacy' index, set to true.
	 */
	public final Optional<Boolean> virtual;
	/**
	 * Purpose of this field is unknown. Only specified in 'pre-1.6' as true.
	 */
	public final Optional<Boolean> mapToResources;

	public final Map<String, Asset> objects;

	public FullAssetIndex(Optional<Boolean> virtual, Optional<Boolean> mapToResources, Map<String, Asset> objects) {
		this.virtual = virtual;
		this.mapToResources = mapToResources;
		this.objects = objects;
	}

	public boolean isVirtual() {
		return this.virtual.isPresent() && this.virtual.get();
	}

	public static FullAssetIndex parse(JsonValue value) {
		JsonObject json = value.asObject();

		Optional<Boolean> virtual = json.getOptional("virtual").map(JsonValue::asBoolean).map(JsonBool::value);
		Optional<Boolean> mapToResources = json.getOptional("map_to_resources").map(JsonValue::asBoolean).map(JsonBool::value);

		Map<String, Asset> objects = new HashMap<>();
		json.get("objects").asObject().forEach((path, asset) -> {
			Asset parsed = Asset.parse(asset);
			objects.put(path, parsed);
		});

		return new FullAssetIndex(virtual, mapToResources, Collections.unmodifiableMap(objects));
	}
}
