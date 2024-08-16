package io.github.cichlidmc.pistonmetaparser;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.github.cichlidmc.pistonmetaparser.manifest.LatestVersions;
import io.github.cichlidmc.pistonmetaparser.manifest.Version;
import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class VersionManifest {

	public final LatestVersions latest;
	public final List<Version> versions;

	private final Map<String, Version> versionMap;

	private VersionManifest(LatestVersions latest, List<Version> versions) {
		this.latest = latest;
		this.versions = versions;
		this.versionMap = versions.stream().collect(Collectors.toMap(v -> v.id, Function.identity()));
	}

	public Version getVersion(String id) {
		return this.versionMap.get(id);
	}

	public void preFetchVersions() {
		this.versions.forEach(Version::expand);
	}

	public void preFetch() {
		this.versions.forEach(version -> version.expand().assetIndex.expand());
	}

	public static VersionManifest parse(JsonValue value) {
		JsonObject json = value.asObject();
		LatestVersions latest = LatestVersions.parse(json.get("latest"));
		List<Version> versions = json.get("versions").asArray().stream()
				.map(JsonValue::asObject)
				.map(Version::parse)
				.collect(Collectors.toList());

		return new VersionManifest(latest, versions);
	}
}
