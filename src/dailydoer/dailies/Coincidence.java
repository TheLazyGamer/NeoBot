package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

public class Coincidence
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runCoincidence");
        driver.get("http://www.neopets.com/space/coincidence.phtml");

        ArrayList<String> questItems = new ArrayList<String>();
        ArrayList<Integer> questAmounts = new ArrayList<Integer>();

        for (int x = 1; Utils.isElementPresentXP("//*[@id='questItems']/tbody/tr/td[" + x + "]", driver); x++) {
            String questItem = driver.findElement(By.xpath("//*[@id='questItems']/tbody/tr/td[" + x + "]")).getText().replace("\n", "").trim();
            Utils.logMessage("Coincidence questItem " + x + ": " + questItem);
            questItems.add(questItem.substring(0, questItem.lastIndexOf("x")));
            questAmounts.add(Integer.parseInt(questItem.substring(questItem.lastIndexOf("x") + 1, questItem.length())));
        }

        boolean doneShopping = false;

        while (!doneShopping) {

            for (int x = 0; x < questItems.size(); x++) {
                if (!Utils.shopWizard(driver, questItems.get(x))) {
                    Utils.logMessage("Failed ending 1 runCoincidence");
                    return false;
                }
            }

            ArrayList<String> tempQuestItems = new ArrayList<String>();
            ArrayList<Integer> tempQuestAmounts = new ArrayList<Integer>();
            tempQuestItems.addAll(questItems);
            tempQuestAmounts.addAll(questAmounts);

            driver.get("http://www.neopets.com/quickstock.phtml");
            for (int x = 2; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
                String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]")).getText();
                for (int y = 0; y < tempQuestItems.size(); y++) {
                    if (invItem.contains(tempQuestItems.get(y))) {
                        tempQuestAmounts.set(y, tempQuestAmounts.get(y) - 1);
                        if (tempQuestAmounts.get(y) == 0) {
                            tempQuestItems.remove(y);
                            tempQuestAmounts.remove(y);
                            y--;
                        }
                    }
                }
            }

            if (tempQuestAmounts.size() == 0) {
                doneShopping = true;
            }

        }

        Utils.shopURLs.clear();

        driver.get("http://www.neopets.com/space/coincidence.phtml");

        driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td/div[3]/div")).click();

        if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td/a/div/div", driver)) {
            Utils.logMessage("Successfully ending runCoincidence");
            return true;
        }
        Utils.logMessage("Failed ending 2 runCoincidence");
        return false;
    }
}