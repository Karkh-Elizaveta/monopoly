package game.core.session;

import game.core.exceptions.EmptySessionException;
import game.core.exceptions.FullSessionException;
import game.core.exceptions.NoSuchPlayerException;
import game.core.exceptions.PlayersCompleteException;
import game.core.player.Player;
import game.core.field.*;
import game.core.structs.SessionStatus;

import java.util.ArrayList;
import java.util.Random;

public class Session {

    /**
     * session's identifier
     */
    private Integer id;

    /**
     * amount of players
     */
    private Integer capacity;

    /**
     * players in session
     */
    private ArrayList<Player> players;

    /**
     * session status
     */
    private SessionStatus status;

    private ArrayList<Field> playingField;
    public ArrayList<Field> getPlayingField() { return playingField; }

    /**
     * all sessions
     */
    private static ArrayList<Session> sessions = new ArrayList<Session>();

    /**
     * create new session
     * @param capacity amount of players
     */
    private Session(Integer capacity) {
        this.id = sessions.size();
        this.capacity = capacity;
        this.status = SessionStatus.PLAYERS_WAITING;
        this.players = new ArrayList<Player>();
        this.playingField = new ArrayList<Field>();
    }

    /**
     * add new session
     * @param capacity amount of players
     * @return new session
     */
    public static Session createNewSession(Integer capacity){
        Session session = new Session(capacity);
        sessions.add(session);
        return session;
    }

    /**
     * add player to session
     * @param player
     * @throws FullSessionException
     */
    public void addPlayer(Player player) throws FullSessionException, PlayersCompleteException {
        if (status == SessionStatus.PLAYERS_WAITING) {
            if (players.size() < capacity) {
                players.add(player);
                if (players.size() == capacity) this.status = SessionStatus.ALL_PLAYERS_READY;
            } else throw new FullSessionException();
        }
        else throw new PlayersCompleteException();
    }

    public void removePlayer(String playerIP) throws NoSuchPlayerException, EmptySessionException {
        Integer oldPlayers = players.size();
        if (oldPlayers == 0) throw new EmptySessionException();
        for (Player player: players) {
            if (player.getIp().equals(playerIP)) {
                players.remove(player);
                status = (players.size() == 0) ? SessionStatus.GAME_OVER : SessionStatus.PLAYERS_WAITING;
            }
        }
        if (oldPlayers == players.size()) throw new NoSuchPlayerException();
    }
}
