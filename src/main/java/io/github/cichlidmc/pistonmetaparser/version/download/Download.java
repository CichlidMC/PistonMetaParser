package io.github.cichlidmc.pistonmetaparser.version.download;

import java.net.URI;

import io.github.cichlidmc.pistonmetaparser.util.Downloadable;
import io.github.cichlidmc.pistonmetaparser.util.JsonUtils;
import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class Download implements Downloadable {
	public final String sha1;
	public final int size;
	public final URI url;

	public Download(String sha1, int size, URI url) {
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

	public static Download parse(JsonValue value) {
		JsonObject json = value.asObject();

		String sha1 = json.get("sha1").asString().value();
		int size = JsonUtils.parseInt(json.get("size"));
		URI url = JsonUtils.parseUri(json.get("url"));

		return new Download(sha1, size, url);
	}
}
