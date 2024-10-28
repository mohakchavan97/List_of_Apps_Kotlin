package com.mohakchavan.java_kotlin_lib;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Generate {

    public static void main(String[] args) {
        System.out.println("Hello World");
//        System.out.println("args[0]:"+System.getenv("JAVA_HOME"));
        try {

            String text = "This build was generated for pull request %url% which was created by %createdBy% with the title as %title%" +
                    ", body as %body% and was merged to %baseRef% on %mergedAt% by %mergedBy%.";
            String commitText = "\n\n" +
                    "=================================================\n\n" +
                    "This build includes below commits:\n";

            URL url = new URL("https://api.github.com/repos/grubbrr/Kiosk-General/pulls/2031");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Be");
            int code = connection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                final String builder = getStringFromStream(connection.getInputStream());
//                System.out.println("response:" + builder);

                JSONObject jsonObject = new JSONObject(builder);
                JSONObject object = null;

                if (!isStringEmpty(jsonObject.optString("url"))) {
                    text = text.replace("%url%", "\"" + jsonObject.optString("url") + "\"");
                }
                object = null;
                if ((object = jsonObject.optJSONObject("user")) != null && !isStringEmpty(object.optString("login"))) {
                    text = text.replace("%createdBy%", "\"" + object.optString("login") + "\"");
                }
                if (!isStringEmpty(jsonObject.optString("title"))) {
                    String replace = "\n--------------------------------------------------\n" +
                            jsonObject.optString("title") +
                            "\n--------------------------------------------------\n\n";
                    text = text.replace("%title%", replace);
                }
                if (!isStringEmpty(jsonObject.optString("body"))) {
                    String replace = "\n--------------------------------------------------\n" +
                            jsonObject.optString("body") +
                            "\n--------------------------------------------------\n\n";
                    text = text.replace("%body%", replace);
                }
                object = null;
                if ((object = jsonObject.optJSONObject("base")) != null && !isStringEmpty(object.optString("ref"))) {
                    text = text.replace("%baseRef%", "\"" + object.optString("ref") + "\"");
                }
                if (!isStringEmpty(jsonObject.optString("merged_at"))) {
                    text = text.replace("%mergedAt%", "\"" + jsonObject.optString("merged_at") + "\"");
                }
                object = null;
                if ((object = jsonObject.optJSONObject("merged_by")) != null && !isStringEmpty(object.optString("login"))) {
                    text = text.replace("%mergedBy%", "\"" + object.optString("login") + "\"");
                }

//                System.out.println("" + text);
            } else {
                final String builder = getStringFromStream(connection.getErrorStream());
                System.out.println("API Failed with error:" + builder);
            }
            connection.disconnect();


            URL url1 = new URL("https://api.github.com/repos/grubbrr/Kiosk-General/pulls/2031/commits");

            HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
            connection1.setRequestMethod("GET");
            connection1.setRequestProperty("Authorization", "Be");
            int code1 = connection1.getResponseCode();
            if (code1 == HttpURLConnection.HTTP_OK) {
                final String builder = getStringFromStream(connection1.getInputStream());
//                System.out.println("\n\n\nbuilder = " + builder);
                JSONArray commits = new JSONArray(builder);

                if (commits != null && !commits.isEmpty()) {
                    JSONObject object = null;
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < commits.length(); i++) {
                        if ((object = commits.optJSONObject(i)) != null && (object = object.optJSONObject("commit")) != null) {
//                            System.out.println("object.optJSONObject(\"message\") = " + object.optString("message"));

                            if (!isStringEmpty(object.optString("message"))) {
                                stringBuilder.append(object.optString("message") + "\n");
                                stringBuilder.append("--------------------------------------------------\n");
                            }
                        }
                    }
                    commitText += stringBuilder.toString();
//                    System.out.println("" + commitText);
                }

            } else {
                final String builder = getStringFromStream(connection1.getErrorStream());
                System.out.println("API Failed with error:" + builder);
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter("Release Notes.txt"));
            bw.append(text);
            bw.append(commitText);
            bw.close();

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    private static boolean isStringEmpty(String string) {
        return string == null || string.isEmpty();
    }

    private static String getStringFromStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder builder = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

}
