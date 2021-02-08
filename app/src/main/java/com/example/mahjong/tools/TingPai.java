package com.example.mahjong.tools;


import com.example.mahjong.entities.card.Card;
import com.example.mahjong.entities.card.TiaoCard;
import com.example.mahjong.entities.card.TongCard;
import com.example.mahjong.entities.card.WanCard;
import com.example.mahjong.fanXing.Fan;
import com.example.mahjong.entities.handCard.HandCard;

import java.sql.SQLOutput;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TingPai {

    public static Map<Integer, List<Card>> menPaiMap(Set<Card> cards){
//        if (cards.size() == 0){
//            return new HashMap<Integer, List<Card>>();
//        }
//        System.out.println(cards.stream()
//                .collect(Collectors.groupingBy(x -> x.getCardType())));
        return cards.stream()
                .collect(Collectors.groupingBy(x -> x.getCardType()));
    }

    public static void distinct(Set<Card> cards){
        Iterator<Card> cardIterator = cards.iterator();
        Card theCard = null;
        if (cardIterator.hasNext()){
            theCard = cardIterator.next();
        }
        while(cardIterator.hasNext()){
            Card tempCard = cardIterator.next();
            if(theCard.sameCard(tempCard)){
                cardIterator.remove();
            }else{
                theCard = tempCard;
            }
        }
    }

    public static boolean sanPaiChengKe(Card card1, Card card2, Card card3){
        return card1.chengKe(card2, card3);
    }

    public static boolean sanPaiChengShun(Card card1, Card card2, Card card3){
        return card1.chengShun(card2, card3);
    }

    //返回听七对的牌
    public static Card checkTingQiDuiCard(List<Card> cards){
        if (cards.size() == 13){
            Card result = null;
            int i = 1;
            boolean flag = true;
            while(i < 13){
                if(!cards.get(i).sameCard(cards.get(i-1))){
                    if(flag){
                        i--;
                        result = cards.get(i);
                        flag = false;
                    }else{
                        break;
                    }
                }
                i += 2;
            }
            if(i == 13 && flag){
                result = cards.get(12);
            }
            if(i > 12){
                return result;
            }

        }

        return null;
    }

    //返回听七对的牌
    public static Card checkTingQiDuiCard(Set<Card> cards){
        Map<Card, Integer> cardNumMap = getCardNumMap(cards);
        if (cards.size() == 13){
            List<Map.Entry<Card, Integer>> entrySet = cardNumMap.entrySet().stream().filter(x -> x.getValue()%2 == 1).collect(Collectors.toList());
            if (entrySet.size() == 1){
//                System.out.println("321312");
                return entrySet.get(0).getKey().copy();
            }
        }

        return null;
//        return cardNumMap.entrySet().stream().filter(x->x.getValue()==1).findFirst().get().getKey().copy();
    }

    public static Set<Card> calculateTingPaiList(Set<Card> cards){
        Set<Card> result = new TreeSet<>();
        int cardNum = cards.size();

        if (cardNum == 1){
            result.addAll(cards);
            return result;
        }


//        Card qiDui = checkTingQiDuiCard(new ArrayList<>(cards));
//        if(qiDui != null){
//            result.add(qiDui);
//        }


        if(cardNum > 3){
            List<Card> tempCardList = new ArrayList<>(cards);
            Card tempCard1 = new Card(0,0);
            Card tempCard2 = null;
            Card tempCard3 = null;
            for(int i = 0; i < cardNum-2; i ++){
                if (tempCard1.sameCard(tempCardList.get(i))){
                    continue;
                }else {
                    tempCard1 = tempCardList.get(i).copy();
                }

                tempCard2 = new Card(0,0);
                for(int j = i + 1 ; j < Math.min(i+4, cardNum-1); j ++){
                    if (tempCard2.sameCard(tempCardList.get(j))){
                        continue;
                    }else {
                        tempCard2 = tempCardList.get(j).copy();
                    }
                    tempCard3 = new Card(0,0);
                    for(int k = j + 1; k < Math.min(j+4, cardNum); k++){
                        if (tempCard3.sameCard(tempCardList.get(k))){
                            continue;
                        }else {
                            tempCard3 = tempCardList.get(k).copy();
                        }
                        List<Card> cardList = new ArrayList<>(cards);
                        Card card1 = cardList.get(i);
                        Card card2 = cardList.get(j);
                        Card card3 = cardList.get(k);
                        if(sanPaiChengKe(card1, card2, card3) || sanPaiChengShun(card1, card2, card3)){
                            cardList.remove(card1);
                            cardList.remove(card2);
                            cardList.remove(card3);
                            result.addAll(calculateTingPaiList(new TreeSet<>(cardList)));
                        }
                    }
                }
            }
        }

        if ((cardNum-1) % 3 == 0){
            for(int i = 0; i < cardNum-1; i ++) {
                for (int j = i + 1; j < cardNum; j++) {
                    List<Card> cardList = new ArrayList<>(cards);
                    Card card1 = cardList.get(i);
                    Card card2 = cardList.get(j);
                    if(card1.sameCard(card2)){
                        cardList.remove(card1);
                        cardList.remove(card2);

                        Set<Card> cardList2 = new TreeSet<>(cardList);

                        result.addAll(calculateTingPaiList(new TreeSet<>(cardList2)));
                    }
                }
            }
        }

        if (cardNum == 2){
            List<Card> cardList = new ArrayList<>(cards);
//            System.out.println("cardList");
            Set<Card> tingList = cardList.get(0).twoCardsTing(cardList.get(1));
            if (tingList != null){
                result.addAll(tingList);
            }
        }



        return result;
    }

    //返回所有情况的听牌列表
    public static Set<Card> checkTingPaiList(Set<Card> cards){
        int cardNum = cards.size();
        if (cardNum % 3 != 1){
            throw new RuntimeException("牌的数量不合法！" + cardNum);
        }
        Set<Card> result = new TreeSet<>();

        Card qiDui = checkTingQiDuiCard(cards);
//        Card qiDui = checkTingQiDuiCard(new ArrayList<>(cards));
        if(qiDui != null){
            result.add(qiDui);
//            System.out.println("qidui!!!");
        }
        result.addAll(calculateTingPaiList(cards));
        distinct(result);
        return result;
    }

    public static Map<Card, Integer> getCardNumMap(Collection<Card> cards){
        Map<Card, Integer> cardNumMap = new HashMap<>();
        for (Card tempCard : cards){
            Optional<Card> keyCard = cardNumMap.keySet().stream().filter(x->x.sameCard(tempCard)).findFirst();
            if (keyCard.isPresent()){
                cardNumMap.put(keyCard.get(), cardNumMap.get(keyCard.get())+1);
            }else{
                cardNumMap.put(tempCard.copy(), 1);
            }
        }
        return cardNumMap;
    }

    public static Fan getFan(HandCard handCard, Card card){
        Fan theFan = new Fan();
//        Set<Card> cards = new TreeSet<>(handCard.getHandCards());
//        System.out.println(cards.add(card));
//        Map<Card, Integer> cardNumMap = getCardNumMap(cards);

        List<Card> handCardList = new ArrayList<>(handCard.getHandCards());
        handCardList.add(card);
        Map<Card, Integer> cardNumMap= getCardNumMap(handCardList);
//                new TreeMap<>();
//        for (Card tempCard : handCardList){
//            Optional<Card> keyCard = cardNumMap.keySet().stream().filter(x->x.sameCard(tempCard)).findFirst();
//            if (keyCard.isPresent()){
//                cardNumMap.put(keyCard.get(), cardNumMap.get(keyCard.get())+1);
//            }else{
//                cardNumMap.put(tempCard.copy(), 1);
//            }
//        }

        //清一色
        if (handCard.getHuaSeSet().size() == 1){
            theFan.setQingYiSe(true);
        }

        //金钩钓 七对 大对子
        if (cardNumMap.keySet().size() == 1){
            theFan.setJinGouDiao(true);
            theFan.setDaDuiZi(true);
        }else if (cardNumMap.values().stream().allMatch(x->x%2 == 0)){

            theFan.setQiDui(true);
        }else if (!cardNumMap.values().stream().anyMatch(x->x==1 || x==4)){
            theFan.setDaDuiZi(true);
        }

        //根
        int genNum = handCard.getGang().size() + (int) cardNumMap.values().stream().filter(x->x>3).count();
        for (Card tempCard : handCard.getPeng()){
            if (cardNumMap.keySet().stream().anyMatch(x->x.sameCard(tempCard))){
                genNum ++;
            }
        }
        theFan.setGenNum(genNum);
//        System.out.println("根数为" + genNum);

        theFan.calculateBeiShu();
        theFan.name();

        return theFan;

//        Set<Integer> types = handCard.getHuaSeSet();
//
//        //清一色
//        if (types.size() == 1){
//            theFan.setQingYiSe(true);
//        }
//
//        //金钩钓
//        if (handCard.getHandCards().size() == 1){
//            theFan.setDaDuiZi(true);
//            theFan.setJinGouDiao(true);
//        }else{
//            //七对
//            List<Card> handCardList = new ArrayList<>(handCard.getHandCards());
//            if (checkTingQiDuiCard(handCardList) != null && checkTingQiDuiCard(handCardList).sameCard(card)){
//                theFan.setQiDui(true);
//            }else{
//
//                //TODO 大对子
//                handCardList.add(card);
//                Collections.sort(handCardList);
//                boolean flag = true;
//
//                for (int i = 0; i < handCardList.size(); i++){
//                    if (handCardList)
//                }
//            }
//        }

//        Map<Boolean, List<Card>> collect = handCardList.stream().collect(Collectors.groupingBy(Card::sameCard));

    }
    //返回听牌列表及番型
    public static Map<Fan, Card> getTingFanMap(HandCard handCard){
        Map<Fan, Card> result = new TreeMap<>();
        Set<Card> tingPaiList = checkTingPaiList(handCard.getHandCards());

        for (Card tempCard : tingPaiList){
            result.put(getFan(handCard, tempCard), tempCard);
        }

        return result;
    }

    public static void main(String[] args) {

        Instant time1 = Instant.now();

//        List<Card> cards = Arrays.asList(new Card[]{new WanCard(1)
//                ,new WanCard(1),new WanCard(1),new WanCard(2)});
        List<Card> cards = Arrays.asList(new Card[]{new WanCard(1)
                ,new WanCard(1),new WanCard(2),new WanCard(2)
                ,new WanCard(3),new WanCard(3),new WanCard(5)
                ,new WanCard(5),new WanCard(7),new WanCard(7)
                ,new WanCard(9),new WanCard(9),new WanCard(4)});
//        List<Card> cards = Arrays.asList(new Card[]{new WanCard(2)
//                ,new WanCard(2),new WanCard(2),new WanCard(3)
//                ,new WanCard(3),new WanCard(5),new WanCard(6)
//                ,new WanCard(4),new TiaoCard(4),new TiaoCard(4)});
//        List<Card> cards = Arrays.asList(new Card[]{new TongCard(1)
//                ,new TongCard(2),new TongCard(3)
//                ,new TongCard(2), new TongCard(3)
//                ,new TongCard(4),new TongCard(5)
//                ,new TongCard(6),new TongCard(7),new TongCard(8)
//                ,new TongCard(5),new TongCard(5),new TongCard(9)});
//        List<Card> cards = Arrays.asList(new Card[]{new TongCard(5)
//                ,new TongCard(5),new TongCard(5)
//                ,new TongCard(5), new TongCard(6)
//                ,new TongCard(6),new TongCard(6)
//                ,new TongCard(6),new TongCard(7),new TongCard(7)
//                ,new TongCard(7),new TongCard(7),new TongCard(4)});
//        List<Card> cards = Arrays.asList(new Card[]{new WanCard(1)
//                ,new WanCard(1),new WanCard(1)
//                ,new WanCard(6), new WanCard(7)
//                ,new WanCard(8),new WanCard(8)
//                ,new WanCard(8),new TiaoCard(1),new TiaoCard(1)
//                ,new TiaoCard(9),new TiaoCard(9),new TiaoCard(9)});
//        List<Card> cards = Arrays.asList(new Card[]{new WanCard(5),new WanCard(5),
//                                                    new WanCard(5),new WanCard(6)});
//        List<Card> cardsPeng  = Arrays.asList(new Card[]{ new WanCard(3),
//                                                new WanCard(4)});
//        List<Card> cardsGang = Arrays.asList(new WanCard(9));

        Set<Card> handCards = new TreeSet<>();
        handCards.addAll(cards);
//        System.out.println(handCards);

        HandCard handCard = new HandCard(handCards);
//        HandCard handCard = new HandCard(handCards,cardsPeng,cardsGang);
        System.out.println(handCard);
//        System.out.println("听： " + checkTingPaiList(handCard.getHandCards()));
//        System.out.println("听7对check： " + checkTingQiDuiCard(new ArrayList<>(handCard.getHandCards())));
//        System.out.println("听： " + check4Card(handCard.getHandCards()));
//        System.out.println("听： " + checkTingPaiList(handCard.getHandCards()));
        System.out.println(handCard.getTingFanMap());



        Instant time2 = Instant.now();
        System.out.println(time2.toEpochMilli()-time1.toEpochMilli());


    }
}
