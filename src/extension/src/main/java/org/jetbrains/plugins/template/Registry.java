package org.jetbrains.plugins.template;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Registry {

	public static String host = "http://localhost:8080";
	public static String token = "BnnZUiM9QIeuNebS9GoE8Wy4DyQdbij3V76ipsYZZGdGjEXO8bvnpMuXoStaLTaC";

	public static URI createURI(String path) {
		return URI.create(host + path);
	}


}
