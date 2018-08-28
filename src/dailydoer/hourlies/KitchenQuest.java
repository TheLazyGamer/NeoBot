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
import java.util.ArrayList;

public class KitchenQuest
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runKitchenQuest");
        //Formula loosely based on http://www.neopets.com/ntimes/index.phtml?section=477168&week=424
        driver.get("http://www.neopets.com/pirates/academy.phtml?type=status");

        int totalNeopets = 0;
        int neopetLevel = 0;
        int trainingCost = 0;

        for (int pets = 1; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + pets + "]/td/b", driver); pets+=2) {
            String petTitle = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + pets + "]/td/b")).getText();
            if (petTitle.contains(User.PETNAME)) {
                neopetLevel = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + (pets + 1) + "]/td[1]/font/b")).getText());
                totalNeopets++;
            }
            else {
                totalNeopets++;
            }
        }

        Utils.logMessage("We have this many pets: " + totalNeopets + " with our dailydoer pet's level: " + neopetLevel);

        if (neopetLevel < 10) {
            trainingCost = User.AVERAGE_ONE_DUBLOON_COST;
        }
        else if (neopetLevel >= 11 && neopetLevel <= 20) {
            trainingCost = User.AVERAGE_TWO_DUBLOON_COST;
        }
        else if (neopetLevel >= 21 && neopetLevel < 40) {
            trainingCost = User.AVERAGE_FIVE_DUBLOON_COST;
        }
        else if (neopetLevel >= 40 && neopetLevel <= 80) {
            trainingCost = User.AVERAGE_CODESTONE_COST * 3;
        }
        else if (neopetLevel >= 81 && neopetLevel <= 100) {
            trainingCost = User.AVERAGE_CODESTONE_COST * 4;
        }
        else if (neopetLevel >= 101 && neopetLevel <= 120) {
            trainingCost = User.AVERAGE_CODESTONE_COST * 5;
        }
        else if (neopetLevel >= 121 && neopetLevel <= 150) {
            trainingCost = User.AVERAGE_CODESTONE_COST * 6;
        }
        else if (neopetLevel >= 151 && neopetLevel <= 200) {
            trainingCost = User.AVERAGE_CODESTONE_COST * 7;
        }
        else if (neopetLevel >= 201 && neopetLevel <= 249) {
            trainingCost = User.AVERAGE_CODESTONE_COST * 8;
        }
        else if (neopetLevel >= 250 && neopetLevel <= 299) {
            trainingCost = User.AVERAGE_RED_CODESTONE_COST;
        }
        else if (neopetLevel >= 300 && neopetLevel <= 399) {
            trainingCost = User.AVERAGE_RED_CODESTONE_COST * 2;
        }
        else if (neopetLevel >= 400 && neopetLevel <= 499) {
            trainingCost = User.AVERAGE_RED_CODESTONE_COST * 3;
        }
        else if (neopetLevel >= 500 && neopetLevel <= 599) {
            trainingCost = User.AVERAGE_RED_CODESTONE_COST * 4;
        }
        else if (neopetLevel >= 600 && neopetLevel <= 749) {
            trainingCost = User.AVERAGE_RED_CODESTONE_COST * 5;
        }
        else if (neopetLevel >= 750) {
            trainingCost = User.AVERAGE_RED_CODESTONE_COST * 6;
        }

        int maxKitchenQuestExpense = ((User.USEFUL_KQ_STAT_REWARD_PCT * trainingCost) / 100) / totalNeopets;

        driver.get("http://www.neopets.com/island/kitchen.phtml");

        if (Utils.isElementPresentXP("//input[@value='Get out of here!']", driver)) {
            driver.findElement(By.xpath("//input[@value='Get out of here!']")).click();
            driver.get("http://www.neopets.com/island/kitchen.phtml");
        }

        ArrayList<String> neededFoods = new ArrayList<String>();
        if (Utils.isElementPresentXP("//input[@value='Sure, I will help!']", driver)) {
            driver.findElement(By.xpath("//input[@value='Sure, I will help!']")).click();
            for (int foods = 1; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr/td[" + foods + "]/b", driver); foods++) {
                String foodName = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr/td[" + foods + "]/b")).getText();
                neededFoods.add(foodName);
            }
        }

        else if (Utils.isElementPresentXP("//input[@value='I have the Ingredients!']", driver)) {
            for (int foods = 1; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[3]/td[2]/b[" + foods + "]", driver); foods++) {
                String foodName = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[3]/td[2]/b[" + foods + "]")).getText();
                neededFoods.add(foodName);
            }
        }

        int totalCost = 0;

        for (int foods = 0; foods < neededFoods.size(); foods++) {
            while (true) {
                try {
                    URL apiUrl = new URL("http://www.neocodex.us/forum/index.php?app=itemdb&module=search?app=itemdb&module=search&section=search&item=%22" + neededFoods.get(foods).replace(" ", "+") + "%22&description=&rarity_low=&rarity_high=&price_low=&price_high=&shop=&search_order=price&sort=asc&lim=20");
                    HttpURLConnection apiCon = (HttpURLConnection) apiUrl.openConnection();
                    apiCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
                    InputStream apiInStream = apiCon.getInputStream();
                    InputStreamReader apiInStreamReader = new InputStreamReader(apiInStream);
                    BufferedReader apiBufRead = new BufferedReader(apiInStreamReader);

                    boolean hasPrice = false;
                    String resultLine = "";
                    while ((resultLine = apiBufRead.readLine()) != null) {
                        if (resultLine.contains("idbQuickPrice")) {
                            hasPrice = true;
                            resultLine = apiBufRead.readLine();
                            int price = Integer.parseInt(resultLine.substring(resultLine.indexOf(">") + 1, resultLine.indexOf(" ")).replace(",", ""));
                            totalCost+=price;
                            break;
                        }
                    }
                    if (!hasPrice) {
                        totalCost = 99999;
                    }
                    break;
                }
                catch(Exception ex) {
                    totalCost = 99999;
                    break;
                }
            }
        }

        if (totalCost <= maxKitchenQuestExpense) {
            boolean doneShopping = false;

            while (!doneShopping) {

                for (int x = 0; x < neededFoods.size(); x++) {
                    if (!Utils.shopWizard(driver, neededFoods.get(x))) {
                        return false;
                    }
                }

                ArrayList<String> tempNeededFoods = new ArrayList<String>();
                tempNeededFoods.addAll(neededFoods);

                driver.get("http://www.neopets.com/quickstock.phtml");
                for (int x = 2; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
                    String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]")).getText();
                    for (int y = 0; y < tempNeededFoods.size(); y++) {
                        if (invItem.contains(tempNeededFoods.get(y))) {
                            tempNeededFoods.remove(y);
                            y--;
                        }
                    }
                }

                if (tempNeededFoods.size() == 0) {
                    doneShopping = true;
                }

                neededFoods = tempNeededFoods;

            }

            Utils.shopURLs.clear();

            driver.get("http://www.neopets.com/island/kitchen.phtml");

            driver.findElement(By.xpath("//input[@value='I have the Ingredients!']")).click();

            Utils.logMessage("Successfully ending 1 runKitchenQuest");
            return true;

        }
        else {
            Utils.logMessage("Too much money: " + totalCost);
            Utils.logMessage("Can only justify: " + maxKitchenQuestExpense);
            Utils.logMessage("Successfully ending 2 runKitchenQuest");
            return true;
        }
    }
}