package org.jetbrains.plugins.template;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Registry {

	public static String host = "http://localhost:8080";
	public static String token = "OhPADhtsVuzD3jNCfTaPaJ7woDP3T82oZ1ryAk6JT8FTYTHQSvjXAiixim0CnuFU";

	public static URI createURI(String path) {
		return URI.create(host + path);
	}


}
