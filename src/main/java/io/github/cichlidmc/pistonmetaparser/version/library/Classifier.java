package io.github.cichlidmc.pistonmetaparser.version.library;

import io.github.cichlidmc.tinyjson.value.JsonValue;

public class Classifier {
	public final String name;
	public final Artifact artifact;

	public Classifier(String name, Artifact artifact) {
		this.name = name;
		this.artifact = artifact;
	}

	public static Classifier parse(String key, JsonValue value) {
		Artifact artifact = Artifact.parse(value);
		return new Classifier(key, artifact);
	}
}
