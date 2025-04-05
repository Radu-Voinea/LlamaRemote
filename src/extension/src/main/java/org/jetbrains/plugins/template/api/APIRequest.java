package org.jetbrains.plugins.template.api;

import org.jetbrains.plugins.template.MyBundle;
import org.jetbrains.plugins.template.Registry;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APIRequest<RequestBody, ResponseBody> {

	private final HttpRequest httpRequest;
	private final Class<ResponseBody> responseClass;

	public APIRequest(String path, RequestBody requestBody, Class<ResponseBody> responseClass) {
		this.httpRequest = HttpRequest.newBuilder()
				.uri(Registry.createURI(path))
				.header("Content-Type", "application/json")
				.method("GET", HttpRequest.BodyPublishers.ofString(MyBundle.instance().getGson().toJson(requestBody)))
				.build();

		this.responseClass = responseClass;
	}

	public ResponseBody getResponse() {
		try (HttpClient httpClient = HttpClient.newHttpClient()) {
			HttpResponse<String> httpResponse = httpClient.send(this.httpRequest, HttpResponse.BodyHandlers.ofString());
			return MyBundle.instance().getGson().fromJson(httpResponse.body(), this.responseClass);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return null;
	}

}
