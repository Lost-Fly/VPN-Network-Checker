# VPN-Network-Checker: VPN Device Monitoring with Telegram Alerts

### Description:

This application monitors device availability within a VPN network using ICMP (ping) requests and sends notifications to a designated Telegram chat. 
It's designed to help maintain device uptime and promptly alert you to any disconnections or outages.

### Key Features:

- Network Scanning: Discovers accessible devices within a specified IP address range.
- Periodic Monitoring: Regularly checks the availability of found devices.
- Telegram Notifications: Pushes real-time alerts for device availability and unavailability.
- WhiteList: Filters monitoring to a defined set of critical devices for focused attention.
- Customizable Settings: Allows tailoring the application's behavior to specific needs updating the config.properties file

### Configurable Parameters (Config class):

- TIMEOUT: Wait time for ICMP response in milliseconds.
- INITIAL_DELAY: Initial delay before starting periodic checks in minutes.
- PERIOD: Interval between device checks in minutes.
- MAX_RETRY_ATTEMPTS: Maximum retries for device checks upon failure.
- NET_IP_ADDRESS: Base IP address of the subnet to scan.
- NET_MASK: Subnet mask for calculating the IP address range.
- TELEGRAM_BOT_TOKEN: Token of your Telegram bot (set in application.properties).
- TELEGRAM_CHAT_ID: ID of the Telegram chat to receive notifications (set in application.properties).

## Setup and Usage:

1) Configure Settings:
- Modify values in the config.properties as needed.
- Add your Telegram bot token and chat ID to application.properties.
2)Compile JAR File:
- Open a terminal in the project's root directory.
- Run: ```javac -d out/artifacts/VPNnetworkChecker src/org/netchecker/*.java```
3) Set Up Service (Linux):
- Edit VPNnetworkCheck.service in out/artifacts:
  - Provide correct path to JAR file.
  - Adjust other startup parameters if needed.
- Copy the service file to /etc/systemd/system.
- Run: ```systemctl daemon-reload``` and ```systemctl start VPNnetworkCheck.service```
4) Run on Windows:
- Open a terminal in the folder containing the JAR file.
- Run: ```java -jar VPNnetworkChecker.jar```

### Important! Compiled jar file and config.properties have to be placed in the same directory!

5) Connect to VPN:
- Ensure the server or machine is connected to your organization's VPN.
### Initiate Monitoring:
- Once launched, the application will initiate device scanning, periodic checks, and Telegram notifications.
### Alerts example:
![image](https://github.com/Lost-Fly/VPN-Network-Checker/assets/114507453/58c7713b-1179-4359-a669-c055908dc891)


