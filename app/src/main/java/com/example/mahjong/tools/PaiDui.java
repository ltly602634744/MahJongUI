package com.example.mahjong.tools;

import com.example.mahjong.entities.card.Card;

import java.util.*;

public class PaiDui {

    public static final Map<Integer, List<Card>> DANPAIXING = getAllDanPaiXing();

    private List<Card> paiDui;

    public void createPaiDui(){
        List<Card> result = new LinkedList<>();
        for(int i = 0; i < 3; i ++){
            for(int j = 1 ; j < 10; j ++){
                for(int k = 0; k < 4; k++){
                    result.add(new Card(j, i));
                }
            }
        }
        Collections.shuffle(result);
        this.paiDui = result;
    }

    public List<Card> getPaiDui() {
        return paiDui;
    }

    public static Map<Integer, List<Card>> getAllDanPaiXing(){
        Map<Integer, List<Card>> result = new HashMap<>();
        List<Card>  wanPai = new ArrayList<>();
        for(int i = 1; i < 10; i++){
            wanPai.add(new Card(i, 0));
        }
        List<Card>  tongPai = new ArrayList<>();
        for(int i = 1; i < 10; i++){
            tongPai.add(new Card(i, 1));
        }
        List<Card>  tiaoPai = new ArrayList<>();
        for(int i = 1; i < 10; i++){
            tiaoPai.add(new Card(i, 2));
        }
        result.put(0, wanPai);
        result.put(1, tongPai);
        result.put(2, tiaoPai);
        return result;
    }

    public static void main(String[] args) {
        PaiDui paiDui = new PaiDui();
        paiDui.createPaiDui();
        System.out.println(paiDui.getPaiDui());
        System.out.println(paiDui.getPaiDui().size());

        System.out.println(DANPAIXING);
    }
}


