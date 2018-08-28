package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PetPetBattles
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running PetPet Battles...");
        driver.get("http://www.neopets.com/games/petpet_battle/index.phtml");

        driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div/p[2]/a[2]")).click();

        String previousOpponentHP = "";
        String previousMove = "Body Blow";

        while (true) {
            String opponentHP = "";
            //Random ran = new Random();
            //int x = ran.nextInt(1000);
            //sleepMode(1000+x);
            if (driver.getPageSource().contains("You have played more")) {
                Utils.sleepMode(10000);
                break;
            }

            if (driver.getPageSource().contains("Click to play a new game")) {
                driver.findElement(By.xpath("//input[@value='New Game']")).click();
            }

            opponentHP = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table[1]/tbody/tr[6]/td[2]/b")).getText();
            int opponentHPNumber = Integer.parseInt(opponentHP.split("\n")[1].substring(0, opponentHP.split("\n")[1].indexOf("%") - 1));

            if (driver.getPageSource().contains("Shield Left:") && opponentHPNumber <= 20) {
                driver.findElement(By.xpath("//input[@value='Shield']")).click();
            }
            else if (!opponentHP.equals(previousOpponentHP)) {
                previousOpponentHP = opponentHP;
                driver.findElement(By.xpath("//input[@value='" + previousMove + "']")).click();
            }
            else {
                previousOpponentHP = opponentHP;
                if (previousMove.equals("Body Blow")) {
                    previousMove = "Head Shot";
                }
                else {
                    previousMove = "Body Blow";
                }
                driver.findElement(By.xpath("//input[@value='" + previousMove + "']")).click();
            }
        }
        Utils.logMessage("Finished PetPet Battles.");
        return true;
    }
}