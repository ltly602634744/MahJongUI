package com.example.mahjong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mahjong.ErrorResponse.ErrorResponse;
import com.example.mahjong.entities.CardButton;
import com.example.mahjong.entities.card.Card;
import com.example.mahjong.tools.TingPai;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private Button bt1;
    private Button bt2;
    private Button bt3;
    private Button bt4;
    private Button bt5;
    private Button bt6;
    private Button bt7;
    private Button bt8;
    private Button bt9;
    private List<Button> numKeyBoard;

    private Button calculateButton;
    private Button resetButton;

    private RadioButton radioButton0;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioGroup radioGroup;

    private TextView resultView;
    private GridLayout chosenButtons;

    private List<CardButton> cardButtons;

    private int cardType;

//    public static final String URL = "http://192.168.0.11:8080/tingpai/calculate";
    public static final String URL = "https://airy-ripple-291313.uc.r.appspot.com/tingpai/calculate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WindowManager wm1 = this.getWindowManager();
        int windowWidth = wm1.getDefaultDisplay().getWidth();

        // the chosen button list
        cardButtons = new ArrayList<>();

        // buttons and views
        chosenButtons = (GridLayout) findViewById(R.id.handcardView);

        bt1 = (Button) findViewById(R.id.button1);
        bt2 = (Button) findViewById(R.id.button2);
        bt3 = (Button) findViewById(R.id.button3);
        bt4 = (Button) findViewById(R.id.button4);
        bt5 = (Button) findViewById(R.id.button5);
        bt6 = (Button) findViewById(R.id.button6);
        bt7 = (Button) findViewById(R.id.button7);
        bt8 = (Button) findViewById(R.id.button8);
        bt9 = (Button) findViewById(R.id.button9);

        numKeyBoard = Arrays.asList(bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9);
        for (int i = 0; i < numKeyBoard.size(); i++){
            int cardNum = i + 1;
            CharSequence text = numKeyBoard.get(i).getText();

            numKeyBoard.get(i).setOnClickListener(view->{
                CardButton cardButton = new CardButton(this);
                cardButton.setCard(new Card(cardNum, cardType));
                cardButton.setText(text + Card.HUASE[cardType]);

                cardButtons.add(cardButton);
                chosenButtons.addView(cardButton);
                cardButton.getLayoutParams().width = windowWidth/4;

                cardButton.setOnClickListener(v -> {
//                    Instant time1 = Instant.now();

                    List<Card> cards = cardButtons.stream()
                            .map(CardButton::getCard).collect(Collectors.toList());
                    cards.remove(cardButton.getCard());
                    localCalculate(new TreeSet<>(cards));

//                    Instant time2 = Instant.now();
//                    System.out.println(time2.toEpochMilli()-time1.toEpochMilli());
                });

                cardButton.setOnLongClickListener(v -> {
                    chosenButtons.removeView(cardButton);
                    cardButtons.remove(cardButton);
                    return true;
                });

//                cardButton.setListener
            });
        }

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(((group, checkedId) -> {
            if (R.id.radioButton0 == checkedId){
                cardType = 0;
            }else if (R.id.radioButton1 == checkedId){
                cardType = 1;
            }else if (R.id.radioButton2 == checkedId){
                cardType = 2;
            }

        }));

        resultView = (TextView) findViewById(R.id.resultView);


        calculateButton = (Button) findViewById(R.id.buttonConfirm);
        calculateButton.setOnClickListener(v -> {

            //calculate locally
            List<Card> cards = cardButtons.stream().map(CardButton::getCard).collect(Collectors.toList());
            localCalculate(new TreeSet<>(cards));

            //request to server
//            new Thread(()->{
//                // request body
//                List<Card> cards = cardButtons.stream().map(CardButton::getCard).collect(Collectors.toList());
//
//                // result String
//                String result = new String();
//
//                //Create RestTemplate
//                RestTemplate restTemplate = new RestTemplate();
//                ResponseEntity responseEntity = null;
//                try {
//                    responseEntity = restTemplate.postForEntity(URL, cards, Card[].class);
//                    Card[] theCards= (Card[]) responseEntity.getBody();
//                    result += Arrays.asList(theCards).toString();
//                }catch (HttpMessageNotReadableException e){
//                    responseEntity = restTemplate.postForEntity(URL, cards, ErrorResponse.class);
//                    ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
//                    result += errorResponse.getMessage();
//                }catch (Exception e){
//                    result += e.getMessage();
//                }
//                resultView.append(result);
//
//            }).start();
//
        });

        resetButton = (Button) findViewById(R.id.buttonReset);
        resetButton.setOnClickListener(view->{
            cardButtons.clear();
            chosenButtons.removeAllViews();
            resultView.setText("计算结果： ");
        });
    }

    private void localCalculate(Set<Card> cards){
        try{
            Set<Card> tingList = TingPai.checkTingPaiList(cards);
            String result = tingList.toString();
            resultView.setText("计算结果： " + result);
        }catch (RuntimeException e){
            resultView.setText(e.getMessage());
        }
    }
}
