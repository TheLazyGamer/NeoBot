package dailydoer.dailies;

import dailydoer.User;
import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Battledome
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Fighting in the Battledome...");
        driver.get("http://www.neopets.com/dome/fight.phtml");

        int petIndex = 1;

        for (; Utils.isElementPresentXP("//*[@id=\"bdFightPetInfo\"]/div[" + petIndex + "]", driver); petIndex++) {
            String possiblePetName = driver.findElement(By.xpath("//*[@id=\"bdFightPetInfo\"]/div[" + petIndex + "]/div[4]")).getText();
            if (possiblePetName.contains(User.PETNAME)) {
                break;
            }
        }

        if (Utils.isElementPresentXP("//*[@id=\"bdFightPetInfo\"]/div[" + petIndex + "]/div[2]/div[17]", driver))
        {
            String eligibleText = driver.findElement(By.xpath("//*[@id=\"bdFightPetInfo\"]/div[" + petIndex + "]/div[2]/div[17]")).getText();
            Utils.logMessage("eligibleText: " + eligibleText);

            if (eligibleText.contains("This Neopet is ready to fight!"))
            {
                //Click continue this pet
                driver.findElement(By.xpath("//*[@id=\"bdFightStep1\"]/div[3]")).click();
                //Click continue 1-P
                driver.findElement(By.xpath("//*[@id=\"bdFightStep2\"]/div[4]")).click();
                //Select our opponent
                int opponentIndex = 3;

                for (; Utils.isElementPresentXP("//*[@id=\"npcTable\"]/tbody/tr[" + opponentIndex + "]/td[2]", driver); opponentIndex++)
                {
                    String opponentName = driver.findElement(By.xpath("//*[@id=\"npcTable\"]/tbody/tr[" + opponentIndex + "]/td[2]")).getText();
                    if (opponentName.contains(User.BD_OPPONENT))
                    {
                        break;
                    }
                }
                driver.findElement(By.xpath("//*[@id=\"npcTable\"]/tbody/tr[" + opponentIndex + "]/td[4]/div[1]")).click();
                //Start the battle page
                driver.findElement(By.id("bdFightStep3FightButton")).click();
                Utils.sleepMode(5000);
            }
            else {
                Utils.logMessage("Pet was not eligible to fight.");
                return false;
            }
        }

        //If already fighting
        if (Utils.isElementPresentXP("//*[@id=\"start\"]/div", driver))
        {
            while (true)
            {
                //Start the battle by clicking Fight
                driver.findElement(By.xpath("//*[@id=\"start\"]/div")).click();
                //Click the first equip slot
                Utils.sleepMode(5000);
                driver.findElement(By.xpath("//*[@id=\"p1e1m\"]/div")).click();
                //Select Turned Tooth #1
                Utils.sleepMode(2000);
                driver.findElement(By.xpath("//*[@id=\"p1equipment\"]/div[3]/ul/li[1]/img")).click();
                //Click the second equip slot
                Utils.sleepMode(2000);
                driver.findElement(By.xpath("//*[@id=\"p1e2m\"]/div")).click();
                //Select Turned Tooth #2
                Utils.sleepMode(2000);
                driver.findElement(By.xpath("//*[@id=\"p1equipment\"]/div[3]/ul/li[2]/img")).click();
                //Click the faerie ability slot
                Utils.sleepMode(2000);
                driver.findElement(By.xpath("//*[@id=\"p1am\"]/div")).click();
                //Click lens flare
                Utils.sleepMode(2000);
                driver.findElement(By.xpath("//*[@id=\"p1ability\"]/div[3]/table/tbody/tr/td[1]/div/div")).click();
                //Click Fight to kill opponent
                Utils.sleepMode(2000);
                driver.findElement(By.id("fight")).click();
                //Click Collect Rewards
                int counter = 0;
                while (!Utils.isElementPresentXP("//*[@id=\"playground\"]/div[2]/button[1]", driver) && counter < 15)
                {
                    counter++;
                    Utils.sleepMode(1000);
                }
                driver.findElement(By.xpath("//*[@id=\"playground\"]/div[2]/button[1]")).click();
                //Click Play Again
                Utils.sleepMode(5000);
                String rewardText = driver.findElement(By.id("bd_rewards")).getText();
                if (rewardText.contains("You have reached the item limit for today!") || rewardText.contains("You have reached the NP limit for today!"))
                {
                    break;
                }
                driver.findElement(By.id("bdplayagain")).click();
            }
            Utils.logMessage("Finished fighting in the Battledome.");
            return true;
        }
        else {
            Utils.logMessage("Couldn't fight in the Battledome. Something went wrong.");
            return false;
        }
    }
}