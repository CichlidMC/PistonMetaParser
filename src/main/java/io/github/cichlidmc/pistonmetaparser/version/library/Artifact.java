package io.github.cichlidmc.pistonmetaparser.version.library;

import java.net.URI;

import io.github.cichlidmc.pistonmetaparser.util.Downloadable;
import io.github.cichlidmc.pistonmetaparser.util.JsonUtils;
import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class Artifact implements Downloadable {
	public final String path;
	public final String sha1;
	public final int size;
	public final URI url;

	public Artifact(String path, String sha1, int size, URI url) {
		this.path = path;
		this.sha1 = sha1;
		this.size = size;
		this.url = url;
	}

	@Override
	public URI url() {
		return this.url;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public String sha1() {
		return this.sha1;
	}

	public static Artifact parse(JsonValue value) {
		JsonObject json = value.asObject();

		String path = json.get("path").asString().value();
		String sha1 = json.get("sha1").asString().value();
		int size = JsonUtils.parseInt(json.get("size"));
		URI url = JsonUtils.parseUri(json.get("url"));

		return new Artifact(path, sha1, size, url);
	}
}
