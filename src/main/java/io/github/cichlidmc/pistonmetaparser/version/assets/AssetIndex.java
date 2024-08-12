package io.github.cichlidmc.pistonmetaparser.version.assets;

import java.net.URI;

import io.github.cichlidmc.pistonmetaparser.util.Downloadable;
import io.github.cichlidmc.pistonmetaparser.util.JsonUtils;
import io.github.cichlidmc.tinyjson.TinyJson;
import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class AssetIndex implements Downloadable {
	public final String id;
	public final String sha1;
	public final int size;
	public final int totalSize;
	public final URI url;

	private FullAssetIndex full;

	public AssetIndex(String id, String sha1, int size, int totalSize, URI url) {
		this.id = id;
		this.sha1 = sha1;
		this.size = size;
		this.totalSize = totalSize;
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

	public FullAssetIndex expand() {
		if (this.full == null) {
			JsonObject json = TinyJson.fetchOrThrow(this.url).asObject();
			this.full = FullAssetIndex.parse(json);
		}
		return this.full;
	}

	public static AssetIndex parse(JsonValue value) {
		JsonObject json = value.asObject();

		String id = json.get("id").asString().value();
		String sha1 = json.get("sha1").asString().value();
		int size = JsonUtils.parseInt(json.get("size"));
		int totalSize = JsonUtils.parseInt(json.get("totalSize"));
		URI url = JsonUtils.parseUri(json.get("url"));

		return new AssetIndex(id, sha1, size, totalSize, url);
	}
}
