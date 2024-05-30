package org.netchecker;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            }
            // Load a properties file from class path, inside static block
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Check settings
    public static final int TIMEOUT = Integer.parseInt(properties.getProperty("timeout", "9000"));
    public static final int INITIAL_DELAY = Integer.parseInt(properties.getProperty("initial_delay", "1"));
    public static final int PERIOD = Integer.parseInt(properties.getProperty("period", "1"));
    public static final int MAX_RETRY_ATTEMPTS = Integer.parseInt(properties.getProperty("max_retry_attempts", "5"));

    // Network configuration
    public static final String NET_IP_ADDRESS = properties.getProperty("net_ip_address", "192.168.0.0");
    public static final String NET_MASK = properties.getProperty("net_mask", "255.255.255.0");

    // Messages constants
    public static final String NOT_FOUND_DEVICES_MESSAGE = "Не найденные устройства: ";
    public static final String FOUND_DEVICES_MESSAGE = "Найденные устройства: ";
    public static final String UNAVAILABLE_DEVICE_MESSAGE = "Устройство %s (%s) НЕ доступно!";
    public static final String AVAILABLE_DEVICE_MESSAGE = "Устройство %s (%s) доступно";
    public static final String DEVICE = "Устройство ";
    public static final String AVAILABLE = " доступно";
    public static final String UNAVAILABLE = " НЕ доступно!";
    public static final String IP_CREATION_ERROR = "Не удалось сформировать IP-адрес";
    public static final String ATTEMPT_FAILED = "Попытка %s не удалась. Повторная попытка...";

    public static Properties getProperties() {
        return properties;
    }
}
