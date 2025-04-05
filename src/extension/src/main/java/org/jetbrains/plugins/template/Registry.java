package org.jetbrains.plugins.template;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Registry {

	public static String host = "http://localhost:8080";
	public static String token = "3830c08609c2bdd736f077121fb74b10e5dc4d1a0ec531cd9364fdd291757218";

	public static URI createURI(String path) {
		return URI.create(host + path);
	}


}
