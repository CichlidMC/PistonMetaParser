package io.github.cichlidmc.pistonmetaparser.util;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import io.github.cichlidmc.tinyjson.JsonException;
import io.github.cichlidmc.tinyjson.value.JsonValue;
import io.github.cichlidmc.tinyjson.value.composite.JsonObject;

public abstract class Either<A, B> {
	private Either() {}

	public static <A, B> Either<A, B> left(A value) {
		return new Left<>(value);
	}

	public static <A, B> Either<A, B> right(B value) {
		return new Right<>(value);
	}

	public A left() {
		throw new NoSuchElementException();
	}

	public B right() {
		throw new NoSuchElementException();
	}

	public boolean isLeft() {
		return false;
	}

	public boolean isRight() {
		return false;
	}

	public final void ifLeft(Consumer<A> consumer) {
		if (this.isLeft()) {
			consumer.accept(this.left());
		}
	}

	public final void ifRight(Consumer<B> consumer) {
		if (this.isRight()) {
			consumer.accept(this.right());
		}
	}

	public final Optional<A> maybeLeft() {
		return this.isLeft() ? Optional.ofNullable(this.left()) : Optional.empty();
	}

	public final Optional<B> maybeRight() {
		return this.isRight() ? Optional.ofNullable(this.right()) : Optional.empty();
	}

	public final <C, D> Either<C, D> map(Function<A, C> leftFunction, Function<B, D> rightFunction) {
		if (this.isLeft()) {
			return left(leftFunction.apply(this.left()));
		} else {
			return right(rightFunction.apply(this.right()));
		}
	}

	public static <A, B> Either<A, B> parseAndMerge(
			JsonObject json,
			String leftKey, Function<JsonValue, A> leftFunction,
			String rightKey, Function<JsonValue, B> rightFunction
	) {
		Either<JsonUtils.ParseResult<A>, JsonException> leftTry = JsonUtils.tryParse(json, leftKey, leftFunction);
		Either<JsonUtils.ParseResult<B>, JsonException> rightTry = JsonUtils.tryParse(json, rightKey, rightFunction);

		if (leftTry.isLeft() && rightTry.isLeft()) {
			String leftPath = leftTry.left().value.getPath();
			String rightPath = rightTry.left().value.getPath();
			throw new JsonException("Both either values parsed successfully (" + leftPath + " and " + rightPath + ')');
		} else if (leftTry.isRight() && rightTry.isRight()) {
			JsonException e = new JsonException(json, "Both either values failed to parse (" + leftKey + " and " + rightKey + ')');
			e.addSuppressed(leftTry.right());
			e.addSuppressed(rightTry.right());
			throw e;
		} else if (leftTry.isLeft()) {
			return left(leftTry.left().parsed);
		} else { // has to be rightTry.isLeft()
			return right(rightTry.left().parsed);
		}
	}



	private static class Left<A, B> extends Either<A, B> {
		private final A value;

		private Left(A value) {
			this.value = value;
		}

		@Override
		public A left() {
			return this.value;
		}

		@Override
		public boolean isLeft() {
			return true;
		}
	}

	private static class Right<A, B> extends Either<A, B> {
		private final B value;

		private Right(B value) {
			this.value = value;
		}

		@Override
		public B right() {
			return this.value;
		}

		@Override
		public boolean isRight() {
			return true;
		}
	}
}
