package io.github.cichlidmc.pistonmetaparser.version.library;

import java.util.Optional;

import io.github.cichlidmc.pistonmetaparser.util.Either;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class WindowsNatives {
	public static final String ARCH_PLACEHOLDER = "${arch}";

	public final Classifier windows32;
	public final Classifier windows64;

	public WindowsNatives(Classifier windows32, Classifier windows64) {
		this.windows32 = windows32;
		this.windows64 = windows64;
	}

	public static Optional<Either<Classifier, WindowsNatives>> parse(JsonObject classifiers, JsonObject names) {
		return names.getOptional("windows").flatMap(value -> {
			String windowsName = value.asString().value();
			if (!windowsName.contains(ARCH_PLACEHOLDER)) {
				return classifiers.getOptional(windowsName).map(val -> Classifier.parse(windowsName, val)).map(Either::left);
			}

			String w32Name = windowsName.replace(ARCH_PLACEHOLDER, "32");
			String w64Name = windowsName.replace(ARCH_PLACEHOLDER, "64");
			if (classifiers.contains(w32Name) || classifiers.contains(w64Name)) {
				Classifier w32 = Classifier.parse(w32Name, classifiers.get(w32Name));
				Classifier w64 = Classifier.parse(w64Name, classifiers.get(w64Name));
				return Optional.of(Either.right(new WindowsNatives(w32, w64)));
			}

			return Optional.empty();
		});
	}
}
