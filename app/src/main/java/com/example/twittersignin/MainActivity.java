package com.example.twittersignin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnSignIn, btnSignOut, btnGetProfile;
    private TextView txtResult;
    private TwitterSignInManager twitterSignInManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignOut = findViewById(R.id.btnSignOut);
        btnGetProfile = findViewById(R.id.btnGetProfile);
        txtResult = findViewById(R.id.txtResult);

        twitterSignInManager = TwitterSignInManager.getInstance(this);

        btnSignIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        twitterSignInManager.signIn();
                    }
                }
        );

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterSignInManager.signOut();
                Toast.makeText(MainActivity.this, "Signed Out.", Toast.LENGTH_SHORT).show();
                txtResult.setText("");
            }
        });

        btnGetProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (twitterSignInManager.isUserAlreadySignedIn()) {
                    Log.d("TAG","1");
                    twitterSignInManager.showProfile();
                } else {
                    Log.d("TAG","2");
                    Toast.makeText(MainActivity.this, "Please Log in.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}