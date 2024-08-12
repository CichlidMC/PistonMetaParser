package io.github.cichlidmc.pistonmetaparser.manifest;

import java.net.URI;
import java.util.Date;

import io.github.cichlidmc.pistonmetaparser.FullVersion;
import io.github.cichlidmc.pistonmetaparser.VersionType;
import io.github.cichlidmc.pistonmetaparser.util.JsonUtils;
import io.github.cichlidmc.tinyjson.TinyJson;
import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class Version {
	public final String id;
	public final VersionType type;
	public final URI url;
	public final Date time;
	public final Date releaseTime;
	public final String sha1;
	public final int complianceLevel;

	private FullVersion full;

	public Version(String id, VersionType type, URI url, Date time, Date releaseTime, String sha1, int complianceLevel) {
		this.id = id;
		this.type = type;
		this.url = url;
		this.time = time;
		this.releaseTime = releaseTime;
		this.sha1 = sha1;
		this.complianceLevel = complianceLevel;
	}

	public FullVersion expand() {
		if (this.full == null) {
			JsonObject json = TinyJson.fetchOrThrow(this.url).asObject();
			this.full = FullVersion.parse(json);
		}
		return this.full;
	}

	public static Version parse(JsonValue value) {
		JsonObject json = value.asObject();

		String id = json.get("id").asString().value();
		VersionType type = VersionType.parse(json.get("type"));
		URI url = JsonUtils.parseUri(json.get("url"));
		Date time = JsonUtils.parseIsoDate(json.get("time"));
		Date releaseTime = JsonUtils.parseIsoDate(json.get("releaseTime"));
		String sha1 = json.get("sha1").asString().value();
		int complianceLevel = JsonUtils.parseInt(json.get("complianceLevel"));

		return new Version(id, type, url, time, releaseTime, sha1, complianceLevel);
	}
}
