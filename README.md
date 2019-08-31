# NeoBot - Selenium Neopets Dailies and More Bot Automation Suite
A less-detectable, easy-to-use Neopets bot.  Play while you sleep, while at school, while at work, anywhere.


Over 12 months of:
* Constant use
* Improvements
* Updates
* Millions of Neopoints farmed

Features:
* Performs all your standard dailies
* Manages your shop
* Tracks your financial gains
* Trains your pets
* Text and email notifications
* Does not hack the game
* Does not change Neopets files
* Does not edit packets

### Foreward
NeoBot was built, tested, and run on an 8 year old netbook with a single core Via Nano 1.3 GHz processor with 3Gb ram Windows 7 64bit. If it can run on that, you can run it.

### Prerequisites
* A shop of at least size 6
* Minimum 100k NP total, 200k recommended
* A petpet attached to your pets
* To automate the lab ray or forgotten shore, you will need to buy the maps and obtain access
* To automate the battledome, your active pet must have at least two weapons and know Lens Flare


# Setup and Instructions

Create a sub-folder in your Documents folder called Neopets

Download and unzip the package, go into the NeoBot-master folder, and copy and paste all the contents into your Documents folder

Your antivirus may affect sending email. You may need to disable your antivirus, or reconfigure it to permit emails/SMTP (as is the case with Avast).

### Installations and Configurations
| Prereq | Action | Purpose |
| ------ | ------ | ------ |
|[AutoIT3](https://www.autoitscript.com/site/autoit/downloads/) | Download | The main engine that drives the bot|
|[Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) | Download | What the Email functionality uses|
|[Javac](https://learn.yancyparedes.net/2012/03/setting-up-javac-command-on-windows-7/) | Configure | Add javac to your PATH to compile Java scripts|
|[Firefox 41](https://ftp.mozilla.org/pub/firefox/releases/41.0.2/win32/en-US/Firefox%20Setup%2041.0.2.exe) | Download (if you already have a newer version of Firefox, download this into a directory other than the installer's default) | The browser NeoBot uses.

### Update Code Parameters


Open up RunDailies.java and update:
```Java
public static final String YOUR_EMAIL = "CHANGE_ME@gmail.com";
public static final String YOUR_EMAIL_PASSWORD = "CHANGE_ME";
public static final String NUMBER_TO_TEXT = "5551234567@tmomail.net";
public static final String WINDOWS_USER = "CHANGE_ME";
public static final String USERNAME = "CHANGE_ME";
public static final String PASSWORD = "CHANGE_ME";
public static final String PETNAME = "CHANGE_ME";
public static final String PET_ZAPPED = "CHANGE_ME";
public static final String ALTADOR_URL = "CHANGE_ME";
```
Change **YOUR_EMAIL** to your gmail address.

Change **YOUR_EMAIL_PASSWORD** to your gmail password.

Change **NUMBER_TO_TEXT** to your phone number and carrier. Valid carriers are in the table below.

Change **WINDOWS_USER** to your Windows username.

Change **USERNAME** to your Neopets account username.

Change **PASSWORD** to your Neopets account password.

Change **PETNAME** to your active Neopets pet name.

Change **PET_ZAPPED** to your lab rat's name.

Change **ALTADOR_URL** to your Altador Council prize URL address ID (at the end of the URL).
&nbsp;
&nbsp;

| Provider | Email to SMS Address Format |
| ------ | ------ |
| AllTel | number@text.wireless.alltel.com |
| AT&T | number@txt.att.net |
| Boost Mobile | number@myboostmobile.com |
| Cricket | number@sms.mycricket.com |
| Sprint | number@messaging.sprintpcs.com |
| T-Mobile | number@tmomail.net |
| US Cellular | number@email.uscc.net |
| Verizon | number@vtext.com |
| Virgin Mobile | number@vmobl.com |
| Bell | number@txt.bell.ca |
| Eastlink | number@txt.eastlink.ca |
| Fido | number@fido.ca |
| Koodo Mobile | number@msg.koodomobile.com |
| MTS | number@text.mtsmobility.com |
| PC Mobile | number@mobiletxt.ca |
| Public Mobile | number@msg.telus.com |
| Rogers | number@pcs.rogers.com |
| Sasktel | number@sms.sasktel.com |
| Solo Mobile | number@txt.bell.ca |
| TBayTel | number@pcs.rogers.com |
| TELUS | number@msg.telus.com |
| Virgin Mobile | number@vmobile.ca |
| WIND Mobile | number@txt.windmobile.ca |


### Windows Configurations

Open up SetupWindows.bat and update:
```
AutoIt3.exe C:\Users\TheLazyGamer\Documents\Neopets\SetupWindows.au3
```
Change **TheLazyGamer** to your Windows username and save.
&nbsp;
&nbsp;

Open up RunDailies.bat and update:
```
cd C:\Users\TheLazyGamer\Documents\Neopets
```
Change **TheLazyGamer** to your Windows username and save.
&nbsp;
&nbsp;
&nbsp;
&nbsp;

Open up a Run window (Windows+r) and do:
1. firefox.exe -p

2. Click "Create Profile"

3. Click Next.

4. Under "Enter new profile name" put Neo1 and click Finish.

5. Check "Use the selected profile without asking at startup".

6. Click Neo1 and click Start Firefox.

7. Open up [Options](about:preferences).

8. Make sure "Always check if Firefox is your default browser." is unchecked.

9. Click Advanced in the left-hand menu.

10. Click Data Choices and make sure everything is unchecked.

11. Click Update and select "Never check for updates (not recommended: security risk)"

12. If you wish to use a proxy, click Network and to the right of Connection click Settings.

13. Install [uBlock Origin](https://addons.mozilla.org/en-US/firefox/addon/ublock-origin/versions/) Scroll down until you find the compatible version.

14. Close Firefox.

15. Open up RunDailies.java and update:
```Java
FirefoxBinary binary = new FirefoxBinary(new File("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe"));
```
Change **C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe** to your Firefox 41 installation path.
This only needs to be changed if you didn't choose the default installation location

16. Double click RunDailies.bat

&nbsp;
&nbsp;

### Pi Configurations (only use this if you decide to run NeoBot on your Raspberry Pi)

1. Create a bootable USB following the instructions here https://www.raspberrypi.org/documentation/installation/installing-images/

2. run sudo apt-get update and sudo apt-get upgrade

3. run sudo apt-get install chromium-browser (this will force chromium to update to the newest version. Run even if you have Chromium installed.)

4. Download Chromedriver v2.21 for armv7l (confirmed working on Chromium 56) https://github.com/electron/electron/releases/download/v1.6.0/chromedriver-v2.21-linux-armv7l.zip

5. cd to your Downloads folder in terminal

6. run unzip YOUR_DOWNLOAD where YOUR_DOWNLOAD is the downloaded ChromeDriver zip

7. run chmod +x ~/Downloads/chromedriver

8. run sudo mv -f ~/Downloads/chromedriver /usr/local/bin/chromedriver

9. Download Selenium 3.4 for Java here http://selenium-release.storage.googleapis.com/3.4/selenium-java-3.4.0.zip

10. run unzip YOUR_DOWNLOAD where YOUR_DOWNLOAD is the downloaded selenium zip

11. Move client-combined-3.4.0-nodeps into the lib folder inside Downloads which was generated from the previous step

12. Download javax mail from https://github.com/javaee/javamail/releases/download/JAVAMAIL-1_6_0/javax.mail.jar and move the jar into your lib folder inside Downloads

13. Download Apache Commons Email from http://central.maven.org/maven2/org/apache/commons/commons-email/1.5/commons-email-1.5.jar and move the jar into your lib folder inside Downloads

12. Create a folder called Neopets inside Documents

13. Move the lib folder from Downloads into Neopets

14. Create a file in Neopets called RunDailies

15. Paste this into RunDailies:
	#!/bin/bash
	Xvfb :1 -screen 5 1920x1080x24 &
	export DISPLAY=:1.5
	killall chromedriver
	cd /home/pi/Documents/Neopets
	javac -cp .:lib/* NeoDailiesPi.java
	java -cp .:lib/* NeoDailiesPi
	read -p "Press enter to continue"

16. In a terminal cd to Neopets.

17. run chmod 755 RunDailies (this makes it an executable bash file)

18. run sudo apt-get install xvfb

19. In a terminal run: sudo raspi-config

20. Select Localisation Options. Then select Change Locale

21. Select en_US.UTF-8 UTF-8. Then select en_GB.UTF-8

22. Select Localisation Options. Then select Change Keyboard Layout

23. Choose your keyboard, or select one of the generics that best apply (not intl if you're in the US)

24. Choose English (UK)

25. Select the appropriate AltGr configuration (usually no AltGr key).

26. Select No compose key

27. Select No for Ctrl_Alt_Backspace

28. run sudo nano /etc/default/keyboard

29. Change XKBLAYOUT="gb" to "us"

30. Reboot

(19-22 Optional)
19. You may need to do a daily reboot. In a terminal run: sudo crontab -e

20. If you haven't chosen an editor, choose nano (should be option 2)

21. At the bottom of your crontab enter: 0 2 * * * /sbin/shutdown -r

22. Press Ctrl+X to Exit, press Y to save, and press Enter to close

23. We need to start the program upon reboot. In a terminal run: sudo crontab -e

24. If you haven't chosen an editor, choose nano (should be option 2)

25. At the bottom of your crontab enter: @reboot /home/pi/Documents/Neopets/RunDailies

26. Press Ctrl+X to Exit, press Y to save, and press Enter to close

27. Install teamviewer by choosing Raspbian ARM download here https://www.teamviewer.com/en/download/linux

28. Open a terminal and run sudo dpkg -i /path/to/deb/file (it should error, hence the next steps)

29. Run sudo apt-get install -f

30. Run sudo apt--fix-broken install

30. Open TeamViewer GUI and click the gear icon

31. Set the password for remote conection to your choosing (make sure to save your password and 9 digit ID somewhere remote)

32. Open a terminal and run sudo nano /boot/config.txt

33. Uncomment #framebuffer_width=1280 and #framebuffer_height=720

34. Press Ctrl+X to Exit, press Y to save, and press Enter to close

35. Open Chromium and login to Neopets. Navigate to Qasalan Expellibox and enable Flash.

NOTE: The code provided handles this, but your Chromium profile filepath is as follows: user-data-dir=/home/pi/.config/chromium (/Default will be auto-appended)

Resources used:
https://raspberrypi.stackexchange.com/a/67631

https://askubuntu.com/questions/86849/how-to-unzip-a-zip-file-from-the-terminal

https://developers.supportbee.com/blog/setting-up-cucumber-to-run-with-Chrome-on-Linux/

https://raspberrypi.stackexchange.com/questions/61716/getting-message-to-upgrade-my-version-of-chromium-but-when-when-i-try-to-upgrade

https://ubuntuforums.org/showthread.php?t=952575

https://stackoverflow.com/a/43056974

https://stackoverflow.com/a/15514348

http://www.yann.com/en/use-xvfb-selenium-and-chrome-to-drive-a-web-browser-in-php-23/08/2012.html


# License

[All Selenium projects are licensed under the Apache 2.0 License.](http://www.seleniumhq.org/about/license.jsp)

[Apache Commons Email is under Apache License 2.0.](http://www.apache.org/licenses/)

[JavaMail uses multiple licenses](https://java.net/projects/javamail/pages/License)

The provided NeoBot scripts are released under MIT License.
Copyright © 2017 TheLazyGamer

For educational purposes only. I am not responsible for any bans or suspensions to your account. Botting is against Neopet's EULA. Use at your own discretion.
