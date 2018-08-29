package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cheat
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runCheat");
        driver.get("http://www.neopets.com/games/cheat/index.phtml");

        if (Utils.isElementPresentXP("//input[@value='Continue playing Cheat!']", driver)) {
            driver.findElement(By.xpath("//input[@value='Continue playing Cheat!']")).click();
        }
        else {
            driver.findElement(By.xpath("//input[@value='Start a new game (costs 50 NP)']")).click();
        }

        if (Utils.isElementPresentXP("//input[@value='Click to Continue']", driver)) {
            driver.findElement(By.xpath("//input[@value='Click to Continue']")).click();
        }
        else if (Utils.isElementPresentXP("//input[@value='Click here to start over']", driver)) {
            driver.findElement(By.xpath("//input[@value='Click here to start over']")).click();
        }
        else if (Utils.isElementPresentXP("//input[@value='Play Again']", driver)) {
            driver.findElement(By.xpath("//input[@value='Play Again']")).click();
            driver.findElement(By.xpath("//input[@value='Click here to begin the game!']")).click();
        }
        else if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/b/form[1]/input[2]", driver)) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/b/form[1]/input[2]")).click();
            driver.findElement(By.xpath("//input[@value='Click here to begin the game!']")).click();
        }

        while (true) {

            //Random ran = new Random();
            //int pause = ran.nextInt(1000);
            //sleepMode(2000+pause);
            int[] ourCards = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

            for (int x = 1; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/center/table/tbody/tr[" + x + "]", driver); x++) {
                for (int y = 1; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/center/table/tbody/tr[" + x + "]/td[" + y + "]/center/a/img", driver); y++) {
                    String card = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/center/table/tbody/tr[" + x + "]/td[" + y + "]/center/a/img")).getAttribute("src");
                    card = card.substring(card.lastIndexOf("/") + 1, card.indexOf("_"));
                    if (card.equals("2")) {
                        ourCards[0]++;
                    }
                    else if (card.equals("3")) {
                        ourCards[1]++;
                    }
                    else if (card.equals("4")) {
                        ourCards[2]++;
                    }
                    else if (card.equals("5")) {
                        ourCards[3]++;
                    }
                    else if (card.equals("6")) {
                        ourCards[4]++;
                    }
                    else if (card.equals("7")) {
                        ourCards[5]++;
                    }
                    else if (card.equals("8")) {
                        ourCards[6]++;
                    }
                    else if (card.equals("9")) {
                        ourCards[7]++;
                    }
                    else if (card.equals("10")) {
                        ourCards[8]++;
                    }
                    else if (card.equals("11")) {
                        ourCards[9]++;
                    }
                    else if (card.equals("12")) {
                        ourCards[10]++;
                    }
                    else if (card.equals("13")) {
                        ourCards[11]++;
                    }
                    else if (card.equals("14")) {
                        ourCards[12]++;
                    }
                }
            }

            if (Utils.isElementPresentName("x_claim", driver)) {
                WebElement selectElement = driver.findElement(By.name("x_claim"));
                Select select = new Select(selectElement);
                List<WebElement> allOptions = select.getOptions();
                ArrayList<String> allOptionsList = new ArrayList<String>();
                for (int x = 0; x < allOptions.size(); x++) {
                    allOptionsList.add(allOptions.get(x).getText().trim());
                }

                int maxIndex = -1;
                for (int i = 0; i < ourCards.length; i++) {
                    int newNumber = ourCards[i];
                    boolean dropdownContainsNewNumber = false;
                    for (int x = 1; x < allOptionsList.size(); x++) {
                        String option = allOptionsList.get(x);
                        if (option.equals("Ace")) {
                            option = "14";
                        }
                        else if (option.equals("Jack")) {
                            option = "11";
                        }
                        else if (option.equals("Queen")) {
                            option = "12";
                        }
                        else if (option.equals("King")) {
                            option = "13";
                        }
                        if (option.equals(String.valueOf(i + 2)) && newNumber > 0) {
                            dropdownContainsNewNumber = true;
                            break;
                        }
                    }
                    if (dropdownContainsNewNumber) {
                        if (maxIndex == -1 || newNumber > ourCards[maxIndex]) {
                            maxIndex = i;
                        }
                        dropdownContainsNewNumber = false;
                    }
                }

                if (maxIndex > -1) {
                    for (int x = 1; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/center/table/tbody/tr[" + x + "]", driver); x++) {
                        for (int y = 1; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/center/table/tbody/tr[" + x + "]/td[" + y + "]/center/a/img", driver); y++) {
                            String card = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/center/table/tbody/tr[" + x + "]/td[" + y + "]/center/a/img")).getAttribute("src");
                            card = card.substring(card.lastIndexOf("/") + 1, card.indexOf("_"));
                            if (card.equals(String.valueOf(maxIndex + 2))) {
                                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/center/table/tbody/tr[" + x + "]/td[" + y + "]/center/a/img")).click();
                            }
                        }
                    }

                    for (int x = 0; x < allOptionsList.size(); x++) {
                        String option = allOptionsList.get(x);
                        if (option.equals("Ace")) {
                            option = "14";
                        }
                        else if (option.equals("Jack")) {
                            option = "11";
                        }
                        else if (option.equals("Queen")) {
                            option = "12";
                        }
                        else if (option.equals("King")) {
                            option = "13";
                        }
                        if (option.equals(String.valueOf(maxIndex + 2))) {
                            Utils.logMessage("Matching option: " + option);
                            new Select(driver.findElement(By.name("x_claim"))).selectByVisibleText(allOptionsList.get(x));
                            break;
                        }
                    }
                }
                else { //maxIndex never changed, meaning we don't have a valid card. Just play one cheat card.
                    Utils.logMessage("We don't have a valid card. Have to cheat.");
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/center/table/tbody/tr[1]/td[1]/center/a/img")).click();
                    new Select(driver.findElement(By.name("x_claim"))).selectByVisibleText(driver.findElement(By.xpath("//*[@name='x_claim']/option[2]")).getText());
                }

                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/form/input[6]")).click();

                if (Utils.isElementPresentXP("//input[@value='Click to Continue']", driver)) {
                    driver.findElement(By.xpath("//input[@value='Click to Continue']")).click();
                }
                else if (Utils.isElementPresentXP("//input[@value='Click here to start over']", driver)) {
                    driver.findElement(By.xpath("//input[@value='Click here to start over']")).click();
                }
                else if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/form[1]/input[2]", driver)) {
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/form[1]/input[2]")).click();
                    driver.findElement(By.xpath("//input[@value='Click here to begin the game!']")).click();
                }
                else if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/b/form[1]/input[2]", driver)) {
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/b/form[1]/input[2]")).click();
                    driver.findElement(By.xpath("//input[@value='Click here to begin the game!']")).click();
                }
                else if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/form/b/input[2]", driver)) {
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/form/b/input[2]")).click();
                    driver.findElement(By.xpath("//input[@value='Click here to begin the game!']")).click();
                }
            }

            else {
                HashMap<String,Integer> map = new HashMap<String,Integer>();

                for (int x = 1; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[1]/td[" + x + "]", driver); x++) {
                    String playerName = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[1]/td[" + x + "]")).getText().trim();
                    String[] playerData = playerName.split("\n");
                    map.put(playerData[0], Integer.parseInt(playerData[1].substring(playerData[1].indexOf(" ") + 1, playerData[1].indexOf("c") - 1)));
                    Utils.logMessage("Current opponents include: " + playerName);
                }

                String opponentPlay = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/center[1]/font/b")).getText().trim();
                String opponentCardPlayed = opponentPlay.substring(opponentPlay.lastIndexOf(" "), opponentPlay.length()).trim();
                opponentPlay = opponentPlay.substring(0, opponentPlay.lastIndexOf(" ")).trim();
                int opponentAmountOfCardsPlayed = Integer.parseInt(opponentPlay.substring(opponentPlay.lastIndexOf(" "), opponentPlay.length()).trim());
                opponentPlay = opponentPlay.substring(0, opponentPlay.lastIndexOf(" ")).trim();
                opponentPlay = opponentPlay.substring(0, opponentPlay.lastIndexOf(" ")).trim();
                Utils.logMessage("opponentPlay: " + opponentPlay);
                Utils.logMessage("opponentCardPlayed: " + opponentCardPlayed);
                Utils.logMessage("opponentAmountOfCardsPlayed: " + opponentAmountOfCardsPlayed);

                boolean callBluff = false;

                int ourCardsIndexToReference = 0;

                if (opponentCardPlayed.contains("Ace")) {
                    ourCardsIndexToReference = 12;
                }
                if (opponentCardPlayed.contains("two")) {
                    ourCardsIndexToReference = 0;
                }
                if (opponentCardPlayed.contains("three")) {
                    ourCardsIndexToReference = 1;
                }
                if (opponentCardPlayed.contains("four")) {
                    ourCardsIndexToReference = 2;
                }
                if (opponentCardPlayed.contains("five")) {
                    ourCardsIndexToReference = 3;
                }
                if (opponentCardPlayed.contains("six")) {
                    ourCardsIndexToReference = 4;
                }
                if (opponentCardPlayed.contains("seven")) {
                    ourCardsIndexToReference = 5;
                }
                if (opponentCardPlayed.contains("eight")) {
                    ourCardsIndexToReference = 6;
                }
                if (opponentCardPlayed.contains("nine")) {
                    ourCardsIndexToReference = 7;
                }
                if (opponentCardPlayed.contains("ten")) {
                    ourCardsIndexToReference = 8;
                }
                if (opponentCardPlayed.contains("Jack")) {
                    ourCardsIndexToReference = 9;
                }
                if (opponentCardPlayed.contains("Queen")) {
                    ourCardsIndexToReference = 10;
                }
                if (opponentCardPlayed.contains("King")) {
                    ourCardsIndexToReference = 11;
                }

                if (ourCards[ourCardsIndexToReference] + opponentAmountOfCardsPlayed > 4) {
                    callBluff = true;
                }

                int opponentCardsInHand = (int) map.get(opponentPlay);

                if ((opponentAmountOfCardsPlayed == 4 && opponentCardsInHand + opponentAmountOfCardsPlayed <= 12) ||
                        (opponentAmountOfCardsPlayed == 3 && opponentCardsInHand + opponentAmountOfCardsPlayed <= 7) ||
                        (opponentAmountOfCardsPlayed == 2 && opponentCardsInHand + opponentAmountOfCardsPlayed <= 5) ||
                        opponentCardsInHand == 0) {
                    callBluff = true;
                }

                if (callBluff) {
                    driver.findElement(By.xpath("//input[@value='Accuse " + opponentPlay + " of cheating!']")).click();
                    if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center", driver)) {
                        String moneyMessage = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center")).getText();
                        if (moneyMessage.contains("You have already won the maximum neopoints for today.")) {
                            break;
                        }
                    }
                }
                else {
                    driver.findElement(By.xpath("//input[@value='Let " + opponentPlay + " slide']")).click();
                }

                if (Utils.isElementPresentXP("//input[@value='Click to Continue']", driver)) {
                    driver.findElement(By.xpath("//input[@value='Click to Continue']")).click();
                }
                else if (Utils.isElementPresentXP("//input[@value='Play Again']", driver)) {
                    driver.findElement(By.xpath("//input[@value='Play Again']")).click();
                    driver.findElement(By.xpath("//input[@value='Click here to begin the game!']")).click();
                }

            }

        }

        Utils.logMessage("Successfully ending runCheat");
        return true;
    }
}