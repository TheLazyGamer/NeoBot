package dailydoer.dailies;

import dailydoer.User;
import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class LabRay
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runLabRay");
        driver.get("http://www.neopets.com/lab2.phtml");
        List<WebElement> zapPets = driver.findElements(By.xpath("//input[@value='" + User.PET_ZAPPED + "']"));

        for (int x = 0; x < zapPets.size(); x++) {
            WebElement pet = zapPets.get(x);
            try {
                pet.click();
                x = 99999;
            }
            catch(ElementNotVisibleException ex) {

            }
        }
        driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/center/input")).click();

        if (Utils.isElementPresentXP("/html/body/center/p[3]", driver)) {
            String firstHalfMessage = driver.findElement(By.xpath("/html/body/center/p[1]")).getText().replace("\n", " ").trim();
            String secondHalfMessage = driver.findElement(By.xpath("/html/body/center/p[3]")).getText().replace("\n", " ").trim();
            String message = firstHalfMessage + secondHalfMessage;
            Utils.sendEmail(message);
        }
        Utils.logMessage("Successfully ending runLabRay");
        return true;
    }
}