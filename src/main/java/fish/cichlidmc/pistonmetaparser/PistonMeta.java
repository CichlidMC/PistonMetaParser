package fish.cichlidmc.pistonmetaparser;

import java.net.URI;

import fish.cichlidmc.tinyjson.TinyJson;
import fish.cichlidmc.tinyjson.value.JsonValue;

public class PistonMeta {
	public static final URI URL = URI.create("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json");

	public static VersionManifest fetch() {
		JsonValue json = TinyJson.fetchOrThrow(URL);
		return VersionManifest.parse(json.asObject());
	}
}
