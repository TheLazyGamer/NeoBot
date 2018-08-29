package dailydoer.hourlies;

import dailydoer.User;
import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class StockItems
{
    public static boolean run(WebDriver driver, String callType)
    {
        Utils.logMessage("Starting runStockItems");
        driver.get("http://www.neopets.com/market.phtml?type=your");
        int stockedItems = 0;
        if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[1]/b[2]", driver)) {
            stockedItems = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[1]/b[2]")).getText());
        }
        if (stockedItems < 30) {
            int itemsNeeded = 30 - stockedItems;

            ArrayList<String> removedItems = new ArrayList<>();

            driver.get("http://www.neopets.com/safetydeposit.phtml");
            int n = 2;
            while (itemsNeeded > 0) {
                if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[" + n + "]/td[2]/b", driver)) {
                    String storedItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[" + n + "]/td[2]/b")).getText();
                    boolean isIgnoredItem = false;
                    for (int ii = 0; ii < User.IGNORED_ITEMS.size(); ii++) {
                        if (storedItem.contains(User.IGNORED_ITEMS.get(ii))) {
                            isIgnoredItem = true;
                            break;
                        }
                    }
                    if (!isIgnoredItem) {
                        int itemCount = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[" + n + "]/td[5]/b")).getText());
                        storedItem = storedItem.replace("\n", "").trim();
                        if (storedItem.endsWith(")")) {
                            storedItem = storedItem.substring(0, storedItem.indexOf("(")).trim();
                        }
                        Utils.logMessage("runStockItems storedItem: " + storedItem);
                        removedItems.add(storedItem);
                        if (itemCount > itemsNeeded) {
                            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[" + n + "]/td[6]/input")).clear();
                            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[" + n + "]/td[6]/input")).sendKeys(itemsNeeded + "");
                            driver.findElement(By.xpath("//input[@value='Move Selected Items']")).click();
                            break;
                        }
                        else {
                            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[" + n + "]/td[6]/input")).clear();
                            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[" + n + "]/td[6]/input")).sendKeys(itemCount + "");
                            itemsNeeded = itemsNeeded - itemCount;
                        }
                    }
                    n++;
                }
                else {
                    break;
                }
            }
            if (Utils.isElementPresentXP("//input[@value='Move Selected Items']", driver)) {
                driver.findElement(By.xpath("//input[@value='Move Selected Items']")).click();
            }

            itemsNeeded = 30 - stockedItems;
            int itemsClicked = 0;
            Utils.logMessage("runStockItems going to quickstock to put items in shop");
            driver.get("http://www.neopets.com/quickstock.phtml");
            for (n = 2; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]", driver); n++) {
                if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[1]", driver) && Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[2]/input", driver)) {
                    String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[1]")).getText().trim();
                    if (invItem.endsWith(")")) {
                        invItem = invItem.substring(0, invItem.indexOf("(")).trim();
                    }
                    Utils.logMessage("runStockItems invItem: " + invItem);
                    if (invItem.length() > 1 && removedItems.contains(invItem) && itemsClicked < itemsNeeded) {
                        itemsClicked++;
                        driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[2]/input")).click();
                    }
                }
                else {
                    break;
                }
            }

            if (Utils.isElementPresentXP("//input[@value='Submit']", driver)) {
                driver.findElement(By.xpath("//input[@value='Submit']")).click();
                Utils.handleAlert(driver);
            }
            driver.get("http://www.neopets.com/market.phtml?type=your");
        }

        ArrayList<String> itemsToBePriced = new ArrayList<String>();
        ArrayList<Integer> stockedPrices = new ArrayList<Integer>();
        Utils.logMessage("runStockItems about to get items");
        for (int shopItems = 2; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + shopItems + "]/td[5]/input", driver); shopItems++) {
            if (callType.equals("Stock")) {
                if (Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + shopItems + "]/td[5]/input")).getAttribute("value").trim()) == 0) {
                    itemsToBePriced.add(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + shopItems + "]/td[1]")).getText());
                }
            }
            else {
                String stockedItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + shopItems + "]/td[1]")).getText();
                boolean isIgnoredItem = false;
                for (int ii = 0; ii < User.IGNORED_ITEMS.size(); ii++) {
                    if (stockedItem.contains(User.IGNORED_ITEMS.get(ii))) {
                        isIgnoredItem = true;
                        break;
                    }
                }
                if (!isIgnoredItem) {
                    itemsToBePriced.add(stockedItem);
                }
            }
        }

        Utils.logMessage("runStockItems done getting items");

        for (int x = 0; x < itemsToBePriced.size(); x++) {
            driver.get("http://www.neopets.com/market.phtml?type=wizard");

            if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/p[1]/a[1]", driver)) { //Faerie quest link
                Utils.logMessage("runStockItems FQ found");

                if (!Utils.faerieQuest(driver)) {
                    Utils.logMessage("Failed ending runStockItems");
                    return false;
                }

                driver.get("http://www.neopets.com/market.phtml?type=wizard");
            }

            driver.findElement(By.name("shopwizard")).clear();
            driver.findElement(By.name("shopwizard")).sendKeys(itemsToBePriced.get(x));

            new Select(driver.findElement(By.name("criteria"))).selectByVisibleText("identical to my phrase");
            driver.findElement(By.cssSelector("option[value=\"exact\"]")).click();
            driver.findElement(By.cssSelector("div > input[type=\"submit\"]")).click();

            int ourPrice = 999999;
            for (int refreshCnt = 0; refreshCnt < 3; refreshCnt++) {
                if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[4]/td[4]/b", driver)) { //search
                    String secondItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[3]/td[4]/b")).getText();
                    secondItem = secondItem.replace(" NP", "").replaceAll(",", "");
                    int secondResult = Integer.parseInt(secondItem);
                    if (secondResult < ourPrice) {
                        ourPrice = secondResult;
                    }
                }

                driver.navigate().refresh();
                Utils.handleAlert(driver);
                Utils.sleepMode(3000);
            }

            if (ourPrice == 999999) {
                stockedPrices.add(-1);
            }
            else {
                stockedPrices.add(ourPrice - 1);
            }
        }

        driver.get("http://www.neopets.com/market.phtml?type=your");

        ArrayList<String> removedItems = new ArrayList<String>();

        for (int x = 0; x < itemsToBePriced.size(); x++) {
            for (int n = 2; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[1]", driver); n++) {
                if (driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[1]")).getText().equals(itemsToBePriced.get(x))) {
                    int price = stockedPrices.get(x);
                    if (stockedPrices.get(x) != -1) {
                        if (price > User.MINIMUM_KEEP_VALUE) {
                            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[5]/input")).clear();
                            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[5]/input")).sendKeys(String.valueOf(stockedPrices.get(x)));
                        }
                        else {
                            List<WebElement> allOptions = new Select(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[7]/select"))).getOptions();
                            new Select(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[7]/select"))).selectByVisibleText(allOptions.get(allOptions.size() - 1).getText());
                            removedItems.add(itemsToBePriced.get(x));
                        }
                    }
                }
            }
        }

        driver.findElement(By.xpath("//input[@value='Update']")).click();

        if (removedItems.size() > 0) {
            driver.get("http://www.neopets.com/quickstock.phtml");

            for (int x = 2; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
                if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[1]", driver) && Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[4]/input", driver)) {
                    String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[1]")).getText();
                    if (invItem.length() > 1) {
                        if (removedItems.contains(invItem)) {
                            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[4]/input")).click();
                        }
                    }
                }
            }

            driver.findElement(By.xpath("//input[@value='Submit']")).click();
            Utils.handleAlert(driver);
        }

        if (callType.equals("Reprice")) {
            driver.get("http://www.neopets.com/market.phtml?type=sales");
            if (Utils.isElementPresentXP("//input[@value='Clear Sales History']", driver)) {
                driver.findElement(By.xpath("//input[@value='Clear Sales History']")).click();
            }
        }

        Utils.logMessage("Successfully ending runStockItems");
        return true;
    }
}