package io.github.cichlidmc.pistonmetaparser.test;

import io.github.cichlidmc.pistonmetaparser.PistonMeta;
import io.github.cichlidmc.pistonmetaparser.VersionManifest;

public class Analysis {
	public static void main(String[] ignored) {
		VersionManifest manifest = PistonMeta.fetch();
		manifest.preFetch();
		System.out.println("Manifest fully downloaded");
		FindPlaceholders.run(manifest);
		FindFeatures.run(manifest);
		FindNeverUsedFields.run(manifest);
	}
}
