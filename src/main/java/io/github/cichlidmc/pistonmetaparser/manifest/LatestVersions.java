package io.github.cichlidmc.pistonmetaparser.manifest;

import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class LatestVersions {
	/**
	 * The newest release version published.
	 */
	public final String release;
	/**
	 * The newest version published. May be a release, snapshot, or any other version type.
	 */
	public final String snapshot;

	public LatestVersions(String release, String snapshot) {
		this.release = release;
		this.snapshot = snapshot;
	}

	public static LatestVersions parse(JsonValue value) {
		JsonObject json = value.asObject();
		String release = json.get("release").asString().value();
		String snapshot = json.get("snapshot").asString().value();
		return new LatestVersions(release, snapshot);
	}
}
