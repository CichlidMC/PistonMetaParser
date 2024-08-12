package io.github.cichlidmc.pistonmetaparser.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.cichlidmc.pistonmetaparser.VersionManifest;

public class FindFeatures {
	public static void run(VersionManifest manifest) {
		Map<Set<String>, List<String>> map = new HashMap<>();
		manifest.versions.forEach(
				version -> version.expand().arguments.ifLeft(
						split -> split.forEach(
								arg -> arg.rules.forEach(
										rule -> rule.features.ifPresent(
												features -> map.put(features.map.keySet(), arg.values)
										)
								)
						)
				)
		);
		map.forEach((features, args) -> System.out.println(features + ": " + args));
	}
}
