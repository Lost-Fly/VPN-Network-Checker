package org.netchecker;

public class Config {

    // Check settings
    public static final int TIMEOUT = 9000;
    public static final int INITIAL_DELAY = 1;
    public static final int PERIOD = 1;
    public static final int MAX_RETRY_ATTEMPTS = 5;

    // Network configuration
    public static final String NET_IP_ADDRESS = "192.168.0.0";
    public static final String NET_MASK = "255.255.255.0";

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

}
