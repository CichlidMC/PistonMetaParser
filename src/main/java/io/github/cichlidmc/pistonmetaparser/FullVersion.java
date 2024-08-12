package io.github.cichlidmc.pistonmetaparser;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.cichlidmc.pistonmetaparser.util.Either;
import io.github.cichlidmc.pistonmetaparser.util.JsonUtils;
import io.github.cichlidmc.pistonmetaparser.version.JavaVersion;
import io.github.cichlidmc.pistonmetaparser.version.args.SplitArguments;
import io.github.cichlidmc.pistonmetaparser.version.args.StringArguments;
import io.github.cichlidmc.pistonmetaparser.version.assets.AssetIndex;
import io.github.cichlidmc.pistonmetaparser.version.download.Downloads;
import io.github.cichlidmc.pistonmetaparser.version.library.Library;
import io.github.cichlidmc.pistonmetaparser.version.logging.Logging;
import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class FullVersion {
	public final String id;
	public final VersionType type;
	public final String mainClass;

	public final Downloads downloads;

	public final Date releaseTime;
	public final Date time;

	public final String assets;
	public final AssetIndex assetIndex;

	public final Either<SplitArguments, StringArguments> arguments;
	public final Optional<Integer> complianceLevel;
	public final Optional<JavaVersion> javaVersion;
	public final List<Library> libraries;
	public final Optional<Logging> logging;
	public final Optional<Integer> minimumLauncherVersion;

	public FullVersion(String id, VersionType type, String mainClass, Downloads downloads, Date releaseTime, Date time, String assets,
					   AssetIndex assetIndex, Either<SplitArguments, StringArguments> arguments, Optional<Integer> complianceLevel,
					   Optional<JavaVersion> javaVersion, List<Library> libraries, Optional<Logging> logging, Optional<Integer> minimumLauncherVersion) {
		this.id = id;
		this.type = type;
		this.mainClass = mainClass;
		this.downloads = downloads;
		this.releaseTime = releaseTime;
		this.time = time;
		this.assets = assets;
		this.assetIndex = assetIndex;
		this.arguments = arguments;
		this.complianceLevel = complianceLevel;
		this.javaVersion = javaVersion;
		this.libraries = libraries;
		this.logging = logging;
		this.minimumLauncherVersion = minimumLauncherVersion;
	}

	public static FullVersion parse(JsonValue value) {
		JsonObject json = value.asObject();

		String id = json.get("id").asString().value();
		VersionType type = VersionType.parse(json.get("type"));
		String mainClass = json.get("mainClass").asString().value();

		Downloads downloads = Downloads.parse(json.get("downloads"));

		Date releaseTime = JsonUtils.parseIsoDate(json.get("releaseTime"));
		Date time = JsonUtils.parseIsoDate(json.get("time"));

		String assets = json.get("assets").asString().value();
		AssetIndex assetIndex = AssetIndex.parse(json.get("assetIndex"));

		Either<SplitArguments, StringArguments> arguments = Either.parseAndMerge(
				json,
				"arguments", SplitArguments::parse,
				"minecraftArguments", StringArguments::parse
		);

		Optional<Integer> complianceLevel = json.getOptional("complianceLevel").map(JsonUtils::parseInt);
		Optional<JavaVersion> javaVersion = json.getOptional("javaVersion").map(JavaVersion::parse);
		List<Library> libraries = json.get("libraries").asArray().stream()
				.map(Library::parse)
				.collect(Collectors.toList());
		Optional<Logging> logging = json.getOptional("logging").map(Logging::parse);
		Optional<Integer> minimumLauncherVersion = json.getOptional("minimumLauncherVersion").map(JsonUtils::parseInt);

		return new FullVersion(
				id, type, mainClass, downloads, releaseTime, time, assets, assetIndex,
				arguments, complianceLevel, javaVersion, libraries, logging, minimumLauncherVersion
		);
	}
}
