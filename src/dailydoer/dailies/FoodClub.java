package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.text.SimpleDateFormat;
import java.util.*;

public class FoodClub
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runFoodClub");

        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        if (currentHour < 13) {
            driver.get("http://www.neopets.com/~boochi_target");

            String currentRound = "";

            if (Utils.isElementPresentXP("/html/body/center[3]/center[1]/center[1]/table/tbody/tr[3]/td[1]", driver)) {
                currentRound = driver.findElement(By.xpath("/html/body/center[3]/center[1]/center[1]/table/tbody/tr[3]/td[1]")).getText().trim();
            }
            ArrayList<String> betInfoList = new ArrayList<String>();

            Date currentDate = new Date();
            String dateStop = "05/07/1999 01:00:00";

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String currentDateFormattedString = format.format(currentDate);
            Date d1 = null;

            Date d2 = null;

            try {
                d1 = format.parse(currentDateFormattedString);
                d2 = format.parse(dateStop);
            }
            catch (Exception ex) {
                //This error will never happen. Java syntax requires catching it.
            }
            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffDays = diff / (24 * 60 * 60 * 1000);

            //diffDays *= -1; Use this or the one below (not sure which is more efficient)
            diffDays = -diffDays;
            Utils.logMessage(diffDays + " days");

            String daysSince1999May07 = String.valueOf(diffDays);

            if (currentRound.equals(daysSince1999May07)) {
                for (int x = 3; Utils.isElementPresentXP("/html/body/center[3]/center[1]/center[1]/table/tbody/tr[" + x + "]/td[3]", driver); x++) {
                    String betInfo = driver.findElement(By.xpath("/html/body/center[3]/center[1]/center[1]/table/tbody/tr[" + x + "]/td[2]")).getText().trim();
                    betInfoList.add(betInfo);
                }

                driver.get("http://www.neopets.com/pirates/foodclub.phtml?type=bet");
                String betAmount = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/p[4]/b")).getText();
                for (int x = 0; x < betInfoList.size(); x++) {
                    Utils.logMessage("BET " + x + " FOUND");
                    String[] betInfoArr = betInfoList.get(x).split("\n");
                    for (int n = 0; n < betInfoArr.length; n++) {
                        String bet = betInfoArr[n].trim();
                        String arenaName = bet.substring(0, bet.indexOf(":"));
                        Utils.logMessage("arenaName: " + arenaName);
                        String winningPirate = bet.substring(bet.indexOf(":") + 1, bet.length());
                        int xPathValue = 0;
                        if (arenaName.equals("Shipwreck")) {
                            xPathValue = 3;
                        }
                        else if (arenaName.equals("Lagoon")) {
                            xPathValue = 4;
                        }
                        else if (arenaName.equals("Treasure Island")) {
                            xPathValue = 5;
                        }
                        else if (arenaName.equals("Hidden Cove")) {
                            xPathValue = 6;
                        }
                        else if (arenaName.equals("Harpoon Harry's")) {
                            xPathValue = 7;
                        }

                        driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[4]/form/table[1]/tbody/tr[" + xPathValue + "]/td[1]/input")).click();

                        List<WebElement> allOptions = new Select(driver.findElement(By.name("winner" + (xPathValue - 2)))).getOptions();
                        for (int pirateChoices = 0; pirateChoices < allOptions.size(); pirateChoices++) {
                            String option = allOptions.get(pirateChoices).getText();
                            Utils.logMessage("Here's the pirate option: ~" + option.toLowerCase() + "~");
                            Utils.logMessage("Here's our pirate answer: ~" + winningPirate.toLowerCase().trim() + "~");
                            if (option.toLowerCase().contains(winningPirate.toLowerCase().trim())) {
                                Utils.logMessage("About to select our pirate");
                                new Select(driver.findElement(By.name("winner" + (xPathValue - 2)))).selectByVisibleText(option);
                                Utils.logMessage("Select our pirate");
                                break;
                            }
                        }
                        Utils.logMessage("done putting together bet");

                    }
                    driver.findElement(By.name("bet_amount")).clear();
                    driver.findElement(By.name("bet_amount")).sendKeys(betAmount);

                    driver.findElement(By.xpath("//input[@value='Place this bet!']")).click();
                    driver.get("http://www.neopets.com/pirates/foodclub.phtml?type=bet");

                }

                Utils.logMessage("Successfully ending 1 runFoodClub");
                return true;
            }

            if (Utils.isElementPresentXP("/html/body/center[3]/p[1]/font", driver)) {
                Utils.logMessage("Successfully ending 2 runFoodClub");
                return true;
            }
        }
        Utils.logMessage("Failed ending runFoodClub, but we have to return true");
        return true;

    }
}