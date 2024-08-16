package io.github.cichlidmc.pistonmetaparser.test;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import io.github.cichlidmc.pistonmetaparser.VersionManifest;
import io.github.cichlidmc.pistonmetaparser.manifest.Version;
import io.github.cichlidmc.pistonmetaparser.rule.OperatingSystemPredicate;
import io.github.cichlidmc.pistonmetaparser.rule.Rule;
import io.github.cichlidmc.pistonmetaparser.util.Either;
import io.github.cichlidmc.pistonmetaparser.version.args.SplitArguments;
import io.github.cichlidmc.pistonmetaparser.version.assets.FullAssetIndex;
import io.github.cichlidmc.pistonmetaparser.version.download.Downloads;
import io.github.cichlidmc.pistonmetaparser.version.library.Library;
import io.github.cichlidmc.pistonmetaparser.version.library.Natives;

public class FindNeverUsedFields {
	public static void run(VersionManifest manifest) {
		UnusedFields fields = new UnusedFields();

		manifest.versions.stream().map(Version::expand).forEach(version -> {
			checkDownloads(fields, version.downloads);
			checkAssetIndex(fields, version.assetIndex.expand());
			fields.check("arguments", version.arguments);
			version.arguments.ifLeft(split -> checkSplitArguments(fields, split));
			fields.check("complianceLevel", version.complianceLevel);
			fields.check("javaVersion", version.javaVersion);
			version.libraries.forEach(lib -> checkLibrary(fields, lib));
			fields.check("logging", version.logging);
			fields.check("minimumLauncherVersion", version.minimumLauncherVersion);
		});

		System.out.println(fields);
	}

	private static void checkSplitArguments(UnusedFields fields, SplitArguments args) {
		args.forEach(arg -> {
			fields.check("arg.value", arg.values);
			fields.check("arg.rules", arg.rules);
			arg.rules.forEach(rule -> checkRule(fields, rule));
		});
	}

	private static void checkDownloads(UnusedFields fields, Downloads downloads) {
		fields.check("downloads.server", downloads.server);
		fields.check("downloads.clientMappings", downloads.clientMappings);
		fields.check("downloads.serverMappings", downloads.serverMappings);
		fields.check("downloads.windowsServer", downloads.windowsServer);
	}

	private static void checkLibrary(UnusedFields fields, Library library) {
		fields.check("library.artifact", library.artifact);
		fields.check("library.natives", library.natives);
		library.rules.forEach(rule -> checkRule(fields, rule));
		library.natives.ifPresent(natives -> checkNatives(fields, natives));
	}

	private static void checkNatives(UnusedFields fields, Natives natives) {
		fields.check("natives.windows", natives.windows);
		natives.windows.ifPresent(windows -> fields.check("natives.windows", windows));
		fields.check("natives.linux", natives.linux);
		fields.check("natives.macos", natives.macos);
	}

	private static void checkRule(UnusedFields fields, Rule rule) {
		fields.check("rule.features", rule.features);
		fields.check("rule.os", rule.os);
		rule.os.ifPresent(os -> checkOs(fields, os));
	}

	private static void checkOs(UnusedFields fields, OperatingSystemPredicate os) {
		fields.check("os.name", os.name);
		fields.check("os.arch", os.arch);
		fields.check("os.version", os.version);
	}

	private static void checkAssetIndex(UnusedFields fields, FullAssetIndex index) {
		fields.check("assetIndex.virtual", index.virtual);
		fields.check("assetIndex.mapToResources", index.mapToResources);
	}

	private static class UnusedFields {
		private final Set<String> knownFields = new LinkedHashSet<>();
		private final Set<String> presentFields = new HashSet<>();

		public void check(String path, boolean present) {
			this.knownFields.add(path);
			if (present) {
				this.presentFields.add(path);
			}
		}

		public void check(String path, Collection<?> collection) {
			this.check(path, !collection.isEmpty());
		}

		public void check(String path, Optional<?> optional) {
			this.check(path, optional.isPresent());
		}

		public void check(String path, Either<?, ?> either) {
			this.check(path + ".left", either.isLeft());
			this.check(path + ".right", either.isRight());
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder("Never used fields: ");
			int added = 0;
			for (String name : this.knownFields) {
				if (!this.presentFields.contains(name)) {
					if (added != 0) {
						builder.append(", ");
					}
					builder.append(name);
					added++;
				}
			}
			if (added == 0) {
				builder.append("none");
			}
			return builder.toString();
		}
	}
}
