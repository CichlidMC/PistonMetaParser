package io.github.cichlidmc.pistonmetaparser.rule;

import io.github.cichlidmc.tinyjson.JsonException;
import io.github.cichlidmc.tinyjson.value.JsonValue;

public enum RuleAction {
	ALLOW, DISALLOW;

	public static RuleAction parse(JsonValue value) {
		String string = value.asString().value();
		if (string.equals("allow")) {
			return ALLOW;
		} else if (string.equals("disallow")) {
			return DISALLOW;
		} else {
			throw new JsonException(value, "Invalid RuleAction: " + string);
		}
	}
}
