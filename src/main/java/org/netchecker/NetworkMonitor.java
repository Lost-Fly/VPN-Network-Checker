package org.netchecker;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.netchecker.Config.*;

public class NetworkMonitor {

    private static final List<String> foundDevices = new ArrayList<>();
    private static final List<String> notFoundDevices = new ArrayList<>();

    private static final HostsStorage hostsStorage = new HostsStorage();


    public static void main(String[] args) throws IOException {
        int startIp = getSubnetStartIp();
        int endIp = getSubnetEndIp();

        List<CompletableFuture<Void>> initialScanFutures = new ArrayList<>();
        for (int i = startIp; i <= endIp; i++) {
            String ipAddress = getIpAddress(i);
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    boolean isReachable = retryPing(InetAddress.getByName(ipAddress));
                    if (isReachable) {
                        System.out.println(DEVICE + ipAddress + AVAILABLE);
                        foundDevices.add(ipAddress);
                    } else {
                        System.out.println(DEVICE + ipAddress + UNAVAILABLE);
                        notFoundDevices.add(ipAddress);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            initialScanFutures.add(future);
        }
        // Wait for initial scan to complete
        CompletableFuture<Void> initialScanFuture = CompletableFuture.allOf(initialScanFutures.toArray(new CompletableFuture[0]));
        initialScanFuture.join();

        // Send messages to Telegram
        TelegramApi.sendTgMessage(Config.NOT_FOUND_DEVICES_MESSAGE + String.join("\n\n", notFoundDevices));
        TelegramApi.sendTgMessage(Config.FOUND_DEVICES_MESSAGE + String.join("\n\n", foundDevices));

        // Schedule periodic checks for discovered devices
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            for (String ipAddress : foundDevices) {
                if (hostsStorage.getWhiteList().containsKey(ipAddress)) {
                    String deviceName = hostsStorage.getWhiteList().get(ipAddress);
                    try {
                        boolean isReachable = retryPing(InetAddress.getByName(ipAddress));
                        if (!isReachable) {
                            System.out.printf((Config.UNAVAILABLE_DEVICE_MESSAGE) + "%n", deviceName, ipAddress);
                            TelegramApi.sendTgMessage(String.format(Config.UNAVAILABLE_DEVICE_MESSAGE, deviceName, ipAddress));
                        } else {
                            System.out.printf((Config.AVAILABLE_DEVICE_MESSAGE) + "%n", deviceName, ipAddress);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, Config.INITIAL_DELAY, Config.PERIOD, TimeUnit.MINUTES);
    }


    private static boolean retryPing(InetAddress address) {
        for (int attempt = 1; attempt <= Config.MAX_RETRY_ATTEMPTS; attempt++) {
            try {
                if (address.isReachable(Config.TIMEOUT)) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
//            System.out.printf(ATTEMPT_FAILED, attempt);
        }
        return false;
    }


    private static int getSubnetStartIp() {
        String[] subnetOctets = Config.NET_IP_ADDRESS.split("\\.");
        String[] maskOctets = Config.NET_MASK.split("\\.");

        int startIp = 0;
        for (int i = 0; i < 4; i++) {
            int subnetOctet = Integer.parseInt(subnetOctets[i]);
            int maskOctet = Integer.parseInt(maskOctets[i]);
            startIp |= (subnetOctet & maskOctet) << (24 - 8 * i);
        }

        return startIp;
    }


    private static String getIpAddress(int i) throws UnknownHostException {
        byte[] subnetBytes = InetAddress.getByName(Config.NET_IP_ADDRESS).getAddress();
        byte[] ipBytes = subnetBytes.clone();
        ipBytes[3] = (byte) i;

        try {
            return InetAddress.getByAddress(ipBytes).getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(IP_CREATION_ERROR, e);
        }
    }

    private static int getSubnetEndIp() throws UnknownHostException {
        byte[] subnetBytes = InetAddress.getByName(Config.NET_IP_ADDRESS).getAddress();
        byte[] maskBytes = InetAddress.getByName(Config.NET_MASK).getAddress();

        byte[] broadcastBytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            broadcastBytes[i] = (byte) (subnetBytes[i] | ~maskBytes[i]);
        }

        int endIp = InetAddress.getByAddress(broadcastBytes).hashCode();
        return endIp - 1;
    }

}
