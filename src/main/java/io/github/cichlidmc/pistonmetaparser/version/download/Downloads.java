package io.github.cichlidmc.pistonmetaparser.version.download;

import java.util.Optional;

import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class Downloads {
	public final Download client;
	public final Optional<Download> server;
	public final Optional<Download> clientMappings;
	public final Optional<Download> serverMappings;
	public final Optional<Download> windowsServer;

	public Downloads(Download client, Optional<Download> server, Optional<Download> clientMappings,
					 Optional<Download> serverMappings, Optional<Download> windowsServer) {
		this.client = client;
		this.server = server;
		this.clientMappings = clientMappings;
		this.serverMappings = serverMappings;
		this.windowsServer = windowsServer;
	}

	public static Downloads parse(JsonValue value) {
		JsonObject json = value.asObject();

		Download client = Download.parse(json.get("client"));
		Optional<Download> server = json.getOptional("server").map(Download::parse);
		Optional<Download> clientMappings = json.getOptional("client_mappings").map(Download::parse);
		Optional<Download> serverMappings = json.getOptional("server_mappings").map(Download::parse);
		Optional<Download> windowsServer = json.getOptional("windows_server").map(Download::parse);

		return new Downloads(client, server, clientMappings, serverMappings, windowsServer);
	}
}
