package io.github.cichlidmc.pistonmetaparser.rule;

import java.util.List;
import java.util.Optional;

import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class Rule {
	public final RuleAction action;
	public final Optional<Features> features;
	public final Optional<OperatingSystemPredicate> os;

	public Rule(RuleAction action, Optional<Features> features, Optional<OperatingSystemPredicate> os) {
		this.action = action;
		this.features = features;
		this.os = os;
	}

	public boolean test(Features features) {
		return this.matches(features) ? this.action == RuleAction.ALLOW : this.action == RuleAction.DISALLOW;
	}

	private boolean matches(Features features) {
		if (this.features.isPresent() && !this.features.get().matches(features))
			return false;
		return !this.os.isPresent() || this.os.get().matches();
	}

	public static boolean test(List<Rule> rules, Features features) {
		for (Rule rule : rules) {
			if (!rule.test(features)) {
				return false;
			}
		}
		return true;
	}

	public static Rule parse(JsonValue value) {
		JsonObject json = value.asObject();

		RuleAction action = RuleAction.parse(json.get("action"));
		Optional<Features> features = json.getOptional("features").map(Features::parse);
		Optional<OperatingSystemPredicate> os = json.getOptional("os").map(OperatingSystemPredicate::parse);

		return new Rule(action, features, os);
	}
}
