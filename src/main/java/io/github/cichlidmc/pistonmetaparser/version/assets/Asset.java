package io.github.cichlidmc.pistonmetaparser.version.assets;

import java.net.URI;

import io.github.cichlidmc.pistonmetaparser.util.Downloadable;
import io.github.cichlidmc.pistonmetaparser.util.JsonUtils;
import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class Asset implements Downloadable {
	public static final String URL_ROOT = "https://resources.download.minecraft.net/";

	public final String hash;
	public final int size;

	public final String path;
	public final URI url;

	public Asset(String hash, int size) {
		this.hash = hash;
		this.size = size;

		String hashStart = this.hash.substring(0, 2);
		this.path = hashStart + '/' + this.hash;

		this.url = URI.create(URL_ROOT + this.path);
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
		return this.hash;
	}

	public static Asset parse(JsonValue value) {
		JsonObject json = value.asObject();

		String hash = json.get("hash").asString().value();
		int size = JsonUtils.parseInt(json.get("size"));

		return new Asset(hash, size);
	}
}
