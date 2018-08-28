package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

public class Cliffhanger
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runCliffhanger");

        for (int cliffCnt = 0; cliffCnt < 10; cliffCnt++)
        {
            driver.get("http://www.neopets.com/games/cliffhanger/cliffhanger.phtml");

            ArrayList<String> cliffHangerList = new ArrayList<>();

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

            if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/input[4]", driver)) {
                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/input[4]")).click();
                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/input[5]")).click();
            }

            String hintedSentence = driver.findElement(By.xpath("//*[@id='content']/table/tbody/tr/td[2]/table/tbody/tr[2]/td")).getText().trim();
            hintedSentence = hintedSentence.replace("   ", "~");
            hintedSentence = hintedSentence.replace(" ", "");
            hintedSentence = hintedSentence.replace("~", " ");
            hintedSentence = hintedSentence.replace("\n", " ");
            Utils.sleepMode(5000);

            Utils.logMessage("Hinted sentence: " + hintedSentence);

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
                Utils.logMessage("There were still dupe cliffhanger sentences.");

                driver.findElement(By.linkText("N")).click();
                Utils.sleepMode(1000);
                driver.findElement(By.linkText("H")).click();
                Utils.sleepMode(1000);
                driver.findElement(By.linkText("L")).click();
                Utils.sleepMode(1000);
                driver.findElement(By.linkText("D")).click();
                Utils.sleepMode(1000);
                driver.findElement(By.linkText("C")).click();
                Utils.sleepMode(5000);

                hintedSentence = driver.findElement(By.xpath("//*[@id='content']/table/tbody/tr/td[2]/table/tbody/tr[2]/td")).getText().trim();
                hintedSentence = hintedSentence.replace("   ", "~");
                hintedSentence = hintedSentence.replace(" ", "");
                hintedSentence = hintedSentence.replace("~", " ");
                hintedSentence = hintedSentence.replace("\n", " ");

                Utils.logMessage("Hint before final dupe: " + hintedSentence);

                if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[3]/td[2]/table/tbody/tr[2]/td/font[6]", driver)) {
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

            Utils.logMessage("Answer: " + theWord);

            driver.findElement(By.name("solve_puzzle")).clear();
            driver.findElement(By.name("solve_puzzle")).sendKeys(theWord);

            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/input[2]")).click();

            if (cliffCnt == 9) {
                Utils.logMessage("Finished Cliffhanger.");
                return true;
            }
        }

        Utils.logMessage("Failed Cliffhanger. Something went wrong.");
        return false;
    }

}