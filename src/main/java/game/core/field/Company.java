package game.core.field;

import game.core.exceptions.*;
import game.core.player.Player;
import game.core.session.Session;
import game.core.structs.StreetType;

public class Company extends Field {

    private static final Integer MAX_COMPANY = 2;

    /**
     * название коммунального предприятия
     */
    private String name;
    public String getName() { return name; }

    /**
     * стоимость покупки коммунального предприятия
     */
    private Integer cost;
    public Integer getCost() {
        return cost;
    }

    /**
     * владелец коммунального предприятия
     */
    private Player owner;
    public Player getOwner() {
        return owner;
    }
    /**
     * сколько коммунальных предриятий у владельца
     */
    private Boolean allCompanies;
    public Boolean getallCompanies() { return allCompanies; }
    /**
     * заложено коммунальное предприятие или нет
     */
    private boolean isInLimbo = false;
    public boolean getInLimbo() { return isInLimbo; }

    /**
     * проверка на количество коммунальных предприятий
     */
    public void all(Session session) {
        allCompanies = session.
                getPlayingFields()
                .stream().filter(field -> field instanceof Company)
                .allMatch(company -> ((Company) company).getOwner() == owner);
        for(Company company : session
                .getPlayingFields()
                .stream()
                .filter(field -> field instanceof Company)
                .toArray(Company[]::new)) {
            company.allCompanies = this.allCompanies;
        }
    }

    /**
     * покупка предприятия, у которого нет владельца
     */
    public void buyCompany(Player newOwner) {
        owner = newOwner;
        owner.changeBudget(-cost);
        this.all(owner.getSession());
    }

    /**
     * покупка предприятия, у которого есть владелец
     */
    public void buyCompany(Player newOwner, Player oldOwner, Integer contractCost) {
        owner = newOwner;
        owner.changeBudget(-contractCost);
        oldOwner.changeBudget(contractCost);
        this.all(owner.getSession());
    }

    /**
     * освобождение предприятия от владельца
     */
    public void freeCompany() {
        Session oldOwnerSession = owner.getSession();
        owner = null;
        this.all(oldOwnerSession);
    }

    /**
     * предприятие закладывается владельцем в ломбард
     */
    public void putInLimbo() {
        isInLimbo = true;
        owner.changeBudget(cost/2);
    }

    /**
     * предприятие выкупается владельцем из ломбарда
     */
    public void getFromLimbo() {
        isInLimbo = false;
        owner.changeBudget(-cost/2);
    }

    /**
     * возвращение налога
     */
    public void substractTax(Player payer, Integer points) {
        if (!isInLimbo) {
            if (allCompanies) {
                owner.changeBudget(10*points);
                payer.changeBudget(-10*points);
             }
            else {
                owner.changeBudget(4*points);
                payer.changeBudget(-4*points);
            }
        }
    }

    /**
     * конструктор
     * @param inputName имя предприятия
     * @param inputCost стоимость покупки компании
     */
    public Company(String inputName, Integer inputCost) {
        name = inputName;
        owner = null;
        isInLimbo = false;
        allCompanies = false;
        cost = inputCost;
    }
}

