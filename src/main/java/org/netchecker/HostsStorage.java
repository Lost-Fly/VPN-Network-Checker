package org.netchecker;

import java.util.HashMap;
import java.util.Map;

public class HostsStorage {
    private final Map<String, String> whiteList = new HashMap<>();

    public HostsStorage() {
        this.whiteList.put("192.168.0.100", "Main server");
        this.whiteList.put("192.168.0.10", "SQL server");
        this.whiteList.put("192.168.0.11", "File-storage server");
        // ... add your IP addresses and Hostnames
    }

    public Map<String, String> getWhiteList() {
        return whiteList;
    }
}
