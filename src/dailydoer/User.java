package dailydoer;

import java.util.ArrayList;

public class User
{
    public static final String YOUR_EMAIL = "CHANGE_ME@gmail.com"; //Change this to your email/
    public static final String YOUR_EMAIL_PASSWORD = "CHANGE_ME"; //Change this to your email's password/
    public static final String NUMBER_TO_TEXT = "5551234567@tmomail.net"; //Your phone number and carrier. See the README to change this.
    public static final String USERNAME = "CHANGE_ME"; //Change this to your Neopets account username.
    public static final String PASSWORD = "CHANGE_ME"; //Change this to your Neopets account password.
    public static final String PETNAME = "CHANGE_ME"; //Change this to your main pet's name on your Neopets account.
    public static final String PET_ZAPPED = "CHANGE_ME"; //Change this to your lab rat's name.
    public static final String PET_BATTLEDOME = "CHANGE_ME"; //Change this to your Battledome pet's name.
    public static final String WISHING_WELL_ITEM = "Halloween Y14 Goodie Bag"; //Shouldn't need to be changed. This is the item you wish for.
    public static final String BD_OPPONENT = "Koi Warrior"; //Change this as needed to the opponent you fight.
    public static final String ALTADOR_URL = "CHANGE_ME"; //Change this to your Altador Council prize URL address ID.
    public static final boolean RUNNING_ON_LAPTOP = true; //Shouldn't need to be changed. This is for running on a Raspberry Pi conveniently.
    public static final int NEOPOINTS_TO_WITHDRAW = 100000; //The amount of Neopoints to withdraw at the beginning of each day.
    public static final int GRAVE_PETPET = 1; //Change this as needed. This is the index of the Petpet. 1 would be your first pet's Petpet.
    public static final int MINIMUM_KEEP_VALUE = 100; //Change this as needed. This is the minimum NP value where items in your inventory won't be donated.
    public static final int SHOP_WIZARD_REFRESH_COUNT = 12; //Change this as needed. This is the amount of times we refresh prices with the Shop Wizard.

    //These shouldn't need changing, but the costs may need occasional updating. Only used for Kitchen Quest.
    public static final int AVERAGE_ONE_DUBLOON_COST = 700;
    public static final int AVERAGE_TWO_DUBLOON_COST = 650;
    public static final int AVERAGE_FIVE_DUBLOON_COST = 1800;
    public static final int AVERAGE_CODESTONE_COST = 5500;
    public static final int AVERAGE_RED_CODESTONE_COST = 24000;
    public static final int USEFUL_KQ_STAT_REWARD_PCT = 35; //35 for every stat except speed. 12 for hp. 8 for lvl

    public static ArrayList<String> IGNORED_ITEMS = new ArrayList<>();
}