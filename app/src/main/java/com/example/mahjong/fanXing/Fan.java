package com.example.mahjong.fanXing;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Setter
@Getter
//@Builder
public class Fan implements Comparable<Fan>{
    private String name;
    private int genNum;
    private boolean qingYiSe;
    private boolean qiDui;

    private boolean daDuiZi;
    private boolean jinGouDiao;

//    private boolean gangKai;
//    private boolean ziMo;
//    private boolean haiDiLaoYue;

    private int beiShu = 1;

    private int booleanToInt(Boolean... flag){
        Optional<Integer> resultOpt = Stream.of(flag).map(x -> x ? 1 : 0).reduce(Integer::sum);
        if (resultOpt.isPresent()){
            return resultOpt.get();
        }else{
            throw new RuntimeException("计算时出现错误");
        }
    }

    public void calculateBeiShu(){
        int fanShu = 2 * booleanToInt(qingYiSe, qiDui) + booleanToInt(daDuiZi, jinGouDiao) + genNum;
        beiShu = (int) Math.pow(2, fanShu);
//        beiShu = 4 * booleanToInt(qingYiSe, qiDui) + 2 * booleanToInt(daDuiZi, jinGouDiao, gangKai, ziMo, haiDiLaoYue);
    }

    public int compareTo(Fan o) {
        return this.beiShu>o.beiShu ? 1 : -1;
    }

    @Override
    public String toString() {
        return this.name + "带" + this.genNum + "根" + "[" + this.beiShu + "]";
    }
    public void name(){
        this.name = new String();
        if (qingYiSe) this.name += "清一色 ";
        if (qiDui) this.name += "七对 ";
        if (daDuiZi) this.name += "大对子 " ;
        if (jinGouDiao) this.name += "金钩钓";
//        if (qingYiSe || qiDui || daDuiZi ||jinGouDiao == false){
//            this.name += "平胡";
//        }

    }
}

