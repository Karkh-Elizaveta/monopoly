package game.core.field;

import game.core.exceptions.*;
import game.core.player.Player;
import game.core.session.Session;
import game.core.structs.*;

import java.util.ArrayList;

public class Street extends Field {

    private static final Integer MAX_HOUSES = 4;

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
    private Integer typeAmount;
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
     * стоимость постройки отеля
     */
    private Integer hotelCost;
    public Integer getHotelCost() {
        return hotelCost;
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
     * владелец улицы
     */
    private Player owner;
    public Player getOwner() {
        return owner;
    }
    /**
     * в монополии улица или нет
     */
    private Boolean isInMonopoly;
    public Boolean getIsInMonopoly() { return isInMonopoly; }
    /**
     * заложена улица или нет
     */
    private boolean isInLimbo = false;
    public boolean getInLimbo() { return isInLimbo; }
    /**
     * количество домов на улице
     */
    private Integer houses;
    public Integer getHouses() { return houses; }
    /**
     * построен ли отель на улице, по умолчанию отеля на улице нет
     */
    private Boolean hotel;
    public Boolean getHotel() { return hotel; }

    /**
     * проверка на монополию
     */
    public void monopoly(Session session) {
        isInMonopoly = session.
                getPlayingFields()
                .stream().filter(field -> field instanceof Street)
                .filter(street -> ((Street) street).getType() == this.getType())
                .allMatch(street -> ((Street) street).getOwner() == owner);
        for(Street street : session
                .getPlayingFields()
                .stream()
                .filter(field -> field instanceof Street)
                .filter(street -> ((Street) street).getType() == this.getType())
                .toArray(Street[]::new)) {
            street.isInMonopoly = this.isInMonopoly;
            if (!street.isInMonopoly) {
                street.houses = 0;
                street.hotel = false;
            }
            System.out.println(street.name + " monopoly " + street.isInMonopoly);
        }
    }

    /**
     * покупка улицы, у которой нет владельца
     */
    public void buyStreet(Player newOwner) {
        owner = newOwner;
        owner.changeBudget(-cost);
        this.monopoly(owner.getSession());
    }

    /**
     * покупка улицы, у которой есть владелец
     */
    public void buyStreet(Player newOwner, Player oldOwner, Integer contractCost) {
        owner = newOwner;
        owner.changeBudget(-contractCost);
        oldOwner.changeBudget(contractCost);
        this.monopoly(owner.getSession());
    }

    /**
     * освобождение улицы от владельца
     */
    public void freeStreet() {
        Session oldOwnerSession = owner.getSession();
        owner = null;
        this.monopoly(oldOwnerSession);
    }

    /**
     * постройка дома
     */
    public void buyHouse() throws NotMonopolyException, FullHouseException, NotEnoughHousesException {
        if (isInMonopoly) {
            if (owner.getSession()
                    .getPlayingFields()
                    .stream()
                    .filter(field -> field instanceof Street)
                    .filter(street -> ((Street) street).getType() == this.getType())
                    .allMatch(street -> ((Street) street).getHouses() < this.getHouses())) {
                if (this.getHouses() != MAX_HOUSES) {
                    owner.changeBudget(-houseCost);
                    houses++;
                }
                else throw new FullHouseException();
            }
            else throw new NotEnoughHousesException();
        }
        else throw new NotMonopolyException();

    }

    /**
     * продажа дома
     */
    public void saleHouse() throws NotMonopolyException, NotEnoughHousesException {
        if (isInMonopoly) {
            if (owner.getSession()
                    .getPlayingFields()
                    .stream()
                    .filter(field -> field instanceof Street)
                    .filter(street -> ((Street) street).getType() == this.getType())
                    .allMatch(street -> ((Street) street).getHouses() > this.getHouses())) {
                if (this.getHouses() != 0) {
                    owner.changeBudget(houseCost/2);
                    houses--;
                }
                else throw new NotEnoughHousesException();
            }
            else throw new NotEnoughHousesException();
        }
        else throw new NotMonopolyException();
    }

    /**
     * постройка отеля
     */
    public void buyHotel() throws AlreadyHasHotelException, NotEnoughHousesException, NotMonopolyException {
        if (isInMonopoly) {
            if (owner.getSession()
                    .getPlayingFields()
                    .stream()
                    .filter(field -> field instanceof Street)
                    .filter(street -> ((Street) street).getType() == this.getType())
                    .allMatch((street -> (((Street) street).getHouses() == MAX_HOUSES) || ((Street) street).getHotel()))) {
                if (!this.getHotel()) {
                    owner.changeBudget(-hotelCost);
                    houses = 0;
                    hotel = true;
                }
                else throw new AlreadyHasHotelException();
            }
            else throw new NotEnoughHousesException();
        }
        else throw new NotMonopolyException();
    }

    /**
     * продажа отеля
     */
    public void saleHotel() throws NotMonopolyException, NotHotelException {
        if (isInMonopoly) {
            if (!this.getHotel()) {
                owner.changeBudget(hotelCost/2);
                houses = MAX_HOUSES;
                hotel = false;
            }
            else throw new NotHotelException();
        }
        else throw new NotMonopolyException();
    }

    /**
     * улица закладывается владельцем в ломбард
     */
    public void putInLimbo() {
        isInLimbo = true;
        if (hotel) {
            owner.changeBudget(hotelCost/2 + 2*houseCost + cost/2);
            hotel = false;
        } else {
            owner.changeBudget(houses * houseCost/2 + cost/2);
            houses = 0;
        }
    }

    /**
     * улица выкупается владельцем из ломбарда
     */
    public void getFromLimbo() {
        isInLimbo = false;
        owner.changeBudget(-cost/2);
    }

    /**
     * возвращение налога
     */
    public void substractTax(Player payer) throws InLimboException{
        if (!isInLimbo) {
            if (isInMonopoly) {
                if (hotel) {
                    owner.changeBudget(taxWithHotel);
                    payer.changeBudget(-taxWithHotel);
                } else {
                    switch (houses) {
                        case 0:
                            owner.changeBudget(2*tax);
                            payer.changeBudget(-2*tax);
                            break;
                        case 1:
                            owner.changeBudget(taxWithOneHouse);
                            payer.changeBudget(-taxWithOneHouse);
                            break;
                        case 2:
                            owner.changeBudget(taxWithTwoHouses);
                            payer.changeBudget(-taxWithTwoHouses);
                            break;
                        case 3:
                            owner.changeBudget(taxWithThreeHouses);
                            payer.changeBudget(-taxWithThreeHouses);
                            break;
                        case 4:
                            owner.changeBudget(taxWithFourHouses);
                            payer.changeBudget(-taxWithFourHouses);
                            break;
                    }
                }
            }
            else {
                owner.changeBudget(tax);
                payer.changeBudget(-tax);
            }
        }
        else throw new InLimboException();
    }

    /**
     * конструктор
     * @param inputName имя улицы
     * @param inputType тип улицы
     * @param inputCost стоимость покупки улицы
     * @param inputHouseCost стоимость постройки дома на улице
     * @param inputHotelCost стоимость постройки отеля на улице
     * @param inputTax рента, которая берется с пустой улицы
     * @param inputTax1 рента, которая берется с улицы с одним домом
     * @param inputTax2 рента, которая берется с улицы с двумя домами
     * @param inputTax3 рента, которая берется с улицы с тремя домами
     * @param inputTax4 рента, которая берется с улицы с четырьмя домами
     * @param inputTaxH рента, которая берется с улицы с отелем
     */
    public Street(String inputName, StreetType inputType, Integer inputCost, Integer inputHouseCost,
                  Integer inputHotelCost, Integer inputTax, Integer inputTax1, Integer inputTax2,
                  Integer inputTax3, Integer inputTax4, Integer inputTaxH) {
        name = inputName;
        type = inputType;
        typeAmount = inputType.getAmount();
        owner = null;
        houses = 0;
        hotel = false;
        isInLimbo = false;
        isInMonopoly = false;
        tax = inputTax;
        taxWithOneHouse = inputTax1;
        taxWithTwoHouses = inputTax2;
        taxWithThreeHouses = inputTax3;
        taxWithFourHouses = inputTax4;
        taxWithHotel = inputTaxH;
        cost = inputCost;
        houseCost = inputHouseCost;
        hotelCost = inputHotelCost;
    }

}
