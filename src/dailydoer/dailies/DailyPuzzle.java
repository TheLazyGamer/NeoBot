package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DailyPuzzle
{
    public static boolean run(WebDriver driver) throws IOException
    {
        Utils.logMessage("Running Daily Puzzle...");
        String resultLine = "";

        try {
            URL apiUrl = new URL("http://www.jellyneo.net/?go=dailypuzzle");
            HttpURLConnection apiCon = (HttpURLConnection) apiUrl.openConnection();
            apiCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
            InputStream apiInStream = apiCon.getInputStream();
            InputStreamReader apiInStreamReader = new InputStreamReader(apiInStream);
            BufferedReader apiBufRead = new BufferedReader(apiInStreamReader);

            while ((resultLine = apiBufRead.readLine()) != null) {
                if (resultLine.contains("Answer:")) {
                    resultLine = resultLine.substring(resultLine.indexOf("blue") + 6, resultLine.lastIndexOf("<")).trim();
                    break;
                }
            }

        }
        catch (Exception ex) {
            return false;
        }

        driver.get("http://www.neopets.com/community/index.phtml");

        List<WebElement> allOptions = new Select(driver.findElement(By.name("trivia_response"))).getOptions();
        for (int x = 0; x < allOptions.size(); x++) {
            String option = allOptions.get(x).getText();
            Utils.logMessage("Here's the option: ~" + option.toLowerCase() + "~");
            Utils.logMessage("Here's our answer: ~" + resultLine.toLowerCase().trim() + "~");
            if (option.toLowerCase().contains(resultLine.toLowerCase().trim())) {
                new Select(driver.findElement(By.name("trivia_response"))).selectByVisibleText(option);
                Utils.logMessage("About to win money!");
                driver.findElement(By.name("submit")).click();
                break;
            }
        }

        if (!Utils.isElementPresentName("submit", driver)) {
            Utils.logMessage("Finished Daily Puzzle.");
            return true;
        }
        Utils.logMessage("Failed ending Daily Puzzle. Something went wrong");
        return false;
    }
}