package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.WebDriver;

public class MonthlyFreeby
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runMonthlyFreeby");
        driver.get("http://www.neopets.com/freebies/index.phtml");

        if (Utils.isElementPresentXP("//*[@id=\"header\"]/table/tbody/tr[1]/td[1]/a/img", driver)) {
            Utils.logMessage("Successfully ending runMonthlyFreeby");
            return true;
        }
        Utils.logMessage("Failed ending runMonthlyFreeby");
        return false;
    }
}
