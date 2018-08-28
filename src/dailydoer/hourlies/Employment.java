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

public class Employment
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Employment...");
        driver.get("http://www.neopets.com/faerieland/employ/employment.phtml?type=jobs&voucher=basic");

        int bestItemXpathIndex = 0;
        int bestItemProfit = 0;
        int x = 2;

        for (; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]/table/tbody/tr[" + x + "]/td/b[1]", driver); x+=3)
        {
            String jobDesc = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]/table/tbody/tr[" + x + "]/td")).getText().trim();
            jobDesc = jobDesc.replace("\n\n", "<br>").replace("   ", "<br>");
            String[] jobDescSplit = jobDesc.split(" ");
            int itemAmount = Integer.parseInt(jobDescSplit[1]);
            int reward = Integer.parseInt(jobDescSplit[jobDescSplit.length - 2].replace(",", ""));
            String item = jobDesc.substring(jobDesc.indexOf(":") + 1, jobDesc.indexOf("<")).trim();
            Utils.logMessage("itemAmount: " + itemAmount + ", item: " + item + ", reward: " + reward);

            if (item.length() > 1) {
                while (true) {
                    try {
                        URL apiUrl = new URL("http://www.neocodex.us/forum/index.php?app=itemdb&module=search?app=itemdb&module=search&section=search&item=%22" + item.replace(" ", "+") + "%22&description=&rarity_low=&rarity_high=&price_low=&price_high=&shop=&search_order=price&sort=asc&lim=20");
                        HttpURLConnection apiCon = (HttpURLConnection) apiUrl.openConnection();
                        apiCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
                        InputStream apiInStream = apiCon.getInputStream();
                        InputStreamReader apiInStreamReader = new InputStreamReader(apiInStream);
                        BufferedReader apiBufRead = new BufferedReader(apiInStreamReader);

                        String resultLine = "";

                        while ((resultLine = apiBufRead.readLine()) != null) {
                            if (resultLine.contains("idbQuickPrice")) {
                                resultLine = apiBufRead.readLine();
                                Utils.logMessage("Neocodex price: " + resultLine);
                                int price = Integer.parseInt(resultLine.substring(resultLine.indexOf(">") + 1, resultLine.indexOf(" ")).replace(",", ""));
                                int profit = reward - (price * itemAmount);
                                if (profit > bestItemProfit) {
                                    bestItemProfit = profit;
                                    bestItemXpathIndex = x;
                                }
                                break;
                            }
                        }
                        break;
                    }
                    catch(Exception ex) {
                        break;
                        //sleepMode(5000);
                    }
                }
            }
        }

        Utils.logMessage("Winning job found");
        String jobDesc = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]/table/tbody/tr[" + bestItemXpathIndex + "]/td")).getText().trim();
        jobDesc = jobDesc.replace("\n\n", "<br>").replace("   ", "<br>");
        String[] jobDescSplit = jobDesc.split(" ");
        int itemAmount = Integer.parseInt(jobDescSplit[1]);
        String item = jobDesc.substring(jobDesc.indexOf(":") + 1, jobDesc.indexOf("<")).trim();

        driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]/table/tbody/tr[" + (bestItemXpathIndex - 1) + "]/td[3]/b/a")).click();

        boolean jobReceived = false;
        String jobUrl = "";

        Utils.logMessage("About to check if we got the job");

        int petIndex = 1;

        //Checks for "too many jobs" page
        if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]", driver)) {
            if (driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]")).getText().trim().equals("You have already taken enough jobs today! Come back tomorrow.")) {
                Utils.logMessage("Completed the max amount of jobs for today");
                Utils.logMessage("Successfully ending 1 runEmployment");
                return true;
            }
        }

        driver.get("http://www.neopets.com/faerieland/employ/employment.phtml?type=status");

        //Find main pet
        for (; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]/table/tbody/tr[" + petIndex + "]/td[1]/b", driver); petIndex += 2) {
            String possiblePetName = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]/table/tbody/tr[" + petIndex + "]/td[1]/b")).getText();
            if (possiblePetName.contains(User.PETNAME)) {
                break;
            }
        }

        driver.get("http://www.neopets.com/faerieland/employ/employment.phtml?type=status");
        if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]/table/tbody/tr[" + (petIndex + 1) + "]/td[2]/a", driver)) {
            Utils.logMessage("We got the job");
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]/table/tbody/tr[" + (petIndex + 1) + "]/td[2]/a")).click();

            String jobDetails = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]/b[2]")).getText().trim();

            Utils.logMessage("jobDetails: " + jobDetails);
            item = jobDetails.substring(jobDetails.indexOf(":") + 2, jobDetails.length());
            itemAmount = Integer.parseInt(jobDetails.substring(5, 7).trim());
            item = item.trim();

            jobReceived = true;
            jobUrl = driver.getCurrentUrl();

            boolean doneShopping = false;

            while (!doneShopping) {

                if (!Utils.shopWizard(driver, item)) {
                    Utils.logMessage("Failed ending 1 runEmployment");
                    return false;
                }

                driver.get("http://www.neopets.com/quickstock.phtml");
                int itemCount = 0;
                for (int qsIndex = 2; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + qsIndex + "]", driver); qsIndex++) {
                    String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + qsIndex + "]")).getText().trim();
                    if (invItem.contains("N/A")) {
                        invItem = invItem.substring(0, invItem.indexOf(" N/A"));
                    }
                    Utils.logMessage("invItem: ~" + invItem + "~");
                    Utils.logMessage("item: ~" + item + "~");
                    if (invItem.equals(item)) {
                        itemCount++;
                    }
                }

                Utils.logMessage("This is how many employment items we have: " + itemCount);
                Utils.logMessage("This is how many employment items we need total: " + itemAmount);

                if (itemCount >= itemAmount) {
                    doneShopping = true;
                }

            }
            Utils.shopURLs.clear();
        }

        if (jobReceived) {
            driver.get(jobUrl.replace("apply", "desc"));
            if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[3]/b[4]", driver)) {
                Utils.logMessage("We completed a job: " + driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[3]/b[4]")).getText().trim());
                if (driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[3]/b[4]")).getText().trim().equals("Completed Job Data...")) {
                    Utils.logMessage("Successfully ending 2 runEmployment");
                    return true;
                }
            }
        }

        Utils.logMessage("Failed ending 2 runEmployment");
        return false;
    }
}