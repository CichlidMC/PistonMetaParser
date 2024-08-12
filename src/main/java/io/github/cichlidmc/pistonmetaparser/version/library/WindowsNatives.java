package io.github.cichlidmc.pistonmetaparser.version.library;

import java.util.Optional;

import io.github.cichlidmc.pistonmetaparser.util.Either;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class WindowsNatives {
	public static final String ARCH_PLACEHOLDER = "${arch}";

	public final Artifact windows32;
	public final Artifact windows64;

	public WindowsNatives(Artifact windows32, Artifact windows64) {
		this.windows32 = windows32;
		this.windows64 = windows64;
	}

	public static Optional<Either<Artifact, WindowsNatives>> parse(JsonObject classifiers, JsonObject names) {
		return names.getOptional("windows").flatMap(value -> {
			String windowsName = value.asString().value();
			if (!windowsName.contains(ARCH_PLACEHOLDER)) {
				return classifiers.getOptional(windowsName).map(Artifact::parse).map(Either::left);
			}

			String w32Name = windowsName.replace(ARCH_PLACEHOLDER, "32");
			String w64Name = windowsName.replace(ARCH_PLACEHOLDER, "64");
			if (classifiers.contains(w32Name) || classifiers.contains(w64Name)) {
				Artifact w32 = Artifact.parse(classifiers.get(w32Name));
				Artifact w64 = Artifact.parse(classifiers.get(w64Name));
				return Optional.of(Either.right(new WindowsNatives(w32, w64)));
			}

			return Optional.empty();
		});
	}
}
