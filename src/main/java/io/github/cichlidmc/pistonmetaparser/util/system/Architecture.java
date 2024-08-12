package io.github.cichlidmc.pistonmetaparser.util.system;

import io.github.cichlidmc.tinyjson.JsonException;
import io.github.cichlidmc.tinyjson.value.JsonValue;

public enum Architecture {
	X64(64), X86(32), ARM64(64), ARM32(32);

	public static final Architecture CURRENT;

	public final int bits;

	Architecture(int bits) {
		this.bits = bits;
	}

	static {
		String arch = System.getProperty("os.arch");
		boolean is64Bit = arch.contains("64") || arch.startsWith("armv8");

		if (arch.startsWith("arm") || arch.startsWith("aarch64")) {
			CURRENT = is64Bit ? ARM64 : ARM32;
		} else {
			CURRENT = is64Bit ? X64 : X86;
		}
	}

	public static Architecture parse(JsonValue value) {
		String string = value.asString().value();
		switch (string) {
			case "x64": return X64;
			case "x86": return X86;
			case "arm64": return ARM64;
			case "arm32": return ARM32;
			default: throw new JsonException(value, "Invalid Architecture: " + string);
		}
	}
}
