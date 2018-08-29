package dailydoer.hourlies;

import dailydoer.User;
import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class DubloonTraining
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runDubloonTraining");
        driver.get("http://www.neopets.com/pirates/academy.phtml?type=status");

        if (Utils.isElementPresentXP("//input[@value='Complete Course!']", driver)) {
            driver.findElement(By.xpath("//input[@value='Complete Course!']")).click();
            driver.get("http://www.neopets.com/pirates/academy.phtml?type=status");
        }
        else if (driver.getPageSource().contains("Time till course finishes")){
            Utils.logMessage("Successfully ending 1 runDubloonTraining");
            return true;
        }

        int petIndex = 1;
        for (; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + petIndex + "]/td/b", driver); petIndex+=2) {
            String possiblePetName = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + petIndex + "]/td/b")).getText();
            if (possiblePetName.contains(User.PETNAME + " ")) {
                break;
            }
        }

        int level = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + (petIndex+1) + "]/td[1]/font/b")).getText());
        int str = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + (petIndex+1) + "]/td[1]/b[1]")).getText());
        int def = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + (petIndex+1) + "]/td[1]/b[2]")).getText());
        String tempHP = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + (petIndex+1) + "]/td[1]/b[4]")).getText();
        int hp = Integer.parseInt(tempHP.substring(tempHP.indexOf("/") + 1, tempHP.length()).trim());

        Utils.logMessage("dubloonTraining level: " + level);
        Utils.logMessage("dubloonTraining str: " + str);
        Utils.logMessage("dubloonTraining def: " + def);
        Utils.logMessage("dubloonTraining hp: " + hp);

        String trainingCoin = "";
        if (level <= 10) {
            trainingCoin = "One Dubloon Coin";
        }
        else if (level >= 11 && level <= 20) {
            trainingCoin = "Two Dubloon Coin";
        }
        else if (level >= 21 && level <= 30) {
            trainingCoin = "Five Dubloon Coin";
        }
        else if (level >= 31 && level < 40) {
            trainingCoin = "Five Dubloon Coin";
        }
        else {
            Utils.logMessage("Failed ending 1 runDubloonTraining");
            Utils.sendEmail("Level too high for dubloon training. Please change to island.");
            return false;
        }

        driver.get("http://www.neopets.com/quickstock.phtml");
        boolean needToBuyCoin = true;

        for (int x = 2; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
            String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]")).getText();
            if (invItem.contains(trainingCoin)) {
                needToBuyCoin = false;
                break;
            }
        }

        if (needToBuyCoin) {
            boolean doneShopping = false;
            while (!doneShopping) {

                if (!Utils.shopWizard(driver, trainingCoin)) {
                    Utils.logMessage("Failed ending 2 runDubloonTraining");
                    return false;
                }

                driver.get("http://www.neopets.com/quickstock.phtml");
                for (int x = 2; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
                    String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]")).getText();
                    if (invItem.contains(trainingCoin)) {
                        doneShopping = true;
                        break;
                    }
                }
            }
            Utils.shopURLs.clear();
        } //End buying the coin

        driver.get("http://www.neopets.com/pirates/academy.phtml?type=courses");
		/*
		if (str >= level * 2 || def >= level * 2 || hp >= level * 2) {
			new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Level");
			logMessage("Dubloon train level");
		}
		else if (str <= def && str < (level * 2) - 1) {
			new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Strength");
			logMessage("Dubloon train str");
		}
		else if (def <= str && def < (level * 2) - 1) {
			new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Defence");
			logMessage("Dubloon train def");
		}
		else {
			new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Endurance");
			logMessage("Dubloon train hp");
		}
		 */
        //Doing this and commenting out the above because of cupcake training
        new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Level");
        Utils.logMessage("Dubloon train level");
        new Select(driver.findElement(By.name("pet_name"))).selectByValue(User.PETNAME);
        driver.findElement(By.xpath("//input[@value='Start Course']")).click();
        driver.get("http://www.neopets.com/pirates/academy.phtml?type=status");
        driver.findElement(By.xpath("//input[@value='Pay']")).click();

        Utils.logMessage("Successfully ending 2 runDubloonTraining");
        return true;
    }
}