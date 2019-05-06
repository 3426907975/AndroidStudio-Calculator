package com.icat.calculator;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.String;

public class ConversionActivity extends Activity {
    private int cEdit = R.id.editDE;               //当前进制内容
    private byte n_bit = 10;                       //当前进制
    private Button AllBtn[] = new Button[23];      //所有按钮
    private EditText editBI,editDE,editHE,editOC;  //EditText
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);

        //加载EditText
        editBI=((EditText)findViewById(R.id.editBI));
        editDE=((EditText)findViewById(R.id.editDE));
        editHE=((EditText)findViewById(R.id.editHE));
        editOC=((EditText)findViewById(R.id.editOC));

        //绑定切换进制按钮事件
        editBI.addTextChangedListener(new EditChangedListener());
        editDE.addTextChangedListener(new EditChangedListener());
        editHE.addTextChangedListener(new EditChangedListener());
        editOC.addTextChangedListener(new EditChangedListener());


        //设置EditText为自读
        editBI.setFocusable(false);
        editDE.setFocusable(false);
        editHE.setFocusable(false);
        editOC.setFocusable(false);

        //加载进制切换钮
        AllBtn[0] = (Button) findViewById(R.id.btnSelDE);
        AllBtn[1] = (Button) findViewById(R.id.btnSelBI);
        AllBtn[2] = (Button) findViewById(R.id.btnSelOC);
        AllBtn[3] = (Button) findViewById(R.id.btnSelHE);

        //加载数字按钮
        AllBtn[4] = (Button) findViewById(R.id.btnCo0);
        AllBtn[5] = (Button) findViewById(R.id.btnCo1);
        AllBtn[6] = (Button) findViewById(R.id.btnCo2);
        AllBtn[7] = (Button) findViewById(R.id.btnCo3);
        AllBtn[8] = (Button) findViewById(R.id.btnCo4);
        AllBtn[9] = (Button) findViewById(R.id.btnCo5);
        AllBtn[10] = (Button) findViewById(R.id.btnCo6);
        AllBtn[11] = (Button) findViewById(R.id.btnCo7);
        AllBtn[12] = (Button) findViewById(R.id.btnCo8);
        AllBtn[13] = (Button) findViewById(R.id.btnCo9);

        //加载十六进制按钮
        AllBtn[14] = (Button) findViewById(R.id.btnCoA);
        AllBtn[15] = (Button) findViewById(R.id.btnCoB);
        AllBtn[16] = (Button) findViewById(R.id.btnCoC);
        AllBtn[17] = (Button) findViewById(R.id.btnCoD);
        AllBtn[18] = (Button) findViewById(R.id.btnCoE);
        AllBtn[19] = (Button) findViewById(R.id.btnCoF);

        //加载功能按钮
        AllBtn[20] = (Button) findViewById(R.id.btnCoQH);
        AllBtn[21] = (Button) findViewById(R.id.btnCoDel);
        AllBtn[22] = (Button) findViewById(R.id.btnCoAC);

        //绑定所有按钮事件和设置按钮样式
        setNumberBtnColor(10);
        for(int i=0;i<AllBtn.length;i++){
            if (i < 4 || i > 19) {
                AllBtn[i].setBackground(new ConversionActivity.ButtonStyle().get(i,false));
            }
            AllBtn[i].setOnTouchListener(new btnTouch());
            AllBtn[i].setOnClickListener(new ConversionActivity.btnEvent());

        }

        setBtnColor(0);
    }

    //进制EditText被输入事件监听器
    private class EditChangedListener implements TextWatcher {
        private String str = "";
        private int eId;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //输入前
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //正在输入
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString() == null || s.toString().equals(""))
                CoeditAC();

            if(!s.toString().equals("0") && !s.toString().equals("0000")) {

                if (!s.toString().equals(str) || eId != cEdit) {

                    str = s.toString();
                    eId = cEdit;

                    getResult();

                }

            }

        }
    }

    private class btnTouch implements View.OnTouchListener{
        private  int idx;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
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
                if (idx >= 4 && idx <= 19) {
                    setNumberBtnColor(n_bit);
                }else {
                    ((Button) v).setBackground(new ButtonStyle().get(idx, false));
                }
            }
            return false;
        }
    }

    private class ButtonStyle{
        RoundRectShape rrs = new RoundRectShape(new float[] {500,500,500,500,500,500,500,500},null,null);

        private ShapeDrawable get(int idx,boolean down){

            ShapeDrawable num_sd = new ShapeDrawable(rrs);
            if (idx >= 4 && idx <= 13){
                if (!down)//判断是否按下
                    num_sd.getPaint().setColor(Color.rgb(40, 40, 40));
                else
                    num_sd.getPaint().setColor(Color.rgb(40, 80, 60));

            } else if (idx >= 14 && idx <= 19){
                if (!down)
                    num_sd.getPaint().setColor(Color.rgb(0, 102, 0));
                else
                    num_sd.getPaint().setColor(Color.rgb(100, 80, 255));

            } else if (idx >= 20 && idx <= 22) {
                if (!down)
                    num_sd.getPaint().setColor(Color.rgb(102, 102, 100));
                else
                    num_sd.getPaint().setColor(Color.rgb(255, 102, 255));
            }
            num_sd.getPaint().setStyle(Paint.Style.FILL);
            return num_sd;

        }
        private ShapeDrawable get_rgb(int rgb){

            ShapeDrawable num_sd = new ShapeDrawable(rrs);
            num_sd.getPaint().setColor(rgb);
            num_sd.getPaint().setStyle(Paint.Style.FILL);
            return num_sd;

        }
    }

    private void setNumberBtnColor(int len){
        if(len > 16) len = 16;
        for (int i = 0;i < 16;i++){

            if (i>len-1){
                AllBtn[4+i].setBackground(new ButtonStyle().get_rgb(Color.rgb(40, 40, 40)));
            }else if(i >= 0 && i <= 9) {
                AllBtn[4+i].setBackground(new ButtonStyle().get_rgb(Color.rgb(10, 20, 100)));
            }else{
                AllBtn[4+i].setBackground(new ButtonStyle().get_rgb(Color.rgb(0, 102, 40)));

            }

        }
    }


    //设置进制选择按钮颜色
    private void setBtnColor(int idx){
        AllBtn[0].setBackgroundColor(Color.rgb(0,0,0));
        AllBtn[1].setBackgroundColor(Color.rgb(0,0,0));
        AllBtn[2].setBackgroundColor(Color.rgb(0,0,0));
        AllBtn[3].setBackgroundColor(Color.rgb(0,0,0));
        AllBtn[idx].setBackgroundColor(Color.rgb(100,50,102));
    }

    private void CoeditAC(){
        editDE.setText("0");
        editBI.setText("0000");
        editOC.setText("0");
        editHE.setText("0");
    }

    private void CoeditNullSetZero(){
        if(editDE.getText().toString().equals(""))
            editDE.setText("0");
        else if(editBI.getText().toString().equals(""))
            editBI.setText("0000");
        else if(editOC.getText().toString().equals(""))
            editOC.setText("0");
        else if(editHE.getText().toString().equals(""))
            editHE.setText("0");
    }

    private void getResult(){
        if(cEdit == R.id.editDE) {

            if(!("".equals(editDE.getText().toString()))) {
                editBI.setText(Function.system.DeBI(Function.system.DETo(2, Integer.valueOf(editDE.getText().toString()).intValue())));
                editOC.setText(Function.system.DETo(8, Integer.valueOf(editDE.getText().toString()).intValue()));
                editHE.setText(Function.system.DETo(16, Integer.valueOf(editDE.getText().toString()).intValue()));
            }else{
                CoeditAC();
            }

            CoeditNullSetZero();

        }else if(cEdit == R.id.editBI){
            //二进制
            String Bi = editBI.getText().toString().replace(" ","");

            System.out.println(Bi);
            if(!"".equals(Bi)) {
                editDE.setText(Function.system.toDe(Bi, 2));
                editOC.setText(Function.system.DETo(8, Integer.valueOf(editDE.getText().toString()).intValue()));
                editHE.setText(Function.system.DETo(16, Integer.valueOf(editDE.getText().toString()).intValue()));
            }else{
                CoeditAC();
            }

            CoeditNullSetZero();

        }else if(cEdit == R.id.editOC){
            //八进制
            String Oc = editOC.getText().toString();

            if(!"".equals(Oc)) {
                editDE.setText(Function.system.toDe(Oc, 8));
                editBI.setText(Function.system.DeBI(Function.system.DETo(2, Integer.valueOf(editDE.getText().toString()).intValue())));
                editHE.setText(Function.system.DETo(16, Integer.valueOf(editDE.getText().toString()).intValue()));
            }else{
                CoeditAC();
            }
            CoeditNullSetZero();

        }else if(cEdit == R.id.editHE){
            //十六进制
            String He = editHE.getText().toString();

            if(!"".equals(He)) {
                editDE.setText(Function.system.toDe(He, 16));
                editBI.setText(Function.system.DeBI(Function.system.DETo(2, Integer.valueOf(editDE.getText().toString()).intValue())));
                editOC.setText(Function.system.DETo(8, Integer.valueOf(editDE.getText().toString()).intValue()));
            }else{
                CoeditAC();
            }
            CoeditNullSetZero();

        }
    }

    private class btnEvent implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.btnSelDE:{
                    n_bit = 10;
                    cEdit = R.id.editDE;//十进制
                    setNumberBtnColor(n_bit);
                    setBtnColor(0);
                    break;
                }
                case R.id.btnSelBI:{
                    n_bit = 2;
                    cEdit = R.id.editBI;//二进制
                    setNumberBtnColor(n_bit);
                    setBtnColor(1);
                    break;
                }
                case R.id.btnSelOC:{
                    n_bit = 8;
                    cEdit = R.id.editOC;//八进制
                    setNumberBtnColor(n_bit);
                    setBtnColor(2);
                    break;
                }
                case R.id.btnSelHE:{
                    n_bit = 16;
                    cEdit = R.id.editHE;//十六进制
                    setNumberBtnColor(n_bit);
                    setBtnColor(3);
                    break;
                }
                case R.id.btnCoDel:{
                    EditText cz = (EditText)findViewById(cEdit);
                    String str = cz.getText().toString();
                    if("".equals(str)) {
                        CoeditAC();
                    } else {
                        str = str.replace(" ", "");//删除全部空
                        str = str.subSequence(0, str.length() - 1).toString();

                        if (cEdit == R.id.editBI)
                            str = Function.system.DeBI(str);

                        if ("".equals(str.replace("0","")))
                            CoeditAC();
                        else
                            cz.setText(str);
                    }
                    break;
                }
                case R.id.btnCoAC:{
                    CoeditAC();
                    break;
                }
                case R.id.btnCoQH:{
                    finish();
                    break;
                }
                default:{
                    EditText cz = (EditText)findViewById(cEdit);
                    String btnNum = ((Button)findViewById(v.getId())).getText().toString();
                    String str = cz.getText().toString();
                    String eStr;
                    if(str == null) str = "";

                    if(cEdit == R.id.editBI)
                    {
                        if(v.getId() != R.id.btnCo0 && v.getId() != R.id.btnCo1) {
                            Toast.makeText(ConversionActivity.this,"二进制只能是0或1",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        eStr = str + btnNum;
                        eStr = eStr.replace(" ","");

                        if ((Function.system.DelBiZero(eStr).length() >= 4*8 && Function.system.DelBiZero(eStr).replace("0","").length() >= 4*8-1) || Function.system.DelBiZero(eStr).length() > 4*8){
                            Toast.makeText(ConversionActivity.this,"二进制超限！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        eStr = Function.system.DeBI(eStr);

                        cz.setText((eStr));
                        return;
                    }else if(cEdit == R.id.editOC) {
                            if (
                                    v.getId() != R.id.btnCo0 && v.getId() != R.id.btnCo1 && v.getId() != R.id.btnCo2 &&
                                            v.getId() != R.id.btnCo3 && v.getId() != R.id.btnCo4 && v.getId() != R.id.btnCo5 &&
                                            v.getId() != R.id.btnCo6 && v.getId() != R.id.btnCo7
                            ) {
                                Toast.makeText(ConversionActivity.this, "八进制的取值范围在0-7", Toast.LENGTH_SHORT).show();
                                return;
                            }
                    }else if(cEdit == R.id.editDE){
                        if (
                                v.getId() != R.id.btnCo0 && v.getId() != R.id.btnCo1 && v.getId() != R.id.btnCo2 &&
                                        v.getId() != R.id.btnCo3 && v.getId() != R.id.btnCo4 && v.getId() != R.id.btnCo5 &&
                                        v.getId() != R.id.btnCo6 && v.getId() != R.id.btnCo7 && v.getId() != R.id.btnCo8 &&
                                        v.getId() != R.id.btnCo9
                        ) {
                            Toast.makeText(ConversionActivity.this, "十进制的取值范围在0-9", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if(str.equals("0")) str = "";
                    eStr = str + btnNum;

                    if (cEdit == R.id.editOC){
                        if(Function.system.toDe(eStr, 8).equals("2147483647") && !eStr.equals("17777777777")) {
                            Toast.makeText(ConversionActivity.this,"八进制超限！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else if (cEdit == R.id.editHE){
                        if(Function.system.toDe(eStr, 16).equals("2147483647") && !eStr.equals("7FFFFFFF")) {
                            Toast.makeText(ConversionActivity.this,"十六进制超限！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else if(cEdit == R.id.editDE){
                        if(Long.valueOf(eStr).longValue() > 2147483647L){
                            Toast.makeText(ConversionActivity.this, "十进制数值超限！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    cz.setText(eStr);
                }
            }
        }
    }
}
