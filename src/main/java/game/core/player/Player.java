package game.core.player;

import game.core.session.Session;
import game.core.field.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * user in game
 */
public class Player {

    private static final Random random = new Random();
    private static final Integer MAX_GUEST_ID = 9999999;

    /**
     * player's identifier
     */
    private String name;

    /**
     * player's ip-address
     */
    private String ip;

    /**
     * session
     */
    private Session session;
    public Session getSession() { return session; }

    /**
     * amount of money
     */
    private Integer budget;
    public Integer getBudget() {return budget;}

    /**
     * position on the field
     */
    private Integer position = 0;

    /**
     * player's domain
     */
    private ArrayList<Field> domain;
    /**
     * create player for session
     * @param ip user's ip-address
     * @param session choosen session
     */
    public Player(String ip, Session session) {
        name = "guest" + Integer.toString(random.nextInt(MAX_GUEST_ID));
        this.ip = ip;
        this.session = session;
        // денежный маячок: тут есть сумма, которую потом нужно будет передумать
        this.budget = 1500000;
    }

    /**
     *
     * @return player's ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * change player's budget
     * @param change can be positive or negative
     */
    public void changeBudget(Integer change) {
        budget += change;
    }

    /**
     * add street to domain
     */
    public void addStreet(Street street) {
        domain.add(street);
        street.buyStreet(this);
    }

    /**
     * moving
      * @param points
     */
    public void move(Integer points) {
        position = (position + points) % 40;
    }

    // сюда нужны moving на спец клетки типа тюрьмы
}
