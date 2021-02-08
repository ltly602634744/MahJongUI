package com.example.mahjong.entities.card;


import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class Card implements Comparable<Card>{

    public static final String[] HUASE={"万", "桶", "条"};

    protected int cardNumber;
    protected int cardType;

    public Card(){}

    public Card(int cardNumber, int cardType) {
        this.cardNumber = cardNumber;
        this.cardType = cardType;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public boolean sameType(Card... cards){
        return Arrays.stream(cards).allMatch(x->x.cardType==this.cardType);
    }

    public boolean sameCard(Card... theCard){
        return Arrays.stream(theCard)
                .allMatch(x->this.cardType == x.cardType && this.cardNumber == x.cardNumber);
    }

    public boolean chengShun(Card card1, Card card2){
        if (!this.sameType(card1, card2, this)){
            return false;
        }

        int sum = card1.cardNumber + card2.cardNumber;
        int difference = Math.abs(card2.cardNumber-card1.cardNumber);
        if (difference == 2){
            return this.cardNumber == (sum)/2;
        }
        if (difference == 1){
            return this.cardNumber == (sum + 3)/2 || this.cardNumber == (sum - 3)/2;
        }
        return false;
    }

    public boolean chengKe(Card card1, Card card2){
        return this.sameCard(this, card1, card2);
    }

    public Set<Card> twoCardsTing(Card card){
        int difference = Math.abs(this.cardNumber-card.cardNumber);
        if(!this.sameType(card) || difference>2){
            return null;
        }
        Set<Card> result = new TreeSet<>();
        int sum = this.cardNumber + card.cardNumber;
        if(difference == 1) {
            if(this.cardNumber == 1 || card.cardNumber == 1){
                result.add(new Card(3,this.cardType));
            }else if(card.cardNumber == 9 || this.cardNumber == 9){
                result.add(new Card(7,this.cardType));
            }else{
                result.add(new Card((sum+3)/2,this.cardType));
                result.add(new Card((sum-3)/2,this.cardType));
            }
        }else{
            result.add(new Card((this.cardNumber+card.cardNumber)/2, this.cardType));
        }

        return result;

    }

    public Card copy(){
        return new Card(this.cardNumber, this.cardType);
    }

    @Override
    public String toString() {
        return this.cardNumber+HUASE[this.cardType];
    }

    @Override
    public int compareTo(Card theCard) {
        if(this.hashCode() == theCard.hashCode()){
            return 0;
        }
        if(this.cardType == theCard.cardType){
            return this.cardNumber > theCard.cardNumber ? 1 : -1;
        }
        return Integer.compare(this.cardType, theCard.cardType);
    }

}
