package org.jetbrains.plugins.template.api;

import com.raduvoinea.utils.message_builder.MessageBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;


public class OpenAIAPI {

	private static final String API_KEY = System.getenv("OPENAIAPI_KEY");
	private static final String API_URL = "https://api.openai.com/v1/chat/completions";


	public static String getChatGPTResponse(String userMessage) {
		try {
			HttpClient client = HttpClient.newHttpClient();

			String systemPrompt = "You are an system engineer assistant. You get prompts from users and you return one " +
					"liners, never multiple lines, with what the user has requested. You do not return JSONs or " +
					"MarkDown; answer with a ONELINER command necessary to find out what the user has asked. do it for linux." +
					"Do not wrap your answer in any other text. Do not add any other text. Do not add any other characters. ";

			String jsonString = new MessageBuilder("""
					{
					    "model": "gpt-4o",
					    "messages": [
					        {
					            "role": "system",
					            "content": "{systemPrompt}"
					        },
					        {
					            "role": "user",
					            "content": "{content}"
					        }
					    ],
					    "max_tokens": 150
					}
					""")
					.parse("content", escapeJson(userMessage))
					.parse("systemPrompt", systemPrompt)
					.parse();

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(API_URL))
					.header("Content-Type", "application/json")
					.header("Authorization", "Bearer " + API_KEY)
					.POST(HttpRequest.BodyPublishers.ofString(jsonString, StandardCharsets.UTF_8))
					.build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			String body = response.body();
			JSONObject jsonResponse = new JSONObject(body);
			JSONArray choices = jsonResponse.getJSONArray("choices");
			if (choices.length() > 0) {
				JSONObject message = choices.getJSONObject(0).getJSONObject("message");
				String answer = message.getString("content").trim();
				int newlineIndex = answer.indexOf('\n');
				if (newlineIndex != -1) {
					answer = answer.substring(0, newlineIndex);
				}
				return answer;
			} else {
				return "No answer received";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	private static String escapeJson(String str) {
		return str.replace("\"", "\\\"");
	}

	public static void main(String[] args) {
		String[] userQueries = {
				"find all files ending in _old in the current directory recursively",
				"show me how much memory i have left",
				"show me what docker containers i have running"
		};

		for (String query : userQueries) {
			System.out.println("User query: " + query);
			String response = getChatGPTResponse(query);
			System.out.println("ChatGPT Response: " + response);
			System.out.println("------------------------------------------------------");
		}
	}
}
