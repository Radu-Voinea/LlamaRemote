package org.jetbrains.plugins.template;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Registry {

	public static String host = "http://localhost:8080";
	public static String token = "dIzyt4YT5Tlv9U16D13qn0JeQcEtk2eDXBuEE6xvOHQLGHSotie7xQg0DOBKanMuS51p0wQ3AO7lC4zFb7ELGRUpXb1wlndmCzlkqDogt0yt6HPvFBG9twGVaJeBvWhv";

	public static URI createURI(String path) {
		return URI.create(host + path);
	}


}
