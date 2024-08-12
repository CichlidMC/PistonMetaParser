package io.github.cichlidmc.pistonmetaparser.version.library;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.cichlidmc.pistonmetaparser.rule.Rule;
import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class Library {
	public final String name;
	public final Optional<Artifact> artifact;
	public final Optional<Natives> natives;
	public final List<Rule> rules;

	public Library(String name, Optional<Artifact> artifact, Optional<Natives> natives, List<Rule> rules) {
		this.name = name;
		this.artifact = artifact;
		this.natives = natives;
		this.rules = rules;
	}

	public static Library parse(JsonValue value) {
		JsonObject json = value.asObject();

		String name = json.get("name").asString().value();
		Optional<Artifact> artifact = json.get("downloads").asObject().getOptional("artifact").map(Artifact::parse);
		Optional<Natives> natives = Natives.parse(json);
		List<Rule> rules = json.getOptional("rules")
				.map(r -> r.asArray().stream().map(Rule::parse).collect(Collectors.toList()))
				.orElse(Collections.emptyList());

		return new Library(name, artifact, natives, rules);
	}
}
