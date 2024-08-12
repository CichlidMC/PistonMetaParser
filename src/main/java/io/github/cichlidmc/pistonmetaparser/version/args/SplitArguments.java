package io.github.cichlidmc.pistonmetaparser.version.args;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class SplitArguments {
	public final List<Argument> game;
	public final List<Argument> jvm;

	public SplitArguments(List<Argument> game, List<Argument> jvm) {
		this.game = game;
		this.jvm = jvm;
	}

	public void forEach(Consumer<Argument> consumer) {
		this.game.forEach(consumer);
		this.jvm.forEach(consumer);
	}

	public static SplitArguments parse(JsonValue value) {
		JsonObject json = value.asObject();
		List<Argument> game = argumentList(json, "game");
		List<Argument> jvm = argumentList(json, "jvm");
		return new SplitArguments(game, jvm);
	}

	private static List<Argument> argumentList(JsonObject json, String key) {
		return json.get(key).asArray().stream().map(Argument::parse).collect(Collectors.toList());
	}
}
