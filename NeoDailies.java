import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

/**
 * The main class which embodies the entire program.
 */
public class NeoDailies {

	public static final String YOUR_EMAIL = "CHANGE_ME@gmail.com"; //Change this to your email
	public static final String YOUR_EMAIL_PASSWORD = "CHANGE_ME"; //Change this to your email's password
	public static final String NUMBER_TO_TEXT = "5551234567@tmomail.net"; //Your phone number and carrier. See the README to change this
	public static final String WINDOWS_USER = "CHANGE_ME"; //Change this to your windows user account name.
	public static final String USERNAME = "CHANGE_ME"; //Change this to your neopets account username
	public static final String PASSWORD = "CHANGE_ME"; //Change this to your neopets account password
	public static final String PETNAME = "CHANGE_ME"; //Change this to your main pet's name on your neopets account
	public static final String PET_ZAPPED = "CHANGE_ME"; //Change this to your lab rat's name
	public static final String WISHING_WELL_ITEM = "Turned Tooth"; //Shouldn't need to be changed. This is the item you wish for.
	public static final String BD_OPPONENT = "Giant Spectral Mutant Walein"; //Change this as needed to the opponent you fight.
	public static final boolean RUNNING_ON_LAPTOP = true; //Shouldn't need to be changed. This is for my (TheLazyGamer) own convenience to run it on my Pi.
	public static final int GRAVE_PETPET = 1; //Change this as needed. This is the index of the petpet. 1 would be your first pet's petpet.
	public static final int MINIMUM_KEEP_VALUE = 100; //Change this as needed. This is the minimum NP value where items in your inventory won't be donated.

	//These shouldn't need changing, but the costs may need occasional updating. Only used for kitchen quest.
	public static final int AVERAGE_ONE_DUBLOON_COST = 700;
	public static final int AVERAGE_TWO_DUBLOON_COST = 650;
	public static final int AVERAGE_FIVE_DUBLOON_COST = 1800;
	public static final int AVERAGE_CODESTONE_COST = 8000;
	public static final int AVERAGE_RED_CODESTONE_COST = 29500;
	public static final int USEFUL_KQ_STAT_REWARD_PCT = 35; //35 for every stat except speed. 12 for hp. 8 for lvl

	//Needed for Solitaire. DO NOT TOUCH!
	public static boolean madeMove = false;
	public static int cardDiv = 2;
	public static boolean firstMove = true;

	public static ArrayList<String> shopURLs = new ArrayList<String>();
	public static FileWriter logWriter = null;
	public static final ArrayList<String> IGNORED_ITEMS = new ArrayList<String>();

	/**
	 * The main method. Contains the loop where everything runs.
	 * It is the controller for all the automation.
	 * @param argv Not used, never used. Just Java syntax.
	 * @throws Exception This should never happen, but Java requires it.
	 */
	public static void main(String[] argv) throws Exception {
		/*IGNORED_ITEMS.add("Dubloon");
		IGNORED_ITEMS.add("Codestone");*/
		IGNORED_ITEMS.add("Birthday Cupcake");
		IGNORED_ITEMS.add(" Map");
		IGNORED_ITEMS.add(" map");
		IGNORED_ITEMS.add("Wraith Ectoplasm");
		IGNORED_ITEMS.add("Taelias Concoction");

		File logFile = new File("BotLog.log");
		logWriter = new FileWriter(logFile, true);

		while (true) {
			if (!RUNNING_ON_LAPTOP) {
				Runtime.getRuntime().exec("killall chromedriver");
			}
			WebDriver driver = null;
			try {

				GregorianCalendar gc = new GregorianCalendar();
				int hour = gc.get(Calendar.HOUR_OF_DAY);
				logMessage("Done sleeping for now at hour: " + hour);

				if (hour >= 8 && hour <= 22) {

					String baseUrl = "http://www.neopets.com/";

					if (RUNNING_ON_LAPTOP) {
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
						chOption.addArguments("user-data-dir=/home/pi/.config/chromium");
						driver = new ChromeDriver(chOption); //Was service, chOption
						driver.manage().window().maximize(); */
					}

					driver.get(baseUrl);
					if (isElementPresentLT("Log in", driver)) {
						driver.findElement(By.linkText("Log in")).click();
						driver.findElement(By.id("templateLoginPopupUsername")).clear();
						driver.findElement(By.id("templateLoginPopupUsername")).sendKeys(USERNAME);
						driver.findElement(By.name("password")).clear();
						driver.findElement(By.name("password")).sendKeys(PASSWORD);
						driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
					}

					if (RUNNING_ON_LAPTOP) {
						Runtime.getRuntime().exec("SetupWindows.bat");
					}
					sleepMode(10000);

					oncePerDay(driver, "Bank", "EEEE");

					oncePerInterval(driver, "StockSell", 1800);

					oncePerDay(driver, "Stocks", "EEEE");

					//oncePerDay(driver, "Coincidence", "EEEE");

					//oncePerDay(driver, "Lottery", "EEEE");

					oncePerDay(driver, "FoodClub", "EEEE");

					oncePerDay(driver, "LabRay", "EEEE");

					oncePerDay(driver, "Cupcake", "EEEE");

					oncePerDay(driver, "TDMBGPOP", "EEEE");

					oncePerDay(driver, "Battledome", "EEEE");

					oncePerInterval(driver, "Employment", 3600);

					//oncePerInterval(driver, "KitchenQuest", 3600);

					oncePerDay(driver, "Tombola", "EEEE");

					oncePerDay(driver, "Omelette", "EEEE");

					oncePerDay(driver, "Jelly", "EEEE");

					oncePerDay(driver, "AltadorCouncil", "EEEE");

					oncePerDay(driver, "NeggCave", "EEEE");

					oncePerDay(driver, "ShopOfOffers", "EEEE");

					oncePerDay(driver, "AnchorManagement", "EEEE");

					oncePerDay(driver, "FruitMachine", "EEEE");

					oncePerDay(driver, "PotatoCounter", "EEEE");

					oncePerDay(driver, "KacheekSeek", "EEEE");

					oncePerDay(driver, "WiseOldKing", "EEEE");

					oncePerDay(driver, "GrumpyOldKing", "EEEE");

					oncePerDay(driver, "DailyPuzzle", "EEEE");

					oncePerDay(driver, "Crossword", "EEEE");

					oncePerDay(driver, "LunarPuzzle", "EEEE");

					oncePerDay(driver, "Cliffhanger", "EEEE");

					oncePerInterval(driver, "TrudysSurprise", (long) 7200);

					oncePerInterval(driver, "Expellibox", (long) 7200);

					oncePerDay(driver, "GeraptikuTomb", "EEEE");

					oncePerDay(driver, "ForgottenShore", "EEEE");

					oncePerDay(driver, "Meteor", "EEEE");

					//oncePerDay(driver, "IslandMystic", "EEEE");

					oncePerDay(driver, "MoodImprove", "EEEE");

					oncePerDay(driver, "Faeries", "EEEE");

					//oncePerDay(driver, "Advent", "EEEE");

					oncePerDay(driver, "KikoPop", "EEEE");

					oncePerDay(driver, "MonthlyFreeby", "MMM");

					oncePerInterval(driver, "NeoMail", (long) 3600);

					oncePerInterval(driver, "GuessMarrow", (long) 86400);

					oncePerInterval(driver, "SymolHole", (long) 3600);

					oncePerInterval(driver, "ColtzansShrine", (long) 43200);

					oncePerInterval(driver, "WishingWell", (long) 43200);

					oncePerInterval(driver, "Fishing", (long) 43200);

					oncePerInterval(driver, "GraveDanger", (long) 36000);

					//oncePerInterval(driver, "DubloonTraining", (long) 3600);

					//oncePerInterval(driver, "IslandTraining", (long) 3600);

					oncePerInterval(driver, "SecondHandShoppe", (long) 1800);

					oncePerInterval(driver, "Tarla", (long) 1800);

					oncePerInterval(driver, "ShopTill", (long) 1800);

					int snowAvailable[] = {6, 14, 22}; //Hours when asleep
					oncePerSchedule(driver, "Snowager", snowAvailable); //Must run after battledome

					int collectTime[] = {22}; //Hour when we collect winnings
					oncePerSchedule(driver, "CollectWinnings", collectTime);

					int depositTime[] = {22}; //Hour when we deposit money on hand
					oncePerSchedule(driver, "Deposit", depositTime);

					oncePerInterval(driver, "DeleteItems", (long) 1800);

					oncePerInterval(driver, "RepriceItems", (long) 172800);

					oncePerInterval(driver, "StockItems", (long) 1800);

					oncePerDay(driver, "AppleBobbing", "EEEE");

					oncePerInterval(driver, "HealingSprings", (long) 1800);

					//oncePerDay(driver, "PetPetBattles", "EEEE");

					//oncePerDay(driver, "Pyramids", "EEEE"); //5k per day

					//oncePerDay(driver, "Cheat", "EEEE"); //10k per day

					//oncePerDay(driver, "Solitaire", "EEEE"); //5k per day

					//oncePerDay(driver, "Scarab21", "EEEE"); //5k per day

					driver.quit();
				}
				else {
					//It's night, time to sleep until morning
					logMessage("Hibernate for the night");
					sleepMode(1920000); //Sleep 32 minutes
				}
				sleepMode(1920000); //Sleep 32 minutes
			}
			catch(Exception ex) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				String sStackTrace = sw.toString(); //stacktrace as a string
				logMessage("Hit some exception:");
				logMessage(sStackTrace);
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				if (RUNNING_ON_LAPTOP) {
					FileUtils.copyFile(scrFile, new File(Instant.now().getEpochSecond() + "_" + ex.getClass().getCanonicalName().replace(".", "_") + ".jpg"));
					if (!sStackTrace.contains("NoSuchElementException")) {
						sendEmail(sStackTrace.replace("\"", "_"));
					}
				}
				else {
					FileUtils.copyFile(scrFile, new File(Instant.now().getEpochSecond() + ".jpg"));
				}
				driver.quit();
				sleepMode(300000); //Sleep 5 minutes
			}
		}
	}

	/**
	 * Handles any activities which only need to be run once per day. Is only called from main.
	 * @param driver The WebDriver
	 * @param xmlNode The name of the node in the user's PreviousRuns.xml the program is looking for
	 * @param timeFormat The SimpleDateFormat the program uses to get the current day or month. Only EEEE or MMM.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 * @see NeoDailies#oncePerSchedule(WebDriver, String, int[]) oncePerSchedule
	 * @see NeoDailies#runTombola(WebDriver) runTombola
	 * @see NeoDailies#runOmelette(WebDriver) runOmelette
	 * @see NeoDailies#runJelly(WebDriver) runJelly
	 * @see NeoDailies#runPetPetBattles(WebDriver) runPetPetBattles
	 * @see NeoDailies#runPyramids(WebDriver) runPyramids
	 * @see NeoDailies#runCheat(WebDriver) runCheat
	 * @see NeoDailies#runSolitaire(WebDriver) runSolitaire
	 * @see NeoDailies#runScarab21(WebDriver) runScarab21
	 * @see NeoDailies#runBank(WebDriver) runBank
	 * @see NeoDailies#runAltadorCouncil(WebDriver) runAltadorCouncil
	 * @see NeoDailies#runKikoPop(WebDriver) runKikoPop
	 * @see NeoDailies#runNeggCave(WebDriver) runNeggCave
	 * @see NeoDailies#runShopOfOffers(WebDriver) runShopOfOffers
	 * @see NeoDailies#runAppleBobbing(WebDriver) runAppleBobbing
	 * @see NeoDailies#runAnchorManagement(WebDriver) runAnchorManagement
	 * @see NeoDailies#runFruitMachine(WebDriver) runFruitMachine
	 * @see NeoDailies#runTDMBGPOP(WebDriver) runTDMBGPOP
	 * @see NeoDailies#runBattledome(WebDriver) runBattledome
	 * @see NeoDailies#runPotatoCounter(WebDriver) runPotatoCounter
	 * @see NeoDailies#runKacheekSeek(WebDriver) runKacheekSeek
	 * @see NeoDailies#runWiseOldKing(WebDriver) runWiseOldKing
	 * @see NeoDailies#runGrumpyOldKing(WebDriver) runGrumpyOldKing
	 * @see NeoDailies#runDailyPuzzle(WebDriver) runDailyPuzzle
	 * @see NeoDailies#runCrossword(WebDriver) runCrossword
	 * @see NeoDailies#runLunarPuzzle(WebDriver) runLunarPuzzle
	 * @see NeoDailies#runCliffhanger(WebDriver) runCliffhanger
	 * @see NeoDailies#runGeraptikuTomb(WebDriver) runGeraptikuTomb
	 * @see NeoDailies#runForgottenShore(WebDriver) runForgottenShore
	 * @see NeoDailies#runMeteor(WebDriver) runMeteor
	 * @see NeoDailies#runIslandMystic(WebDriver) runIslandMystic
	 * @see NeoDailies#runMoodImprove(WebDriver) runMoodImprove
	 * @see NeoDailies#runFaeries(WebDriver) runFaeries
	 * @see NeoDailies#runLabRay(WebDriver) runLabRay
	 * @see NeoDailies#runStocks(WebDriver) runStocks
	 * @see NeoDailies#runCoincidence(WebDriver) runCoincidence
	 * @see NeoDailies#runLottery(WebDriver) runLottery
	 * @see NeoDailies#runFoodClub(WebDriver) runFoodClub
	 * @see NeoDailies#runCupcake(WebDriver) runCupcake
	 * @see NeoDailies#runMonthlyFreeby(WebDriver) runMonthlyFreeby
	 * @throws Exception Any Exception that occurred in the called methods that needs to be handled within main.
	 */
	private static void oncePerDay(WebDriver driver, String xmlNode, String timeFormat) throws Exception {
		Date currentDay = new Date();
		SimpleDateFormat format2 = new SimpleDateFormat(timeFormat);
		String currentDayString = format2.format(currentDay);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("PreviousRuns.xml"));
		doc.getDocumentElement().normalize();

		NodeList nlList = doc.getDocumentElement().getElementsByTagName(xmlNode).item(0).getChildNodes();
		Node lastRunNode = (Node) nlList.item(1);

		String lastRunDay = doc.getDocumentElement().getElementsByTagName(xmlNode).item(0).getTextContent().trim();

		boolean successTask = false;

		if (!lastRunDay.equals(currentDayString)) {
			if (xmlNode.equals("Tombola")) {
				successTask = runTombola(driver);
			}
			else if (xmlNode.equals("Omelette")) {
				successTask = runOmelette(driver);
			}
			else if (xmlNode.equals("Jelly")) {
				successTask = runJelly(driver);
			}
			else if (xmlNode.equals("PetPetBattles")) {
				successTask = runPetPetBattles(driver);
			}
			else if (xmlNode.equals("Pyramids")) {
				successTask = runPyramids(driver);
			}
			else if (xmlNode.equals("Cheat")) {
				successTask = runCheat(driver);
			}
			else if (xmlNode.equals("Solitaire")) {
				successTask = runSolitaire(driver);
			}
			else if (xmlNode.equals("Scarab21")) {
				successTask = runScarab21(driver);
			}
			else if (xmlNode.equals("Bank")) {
				successTask = runBank(driver);
			}
			else if (xmlNode.equals("AltadorCouncil")) {
				successTask = runAltadorCouncil(driver);
			}
			else if (xmlNode.equals("KikoPop")) {
				successTask = runKikoPop(driver);
			}
			else if (xmlNode.equals("NeggCave")) {
				successTask = runNeggCave(driver);
			}
			else if (xmlNode.equals("ShopOfOffers")) {
				successTask = runShopOfOffers(driver);
			}
			else if (xmlNode.equals("AppleBobbing")) {
				successTask = runAppleBobbing(driver);
			}
			else if (xmlNode.equals("AnchorManagement")) {
				successTask = runAnchorManagement(driver);
			}
			else if (xmlNode.equals("FruitMachine")) {
				successTask = runFruitMachine(driver);
			}
			else if (xmlNode.equals("TDMBGPOP")) {
				successTask = runTDMBGPOP(driver);
			}
			else if (xmlNode.equals("Battledome")) {
				successTask = runBattledome(driver);
			}
			else if (xmlNode.equals("PotatoCounter")) {
				successTask = runPotatoCounter(driver);
			}
			else if (xmlNode.equals("KacheekSeek")) {
				successTask = runKacheekSeek(driver);
			}
			else if (xmlNode.equals("WiseOldKing")) {
				successTask = runWiseOldKing(driver);
			}
			else if (xmlNode.equals("GrumpyOldKing")) {
				successTask = runGrumpyOldKing(driver);
			}
			else if (xmlNode.equals("DailyPuzzle")) {
				successTask = runDailyPuzzle(driver);
			}
			else if (xmlNode.equals("Crossword")) {
				successTask = runCrossword(driver);
			}
			else if (xmlNode.equals("LunarPuzzle")) {
				successTask = runLunarPuzzle(driver);
			}
			else if (xmlNode.equals("Cliffhanger")) {
				successTask = runCliffhanger(driver);
			}
			else if (xmlNode.equals("GeraptikuTomb")) {
				successTask = runGeraptikuTomb(driver);
			}
			else if (xmlNode.equals("ForgottenShore")) {
				successTask = runForgottenShore(driver);
			}
			else if (xmlNode.equals("Meteor")) {
				successTask = runMeteor(driver);
			}
			else if (xmlNode.equals("IslandMystic")) {
				successTask = runIslandMystic(driver);
			}
			else if (xmlNode.equals("MoodImprove")) {
				successTask = runMoodImprove(driver);
			}
			else if (xmlNode.equals("Faeries")) {
				successTask = runFaeries(driver);
			}
			else if (xmlNode.equals("Advent")) {
				successTask = runAdvent(driver);
			}
			else if (xmlNode.equals("LabRay")) {
				successTask = runLabRay(driver);
			}
			else if (xmlNode.equals("Stocks")) {
				successTask = runStocks(driver);
			}
			else if (xmlNode.equals("Coincidence")) {
				successTask = runCoincidence(driver);
			}
			else if (xmlNode.equals("Lottery")) {
				successTask = runLottery(driver);
			}
			else if (xmlNode.equals("FoodClub")) {
				successTask = runFoodClub(driver);
			}
			else if (xmlNode.equals("Cupcake")) {
				successTask = runCupcake(driver);
			}
			else if (xmlNode.equals("MonthlyFreeby")) {
				successTask = runMonthlyFreeby(driver);
			}

			if (successTask) {
				lastRunNode.setTextContent(currentDayString);

				//write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("PreviousRuns.xml"));
				transformer.transform(source, result);
			}
			sleepMode(5000);
		}
	}

	/**
	 * Performs the Tombola daily.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runTombola(WebDriver driver) {
		logMessage("Starting runTombola");
		driver.get("http://www.neopets.com/island/tombola.phtml");
		if (isElementPresentXP("//form[@action='tombola2.phtml']/input[@type='submit']", driver)) {
			driver.findElement(By.xpath("//form[@action='tombola2.phtml']/input[@type='submit']")).click();
			return true;
		}
		logMessage("Successfully ending runTombola");
		return false;
	}

	/**
	 * Performs the Giant Omelette daily.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runOmelette(WebDriver driver) {
		logMessage("Starting runOmelette");
		driver.get("http://www.neopets.com/prehistoric/omelette.phtml");
		if (isElementPresentXP("//form[@action='omelette.phtml']/input[@type='submit']", driver)) {
			driver.findElement(By.xpath("//form[@action='omelette.phtml']/input[@type='submit']")).click();
			logMessage("Successfully ending runOmelette");
			return true;
		}
		logMessage("Failed ending runOmelette");
		return false;
	}

	/**
	 * Performs the Giant Jelly daily.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runJelly(WebDriver driver) {
		logMessage("Starting runJelly");
		driver.get("http://www.neopets.com/jelly/jelly.phtml");
		if (isElementPresentXP("//form[@action='jelly.phtml']/input[@type='submit']", driver)) {
			driver.findElement(By.xpath("//form[@action='jelly.phtml']/input[@type='submit']")).click();
			logMessage("Successfully ending runJelly");
			return true;
		}
		logMessage("Failed ending runJelly");
		return false;
	}

	/**
	 * Plays PetPet Battles until the max games per day are played.
	 * Uses the algorithm of defending as much as it can, and then attacking.
	 * Once attacking, it changes attacks when the previous attack does no damage.
	 * This daily is useless unless you're going for the trophy.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runPetPetBattles(WebDriver driver) {
		logMessage("Starting runPetPetBattles");
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
				sleepMode(10000);
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
		logMessage("Successfully ending runPetPetBattles");
		return true;
	}

	/**
	 * Plays Pyramids until the max neopoints per day are earned.
	 * Searches from the left to right, going from bottom to top.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 * @see NeoDailies#runCheat(WebDriver) runCheat
	 * @see NeoDailies#runScarab21(WebDriver) runScarab21
	 * @see NeoDailies#runSolitaire(WebDriver) runSolitaire
	 */
	private static boolean runPyramids(WebDriver driver) {
		logMessage("Starting runPyramids");
		driver.get("http://www.neopets.com/games/pyramids/index.phtml");
		if (isElementPresentXP("//input[@value='Play Pyramids!']", driver)) {
			driver.findElement(By.xpath("//input[@value='Play Pyramids!']")).click();
		}
		else {
			driver.findElement(By.xpath("//input[@value='Continue Playing']")).click();
		}

		while (true) {
			boolean cardsToPlay = true;

			while (cardsToPlay) {
				try {

					List<WebElement> boards = driver.findElements(By.tagName("table"));
					WebElement board = boards.get(11); //table containing the game
					List<WebElement> cards = board.findElements(By.tagName("img"));
					String curTemp = cards.get(1).getAttribute("src");
					int cur = Integer.parseInt(curTemp.substring(curTemp.lastIndexOf("/") + 1, curTemp.indexOf("_"))); //current card to play on
					ArrayList<Integer> face = new ArrayList<Integer>(); //array for comparing playable cards to the topmost card on deck
					ArrayList<String> vis = new ArrayList<String>(); //array containing the same playable cards, for playing them.
					for (int x = 2; x < cards.size(); x++) {
						String cardImageSrc = cards.get(x).getAttribute("src");
						if (!cardImageSrc.contains("pyramid") && !cardImageSrc.contains("blank")) {
							face.add(Integer.parseInt(cardImageSrc.substring(cardImageSrc.lastIndexOf("/") + 1, cardImageSrc.indexOf("_"))));
							vis.add(cardImageSrc.substring(cardImageSrc.lastIndexOf("/"), cardImageSrc.lastIndexOf(".")));
						}
					}
					logMessage("In Pyramids about to find a playable");
					int x = face.size() - 1;

					for (; x >= 0; x--) {
						int faceUpPlayableCard = face.get(x);
						if ((cur == 2 && (faceUpPlayableCard == 14 || faceUpPlayableCard == 3)) || (cur == 14 && (faceUpPlayableCard == 13 || faceUpPlayableCard == 2)) || (faceUpPlayableCard == cur - 1 || faceUpPlayableCard == cur + 1)) {
							String faceUpPlayableCardFull = vis.get(x);
							for (int y = 0; y < cards.size(); y++) {
								if (cards.get(y).getAttribute("src").contains(faceUpPlayableCardFull)) {
									cards.get(y).click();
									y = 9999;
									x = -9999;
								}
							}
						}
					}
					if (x >= -1) {
						cardsToPlay = false;
					}
				}
				catch(Exception ex) {
					logMessage("Exception occured in Pyramids");
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					ex.printStackTrace(pw);
					String sStackTrace = sw.toString(); //stacktrace as a string
					logMessage(sStackTrace);
					driver.get("http://www.neopets.com/games/pyramids/index.phtml");
					driver.findElement(By.xpath("//input[@value='Cancel Current Game']")).click();
					driver.findElement(By.xpath("//input[@value='Play Pyramids!']")).click();
				}
			}

			try {
				if (!isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/p/font/b/a", driver)) {
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr[2]/td/table/tbody/tr[1]/td/a")).click();
					sleepMode(1250);
				}
				else { //Collect Points is present meaning game is over
					sleepMode(5000);
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/p/font/b/a")).click();
					sleepMode(5000);
					String winText = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]")).getText();
					if (winText.contains("You have reached the")) {
						break;
					}
					driver.get("http://www.neopets.com/games/pyramids/index.phtml");
					sleepMode(5000);

					if (isElementPresentXP("//input[@value='Play Pyramids!']", driver)) {
						driver.findElement(By.xpath("//input[@value='Play Pyramids!']")).click();
					}
					else {
						driver.findElement(By.xpath("//input[@value='Continue Playing']")).click();
					}
					sleepMode(5000);
				}
			}
			catch(NoSuchElementException ex) {
				driver.navigate().refresh();
			}
		}
		logMessage("Successfully ending runPyramids");
		return true;
	}

	/**
	 * Plays Cheat until the max neopoints per day are earned.
	 * Never cheats if it can be avoided (best algorithm especially at higher levels).
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 * @see NeoDailies#runPyramids(WebDriver) runPyramids
	 * @see NeoDailies#runScarab21(WebDriver) runScarab21
	 * @see NeoDailies#runSolitaire(WebDriver) runSolitaire
	 */
	private static boolean runCheat(WebDriver driver) {
		logMessage("Starting runCheat");
		driver.get("http://www.neopets.com/games/cheat/index.phtml");

		if (isElementPresentXP("//input[@value='Continue playing Cheat!']", driver)) {
			driver.findElement(By.xpath("//input[@value='Continue playing Cheat!']")).click();
		}
		else {
			driver.findElement(By.xpath("//input[@value='Start a new game (costs 50 NP)']")).click();
		}

		if (isElementPresentXP("//input[@value='Click to Continue']", driver)) {
			driver.findElement(By.xpath("//input[@value='Click to Continue']")).click();
		}
		else if (isElementPresentXP("//input[@value='Click here to start over']", driver)) {
			driver.findElement(By.xpath("//input[@value='Click here to start over']")).click();
		}
		else if (isElementPresentXP("//input[@value='Play Again']", driver)) {
			driver.findElement(By.xpath("//input[@value='Play Again']")).click();
			driver.findElement(By.xpath("//input[@value='Click here to begin the game!']")).click();
		}
		else if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/b/form[1]/input[2]", driver)) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/b/form[1]/input[2]")).click();
			driver.findElement(By.xpath("//input[@value='Click here to begin the game!']")).click();
		}

		while (true) {

			//Random ran = new Random();
			//int pause = ran.nextInt(1000);
			//sleepMode(2000+pause);
			int[] ourCards = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

			for (int x = 1; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/center/table/tbody/tr[" + x + "]", driver); x++) {
				for (int y = 1; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/center/table/tbody/tr[" + x + "]/td[" + y + "]/center/a/img", driver); y++) {
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

			if (isElementPresentName("x_claim", driver)) {
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
					for (int x = 1; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/center/table/tbody/tr[" + x + "]", driver); x++) {
						for (int y = 1; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/center/table/tbody/tr[" + x + "]/td[" + y + "]/center/a/img", driver); y++) {
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
							logMessage("Matching option: " + option);
							new Select(driver.findElement(By.name("x_claim"))).selectByVisibleText(allOptionsList.get(x));
							break;
						}
					}
				}
				else { //maxIndex never changed, meaning we don't have a valid card. Just play one cheat card.
					logMessage("We don't have a valid card. Have to cheat.");
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/center/table/tbody/tr[1]/td[1]/center/a/img")).click();
					new Select(driver.findElement(By.name("x_claim"))).selectByVisibleText(driver.findElement(By.xpath("//*[@name='x_claim']/option[2]")).getText());
				}

				driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/form/input[6]")).click();

				if (isElementPresentXP("//input[@value='Click to Continue']", driver)) {
					driver.findElement(By.xpath("//input[@value='Click to Continue']")).click();
				}
				else if (isElementPresentXP("//input[@value='Click here to start over']", driver)) {
					driver.findElement(By.xpath("//input[@value='Click here to start over']")).click();
				}
				else if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/form[1]/input[2]", driver)) {
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/form[1]/input[2]")).click();
					driver.findElement(By.xpath("//input[@value='Click here to begin the game!']")).click();
				}
				else if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/b/form[1]/input[2]", driver)) {
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/b/form[1]/input[2]")).click();
					driver.findElement(By.xpath("//input[@value='Click here to begin the game!']")).click();
				}
				else if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/form/b/input[2]", driver)) {
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/form/b/input[2]")).click();
					driver.findElement(By.xpath("//input[@value='Click here to begin the game!']")).click();
				}
			}

			else {
				HashMap<String,Integer> map = new HashMap<String,Integer>();

				for (int x = 1; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[1]/td[" + x + "]", driver); x++) {
					String playerName = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[1]/td[" + x + "]")).getText().trim();
					String[] playerData = playerName.split("\n");
					map.put(playerData[0], Integer.parseInt(playerData[1].substring(playerData[1].indexOf(" ") + 1, playerData[1].indexOf("c") - 1)));
					logMessage("Current opponents include: " + playerName);
				}

				String opponentPlay = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/center[1]/font/b")).getText().trim();
				String opponentCardPlayed = opponentPlay.substring(opponentPlay.lastIndexOf(" "), opponentPlay.length()).trim();
				opponentPlay = opponentPlay.substring(0, opponentPlay.lastIndexOf(" ")).trim();
				int opponentAmountOfCardsPlayed = Integer.parseInt(opponentPlay.substring(opponentPlay.lastIndexOf(" "), opponentPlay.length()).trim());
				opponentPlay = opponentPlay.substring(0, opponentPlay.lastIndexOf(" ")).trim();
				opponentPlay = opponentPlay.substring(0, opponentPlay.lastIndexOf(" ")).trim();
				logMessage("opponentPlay: " + opponentPlay);
				logMessage("opponentCardPlayed: " + opponentCardPlayed);
				logMessage("opponentAmountOfCardsPlayed: " + opponentAmountOfCardsPlayed);

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
					if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center", driver)) {
						String moneyMessage = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center")).getText();
						if (moneyMessage.contains("You have already won the maximum neopoints for today.")) {
							break;
						}
					}
				}
				else {
					driver.findElement(By.xpath("//input[@value='Let " + opponentPlay + " slide']")).click();
				}

				if (isElementPresentXP("//input[@value='Click to Continue']", driver)) {
					driver.findElement(By.xpath("//input[@value='Click to Continue']")).click();
				}
				else if (isElementPresentXP("//input[@value='Play Again']", driver)) {
					driver.findElement(By.xpath("//input[@value='Play Again']")).click();
					driver.findElement(By.xpath("//input[@value='Click here to begin the game!']")).click();
				}

			}

		}

		logMessage("Successfully ending runCheat");
		return true;
	}

	/**
	 * Plays Solitaire until the max neopoints per day are earned.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 * @see NeoDailies#runCheat(WebDriver) runCheat
	 * @see NeoDailies#runPyramids(WebDriver) runPyramids
	 * @see NeoDailies#runScarab21(WebDriver) runScarab21
	 * @see NeoDailies#processDrawnCard(WebDriver) processDrawnCard
	 * @see NeoDailies#processBottomCards(WebDriver) processBottomCards
	 * @see NeoDailies#processTopCards(WebDriver) processTopCards
	 */
	private static boolean runSolitaire(WebDriver driver) {
		logMessage("Starting runSolitaire");
		driver.get("http://www.neopets.com/games/sakhmet_solitaire/index.phtml");
		madeMove = false;
		cardDiv = 2;
		firstMove = true;

		if (isElementPresentXP("//input[@value='Continue Playing']", driver)) {
			driver.findElement(By.xpath("//input[@value='Continue Playing']")).click();
		}
		else {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/div/form/input[1]")).click();
			driver.findElement(By.xpath("//input[@value='Play Sakhmet Solitaire!']")).click();
		}

		while (true) {
			try {
				processDrawnCard(driver);

				if (madeMove || firstMove) {
					//We are now done moving our drawn card. Onto the cards already on the board at the bottoms of their pile
					processBottomCards(driver);

					//We will now consolidate the top cards
					processTopCards(driver);
				}
			}
			catch(NoSuchElementException ex) {
				driver.navigate().refresh();
			}
			firstMove = false;
			if (madeMove) {
				//Do nothing and reloop until it becomes false
				madeMove = false;
			}
			else {
				//Draw card. If there is no card, end the game (including checking if we hit max NP for the day)
				if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[2]/a/img", driver)) {
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[2]/a/img")).click();
					//Random ran = new Random();
					//int pause = ran.nextInt(1000);
					//sleepMode(1000+pause);
					if (!isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[2]/a/img", driver)) {
						cardDiv = 3;
					}
				}
				else {
					//Click collect winnings
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[3]/div/form[1]/a/b")).click();
					handleAlert(driver);
					String completionText = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]")).getText();
					if (completionText.contains("You have reached the")) {
						break;
					}
					driver.findElement(By.xpath("//input[@value='Play Sakhmet Solitaire Again!']")).click();
					madeMove = false;
					cardDiv = 2;
					firstMove = true;

					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/div/form/input[1]")).click();
					driver.findElement(By.xpath("//input[@value='Play Sakhmet Solitaire!']")).click();
				}
			}
		}
		logMessage("Successfully ending runSolitaire");
		return true;
	}

	/**
	 * Plays Scarab 21 until the max neopoints per day are earned.
	 * Uses an algorithm based on forum advice and play experience.
	 * Will try to set up the board to hit a 21 as likely as possible.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 * @see NeoDailies#runCheat(WebDriver) runCheat
	 * @see NeoDailies#runPyramids(WebDriver) runPyramids
	 * @see NeoDailies#runSolitaire(WebDriver) runSolitaire
	 */
	private static boolean runScarab21(WebDriver driver) {
		logMessage("Starting runScarab21");
		driver.get("http://www.neopets.com/games/scarab21/index.phtml");

		if (isElementPresentXP("//input[@value='Play Scarab 21!!!']", driver)) {
			driver.findElement(By.xpath("//input[@value='Play Scarab 21!!!']")).click();
		}
		else {
			driver.findElement(By.xpath("//input[@value='Cancel Current Game']")).click();
			driver.findElement(By.xpath("//input[@value='Play Scarab 21!!!']")).click();
		}

		while (true) {
			//Random ran = new Random();
			//int pause = ran.nextInt(1000);
			//sleepMode(2000 + pause);
			//Collect Points Message (meaning game over)
			if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/div/a/b", driver)) {
				if (driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/div/a/b")).getText().contains("Collect Points")) {
					//Click Collect Points
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/div/a/b")).click();
					String winMessage = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/p")).getText();
					if (winMessage.contains("You have reached the")) {
						break;
					}
					driver.findElement(By.xpath("//input[@value='Play Again!']")).click();
				}

			}

			String drawnCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[1]/table[3]/tbody/tr/td[2]/img")).getAttribute("src");

			int drawnCardNum = Integer.parseInt(drawnCardImg.substring(drawnCardImg.lastIndexOf("/") + 1, drawnCardImg.lastIndexOf("_")));
			int mathDrawnCardNum = -1;
			if (drawnCardNum == 14) {
				mathDrawnCardNum = 11;
			}
			else if (drawnCardNum == 11 || drawnCardNum == 12 || drawnCardNum == 13) {
				mathDrawnCardNum = 10;
			}
			else {
				mathDrawnCardNum = drawnCardNum;
			}

			ArrayList<String> columnPoints = new ArrayList<String>();
			String column1Points = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[3]/td[1]")).getText().trim();
			String column2Points = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[3]/td[2]")).getText().trim();
			String column3Points = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[3]/td[3]")).getText().trim();
			String column4Points = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[3]/td[4]")).getText().trim();
			String column5Points = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[3]/td[5]")).getText().trim();
			columnPoints.add(column1Points);
			columnPoints.add(column2Points);
			columnPoints.add(column3Points);
			columnPoints.add(column4Points);
			columnPoints.add(column5Points);

			int recommendedColumn = -1;
			for (int x = 0; x < columnPoints.size(); x++) {

				int columnPointA = -1;
				int columnPointB = -1;

				String columnPoint = columnPoints.get(x);
				if (columnPoint.contains("or")) {
					columnPointA = Integer.parseInt(columnPoint.substring(0, columnPoint.indexOf(" ")));
					columnPointB = Integer.parseInt(columnPoint.substring(columnPoint.lastIndexOf(" ") + 1, columnPoint.length()));
				}
				else {
					columnPointA = Integer.parseInt(columnPoint);
				}

				if (drawnCardNum == 14) {
					if (columnPointA == 10 || columnPointB == 10) {
						if (driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[2]/td[" + (x + 1) + "]/img[1]")).getAttribute("src").contains("11_spades") &&
								drawnCardImg.contains("14_spades")) {
							recommendedColumn = x + 1;
							break;
						}
						else if (!isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[2]/td[" + (x + 1) + "]/img[2]", driver)) {
							recommendedColumn = x + 1;
						}
						else if (recommendedColumn == -1) {
							recommendedColumn = x + 1;
						}
					}
					else if (columnPointA == 20 || columnPointB == 20) {
						recommendedColumn = x + 1;
					}
				}
				else if (mathDrawnCardNum == 10) {
					if (columnPointA == 11 || columnPointB == 11) {
						if (driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[2]/td[" + (x + 1) + "]/img[1]")).getAttribute("src").contains("14_spades") &&
								drawnCardImg.contains("11_spades")) {
							recommendedColumn = x + 1;
							break;
						}
						else if (!isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[2]/td[" + (x + 1) + "]/img[2]", driver)) {
							recommendedColumn = x + 1;
							break;
						}
						else {
							recommendedColumn = x + 1;
							break;
						}
					}
					else if (columnPointA == 0) {
						recommendedColumn = x + 1;
					}
				}
				else if (mathDrawnCardNum + columnPointA == 21 || mathDrawnCardNum + columnPointB == 21) {
					recommendedColumn = x + 1;
					break;
				}
				else if (mathDrawnCardNum + columnPointA == 11 || mathDrawnCardNum + columnPointB == 11) {
					recommendedColumn = x + 1;
				}
				else if (mathDrawnCardNum == 10 && columnPointA == 0 && recommendedColumn == -1) {
					recommendedColumn = x + 1;
				}
			}

			if (recommendedColumn != -1) {
				//We found a good column, let's move our card there
				driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[1]/td[" + recommendedColumn + "]/a/img")).click();
			}
			else {
				//we didn't find a good column. Just place it where we have the lowest number (should that include 0?)
				int lowestNumber = 999999;
				int lowestNumberColumn = 0;
				for (int x = 0; x < columnPoints.size(); x++) {
					int columnPointA = -1;

					String columnPoint = columnPoints.get(x);
					if (columnPoint.contains("or")) {
						columnPointA = Integer.parseInt(columnPoint.substring(0, columnPoint.indexOf(" ")));
					}
					else {
						columnPointA = Integer.parseInt(columnPoint);
					}

					if (mathDrawnCardNum == 11) {
						mathDrawnCardNum = 1;
					}

					logMessage("This is mathDrawnCardNum: " + mathDrawnCardNum);

					if ((columnPointA < lowestNumber) && (columnPointA + mathDrawnCardNum <= 21) && columnPointA != 0 && columnPointA != 1) {
						if (columnPointA == 10) {
							if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[2]/td[" + (x + 1) + "]/img[2]", driver)) {
								lowestNumber = columnPointA;
								lowestNumberColumn = x + 1;
							}
						}
						else {
							if (columnPointA != 11) {
								lowestNumber = columnPointA;
								lowestNumberColumn = x + 1;
							}
						}
					}
					else if (columnPointA == 0 && drawnCardNum == 14) {
						lowestNumber = columnPointA;
						lowestNumberColumn = x + 1;
						break;
					}
				}

				if (lowestNumberColumn == 0) {
					for (int x = 0; x < columnPoints.size(); x++) {
						int columnPointA = -1;

						String columnPoint = columnPoints.get(x);
						if (columnPoint.contains("or")) {
							columnPointA = Integer.parseInt(columnPoint.substring(0, columnPoint.indexOf(" ")));
						}
						else {
							columnPointA = Integer.parseInt(columnPoint);
						}

						if (mathDrawnCardNum == 11) {
							mathDrawnCardNum = 1;
						}

						if ((columnPointA < lowestNumber) && (columnPointA + mathDrawnCardNum <= 21) && columnPointA != 1) {
							if (columnPointA == 10) {
								if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[2]/td[" + (x + 1) + "]/img[2]", driver)) {
									lowestNumber = columnPointA;
									lowestNumberColumn = x + 1;
								}
							}
							else if (columnPointA == 0 && mathDrawnCardNum == 10) {
								lowestNumber = columnPointA;
								lowestNumberColumn = x + 1;
							}
							else {
								lowestNumber = columnPointA;
								lowestNumberColumn = x + 1;
							}
						}
					}
				}

				if (lowestNumberColumn == 0) {
					for (int x = 0; x < columnPoints.size(); x++) {
						int columnPointA = -1;

						String columnPoint = columnPoints.get(x);
						if (columnPoint.contains("or")) {
							columnPointA = Integer.parseInt(columnPoint.substring(0, columnPoint.indexOf(" ")));
						}
						else {
							columnPointA = Integer.parseInt(columnPoint);
						}

						if (mathDrawnCardNum == 11) {
							mathDrawnCardNum = 1;
						}

						if ((columnPointA < lowestNumber) && (columnPointA + mathDrawnCardNum <= 21)) {
							lowestNumber = columnPointA;
							lowestNumberColumn = x + 1;
							break;
						}
					}
				}

				driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[1]/td[" + lowestNumberColumn + "]/a/img")).click();
			}
		}
		logMessage("Successfully ending runScarab21");
		return true;
	}

	/**
	 * First thing each day, goes to the bank and withdraws enough money to have 100k on hand.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runBank(WebDriver driver) {
		logMessage("Starting runBank");
		driver.get("http://www.neopets.com/bank.phtml");
		if (isElementPresentXP("//form[@onsubmit='return one_submit();']/input[@type='submit']", driver)) {
			driver.findElement(By.xpath("//form[@onsubmit='return one_submit();']/input[@type='submit']")).click();
		}

		int neopoints = Integer.parseInt(driver.findElement(By.id("npanchor")).getText().replace(",", ""));
		if (neopoints < 100000) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table[1]/tbody/tr/td[2]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/input[1]")).clear();
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table[1]/tbody/tr/td[2]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/input[1]")).sendKeys("" + (100000 - neopoints));
			driver.findElement(By.xpath("//input[@value='Withdraw']")).click();
			handleAlert(driver);
		}

		logMessage("Successfully ending runBank");
		return true;
	}

	/**
	 * Collects the user's daily Altador Council reward.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runAltadorCouncil(WebDriver driver) {
		logMessage("Starting runAltadorCouncil");
		driver.get("http://www.neopets.com/altador/council.phtml?prhv=70041bb78f5cc88f70ed399c210e6423");
		if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/input[3]", driver)) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/input[3]")).click();
			logMessage("Successfully ending runAltadorCouncil");
			return true;
		}
		logMessage("Failed ending runAltadorCouncil");
		return false;
	}

	/**
	 * Solves the Negg Cave puzzle daily.
	 * Uses the same solver code as TDN.
	 * Doesn't win 100% of the time because of ambiguous answers, but has a 95+% win rate.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runNeggCave(WebDriver driver) {
		logMessage("Starting runNeggCave");
		driver.get("http://www.neopets.com/shenkuu/neggcave/");
		sleepMode(5000);
		driver.get("http://www.neopets.com/shenkuu/neggcave/");

		String neggBreakerSource = driver.getPageSource();

		String jsCode = "return SolvePuzzle(arguments[0]); function getArr(nRows, nCols, init) { var clueItems = []; "
				+ "for (var j = 0; j < nRows; ++j) { var row = []; for (var k = 0; k < nCols; ++k) { row.push(init); } "
				+ "clueItems.push(row); } return clueItems; } function clueFits(guess, clue) { for (var x = 0; x <= "
				+ "3 - clue.length; ++x) { for (var y = 0; y <= 3 - clue[0].length; ++y) { var works = true; for (var "
				+ "dx = 0; dx < clue.length; ++dx) { for (var dy = 0; dy < clue[0].length; ++dy) { var guessSqr = "
				+ "guess[x + dx][y + dy]; var clueSqr = clue[dx][dy]; if (!((clueSqr[0] == 3 || guessSqr % 3 == "
				+ "clueSqr[0]) && (clueSqr[1] == 3 || (Math.floor(guessSqr / 3) == clueSqr[1])))) { works = false; dx "
				+ "= 3; break; } } } if (works) { return true; } } } return false; } function SolvePuzzle(source) { "
				+ "source = source.split(\"<table class=\\\"mnc_clue_table\\\">\"); var clueSquares = [ [\"mnc_negg_clue_s0c0\", "
				+ "[0, 0]], [\"mnc_negg_clue_s0c1\", [0, 1]], [\"mnc_negg_clue_s0c2\", [0, 2]], [\"mnc_negg_clue_s0cX\", [0, 3]], "
				+ "[\"mnc_negg_clue_s1c0\", [1, 0]], [\"mnc_negg_clue_s1c1\", [1, 1]], [\"mnc_negg_clue_s1c2\", [1, 2]], "
				+ "[\"mnc_negg_clue_s1cX\", [1, 3]], [\"mnc_negg_clue_s2c0\", [2, 0]], [\"mnc_negg_clue_s2c1\", [2, 1]], "
				+ "[\"mnc_negg_clue_s2c2\", [2, 2]], [\"mnc_negg_clue_s2cX\", [2, 3]], [\"mnc_negg_clue_sXc0\", [3, 0]], "
				+ "[\"mnc_negg_clue_sXc1\", [3, 1]], [\"mnc_negg_clue_sXc2\", [3, 2]], [\"mnc_negg_clue_sXcX\", [3, 3]], "
				+ "[\"<td class=\\\"empty\\\"></td>\", [3, 3]], ]; var out = \"\"; var clues = []; for (var i = 1; i < "
				+ "source.length; ++i) { var table = source[i].substring(0, source[i].indexOf(\"</table>\")); var nItems = "
				+ "table.split(\"<td\").length - 1; var nRows = table.split(\"<tr>\").length - 1; var nCols = nItems / nRows; "
				+ "var clueItems = getArr(nRows, nCols, -1); var m = 0; for (var k = 0; k < table.length; ++k) { for (var j = "
				+ "0; j < clueSquares.length; ++j) { var clueStr = clueSquares[j][0]; var clueDesc = clueSquares[j][1]; if "
				+ "(table.substring(k, k + clueStr.length) == clueStr) { clueItems[Math.floor(m / nCols)][m % nCols] = clueDesc; "
				+ "m++; break; } } } clues.push(clueItems); } var permCount = 1; var N = 9; for (var i = 1; i <= N; ++i) { "
				+ "permCount *= i; } for (var perm0 = 0; perm0 < permCount; ++perm0) { var perm = perm0; var curPerm = permCount; "
				+ "var guess = []; for (var i = 0; i < N; ++i) { guess.push(-1); } for (var i = N; i >= 1; --i) { curPerm /= i; "
				+ "var place = Math.floor(perm / curPerm); var m = 0; for (var j = 0; j < N; ++j) { if (guess[j] == -1) { if (m == "
				+ "place) { guess[j] = i - 1; } m++; } } perm %= curPerm; } var guess1 = getArr(3, 3, -1); for (var i = 0; i < N; "
				+ "++i) { guess1[Math.floor(i / 3)][i % 3] = guess[i]; } var isSolution = true; for (var i = 0; i < clues.length; "
				+ "++i) { if (!clueFits(guess1, clues[i])) { isSolution = false; break; } } if (isSolution) { return guess; } } "
				+ "return [9, 9, 9, 9, 9, 9, 9, 9, 9]; } ";

		JavascriptExecutor js = (JavascriptExecutor) driver;
		Object jsReturn = js.executeScript(jsCode, neggBreakerSource);
		String[] jsValues = jsReturn.toString().replace(" ", "").replace("[", "").replace("]", "").split(",");
		for (int x = 0; x < jsValues.length; x++) {
			String neggBoxID = "";
			if (x == 0) {
				neggBoxID = "mnc_grid_cell_0_0";
			}
			else if (x == 1) {
				neggBoxID = "mnc_grid_cell_0_1";
			}
			else if (x == 2) {
				neggBoxID = "mnc_grid_cell_0_2";
			}
			else if (x == 3) {
				neggBoxID = "mnc_grid_cell_1_0";
			}
			else if (x == 4) {
				neggBoxID = "mnc_grid_cell_1_1";
			}
			else if (x == 5) {
				neggBoxID = "mnc_grid_cell_1_2";
			}
			else if (x == 6) {
				neggBoxID = "mnc_grid_cell_2_0";
			}
			else if (x == 7) {
				neggBoxID = "mnc_grid_cell_2_1";
			}
			else if (x == 8) {
				neggBoxID = "mnc_grid_cell_2_2";
			}

			int jsValue = Integer.parseInt(jsValues[x]);

			if (jsValue == 0) { //blue water
				driver.findElement(By.id("mnc_parch_ui_color_0")).click(); //Click blue
				driver.findElement(By.id("mnc_parch_ui_symbol_0")).click(); //Click water
			}
			else if (jsValue == 1) { //blue fire
				driver.findElement(By.id("mnc_parch_ui_color_0")).click(); //Click blue
				driver.findElement(By.id("mnc_parch_ui_symbol_1")).click(); //Click fire
			}
			else if (jsValue == 2) { //blue wind
				driver.findElement(By.id("mnc_parch_ui_color_0")).click(); //Click blue
				driver.findElement(By.id("mnc_parch_ui_symbol_2")).click(); //Click wind
			}
			else if (jsValue == 3) { //red water
				driver.findElement(By.id("mnc_parch_ui_color_1")).click(); //Click red
				driver.findElement(By.id("mnc_parch_ui_symbol_0")).click(); //Click water
			}
			else if (jsValue == 4) { //red fire
				driver.findElement(By.id("mnc_parch_ui_color_1")).click(); //Click red
				driver.findElement(By.id("mnc_parch_ui_symbol_1")).click(); //Click fire
			}
			else if (jsValue == 5) { //red wind
				driver.findElement(By.id("mnc_parch_ui_color_1")).click(); //Click red
				driver.findElement(By.id("mnc_parch_ui_symbol_2")).click(); //Click wind
			}
			else if (jsValue == 6) { //yellow water
				driver.findElement(By.id("mnc_parch_ui_color_2")).click(); //Click yellow
				driver.findElement(By.id("mnc_parch_ui_symbol_0")).click(); //Click water
			}
			else if (jsValue == 7) { //yellow fire
				driver.findElement(By.id("mnc_parch_ui_color_2")).click(); //Click yellow
				driver.findElement(By.id("mnc_parch_ui_symbol_1")).click(); //Click fire
			}
			else if (jsValue == 8) { //yellow wind
				driver.findElement(By.id("mnc_parch_ui_color_2")).click(); //Click yellow
				driver.findElement(By.id("mnc_parch_ui_symbol_2")).click(); //Click wind
			}

			driver.findElement(By.id(neggBoxID)).click(); //Click grid cell
			driver.findElement(By.id("mnc_parch_ui_clear_text")).click(); //Click Clear to reset shape and color selection
		}
		driver.findElement(By.id("mnc_negg_submit_text")).click(); //Click Submit
		sleepMode(20000);
		logMessage("Successfully ending runNeggCave");
		return true;
	}

	/**
	 * Collects the user's daily Shop of Offers reward.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runShopOfOffers(WebDriver driver) {
		logMessage("Starting runShopOfOffers");
		driver.get("http://www.neopets.com/shop_of_offers.phtml?slorg_payout=yes");

		if (isElementPresentXP("//*[@id=\"header\"]/table/tbody/tr[1]/td[1]/a/img", driver)) {
			logMessage("Successfully ending runShopOfOffers");
			return true;
		}
		logMessage("Failed ending runShopOfOffers");
		return false;
	}

	/**
	 * Performs the Apple Bobbing daily.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runAppleBobbing(WebDriver driver) {
		logMessage("Starting runAppleBobbing");
		driver.get("http://www.neopets.com/halloween/applebobbing.phtml?bobbing=1");

		if (isElementPresentXP("//*[@id=\"header\"]/table/tbody/tr[1]/td[1]/a/img", driver)) {
			logMessage("Successfully ending runAppleBobbing");
			return true;
		}
		logMessage("Failed ending runAppleBobbing");
		return false;
	}

	/**
	 * Performs the Anchor Management daily.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runAnchorManagement(WebDriver driver) {
		logMessage("Starting runAnchorManagement");
		driver.get("http://www.neopets.com/pirates/anchormanagement.phtml");
		if (isElementPresentXP("//*[@id=\"btn-fire\"]", driver)) {
			driver.findElement(By.xpath("//*[@id=\"btn-fire\"]/a/div")).click();
			logMessage("Successfully ending runAnchorManagement");
			return true;
		}
		logMessage("Failed ending runAnchorManagement");
		return false;
	}

	/**
	 * Plays the Fruit Machine daily.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runFruitMachine(WebDriver driver) {
		logMessage("Starting runFruitMachine");
		driver.get("http://www.neopets.com/desert/fruit/index.phtml");
		if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/input[3]", driver)) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/input[3]")).click();
			sleepMode(10000); //extra 10 seconds to let the wheel spin
			logMessage("Successfully ending runFruitMachine");
			return true;
		}
		logMessage("Failed ending runFruitMachine");
		return false;
	}

	/**
	 * Performs the TDMBGPOP daily.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runTDMBGPOP(WebDriver driver) {
		logMessage("Starting runTDMBGPOP");
		driver.get("http://www.neopets.com/faerieland/tdmbgpop.phtml");
		if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[3]/form/input[2]", driver)) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[3]/form/input[2]")).click();
			logMessage("Successfully ending runTDMBGPOP");
			return true;
		}
		logMessage("Failed ending runTDMBGPOP");
		return false;
	}

	/**
	 * Plays the battledome until the max neopoints and items per day are earned.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runBattledome(WebDriver driver) {
		logMessage("Starting runBattledome");
		driver.get("http://www.neopets.com/dome/fight.phtml");

		if (isElementPresentXP("//*[@id=\"bdFightPetInfo\"]/div[1]/div[2]/div[17]", driver)) {
			String eligibleText = driver.findElement(By.xpath("//*[@id=\"bdFightPetInfo\"]/div[1]/div[2]/div[17]")).getText();
			logMessage("eligibleText: " + eligibleText);
			if (eligibleText.contains("This Neopet is ready to fight!")) {
				//Click continue this pet
				driver.findElement(By.xpath("//*[@id=\"bdFightStep1\"]/div[3]")).click();
				//Click continue 1-P
				driver.findElement(By.xpath("//*[@id=\"bdFightStep2\"]/div[4]")).click();
				//Select our opponent
				int opponentIndex = 3;
				for (; isElementPresentXP("//*[@id=\"npcTable\"]/tbody/tr[" + opponentIndex + "]/td[2]", driver); opponentIndex++) {
					String opponentName = driver.findElement(By.xpath("//*[@id=\"npcTable\"]/tbody/tr[" + opponentIndex + "]/td[2]")).getText();
					if (opponentName.contains(BD_OPPONENT)) {
						break;
					}
				}
				driver.findElement(By.xpath("//*[@id=\"npcTable\"]/tbody/tr[" + opponentIndex + "]/td[4]/div[1]")).click();
				//Start the battle page
				driver.findElement(By.id("bdFightStep3FightButton")).click();
				sleepMode(5000);
			}
			else {
				logMessage("Pet was no eligible to fight");
				return false;
			}
		}
		if (isElementPresentXP("//*[@id=\"start\"]/div", driver)) { //Already fighting
			while (true) {
				//Start the battle by clicking Fight
				driver.findElement(By.xpath("//*[@id=\"start\"]/div")).click();
				//Click the first equip slot
				sleepMode(5000);
				driver.findElement(By.xpath("//*[@id=\"p1e1m\"]/div")).click();
				//Select Turned Tooth
				sleepMode(2000);
				driver.findElement(By.xpath("//*[@id=\"p1equipment\"]/div[3]/ul/li[1]/img")).click();
				//Click the second equip slot
				sleepMode(2000);
				driver.findElement(By.xpath("//*[@id=\"p1e2m\"]/div")).click();
				//Select Skeletal Fire gun
				sleepMode(2000);
				driver.findElement(By.xpath("//*[@id=\"p1equipment\"]/div[3]/ul/li[2]/img")).click();
				//Click the faerie ability slot
				sleepMode(2000);
				driver.findElement(By.xpath("//*[@id=\"p1am\"]/div")).click();
				//Click lens flare
				sleepMode(2000);
				driver.findElement(By.xpath("//*[@id=\"p1ability\"]/div[3]/table/tbody/tr/td[2]/div/div")).click();
				//Click Fight to kill opponent
				sleepMode(2000);
				driver.findElement(By.id("fight")).click();
				//Click Collect Rewards
				int counter = 0;
				while (!isElementPresentXP("//*[@id=\"playground\"]/div[2]/button[1]", driver) && counter < 15) {
					counter++;
					sleepMode(1000);
				}
				driver.findElement(By.xpath("//*[@id=\"playground\"]/div[2]/button[1]")).click();
				//Click Play Again
				sleepMode(5000);
				String rewardText = driver.findElement(By.id("bd_rewards")).getText();
				if (rewardText.contains("You have reached the item limit for today!") || rewardText.contains("You have reached the NP limit for today!")) {
					break;
				}
				driver.findElement(By.id("bdplayagain")).click();
			}
			logMessage("Successfully ending runBattledome");
			return true;
		}
		else {
			logMessage("Pet was already fighting");
			return false;
		}
	}

	/**
	 * Plays Potato Counter the max number of games per day.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runPotatoCounter(WebDriver driver) {
		logMessage("Starting runPotatoCounter");
		driver.get("http://www.neopets.com/medieval/potatocounter.phtml");

		if (isElementPresentXP("//input[@value='Guess!']", driver)) {
			for (int n = 0; n < 3; n++) {
				long startTime = System.currentTimeMillis();
				int taterCounter = 0;
				for (int x = 1; x < 15; x++) {
					for (int y = 1; y < 15; y++) {
						if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + x + "]/td[" + y + "]/img", driver)) {
							taterCounter++;
						}
					}
				}
				driver.findElement(By.name("guess")).sendKeys(String.valueOf(taterCounter));
				long endTime = System.currentTimeMillis();
				logMessage("PotatoCounter took " + (endTime - startTime) + " milliseconds for " + taterCounter + " potatoes");

				driver.findElement(By.xpath("//input[@value='Guess!']")).click();
				sleepMode(5000);
				if (isElementPresentXP("//input[@value='Play Again']", driver)) {
					driver.findElement(By.xpath("//input[@value='Play Again']")).click();
				}
				else {
					logMessage("Successfully ending runPotatoCounter1");
					return true;
				}

				if (n == 2) {
					logMessage("Successfully ending runPotatoCounter2");
					return true;
				}
			}
		}
		else if (isElementPresentXP("//input[@value='Back to Meridell']", driver)) {
			logMessage("Successfully ending runPotatoCounter3");
			return true;
		}

		logMessage("Failed ending runPotatoCounter");
		return false;
	}

	/**
	 * Plays Kacheek Seek until the user's pet is bored.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runKacheekSeek(WebDriver driver) {
		logMessage("Starting runKacheekSeek");
		driver.get("http://www.neopets.com/games/hidenseek/19.phtml");

		Random ran = new Random();

		for (int x = 1; x < 12; x++) {
			driver.findElement(By.xpath("/html/body/map/area[" + x + "]")).click();
			sleepMode(ran.nextInt(2000) + 1000);

			if (isElementPresentXP("/html/body/center/p[1]/b", driver)) {
				String winText = driver.findElement(By.xpath("/html/body/center/p[1]/b")).getText();
				if (winText.contains("SO BORED")) {
					break;
				}
				else {
					driver.get("http://www.neopets.com/games/hidenseek/19.phtml");
					x = 0;
				}
			}
			else {
				driver.findElement(By.xpath("/html/body/center/p/a")).click();
			}
			sleepMode(ran.nextInt(2000) + 1000);
		}

		logMessage("Successfully ending runKacheekSeek");
		return true;
	}

	/**
	 * Performs the Wise Old King daily. Same answer each time.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runWiseOldKing(WebDriver driver) {
		logMessage("Starting runWiseOldKing");
		driver.get("http://www.neopets.com/medieval/wiseking.phtml");
		List<WebElement> selects = driver.findElements(By.xpath("//form//select"));
		if (selects.size() > 0) {
			for (WebElement select : selects) {
				List<WebElement> options = select.findElements(By.tagName("option"));
				options.get(2).click();
			}
			logMessage("About to impress");
			if (isElementPresentXP("//input[@value='Impress King Hagan with your wisdom!']", driver)) {
				driver.findElement(By.xpath("//input[@value='Impress King Hagan with your wisdom!']")).click();
			}
			logMessage("Impressed");
			logMessage("Successfully ending runWiseOldKing");
			return true;
		}
		logMessage("Failed ending runWiseOldKing");
		return false;

	}

	/**
	 * Performs the Grumpy Old King daily. Same answer each time. Plays twice.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runGrumpyOldKing(WebDriver driver) {
		logMessage("Starting runGrumpyOldKing");
		for (int x = 0; x < 2; x++) {
			driver.get("http://www.neopets.com/medieval/grumpyking.phtml");
			List<WebElement> selects2 = driver.findElements(By.xpath("//form//select"));
			if (selects2.size() > 0) {
				for (WebElement select : selects2) {
					List<WebElement> options = select.findElements(By.tagName("option"));
					options.get(2).click();
				}
				logMessage("About to unbore number: " + x);
				if (isElementPresentXP("//input[@value='Tell the King your joke!']", driver)) {
					driver.findElement(By.xpath("//input[@value='Tell the King your joke!']")).click();
				}
				logMessage("Unbored number: " + x);
				sleepMode(5000);
			}
			if (x == 1) {
				logMessage("Successfully ending runGrumpyOldKing");
				return true;
			}
		}
		logMessage("Failed ending runGrumpyOldKing");
		return false;
	}

	/**
	 * Answers the Daily Puzzle. Grabs the answer from JN.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 * @throws IOException Only occurs if there's an error connecting to JN.
	 */
	private static boolean runDailyPuzzle(WebDriver driver) throws IOException {
		logMessage("Starting runDailyPuzzle");
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
			logMessage("Here's the option: ~" + option.toLowerCase() + "~");
			logMessage("Here's our answer: ~" + resultLine.toLowerCase().trim() + "~");
			if (option.toLowerCase().contains(resultLine.toLowerCase().trim())) {
				new Select(driver.findElement(By.name("trivia_response"))).selectByVisibleText(option);
				logMessage("About to win money!");
				driver.findElement(By.name("submit")).click();
				break;
			}
		}

		if (!isElementPresentName("submit", driver)) {
			logMessage("Successfully ending runDailyPuzzle");
			return true;
		}
		logMessage("Failed ending runDailyPuzzle");
		return false;
	}

	/**
	 * Answers the daily Faerie Crossword. Grabs the answer from JN.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @throws IOException Only occurs if there's an error connecting to JN.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runCrossword(WebDriver driver) throws IOException {
		logMessage("Starting runCrossword");
		String resultLine = "";

		ArrayList<String> acrossList = new ArrayList<String>();
		ArrayList<String> downList = new ArrayList<String>();

		try {

			URL apiUrl = new URL("http://www.jellyneo.net/?go=faerie_crossword");
			HttpURLConnection apiCon = (HttpURLConnection) apiUrl.openConnection();
			apiCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
			InputStream apiInStream = apiCon.getInputStream();
			InputStreamReader apiInStreamReader = new InputStreamReader(apiInStream);
			BufferedReader apiBufRead = new BufferedReader(apiInStreamReader);

			while ((resultLine = apiBufRead.readLine()) != null) {
				if (resultLine.contains("Across")) {
					resultLine = apiBufRead.readLine().trim().replace("<br>", "");
					try {
						while (Character.isDigit(resultLine.charAt(0))) {
							acrossList.add(resultLine);
							logMessage("Adding this across: " + resultLine);
							resultLine = apiBufRead.readLine().trim().replace("<br>", "");
						}
					}
					catch(StringIndexOutOfBoundsException ex) {
						//This error is normal. Don't need to do anything.
					}
				}
				else if (resultLine.contains("Down")) {
					resultLine = apiBufRead.readLine().trim().replace("<br>", "");
					try {
						while (Character.isDigit(resultLine.charAt(0))) {
							downList.add(resultLine);
							logMessage("Adding this down: " + resultLine);
							resultLine = apiBufRead.readLine().trim().replace("<br>", "");
						}
					}
					catch(StringIndexOutOfBoundsException ex) {
						//This error is normal. Don't need to do anything.
					}
				}
			}

		}
		catch (Exception ex) {
			return false;
		}

		driver.get("http://www.neopets.com/games/crossword/index.phtml");

		//Click the button to start the crossword
		driver.findElement(By.xpath("//*[@id='content']/table/tbody/tr/td[2]/div[2]/center[2]/form/input")).click();

		for (int acrossEle = 1; isElementPresentXP("//*[@id='content']/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[4]/td[1]/a[" + acrossEle + "]", driver); acrossEle++) {
			String acrossQ = driver.findElement(By.xpath("//*[@id='content']/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[4]/td[1]/a[" + acrossEle + "]")).getText();
			for (int x = 0; x < acrossList.size(); x++) {
				String acrossListA = acrossList.get(x);
				if (acrossListA.substring(0, acrossListA.indexOf(".")).equals(acrossQ.substring(0, acrossQ.indexOf(".")))) {
					driver.findElement(By.xpath("//*[@id='content']/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[4]/td[1]/a[" + acrossEle + "]")).click();
					driver.findElement(By.name("x_word")).clear();
					driver.findElement(By.name("x_word")).sendKeys(acrossListA.substring(acrossListA.indexOf(".") + 1, acrossListA.length()).trim());
					sleepMode(100);
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/form/input[2]")).click();
					sleepMode(3000);
				}

			}
		}

		for (int downEle = 1; isElementPresentXP("//*[@id='content']/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[4]/td[2]/a[" + downEle + "]", driver); downEle++) {
			String downQ = driver.findElement(By.xpath("//*[@id='content']/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[4]/td[2]/a[" + downEle + "]")).getText();
			for (int x = 0; x < downList.size(); x++) {
				String downListA = downList.get(x);
				if (downListA.substring(0, downListA.indexOf(".")).equals(downQ.substring(0, downQ.indexOf(".")))) {
					driver.findElement(By.xpath("//*[@id='content']/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[4]/td[2]/a[" + downEle + "]")).click();
					driver.findElement(By.name("x_word")).clear();
					driver.findElement(By.name("x_word")).sendKeys(downListA.substring(downListA.indexOf(".") + 1, downListA.length()).trim());
					sleepMode(100);
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/form/input[2]")).click();
					sleepMode(3000);
				}

			}
		}

		if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/center/img", driver)) {
			logMessage("Successfully ending runCrossword");
			return true;
		}
		logMessage("Failed ending runCrossword");
		return false;
	}

	/**
	 * Answers the daily Lunar Puzzle.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runLunarPuzzle(WebDriver driver) {
		/*
		 * These are the different sources for solving (currently using Sunnyneo)
		 * http://www.sunnyneo.com/lunartemple.php
		 * http://www.jellyneo.net/?go=lunartemple
		 * http://thedailyneopets.com/articles/solving-lunar-temple-puzzle/
		 * http://neopets-cheats.com/neopets-lunar-temple-puzzle/
		 */

		logMessage("Starting runLunarPuzzle");
		driver.get("http://www.neopets.com/shenkuu/lunar/?show=puzzle");

		sleepMode(5000);

		String lunarSource = driver.getPageSource();
		lunarSource = lunarSource.substring(lunarSource.indexOf("angleKreludor"), lunarSource.length());
		lunarSource = lunarSource.substring(lunarSource.indexOf("=") + 1, lunarSource.indexOf("&"));

		double angleKreludor = Double.parseDouble(lunarSource);

		//This division and rounding is for JN's answer
		//double answer = angleKreludor / 22.5;
		//answer = Math.round(answer);
		if (angleKreludor >= 0.0 && angleKreludor <= 11.24) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[1]/input")).click();
		}
		else if (angleKreludor >= 11.25 && angleKreludor <= 33.74) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[2]/input")).click();
		}
		else if (angleKreludor >= 33.75 && angleKreludor <= 56.24) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[3]/input")).click();
		}
		else if (angleKreludor >= 56.25 && angleKreludor <= 78.74) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[4]/input")).click();
		}
		else if (angleKreludor >= 78.75 && angleKreludor <= 101.24) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[5]/input")).click();
		}
		else if (angleKreludor >= 101.25 && angleKreludor <= 123.74) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[6]/input")).click();
		}
		else if (angleKreludor >= 123.75 && angleKreludor <= 146.24) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[7]/input")).click();
		}
		else if (angleKreludor >= 146.25 && angleKreludor <= 168.74) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[8]/input")).click();
		}
		else if (angleKreludor >= 168.75 && angleKreludor <= 191.24) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[1]/td[1]/input")).click();
		}
		else if (angleKreludor >= 191.25 && angleKreludor <= 213.74) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[1]/td[2]/input")).click();
		}
		else if (angleKreludor >= 213.75 && angleKreludor <= 236.24) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[1]/td[3]/input")).click();
		}
		else if (angleKreludor >= 236.25 && angleKreludor <= 258.74) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[1]/td[4]/input")).click();
		}
		else if (angleKreludor >= 258.75 && angleKreludor <= 281.24) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[1]/td[5]/input")).click();
		}
		else if (angleKreludor >= 281.25 && angleKreludor <= 303.74) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[1]/td[6]/input")).click();
		}
		else if (angleKreludor >= 303.75 && angleKreludor <= 326.24) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[1]/td[7]/input")).click();
		}
		else if (angleKreludor >= 326.25 && angleKreludor <= 348.74) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[1]/td[8]/input")).click();
		}
		else if (angleKreludor >= 348.75 && angleKreludor <= 360.0) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[1]/input")).click();
		}

		sleepMode(5000);
		if (driver.getPageSource().contains("http://images.neopets.com/shenkuu/lunar/gnorbu_correct.gif")) {
			logMessage("Successfully ending runLunarPuzzle");
			return true;
		}
		logMessage("Failed ending runLunarPuzzle");
		return false;

	}

	/**
	 * Plays Cliffhanger until the max games per day are played.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runCliffhanger(WebDriver driver) {
		logMessage("Starting runCliffhanger");

		for (int cliffCnt = 0; cliffCnt < 10; cliffCnt++) {

			driver.get("http://www.neopets.com/games/cliffhanger/cliffhanger.phtml");

			ArrayList<String> cliffHangerList = new ArrayList<String>();

			cliffHangerList.add("Happy gadgadsbogen day");
			cliffHangerList.add("No news is impossible");
			cliffHangerList.add("Super Glue is forever");
			cliffHangerList.add("Better late than never");
			cliffHangerList.add("Meercas despise red neggs");
			cliffHangerList.add("Scorchios like hot places");
			cliffHangerList.add("Dr Frank Sloth is green");
			cliffHangerList.add("All roads lead to neopia");
			cliffHangerList.add("Koi invented the robotic fish");
			cliffHangerList.add("Dung furniture stinks like dung");
			cliffHangerList.add("Keep your broken toys clean");
			cliffHangerList.add("Today is your lucky day");
			cliffHangerList.add("Nimmos are very spiritual beings");
			cliffHangerList.add("A buzz will never sting you");
			cliffHangerList.add("Be nice to Shoyrus or else");
			cliffHangerList.add("Mr black makes the best shopkeeper");
			cliffHangerList.add("The beader has a beaming smile");
			cliffHangerList.add("The techo is a tree acrobat");
			cliffHangerList.add("Chia bombers are mud slinging fools");
			cliffHangerList.add("Only real card sharks play cheat");
			cliffHangerList.add("Garon loves an endless challenging maze");
			cliffHangerList.add("Great neopets are not always wise");
			cliffHangerList.add("Moogi is a true poogle racer");
			cliffHangerList.add("Fuzios wear the coolest red shoes");
			cliffHangerList.add("Number six is on the run");
			cliffHangerList.add("Carrots are so expensive these days");
			cliffHangerList.add("Faeries are quite fond of reading");
			cliffHangerList.add("Korbats are creatures of the night");
			cliffHangerList.add("Skeiths are strong but very lazy");
			cliffHangerList.add("Chombies are shy and eat plants");
			cliffHangerList.add("Flotsams are no longer limited edition");
			cliffHangerList.add("Kacheekers is a two player game");
			cliffHangerList.add("Tyrannians will eat everything and anything");
			cliffHangerList.add("An air of mystery surrounds the acara");
			cliffHangerList.add("The Cybunny is the fastest neopet ever");
			cliffHangerList.add("The pen is mightier than the pencil");
			cliffHangerList.add("The Snowager sleeps most of its life");
			cliffHangerList.add("You cannot teach an old grarrl mathematics");
			cliffHangerList.add("Most Wild Kikos Swim in Kiko Lake");
			cliffHangerList.add("Some neggs will bring you big disappointment");
			cliffHangerList.add("Some neggs will bring you big neopoints");
			cliffHangerList.add("Unis just love looking at their reflection");
			cliffHangerList.add("When there is smoke there is pollution");
			cliffHangerList.add("Kyrii take special pride in their fur");
			cliffHangerList.add("Maybe the missing link is really missing");
			cliffHangerList.add("Never underestimate the power of streaky bacon");
			cliffHangerList.add("Faerie food is food from the heavens");
			cliffHangerList.add("Frolic in the snow of happy valley");
			cliffHangerList.add("Mister pickles has a terrible tigersquash habit");
			cliffHangerList.add("Jubjubs defend themselves with their deafening screech");
			cliffHangerList.add("Kauvara mixes up potions like no other");
			cliffHangerList.add("Neopian inflation is a fact of life");
			cliffHangerList.add("Poogles look the best in frozen collars");
			cliffHangerList.add("Tornado rings and cement mixers are unstoppable");
			cliffHangerList.add("Uggaroo gets tricky with his coconut shells");
			cliffHangerList.add("Chombies hate fungus balls with a passion");
			cliffHangerList.add("Asparagus is the food of the gods");
			cliffHangerList.add("A miss is as good as a mister");
			cliffHangerList.add("A neopoint saved is a neopoint not enough");
			cliffHangerList.add("A tuskaninny named colin lives on terror mountain");
			cliffHangerList.add("An iron rod bends while it is hot");
			cliffHangerList.add("Do not bathe if there is no water");
			cliffHangerList.add("Dr Death is the keeper of disowned neopets");
			cliffHangerList.add("If your hedge needs trimming call a chomby");
			cliffHangerList.add("Pet rocks make the most playful of petpets");
			cliffHangerList.add("The advent calendar is only open in december");
			cliffHangerList.add("The Alien Aisha Vending Machine serves great good");
			cliffHangerList.add("The big spender is an international jet setter");
			cliffHangerList.add("The Bruce is from Snowy Valley High School");
			cliffHangerList.add("The healing springs mends your wounds after battle");
			cliffHangerList.add("The hidden tower is for big spenders only");
			cliffHangerList.add("The library faerie tends to the crossword puzzle");
			cliffHangerList.add("The tatsu population was almost reduced to extinction");
			cliffHangerList.add("You should try to raise your hit points");
			cliffHangerList.add("Have you trained your pet for the Battledome");
			cliffHangerList.add("Keep your pet company with a neopet pet");
			cliffHangerList.add("Flame the Tame is a ferocious feline fireball");
			cliffHangerList.add("Whack a beast and win some major points");
			cliffHangerList.add("Doctor Sloth tried to mutate neopets but failed");
			cliffHangerList.add("Faerie pancakes go great with crazy crisp tacos");
			cliffHangerList.add("Kougras are said to bring very good luck");
			cliffHangerList.add("Scratch my back and I will scratch yours");
			cliffHangerList.add("Children should not be seen spanked or grounded");
			cliffHangerList.add("Kacheeks have mastered the art of picking flowers");
			cliffHangerList.add("Kikoughela is a fancy word for cough medicine");
			cliffHangerList.add("Snowbeasts love to attack grundos with mud snowballs");
			cliffHangerList.add("An idle mind is the best way to relax");
			cliffHangerList.add("Do not open a shop if you cannot smile");
			cliffHangerList.add("Do not try to talk to a shy peophin");
			cliffHangerList.add("It is always better to give than to receive");
			cliffHangerList.add("Get three times the taste with the triple dog");
			cliffHangerList.add("Let every zafara take care of its own tail");
			cliffHangerList.add("Put all of your neopoints on poogle number two");
			cliffHangerList.add("The barking of Lupes does not hurt the clouds");
			cliffHangerList.add("The battledome is near but the way is icy");
			cliffHangerList.add("The meat of a sporkle is bitter and inedible");
			cliffHangerList.add("The quick brown fox jumps over the lazy dog");
			cliffHangerList.add("The tyrannian volcano is the hottest place in neopia");
			cliffHangerList.add("Look out for the moehog transmogrification potion lurking around");
			cliffHangerList.add("Mika and Carassa Want You To Buy Their Junk");
			cliffHangerList.add("Take your pet to tyrammet for a fabulous time");
			cliffHangerList.add("Your pet deserves a nice stay at the neolodge");
			cliffHangerList.add("Enter the lair of the beast if you dare");
			cliffHangerList.add("Every neopet should have a job and a corndog");
			cliffHangerList.add("Stego is a baby stegosaurus that all neopets love");
			cliffHangerList.add("Treat your usul well and it will be useful");
			cliffHangerList.add("Plesio is the captain of the tyrannian sea division");
			cliffHangerList.add("Poogle five is very chubby but is lightning quick");
			cliffHangerList.add("Sticks n stones are like the greatest band ever");
			cliffHangerList.add("Terror Mountain is home to the infamous Ski Lodge");
			cliffHangerList.add("Magical ice weapons are from the ice cave walls");
			cliffHangerList.add("Meercas are to blame for all the stolen fuzzles");
			cliffHangerList.add("Neopets battledome is not for the weak or sensitive");
			cliffHangerList.add("Poogles have extremely sharp teeth and they are cuddly");
			cliffHangerList.add("Uggaroo follows footsteps to find food for his family");
			cliffHangerList.add("Congratulations to everybody who helped defeat the evil monoceraptor");
			cliffHangerList.add("A chia who is a mocker dances without a tamborine");
			cliffHangerList.add("If you live with lupes you will learn to howl");
			cliffHangerList.add("Oh where is the tooth faerie when you need her");
			cliffHangerList.add("To know and to act are one and the same");
			cliffHangerList.add("All neopets can find a job at the employment agency");
			cliffHangerList.add("The best thing to spend on your neopet is time");
			cliffHangerList.add("The kindhearted faerie queen rules faerieland with a big smile");
			cliffHangerList.add("The lair of the beast is cold and dark inside");
			cliffHangerList.add("The meerca is super fast making it difficult to catch");
			cliffHangerList.add("The pound is not the place to keep streaky bacon");
			cliffHangerList.add("The sunken city of Maraqua has some great hidden treasures");
			cliffHangerList.add("The tyrannian jungle is full of thick muddle and mash");
			cliffHangerList.add("The wise aisha has long ears and a short tongue");
			cliffHangerList.add("Yes boy ice cream sell out all of their shows");
			cliffHangerList.add("Love your neopet but do not hug it too much");
			cliffHangerList.add("Only ask of the Queen Faerie what you really need");
			cliffHangerList.add("Some neohomes are made with mud and dung and straw");
			cliffHangerList.add("With the right training Tuskaninnies can become quite fearsome fighters");
			cliffHangerList.add("Chias are loveable little characters who are full of joy");
			cliffHangerList.add("Store all of your Neopian trading cards in your neodeck");
			cliffHangerList.add("There is nothing like a tall glass of slime potion");
			cliffHangerList.add("Under a tattered cloak you will generally find doctor sloth");
			cliffHangerList.add("Become a BattleDome master by training on the Mystery Island");
			cliffHangerList.add("Better to be safe than meet up with a monocerous");
			cliffHangerList.add("Grarrg is the tyrannian battle master that takes no slack");
			cliffHangerList.add("Please wipe your feet before you enter the Scorchio den");
			cliffHangerList.add("Faeries bend down their wings to a seeker of knowledge");
			cliffHangerList.add("Kyruggi is the grand elder in the tyrannian town hall");
			cliffHangerList.add("Meercas are talented pranksters that take pride in their tails");
			cliffHangerList.add("Bouncing around on its tail the blumaroo is quite happy");
			cliffHangerList.add("A journey of a million miles begins on the marketplace map");
			cliffHangerList.add("Be sure to visit the Neggery for some great magical neggs");
			cliffHangerList.add("By all means trust in neopia but tie your camel first");
			cliffHangerList.add("Do not wake the snowager unless you want to be eaten");
			cliffHangerList.add("If a pteri and lenny were to race neither would win");
			cliffHangerList.add("Ask a lot of questions but only take what is offered");
			cliffHangerList.add("The bluna was first sighted under the ice caps of tyrannia");
			cliffHangerList.add("The Neopedia is a good place to start your Neopian Adventures");
			cliffHangerList.add("You cannot wake a Bruce who is pretending to be asleep");
			cliffHangerList.add("You know the soup kitchen is a great place to go");
			cliffHangerList.add("You know you can create a free homepage for your pet");
			cliffHangerList.add("You probably do not want to know what that odor is");
			cliffHangerList.add("Give the wheel of excitement a spin or two or three");
			cliffHangerList.add("Have you told your friends about the greatest site on earth");
			cliffHangerList.add("Kaus love to sing although they only know a single note");
			cliffHangerList.add("Make certain your pet is well equipped before entering the battledome");
			cliffHangerList.add("Only mad gelerts and englishmen go out in the noonday sun");
			cliffHangerList.add("When eating a radioactive negg remember the pet who planted it");
			cliffHangerList.add("When friends ask about the battledome say there is no tomorrow");
			cliffHangerList.add("When the blind lead the blind get out of the way");
			cliffHangerList.add("Bruce could talk under wet cement with a mouthful of marbles");
			cliffHangerList.add("Count Von Roo is one of the nastier denizens of neopia");
			cliffHangerList.add("Every buzz is a kau in the eyes of its mother");
			cliffHangerList.add("Space slushies are just the thing on a cold winter day");
			cliffHangerList.add("Faerie poachers hang out in faerieland with their jars wide open");
			cliffHangerList.add("Listen to your pet or your tongue will keep you deaf");
			cliffHangerList.add("Poogle number five always wins unless he trips over a hurdle");
			cliffHangerList.add("Grarrls are ferocious creatures or at least they try to be");
			cliffHangerList.add("Jetsams are the meanest Neopets to ever swim the Neopian sea");
			cliffHangerList.add("Tyrannia is the prehistoric kingdom miles beneath the surface of neopia");
			cliffHangerList.add("A kyrii will get very upset if its hair gets messed up");
			cliffHangerList.add("By all means make neofriends with peophins but learn to swim first");
			cliffHangerList.add("Do not be in a hurry to tie what you cannot untie");
			cliffHangerList.add("Do not speak of an elephante if there is no tree nearby");
			cliffHangerList.add("Do not think there are no jetsams if the water is calm");
			cliffHangerList.add("If you see a man riding a wooden stick ask him why");
			cliffHangerList.add("If you want to have lots of adventures then adopt a wocky");
			cliffHangerList.add("Eat all day at the giant omelette but do not be greedy");
			cliffHangerList.add("Fly around the canyons of tyrannia shooting the evil pterodactyls and grarrls");
			cliffHangerList.add("The Grarrl will roar and ten eggs will hatch into baby grarrls");
			cliffHangerList.add("The Snow Faerie Quest is for those that can brave the cold");
			cliffHangerList.add("The wheel of mediocrity is officially the most second rate game around");
			cliffHangerList.add("You should not throw baseballs up when the ceiling fan is on");
			cliffHangerList.add("When an Elephante is in trouble even a Nimmo will kick him");
			cliffHangerList.add("Catch the halter rope and it will lead you to the kau");
			cliffHangerList.add("Dirty snow is the best way to make your battledome opponent mad");
			cliffHangerList.add("Krawk have been known to be as strong as full grown neopets");
			cliffHangerList.add("There is only one ryshu and there is only one techo master");
			cliffHangerList.add("Uggsul invites you to play a game or two of tyranu evavu");
			cliffHangerList.add("Myncies love to hug their plushies and eat sap on a stick");
			cliffHangerList.add("Everyone loves to drink a hot cup of borovan now and then");
			cliffHangerList.add("Jarbjarb likes to watch the tyrannian sunset while eating a ransaurus steak");
			cliffHangerList.add("Quiggles spend all day splashing around in the pool at the neolodge");
			cliffHangerList.add("Experience is the comb that nature gives us when we are bald");
			cliffHangerList.add("Cliffhanger is a brilliant game that will make your pet more intelligent");
			cliffHangerList.add("A Scorchio is a good storyteller if it can make a Skeith listen");
			cliffHangerList.add("Do not be greedy and buy every single food item from the shops");
			cliffHangerList.add("If at first you do not succeed play the ice caves puzzle again");
			cliffHangerList.add("If you go too slow try to keep your worms in a tin");
			cliffHangerList.add("If your totem is made of wax do not walk in the sun");
			cliffHangerList.add("It makes total sense to have a dung carpet in your dung neohome");
			cliffHangerList.add("We never know the worth of items till the wishing well is dry");
			cliffHangerList.add("The Neopian Hospital will help get your pet on the road to recovery");
			cliffHangerList.add("You can lead a kau to water but you cannot make it drink");
			cliffHangerList.add("Bang and smash your way to the top in the bumper cars game");
			cliffHangerList.add("Myncies come from large families and eat their dinner up in the trees");
			cliffHangerList.add("Faerieland is not for pets that are afraid of heights or fluffy clouds");
			cliffHangerList.add("Why beg for stuff when you can make money at the wheel of excitement");
			cliffHangerList.add("You know you should never talk to Bruce even when his mouth is full");
			cliffHangerList.add("Your neopet will need a mint after eating a chili cheese dog with onions");
			cliffHangerList.add("Building a neohome is a way to build a foundation for your little pets");
			cliffHangerList.add("The beast that lives in the tyrannian mountains welcomes all visitors with a sharp smile");
			cliffHangerList.add("The whisper of an acara can be heard farther than the roar of a wocky");
			cliffHangerList.add("You really have to be well trained if you want to own a wild reptillior");
			cliffHangerList.add("Bronto bites are all the rage and they are meaty and very easy to carry");

			if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/input[4]", driver)) {
				driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/input[4]")).click();
				driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/input[5]")).click();
			}

			String hintedSentence = driver.findElement(By.xpath("//*[@id='content']/table/tbody/tr/td[2]/table/tbody/tr[2]/td")).getText().trim();
			hintedSentence = hintedSentence.replace("   ", "~");
			hintedSentence = hintedSentence.replace(" ", "");
			hintedSentence = hintedSentence.replace("~", " ");
			hintedSentence = hintedSentence.replace("\n", " ");
			sleepMode(5000);

			logMessage("hintedSentence: " + hintedSentence);

			int count = hintedSentence.length() - hintedSentence.replace(" ", "").length();

			for (int x = 0; x < cliffHangerList.size(); x++) {
				if ((cliffHangerList.get(x).length() - cliffHangerList.get(x).replace(" ", "").length()) != count) {
					cliffHangerList.remove(x);
					x--;
				}
			}

			for (int x = 0; x < cliffHangerList.size(); x++) {
				if (hintedSentence.length() != (cliffHangerList.get(x).length())) {
					cliffHangerList.remove(x);
					x--;
				}
			}

			for (int x = 0; x < cliffHangerList.size(); x++) {
				String cliffHangerTestString = cliffHangerList.get(x);
				cliffHangerTestString = cliffHangerTestString.substring(0, cliffHangerTestString.indexOf(" "));
				String cliffHangerTestString2 = cliffHangerList.get(x);
				cliffHangerTestString2 = cliffHangerTestString2.substring(cliffHangerTestString2.lastIndexOf(" "), cliffHangerTestString2.length());

				if (cliffHangerTestString.length() != hintedSentence.substring(0, hintedSentence.indexOf(" ")).length() || cliffHangerTestString2.length() != hintedSentence.substring(hintedSentence.lastIndexOf(" "), hintedSentence.length()).length()) {
					cliffHangerList.remove(x);
					x--;
				}
			}

			String theWord = cliffHangerList.get(0);

			if (cliffHangerList.size() > 1) {
				logMessage("There were still dupe cliffhanger sentences");

				driver.findElement(By.linkText("N")).click();
				sleepMode(1000);
				driver.findElement(By.linkText("H")).click();
				sleepMode(1000);
				driver.findElement(By.linkText("L")).click();
				sleepMode(1000);
				driver.findElement(By.linkText("D")).click();
				sleepMode(1000);
				driver.findElement(By.linkText("C")).click();
				sleepMode(5000);

				hintedSentence = driver.findElement(By.xpath("//*[@id='content']/table/tbody/tr/td[2]/table/tbody/tr[2]/td")).getText().trim();
				hintedSentence = hintedSentence.replace("   ", "~");
				hintedSentence = hintedSentence.replace(" ", "");
				hintedSentence = hintedSentence.replace("~", " ");
				hintedSentence = hintedSentence.replace("\n", " ");

				logMessage("Hint before final dedupes: " + hintedSentence);

				if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[3]/td[2]/table/tbody/tr[2]/td/font[6]", driver)) {
					for (int x = 2; x < 7; x++) {
						String color = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[3]/td[2]/table/tbody/tr[2]/td/font[" + x + "]")).getAttribute("color");
						if (color.contains("black")) {
							String failedLetter = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[3]/td[2]/table/tbody/tr[2]/td/font[" + x + "]")).getText().trim();
							for (int y = 0; y < cliffHangerList.size(); y++) {
								if (cliffHangerList.get(y).toLowerCase().contains(failedLetter.toLowerCase())) {
									cliffHangerList.remove(y);
									y--;
								}
							}
						}
					}
				}

				for (int n = 0; n < hintedSentence.length(); n++) {
					char hintedSentenceChar = hintedSentence.toLowerCase().charAt(n);

					if (hintedSentenceChar != ('_') && hintedSentenceChar != (' ')) {
						for (int x = 0; x < cliffHangerList.size(); x++) {
							if (cliffHangerList.get(x).toLowerCase().charAt(n) != hintedSentenceChar) {
								cliffHangerList.remove(x);
								x--;
							}
						}
					}
				}
				theWord = cliffHangerList.get(0);
			}

			logMessage("This must be the answer: " + theWord);

			driver.findElement(By.name("solve_puzzle")).clear();
			driver.findElement(By.name("solve_puzzle")).sendKeys(theWord);

			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/input[2]")).click();

			if (cliffCnt == 9) {
				logMessage("Successfully ending runCliffhanger");
				return true;
			}
		}

		logMessage("Failed ending runCliffhanger");
		return false;
	}


	/**
	 * Purchases 1000 shares of any stock currently at 15np per share.
	 * Uses the bargain stock list and grabs the first stock it sees at 15np.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runStocks(WebDriver driver) {
		logMessage("Starting runStocks");

		driver.get("http://www.neopets.com/stockmarket.phtml?type=list&bargain=true");

		String stockSource = driver.getPageSource();

		if (stockSource.contains("/marquee")) {
			stockSource = stockSource.substring(stockSource.indexOf("marquee"), stockSource.indexOf("/marquee"));

			String[] stockSourceArr = stockSource.split("><b");

			for (int x = 1; x < stockSourceArr.length; x++) {
				stockSourceArr[x] = stockSourceArr[x].substring(1, stockSourceArr[x].indexOf("<"));
				String tickerPrice = stockSourceArr[x];

				tickerPrice = tickerPrice.substring(tickerPrice.indexOf(" ")+1, tickerPrice.lastIndexOf(" "));

				if (tickerPrice.equals("15")) {
					logMessage("Found a stock for 15");
					driver.get("http://www.neopets.com/stockmarket.phtml?type=buy&ticker=" + stockSourceArr[x].substring(0, stockSourceArr[x].indexOf(" ")));

					if (isElementPresentName("amount_shares", driver)) {
						driver.findElement(By.name("amount_shares")).clear();
						driver.findElement(By.name("amount_shares")).sendKeys("1000");
						driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[3]/td/input")).click();
						sleepMode(5000); //Allow time for purchase to process
						logMessage("Successfully ending runStocks");
						return true;
					}
					else {
						logMessage("Failed ending 1 runStocks");
						return false;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Performs The Coincidence daily.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runCoincidence(WebDriver driver) {
		logMessage("Starting runCoincidence");
		driver.get("http://www.neopets.com/space/coincidence.phtml");

		ArrayList<String> questItems = new ArrayList<String>();
		ArrayList<Integer> questAmounts = new ArrayList<Integer>();

		for (int x = 1; isElementPresentXP("//*[@id='questItems']/tbody/tr/td[" + x + "]", driver); x++) {
			String questItem = driver.findElement(By.xpath("//*[@id='questItems']/tbody/tr/td[" + x + "]")).getText().replace("\n", "").trim();
			logMessage("Coincidence questItem " + x + ": " + questItem);
			questItems.add(questItem.substring(0, questItem.lastIndexOf("x")));
			questAmounts.add(Integer.parseInt(questItem.substring(questItem.lastIndexOf("x") + 1, questItem.length())));
		}

		boolean doneShopping = false;

		while (!doneShopping) {

			for (int x = 0; x < questItems.size(); x++) {
				if (!shopWizard(driver, questItems.get(x))) {
					logMessage("Failed ending 1 runCoincidence");
					return false;
				}
			}

			ArrayList<String> tempQuestItems = new ArrayList<String>();
			ArrayList<Integer> tempQuestAmounts = new ArrayList<Integer>();
			tempQuestItems.addAll(questItems);
			tempQuestAmounts.addAll(questAmounts);

			driver.get("http://www.neopets.com/quickstock.phtml");
			for (int x = 2; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
				String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]")).getText();
				for (int y = 0; y < tempQuestItems.size(); y++) {
					if (invItem.contains(tempQuestItems.get(y))) {
						tempQuestAmounts.set(y, tempQuestAmounts.get(y) - 1);
						if (tempQuestAmounts.get(y) == 0) {
							tempQuestItems.remove(y);
							tempQuestAmounts.remove(y);
							y--;
						}
					}
				}
			}

			if (tempQuestAmounts.size() == 0) {
				doneShopping = true;
			}

		}

		shopURLs.clear();

		driver.get("http://www.neopets.com/space/coincidence.phtml");

		driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td/div[3]/div")).click();

		if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td/a/div/div", driver)) {
			logMessage("Successfully ending runCoincidence");
			return true;
		}
		logMessage("Failed ending 2 runCoincidence");
		return false;
	}

	/**
	 * Performs the Geraptiku Tomb daily.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runGeraptikuTomb(WebDriver driver) {
		logMessage("Starting runGeraptikuTomb");
		driver.get("http://www.neopets.com/worlds/geraptiku/tomb.phtml");
		if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div[2]/form[1]/input[2]", driver)) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div[2]/form[1]/input[2]")).click();
			if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div[2]/form[1]/input", driver)) {
				driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div[2]/form[1]/input")).click();
				logMessage("Successfully ending runExpellibox");
				return true;
			}
			logMessage("Failed ending 1 runExpellibox");
			return false;
		}
		logMessage("Failed ending 1 runExpellibox");
		return false;
	}

	/**
	 * Performs the Forgotten Shore daily.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runForgottenShore(WebDriver driver) {
		logMessage("Starting runForgottenShore");
		driver.get("http://www.neopets.com/pirates/forgottenshore.phtml");

		String forgottenSource = driver.getPageSource();
		if (forgottenSource.contains("?confirm=1")) {
			forgottenSource = forgottenSource.substring(forgottenSource.indexOf("?confirm=1"), forgottenSource.indexOf("\"><div id=\"shore_"));
			logMessage("ForgottenSource: " + forgottenSource);
			driver.get("http://www.neopets.com/pirates/forgottenshore.phtml" + forgottenSource.replace("&amp;", "&"));
			sleepMode(5000);
			logMessage("Successfully ending runForgottenShore 1");
			return true;
		}
		else {
			logMessage("Successfully ending runForgottenShore 2");
			return true;
		}
	}

	/**
	 * Performs the Kreludor Meteor daily.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runMeteor(WebDriver driver) {
		logMessage("Starting runMeteor");
		driver.get("http://www.neopets.com/moon/meteor.phtml");
		if (isElementPresentXP("//input[@value='Take a chance']", driver)) {
			driver.findElement(By.xpath("//input[@value='Take a chance']")).click();
			new Select(driver.findElement(By.name("pickstep"))).selectByVisibleText("Poke the meteor with a stick.");
			driver.findElement(By.name("meteorsubmit")).click();
			sleepMode(5000); //Might need extra sleep
			logMessage("Successfully ending runMeteor");
			return true;
		}
		logMessage("Failed ending runMeteor");
		return false;
	}

	/**
	 * Performs the Island Mystic daily (only useful for the avatar).
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runIslandMystic(WebDriver driver) {
		logMessage("Starting runIslandMystic");
		driver.get("http://www.neopets.com/island/mystichut.phtml");
		if (isElementPresentXP("//*[@id=\"header\"]/table/tbody/tr[1]/td[1]/a/img", driver)) {
			logMessage("Successfully ending runIslandMystic");
			return true;
		}
		logMessage("Failed ending runIslandMystic");
		return false;
	}

	/**
	 * Checks if the user's active pet's mood is not "delighted!".
	 * If it isn't, the program goes to the merry-go-round until it is.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runMoodImprove(WebDriver driver) {
		logMessage("Starting runMoodImprove");
		while (true) {
			driver.get("http://www.neopets.com/worlds/roo/merrygoround.phtml");
			String mood = "";
			if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[1]/div[1]/table/tbody/tr[4]/td/table/tbody/tr[3]/td[2]/b", driver)) {
				mood = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[1]/div[1]/table/tbody/tr[4]/td/table/tbody/tr[3]/td[2]/b")).getText().trim();
			}
			else if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[1]/div[1]/table/tbody/tr[5]/td/table/tbody/tr[3]/td[2]/b", driver)) {
				mood = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[1]/div[1]/table/tbody/tr[5]/td/table/tbody/tr[3]/td[2]/b")).getText().trim();
			}
			else {
				return false;
			}
			if (!mood.equals("delighted!")) {
				driver.findElement(By.xpath("//input[@value='" + PETNAME + "']")).click();
				driver.findElement(By.xpath("//input[@value='Go on the Ride!']")).click();
				sleepMode(5000);
			}
			else {
				break;
			}
		}
		logMessage("Successfully ending runMoodImprove");
		return true;
	}

	/**
	 * Collects the daily Advent Calendar gift.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runAdvent(WebDriver driver) {
		logMessage("Starting runAdvent");

		driver.get("http://www.neopets.com/winter/adventcalendar.phtml");
		if (isElementPresentXP("//input[@value='Collect My Prize!!!']", driver)) {
			driver.findElement(By.xpath("//input[@value='Collect My Prize!!!']")).click();
		}/*
		sleepMode(10000);
		driver.get("http://www.neopets.com/winter/adventcalendar.phtml");
		sleepMode(10000);
		if (isElementPresentXP("//*[@id=\"dom_overlay_container\"]", driver)) {
			driver.findElement(By.xpath("//*[@id=\"dom_overlay_container\"]")).click();
		}*/

		logMessage("Successfully ending runFaeries");
		return true;
	}

	/**
	 * Performs the user's daily faerie quest assuming they have the FQ cookie.
	 * Will also visit the faerie festival daily if enabled.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runFaeries(WebDriver driver) {
		logMessage("Starting runFaeries");
		//Uncomment this when the faerie festival rolls around (regularly September)
		//driver.get("http://www.neopets.com/faeriefestival/");

		//Check if we're already on a FQ and complete it before taking our daily one
		driver.get("http://www.neopets.com/market.phtml?type=wizard");

		if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/p[1]/a[1]", driver)) { //Faerie quest link
			logMessage("runFaeries ShopWizard FQ found");
			if (!faerieQuest(driver)) {
				return false;
			}
		}

		driver.get("http://www.neopets.com/quests.phtml");

		if (isElementPresentID("fq-fc-accept", driver)) {
			driver.findElement(By.id("fq-fc-accept")).click();
			faerieQuest(driver);
		}

		logMessage("Successfully ending runFaeries");
		return true;
	}

	/**
	 * Zaps the user's chosen lab rat. Emails them the result of the zap.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runLabRay(WebDriver driver) {
		logMessage("Starting runLabRay");
		driver.get("http://www.neopets.com/lab2.phtml");
		List<WebElement> zapPets = driver.findElements(By.xpath("//input[@value='" + PET_ZAPPED + "']"));

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

		if (isElementPresentXP("/html/body/center/p[3]", driver)) {
			String firstHalfMessage = driver.findElement(By.xpath("/html/body/center/p[1]")).getText().replace("\n", " ").trim();
			String secondHalfMessage = driver.findElement(By.xpath("/html/body/center/p[3]")).getText().replace("\n", " ").trim();
			String message = firstHalfMessage + secondHalfMessage;
			sendEmail(message);
		}
		logMessage("Successfully ending runLabRay");
		return true;
	}

	/**
	 * Plays the Neopian Lottery the max games per day (20 tickets).
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runLottery(WebDriver driver) {
		logMessage("Starting runLottery");
		//https://www.andrew.cmu.edu/user/kmliu/neopets/lottery2.html Same algorithm but in Javascript
		driver.get("http://www.neopets.com/games/lottery.phtml");

		int[] arr = new int[30];
		for (int i = 0; i < 30; i++) {
			arr[i] = i + 1;
		}

		int[] arr1 = shuffle(arr);
		int[] arr2 = leaf(Arrays.copyOfRange(arr1, 0, 15), Arrays.copyOfRange(arr1, 15, 30));
		arr2 = leaf(Arrays.copyOfRange(arr2, 0, 15), Arrays.copyOfRange(arr2, 15, 30));
		int[] arr3 = leaf(Arrays.copyOfRange(arr2, 0, 15), Arrays.copyOfRange(arr2, 15, 30));
		arr3 = leaf(Arrays.copyOfRange(arr3, 0, 15), Arrays.copyOfRange(arr3, 15, 30));
		int[] arr4 = leaf(Arrays.copyOfRange(arr3, 0, 15), Arrays.copyOfRange(arr3, 15, 30));
		arr4 = leaf(Arrays.copyOfRange(arr4, 0, 15), Arrays.copyOfRange(arr4, 15, 30));
		ArrayList<Integer> marr = new ArrayList<Integer>();
		for (int x = 0; x < arr1.length; x++) {
			marr.add(arr1[x]);
		}
		for (int x = 0; x < arr2.length; x++) {
			marr.add(arr2[x]);
		}
		for (int x = 0; x < arr3.length; x++) {
			marr.add(arr3[x]);
		}
		for (int x = 0; x < arr4.length; x++) {
			marr.add(arr4[x]);
		}

		int i = 0;
		for (; i < 20; i++) {
			System.out.print("Ticket " + (i + 1) + ": ");
			int j = 0;
			driver.findElement(By.name("one")).clear();
			driver.findElement(By.name("one")).sendKeys("" + marr.get(6 * i + j));
			j++;
			driver.findElement(By.name("two")).clear();
			driver.findElement(By.name("two")).sendKeys("" + marr.get(6 * i + j));
			j++;
			driver.findElement(By.name("three")).clear();
			driver.findElement(By.name("three")).sendKeys("" + marr.get(6 * i + j));
			j++;
			driver.findElement(By.name("four")).clear();
			driver.findElement(By.name("four")).sendKeys("" + marr.get(6 * i + j));
			j++;
			driver.findElement(By.name("five")).clear();
			driver.findElement(By.name("five")).sendKeys("" + marr.get(6 * i + j));
			j++;
			driver.findElement(By.name("six")).clear();
			driver.findElement(By.name("six")).sendKeys("" + marr.get(6 * i + j));
			driver.findElement(By.xpath("//input[@value='Buy a Lottery Ticket!']")).click();
			sleepMode(5000);
		}
		if (i == 20) {
			logMessage("Successfully ending runLottery");
			return true;
		}
		logMessage("Failed ending runLottery");
		return false;
	}

	/**
	 * Plays the Food Club. Uses Garet's bets.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runFoodClub(WebDriver driver) {
		logMessage("Starting runFoodClub");

		Date date = new Date();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

		if (currentHour < 13) {
			driver.get("http://www.neopets.com/~boochi_target");

			String currentRound = "";

			if (isElementPresentXP("/html/body/center[3]/center[1]/center[1]/table/tbody/tr[3]/td[1]", driver)) {
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
			logMessage(diffDays + " days");

			String daysSince1999May07 = String.valueOf(diffDays);

			if (currentRound.equals(daysSince1999May07)) {
				for (int x = 3; isElementPresentXP("/html/body/center[3]/center[1]/center[1]/table/tbody/tr[" + x + "]/td[3]", driver); x++) {
					String betInfo = driver.findElement(By.xpath("/html/body/center[3]/center[1]/center[1]/table/tbody/tr[" + x + "]/td[2]")).getText().trim();
					betInfoList.add(betInfo);
				}

				driver.get("http://www.neopets.com/pirates/foodclub.phtml?type=bet");
				String betAmount = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/p[4]/b")).getText();
				for (int x = 0; x < betInfoList.size(); x++) {
					logMessage("BET " + x + " FOUND");
					String[] betInfoArr = betInfoList.get(x).split("\n");
					for (int n = 0; n < betInfoArr.length; n++) {
						String bet = betInfoArr[n].trim();
						String arenaName = bet.substring(0, bet.indexOf(":"));
						logMessage("arenaName: " + arenaName);
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
							logMessage("Here's the pirate option: ~" + option.toLowerCase() + "~");
							logMessage("Here's our pirate answer: ~" + winningPirate.toLowerCase().trim() + "~");
							if (option.toLowerCase().contains(winningPirate.toLowerCase().trim())) {
								logMessage("About to select our pirate");
								new Select(driver.findElement(By.name("winner" + (xPathValue - 2)))).selectByVisibleText(option);
								logMessage("Select our pirate");
								break;
							}
						}
						logMessage("done putting together bet");

					}
					driver.findElement(By.name("bet_amount")).clear();
					driver.findElement(By.name("bet_amount")).sendKeys(betAmount);

					driver.findElement(By.xpath("//input[@value='Place this bet!']")).click();
					driver.get("http://www.neopets.com/pirates/foodclub.phtml?type=bet");

				}

				logMessage("Successfully ending 1 runFoodClub");
				return true;
			}

			if (isElementPresentXP("/html/body/center[3]/p[1]/font", driver)) {
				logMessage("Successfully ending 2 runFoodClub");
				return true;
			}
		}
		logMessage("Failed ending runFoodClub, but we have to return true");
		return true;

	}

	/**
	 * Adopts a Neopet whose birthday is today, gets the current day's cupcake, and abandons the pet.
	 * The cupcake is then fed to the user's active pet. In other words, cupcake training.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 * @throws IOException Occurs if BirthdayList.csv doesn't exist, or if the date format is messed with (this should never occur).
	 */
	private static boolean runCupcake(WebDriver driver) {
		try {
			logMessage("Starting runCupcake");

			Calendar cal = Calendar.getInstance();
			String currentDateStamp = new SimpleDateFormat("MMMM").format(cal.getTime()) + " " + new SimpleDateFormat("d").format(cal.getTime()) + ",";
			logMessage("currentDateStamp: " + currentDateStamp);

			ArrayList<String> birthdayPets = new ArrayList<String>();

			String fileLine = "";
			try (BufferedReader br = new BufferedReader(new FileReader("BirthdayList.csv"))) {
				while ((fileLine = br.readLine()) != null) {
					if (fileLine.contains(currentDateStamp)) {
						logMessage("Bday pet: " + fileLine.substring(fileLine.indexOf(",") + 1, fileLine.length()));
						birthdayPets.add(fileLine.substring(fileLine.indexOf(",") + 1, fileLine.length()));
					}
				}
			}

			String adoptedPet = "";
			for (int x = 0; x < birthdayPets.size(); x++) {
				adoptedPet = birthdayPets.get(x);
				driver.get("http://www.neopets.com/pound/adopt.phtml?search=" + birthdayPets.get(x));
				sleepMode(1000);
				if (isElementPresentID("pet1_price_div", driver)) {
					break;
				}
				else if (x == birthdayPets.size() - 1) {
					return true;
				}
			}

			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td/div[2]/div[3]/div[1]/table/tbody/tr[2]/td/div/img")).click();
			handleAlert(driver);
			sleepMode(3000);

			driver.get("http://www.neopets.com/petlookup.phtml?pet=" + adoptedPet);

			String currentDay = new SimpleDateFormat("E").format(cal.getTime());
			sleepMode(30000);

			if (isElementPresentXP("//*[@id=\"birthdayPrize71521\"]/img", driver)) {

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

				sleepMode(1000);
				driver.findElement(By.xpath("//*[@id=\"popupBirthdaySelect\"]/div[2]/div")).click();

			}

			sleepMode(5000);

			driver.get("http://www.neopets.com/pound/abandon.phtml");

			WebElement abandonPet = null;
			if (isElementPresentID(adoptedPet + "_button", driver)) {
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
			while (isElementPresentID(adoptedPet + "_button", driver)) {
				abandonPet.click();
				sleepMode(1000);
			}

			sleepMode(5000);

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

						new Select(driver.findElement(By.name("action"))).selectByVisibleText("Feed to " + PETNAME + ".");

						driver.findElement(By.xpath("//input[@value='Submit']")).click();

						sleepMode(5000);

						driver.close(); //may not be needed since the window closes automatically
						driver.switchTo().window(winHandleBefore);

						itemRow = 99999;
						itemColumn = 99999;
					}
				}
			}
		}
		catch (Exception ex) {
			return true;
		}
		logMessage("Successfully ending runCupcake");
		return true;
	}

	/**
	 * Goes through the user's inventory and donates any items worth less than 100np according to JN.
	 * For items worth more, they are put into the user's SDB.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runDeleteItems(WebDriver driver) {
		logMessage("Starting runDeleteItems");
		driver.get("http://www.neopets.com/quickstock.phtml");

		String resultLine = "";

		for (int x = 2; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
			if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[1]", driver) &&
					isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[4]/input", driver) &&
					!isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[1]/b", driver)) {
				String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[1]")).getText();
				if (invItem.length() > 1 && !invItem.contains("Weak Bottled")) {
					boolean hasNeocodexPrice = false;
					boolean hasJellyneoPrice = false;
					boolean priceBelow100 = false;
					try {
						URL apiUrl = new URL("http://www.neocodex.us/forum/index.php?app=itemdb&module=search?app=itemdb&module=search&section=search&item=%22" + invItem.replace(" ", "+") + "%22&description=&rarity_low=&rarity_high=&price_low=&price_high=&shop=&search_order=price&sort=asc&lim=20");
						HttpURLConnection apiCon = (HttpURLConnection) apiUrl.openConnection();
						apiCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
						InputStream apiInStream = apiCon.getInputStream();
						InputStreamReader apiInStreamReader = new InputStreamReader(apiInStream);
						BufferedReader apiBufRead = new BufferedReader(apiInStreamReader);

						while ((resultLine = apiBufRead.readLine()) != null) {
							if (resultLine.contains("idbQuickPrice")) {
								hasNeocodexPrice = true;
								resultLine = apiBufRead.readLine();
								int price = Integer.parseInt(resultLine.substring(resultLine.indexOf(">") + 1, resultLine.indexOf(" ")).replace(",", ""));
								if (price <= MINIMUM_KEEP_VALUE || invItem.contains("Battlecard")) {
									priceBelow100 = true;
									driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[4]/input")).click();
								}
								else {
									driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[3]/input")).click();
								}
								break;
							}
						}
						if (!hasNeocodexPrice) {
							driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[3]/input")).click();
						}
					}
					catch(Exception ex) {
						ex.printStackTrace();
						sleepMode(30000);
					}

					if (!priceBelow100) {
						try {
							logMessage("Checking Jellyneo prices");
							URL apiUrl = new URL("https://items.jellyneo.net/search/?name=" + invItem.replace(" ", "+") + "&name_type=3");
							HttpURLConnection apiCon = (HttpURLConnection) apiUrl.openConnection();
							apiCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
							InputStream apiInStream = apiCon.getInputStream();
							InputStreamReader apiInStreamReader = new InputStreamReader(apiInStream);
							BufferedReader apiBufRead = new BufferedReader(apiInStreamReader);

							while ((resultLine = apiBufRead.readLine()) != null) {
								if (resultLine.contains("price-history-link")) {
									hasJellyneoPrice = true;
									int price = Integer.parseInt(removeHTML(resultLine).replace(",", "").replace("NP", "").trim());
									if (price <= MINIMUM_KEEP_VALUE || invItem.contains("Battlecard")) {
										priceBelow100 = true;
										driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[4]/input")).click();
									}
									else {
										driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[3]/input")).click();
									}
									break;
								}
							}
							if (!hasNeocodexPrice && !hasJellyneoPrice) {
								driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[3]/input")).click();
							}
						}
						catch(Exception ex) {
							ex.printStackTrace();
							sleepMode(30000);
						}
					}
					sleepMode(5000);
				}
				else if (invItem.contains("Weak Bottled")) { //Deposit Weak Bottle Faeries
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[3]/input")).click();
				}
			}
			else if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[1]", driver)) {
				String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[1]")).getText().trim();
				if (invItem.endsWith(" Birthday Cupcake")) {
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[3]/input")).click();
				}
				else if (invItem.contains("Pile of Dung")) {
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[5]/input")).click();
				}
			}
		}

		if (isElementPresentXP("//input[@value='Submit']", driver)) {
			driver.findElement(By.xpath("//input[@value='Submit']")).click();
			handleAlert(driver);
		}

		logMessage("Successfully ending runDeleteItems");
		return true;
	}

	/**
	 * When the new month rolls over, grabs the user's monthly freebies.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 */
	private static boolean runMonthlyFreeby(WebDriver driver) {
		logMessage("Starting runMonthlyFreeby");
		driver.get("http://www.neopets.com/freebies/index.phtml");
		if (isElementPresentXP("//*[@id=\"header\"]/table/tbody/tr[1]/td[1]/a/img", driver)) {
			logMessage("Successfully ending runMonthlyFreeby");
			return true;
		}
		logMessage("Failed ending runMonthlyFreeby");
		return false;
	}

	/**
	 * Handles any activities which are run at minimum scheduled intervals. Is only called from main.
	 * @param driver The WebDriver
	 * @param xmlNode The name of the node in the user's PreviousRuns.xml the program is looking for
	 * @param requiredInterval The amount of time in seconds the node requires to have passed.+
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 * @see NeoDailies#oncePerSchedule(WebDriver, String, int[]) oncePerSchedule
	 * @see NeoDailies#runStockSell(WebDriver) runStockSell
	 * @see NeoDailies#runTrudysSurprise(WebDriver) runTrudysSurprise
	 * @see NeoDailies#runExpellibox(WebDriver) runExpellibox
	 * @see NeoDailies#runKitchenQuest(WebDriver) runKitchenQuest
	 * @see NeoDailies#runEmployment(WebDriver) runEmployment
	 * @see NeoDailies#runHealingSprings(WebDriver) runHealingSprings
	 * @see NeoDailies#runNeoMail(WebDriver) runNeoMail
	 * @see NeoDailies#runGuessMarrow(WebDriver) runGuessMarrow
	 * @see NeoDailies#runSymolHole(WebDriver) runSymolHole
	 * @see NeoDailies#runColtzansShrine(WebDriver) runColtzansShrine
	 * @see NeoDailies#runWishingWell(WebDriver) runWishingWell
	 * @see NeoDailies#runFishing(WebDriver) runFishing
	 * @see NeoDailies#runGraveDanger(WebDriver) runGraveDanger
	 * @see NeoDailies#runDubloonTraining(WebDriver) runDubloonTraining
	 * @see NeoDailies#runIslandTraining(WebDriver) runIslandTraining
	 * @see NeoDailies#runSecondHandShoppe(WebDriver) runSecondHandShoppe
	 * @see NeoDailies#runTarla(WebDriver) runTarla
	 * @see NeoDailies#runShopTill(WebDriver) runShopTill
	 * @see NeoDailies#runDeleteItems(WebDriver) runDeleteItems
	 * @see NeoDailies#runStockItems(WebDriver, String) runStockItems
	 * @throws Exception Any Exception that occurred in the called methods that needs to be handled within main.
	 */
	private static void oncePerInterval(WebDriver driver, String xmlNode, long requiredInterval) throws Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("PreviousRuns.xml"));
		doc.getDocumentElement().normalize();

		NodeList nlList = doc.getDocumentElement().getElementsByTagName(xmlNode).item(0).getChildNodes();
		Node lastRunNode = (Node) nlList.item(1);

		long lastRunEpoch = Long.parseLong(doc.getDocumentElement().getElementsByTagName(xmlNode).item(0).getTextContent().trim());

		long currentEpoch = Instant.now().getEpochSecond();

		boolean successTask = false;

		if ((currentEpoch - lastRunEpoch) > requiredInterval) {
			if (xmlNode.equals("StockSell")) {
				successTask = runStockSell(driver);
			}
			else if (xmlNode.equals("TrudysSurprise")) {
				successTask = runTrudysSurprise(driver);
			}
			else if (xmlNode.equals("Expellibox")) {
				successTask = runExpellibox(driver);
			}
			else if (xmlNode.equals("KitchenQuest")) {
				successTask = runKitchenQuest(driver);
			}
			else if (xmlNode.equals("Employment")) {
				successTask = runEmployment(driver);
			}
			else if (xmlNode.equals("HealingSprings")) {
				successTask = runHealingSprings(driver);
			}
			else if (xmlNode.equals("NeoMail")) {
				successTask = runNeoMail(driver);
			}
			else if (xmlNode.equals("GuessMarrow")) {
				successTask = runGuessMarrow(driver);
			}
			else if (xmlNode.equals("SymolHole")) {
				successTask = runSymolHole(driver);
			}
			else if (xmlNode.equals("ColtzansShrine")) {
				successTask = runColtzansShrine(driver);
			}
			else if (xmlNode.equals("WishingWell")) {
				successTask = runWishingWell(driver);
			}
			else if (xmlNode.equals("Fishing")) {
				successTask = runFishing(driver);
			}
			else if (xmlNode.equals("GraveDanger")) {
				successTask = runGraveDanger(driver);
			}
			else if (xmlNode.equals("DubloonTraining")) {
				successTask = runDubloonTraining(driver);
			}
			else if (xmlNode.equals("IslandTraining")) {
				successTask = runIslandTraining(driver);
			}
			else if (xmlNode.equals("SecondHandShoppe")) {
				successTask = runSecondHandShoppe(driver);
			}
			else if (xmlNode.equals("Tarla")) {
				successTask = runTarla(driver);
			}
			else if (xmlNode.equals("ShopTill")) {
				successTask = runShopTill(driver);
			}
			else if (xmlNode.equals("DeleteItems")) {
				successTask = runDeleteItems(driver);
			}
			else if (xmlNode.equals("RepriceItems")) {
				successTask = runStockItems(driver, "Reprice");
			}
			else if (xmlNode.equals("StockItems")) {
				successTask = runStockItems(driver, "Stock");
			}

			if (successTask) {
				lastRunNode.setTextContent(String.valueOf(currentEpoch));

				//write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("PreviousRuns.xml"));
				transformer.transform(source, result);

				sleepMode(5000);
			}
		}
	}

	/**
	 * Checks the user's stocks to find out if any are at 60np or above.
	 * According to NeoDaq, 60np is the golden number.
	 * If any are, sells all the user's shares of that stock
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runStockSell(WebDriver driver) {
		logMessage("Starting runStockSell");
		driver.get("http://www.neopets.com/stockmarket.phtml?type=portfolio");

		boolean sellingStocks = false;
		for (int x = 3; isElementPresentXP("//*[@id=\"postForm\"]/table[1]/tbody/tr[" + x + "]/td[4]", driver); x+=2) {
			try {
				int currentPrice = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"postForm\"]/table[1]/tbody/tr[" + x + "]/td[4]")).getText().trim());
				if (currentPrice >= 60) {
					sellingStocks = true;
					String stockID = driver.findElement(By.xpath("//*[@id=\"postForm\"]/table[1]/tbody/tr[" + x + "]/td[1]/img[1]")).getAttribute("id").trim();
					stockID = stockID.substring(0, stockID.indexOf("d"));
					logMessage("Selling stock: " + stockID + " at price: " + currentPrice);
					driver.findElement(By.xpath("//*[@id=\"postForm\"]/table[1]/tbody/tr[" + x + "]/td[1]/img[1]")).click();

					for (int y = 2; isElementPresentXP("//*[@id=\"" + stockID + "\"]/td/table/tbody/tr[" + y + "]/td[1]", driver); y++) {
						String shareAmount = driver.findElement(By.xpath("//*[@id=\"" + stockID + "\"]/td/table/tbody/tr[" + y + "]/td[1]")).getText().trim().replace(",", "");

						logMessage("Selling shareAmount: " + shareAmount);
						if (isElementPresentXP("//*[@id=\"" + stockID + "\"]/td/table/tbody/tr[" + y + "]/td[7]/input", driver)) {
							driver.findElement(By.xpath("//*[@id=\"" + stockID + "\"]/td/table/tbody/tr[" + y + "]/td[7]/input")).clear();
							driver.findElement(By.xpath("//*[@id=\"" + stockID + "\"]/td/table/tbody/tr[" + y + "]/td[7]/input")).sendKeys(shareAmount);
						}
						else {
							driver.findElement(By.xpath("//*[@id=\"" + stockID + "\"]/td/table/tbody/tr[" + y + "]/td[6]/input")).clear();
							driver.findElement(By.xpath("//*[@id=\"" + stockID + "\"]/td/table/tbody/tr[" + y + "]/td[6]/input")).sendKeys(shareAmount);
						}
					}
				}
			}
			catch(NumberFormatException ex) {
				//This is a normal error. Doesn't need handling.
			}
		}

		if (sellingStocks) {
			driver.findElement(By.xpath("//input[@value='Sell Shares']")).click();
		}

		logMessage("Successfully ending runStockSell");
		return true;
	}

	/**
	 * Plays Trudy's Surprise.
	 * Runs every few hours because flash is buggy on slow computers, thus could possibly need re-attempts.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runTrudysSurprise(WebDriver driver) {
		logMessage("Starting runTrudysSurprise");
		driver.get("http://www.neopets.com/trudys_surprise.phtml");

		sleepMode(10000);

		Actions builder = new Actions(driver);
		try {
			builder.moveToElement(driver.findElement(By.xpath("//*[@id=\"tempcontent\"]")), 400, 500).doubleClick().build().perform();
			sleepMode(1000);
			builder.moveToElement(driver.findElement(By.xpath("//*[@id=\"tempcontent\"]")), 400, 500).doubleClick().build().perform();
			sleepMode(5000);
			builder.moveToElement(driver.findElement(By.xpath("//*[@id=\"tempcontent\"]")), 400, 500).click().build().perform();
		}
		catch(Exception ex) {

		}
		sleepMode(20000);

		logMessage("Successfully ending runTrudysSurprise");
		return true;
	}

	/**
	 * Plays Qasalan Expellibox.
	 * Runs every few hours because flash is buggy on slow computers, thus could possibly need re-attempts.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runExpellibox(WebDriver driver) {
		logMessage("Starting runExpellibox");
		driver.get("http://ncmall.neopets.com/mall/shop.phtml?page=giveaway");

		sleepMode(10000);

		Actions builder = new Actions(driver);
		try {
			builder.moveToElement(driver.findElement(By.xpath("//*[@id=\"main_div\"]/div/div[2]")), 350, 430).doubleClick().build().perform();
			sleepMode(1000);
			builder.moveToElement(driver.findElement(By.xpath("//*[@id=\"main_div\"]/div/div[2]")), 350, 430).click().build().perform();
		}
		catch(Exception ex) {

		}
		sleepMode(20000);

		logMessage("Successfully ending runExpellibox");
		return true;
	}

	/**
	 * Plays Kiko Pop.
	 * Runs every few hours because flash is buggy on slow computers, thus could possibly need re-attempts.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runKikoPop(WebDriver driver) {
		logMessage("Starting runKikoPop");
		driver.get("http://www.neopets.com/worlds/kiko/kpop/");

		sleepMode(10000);

		if (isElementPresentXP("//*[@id=\"difficultyForm\"]/div[3]/div", driver)) {
			driver.findElement(By.xpath("//*[@id=\"difficultyForm\"]/div[3]/div")).click();
		}

		sleepMode(10000);

		Actions builder = new Actions(driver);
		try {
			builder.moveToElement(driver.findElement(By.name("kikopop")), 460, 300).click().build().perform();
		}
		catch(Exception ex) {

		}

		sleepMode(15000);

		Actions builder2 = new Actions(driver);
		try {
			builder2.moveToElement(driver.findElement(By.name("kikopop")), 540, 540).click().build().perform();
		}
		catch(Exception ex) {

		}

		sleepMode(10000);

		logMessage("Successfully ending runKikoPop");
		return true;
	}

	/**
	 * Grabs a Faerie Employment job. Checks prices to choose most profitable job.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runEmployment(WebDriver driver) {
		logMessage("Starting runEmployment");
		driver.get("http://www.neopets.com/faerieland/employ/employment.phtml?type=jobs&voucher=basic");

		int bestItemXpathIndex = 0;
		int bestItemProfit = 0;
		int x = 2;

		for (; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]/table/tbody/tr[" + x + "]/td/b[1]", driver); x+=3) {
			String jobDesc = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]/table/tbody/tr[" + x + "]/td")).getText().trim();
			jobDesc = jobDesc.replace("\n\n", "<br>").replace("   ", "<br>");
			String[] jobDescSplit = jobDesc.split(" ");
			int itemAmount = Integer.parseInt(jobDescSplit[1]);
			int reward = Integer.parseInt(jobDescSplit[jobDescSplit.length - 2].replace(",", ""));
			String item = jobDesc.substring(jobDesc.indexOf(":") + 1, jobDesc.indexOf("<")).trim();
			logMessage("itemAmount: " + itemAmount + ", item: " + item + ", reward: " + reward);

			if (item.length() > 1) {
				while (true) {
					try {
						URL apiUrl = new URL("http://www.neocodex.us/forum/index.php?app=itemdb&module=search?app=itemdb&module=search&section=search&item=%22" + item.replace(" ", "+") + "%22&description=&rarity_low=&rarity_high=&price_low=&price_high=&shop=&search_order=price&sort=asc&lim=20");
						HttpURLConnection apiCon = (HttpURLConnection) apiUrl.openConnection();
						apiCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
						InputStream apiInStream = apiCon.getInputStream();
						InputStreamReader apiInStreamReader = new InputStreamReader(apiInStream);
						BufferedReader apiBufRead = new BufferedReader(apiInStreamReader);

						String resultLine = "";

						while ((resultLine = apiBufRead.readLine()) != null) {
							if (resultLine.contains("idbQuickPrice")) {
								resultLine = apiBufRead.readLine();
								logMessage("Neocodex price: " + resultLine);
								int price = Integer.parseInt(resultLine.substring(resultLine.indexOf(">") + 1, resultLine.indexOf(" ")).replace(",", ""));
								int profit = reward - (price * itemAmount);
								if (profit > bestItemProfit) {
									bestItemProfit = profit;
									bestItemXpathIndex = x;
								}
								break;
							}
						}
						break;
					}
					catch(Exception ex) {
						break;
						//sleepMode(5000);
					}
				}
			}
		}

		logMessage("Winning job found");
		String jobDesc = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]/table/tbody/tr[" + bestItemXpathIndex + "]/td")).getText().trim();
		jobDesc = jobDesc.replace("\n\n", "<br>").replace("   ", "<br>");
		String[] jobDescSplit = jobDesc.split(" ");
		int itemAmount = Integer.parseInt(jobDescSplit[1]);
		String item = jobDesc.substring(jobDesc.indexOf(":") + 1, jobDesc.indexOf("<")).trim();

		driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]/table/tbody/tr[" + (bestItemXpathIndex - 1) + "]/td[3]/b/a")).click();

		boolean jobReceived = false;
		String jobUrl = "";

		logMessage("About to check if we got the job");

		if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]", driver)) {
			if (driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]")).getText().trim().equals("You have already taken enough jobs today! Come back tomorrow.")) {
				logMessage("Completed the max amount of jobs for today");
				logMessage("Successfully ending 1 runEmployment");
				return true;
			}
		}

		driver.get("http://www.neopets.com/faerieland/employ/employment.phtml?type=status");
		if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]/table/tbody/tr[2]/td[2]/a", driver)) {
			logMessage("We got the job");
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]/table/tbody/tr[2]/td[2]/a")).click();

			String jobDetails = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[2]/b[2]")).getText().trim();

			logMessage("jobDetails: " + jobDetails);
			item = jobDetails.substring(jobDetails.indexOf(":") + 2, jobDetails.length());
			itemAmount = Integer.parseInt(jobDetails.substring(5, 7).trim());
			item = item.trim();

			jobReceived = true;
			jobUrl = driver.getCurrentUrl();

			boolean doneShopping = false;

			while (!doneShopping) {

				if (!shopWizard(driver, item)) {
					logMessage("Failed ending 1 runEmployment");
					return false;
				}

				driver.get("http://www.neopets.com/quickstock.phtml");
				int itemCount = 0;
				for (int qsIndex = 2; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + qsIndex + "]", driver); qsIndex++) {
					String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + qsIndex + "]")).getText().trim();
					if (invItem.contains("N/A")) {
						invItem = invItem.substring(0, invItem.indexOf(" N/A"));
					}
					logMessage("invItem: ~" + invItem + "~");
					logMessage("item: ~" + item + "~");
					if (invItem.equals(item)) {
						itemCount++;
					}
				}

				logMessage("This is how many employment items we have: " + itemCount);
				logMessage("This is how many employment items we need total: " + itemAmount);

				if (itemCount >= itemAmount) {
					doneShopping = true;
				}

			}
			shopURLs.clear();
		}

		if (jobReceived) {
			driver.get(jobUrl.replace("apply", "desc"));
			if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[3]/b[4]", driver)) {
				logMessage("We completed a job: " + driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[3]/b[4]")).getText().trim());
				if (driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[3]/b[4]")).getText().trim().equals("Completed Job Data...")) {
					logMessage("Successfully ending 2 runEmployment");
					return true;
				}
			}
		}

		logMessage("Failed ending 2 runEmployment");
		return false;
	}

	/**
	 * Starts a Kitchen Quest.
	 * Checks the price of the items, and uses an algorithm to check if the cost of the items justifies the reward.
	 * If the price is justifiable, the quest is completed.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runKitchenQuest(WebDriver driver) {
		logMessage("Starting runKitchenQuest");
		//Formula loosely based on http://www.neopets.com/ntimes/index.phtml?section=477168&week=424
		driver.get("http://www.neopets.com/pirates/academy.phtml?type=status");

		int totalNeopets = 0;
		int neopetLevel = 0;
		int trainingCost = 0;

		for (int pets = 1; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + pets + "]/td/b", driver); pets+=2) {
			String petTitle = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + pets + "]/td/b")).getText();
			if (petTitle.contains(PETNAME)) {
				neopetLevel = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + (pets + 1) + "]/td[1]/font/b")).getText());
				totalNeopets++;
			}
			else {
				totalNeopets++;
			}
		}

		logMessage("We have this many pets: " + totalNeopets + " with our main pet's level: " + neopetLevel);

		if (neopetLevel < 10) {
			trainingCost = AVERAGE_ONE_DUBLOON_COST;
		}
		else if (neopetLevel >= 11 && neopetLevel <= 20) {
			trainingCost = AVERAGE_TWO_DUBLOON_COST;
		}
		else if (neopetLevel >= 21 && neopetLevel < 40) {
			trainingCost = AVERAGE_FIVE_DUBLOON_COST;
		}
		else if (neopetLevel >= 40 && neopetLevel <= 80) {
			trainingCost = AVERAGE_CODESTONE_COST * 3;
		}
		else if (neopetLevel >= 81 && neopetLevel <= 100) {
			trainingCost = AVERAGE_CODESTONE_COST * 4;
		}
		else if (neopetLevel >= 101 && neopetLevel <= 120) {
			trainingCost = AVERAGE_CODESTONE_COST * 5;
		}
		else if (neopetLevel >= 121 && neopetLevel <= 150) {
			trainingCost = AVERAGE_CODESTONE_COST * 6;
		}
		else if (neopetLevel >= 151 && neopetLevel <= 200) {
			trainingCost = AVERAGE_CODESTONE_COST * 7;
		}
		else if (neopetLevel >= 201 && neopetLevel <= 249) {
			trainingCost = AVERAGE_CODESTONE_COST * 8;
		}
		else if (neopetLevel >= 250 && neopetLevel <= 299) {
			trainingCost = AVERAGE_RED_CODESTONE_COST;
		}
		else if (neopetLevel >= 300 && neopetLevel <= 399) {
			trainingCost = AVERAGE_RED_CODESTONE_COST * 2;
		}
		else if (neopetLevel >= 400 && neopetLevel <= 499) {
			trainingCost = AVERAGE_RED_CODESTONE_COST * 3;
		}
		else if (neopetLevel >= 500 && neopetLevel <= 599) {
			trainingCost = AVERAGE_RED_CODESTONE_COST * 4;
		}
		else if (neopetLevel >= 600 && neopetLevel <= 749) {
			trainingCost = AVERAGE_RED_CODESTONE_COST * 5;
		}
		else if (neopetLevel >= 750) {
			trainingCost = AVERAGE_RED_CODESTONE_COST * 6;
		}

		int maxKitchenQuestExpense = ((USEFUL_KQ_STAT_REWARD_PCT * trainingCost) / 100) / totalNeopets;

		driver.get("http://www.neopets.com/island/kitchen.phtml");

		if (isElementPresentXP("//input[@value='Get out of here!']", driver)) {
			driver.findElement(By.xpath("//input[@value='Get out of here!']")).click();
			driver.get("http://www.neopets.com/island/kitchen.phtml");
		}

		ArrayList<String> neededFoods = new ArrayList<String>();
		if (isElementPresentXP("//input[@value='Sure, I will help!']", driver)) {
			driver.findElement(By.xpath("//input[@value='Sure, I will help!']")).click();
			for (int foods = 1; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr/td[" + foods + "]/b", driver); foods++) {
				String foodName = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr/td[" + foods + "]/b")).getText();
				neededFoods.add(foodName);
			}
		}

		else if (isElementPresentXP("//input[@value='I have the Ingredients!']", driver)) {
			for (int foods = 1; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[3]/td[2]/b[" + foods + "]", driver); foods++) {
				String foodName = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[3]/td[2]/b[" + foods + "]")).getText();
				neededFoods.add(foodName);
			}
		}

		int totalCost = 0;

		for (int foods = 0; foods < neededFoods.size(); foods++) {
			while (true) {
				try {
					URL apiUrl = new URL("http://www.neocodex.us/forum/index.php?app=itemdb&module=search?app=itemdb&module=search&section=search&item=%22" + neededFoods.get(foods).replace(" ", "+") + "%22&description=&rarity_low=&rarity_high=&price_low=&price_high=&shop=&search_order=price&sort=asc&lim=20");
					HttpURLConnection apiCon = (HttpURLConnection) apiUrl.openConnection();
					apiCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
					InputStream apiInStream = apiCon.getInputStream();
					InputStreamReader apiInStreamReader = new InputStreamReader(apiInStream);
					BufferedReader apiBufRead = new BufferedReader(apiInStreamReader);

					boolean hasPrice = false;
					String resultLine = "";
					while ((resultLine = apiBufRead.readLine()) != null) {
						if (resultLine.contains("idbQuickPrice")) {
							hasPrice = true;
							resultLine = apiBufRead.readLine();
							int price = Integer.parseInt(resultLine.substring(resultLine.indexOf(">") + 1, resultLine.indexOf(" ")).replace(",", ""));
							totalCost+=price;
							break;
						}
					}
					if (!hasPrice) {
						totalCost = 99999;
					}
					break;
				}
				catch(Exception ex) {
					totalCost = 99999;
					break;
				}
			}
		}

		if (totalCost <= maxKitchenQuestExpense) {
			boolean doneShopping = false;

			while (!doneShopping) {

				for (int x = 0; x < neededFoods.size(); x++) {
					if (!shopWizard(driver, neededFoods.get(x))) {
						return false;
					}
				}

				ArrayList<String> tempNeededFoods = new ArrayList<String>();
				tempNeededFoods.addAll(neededFoods);

				driver.get("http://www.neopets.com/quickstock.phtml");
				for (int x = 2; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
					String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]")).getText();
					for (int y = 0; y < tempNeededFoods.size(); y++) {
						if (invItem.contains(tempNeededFoods.get(y))) {
							tempNeededFoods.remove(y);
							y--;
						}
					}
				}

				if (tempNeededFoods.size() == 0) {
					doneShopping = true;
				}

				neededFoods = tempNeededFoods;

			}

			shopURLs.clear();

			driver.get("http://www.neopets.com/island/kitchen.phtml");

			driver.findElement(By.xpath("//input[@value='I have the Ingredients!']")).click();

			logMessage("Successfully ending 1 runKitchenQuest");
			return true;

		}
		else {
			logMessage("Too much money: " + totalCost);
			logMessage("Can only justify: " + maxKitchenQuestExpense);
			logMessage("Successfully ending 2 runKitchenQuest");
			return true;
		}
	}

	/**
	 * Checks if the user's pet is sick, hungry, or damaged.
	 * If any of those hold true, use the Healing Springs.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runHealingSprings(WebDriver driver) {
		logMessage("Starting runHealingSprings");
		driver.get("http://www.neopets.com/quickref.phtml");

		String source = driver.getPageSource();

		boolean needToHeal = false;

		if (source.contains(PETNAME + " is suffering from")) {
			logMessage("We're sick");
			needToHeal = true;
		}

		if (isElementPresentXP("//*[@id=\"" + PETNAME + "_details\"]/table/tbody/tr[2]/td/div[1]/table/tbody/tr[6]/td/font/b", driver)) {
			String hpStats = driver.findElement(By.xpath("//*[@id=\"" + PETNAME + "_details\"]/table/tbody/tr[2]/td/div[1]/table/tbody/tr[6]/td/font/b")).getText();
			int currentHP = Integer.parseInt(hpStats.substring(0, hpStats.indexOf(" ")));
			int totalHP = Integer.parseInt(hpStats.substring(hpStats.indexOf("/") + 2, hpStats.length()));
			String hunger = driver.findElement(By.xpath("//*[@id=\"" + PETNAME + "_details\"]/table/tbody/tr[2]/td/div[1]/table/tbody/tr[8]/td/b")).getText().trim().toLowerCase();

			if (currentHP < totalHP || hunger.equals("hungry") || hunger.equals("very hungry") ||
					hunger.equals("famished") || hunger.equals("starving") || hunger.equals("dying")) {
				needToHeal = true;
			}

		}
		else {
			logMessage("Failed ending 1 runHealingSprings");
			return false;
		}

		if (needToHeal) {
			driver.get("http://www.neopets.com/faerieland/springs.phtml");
			if (isElementPresentXP("//input[@value='Heal my Pets']", driver)) {
				driver.findElement(By.xpath("//input[@value='Heal my Pets']")).click();
			}
			else {
				logMessage("Failed ending 2 runHealingSprings");
				return false;
			}
		}

		logMessage("Successfully ending runHealingSprings");
		return true;
	}

	/**
	 * Checks if the user has any mail or notifications.
	 * If either of those holds true, an email is sent.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runNeoMail(WebDriver driver) {
		logMessage("Starting runNeoMail");
		driver.get("http://www.neopets.com/neomessages.phtml");
		if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[3]/td[1]/input", driver)) {
			sendEmail("NeoMail Received");
		}
		else if (isElementPresentXP("//*[@id=\"header\"]/table/tbody/tr[1]/td[2]/a[2]/b", driver)) {
			sendEmail("Trade Offer Received");
		}
		logMessage("Successfully ending runNeoMail");
		return true;
	}

	/**
	 * Checks if the Marrow is ready to be guessed. If it is, guesses 427.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runGuessMarrow(WebDriver driver) {
		logMessage("Starting runGuessMarrow");
		driver.get("http://www.neopets.com/medieval/guessmarrow.phtml");
		if (isElementPresentXP("//*[@id=\"content\"]/div[1]/table/tbody/tr/td[2]/center[2]/form/input[1]", driver)) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/div[1]/table/tbody/tr/td[2]/center[2]/form/input[1]")).clear();
			driver.findElement(By.xpath("//*[@id=\"content\"]/div[1]/table/tbody/tr/td[2]/center[2]/form/input[1]")).sendKeys("427");
			driver.findElement(By.xpath("//input[@value='Guess!']")).click();
			logMessage("Successfully ending runGuessMarrow");
			return true;
		}
		logMessage("Failed ending runGuessMarrow");
		return false;
	}

	/**
	 * Performs the Symol Hole daily.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runSymolHole(WebDriver driver) {
		logMessage("Starting runSymolHole");
		driver.get("http://www.neopets.com/medieval/symolhole.phtml");
		if (isElementPresentXP("//*[@id='content']/table/tbody/tr/td[2]/center/form/input[3]", driver)) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center/form/input[3]")).click();
			logMessage("Successfully ending runSymolHole");
			return true;
		}
		logMessage("Failed ending runSymolHole");
		return false;
	}

	/**
	 * Performs the Colzan's Shrine daily.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runColtzansShrine(WebDriver driver) {
		logMessage("Starting runColtzansShrine");
		driver.get("http://www.neopets.com/desert/shrine.phtml");
		if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/form[1]/input[2]", driver)) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/form[1]/input[2]")).click();
			logMessage("Successfully ending runColtzansShrine");
			return true;
		}
		logMessage("Failed ending runColtzansShrine");
		return false;
	}

	/**
	 * Place the max amount of Wishing Well wishes (7) for the minimum required amount (21np).
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runWishingWell(WebDriver driver) {
		logMessage("Starting runWishingWell");
		driver.get("http://www.neopets.com/wishing.phtml");

		for (int x = 0; x < 7; x++) {
			driver.findElement(By.name("donation")).clear();
			driver.findElement(By.name("donation")).sendKeys("21");
			driver.findElement(By.name("wish")).clear();
			driver.findElement(By.name("wish")).sendKeys(WISHING_WELL_ITEM);
			driver.findElement(By.xpath("//input[@value='Make a Wish']")).click();
		}
		logMessage("Successfully ending runWishingWell");
		return true;
	}

	/**
	 * Uses the user's active pet to perform the Fishing activity.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runFishing(WebDriver driver) {
		logMessage("Starting runFishing");
		driver.get("http://www.neopets.com/water/fishing.phtml");
		if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center[1]/form/input[2]", driver)) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center[1]/form/input[2]")).click();
			sleepMode(2000);
			if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center[2]/form/input", driver)) {
				driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center[2]/form/input")).click();
				logMessage("Successfully ending 1 runFishing");
				return true;
			}

			else if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center[3]/form/input", driver)) {
				driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center[3]/form/input")).click();
				logMessage("Successfully ending 2 runFishing");
				return true;
			}
			logMessage("Failed ending 1 runFishing");
			return false;
		}
		logMessage("Failed ending 2 runFishing");
		return false;
	}

	/**
	 * Collects the user's previous Grave Danger reward, then uses the user's designated petpet to run it again.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runGraveDanger(WebDriver driver) {
		logMessage("Starting runGraveDanger");
		driver.get("http://www.neopets.com/halloween/gravedanger/");

		//Received reward so clicking next adventure
		try {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form[1]/input[2]")).click();
		}
		catch(ElementNotVisibleException ex) {

		}

		//Click petpet to run
		driver.findElement(By.xpath("//*[@id=\"gdSelection\"]/div[" + GRAVE_PETPET + "]")).click();

		//Click Choose a petpet(starts adventure)
		driver.findElement(By.xpath("//*[@id=\"gdSelection\"]/button")).click();
		handleAlert(driver);

		sleepMode(5000);

		if (isElementPresentID("gdRemaining", driver)) {
			logMessage("Successfully ending runGraveDanger");
			return true;
		}
		logMessage("Failed ending runGraveDanger");
		return false;

	}

	/**
	 * Trains the user's active pet's stats evenly (double its level) at the Swashbuckling Academy.
	 * Handles completion, buying the necessary dubloon, and re-enrolling.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runDubloonTraining(WebDriver driver) {
		logMessage("Starting runDubloonTraining");
		driver.get("http://www.neopets.com/pirates/academy.phtml?type=status");

		if (isElementPresentXP("//input[@value='Complete Course!']", driver)) {
			driver.findElement(By.xpath("//input[@value='Complete Course!']")).click();
			driver.get("http://www.neopets.com/pirates/academy.phtml?type=status");
		}
		else if (driver.getPageSource().contains("Time till course finishes")){
			logMessage("Successfully ending 1 runDubloonTraining");
			return true;
		}

		int petIndex = 1;
		for (; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + petIndex + "]/td/b", driver); petIndex+=2) {
			String possiblePetName = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + petIndex + "]/td/b")).getText();
			if (possiblePetName.contains(PETNAME + " ")) {
				break;
			}
		}

		int level = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + (petIndex+1) + "]/td[1]/font/b")).getText());
		int str = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + (petIndex+1) + "]/td[1]/b[1]")).getText());
		int def = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + (petIndex+1) + "]/td[1]/b[2]")).getText());
		String tempHP = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + (petIndex+1) + "]/td[1]/b[4]")).getText();
		int hp = Integer.parseInt(tempHP.substring(tempHP.indexOf("/") + 1, tempHP.length()).trim());

		logMessage("dubloonTraining level: " + level);
		logMessage("dubloonTraining str: " + str);
		logMessage("dubloonTraining def: " + def);
		logMessage("dubloonTraining hp: " + hp);

		String trainingCoin = "";
		if (level <= 10) {
			trainingCoin = "One Dubloon Coin";
		}
		else if (level >= 11 && level <= 20) {
			trainingCoin = "Two Dubloon Coin";
		}
		else if (level >= 21 && level <= 30) {
			trainingCoin = "Five Dubloon Coin";
		}
		else if (level >= 31 && level < 40) {
			trainingCoin = "Five Dubloon Coin";
		}
		else {
			logMessage("Failed ending 1 runDubloonTraining");
			sendEmail("Level too high for dubloon training. Please change to island.");
			return false;
		}

		driver.get("http://www.neopets.com/quickstock.phtml");
		boolean needToBuyCoin = true;

		for (int x = 2; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
			String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]")).getText();
			if (invItem.contains(trainingCoin)) {
				needToBuyCoin = false;
				break;
			}
		}

		if (needToBuyCoin) {
			boolean doneShopping = false;
			while (!doneShopping) {

				if (!shopWizard(driver, trainingCoin)) {
					logMessage("Failed ending 2 runDubloonTraining");
					return false;
				}

				driver.get("http://www.neopets.com/quickstock.phtml");
				for (int x = 2; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
					String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]")).getText();
					if (invItem.contains(trainingCoin)) {
						doneShopping = true;
						break;
					}
				}
			}
			shopURLs.clear();
		} //End buying the coin

		driver.get("http://www.neopets.com/pirates/academy.phtml?type=courses");
		/*
		if (str >= level * 2 || def >= level * 2 || hp >= level * 2) {
			new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Level");
			logMessage("Dubloon train level");
		}
		else if (str <= def && str < (level * 2) - 1) {
			new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Strength");
			logMessage("Dubloon train str");
		}
		else if (def <= str && def < (level * 2) - 1) {
			new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Defence");
			logMessage("Dubloon train def");
		}
		else {
			new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Endurance");
			logMessage("Dubloon train hp");
		}
		 */
		//Doing this and commenting out the above because of cupcake training
		new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Level");
		logMessage("Dubloon train level");
		new Select(driver.findElement(By.name("pet_name"))).selectByValue(PETNAME);
		driver.findElement(By.xpath("//input[@value='Start Course']")).click();
		driver.get("http://www.neopets.com/pirates/academy.phtml?type=status");
		driver.findElement(By.xpath("//input[@value='Pay']")).click();

		logMessage("Successfully ending 2 runDubloonTraining");
		return true;
	}

	/**
	 * Trains the user's active pet's stats evenly (double its level) at the Mystery Island Training School.
	 * Handles completion, buying the necessary codestones, and re-enrolling.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runIslandTraining(WebDriver driver) {
		logMessage("Starting runIslandTraining");
		//Following Quickest method here http://www.neopets.com/~Alluar (except we only double HP)
		driver.get("http://www.neopets.com/island/training.phtml?type=status");

		if (isElementPresentXP("//input[@value='Complete Course!']", driver)) {
			driver.findElement(By.xpath("//input[@value='Complete Course!']")).click();
			driver.get("http://www.neopets.com/island/training.phtml?type=status");
		}
		else if (driver.getPageSource().contains("Time till course finishes")){
			logMessage("Successfully ending 1 runIslandTraining");
			return true;
		}

		int petIndex = 1;
		for (; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + petIndex + "]/td/b", driver); petIndex+=2) {
			String possiblePetName = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + petIndex + "]/td/b")).getText();
			if (possiblePetName.contains(PETNAME + " ")) {
				break;
			}
		}

		int level = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + (petIndex+1) + "]/td[1]/font/b")).getText());
		int str = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + (petIndex+1) + "]/td[1]/b[1]")).getText());
		int def = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + (petIndex+1) + "]/td[1]/b[2]")).getText());
		String tempHP = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + (petIndex+1) + "]/td[1]/b[4]")).getText();
		int hp = Integer.parseInt(tempHP.substring(tempHP.indexOf("/") + 1, tempHP.length()).trim());

		logMessage("islandTraining level: " + level);
		logMessage("islandTraining str: " + str);
		logMessage("islandTraining def: " + def);
		logMessage("islandTraining hp: " + hp);

		driver.get("http://www.neopets.com/island/training.phtml?type=courses");

		if (level <= 80) {
			if (str >= level * 2 || def >= level * 2 || hp >= level * 2) {
				new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Level");
				logMessage("Island train level");
			}
			else if (str <= def && str < (level * 2) - 1) {
				new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Strength");
				logMessage("Island train str");
			}
			else if (def <= str && def < (level * 2) - 1) {
				new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Defence");
				logMessage("Island train def");
			}
			else {
				new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Endurance");
				logMessage("Island train hp");
			}
		}
		else {
			new Select(driver.findElement(By.name("course_type"))).selectByVisibleText("Level");
			logMessage("Island train level");
		}

		new Select(driver.findElement(By.name("pet_name"))).selectByValue(PETNAME);
		driver.findElement(By.xpath("//input[@value='Start Course']")).click();
		driver.get("http://www.neopets.com/island/training.phtml?type=status");

		ArrayList<String> codestonesNeeded = new ArrayList<String>();
		for (int x = 1; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[2]/td[2]/p/b[" + x + "]", driver); x++) {
			codestonesNeeded.add(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[2]/td[2]/p/b[" + x + "]")).getText().trim());
		}

		driver.get("http://www.neopets.com/quickstock.phtml");
		for (int x = 2; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
			String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]")).getText();
			for (int y = 0; y < codestonesNeeded.size(); y++) {
				if (invItem.contains(codestonesNeeded.get(y))) {
					codestonesNeeded.remove(y);
					break;
				}
			}
		}

		boolean doneShopping = false;

		while (!doneShopping) {

			for (int x = 0; x < codestonesNeeded.size(); x++) {
				if (!shopWizard(driver, codestonesNeeded.get(x))) {
					logMessage("Failed ending runIslandTraining");
					return false;
				}
			}

			driver.get("http://www.neopets.com/quickstock.phtml");
			for (int x = 2; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
				String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]")).getText();
				for (int y = 0; y < codestonesNeeded.size(); y++) {
					if (invItem.contains(codestonesNeeded.get(y))) {
						codestonesNeeded.remove(y);
						break;
					}
				}
			}

			if (codestonesNeeded.size() == 0) {
				doneShopping = true;
			}

		}

		shopURLs.clear();

		driver.get("http://www.neopets.com/island/training.phtml?type=status");

		driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[2]/td[2]/a/b")).click();

		logMessage("Successfully ending 2 runIslandTraining");
		return true;
	}

	/**
	 * Grabs a (hopefully) non-garbage item from the Second Hand Shoppe.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runSecondHandShoppe(WebDriver driver) {
		logMessage("Starting runSecondHandShoppe");
		driver.get("http://www.neopets.com/thriftshoppe/index.phtml");

		for (int x = 2; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + x + "]/td[1]/a/div[1]/img", driver); x++) {
			for (int y = 1; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + x + "]/td[" + y + "]/a/div[1]/img", driver); y++) {
				String itemImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + x + "]/td[" + y + "]/a/div[1]/img")).getAttribute("src").trim();
				logMessage("runSecondHandShoppe itemImg: " + itemImg);
				if (!itemImg.equals("http://images.neopets.com/items/med_booby_5.gif") &&
						!itemImg.equals("http://images.neopets.com/items/vor_mossy_rock.gif") &&
						!itemImg.equals("http://images.neopets.com/items/newbie_potatosack.gif") &&
						!itemImg.equals("http://images.neopets.com/items/bkg_oceanrockbackground.gif")) {
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + x + "]/td[" + y + "]/a/div[1]/img")).click();
					break;
				}
			}
		}
		logMessage("Successfully ending runSecondHandShoppe");
		return true;
	}

	/**
	 * Visits Tarla's non-Toolbar daily.
	 * Because this daily is random, it gets checked every run.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runTarla(WebDriver driver) {
		logMessage("Starting runTarla");
		driver.get("http://www.neopets.com/freebies/tarlastoolbar.phtml");

		if (isElementPresentXP("//*[@id=\"header\"]/table/tbody/tr[1]/td[1]/a/img", driver)) {
			logMessage("Successfully ending runTarla");
			return true;
		}
		logMessage("Failed ending runTarla");
		return false;
	}

	/**
	 * Grabs any NPs in the user's shop till.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runShopTill(WebDriver driver) {
		logMessage("Starting runShopTill");
		driver.get("http://www.neopets.com/market.phtml?type=till");
		if (isElementPresentXP("//input[@value='Withdraw']", driver)) {
			String npString = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/p[1]/b")).getText();
			int npToWithdraw = Integer.parseInt(npString.replace(",", "").replace(" NP", ""));
			if (npToWithdraw > 0) {
				driver.findElement(By.name("amount")).clear();
				driver.findElement(By.name("amount")).sendKeys("" + npToWithdraw);
				driver.findElement(By.xpath("//input[@value='Withdraw']")).click();
			}
			logMessage("Successfully ending runShopTill");
			return true;
		}
		else {
			logMessage("Failed ending runShopTill");
			return false;
		}
	}

	/**
	 * Checks if the user has less than 30 items in their shop.
	 * If they do, items are grabbed from their SDB and put in there to make it 30.
	 * If the user is running stock, the new items (and any item priced 0) are then priced.
	 * If they're running reprice, every item is then priced.
	 * @param driver The WebDriver
	 * @param callType The string telling whether to price just what is newly stocked, or to reprice everything.
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 */
	private static boolean runStockItems(WebDriver driver, String callType) {
		logMessage("Starting runStockItems");
		driver.get("http://www.neopets.com/market.phtml?type=your");
		int stockedItems = 0;
		if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[1]/b[2]", driver)) {
			stockedItems = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center[1]/b[2]")).getText());
		}
		if (stockedItems < 30) {
			int itemsNeeded = 30 - stockedItems;

			ArrayList<String> removedItems = new ArrayList<String>();

			driver.get("http://www.neopets.com/safetydeposit.phtml");
			int n = 2;
			while (itemsNeeded > 0) {
				if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[" + n + "]/td[2]/b", driver)) {
					String storedItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[" + n + "]/td[2]/b")).getText();
					boolean isIgnoredItem = false;
					for (int ii = 0; ii < IGNORED_ITEMS.size(); ii++) {
						if (storedItem.contains(IGNORED_ITEMS.get(ii))) {
							isIgnoredItem = true;
							break;
						}
					}
					if (!isIgnoredItem) {
						int itemCount = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[" + n + "]/td[5]/b")).getText());
						storedItem = storedItem.replace("\n", "").trim();
						if (storedItem.endsWith(")")) {
							storedItem = storedItem.substring(0, storedItem.indexOf("(")).trim();
						}
						logMessage("runStockItems storedItem: " + storedItem);
						removedItems.add(storedItem);
						if (itemCount > itemsNeeded) {
							driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[" + n + "]/td[6]/input")).clear();
							driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[" + n + "]/td[6]/input")).sendKeys(itemsNeeded + "");
							driver.findElement(By.xpath("//input[@value='Move Selected Items']")).click();
							break;
						}
						else {
							driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[" + n + "]/td[6]/input")).clear();
							driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[" + n + "]/td[6]/input")).sendKeys(itemCount + "");
							itemsNeeded = itemsNeeded - itemCount;
						}
					}
					n++;
				}
				else {
					break;
				}
			}
			if (isElementPresentXP("//input[@value='Move Selected Items']", driver)) {
				driver.findElement(By.xpath("//input[@value='Move Selected Items']")).click();
			}

			itemsNeeded = 30 - stockedItems;
			int itemsClicked = 0;
			logMessage("runStockItems going to quickstock to put items in shop");
			driver.get("http://www.neopets.com/quickstock.phtml");
			for (n = 2; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]", driver); n++) {
				if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[1]", driver) && isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[2]/input", driver)) {
					String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[1]")).getText().trim();
					if (invItem.endsWith(")")) {
						invItem = invItem.substring(0, invItem.indexOf("(")).trim();
					}
					logMessage("runStockItems invItem: " + invItem);
					if (invItem.length() > 1 && removedItems.contains(invItem) && itemsClicked < itemsNeeded) {
						itemsClicked++;
						driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[2]/input")).click();
					}
				}
				else {
					break;
				}
			}

			if (isElementPresentXP("//input[@value='Submit']", driver)) {
				driver.findElement(By.xpath("//input[@value='Submit']")).click();
				handleAlert(driver);
			}
			driver.get("http://www.neopets.com/market.phtml?type=your");
		}

		ArrayList<String> itemsToBePriced = new ArrayList<String>();
		ArrayList<Integer> stockedPrices = new ArrayList<Integer>();
		logMessage("runStockItems about to get items");
		for (int shopItems = 2; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + shopItems + "]/td[5]/input", driver); shopItems++) {
			if (callType.equals("Stock")) {
				if (Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + shopItems + "]/td[5]/input")).getAttribute("value").trim()) == 0) {
					itemsToBePriced.add(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + shopItems + "]/td[1]")).getText());
				}
			}
			else {
				String stockedItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + shopItems + "]/td[1]")).getText();
				boolean isIgnoredItem = false;
				for (int ii = 0; ii < IGNORED_ITEMS.size(); ii++) {
					if (stockedItem.contains(IGNORED_ITEMS.get(ii))) {
						isIgnoredItem = true;
						break;
					}
				}
				if (!isIgnoredItem) {
					itemsToBePriced.add(stockedItem);
				}
			}
		}

		logMessage("runStockItems done getting items");

		for (int x = 0; x < itemsToBePriced.size(); x++) {
			driver.get("http://www.neopets.com/market.phtml?type=wizard");

			if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/p[1]/a[1]", driver)) { //Faerie quest link
				logMessage("runStockItems FQ found");

				if (!faerieQuest(driver)) {
					logMessage("Failed ending runStockItems");
					return false;
				}

				driver.get("http://www.neopets.com/market.phtml?type=wizard");
			}

			driver.findElement(By.name("shopwizard")).clear();
			driver.findElement(By.name("shopwizard")).sendKeys(itemsToBePriced.get(x));

			new Select(driver.findElement(By.name("criteria"))).selectByVisibleText("identical to my phrase");
			driver.findElement(By.cssSelector("option[value=\"exact\"]")).click();
			driver.findElement(By.cssSelector("div > input[type=\"submit\"]")).click();

			int ourPrice = 999999;
			for (int refreshCnt = 0; refreshCnt < 3; refreshCnt++) {
				if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[4]/td[4]/b", driver)) { //search
					String secondItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[3]/td[4]/b")).getText();
					secondItem = secondItem.replace(" NP", "").replaceAll(",", "");
					int secondResult = Integer.parseInt(secondItem);
					if (secondResult < ourPrice) {
						ourPrice = secondResult;
					}
				}

				driver.navigate().refresh();
				handleAlert(driver);
				sleepMode(3000);
			}

			if (ourPrice == 999999) {
				stockedPrices.add(-1);
			}
			else {
				stockedPrices.add(ourPrice - 1);
			}
		}

		driver.get("http://www.neopets.com/market.phtml?type=your");

		ArrayList<String> removedItems = new ArrayList<String>();

		for (int x = 0; x < itemsToBePriced.size(); x++) {
			for (int n = 2; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[1]", driver); n++) {
				if (driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[1]")).getText().equals(itemsToBePriced.get(x))) {
					int price = stockedPrices.get(x);
					if (stockedPrices.get(x) != -1) {
						if (price > MINIMUM_KEEP_VALUE) {
							driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[5]/input")).clear();
							driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[5]/input")).sendKeys(String.valueOf(stockedPrices.get(x)));
						}
						else {
							List<WebElement> allOptions = new Select(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[7]/select"))).getOptions();
							new Select(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + n + "]/td[7]/select"))).selectByVisibleText(allOptions.get(allOptions.size() - 1).getText());
							removedItems.add(itemsToBePriced.get(x));
						}
					}
				}
			}
		}

		driver.findElement(By.xpath("//input[@value='Update']")).click();

		if (removedItems.size() > 0) {
			driver.get("http://www.neopets.com/quickstock.phtml");

			for (int x = 2; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
				if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[1]", driver) && isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[4]/input", driver)) {
					String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[1]")).getText();
					if (invItem.length() > 1) {
						if (removedItems.contains(invItem)) {
							driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]/td[4]/input")).click();
						}
					}
				}
			}

			driver.findElement(By.xpath("//input[@value='Submit']")).click();
			handleAlert(driver);
		}

		if (callType.equals("Reprice")) {
			driver.get("http://www.neopets.com/market.phtml?type=sales");
			if (isElementPresentXP("//input[@value='Clear Sales History']", driver)) {
				driver.findElement(By.xpath("//input[@value='Clear Sales History']")).click();
			}
		}

		logMessage("Successfully ending runStockItems");
		return true;
	}

	/**
	 * Handles Snowager, depositing any money into the user's bank at the end of the day, and collecting the user's food club winnings.
	 * @param driver The WebDriver
	 * @param xmlNode The name of the node in the user's PreviousRuns.xml the program is looking for
	 * @param validHours The hour(s) that the program uses for the specified xmlNode
	 * @see NeoDailies#oncePerDay(WebDriver, String, String) oncePerDay
	 * @see NeoDailies#oncePerInterval(WebDriver, String, long) oncePerInterval
	 * @throws IOException Any IOException that occurred in the called methods that needs to be handled within main.
	 */
	private static void oncePerSchedule(WebDriver driver, String xmlNode, int[] validHours) throws IOException {
		GregorianCalendar gc = new GregorianCalendar();
		int hour = gc.get(Calendar.HOUR_OF_DAY);
		logMessage("oncePerSchedule hour: " + hour);

		if (xmlNode.equals("Snowager")) {
			if (IntStream.of(validHours).anyMatch(x -> x == hour)) {
				driver.get("http://www.neopets.com/winter/snowager.phtml");
				if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td/p[4]/a", driver)) {
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td/p[4]/a")).click();
				}
			}
		}

		else if (xmlNode.equals("Deposit")) {
			if (IntStream.of(validHours).anyMatch(x -> x == hour)) {
				logMessage("Starting Deposit");
				driver.get("http://www.neopets.com/bank.phtml");
				String npString = driver.findElement(By.xpath("//*[@id=\"npanchor\"]")).getText();
				int npToDeposit = Integer.parseInt(npString.replace(",", "").replace(" NP", ""));
				if (npToDeposit > 10000) {
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table[1]/tbody/tr/td[1]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/form/input[2]")).clear();
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table[1]/tbody/tr/td[1]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/form/input[2]")).sendKeys("" + (npToDeposit - 10000));
					driver.findElement(By.xpath("//input[@value='Deposit']")).click();
					handleAlert(driver);
				}

				Date dateCurrent = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String currentDate = format.format(dateCurrent);

				String currentNP = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[2]/td/table/tbody/tr[2]/td[2]")).getText();
				currentNP = currentNP.replace(",", "").replace("NP", "").trim();

				String depositHistory = "";
				String fileLine = "";
				int yesterdayNP = 0;
				try (BufferedReader br = new BufferedReader(new FileReader("MoneyTracking.csv"))) {
					while ((fileLine = br.readLine()) != null) {
						if (!fileLine.contains(currentDate)) {
							depositHistory = depositHistory + fileLine + "\n";
							try {
								yesterdayNP = Integer.parseInt(fileLine.split(",")[1]);
							}
							catch(NumberFormatException ex) {

							}
						}
					}
				}

				File resultFile = new File("MoneyTracking.csv");
				PrintWriter writer = new PrintWriter(resultFile);
				writer.close();

				@SuppressWarnings("resource")
				FileWriter resultWriter = new FileWriter(resultFile, true);
				resultWriter.append(depositHistory + currentDate + "," + currentNP + "," + (Integer.parseInt(currentNP) - yesterdayNP) + "\n");
				resultWriter.flush();

				logMessage("Successfully ending Deposit");
			}
		}

		else if (xmlNode.equals("CollectWinnings")) {
			if (IntStream.of(validHours).anyMatch(x -> x == hour)) {
				logMessage("Starting CollectWinnings");
				driver.get("http://www.neopets.com/pirates/foodclub.phtml?type=collect");

				if (isElementPresentXP("//input[@value='Collect Your Winnings!']", driver)) {
					driver.findElement(By.xpath("//input[@value='Collect Your Winnings!']")).click();
				}

				logMessage("Successfully ending CollectWinnings");
			}
		}

		sleepMode(5000);

	}

	/**
	 * Checks for and handles any alerts Neopets gives.
	 * @param driver The WebDriver
	 */
	private static void handleAlert(WebDriver driver) {
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
		}
		catch(NoAlertPresentException e) {}
	}

	/**
	 * Checks if an element is present using the element's id.
	 * @param id The ID of the element
	 * @param driver The WebDriver
	 * @return The boolean telling if the element is there or not
	 * @see NeoDailies#isElementPresentLT(String, WebDriver) isElementPresentLT
	 * @see NeoDailies#isElementPresentName(String, WebDriver) isElementPresentName
	 * @see NeoDailies#isElementPresentXP(String, WebDriver) isElementPresentXP
	 */
	private static boolean isElementPresentID(String id, WebDriver driver) {
		boolean present;
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		present = driver.findElements(By.id(id)).size() != 0;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return present;
	}

	/**
	 * Checks if an element is present using the element's link text.
	 * @param lt The link text of the element
	 * @param driver The WebDriver
	 * @return The boolean telling if the element is there or not
	 * @see NeoDailies#isElementPresentID(String, WebDriver) isElementPresentID
	 * @see NeoDailies#isElementPresentName(String, WebDriver) isElementPresentName
	 * @see NeoDailies#isElementPresentXP(String, WebDriver) isElementPresentXP
	 */
	private static boolean isElementPresentLT(String lt, WebDriver driver) {
		boolean present;
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		present = driver.findElements(By.linkText(lt)).size() != 0;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return present;
	}

	/**
	 * Checks if an element is present using the element's name.
	 * @param name The name of the element
	 * @param driver The WebDriver
	 * @return The boolean telling if the element is there or not
	 * @see NeoDailies#isElementPresentID(String, WebDriver) isElementPresentID
	 * @see NeoDailies#isElementPresentLT(String, WebDriver) isElementPresentLT
	 * @see NeoDailies#isElementPresentXP(String, WebDriver) isElementPresentXP
	 */
	private static boolean isElementPresentName(String name, WebDriver driver) {
		boolean present;
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		present = driver.findElements(By.name(name)).size() != 0;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return present;
	}

	/**
	 * Checks if an element is present using the element's xpath.
	 * @param xpath The xpath of the element
	 * @param driver The WebDriver
	 * @return The boolean telling if the element is there or not
	 * @see NeoDailies#isElementPresentID(String, WebDriver) isElementPresentID
	 * @see NeoDailies#isElementPresentLT(String, WebDriver) isElementPresentLT
	 * @see NeoDailies#isElementPresentName(String, WebDriver) isElementPresentName
	 */
	private static boolean isElementPresentXP(String xpath, WebDriver driver) {
		boolean present;
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		present = driver.findElements(By.xpath(xpath)).size() != 0;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return present;
	}

	/**
	 * Log any messages into the program's log file located in the working directory.
	 * The log message will contain a timestamp associated with it.
	 * @param message The message that gets logged into the log file
	 */
	private static void logMessage(String message) {
		try {
			Date logDate = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String logDateString = format.format(logDate);
			logWriter.append("[" + logDateString + "] " + message + "\n");
			logWriter.flush();
		}
		catch(IOException e) {
			System.out.println("IOException in logMessage");
			e.printStackTrace();
		}
	}

	/**
	 * Pauses the current thread for the specified number of milliseconds.
	 * @param pauseLen The length of time to pause in milliseconds
	 */
	private static void sleepMode(int pauseLen) {
		try {
			Thread.sleep(pauseLen);
		}
		catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Used by runLottery to generate an array of numbers.
	 * @param a The first array of numbers
	 * @param b second first array of numbers
	 * @return The new array
	 * @see NeoDailies#runLottery(WebDriver) runLottery
	 */
	private static int[] leaf(int[] a, int[] b) {
		int[] res = new int[30];
		for (int i = 0; i < a.length; i++) {
			res[i * 2] = a[i];
			res[i * 2 + 1] = b[i];
		}
		return res;
	}

	/**
	 * Used by runLottery to randomize (shuffle) an array.
	 * @param o The array of numbers
	 * @return The newly shuffled array
	 * @see NeoDailies#runLottery(WebDriver) runLottery
	 */
	private static int[] shuffle(int[] o) {
		int j = 0;
		int x = 0;
		int i = o.length;
		while (i > 0) {
			j = (int) Math.floor((Math.random() * i));
			x = o[--i];
			o[i] = o[j];
			o[j] = x;
		}
		return o;
	}

	/**
	 * Completes the Faerie Quest the user is currently on.
	 * @param driver The WebDriver
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#runFaeries(WebDriver) runFaeries
	 * @see NeoDailies#runStockItems(WebDriver, String) runStockItems
	 * @see NeoDailies#shopWizard(WebDriver, String) shopWizard
	 */
	private static boolean faerieQuest(WebDriver driver) {
		while (true) {

			try {

				driver.get("http://www.neopets.com/quests.phtml");

				String questItemLink = "";

				String questItem = driver.findElement(By.xpath("//*[@id=\"fq2\"]/div[3]/table[1]/tbody/tr/td/b")).getText().trim();
				String resultLine = "";

				URL apiUrl = new URL("http://www.neocodex.us/forum/index.php?app=itemdb&module=search?app=itemdb&module=search&section=search&item=%22" + questItem.replace(" ", "+") + "%22&description=&rarity_low=&rarity_high=&price_low=&price_high=&shop=&search_order=price&sort=asc&lim=20");
				HttpURLConnection apiCon = (HttpURLConnection) apiUrl.openConnection();
				apiCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
				InputStream apiInStream = apiCon.getInputStream();
				InputStreamReader apiInStreamReader = new InputStreamReader(apiInStream);
				BufferedReader apiBufRead = new BufferedReader(apiInStreamReader);

				while ((resultLine = apiBufRead.readLine()) != null) {
					if (resultLine.contains("<li style='margin-top: 30px; width: 110px;'>")) {
						resultLine = apiBufRead.readLine();
						resultLine = resultLine.substring(resultLine.indexOf("\"") + 1, resultLine.lastIndexOf("\""));
						questItemLink = resultLine;
						break;
					}
				}

				ArrayList<String> itemShopLinks = new ArrayList<String>();
				boolean atShops = false;

				URL itemUrl = new URL(questItemLink);
				HttpURLConnection itemCon = (HttpURLConnection) itemUrl.openConnection();
				itemCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
				InputStream itemInStream = itemCon.getInputStream();
				InputStreamReader itemInStreamReader = new InputStreamReader(itemInStream);
				BufferedReader itemBufRead = new BufferedReader(itemInStreamReader);

				while ((resultLine = itemBufRead.readLine()) != null) {

					if (atShops) {
						if (resultLine.contains("a href")) {
							resultLine = resultLine.substring(resultLine.indexOf("'") + 1, resultLine.lastIndexOf("'"));
							itemShopLinks.add(resultLine);
						}
						else {
							break;
						}
					}
					if (resultLine.contains("<h3 class='bar'>Shop Owners</h3>")) {
						atShops = true;
					}
				}

				boolean doneShopping = false;
				int buyingAttempts = 0;
				while (!doneShopping) {
					if (itemShopLinks.size() == 0) {
						sendEmail("On a Faerie quest NO ITEM");
						return false;
					}
					else if (buyingAttempts >= 5) {
						sendEmail("On a Faerie quest CAN'T BUY ANYTHING");
						return false;
					}
					for (int shopLinkCnt = 0; shopLinkCnt < itemShopLinks.size(); shopLinkCnt++) {
						driver.get(itemShopLinks.get(shopLinkCnt));
						if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[4]/table/tbody/tr/td/a/img", driver)) {
							try {
								driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[4]/table/tbody/tr/td/a/img")).click();
								handleAlert(driver);
							}
							catch(Exception ex) {

							}
						}
						sleepMode(5000);

						driver.get("http://www.neopets.com/quickstock.phtml");
						for (int x = 2; isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]", driver); x++) {
							String invItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[" + x + "]")).getText();
							if (invItem.contains(questItem)) {
								doneShopping = true;
								shopLinkCnt = 99999;
								break;
							}
						}
					}
					buyingAttempts++;
				}

				driver.get("http://www.neopets.com/quests.phtml");
				if (isElementPresentID("complete_faerie_quest", driver)) {
					driver.findElement(By.id("complete_faerie_quest")).click();
				}
				return true;
			}
			catch(Exception ex) {
				//This doesn't need its exception printed. This is normal.
				sleepMode(15000);
			}
		}
	}

	/**
	 * Finds the best price for the item the user is trying to buy, and purchases it.
	 * @param driver The WebDriver
	 * @param itemBuying The item that is currently trying to be bought.
	 * @return A boolean telling if the activity successfully finished or not.
	 * @see NeoDailies#runCoincidence(WebDriver) runCoincidence
	 * @see NeoDailies#runEmployment(WebDriver) runEmployment
	 * @see NeoDailies#runKitchenQuest(WebDriver) runKitchenQuest
	 * @see NeoDailies#runDubloonTraining(WebDriver) runDubloonTraining
	 * @see NeoDailies#runIslandTraining(WebDriver) runIslandTraining
	 */
	private static boolean shopWizard(WebDriver driver, String itemBuying) {

		driver.get("http://www.neopets.com/safetydeposit.phtml");
		if (isElementPresentName("obj_name", driver)) {
			driver.findElement(By.name("obj_name")).clear();
			driver.findElement(By.name("obj_name")).sendKeys(itemBuying);
			driver.findElement(By.xpath("//input[@value='Find']")).click();
		}
		if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[2]/td[5]/b", driver)) {
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[2]/td[6]/input")).clear();
			driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[2]/td[6]/input")).sendKeys(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table[2]/tbody/tr[2]/td[5]/b")).getText());
			driver.findElement(By.xpath("//input[@value='Move Selected Items']")).click();
			return true;
		}

		driver.get("http://www.neopets.com/market.phtml?type=wizard");

		if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/p[1]/a[1]", driver)) { //Faerie quest link
			logMessage("ShopWizard FQ found");

			if (!faerieQuest(driver)) {
				return false;
			}

			driver.get("http://www.neopets.com/market.phtml?type=wizard");
		}

		if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div[2]/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/form/table/tbody/tr[1]/td[2]/input", driver)) {
			ArrayList<String> shopLinks = new ArrayList<String>();
			ArrayList<Integer> shopPrices = new ArrayList<Integer>();

			driver.findElement(By.name("shopwizard")).clear();
			driver.findElement(By.name("shopwizard")).sendKeys(itemBuying);

			new Select(driver.findElement(By.name("criteria"))).selectByVisibleText("identical to my phrase");
			driver.findElement(By.cssSelector("option[value=\"exact\"]")).click();
			driver.findElement(By.cssSelector("div > input[type=\"submit\"]")).click();

			for (int refreshCnt = 0; refreshCnt < 4; refreshCnt++) {
				if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[4]/td[4]/b", driver)) { //search
					if (!shopLinks.contains(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[2]/td[1]/a")).getAttribute("href"))) {
						String firstItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[2]/td[4]/b")).getText();
						String secondItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[3]/td[4]/b")).getText();
						String thirdItem = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[4]/td[4]/b")).getText();

						firstItem = firstItem.replace(" NP", "").replaceAll(",", "");
						secondItem = secondItem.replace(" NP", "").replaceAll(",", "");
						thirdItem = thirdItem.replace(" NP", "").replaceAll(",", "");

						int firstResult = Integer.parseInt(firstItem);
						int secondResult = Integer.parseInt(secondItem);
						int thirdResult = Integer.parseInt(thirdItem);

						shopLinks.add(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[2]/td[1]/a")).getAttribute("href"));
						shopLinks.add(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[3]/td[1]/a")).getAttribute("href"));
						shopLinks.add(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table[2]/tbody/tr[4]/td[1]/a")).getAttribute("href"));
						shopPrices.add(firstResult);
						shopPrices.add(secondResult);
						shopPrices.add(thirdResult);
					}

					if (refreshCnt != 3) {
						driver.navigate().refresh();
						handleAlert(driver);
						sleepMode(3000);
					}
				} //end first search
			}

			Collections.sort(shopPrices);

			String shopLink = "";
			int priceIndex = 0;

			for (int linkCnt = 0; linkCnt < shopLinks.size(); linkCnt++) {
				String link = shopLinks.get(linkCnt);
				if (link.endsWith(String.valueOf(shopPrices.get(priceIndex)))) {
					if (!shopURLs.contains(link)) {
						shopLink = link;
						break;
					}
					else {
						linkCnt = -1;
						priceIndex++;
					}

				}
			}
			logMessage("ShopLink: " + shopLink);
			shopURLs.add(shopLink);
			driver.get(shopLink);
			if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[4]/table/tbody/tr/td/a/img", driver)) {
				try {
					driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[4]/table/tbody/tr/td/a/img")).click();
					handleAlert(driver);
				}
				catch(Exception ex) {
					//This error is normal, and does not need handling
				}
			}
			sleepMode(5000);
		}
		return true;
	}

	/**
	 * Process the drawn card, sending it to an empty space or a pile.
	 * @param driver The WebDriver
	 * @see NeoDailies#runSolitaire(WebDriver) runSolitaire
	 * @see NeoDailies#processBottomCards(WebDriver) processBottomCards
	 * @see NeoDailies#processTopCards(WebDriver) processTopCards
	 */
	private static void processDrawnCard(WebDriver driver) {
		boolean drawnCardPlayed = false;
		while (!drawnCardPlayed) {
			if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/a/img", driver)) {
				String drawnCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/a/img")).getAttribute("src");
				String drawnCardID = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/a/img")).getAttribute("id");
				int drawnCardNum = -1;
				String drawnCardSuit = "";
				if (drawnCardID.contains("_")) {
					drawnCardSuit = drawnCardID.substring(drawnCardID.indexOf("_") + 1, drawnCardID.length());
					drawnCardNum = Integer.parseInt(drawnCardID.substring(0, drawnCardID.indexOf("_")));
				}
				String drawnCardColor = "";
				if (drawnCardSuit.equals("spades") || drawnCardSuit.equals("clubs")) {
					drawnCardColor = "black";
				}
				else if (drawnCardSuit.equals("hearts") || drawnCardSuit.equals("diamonds")) {
					drawnCardColor = "red";
				}

				logMessage("processDrawnCard drawnCardImg: " + drawnCardImg);

				for (int x = 5; x < 9; x++) {
					String acePileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[" + x + "]/a/img")).getAttribute("src");

					if (acePileCardImg.contains("new_open")) {
						if (drawnCardNum == 14) {
							driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/a/img")).click();
							driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[" + x + "]/a/img")).click();
							drawnCardPlayed = true;
							madeMove = true;
							break;
						}
					}
					else {
						int acePileCardNum = Integer.parseInt(acePileCardImg.substring(acePileCardImg.lastIndexOf("/") + 1, acePileCardImg.lastIndexOf("_")));
						String acePileCardSuit = acePileCardImg.substring(acePileCardImg.lastIndexOf("_") + 1, acePileCardImg.lastIndexOf("."));

						if (((acePileCardNum == 14 && drawnCardNum == 2) && drawnCardSuit.equals(acePileCardSuit)) || (acePileCardNum == drawnCardNum - 1 && drawnCardSuit.equals(acePileCardSuit))) {
							driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/a/img")).click();
							driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[" + x + "]/a/img")).click();
							drawnCardPlayed = true;
							madeMove = true;
							break;
						}
					}
				}

				if (!drawnCardPlayed) { //Let's put our drawn card into a pile!
					for (int x = 2; x < 9; x++) {
						int y = 1;
						while (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img", driver)) {
							y++;
						}
						y--;
						String normalPileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img")).getAttribute("src");
						if (normalPileCardImg.contains("new_blank_card")) {
							if (drawnCardNum == 13) {
								driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/a/img")).click();
								driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img")).click();
								drawnCardPlayed = true;
								madeMove = true;
								break;
							}
						}
						else {
							int normalPileCardNum = Integer.parseInt(normalPileCardImg.substring(normalPileCardImg.lastIndexOf("/") + 1, normalPileCardImg.lastIndexOf("_")));
							String normalPileCardSuit = normalPileCardImg.substring(normalPileCardImg.lastIndexOf("_") + 1, normalPileCardImg.lastIndexOf("."));
							String normalPileCardColor = "";
							if (normalPileCardSuit.equals("spades") || normalPileCardSuit.equals("clubs")) {
								normalPileCardColor = "black";
							}
							else if (normalPileCardSuit.equals("hearts") || normalPileCardSuit.equals("diamonds")) {
								normalPileCardColor = "red";
							}
							if (normalPileCardNum == drawnCardNum + 1 && !normalPileCardColor.equals(drawnCardColor) && normalPileCardNum != 14) {
								driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/a/img")).click();
								driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img")).click();
								drawnCardPlayed = true;
								madeMove = true;
								break;
							}
						}
					}
				}
				if (drawnCardPlayed) {
					drawnCardPlayed = false;
				}
				else {
					drawnCardPlayed = true;
				}
			}
			else {
				drawnCardPlayed = true;
			}
		}
	}

	/**
	 * Process the cards at the bottom of piles, sending them to empty spaces or ace piles.
	 * @param driver The WebDriver
	 * @see NeoDailies#runSolitaire(WebDriver) runSolitaire
	 * @see NeoDailies#processDrawnCard(WebDriver) processDrawnCard
	 * @see NeoDailies#processTopCards(WebDriver) processTopCards
	 */
	private static void processBottomCards(WebDriver driver) {
		for (int x = 2; x < 9; x++) {
			logMessage("processBottomCards processing bottom card");
			int y = 1;
			while (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img", driver)) {
				y++;
			}
			y--;

			String bottomPileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img")).getAttribute("src");
			if (!bottomPileCardImg.contains("new_blank_card")) {
				int bottomPileCardNum = Integer.parseInt(bottomPileCardImg.substring(bottomPileCardImg.lastIndexOf("/") + 1, bottomPileCardImg.lastIndexOf("_")));

				if (bottomPileCardNum == 14) {
					for (int n = 5; n < 9; n++) {
						String acePileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[" + n + "]/a/img")).getAttribute("src");

						if (acePileCardImg.contains("new_open")) {
							driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img")).click();
							driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[" + n + "]/a/img")).click();
							madeMove = true;
							x = 1;
							break;
						}
					}
				}
				else if (bottomPileCardNum == 13 && isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/img", driver)) {
					for (int m = 2; m < 9; m++) {
						int n = 1;
						while (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img", driver)) {
							n++;
						}
						n--;
						String possiblyEmptyPileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img")).getAttribute("src");
						if (possiblyEmptyPileCardImg.contains("new_blank_card")) {
							driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img")).click();
							driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img")).click();
							madeMove = true;
							x = 1;
							break;
						}
					}
				}
				else { //The card in the bottom of the pile wasn't an ace or a king. Check if it can go into an Ace pile
					String bottomPileCardSuit = bottomPileCardImg.substring(bottomPileCardImg.lastIndexOf("_") + 1, bottomPileCardImg.lastIndexOf("."));
					logMessage("processBottomCards This is the bottom card: " + bottomPileCardImg);
					for (int n = 5; n < 9; n++) {
						String acePileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[" + n + "]/a/img")).getAttribute("src");
						logMessage("processBottomCards this is the acePileCardImg: " + acePileCardImg);
						if (!acePileCardImg.contains("new_open")) {
							String acePileCardSuit = acePileCardImg.substring(acePileCardImg.lastIndexOf("_") + 1, acePileCardImg.lastIndexOf("."));
							int acePileCardNum = Integer.parseInt(acePileCardImg.substring(acePileCardImg.lastIndexOf("/") + 1, acePileCardImg.lastIndexOf("_")));
							if (bottomPileCardSuit.equals(acePileCardSuit) && (acePileCardNum == bottomPileCardNum - 1 || acePileCardNum == bottomPileCardNum + 12)) {
								driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img")).click();
								driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[" + n + "]/a/img")).click();
								madeMove = true;
								x = 1;
								break;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Process the cards at the top of piles, sending them to empty spaces or other piles.
	 * @param driver The WebDriver
	 * @see NeoDailies#runSolitaire(WebDriver) runSolitaire
	 * @see NeoDailies#processBottomCards(WebDriver) processBottomCards
	 * @see NeoDailies#processDrawnCard(WebDriver) processDrawnCard
	 */
	private static void processTopCards(WebDriver driver) {
		for (int x = 2; x < 9; x++) {
			if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[1]/img", driver)) {
				String topPileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[1]/img")).getAttribute("src");
				if (!topPileCardImg.contains("new_blank_card")) {
					int topPileCardNum = Integer.parseInt(topPileCardImg.substring(topPileCardImg.lastIndexOf("/") + 1, topPileCardImg.lastIndexOf("_")));
					String topPileCardSuit = topPileCardImg.substring(topPileCardImg.indexOf("_") + 1, topPileCardImg.lastIndexOf("."));

					String topPileCardColor = "";
					if (topPileCardSuit.equals("spades") || topPileCardSuit.equals("clubs")) {
						topPileCardColor = "black";
					}
					else if (topPileCardSuit.equals("hearts") || topPileCardSuit.equals("diamonds")) {
						topPileCardColor = "red";
					}

					if (topPileCardNum == 13) {
						//If there's a down card on top of the king
						if (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/img[1]", driver)) {
							//Check for an empty spot
							for (int m = 2; m < 9; m++) {
								int n = 1;
								while (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img", driver)) {
									n++;
								}
								n--;
								String possiblyEmptyPileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img")).getAttribute("src");
								if (possiblyEmptyPileCardImg.contains("new_blank_card")) {
									driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[1]/img")).click();
									driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img")).click();
									madeMove = true;
									x = 1;
									processBottomCards(driver);
									break;
								}
							}
						}
					}
					else {
						for (int m = 2; m < 9; m++) {
							int n = 1;
							while (isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img", driver)) {
								n++;
							}
							n--;
							String bottomPileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img")).getAttribute("src");
							if (!bottomPileCardImg.contains("new_blank_card")) {
								int bottomPileCardNum = Integer.parseInt(bottomPileCardImg.substring(bottomPileCardImg.lastIndexOf("/") + 1, bottomPileCardImg.lastIndexOf("_")));
								String bottomPileCardSuit = bottomPileCardImg.substring(bottomPileCardImg.lastIndexOf("_") + 1, bottomPileCardImg.lastIndexOf("."));
								String bottomPileCardColor = "";
								if (bottomPileCardSuit.equals("spades") || bottomPileCardSuit.equals("clubs")) {
									bottomPileCardColor = "black";
								}
								else if (bottomPileCardSuit.equals("hearts") || bottomPileCardSuit.equals("diamonds")) {
									bottomPileCardColor = "red";
								}

								if (bottomPileCardNum == topPileCardNum + 1 && !topPileCardColor.equals(bottomPileCardColor)) {
									driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[1]/img")).click();
									driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img")).click();
									madeMove = true;
									x = 1;
									processBottomCards(driver);
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Send an email and text message to the user.
	 * @param userBody The text of the body of the email sent.
	 */
	private static void sendEmail(String userBody) {
		String subject = "NeoBot";

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(YOUR_EMAIL, YOUR_EMAIL_PASSWORD);
			}
		});

		boolean sentMail = false;
		while (!sentMail) {
			try {
				logMessage("Creating Message");
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(YOUR_EMAIL));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(NUMBER_TO_TEXT));
				message.setRecipients(Message.RecipientType.CC,
						InternetAddress.parse(YOUR_EMAIL));


				if (userBody.contains("CRASH") || userBody.contains("Status") || userBody.contains("Exception")) {
					message.setSubject(subject);
					// This mail has 2 parts, the BODY and the embedded image
					MimeMultipart multipart = new MimeMultipart("related");

					// first part (the html)
					BodyPart messageBodyPart = new MimeBodyPart();
					String htmlText = "<H1>" + userBody + "</H1><img src=\"cid:image\">";
					messageBodyPart.setContent(htmlText, "text/html");
					// add it
					multipart.addBodyPart(messageBodyPart);

					// second part (the image)
					messageBodyPart = new MimeBodyPart();
					String attachedImage = getLatestFileFromDir(new java.io.File(".").getCanonicalPath()).toString();
					FileDataSource fds = new FileDataSource(new File(attachedImage));


					messageBodyPart.setDataHandler(new DataHandler(fds));
					messageBodyPart.setHeader("Content-ID", "<image>");

					// add image to the multipart
					multipart.addBodyPart(messageBodyPart);

					// put everything together
					message.setContent(multipart); }
				else {
					message.setSubject(subject);
					message.setText(userBody); }

				logMessage("About to send message");
				Transport.send(message);
				logMessage("Message successfully sent");
				sentMail = true; }
			catch(MessagingException e) {
				logMessage("MessagingException occurred... retrying in 10 seconds");
				e.printStackTrace();
				sleepMode(10000); }
			catch(Exception ex) {
				logMessage("Unknown exception occurred. Not sending email");
				ex.printStackTrace();
				sentMail = true; }
		}
	}

	/**
	 * Grab the newest file from the working directory.
	 * This will be the error screenshot given this method's use.
	 * @param dirPath The working directory of the bot
	 * @return The screenshot file to be attached in the email.
	 * @see NeoDailies#sendEmail(String) sendEmail
	 */
	private static File getLatestFileFromDir(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			return null;
		}

		File lastModifiedFile = files[0];
		for (int i = 1; i < files.length; i++) {
			if (lastModifiedFile.lastModified() < files[i].lastModified()) {
				lastModifiedFile = files[i];
			}
		}
		return lastModifiedFile;
	}

	/**
	 * Remove any HTML tags from a string. Everything
	 * between a < and a > will be removed along with the
	 * < and > themselves.
	 * @param rawHtml The raw string with all the HTML tags.
	 * @return The clean string with no HTML tags.
	 */
	public static String removeHTML(String rawHtml) {
		while (rawHtml.contains(">")) {
			try {
				int firstLessThanIndex = rawHtml.indexOf("<");
				int firstGreaterThanIndex = rawHtml.indexOf(">")+1;
				StringBuffer buf = new StringBuffer(rawHtml);
				buf.replace(firstLessThanIndex, firstGreaterThanIndex, "");
				rawHtml = buf.toString();
			}
			catch (StringIndexOutOfBoundsException ex) {
				try {
					int lastLessThanIndex = rawHtml.lastIndexOf("<");
					int lastGreaterThanIndex = rawHtml.lastIndexOf(">")+1;
					StringBuffer buf = new StringBuffer(rawHtml);
					buf.replace(lastLessThanIndex, lastGreaterThanIndex, "");
					rawHtml = buf.toString();
				}
				catch (StringIndexOutOfBoundsException e) {
					//ex.printStackTrace();
					break;
				}
			}
		}
		return rawHtml.trim();
	}

}
