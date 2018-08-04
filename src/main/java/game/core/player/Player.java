package game.core.player;

import game.core.session.Session;

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

    /**
     * amount of money
     */
    private Integer budget;

    /**
     * position on the field
     */
    private Integer position = 0;

    /**
     * create player for session
     * @param ip user's ip-address
     * @param session choosen session
     */
    public Player(String ip, Session session) {
        name = "guest" + Integer.toString(random.nextInt(MAX_GUEST_ID));
        this.ip = ip;
        this.session = session;
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
        budget =+ change;
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
