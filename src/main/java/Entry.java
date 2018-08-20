import game.core.field.Street;
import game.core.player.Player;
import game.core.session.Session;
import game.core.structs.*;

import javax.annotation.processing.SupportedSourceVersion;

public class Entry {
    public static void main (String[] args) {
        System.out.println("hello, my dear");
        Street testStreet = new Street("Дерибасовская", StreetType.BLUE, 5, 1, 2, 10,20, 30, 40, 50, 60);
        Street testStreet2 = new Street("Ленина", StreetType.BLUE, 5, 1, 2, 10,20, 30, 40, 50, 60);
        Session testSession = Session.createNewSession(4);
        testSession.getPlayingField().add(testStreet);
        testSession.getPlayingField().add(testStreet2);
        Player testPlayer = new Player("1234", testSession);
        Player testPayer = new Player("1235", testSession);
        testStreet.buyStreet(testPlayer);
        testStreet2.buyStreet(testPlayer);
        System.out.println("lost owner");
        testStreet2.freeStreet();
    }
}
