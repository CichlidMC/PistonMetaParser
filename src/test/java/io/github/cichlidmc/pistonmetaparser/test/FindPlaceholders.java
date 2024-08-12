package io.github.cichlidmc.pistonmetaparser.test;

import java.util.HashSet;
import java.util.Set;

import io.github.cichlidmc.pistonmetaparser.FullVersion;
import io.github.cichlidmc.pistonmetaparser.VersionManifest;
import io.github.cichlidmc.pistonmetaparser.manifest.Version;

public class FindPlaceholders {
	public static void run(VersionManifest manifest) {
		Set<String> placeholders = new HashSet<>();
		for (Version shortVersion : manifest.versions) {
			FullVersion version = shortVersion.expand();
			version.arguments.ifRight(args -> getPlaceholders(placeholders, args.value));
			version.arguments.ifLeft(split -> {
				split.game.forEach(arg -> arg.values.forEach(val -> getPlaceholders(placeholders, val)));
				split.jvm.forEach(arg -> arg.values.forEach(val -> getPlaceholders(placeholders, val)));
			});
		}
		System.out.println(placeholders);
	}

	private static void getPlaceholders(Set<String> placeholders, String s) {
		int state = 0;
		StringBuilder placeholder = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '$') {
				state = 1;
			} else if (state == 1 && c == '{') {
				state = 2;
			} else if (state == 2) {
				if (c == '}') {
					state = 0;
					placeholders.add(placeholder.toString());
					placeholder = new StringBuilder();
				} else {
					placeholder.append(c);
				}
			}
		}
	}
}
