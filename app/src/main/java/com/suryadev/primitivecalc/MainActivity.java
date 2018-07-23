package com.suryadev.primitivecalc;

import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener , View.OnLongClickListener {


        private TextView userInput;
        private Button button0 , button1 , button2 , button3 , button4 , button5 , button6 ,
                button7 , button8 , button9 , buttonSum , buttonSub , buttonDivision ,
                buttonMul , buttonDelete , buttonEqual ,copyToClipboard,dot;

        private List userInputBuffer;
        private static  String DELETE = "DELETE";
        private static  String EQUAL = "=";
        private static  String COPY_TO_CLIPBOARD = "Copy";
        private final int REQ_CODE_SPEECH_INPUT = 100;


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


        ((Button)findViewById(R.id.speak)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });


    }


    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
       // intent.putExtra(RecognizerIntent.EXTRA_PROMPT, );
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),"Speech not Supported",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    try{

                        String resultValue = result.get(0);

                        Expression expression = new ExpressionBuilder(resultValue).build();

                        // Calculate the result and display
                        double expResult = expression.evaluate();
                        userInputBuffer.add(String.valueOf(expResult));
                        userInput.setText(String.valueOf(new Double(expResult).longValue()));
                        

                    }catch(Exception e) {
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.calc_layout), "Can't get that numbers try again", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                promptSpeechInput();
                            }
                        });
                        snackbar.show();
                    }

                }
                break;
            }

        }
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
        userInput.setTextColor(Color.parseColor("#747577"));

        if(userAction.equals(EQUAL) && userInputBuffer.isEmpty() == true) {
          return;
        }


            if(userInputBuffer.isEmpty() == true &&
                 BasicCalculator.isOperator(userAction) == true){

                /**  Special Case for (-) Hold The Previous result value **/
                    if(userAction.equals("-")) {
                               userInputBuffer.add("-");
                                userInput.setText(userInputBuffer.toString());
                    }
                /**  Special Case for (-) Hold The Previous result value **/

             return;
         }else if( (userInputBuffer.isEmpty() == false) && BasicCalculator.isOperator((String)userInputBuffer.get(userInputBuffer.size()-1)) == true &&
                BasicCalculator.isOperator(userAction) == true ){
            return;
        }

        if(userAction.equals(EQUAL) && (userInputBuffer.isEmpty() == false)){

             String temp = "";


             /** Avoid Tailing Operators **/
             if(BasicCalculator.isOperator((String)userInputBuffer.get(userInputBuffer.size()-1)) == true ){
                 temp = (String)userInputBuffer.get(userInputBuffer.size()-1);
                 userInputBuffer.remove(userInputBuffer.size()-1);
            }

           /**  // CUSTOM ALGORITHM TO EVALUATE EXPRESSION
            try {
                 userInput.setText(String.valueOf(
                        BasicCalculator.calculate(userInputBuffer.toString())));
                 userInput.setTextColor(Color.BLACK);
                             userInputBuffer.clear();
                             userInputBuffer.add(userInput.getText().toString());


            }catch(InvalidExpression e ){
                 userInput.setText(e.getMessage());
                 userInput.setTextColor(Color.parseColor("#f44646"));
            }
         **/

            /** USING EXP4J LIB TO EVALUATE EXPRESSION **/

                     try {

                        Expression expression = new ExpressionBuilder(userInputBuffer.toString()).build();

                         // Calculate the result and display
                        double result = expression.evaluate();

                        userInput.setText(String.valueOf(new Double(result).longValue()));

                        userInputBuffer.clear();
                        userInput.setTextColor(Color.BLACK);
                        userInputBuffer.add(userInput.getText().toString());

                    } catch (Exception ex) {
                        // Display an error message
                        userInput.setText("Invalid Expression");
                        userInput.setTextColor(Color.parseColor("#f44646"));
                    }


            /** USING EXP4J LIB TO EVALUATE COMPUTATION  **/

            userInputBuffer.add(temp);
            return;
        }


        if( userAction.equals(COPY_TO_CLIPBOARD) && (userInputBuffer.isEmpty() == false)){
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboard.setText(userInput.getText());
          Toast.makeText(getApplicationContext(),"Results Copied to Clipboard.",Toast.LENGTH_LONG).show();
            return;
        }


        if(userAction.equals(DELETE) == false ) {
            /** append to user input to  display **/

            if(userAction.equals(COPY_TO_CLIPBOARD)){
                Toast.makeText(getApplicationContext(),"Nothing to Copy!",Toast.LENGTH_SHORT).show();
                return;
            }


             /** Appending the values to expression buffer from the user screen **/
            userInputBuffer.add(userAction);
            userInput.setText(userInputBuffer.toString());


         }else if(userAction.equals(DELETE) == true ){

            if(userInputBuffer.isEmpty() == false) {
                userInputBuffer.remove(userInputBuffer.size() - 1);
                userInput.setText(userInputBuffer.toString());
                userInput.setTextColor(Color.parseColor("#747577"));
            }
        }






    }






}
