package fish.cichlidmc.pistonmetaparser.rule;

import fish.cichlidmc.tinyjson.JsonException;
import fish.cichlidmc.tinyjson.value.JsonValue;

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
