package org.jetbrains.plugins.template;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Registry {

	public static String host = "http://localhost:8080";
	public static String token = "zoK5mk7DGw44FQO4mW93UWD54DRmhE67OdCu8ofz84zoA3zBMugZOxMehtvGCXrX";

	public static URI createURI(String path) {
		return URI.create(host + path);
	}


}
