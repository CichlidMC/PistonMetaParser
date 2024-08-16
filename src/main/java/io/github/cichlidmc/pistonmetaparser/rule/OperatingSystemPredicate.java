package io.github.cichlidmc.pistonmetaparser.rule;

import java.util.Optional;
import java.util.regex.Pattern;

import io.github.cichlidmc.pistonmetaparser.util.JsonUtils;
import io.github.cichlidmc.pistonmetaparser.util.system.Architecture;
import io.github.cichlidmc.pistonmetaparser.util.system.OperatingSystem;
import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class OperatingSystemPredicate {
	public final Optional<OperatingSystem> name;
	public final Optional<Architecture> arch;
	public final Optional<Pattern> version;

	public OperatingSystemPredicate(Optional<OperatingSystem> name, Optional<Architecture> arch, Optional<Pattern> version) {
		this.name = name;
		this.arch = arch;
		this.version = version;
	}

	public boolean matches() {
		if (this.name.isPresent() && this.name.get() != OperatingSystem.CURRENT)
			return false;
		if (this.arch.isPresent() && this.arch.get() != Architecture.CURRENT)
			return false;
		return !this.version.isPresent() || this.version.get().matcher(OperatingSystem.VERSION).matches();
	}

	public static OperatingSystemPredicate parse(JsonValue value) {
		JsonObject json = value.asObject();

		Optional<OperatingSystem> os = json.getOptional("name").map(OperatingSystem::parse);
		Optional<Architecture> arch = json.getOptional("arch").map(Architecture::parse);
		Optional<Pattern> version = json.getOptional("version").map(JsonUtils::parseRegex);

		return new OperatingSystemPredicate(os, arch, version);
	}
}
