package game.core.field;

import game.core.player.Player;
import game.core.structs.*;

public class Street extends Field {
    /**
     * название улицы
     */
    private String name;
    public String getName() { return name; }

    /**
     * тип улицы
     */
    private StreetType type;
    public StreetType getType() {
        return type;
    }

    /**
     * количество улиц выбранного типа
     */
    private Integer typeAmount = type.getAmount();
    public Integer getTypeAmount() {
        return typeAmount;
    }

    /**
     * стоимость покупки улицы
     */
    private Integer cost;
    public Integer getCost() {
        return cost;
    }

    /**
     * стоимость постройки дома
     */
    private Integer houseCost;
    public Integer getHouseCost() { return houseCost; }

    /**
     * количество домов на улице,по умолчанию домов на улице нет
     */
    private Integer houses = 0;

    /**
     * постройка дома
     */
    public void addHouse(){
        owner.changeBudget(-houseCost);
        houses++;
    }

    /**
     * продажа дома
     */
    public void saleHouse() {
        owner.changeBudget(houseCost);
        houses--;
    }

    /**
    * стоимость постройки отеля
    */
    private Integer hotelCost;
    public Integer getHotelCost() {
        return hotelCost;
    }

    /**
     * построен ли отель на улице, по умолчанию отеля на улице нет
     */
    private Boolean hotel = false;

    /**
     * постройка отеля
     */
    public void addHotel() {
        owner.changeBudget(-hotelCost);
        houses = 0;
        hotel = true;
    }

    /**
     * продажа отеля
     */
    public void saleHotel() {
        owner.changeBudget(hotelCost);
        houses = 4;
        hotel = false;
    }

    /**
     * налог по умолчанию
     */
    private Integer tax;
    /**
     * налог при постройке одного дома на улице
     */
    private Integer taxWithOneHouse;
    /**
     * налог при постройке двух домов на улице
     */
    private Integer taxWithTwoHouses;
    /**
     * налог при постройке трех домов на улице
     */
    private Integer taxWithThreeHouses;
    /**
     * налог при постройке четырех домов на улице
     */
    private Integer taxWithFourHouses;
    /**
     * налог при постройке отеля на улице
     */
    private Integer taxWithHotel;
    /**
     * возвращение налога
     */
    public void getTax(Integer paidTax) {
        if (!inLimbo) {
            if (hotel) owner.changeBudget(taxWithHotel);
            else {
                if (houses == 0) {
                    owner.changeBudget(tax);
                } else {
                    owner.changeBudget(((int) (1 / houses)) * taxWithOneHouse + ((int) (2 / houses % 2)) * taxWithTwoHouses +
                            ((int) (3 / houses % 3)) * taxWithThreeHouses + ((int) (4 / houses % 4)) * taxWithFourHouses);
                }
            }
        }
    }

    /**
     * владелец, по умолчанию владельца нет
     */
    private Player owner = null;

    /**
     * покупка улицы
     */
    public void buyStreet(Player newOwner) {
        owner = newOwner;
    }

    /**
     * улица освобождается
     */
    public void freeStreet() {
        owner = null;
    }

    /**
     * заложена улица или нет, по умолчанию не заложена
     */
    private boolean inLimbo = false;
    public boolean getInLimbo() { return inLimbo; }

    /**
     * улица закладывается владельцем в ломбард
     */
    public void putInLimbo() {
        inLimbo = true;
        if (hotel) {
            owner.changeBudget(hotelCost + cost/2);
            hotel = false;
        } else {
            owner.changeBudget(houses * houseCost + cost/2);
            houses = 0;
        }
    }

    /**
     * улица выкупается владельцем из ломбарда
     */
    public void getFromLimbo() {
        inLimbo = false;
        owner.changeBudget(-cost/2);
    }

}
