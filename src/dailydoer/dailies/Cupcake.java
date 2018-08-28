package dailydoer.dailies;

import dailydoer.User;
import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Cupcake
{
    public static boolean run(WebDriver driver)
    {
        try {
            Utils.logMessage("Starting runCupcake");

            Calendar cal = Calendar.getInstance();
            String currentDateStamp = new SimpleDateFormat("MMMM").format(cal.getTime()) + " " + new SimpleDateFormat("d").format(cal.getTime()) + ",";
            Utils.logMessage("currentDateStamp: " + currentDateStamp);

            ArrayList<String> birthdayPets = new ArrayList<String>();

            String fileLine = "";
            try (BufferedReader br = new BufferedReader(new FileReader("dailydoer/resources/BirthdayList.csv"))) {
                while ((fileLine = br.readLine()) != null) {
                    if (fileLine.contains(currentDateStamp)) {
                        Utils.logMessage("Bday pet: " + fileLine.substring(fileLine.indexOf(",") + 1, fileLine.length()));
                        birthdayPets.add(fileLine.substring(fileLine.indexOf(",") + 1, fileLine.length()));
                    }
                }
            }

            String adoptedPet = "";
            for (int x = 0; x < birthdayPets.size(); x++) {
                adoptedPet = birthdayPets.get(x);
                driver.get("http://www.neopets.com/pound/adopt.phtml?search=" + birthdayPets.get(x));
                Utils.sleepMode(1000);
                if (Utils.isElementPresentID("pet1_price_div", driver)) {
                    break;
                }
                else if (x == birthdayPets.size() - 1) {
                    return true;
                }
            }

            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td/div[2]/div[3]/div[1]/table/tbody/tr[2]/td/div/img")).click();
            Utils.handleAlert(driver);
            Utils.sleepMode(3000);

            driver.get("http://www.neopets.com/petlookup.phtml?pet=" + adoptedPet);


            String currentDay = new SimpleDateFormat("E").format(cal.getTime());
            Utils.sleepMode(30000);

            if (Utils.isElementPresentXP("//*[@id=\"birthdayPrize71521\"]/img", driver)) {

                if (currentDay.equals("Sun") || currentDay.equals("Mon") || currentDay.equals("Tue")) {
                    //Pink Cupcake for HP
                    driver.findElement(By.xpath("//*[@id=\"birthdayPrize71521\"]/img")).click();
                }
                else if (currentDay.equals("Wed") || currentDay.equals("Thu")) {
                    //Yellow Cupcake for STR
                    driver.findElement(By.xpath("//*[@id=\"birthdayPrize71522\"]/img")).click();
                }
                else if (currentDay.equals("Fri") || currentDay.equals("Sat")) {
                    //Blue Cupcake for DEF
                    driver.findElement(By.xpath("//*[@id=\"birthdayPrize71523\"]/img")).click();
                }

                Utils.sleepMode(1000);
                driver.findElement(By.xpath("//*[@id=\"popupBirthdaySelect\"]/div[2]/div")).click();

            }

            Utils.sleepMode(5000);

            driver.get("http://www.neopets.com/pound/abandon.phtml");

            WebElement abandonPet = null;
            if (Utils.isElementPresentID(adoptedPet + "_button", driver)) {
                List<WebElement> abandonPets = driver.findElements(By.id(adoptedPet + "_button"));

                for (int x = 0; x < abandonPets.size(); x++) {
                    abandonPet = abandonPets.get(x);

                    try {
                        abandonPet.click();
                        x = 99999;
                    }
                    catch(ElementNotVisibleException ex) {

                    }
                }
            }
            while (Utils.isElementPresentID(adoptedPet + "_button", driver)) {
                abandonPet.click();
                Utils.sleepMode(1000);
            }

            Utils.sleepMode(5000);

            driver.findElement(By.xpath("//*[@id=\"" + User.PETNAME + "_menu_launcher\"]")).click();
            driver.findElement(By.xpath("//*[@id=\"" + User.PETNAME + "_menu\"]/li[1]")).click();

		/*
		driver.get("http://www.neopets.com/inventory.phtml");

		int itemRow = 1;
		int itemColumn = 1;
		for (; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[3]/table/tbody/tr[2]/td/table/tbody/tr[" + itemRow + "]/td[1]", driver); itemRow++) {
			for (; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[3]/table/tbody/tr[2]/td/table/tbody/tr[" + itemRow + "]/td[" + itemColumn + "]", driver); itemColumn++) {
				String itemText = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[3]/table/tbody/tr[2]/td/table/tbody/tr[" + itemRow + "]/td[" + itemColumn + "]")).getText();

				if (itemText.contains("Birthday Cupcake")) {
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[3]/table/tbody/tr[2]/td/table/tbody/tr[" + itemRow + "]/td[" + itemColumn + "]")).click();
					sleepMode(3000);

					String winHandleBefore = driver.getWindowHandle();

					for (String winHandle : driver.getWindowHandles()) {
						driver.switchTo().window(winHandle);
					}

					sleepMode(5000);

					new Select(driver.findElement(By.name("action"))).selectByVisibleText("Feed to " + User.PETNAME + ".");

					driver.findElement(By.xpath("//input[@value='Submit']")).click();

					sleepMode(5000);

					driver.close(); //may not be needed since the window closes automatically
					driver.switchTo().window(winHandleBefore);

					itemRow = 99999;
					itemColumn = 99999;
				}
			}
		}*/
        }
        catch (Exception ex) {
            return true;
        }
        Utils.logMessage("Successfully ending runCupcake");
        return true;
    }
}