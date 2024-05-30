package org.netchecker;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HostsService {
    private final Map<String, String> whiteList = new HashMap<>();

    public HostsService() {
        Properties props = Config.getProperties();
        for (String key : props.stringPropertyNames()) {
            if (key.startsWith("device.")) {
                String ip = key.substring(7);
                String name = props.getProperty(key);
                this.whiteList.put(ip, name);
            }
        }
    }

    public Map<String, String> getWhiteList() {
        return whiteList;
    }
}
