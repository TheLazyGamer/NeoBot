package dailydoer.hourlies;

import dailydoer.User;
import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteItems
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Depositing and discarding inventory items...");
        driver.get("http://www.neopets.com/quickstock.phtml");

        String resultLine = "";

        for (int x = 2; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
            if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[1]", driver) &&
                    Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[4]/input", driver) &&
                    !Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[1]/b", driver)) {
                String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[1]")).getText();
                if (invItem.length() > 1 && !invItem.contains("Weak Bottled")) {
                    boolean hasNeocodexPrice = false;
                    boolean hasJellyneoPrice = false;
                    boolean priceBelowThreshold = false;
                    try {
                        URL apiUrl = new URL("http://www.neocodex.us/forum/index.php?app=itemdb&module=search?app=itemdb&module=search&section=search&item=%22" + invItem.replace(" ", "+") + "%22&description=&rarity_low=&rarity_high=&price_low=&price_high=&shop=&search_order=price&sort=asc&lim=20");
                        HttpURLConnection apiCon = (HttpURLConnection) apiUrl.openConnection();
                        apiCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
                        InputStream apiInStream = apiCon.getInputStream();
                        InputStreamReader apiInStreamReader = new InputStreamReader(apiInStream);
                        BufferedReader apiBufRead = new BufferedReader(apiInStreamReader);

                        while ((resultLine = apiBufRead.readLine()) != null) {
                            if (resultLine.contains("idbQuickPrice")) {
                                hasNeocodexPrice = true;
                                resultLine = apiBufRead.readLine();
                                int price = Integer.parseInt(resultLine.substring(resultLine.indexOf(">") + 1, resultLine.indexOf(" ")).replace(",", ""));
                                if (price <= User.MINIMUM_KEEP_VALUE) {
                                    priceBelowThreshold = true;
                                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[4]/input")).click();
                                } else {
                                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[3]/input")).click();
                                }
                                break;
                            }
                        }
                        if (!hasNeocodexPrice) {
                            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[3]/input")).click();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Utils.sleepMode(30000);
                    }

                    if (!priceBelowThreshold) {
                        try {
                            Utils.logMessage("Checking Jellyneo prices");
                            URL apiUrl = new URL("https://items.jellyneo.net/search/?name=" + invItem.replace(" ", "+") + "&name_type=3");
                            HttpURLConnection apiCon = (HttpURLConnection) apiUrl.openConnection();
                            apiCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
                            InputStream apiInStream = apiCon.getInputStream();
                            InputStreamReader apiInStreamReader = new InputStreamReader(apiInStream);
                            BufferedReader apiBufRead = new BufferedReader(apiInStreamReader);

                            while ((resultLine = apiBufRead.readLine()) != null) {
                                if (resultLine.contains("price-history-link")) {
                                    hasJellyneoPrice = true;
                                    int price = Integer.parseInt(Utils.removeHTML(resultLine).replace(",", "").replace("NP", "").trim());
                                    if (price <= User.MINIMUM_KEEP_VALUE) {
                                        priceBelowThreshold = true;
                                        driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[4]/input")).click();
                                    } else {
                                        driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[3]/input")).click();
                                    }
                                    break;
                                }
                            }
                            if (!hasNeocodexPrice && !hasJellyneoPrice) {
                                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[3]/input")).click();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Utils.sleepMode(30000);
                        }
                    }
                    Utils.sleepMode(5000);
                } else if (invItem.contains("Weak Bottled")) { //Deposit Weak Bottle Faeries
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[3]/input")).click();
                }
            } else if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[1]", driver)) {
                String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[1]")).getText().trim();
                if (invItem.endsWith(" Birthday Cupcake")) {
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[3]/input")).click();
                } else if (invItem.contains("Pile of Dung")) {
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[5]/input")).click();
                }
            }
        }

        if (Utils.isElementPresentXP("//input[@value='Submit']", driver)) {
            driver.findElement(By.xpath("//input[@value='Submit']")).click();
            Utils.handleAlert(driver);
        }

        Utils.logMessage("Successfully ending runDeleteItems");
        return true;
    }
}