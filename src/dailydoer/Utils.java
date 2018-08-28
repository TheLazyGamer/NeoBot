package dailydoer;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Utils
{
    public static ArrayList<String> shopURLs = new ArrayList<>();

    public static boolean faerieQuest(WebDriver driver)
    {
        while (true) {
            try {
                driver.get("http://www.neopets.com/quests.phtml");

                String questItemLink = "";

                String questItem = driver.findElement(By.xpath("//*[@id=\"fq2\"]/div[3]/table[1]/tbody/tr/td/b")).getText().trim();
                String resultLine = "";

                URL apiUrl = new URL("http://www.neocodex.us/forum/index.php?app=itemdb&module=search?app=itemdb&module=search&section=search&item=%22" + questItem.replace(" ", "+") + "%22&description=&rarity_low=&rarity_high=&price_low=&price_high=&shop=&search_order=price&sort=asc&lim=20");
                HttpURLConnection apiCon = (HttpURLConnection) apiUrl.openConnection();
                apiCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
                InputStream apiInStream = apiCon.getInputStream();
                InputStreamReader apiInStreamReader = new InputStreamReader(apiInStream);
                BufferedReader apiBufRead = new BufferedReader(apiInStreamReader);

                while ((resultLine = apiBufRead.readLine()) != null) {
                    if (resultLine.contains("<li style='margin-top: 30px; width: 110px;'>")) {
                        resultLine = apiBufRead.readLine();
                        resultLine = resultLine.substring(resultLine.indexOf("\"") + 1, resultLine.lastIndexOf("\""));
                        questItemLink = resultLine;
                        break;
                    }
                }

                ArrayList<String> itemShopLinks = new ArrayList<String>();
                boolean atShops = false;

                URL itemUrl = new URL(questItemLink);
                HttpURLConnection itemCon = (HttpURLConnection) itemUrl.openConnection();
                itemCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
                InputStream itemInStream = itemCon.getInputStream();
                InputStreamReader itemInStreamReader = new InputStreamReader(itemInStream);
                BufferedReader itemBufRead = new BufferedReader(itemInStreamReader);

                while ((resultLine = itemBufRead.readLine()) != null) {

                    if (atShops) {
                        if (resultLine.contains("a href")) {
                            resultLine = resultLine.substring(resultLine.indexOf("'") + 1, resultLine.lastIndexOf("'"));
                            itemShopLinks.add(resultLine);
                        }
                        else {
                            break;
                        }
                    }
                    if (resultLine.contains("<h3 class='bar'>Shop Owners</h3>")) {
                        atShops = true;
                    }
                }

                boolean doneShopping = false;
                int buyingAttempts = 0;
                while (!doneShopping) {
                    if (itemShopLinks.size() == 0) {
                        sendEmail("On a Faerie quest NO ITEM");
                        return false;
                    }
                    else if (buyingAttempts >= 5) {
                        sendEmail("On a Faerie quest CAN'T BUY ANYTHING");
                        return false;
                    }
                    for (int shopLinkCnt = 0; shopLinkCnt < itemShopLinks.size(); shopLinkCnt++) {
                        driver.get(itemShopLinks.get(shopLinkCnt));
                        if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[4]/table/tbody/tr/td/a/img", driver)) {
                            try {
                                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[4]/table/tbody/tr/td/a/img")).click();
                                handleAlert(driver);
                            }
                            catch(Exception ex) {

                            }
                        }
                        sleepMode(5000);

                        driver.get("http://www.neopets.com/quickstock.phtml");
                        for (int x = 2; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
                            String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]")).getText();
                            if (invItem.contains(questItem)) {
                                doneShopping = true;
                                shopLinkCnt = 99999;
                                break;
                            }
                        }
                    }
                    buyingAttempts++;
                }

                driver.get("http://www.neopets.com/quests.phtml");
                if (isElementPresentID("complete_faerie_quest", driver)) {
                    driver.findElement(By.id("complete_faerie_quest")).click();
                }
                return true;
            }
            catch(Exception ex) {
                //This doesn't need its exception printed. This is normal.
                sleepMode(15000);
            }
        }
    }

    public static boolean shopWizard(WebDriver driver, String itemBuying) {

        driver.get("http://www.neopets.com/safetydeposit.phtml");
        if (isElementPresentName("obj_name", driver)) {
            driver.findElement(By.name("obj_name")).clear();
            driver.findElement(By.name("obj_name")).sendKeys(itemBuying);
            driver.findElement(By.xpath("//input[@value='Find']")).click();
        }
        if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[2]/td[5]/b", driver)) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[2]/td[6]/input")).clear();
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[2]/td[6]/input")).sendKeys(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[2]/td[5]/b")).getText());
            driver.findElement(By.xpath("//input[@value='Move Selected Items']")).click();
            return true;
        }

        driver.get("http://www.neopets.com/market.phtml?type=wizard");

        if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/p[1]/a[1]", driver)) { //Faerie quest link
            logMessage("ShopWizard FQ found");

            if (!faerieQuest(driver)) {
                return false;
            }

            driver.get("http://www.neopets.com/market.phtml?type=wizard");
        }

        if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div[2]/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/form/table/tbody/tr[1]/td[2]/input", driver)) {
            ArrayList<String> shopLinks = new ArrayList<String>();
            ArrayList<Integer> shopPrices = new ArrayList<Integer>();

            driver.findElement(By.name("shopwizard")).clear();
            driver.findElement(By.name("shopwizard")).sendKeys(itemBuying);

            new Select(driver.findElement(By.name("criteria"))).selectByVisibleText("identical to my phrase");
            driver.findElement(By.cssSelector("option[value=\"exact\"]")).click();
            driver.findElement(By.cssSelector("div > input[type=\"submit\"]")).click();

            for (int refreshCnt = 0; refreshCnt < User.SHOP_WIZARD_REFRESH_COUNT; refreshCnt++) {
                if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[4]/td[4]/b", driver)) { //search
                    if (!shopLinks.contains(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[2]/td[1]/a")).getAttribute("href"))) {
                        String firstItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[2]/td[4]/b")).getText();
                        String secondItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[3]/td[4]/b")).getText();
                        String thirdItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[4]/td[4]/b")).getText();

                        firstItem = firstItem.replace(" NP", "").replaceAll(",", "");
                        secondItem = secondItem.replace(" NP", "").replaceAll(",", "");
                        thirdItem = thirdItem.replace(" NP", "").replaceAll(",", "");

                        int firstResult = Integer.parseInt(firstItem);
                        int secondResult = Integer.parseInt(secondItem);
                        int thirdResult = Integer.parseInt(thirdItem);

                        shopLinks.add(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[2]/td[1]/a")).getAttribute("href"));
                        shopLinks.add(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[3]/td[1]/a")).getAttribute("href"));
                        shopLinks.add(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[4]/td[1]/a")).getAttribute("href"));
                        shopPrices.add(firstResult);
                        shopPrices.add(secondResult);
                        shopPrices.add(thirdResult);
                    }

                    if (refreshCnt != 3) {
                        driver.navigate().refresh();
                        handleAlert(driver);
                        sleepMode(3000);
                    }
                } //end first search
            }

            Collections.sort(shopPrices);

            String shopLink = "";
            int priceIndex = 0;

            for (int linkCnt = 0; linkCnt < shopLinks.size(); linkCnt++) {
                String link = shopLinks.get(linkCnt);
                if (link.endsWith(String.valueOf(shopPrices.get(priceIndex)))) {
                    if (!shopURLs.contains(link)) {
                        shopLink = link;
                        break;
                    }
                    else {
                        linkCnt = -1;
                        priceIndex++;
                    }

                }
            }
            logMessage("ShopLink: " + shopLink);
            shopURLs.add(shopLink);
            driver.get(shopLink);
            if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[4]/table/tbody/tr/td/a/img", driver)) {
                try {
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[4]/table/tbody/tr/td/a/img")).click();
                    handleAlert(driver);
                }
                catch(Exception ex) {
                    //This error is normal, and does not need handling
                }
            }
            sleepMode(5000);
        }
        return true;
    }

    public static boolean isElementPresentID(String id, WebDriver driver) {
        boolean present;
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        present = driver.findElements(By.id(id)).size() != 0;
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return present;
    }

    public static boolean isElementPresentLT(String lt, WebDriver driver) {
        boolean present;
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        present = driver.findElements(By.linkText(lt)).size() != 0;
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return present;
    }

    public static boolean isElementPresentName(String name, WebDriver driver) {
        boolean present;
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        present = driver.findElements(By.name(name)).size() != 0;
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return present;
    }

    public static boolean isElementPresentXP(String xpath, WebDriver driver) {
        boolean present;
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        present = driver.findElements(By.xpath(xpath)).size() != 0;
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return present;
    }

    public static void sleepMode(int pauseLen) {
        try {
            Thread.sleep(pauseLen);
        }
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void handleAlert(WebDriver driver) {
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        }
        catch(NoAlertPresentException e) {}
    }

    public static void logMessage(String message) {
        try {
            File logFile = new File("dailydoer/resources/BotLog.log");
            FileWriter logWriter = new FileWriter(logFile, true);
            Date logDate = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String logDateString = format.format(logDate);
            logWriter.append("[" + logDateString + "] " + message + "\n");
            logWriter.flush();
        }
        catch(IOException e) {
            System.out.println("IOException in logMessage");
            e.printStackTrace();
        }
    }

    public static File getLatestFileFromDir(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        return lastModifiedFile;
    }

    public static String removeHTML(String rawHtml) {
        while (rawHtml.contains(">")) {
            try {
                int firstLessThanIndex = rawHtml.indexOf("<");
                int firstGreaterThanIndex = rawHtml.indexOf(">")+1;
                StringBuffer buf = new StringBuffer(rawHtml);
                buf.replace(firstLessThanIndex, firstGreaterThanIndex, "");
                rawHtml = buf.toString();
            }
            catch (StringIndexOutOfBoundsException ex) {
                try {
                    int lastLessThanIndex = rawHtml.lastIndexOf("<");
                    int lastGreaterThanIndex = rawHtml.lastIndexOf(">")+1;
                    StringBuffer buf = new StringBuffer(rawHtml);
                    buf.replace(lastLessThanIndex, lastGreaterThanIndex, "");
                    rawHtml = buf.toString();
                }
                catch (StringIndexOutOfBoundsException e) {
                    //ex.printStackTrace();
                    break;
                }
            }
        }
        return rawHtml.trim();
    }

    public static void sendEmail(String userBody) {
        String subject = "NeoBot";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(User.YOUR_EMAIL, User.YOUR_EMAIL_PASSWORD);
                    }
                });

        boolean sentMail = false;
        while (!sentMail) {
            try {
                logMessage("Creating Message");
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(User.YOUR_EMAIL));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(User.NUMBER_TO_TEXT));
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(User.YOUR_EMAIL));


                if (userBody.contains("CRASH") || userBody.contains("Status") || userBody.contains("Exception")) {
                    message.setSubject(subject);
                    // This mail has 2 parts, the BODY and the embedded image
                    MimeMultipart multipart = new MimeMultipart("related");

                    // first part (the html)
                    BodyPart messageBodyPart = new MimeBodyPart();
                    String htmlText = "<H1>" + userBody + "</H1><img src=\"cid:image\">";
                    messageBodyPart.setContent(htmlText, "text/html");
                    // add it
                    multipart.addBodyPart(messageBodyPart);

                    // second part (the image)
                    messageBodyPart = new MimeBodyPart();
                    String attachedImage = getLatestFileFromDir(new java.io.File(".").getCanonicalPath()).toString();
                    FileDataSource fds = new FileDataSource(new File(attachedImage));


                    messageBodyPart.setDataHandler(new DataHandler(fds));
                    messageBodyPart.setHeader("Content-ID", "<image>");

                    // add image to the multipart
                    multipart.addBodyPart(messageBodyPart);

                    // put everything together
                    message.setContent(multipart); }
                else {
                    message.setSubject(subject);
                    message.setText(userBody); }

                logMessage("About to send message");
                Transport.send(message);
                logMessage("Message successfully sent");
                sentMail = true; }
            catch(MessagingException e) {
                logMessage("MessagingException occurred... retrying in 10 seconds");
                e.printStackTrace();
                sleepMode(10000); }
            catch(Exception ex) {
                logMessage("Unknown exception occurred. Not sending email");
                ex.printStackTrace();
                sentMail = true; }
        }
    }
}
