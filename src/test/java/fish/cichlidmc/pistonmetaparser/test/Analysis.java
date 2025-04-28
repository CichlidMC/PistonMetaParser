package fish.cichlidmc.pistonmetaparser.test;

import fish.cichlidmc.pistonmetaparser.PistonMeta;
import fish.cichlidmc.pistonmetaparser.VersionManifest;

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
