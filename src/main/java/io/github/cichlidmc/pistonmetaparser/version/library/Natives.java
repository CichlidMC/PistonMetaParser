package io.github.cichlidmc.pistonmetaparser.version.library;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.cichlidmc.pistonmetaparser.util.Either;
import io.github.cichlidmc.pistonmetaparser.util.system.Architecture;
import io.github.cichlidmc.pistonmetaparser.util.system.OperatingSystem;
import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;
import io.github.cichlidmc.tinyjson.value.primitive.JsonString;

public class Natives {
	public final Optional<Either<Classifier, WindowsNatives>> windows;
	public final Optional<Classifier> linux;
	public final Optional<Classifier> macos;

	/**
	 * List of paths to be excluded from extraction. Usually only contains "META-INF/".
	 * Ignoring this is probably fine.
	 */
	public final List<String> extractExclude;

	public Natives(Optional<Either<Classifier, WindowsNatives>> windows, Optional<Classifier> linux, Optional<Classifier> macos, List<String> extractExclude) {
		this.windows = windows;
		this.linux = linux;
		this.macos = macos;
		this.extractExclude = extractExclude;
	}

	/**
	 * Choose the native artifact for the current system.
	 */
	public Optional<Classifier> choose() {
		if (OperatingSystem.CURRENT == OperatingSystem.WINDOWS && this.windows.isPresent()) {
			Either<Classifier, WindowsNatives> either = this.windows.get();
			if (either.isLeft()) {
				return Optional.of(either.left());
			} else {
				WindowsNatives windows = either.right();
				Classifier classifier = Architecture.CURRENT.bits == 64 ? windows.windows64 : windows.windows32;
				return Optional.of(classifier);
			}
		} else if (OperatingSystem.CURRENT == OperatingSystem.MACOS) {
			return this.macos;
		} else if (OperatingSystem.CURRENT == OperatingSystem.LINUX) {
			return this.linux;
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Natives are weirdly organized, this is parsed from the library's root object.
	 */
	public static Optional<Natives> parse(JsonObject library) {
		return library.get("downloads").asObject().getOptional("classifiers").map(value -> {
			JsonObject classifiers = value.asObject();
			JsonObject names = library.get("natives").asObject();
			Optional<Either<Classifier, WindowsNatives>> windows = WindowsNatives.parse(classifiers, names);
			Optional<Classifier> linux = getClassifier(classifiers, names, "linux");
			Optional<Classifier> macos = getClassifier(classifiers, names, "osx");

			List<String> extractExclude = library.getOptional("extract").map(extract -> extract.asObject()
					.get("exclude").asArray().stream()
					.map(JsonValue::asString)
					.map(JsonString::value)
					.collect(Collectors.toList())
			).orElse(Collections.emptyList());

			return new Natives(windows, linux, macos, extractExclude);
		});
	}

	private static Optional<Classifier> getClassifier(JsonObject classifiers, JsonObject names, String key) {
		return names.getOptional(key).flatMap(value -> {
			String string = value.asString().value();
			return classifiers.getOptional(string).map(val -> Classifier.parse(string, val));
		});
	}
}
