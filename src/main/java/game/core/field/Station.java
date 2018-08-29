package game.core.field;

import game.core.exceptions.*;
import game.core.player.Player;
import game.core.session.Session;
import game.core.structs.StreetType;

public class Station extends Field {

    /**
     * название станции
     */
    private String name;
    public String getName() { return name; }

    /**
     * стоимость покупки станции
     */
    private Integer cost;
    public Integer getCost() {
        return cost;
    }

    /**
     * налог от одной станции
     */
    private Integer taxWithOneStation;
    /**
     * налог от двух станций
     */
    private Integer taxWithTwoStations;
    /**
     * налог от трех станций
     */
    private Integer taxWithThreeStations;
    /**
     * налог от четырех станций
     */
    private Integer taxWithFourStations;

    /**
     * владелец станции
     */
    private Player owner;
    public Player getOwner() {
        return owner;
    }

    /**
     * сколько станций у владельца
     */
    private Integer howManyStations;
    public Integer getHowManyStations() { return howManyStations; }

    /**
     * заложена станция или нет
     */
    private boolean isInLimbo = false;
    public boolean getInLimbo() { return isInLimbo; }

    /**
     * проверка на количество станций
     */
    public void count(Session session) {
        howManyStations = (int) session.
                getPlayingFields()
                .stream().filter(field -> field instanceof Station)
                .filter(company -> ((Company) company).getOwner() == owner)
                .count();
        for(Station station : session
                .getPlayingFields()
                .stream()
                .filter(field -> field instanceof Station)
                .toArray(Station[]::new)) {
            station.howManyStations = this.howManyStations;
        }
    }

    /**
     * покупка станции, у которого нет владельца
     */
    public void buyStation(Player newOwner) {
        owner = newOwner;
        owner.changeBudget(-cost);
        this.count(owner.getSession());
    }

    /**
     * покупка станции, у которого есть владелец
     */
    public void buyStation(Player newOwner, Player oldOwner, Integer contractCost) {
        owner = newOwner;
        owner.changeBudget(-contractCost);
        oldOwner.changeBudget(contractCost);
        this.count(owner.getSession());
    }

    /**
     * освобождение станции от владельца
     */
    public void freeCompany() {
        Session oldOwnerSession = owner.getSession();
        owner = null;
        this.count(oldOwnerSession);
    }

    /**
     * станция закладывается владельцем в ломбард
     */
    public void putInLimbo() {
        isInLimbo = true;
        owner.changeBudget(cost/2);
    }

    /**
     * станция выкупается владельцем из ломбарда
     */
    public void getFromLimbo() {
        isInLimbo = false;
        owner.changeBudget(-cost/2);
    }

    /**
     * возвращение налога
     */
    public void substractTax(Player payer, Integer points) throws InLimboException {
        if (!isInLimbo) {
            switch (howManyStations) {
                case 1:
                    owner.changeBudget(taxWithOneStation);
                    payer.changeBudget(-taxWithOneStation);
                    break;
                case 2:
                    owner.changeBudget(taxWithTwoStations);
                    payer.changeBudget(-taxWithTwoStations);
                    break;
                case 3:
                    owner.changeBudget(taxWithThreeStations);
                    payer.changeBudget(-taxWithThreeStations);
                    break;
                case 4:
                    owner.changeBudget(taxWithFourStations);
                    payer.changeBudget(-taxWithFourStations);
                    break;
            }
        }
        else throw new InLimboException();
    }

    public void moveStation(Station newStation) {
        owner.setPosition(newStation);
    }

    /**
     * конструктор
     * @param inputName имя предприятия
     * @param inputCost стоимость покупки компании
     * @param inputTax1 рента, которая берется с одной станции
     * @param inputTax2 рента, которая берется с двух станций
     * @param inputTax3 рента, которая берется с трех станций
     * @param inputTax4 рента, которая берется с четырех станций
     */
    public Station(String inputName, Integer inputCost, Integer inputTax1, Integer inputTax2,
                   Integer inputTax3, Integer inputTax4) {
        name = inputName;
        owner = null;
        isInLimbo = false;
        howManyStations = 0;
        cost = inputCost;
        taxWithOneStation = inputTax1;
        taxWithTwoStations = inputTax2;
        taxWithThreeStations = inputTax3;
        taxWithFourStations = inputTax4;
    }
}

