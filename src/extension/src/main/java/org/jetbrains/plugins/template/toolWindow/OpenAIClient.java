package org.jetbrains.plugins.template.toolWindow;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;

public class OpenAIClient {

    private static final String API_KEY = "key";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static String getChatGPTResponse(String userMessage) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setDoOutput(true);

            String jsonInputString = "{"
                    + "\"model\": \"gpt-3.5-turbo\","
                    + "\"messages\": ["
                    + "    {\"role\": \"system\", \"content\": \"You are an engine system assistant. You get prompts from users and you return one liners, never multiple lines, with what the user has requested. You do not return JSONs; answer " +
                    "with the command necessary to find out what the user has asked. do it for linux\"},"
                    + "    {\"role\": \"user\", \"content\": \"" + escapeJson(userMessage) + "\"}"
                    + "],"
                    + "\"max_tokens\": 150"
                    + "}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = connection.getResponseCode();
            BufferedReader br;
            if (code == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"));
            }

            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            br.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray choices = jsonResponse.getJSONArray("choices");
            if (choices.length() > 0) {
                JSONObject message = choices.getJSONObject(0).getJSONObject("message");
                String answer = message.getString("content").trim();
                // Dacă răspunsul conține mai multe linii, ia doar prima linie
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
