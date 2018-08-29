package dailydoer.hourlies;

import dailydoer.User;
import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;

public class IslandTraining
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Island Training...");
        //Following Quickest method here http://www.neopets.com/~Alluar (except we only double HP)
        driver.get("http://www.neopets.com/island/training.phtml?type=status");

        if (Utils.isElementPresentXP("//input[@value='Complete Course!']", driver)) {
            driver.findElement(By.xpath("//input[@value='Complete Course!']")).click();
            driver.get("http://www.neopets.com/island/training.phtml?type=status");
        }
        else if (driver.getPageSource().contains("Time till course finishes")){
            Utils.logMessage("Successfully ending 1 Island Training.");
            return true;
        }

        int petIndex = 1;

        if (!driver.getPageSource().contains("This course has not been paid for yet")) {
            for (; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + petIndex + "]/td/b", driver); petIndex += 2) {
                String possiblePetName = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + petIndex + "]/td/b")).getText();
                if (possiblePetName.contains(User.PETNAME + " ")) {
                    break;
                }
            }

            int level = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + (petIndex + 1) + "]/td[1]/font/b")).getText());
            int str = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + (petIndex + 1) + "]/td[1]/b[1]")).getText());
            int def = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + (petIndex + 1) + "]/td[1]/b[2]")).getText());
            String tempHP = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + (petIndex + 1) + "]/td[1]/b[4]")).getText();
            int hp = Integer.parseInt(tempHP.substring(tempHP.indexOf("/") + 1, tempHP.length()).trim());

            Utils.logMessage("islandTraining level: " + level);
            Utils.logMessage("islandTraining str: " + str);
            Utils.logMessage("islandTraining def: " + def);
            Utils.logMessage("islandTraining hp: " + hp);

            driver.get("http://www.neopets.com/island/training.phtml?type=courses");

            if (level <= 80) {
                if (str > level * 2 || def > level * 2 || hp > level * 2) {
                    new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Level");
                    Utils.logMessage("Island train level");
                }
                else if (str <= def && str < (level * 2) - 1) {
                    new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Strength");
                    Utils.logMessage("Island train str");
                }
                else if (def <= str && def < (level * 2) - 1) {
                    new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Defence");
                    Utils.logMessage("Island train def");
                }
                else {
                    new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Endurance");
                    Utils.logMessage("Island train hp");
                }
            }
            else {
                new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Level");
                Utils.logMessage("Island train level");
            }

            new Select(driver.findElement(By.name("pet_name"))).selectByValue(User.PETNAME);
            driver.findElement(By.xpath("//input[@value='Start Course']")).click();

        }

        driver.get("http://www.neopets.com/island/training.phtml?type=status");

        ArrayList<String> codestonesNeeded = new ArrayList<String>();
        for (int x = 1; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + (petIndex + 1) + "]/td[2]/p/b[" + x + "]", driver); x++) {
            codestonesNeeded.add(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + (petIndex + 1) + "]/td[2]/p/b[" + x + "]")).getText().trim());
        }

        driver.get("http://www.neopets.com/quickstock.phtml");
        for (int x = 2; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
            String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]")).getText();
            for (int y = 0; y < codestonesNeeded.size(); y++) {
                if (invItem.contains(codestonesNeeded.get(y))) {
                    codestonesNeeded.remove(y);
                    break;
                }
            }
        }

        boolean doneShopping = false;

        while (!doneShopping) {

            for (int x = 0; x < codestonesNeeded.size(); x++) {
                if (!Utils.shopWizard(driver, codestonesNeeded.get(x))) {
                    Utils.logMessage("Failed Island Training.");
                    return false;
                }
            }

            driver.get("http://www.neopets.com/quickstock.phtml");
            for (int x = 2; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
                String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]")).getText();
                for (int y = 0; y < codestonesNeeded.size(); y++) {
                    if (invItem.contains(codestonesNeeded.get(y))) {
                        codestonesNeeded.remove(y);
                        break;
                    }
                }
            }

            if (codestonesNeeded.size() == 0) {
                doneShopping = true;
            }

        }

        Utils.shopURLs.clear();

        driver.get("http://www.neopets.com/island/training.phtml?type=status");

        driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + (petIndex + 1) + "]/td[2]/a/b")).click();

        Utils.logMessage("Successfully ending 2 Island Training.");
        return true;
    }
}