package com.icat.calculator;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    private Button AllBtn[] = new Button[22];
    private EditText editFormula;
    private EditText editResult;
    private boolean isOper = false;//最后一位是否为四则运算符
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editFormula = (EditText) findViewById(R.id.editFormula);
        editResult = (EditText) findViewById(R.id.editResult);

        editFormula.setFocusable(false);//设置结果禁止编辑
        editResult.setFocusable(false);//设置结果禁止编辑

        AllBtn[0] = (Button) findViewById(R.id.btn0);
        AllBtn[1] = (Button) findViewById(R.id.btn1);
        AllBtn[2] = (Button) findViewById(R.id.btn2);
        AllBtn[3] = (Button) findViewById(R.id.btn3);
        AllBtn[4] = (Button) findViewById(R.id.btn4);
        AllBtn[5] = (Button) findViewById(R.id.btn5);
        AllBtn[6] = (Button) findViewById(R.id.btn6);
        AllBtn[7] = (Button) findViewById(R.id.btn7);
        AllBtn[8] = (Button) findViewById(R.id.btn8);
        AllBtn[9] = (Button) findViewById(R.id.btn9);
        AllBtn[10] = (Button) findViewById(R.id.btnPoit);

        AllBtn[11] = (Button) findViewById(R.id.btnAC);
        AllBtn[12] = (Button) findViewById(R.id.btnZK);
        AllBtn[13] = (Button) findViewById(R.id.btnYK);

        AllBtn[14] = (Button) findViewById(R.id.btnDiv);
        AllBtn[15] = (Button) findViewById(R.id.btnAdd);
        AllBtn[16] = (Button) findViewById(R.id.btnRed);
        AllBtn[17] = (Button) findViewById(R.id.btnTak);
        AllBtn[18] = (Button) findViewById(R.id.btnEqual);

        AllBtn[19] = (Button) findViewById(R.id.btnQH);
        AllBtn[20] = (Button) findViewById(R.id.btnE);
        AllBtn[21] = (Button) findViewById(R.id.btnDel);


        for(int i=0;i<AllBtn.length;i++){
            AllBtn[i].setBackground(new ButtonStyle().get(i,false));
            AllBtn[i].setOnClickListener(new btnEvent());
            AllBtn[i].setOnTouchListener(new btnTouck());//设置按钮按下特效
        }

    }

    private class btnTouck implements View.OnTouchListener{
        private int idx = 0;
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction() == MotionEvent.ACTION_DOWN){

                for(int i=0;i<AllBtn.length;i++){
                    if (AllBtn[i].getId() == v.getId()) {
                        idx = i;
                        break;
                    }
                }

                ((Button)v).setBackground(new ButtonStyle().get(idx,true));

            }else if (event.getAction() == MotionEvent.ACTION_UP){
                for(int i=0;i<AllBtn.length;i++){
                    if (AllBtn[i].getId() == v.getId()) {
                        idx = i;
                        break;
                    }
                }

                ((Button)v).setBackground(new ButtonStyle().get(idx,false));

            }

            return false;
        }
    }

    private class btnEvent implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String res;
            switch (v.getId()) {

                case R.id.btnAC: {
                    editFormula.setText("0");
                    editResult.setText("0");
                    break;
                }
                case R.id.btnEqual :{
                    editFormula.setText(editResult.getText());
                    editResult.setText("0");
                    break;
                }
                case R.id.btnDel: {
                    editFormula.setText(editFormula.getText().toString().substring(0, editFormula.length() - 1));
                    if ("".equals(editFormula.getText().toString().replace("0", ""))) {
                        editFormula.setText("0");
                        editResult.setText("0");
                    }else{
                        res = Function.JavaScript.JavascriptEval(this,editFormula.getText().toString());
                        if (res != null)
                            editResult.setText(res);
                    }
                    break;
                }
                case R.id.btnQH: {
                    Intent i = new Intent(MainActivity.this, ConversionActivity.class);
                    startActivity(i);
                    break;
                }
                default:{
                    if (v.getId() == R.id.btnAdd || v.getId() == R.id.btnDiv
                            || v.getId() == R.id.btnRed || v.getId() == R.id.btnTak) {
                        if (isOper) return;
                        isOper = true;
                    } else {
                        isOper = false;
                    }
                    if ("".equals(editFormula.getText().toString().replace("0", "")))
                        editFormula.setText("");

                    editFormula.setText(editFormula.getText().toString() + ((Button) findViewById(v.getId())).getText().toString());

                    if ("".equals(editFormula.getText().toString().replace("0", "")))
                        editFormula.setText("0");
                    res = Function.JavaScript.JavascriptEval(this,editFormula.getText().toString());
                    if (res != null)
                        editResult.setText(res);

                    break;
                }
            }
        }
    }

    private class ButtonStyle{
        RoundRectShape rrs = new RoundRectShape(new float[] {500,500,500,500,500,500,500,500},null,null);

        private ShapeDrawable get(int idx,boolean down){

            ShapeDrawable num_sd = new ShapeDrawable(rrs);
            if (idx >= 11 && idx <= 13){
                if (!down)
                    num_sd.getPaint().setColor(Color.rgb(0, 102, 0));
                else
                    num_sd.getPaint().setColor(Color.rgb(100, 80, 255));

            } else if (idx >= 14 && idx <= 18){
                if (!down)
                    num_sd.getPaint().setColor(Color.rgb(205, 102, 0));
                else
                    num_sd.getPaint().setColor(Color.rgb(255, 102, 255));

            } else if (idx >= 19 && idx <= 21) {
                if (!down)
                    num_sd.getPaint().setColor(Color.rgb(102, 102, 100));
                else
                    num_sd.getPaint().setColor(Color.rgb(255, 102, 255));

            } else {
                if (!down)
                    num_sd.getPaint().setColor(Color.rgb(40, 40, 40));
                else
                    num_sd.getPaint().setColor(Color.rgb(40, 80, 60));
            }

            num_sd.getPaint().setStyle(Paint.Style.FILL);
            return num_sd;

        }
    }


}
