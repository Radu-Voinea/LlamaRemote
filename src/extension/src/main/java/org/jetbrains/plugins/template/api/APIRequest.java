package org.jetbrains.plugins.template.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.template.LLamaRemote;
import org.jetbrains.plugins.template.Registry;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APIRequest<RequestBody, ResponseBody> {

	private final HttpRequest httpRequest;
	private final Class<ResponseBody> responseClass;

	public APIRequest(@NotNull String path, @NotNull String method, @Nullable RequestBody requestBody, @NotNull Class<ResponseBody> responseClass) {
		HttpRequest.Builder builder = HttpRequest.newBuilder()
				.uri(Registry.createURI(path))
				.header("Content-Type", "application/json");

		if (Registry.token != null) {
			builder = builder.header("Authorization", "Bearer " + Registry.token);
		}

		if (requestBody != null) {
			builder = builder.method(method, HttpRequest.BodyPublishers.ofString(LLamaRemote.instance().getGson().toJson(requestBody)));
		} else {
			builder = builder.method(method, HttpRequest.BodyPublishers.noBody());
		}

		this.httpRequest = builder.build();
		this.responseClass = responseClass;
	}

	public ResponseBody getResponse() {
		try (HttpClient httpClient = HttpClient.newHttpClient()) {
			HttpResponse<String> httpResponse = httpClient.send(this.httpRequest, HttpResponse.BodyHandlers.ofString());
			System.out.println(httpResponse);
			return LLamaRemote.instance().getGson().fromJson(httpResponse.body(), this.responseClass);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return null;
	}

}
