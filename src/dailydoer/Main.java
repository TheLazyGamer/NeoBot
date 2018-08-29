package dailydoer;

import dailydoer.handlers.*;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

public class Main
{
    public static void main(String[] argv) throws Exception
    {
        User.IGNORED_ITEMS.add("Dubloon");
        User.IGNORED_ITEMS.add("Codestone");
        User.IGNORED_ITEMS.add("Birthday Cupcake");
        User.IGNORED_ITEMS.add(" Map");
        User.IGNORED_ITEMS.add(" map");
        User.IGNORED_ITEMS.add(" Paint Brush");
        User.IGNORED_ITEMS.add("Bottled ");
        User.IGNORED_ITEMS.add("Transmogrification");

        while (true) {
            if (!User.RUNNING_ON_LAPTOP) {
                Runtime.getRuntime().exec("killall chromedriver");
            }
            WebDriver driver = null;
            try {

                GregorianCalendar gc = new GregorianCalendar();
                int hour = gc.get(Calendar.HOUR_OF_DAY);
                Utils.logMessage("Done sleeping for now at hour: " + hour);

                if (hour >= 4 && hour <= 23) {

                    String baseUrl = "http://www.neopets.com/";

                    if (User.RUNNING_ON_LAPTOP) {
                        String current = new java.io.File(".").getCanonicalPath();
                        ProfilesIni profile = new ProfilesIni();
                        FirefoxProfile myprofile = profile.getProfile("Neo1");
                        myprofile.setPreference("browser.download.folderList", 2);
                        myprofile.setPreference("browser.download.manager.showWhenStarting", false);
                        myprofile.setPreference("browser.download.dir", current);
                        myprofile.setPreference("browser.helperApps.alwaysAsk.force", false);
                        myprofile.setPreference("browser.download.manager.alertOnEXEOpen", false);
                        myprofile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/msword,application/csv,text/csv,image/png ,image/jpeg, application/pdf, text/html,text/plain,application/octet-stream");
                        myprofile.setPreference("browser.cache.disk.enable", false);
                        myprofile.setPreference("browser.cache.disk.smart_size.enabled", false);
                        myprofile.setPreference("browser.cache.disk.capacity", 0);
                        FirefoxBinary binary = new FirefoxBinary(new File("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe"));
                        driver = new FirefoxDriver(binary, myprofile);
                    }
                    else {
						/* ChromeDriverService service = new ChromeDriverService.Builder()
						.usingDriverExecutable(new File("/usr/local/bin/chromedriver"))
						.usingAnyFreePort()
						.withEnvironment(ImmutableMap.of("DISPLAY",":20"))
						.build();
						service.start();

						System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
						ChromeOptions chOption = new ChromeOptions();
						chOption.addArguments("User-data-dir=/home/pi/.config/chromium");
						driver = new ChromeDriver(chOption); //Was service, chOption
						driver.manage().window().maximize(); */
                    }

                    driver.get(baseUrl);
                    if (Utils.isElementPresentLT("Log in", driver)) {
                        driver.findElement(By.linkText("Log in")).click();
                        driver.findElement(By.id("templateLoginPopupUsername")).clear();
                        driver.findElement(By.id("templateLoginPopupUsername")).sendKeys(User.USERNAME);
                        driver.findElement(By.name("password")).clear();
                        driver.findElement(By.name("password")).sendKeys(User.PASSWORD);
                        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
                    }

                    if (User.RUNNING_ON_LAPTOP) {
                        Runtime.getRuntime().exec("../SetupWindows.bat");
                    }
                    Utils.sleepMode(10000);

                    OncePerDay.run(driver, "Bank", "EEEE");

                    OncePerDay.run(driver, "Battledome", "EEEE");

                    int snowAvailable[] = {6, 14, 22}; //Hours when asleep
                    OncePerSchedule.run(driver, "Snowager", snowAvailable); //Must run after battledome

                    OncePerInterval.run(driver, "SellStock", 1800);

                    OncePerDay.run(driver, "BuyStock", "EEEE");

                    //OncePerDay.run(driver, "Coincidence", "EEEE");

                    OncePerDay.run(driver, "Lottery", "EEEE");

                    OncePerDay.run(driver, "FoodClub", "EEEE");

                    //OncePerDay.run(driver, "LabRay", "EEEE");

                    OncePerDay.run(driver, "Cupcake", "EEEE");

                    OncePerDay.run(driver, "GrundoPlushie", "EEEE");

                    OncePerInterval.run(driver, "Employment", 3600);

                    //OncePerInterval.run(driver, "KitchenQuest", 3600);

                    OncePerDay.run(driver, "Tombola", "EEEE");

                    OncePerDay.run(driver, "Omelette", "EEEE");

                    OncePerDay.run(driver, "Jelly", "EEEE");

                    OncePerDay.run(driver, "AltadorCouncil", "EEEE");

                    OncePerDay.run(driver, "NeggCave", "EEEE");

                    OncePerDay.run(driver, "ShopOfOffers", "EEEE");

                    OncePerDay.run(driver, "AnchorManagement", "EEEE");

                    OncePerDay.run(driver, "FruitMachine", "EEEE");

                    //OncePerDay.run(driver, "PotatoCounter", "EEEE");

                    //OncePerDay.run(driver, "KacheekSeek", "EEEE");

                    OncePerDay.run(driver, "WiseOldKing", "EEEE");

                    OncePerDay.run(driver, "GrumpyOldKing", "EEEE");

                    OncePerDay.run(driver, "DailyPuzzle", "EEEE");

                    OncePerDay.run(driver, "Crossword", "EEEE");

                    OncePerDay.run(driver, "LunarPuzzle", "EEEE");

                    OncePerDay.run(driver, "Cliffhanger", "EEEE");

                    OncePerDay.run(driver, "TrudysSurprise", "EEEE");

                    OncePerDay.run(driver, "Expellibox", "EEEE");

                    OncePerDay.run(driver, "GeraptikuTomb", "EEEE");

                    OncePerDay.run(driver, "ForgottenShore", "EEEE");

                    OncePerDay.run(driver, "Meteor", "EEEE");

                    OncePerDay.run(driver, "IslandMystic", "EEEE");

                    //OncePerDay.run(driver, "MoodImprove", "EEEE");

                    OncePerDay.run(driver, "Faeries", "EEEE");

                    //OncePerDay.run(driver, "Advent", "EEEE");

                    OncePerDay.run(driver, "KikoPop", "EEEE");

                    OncePerDay.run(driver, "MonthlyFreeby", "MMM");

                    OncePerInterval.run(driver, "NeoMail", (long) 3600);

                    OncePerInterval.run(driver, "GuessMarrow", (long) 86400);

                    OncePerInterval.run(driver, "SymolHole", (long) 3600);

                    OncePerInterval.run(driver, "ColtzansShrine", (long) 43200);

                    OncePerInterval.run(driver, "WishingWell", (long) 43200);

                    OncePerInterval.run(driver, "Fishing", (long) 43200);

                    OncePerInterval.run(driver, "GraveDanger", (long) 36000);

                    //OncePerInterval.run(driver, "DubloonTraining", (long) 3600);

                    OncePerInterval.run(driver, "IslandTraining", (long) 3600);

                    OncePerInterval.run(driver, "SecondHandShoppe", (long) 1800);

                    OncePerInterval.run(driver, "Tarla", (long) 1800);

                    OncePerInterval.run(driver, "BuriedTreasure", (long) 10800);

                    OncePerInterval.run(driver, "ShopTill", (long) 1800);

                    int collectTime[] = {22}; //Hour when we collect winnings
                    OncePerSchedule.run(driver, "CollectWinnings", collectTime);

                    int depositTime[] = {22}; //Hour when we deposit money on hand
                    OncePerSchedule.run(driver, "Deposit", depositTime);

                    OncePerInterval.run(driver, "DeleteItems", (long) 1800);

                    //OncePerInterval.run(driver, "RepriceItems", (long) 172800);

                    //OncePerInterval.run(driver, "StockItems", (long) 1800);

                    OncePerDay.run(driver, "AppleBobbing", "EEEE");

                    //OncePerInterval.run(driver, "WheelOfMisfortune", (long) 7200);

                    OncePerInterval.run(driver, "WheelOfMediocrity", (long) 2400);

                    OncePerInterval.run(driver, "WheelOfExcitement", (long) 7200);

                    OncePerDay.run(driver, "WheelOfKnowledge", "EEEE");

                    OncePerInterval.run(driver, "HealingSprings", (long) 1800);

                    OncePerDay.run(driver, "PetPetBattles", "EEEE");

                    //OncePerDay.run(driver, "Pyramids", "EEEE"); //5k per day

                    //OncePerDay.run(driver, "Cheat", "EEEE"); //10k per day

                    //OncePerDay.run(driver, "Solitaire", "EEEE"); //5k per day

                    //OncePerDay.run(driver, "Scarab21", "EEEE"); //5k per day

                    driver.quit();
                }
                else {
                    //It's night, time to sleep until morning
                    Utils.logMessage("Hibernate for the night");
                    Utils.sleepMode(1920000); //Sleep 32 minutes
                }
                Utils.sleepMode(1920000); //Sleep 32 minutes
            }
            catch(Exception ex) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                String sStackTrace = sw.toString();
                Utils.logMessage("Hit some exception:");
                Utils.logMessage(sStackTrace);
                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                if (User.RUNNING_ON_LAPTOP) {
                    FileUtils.copyFile(scrFile, new File(Instant.now().getEpochSecond() + "_" + ex.getClass().getCanonicalName().replace(".", "_") + ".jpg"));
                    if (!sStackTrace.contains("NoSuchElementException")) {
                        Utils.sendEmail(sStackTrace.replace("\"", "_"));
                    }
                }
                else {
                    FileUtils.copyFile(scrFile, new File(Instant.now().getEpochSecond() + ".jpg"));
                }
                driver.quit();
                Utils.sleepMode(300000); //Sleep 5 minutes
            }
        }
    }
}