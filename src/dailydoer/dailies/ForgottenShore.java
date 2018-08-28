package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.WebDriver;

public class ForgottenShore
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Forgotten Shore...");
        driver.get("http://www.neopets.com/pirates/forgottenshore.phtml");
        String forgottenSource = driver.getPageSource();

        if (forgottenSource.contains("?confirm=1"))
        {
            forgottenSource = forgottenSource.substring(forgottenSource.indexOf("?confirm=1"), forgottenSource.indexOf("\"><div id=\"shore_"));
            driver.get("http://www.neopets.com/pirates/forgottenshore.phtml" + forgottenSource.replace("&amp;", "&"));
            Utils.sleepMode(5000);
            Utils.logMessage("Found something at the shore.");
            return true;
        }
        else {
            Utils.logMessage("Nothing at the shore today.");
            return true;
        }
    }
}