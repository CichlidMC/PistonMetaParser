package io.github.cichlidmc.pistonmetaparser.version.library;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.cichlidmc.pistonmetaparser.util.Either;
import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;
import io.github.cichlidmc.tinyjson.value.primitive.JsonString;

public class Natives {
	public final Optional<Either<Artifact, WindowsNatives>> windows;
	public final Optional<Artifact> linux;
	public final Optional<Artifact> macos;

	/**
	 * List of paths to be excluded from extraction. Usually only contains "META-INF/".
	 * Ignoring this is probably fine.
	 */
	public final List<String> extractExclude;

	public Natives(Optional<Either<Artifact, WindowsNatives>> windows, Optional<Artifact> linux, Optional<Artifact> macos, List<String> extractExclude) {
		this.windows = windows;
		this.linux = linux;
		this.macos = macos;
		this.extractExclude = extractExclude;
	}

	/**
	 * Natives are weirdly organized, this is parsed from the library's root object.
	 */
	public static Optional<Natives> parse(JsonObject library) {
		return library.get("downloads").asObject().getOptional("classifiers").map(value -> {
			JsonObject classifiers = value.asObject();
			JsonObject names = library.get("natives").asObject();
			Optional<Either<Artifact, WindowsNatives>> windows = WindowsNatives.parse(classifiers, names);
			Optional<Artifact> linux = getArtifact(classifiers, names, "linux");
			Optional<Artifact> macos = getArtifact(classifiers, names, "macos");

			List<String> extractExclude = library.getOptional("extract").map(extract -> extract.asObject()
					.get("exclude").asArray().stream()
					.map(JsonValue::asString)
					.map(JsonString::value)
					.collect(Collectors.toList())
			).orElse(Collections.emptyList());

			return new Natives(windows, linux, macos, extractExclude);
		});
	}

	private static Optional<Artifact> getArtifact(JsonObject classifiers, JsonObject names, String key) {
		return names.getOptional(key).flatMap(value -> {
			String string = value.asString().value();
			return classifiers.getOptional(string).map(Artifact::parse);
		});
	}
}
