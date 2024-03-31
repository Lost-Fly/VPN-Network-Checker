package org.netchecker;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.Properties;

public class TelegramApi {

    private static final String URL_TEMPLATE = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
    private static final String PROPERTIES_FILE = "/application.properties";
    private static final String TG_RESPONSE = "Ответ от Telegram API: ";

    public static void sendTgMessage(String message) throws IOException {
        Properties props = new Properties();
        try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(TelegramApi.class.getResourceAsStream(PROPERTIES_FILE)))) {
            props.load(reader);
        }
        // Add your TG Bot properties
        String telegramBotToken = props.getProperty("telegram.bot.token");
        String telegramChatId = props.getProperty("telegram.chat.id");
        String encodedMessage = URLEncoder.encode(message, "UTF-8");
        String urlString = String.format(URL_TEMPLATE, telegramBotToken, telegramChatId, encodedMessage);

        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);

        try (
                PrintWriter out = new PrintWriter(connection.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        ) {
            out.print(encodedMessage);
            out.flush();

            String responseLine;
            StringBuilder response = new StringBuilder();
            while ((responseLine = in.readLine()) != null) {
                response.append(responseLine);
            }

            System.out.println(TG_RESPONSE + response.toString());
        }
    }
}

