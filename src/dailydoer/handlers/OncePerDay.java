package dailydoer.handlers;

import dailydoer.Utils;
import dailydoer.dailies.*;

import org.openqa.selenium.WebDriver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OncePerDay
{
    public static void run(WebDriver driver, String xmlNode, String timeFormat) throws Exception
    {
        Date currentDay = new Date();
        SimpleDateFormat format2 = new SimpleDateFormat(timeFormat);
        String currentDayString = format2.format(currentDay);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File("dailydoer/resources/PreviousRuns.xml"));
        doc.getDocumentElement().normalize();

        NodeList nlList = doc.getDocumentElement().getElementsByTagName(xmlNode).item(0).getChildNodes();
        Node lastRunNode = nlList.item(1);

        String lastRunDay = doc.getDocumentElement().getElementsByTagName(xmlNode).item(0).getTextContent().trim();

        boolean successTask = false;

        if (!lastRunDay.equals(currentDayString))
        {
            switch (xmlNode)
            {
                case "Advent":
                    successTask = Advent.run(driver);
                    break;
                case "AltadorCouncil":
                    successTask = AltadorCouncil.run(driver);
                    break;
                case "AnchorManagement":
                    successTask = AnchorManagement.run(driver);
                    break;
                case "AppleBobbing":
                    successTask = AppleBobbing.run(driver);
                    break;
                case "Bank":
                    successTask = Bank.run(driver);
                    break;
                case "Battledome":
                    successTask = Battledome.run(driver);
                    break;
                case "BuyStock":
                    successTask = BuyStock.run(driver);
                    break;
                case "Cheat":
                    successTask = Cheat.run(driver);
                    break;
                case "Cliffhanger":
                    successTask = Cliffhanger.run(driver);
                    break;
                case "Coincidence":
                    successTask = Coincidence.run(driver);
                    break;
                case "Crossword":
                    successTask = Crossword.run(driver);
                    break;
                case "Cupcake":
                    successTask = Cupcake.run(driver);
                    break;
                case "DailyPuzzle":
                    successTask = DailyPuzzle.run(driver);
                    break;
                case "Expellibox":
                    successTask = Expellibox.run(driver);
                    break;
                case "Faeries":
                    successTask = Faeries.run(driver);
                    break;
                case "FoodClub":
                    successTask = FoodClub.run(driver);
                    break;
                case "ForgottenShore":
                    successTask = ForgottenShore.run(driver);
                    break;
                case "FruitMachine":
                    successTask = FruitMachine.run(driver);
                    break;
                case "GeraptikuTomb":
                    successTask = GeraptikuTomb.run(driver);
                    break;
                case "GrumpyOldKing":
                    successTask = GrumpyOldKing.run(driver);
                    break;
                case "IslandMystic":
                    successTask = IslandMystic.run(driver);
                    break;
                case "Jelly":
                    successTask = Jelly.run(driver);
                    break;
                case "KacheekSeek":
                    successTask = KacheekSeek.run(driver);
                    break;
                case "KikoPop":
                    successTask = KikoPop.run(driver);
                    break;
                case "LabRay":
                    successTask = LabRay.run(driver);
                    break;
                case "Lottery":
                    successTask = Lottery.run(driver);
                    break;
                case "LunarPuzzle":
                    successTask = LunarPuzzle.run(driver);
                    break;
                case "Meteor":
                    successTask = Meteor.run(driver);
                    break;
                case "MonthlyFreeby":
                    successTask = MonthlyFreeby.run(driver);
                    break;
                case "MoodImprove":
                    successTask = MoodImprove.run(driver);
                    break;
                case "NeggCave":
                    successTask = NeggCave.run(driver);
                    break;
                case "Omelette":
                    successTask = Omelette.run(driver);
                    break;
                case "PetPetBattles":
                    successTask = PetPetBattles.run(driver);
                    break;
                case "PotatoCounter":
                    successTask = PotatoCounter.run(driver);
                    break;
                case "Pyramids":
                    successTask = Pyramids.run(driver);
                    break;
                case "Scarab21":
                    successTask = Scarab21.run(driver);
                    break;
                case "ShopOfOffers":
                    successTask = ShopOfOffers.run(driver);
                    break;
                case "Solitaire":
                    successTask = Solitaire.run(driver);
                    break;
                case "GrundoPlushie":
                    successTask = GrundoPlushie.run(driver);
                    break;
                case "Tombola":
                    successTask = Tombola.run(driver);
                    break;
                case "TrudysSurprise":
                    successTask = TrudysSurprise.run(driver);
                    break;
                case "WheelOfKnowledge":
                    successTask = WheelOfKnowledge.run(driver);
                    break;
                case "WiseOldKing":
                    successTask = WiseOldKing.run(driver);
                    break;
            }

            if (successTask) {
                lastRunNode.setTextContent(currentDayString);

                //write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File("dailydoer/resources/PreviousRuns.xml"));
                transformer.transform(source, result);
            }
            Utils.sleepMode(5000);
        }
    }
}