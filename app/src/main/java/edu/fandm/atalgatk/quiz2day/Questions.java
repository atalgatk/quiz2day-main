package edu.fandm.atalgatk.quiz2day;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//firebase imports
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;


public class Questions extends AppCompatActivity {

    Button btnA, btnB, btnC, btnD, submit; // buttons for answers and submitting

    //stores what the user selects & the correct ans
    String selectedAnswer ="";
    String correctAnswer ="";

    //stores subject
    String subject = "";

    //store q related items
    TextView questionText;
    ImageView questionImage;

    //stores explanation of correct ans
    String explanationText ="";
    TextView subjectTitle;

    //firebase database reference
    FirebaseFirestore db;

    //to keep track of all colors according to users choice
    ColorStateList defaultColor, selectedColor, correctColor, wrongColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_questions);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //linking my buttons from the xml
        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);
        submit = findViewById(R.id.submit_button);

        //linking my question + img
        questionText = findViewById(R.id.questionText);
        questionImage = findViewById(R.id.questionImage);
        subjectTitle = findViewById(R.id.subjectTitle);

        //defining my colors for the buttons during different states
        defaultColor = ColorStateList.valueOf(Color.parseColor("#3E3D53")); //original gray
        selectedColor = getColorStateList(android.R.color.holo_blue_light); // when clicked
        correctColor = getColorStateList(android.R.color.holo_green_dark);  // correct
        wrongColor = getColorStateList(android.R.color.holo_red_dark);      // wrong

        //initializing my firestore database
        db = FirebaseFirestore.getInstance();

        //getting data (subject + level) from previous screen
        subject = getIntent().getStringExtra("subject");
        String level = getIntent().getStringExtra("level");

        //fallback used to prevent crashing
        if (subject == null ) subject = "Math";
        if (level == null) level = "ESL-1";

        //setting subject
        subjectTitle.setText(subject);

        //loading question from FIRESTORE based on subject and level
        db.collection("Qbank")
                .whereEqualTo("Category", subject)
                .whereEqualTo("Level", level)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                            if (queryDocumentSnapshots.isEmpty()) {
                                questionText.setText("No questions found"); //show if no questions found
                                return;
                            }

                            // pick a random question
                            int size = queryDocumentSnapshots.size();
                            int randomIndex = new java.util.Random().nextInt(size);

                            QueryDocumentSnapshot doc = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(randomIndex);

                            // Setting our QUESTION TEXT
                            questionText.setText(doc.getString("Question"));

                            //Set answer options
                            btnA.setText(doc.getString("A"));
                            btnB.setText(doc.getString("B"));
                            btnC.setText(doc.getString("C"));
                            btnD.setText(doc.getString("D"));

                            // SET the CORRECT ANSWER
                            String answerKey = doc.getString("Answer");

                            //match the key to the actual text we have in our QBank
                            if (answerKey.equals("A")) correctAnswer = doc.getString("A");
                            else if (answerKey.equals("B")) correctAnswer = doc.getString("B");
                            else if (answerKey.equals("C")) correctAnswer = doc.getString("C");
                            else if (answerKey.equals("D")) correctAnswer = doc.getString("D");

                            // get and show explanation
                            explanationText = doc.getString("Why");

                            // how we handle an img if there is one
                            String imageUrl = doc.getString("img");

                            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                                questionImage.setVisibility(View.VISIBLE); //if image exists we show it
                                Picasso.get().load(imageUrl).into(questionImage);
                            } else {
                                questionImage.setImageDrawable(null);
                                questionImage.setVisibility(View.GONE); //hide image if none available
                            }

                        }) // if we have an error loading questions
                        .addOnFailureListener(e -> {
                            questionText.setText("Error loading question");
                        });


        //selection logic
        // if a user clicks an option we store their ans and highlight it
        btnA.setOnClickListener(v->selectAnswer(btnA));
        btnB.setOnClickListener(v->selectAnswer(btnB));
        btnC.setOnClickListener(v -> selectAnswer(btnC));
        btnD.setOnClickListener(v -> selectAnswer(btnD));

        //submit button logic
        submit.setOnClickListener(v->{
            if (selectedAnswer.equals("")) return; //if nth selected do nth

            if (selectedAnswer.equals(correctAnswer)){ //if ans is correct
                highlightCorrect(); //we highlight the correct answer it green

                //saving that the user completed the subject for today
                SharedPreferences prefs = getSharedPreferences("Quiz2Day", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                //getting todays date
                String today = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault())
                        .format(new java.util.Date());

                //marking subject as done
                editor.putBoolean(subject + "_done", true);
                editor.putString("last_date", today);
                editor.apply();

                //go to why (explanation) screen
                new android.os.Handler().postDelayed(() -> {
                    Intent i = new Intent(this, Explanation.class);
                    i.putExtra("explanation", explanationText);  //passing explanation
                    i.putExtra("answer", correctAnswer);        // passing the correct answer
                    i.putExtra("subject", subject);             // passing the subject

                    startActivity(i);
                }, 500); //adding a delay before activity starts
            }else{
                highlightWrong(); // if ans is wrong we highlight it red
                Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show(); //show msg saying incorrect
            }
        });

    }

    // when selecting answer we control the highlights accordingly
    private void selectAnswer(Button selected) {
        //reset
        resetButtons();
        selected.setBackgroundTintList(selectedColor);
        selectedAnswer = selected.getText().toString();
    }

    // to reset all buttons to their default color
    private void resetButtons(){
        btnA.setBackgroundTintList(defaultColor);
        btnB.setBackgroundTintList(defaultColor);
        btnC.setBackgroundTintList(defaultColor);
        btnD.setBackgroundTintList(defaultColor);
    }

    //if answer is correct turns the answer green
    private void highlightCorrect() {
        if (selectedAnswer.equals(btnA.getText().toString()))
            btnA.setBackgroundTintList(correctColor);

        if (selectedAnswer.equals(btnB.getText().toString()))
            btnB.setBackgroundTintList(correctColor);

        if (selectedAnswer.equals(btnC.getText().toString()))
            btnC.setBackgroundTintList(correctColor);

        if (selectedAnswer.equals(btnD.getText().toString()))
            btnD.setBackgroundTintList(correctColor);
    }


    //if answer is wrong turns highlight red
    private void highlightWrong() {
        if (selectedAnswer.equals(btnA.getText().toString()))
            btnA.setBackgroundTintList(wrongColor);

        if (selectedAnswer.equals(btnB.getText().toString()))
            btnB.setBackgroundTintList(wrongColor);

        if (selectedAnswer.equals(btnC.getText().toString()))
            btnC.setBackgroundTintList(wrongColor);

        if (selectedAnswer.equals(btnD.getText().toString()))
            btnD.setBackgroundTintList(wrongColor);
    }


    //handles the back button in the top bar
    @Override
    public boolean onSupportNavigateUp(){
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

}