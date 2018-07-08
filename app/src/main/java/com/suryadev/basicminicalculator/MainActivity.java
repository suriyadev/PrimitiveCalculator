package com.suryadev.basicminicalculator;

import android.content.ClipboardManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener , View.OnLongClickListener {


        private TextView userInput;
        private Button button0 , button1 , button2 , button3 , button4 , button5 , button6 ,
                button7 , button8 , button9 , buttonSum , buttonSub , buttonDivision ,
                buttonMul , buttonDelete , buttonEqual ,copyToClipboard,dot;

        private List userInputBuffer;
        private static  String DELETE = "DELETE";
        private static  String EQUAL = "=";
        private static  String COPY_TO_CLIPBOARD = "Copy to Clipboard";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInput = findViewById(R.id.user_input);
        userInput.setMovementMethod(new ScrollingMovementMethod());
        userInputBuffer = new MyList();


        copyToClipboard =  findViewById(R.id.cpy_to_clipboard);
        copyToClipboard.setOnClickListener(this);

        dot =  findViewById(R.id.dot);
        dot.setOnClickListener(this);
        button0 =  findViewById(R.id.num_zero);
        button0.setOnClickListener(this);
        button1 =  findViewById(R.id.num_one);
        button1.setOnClickListener(this);
        button2 =  findViewById(R.id.num_two);
        button2.setOnClickListener(this);
        button3 =  findViewById(R.id.num_three);
        button3.setOnClickListener(this);
        button4 =  findViewById(R.id.num_four);
        button4.setOnClickListener(this);
        button5 =  findViewById(R.id.num_five);
        button5.setOnClickListener(this);
        button6 =  findViewById(R.id.num_six);
        button6.setOnClickListener(this);
        button7 =  findViewById(R.id.num_seven);
        button7.setOnClickListener(this);
        button8 =  findViewById(R.id.num_eight);
        button8.setOnClickListener(this);
        button9 =  findViewById(R.id.num_nine);
        button9.setOnClickListener(this);
        buttonSum =  findViewById(R.id.sum);
        buttonSum.setOnClickListener(this);
        buttonSub =  findViewById(R.id.sub);
        buttonSub.setOnClickListener(this);
        buttonMul =  findViewById(R.id.multiply);
        buttonMul.setOnClickListener(this);
        buttonDivision =  findViewById(R.id.div);
        buttonDivision.setOnClickListener(this);
        buttonDelete =  findViewById(R.id.delete);
        buttonDelete.setOnClickListener(this);
        buttonDelete.setOnLongClickListener(this);
        buttonEqual =  findViewById(R.id.equal);
        buttonEqual.setOnClickListener(this);



    }

    @Override
    public boolean onLongClick(View v) {
        Button userClickedButton = findViewById(v.getId());
        String userAction = userClickedButton.getText().toString();
        if(userAction.equals(DELETE) == true ) {
               userInput.setText("");
               userInputBuffer.clear();
        }
         return true;
    }

    @Override
    public void onClick(View v) {
        Button userClickedButton = findViewById(v.getId());
        String userAction = userClickedButton.getText().toString();

        // Reset the textview color
        userInput.setTextColor(Color.BLACK);

        if(userAction.equals(EQUAL) && userInputBuffer.isEmpty() == true) {
          return;
        }


            if(userInputBuffer.isEmpty() == true &&
                 BasicCalculator.isOperator(userAction) == true){
             return;
         }else if( (userInputBuffer.isEmpty() == false) && BasicCalculator.isOperator((String)userInputBuffer.get(userInputBuffer.size()-1)) == true &&
                BasicCalculator.isOperator(userAction) == true ){
            return;
        }

        if(userAction.equals(EQUAL) && (userInputBuffer.isEmpty() == false)){

             String temp = "";

             if(BasicCalculator.isOperator((String)userInputBuffer.get(userInputBuffer.size()-1)) == true ){
                 temp = (String)userInputBuffer.get(userInputBuffer.size()-1);
                 userInputBuffer.remove(userInputBuffer.size()-1);
            }


            try {
                 userInput.setText(String.valueOf(
                        BasicCalculator.calculate(userInputBuffer.toString())));
            }catch(InvalidExpression e ){
                 userInput.setText(e.getMessage());
                 userInput.setTextColor(Color.parseColor("#f44646"));
            }
            userInputBuffer.add(temp);
            return;
        }


        if(userAction.equals(COPY_TO_CLIPBOARD) && (userInputBuffer.isEmpty() == false)){
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboard.setText(userInput.getText());
          Toast.makeText(getApplicationContext(),"Results Copied to Clipboard.",Toast.LENGTH_LONG).show();
            return;
        }


        if(userAction.equals(DELETE) == false ) {
            /** append to user input to  display **/

            if(userAction.equals(COPY_TO_CLIPBOARD)){
                Toast.makeText(getApplicationContext(),"Nothing to Copy!",Toast.LENGTH_LONG).show();
                return;
            }

            userInputBuffer.add(userAction);
            userInput.setText(userInputBuffer.toString());


         }else if(userAction.equals(DELETE) == true ){

            if(userInputBuffer.isEmpty() == false) {
                userInputBuffer.remove(userInputBuffer.size() - 1);
                userInput.setText(userInputBuffer.toString());
            }
        }






    }






}
