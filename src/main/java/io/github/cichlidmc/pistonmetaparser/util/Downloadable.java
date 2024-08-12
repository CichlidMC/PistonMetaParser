package io.github.cichlidmc.pistonmetaparser.util;

import java.net.URI;

public interface Downloadable {
	URI url();
	int size();
	String sha1();
}
