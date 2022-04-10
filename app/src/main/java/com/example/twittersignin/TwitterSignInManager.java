package com.example.twittersignin;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.hotspot2.pps.Credential;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;

public class TwitterSignInManager {

    private static TwitterSignInManager instance = null;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private OAuthProvider.Builder provider;
    private FirebaseUser firebaseUser;
    private Activity activity;

    private TwitterSignInManager() {
    }

    public static TwitterSignInManager getInstance(Context context) {
        if (instance == null) {
            instance = new TwitterSignInManager();
        }
        instance.init(context);
        return instance;
    }

    private void init(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        provider = OAuthProvider.newBuilder("twitter.com");
        firebaseAuth = FirebaseAuth.getInstance();
    }


    void signIn() {
        firebaseAuth
                .startActivityForSignInWithProvider(/* activity= */ activity, provider.build())
                .addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(context, "Signed In Successfully.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Signed In Failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    void showProfile() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String profile = "Name: " + user.getDisplayName() +
                "\n" +
                "Email: " + user.getEmail() + "\n" +
                "Phone Number: " + user.getPhoneNumber();
        Log.d("TAG", profile);
    }

    boolean isUserAlreadySignedIn() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            return true;
        } else {
            return false;
        }
    }

    void signOut() {
        firebaseAuth.signOut();
    }

    public void unlink(String providerId) {
        // [START auth_unlink]
        firebaseAuth.getCurrentUser().unlink(providerId)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Auth provider unlinked from account
                            // ...
                            Toast.makeText(context, "Account unlinked.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        // [END auth_unlink]
    }

    void checkPendingResult() {
        Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    showProfile();
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                }
                            });
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.
            signIn();
        }
    }

}
