package fish.cichlidmc.pistonmetaparser.version.logging;

import java.net.URI;

import fish.cichlidmc.pistonmetaparser.util.JsonUtils;
import fish.cichlidmc.tinyjson.value.JsonValue;
import fish.cichlidmc.tinyjson.value.composite.JsonObject;

public class LoggingFile {
	public final String id;
	public final String sha1;
	public final int size;
	public final URI url;

	public LoggingFile(String id, String sha1, int size, URI url) {
		this.id = id;
		this.sha1 = sha1;
		this.size = size;
		this.url = url;
	}

	public static LoggingFile parse(JsonValue value) {
		JsonObject json = value.asObject();

		String id = json.get("id").asString().value();
		String sha1 = json.get("sha1").asString().value();
		int size = JsonUtils.parseInt(json.get("size"));
		URI url = JsonUtils.parseUri(json.get("url"));

		return new LoggingFile(id, sha1, size, url);
	}
}
