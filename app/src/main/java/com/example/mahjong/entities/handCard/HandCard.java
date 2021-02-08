package com.example.mahjong.entities.handCard;


import com.example.mahjong.entities.card.Card;
import com.example.mahjong.entities.card.TiaoCard;
import com.example.mahjong.entities.card.TongCard;
import com.example.mahjong.entities.card.WanCard;
import com.example.mahjong.fanXing.Fan;
import com.example.mahjong.tools.TingPai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class HandCard {
    private Set<Card> handCards;
    private List<Card> peng;
    private List<Card> gang;

    public HandCard(Set<Card> handCards, List<Card> peng, List<Card> gang) {
        this.handCards = handCards;
        this.peng = peng;
        this.gang = gang;
    }

    public HandCard(Set<Card> handCards) {
        this(handCards, new ArrayList<>(), new ArrayList<>());
    }

    public HandCard(){
        this(new TreeSet<>());
    }

    public Set<Card> getHandCards() {
        return handCards;
    }

    public List<Card> getPeng() {
        return peng;
    }

    public List<Card> getGang() {
        return gang;
    }

    public void moPai(Card theCard){
        this.handCards.add(theCard);
    }

    public void moPai(int value, String type){
        int typeNum = -1;
        if (type.equals("万")){
            typeNum = 0;
        }else if(type.equals("桶")){
            typeNum = 1;
        }else if(type.equals("条")){
            typeNum = 2;
        }
        this.moPai(new Card(value, typeNum));
    }

    public void daPai(Card theCard){
        this.handCards.remove(theCard);
    }

    public boolean checkPengPai(Card theCard){
        int sameCardCount = (int) this.handCards.stream()
                                        .filter((x)->x.sameCard(theCard))
                                        .count();
        if(sameCardCount >= 2){
            return true;
        }
        return false;
    }

    public void pengPai(Card theCard){
        List<Card> pengPai = this.handCards.stream()
                .filter((x) -> x.sameCard(theCard))
                .limit(2)
                .collect(Collectors.toList());
        this.handCards.removeAll(pengPai);
        this.peng.add(theCard);
    }

    //直杠：手中已经有三张相同牌，别人再打出一张
    public boolean checkZhiGang(Card theCard){
        int sameCardCount = (int) this.handCards.stream()
                .filter((x)->x.sameCard(theCard))
                .count();
        if(sameCardCount == 3){
            return true;
        }
        return false;
    }

    private boolean checkHandCardContains(Card theCard){
        return this.handCards.stream()
                .anyMatch(x->x.sameCard(theCard));
    }

    //弯杠： 自己碰后， 又摸起来一张
    public List<Card> wanGangList(){
        List<Card> wanGangList = new ArrayList<>();
        for(Card tempCard : this.peng){
            if(this.checkHandCardContains(tempCard)){
                wanGangList.add(tempCard);
            }
        }
        return wanGangList;
    }

    //暗杠： 自己手中又四张相同的牌
    public List<Card> anGangList(){
        List<Card> anGangList = new ArrayList<>();
        int count = 1;
        Iterator<Card> cardIterator = this.handCards.iterator();
        Card theCard = cardIterator.next();
        while(cardIterator.hasNext()){
            Card tempCard = cardIterator.next();
            if(theCard.sameCard(tempCard)){
                count ++;
                if(count == 4){
                    anGangList.add(tempCard);
                }
            }else{
                count = 1;
                theCard = tempCard;
            }
        }
        return anGangList;
    }

    public void gangPai(Card theCard, Card gangWeiCard){

//        this.peng.stre
//        Card copyCard = theCard.copy();
        this.gang.add(theCard);
        this.handCards.removeIf(x->x.sameCard(theCard));
        this.peng.removeIf(x->x.sameCard(theCard));
        this.handCards.add(gangWeiCard);
    }

    public Set<Integer> getHuaSeSet(){
        Map<Integer, List<Card>> handMenPaiMap = TingPai.menPaiMap(this.handCards);
        Map<Integer, List<Card>> pengMenPaiMap = TingPai.menPaiMap(new TreeSet<Card>(this.peng));
        Map<Integer, List<Card>> gangMenPaiMap = TingPai.menPaiMap(new TreeSet<Card>(this.gang));

        Set<Integer> types = new HashSet<>();
        types.addAll(handMenPaiMap.keySet());
        types.addAll(pengMenPaiMap.keySet());
        types.addAll(gangMenPaiMap.keySet());
        return types;
    }

    //获取听牌列表
    public Set<Card> tingPaiList(){

        // 判断是否花猪
//        Set<Integer> types = getHuaSeSet();
        if (this.huaZhu()){
            throw new RuntimeException("花猪！");
        }

        return TingPai.checkTingPaiList(this.handCards);
    }

    public Map<Fan, Card> getTingFanMap(){
        if (this.huaZhu()){
            throw new RuntimeException("花猪！");
        }
        return TingPai.getTingFanMap(this);
    }

    public boolean huaZhu(){
        return this.getHuaSeSet().size() == 3;
    }

    public boolean checkHu(Card theCard){
        return this.tingPaiList().stream().anyMatch(x->x.sameCard(theCard));
    }

//    public Set<Card> tingPai(){
//
//    }

//    //查是否胡金钩钓
//    private boolean check1Card(Card theCard){
//        if (this.handCards.size() != 1){
//            return false;
//        }
//        return this.handCards.stream().allMatch(x->x.sameCard(theCard));
//    }
//
//    //查是否手中有四张牌时胡牌
//    private boolean check4Card(Card theCard){
//
//    }
//
//    public boolean checkHuPai(Card theCard){
//        this.handCards.add(theCard);
//
//    }

    public String test(){
        System.out.println("Test!!!");
        return "Test!!";
    }

    @Override
    public String toString() {
        return "HandCard{" +
                "handCards=" + handCards +
                ", \npeng=" + peng +
                ", \ngang=" + gang +
                '}';
    }

    public static void main(String[] args) {


        // create handCards
        List<Card> cards = Arrays.asList(new Card[]{new WanCard(3),
                new WanCard(1),new TiaoCard(8),new TiaoCard(4)
                ,new WanCard(2),new WanCard(1)
                ,new WanCard(1),new WanCard(7),new WanCard(7)
                ,new WanCard(4),new TiaoCard(4),new TiaoCard(4)
                ,new TiaoCard(4)});
        Set<Card> handCards = new TreeSet<>();
        handCards.addAll(cards);

        HandCard handCard = new HandCard(handCards);
        System.out.println(handCard);


        // mopai test
        Card theCard = new Card(1,1);
        handCard.moPai(theCard);
        System.out.println("mo jin 1 tong:" + handCard);

        //dapai test
        handCard.daPai(theCard);
        System.out.println("da chu 1 tong: "+ handCard);

        //check pengpai
        Card yiWan = new WanCard(1);
        System.out.println("check peng 1 wan: " + handCard.checkPengPai(yiWan));

        //peng pai
        handCard.pengPai(yiWan);
        System.out.println("peng le 1 wan" + handCard);

        //zhigang
        System.out.println("check zhigang: " + handCard.checkZhiGang(yiWan));

        //check angang List
        System.out.println("angang List: " + handCard.anGangList());

        //wangang List
        System.out.println("wangang List: " + handCard.wanGangList());

        Card gangweiPai1 = new TongCard(8);
        Card gangweiPai2 = new TongCard(7);
        //gang 1 wan
        handCard.gangPai(yiWan, gangweiPai1);
        System.out.println("gang 1 wan, mo 8 tong: " + handCard);

        //gang 4 wan
        handCard.gangPai(new TiaoCard(4), gangweiPai2);
        System.out.println("gang 4 tiao, mo 7 tong: " + handCard);
    }
}
