package io.github.cichlidmc.pistonmetaparser.version.args;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.github.cichlidmc.pistonmetaparser.rule.Rule;
import io.github.cichlidmc.pistonmetaparser.util.JsonUtils;
import io.github.cichlidmc.tinyjson.JsonException;
import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;
import io.github.cichlidmc.tinyjson.value.primitive.JsonString;

public class Argument {
	public final List<Rule> rules;
	public final List<String> values;

	public Argument(List<Rule> rules, List<String> values) {
		this.rules = rules;
		this.values = values;
	}

	public static Argument parse(JsonValue value) {
		if (value instanceof JsonString) {
			String string = ((JsonString) value).value();
			return new Argument(Collections.emptyList(), Collections.singletonList(string));
		} else if (value instanceof JsonObject) {
			JsonObject json = value.asObject();
			List<Rule> rules = json.get("rules").asArray().stream()
					.map(Rule::parse)
					.collect(Collectors.toList());
			List<String> values = JsonUtils.listOrSingle(json.get("value"), v -> v.asString().value());
			return new Argument(rules, values);
		} else {
			throw new JsonException(value, "Argument is not a string or object");
		}
	}
}
