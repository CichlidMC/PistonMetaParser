package io.github.cichlidmc.pistonmetaparser.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import io.github.cichlidmc.tinyjson.JsonException;
import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonArray;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public class JsonUtils {
	public static URI parseUri(JsonValue value) {
		String string = value.asString().value();
		try {
			return new URI(string);
		} catch (URISyntaxException e) {
			throw new JsonException(value, "Invalid URI: " + string);
		}
	}

	public static Date parseIsoDate(JsonValue value) {
		String string = value.asString().value();
		try {
			return Date.from(Instant.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(string)));
		} catch (DateTimeException e) {
			throw new JsonException(value, "Invalid ISO date: " + string);
		}
	}

	public static Pattern parseRegex(JsonValue value) {
		String string = value.asString().value();
		try {
			return Pattern.compile(string);
		} catch (PatternSyntaxException e) {
			throw new JsonException(value, "Invalid regex: " + string);
		}
	}

	public static int parseInt(JsonValue value) {
		return value.asNumber().strictValue().intValue();
	}

	public static <T> List<T> listOrSingle(JsonValue value, Function<JsonValue, T> function) {
		if (value instanceof JsonArray) {
			return value.asArray().stream().map(function).collect(Collectors.toList());
		} else {
			return Collections.singletonList(function.apply(value));
		}
	}

	public static <T> Either<ParseResult<T>, JsonException> tryParse(JsonObject json, String key, Function<JsonValue, T> function) {
		try {
			JsonValue value = json.get(key);
			T parsed = function.apply(value);
			return Either.left(new ParseResult<>(value, parsed));
		} catch (JsonException e) {
			return Either.right(e);
		}
	}

	public static class ParseResult<T> {
		public final JsonValue value;
		public final T parsed;

		public ParseResult(JsonValue value, T parsed) {
			this.value = value;
			this.parsed = parsed;
		}
	}
}
