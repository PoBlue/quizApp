package com.blues.quizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String noAnswer = "noAnswer";
    Map<String,String> correctAnswers = new HashMap<String, String>();
    Map<String,String> answers = new HashMap<String, String>();
    String[] answerKeys = new String[]{
            "Queastion 1",
            "Queastion 2",
            "Queastion 3",
            "Queastion 5"
    };
    int countCheckBoxAnswer = 0;
    int countCorrectAnswer = -1;
    String checkBoxAnswerKey = "Question 4";
    String answerToQuestion6 = "no answer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAnswerDict();
        addRationButtons();
        addCheckBoxes();

        EditText answerTextView = (EditText) findViewById(R.id.answer6_editTextView);
        answerTextView.setOnEditorActionListener(answerListener);
    }

    TextView.OnEditorActionListener answerListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            String inputText = textView.getText().toString();
            answerToQuestion6 = inputText;

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            return true;
        }
    };

    public void initAnswerDict(){
        String[] correctAnswersArray = getResources().getStringArray(R.array.answers);

        for (int i = 0; i < correctAnswersArray.length; i++) {
            String key = answerKeys[i];
            correctAnswers.put(key,correctAnswersArray[i]);
            answers.put(key, noAnswer);
        }
    }

    View.OnClickListener checkBoxListerner = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            String[] correctAnswer = getResources().getStringArray(R.array.answers_to_q4);
            countCorrectAnswer = correctAnswer.length;
            CheckBox checkBox = (CheckBox) view;
            String answer = checkBox.getText().toString();

            if (Arrays.asList(correctAnswer).contains(answer)){
                if (checkBox.isChecked()){
                    countCheckBoxAnswer += 1;
                } else {
                    countCheckBoxAnswer -= 1;
                }
            }
        }
    };

    public void addCheckBoxes(){
        LinearLayout checkBoxes = (LinearLayout) findViewById(R.id.checkboxes_choices4);
        String[] choices = getResources().getStringArray(R.array.choices4);

        for (int i = 0; i < choices.length; i++){
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(choices[i]);
            checkBox.setId(i);
            checkBox.setOnClickListener(checkBoxListerner);
            checkBoxes.addView(checkBox);
        }
    }

    public void addRationButtons(){
        int[] choicesIds = new int[]{
                R.array.choices1,
                R.array.choices2,
                R.array.choices3,
                R.array.choices5
        };

        int[] radioGroupIds = new int[]{
                R.id.radioGroup_choices1,
                R.id.radioGroup_choices2,
                R.id.radioGroup_choices3,
                R.id.radioGroup_choices5
        };

        for (int i = 0; i < radioGroupIds.length; i++){
            addRationButtonsToGroup(radioGroupIds[i],choicesIds[i],answerKeys[i]);
        }
    }

    public void addRationButtonsToGroup(int groupId, int choicesArrayId, final String key){
        RadioGroup radioGroup = (RadioGroup) findViewById(groupId);
        String[] choices = getResources().getStringArray(choicesArrayId);

        for (int i = 0; i < choices.length; i++){
            RadioButton radioButtonView = new RadioButton(this);
            radioButtonView.setText(choices[i]);
            radioButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String answer = ((RadioButton) view).getText().toString();
                    answers.put(key,answer);
                }
            });
            radioGroup.addView(radioButtonView);
        }
    }


    public void submit(View v){
        int score = 0;
        String noanswerText = "No answer in: ";
        String correctAnswerText = "# Correct answer: ";
        String inCorrectAnswerText = "# InCorrect answer #: \n\n";

        for (Map.Entry<String,String> answer: answers.entrySet()){
            String key = answer.getKey();
            String value = answer.getValue();

            if (value.equals(noAnswer)){
                noanswerText = noanswerText + key ;
                Toast.makeText(this,noanswerText,Toast.LENGTH_LONG).show();
                return;
            } else if (value.equals(correctAnswers.get(key))){
                score += 5;
                correctAnswerText = correctAnswerText + key + ", ";
            } else {
                inCorrectAnswerText = inCorrectAnswerText + key + ",should be: " + correctAnswers.get(key) + " \n\n";
            }
        }

        if (countCorrectAnswer != countCheckBoxAnswer){
            inCorrectAnswerText += checkBoxAnswerKey + ", " + "should be: sensory and motor and interneurons \n\n";
        } else {
            score += 5;
            correctAnswerText += checkBoxAnswerKey + ", ";
        }

        String correctAnswer6 = getResources().getString(R.string.answer_last_question);
        if (answerToQuestion6.equals(correctAnswer6)){
            score += 10;
            correctAnswerText += "Queastion 6, ";
        } else {
            inCorrectAnswerText += "Queastion 6: " + " should be: " + correctAnswer6 + " \n";
        }

        Toast.makeText(this,
                "# The final score is: --> " + score + "\n"
                        + "---------- \n"
                        + correctAnswerText + "\n"
                        + "---------- \n"
                        + inCorrectAnswerText,
                Toast.LENGTH_LONG)
                .show();
    }

}
