package btao.com.quintessencelearning;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Calendar;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private final String TAG = "SignUp";
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private EditText inputName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirm;
    String name;
    String email;
    String password;
    String confirm_passsword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_up);
    }
    public void Register(final View view){
        inputName = (EditText) findViewById(R.id.text_name);
        inputEmail = (EditText) findViewById(R.id.text_email);
        inputPassword = (EditText) findViewById(R.id.text_password);
        inputConfirm = (EditText) findViewById(R.id.text_confirm_password);

        name = inputName.getText().toString();
        email = inputEmail.getText().toString();
        password = inputPassword.getText().toString();
        confirm_passsword = inputConfirm.getText().toString();
        if (password.equals(confirm_passsword)) {

            try {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    String uid = user.getUid();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name).build();
                                    user.updateProfile(profileUpdates);

                                    signUp(view, uid);
                                    Log.d(TAG, "created user successfully");
                                    Toast.makeText(SignUp.this, "Account Created", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), WelcomeScreen.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.w(TAG, "did not create user successfully", task.getException());
                                    Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "You cannot leave any fields blank", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(),R.string.password_match,Toast.LENGTH_SHORT).show();
        }
    }

    public void signUp(View view,String uid){
        DatabaseReference mUserRef = mDatabase.child("Users");

        inputName = (EditText) findViewById(R.id.text_name);
        inputEmail = (EditText) findViewById(R.id.text_email);

        String user_name = inputName.getText().toString();
        String user_email = inputEmail.getText().toString();

        Calendar c = Calendar.getInstance();
        Long user_join_date = new Long(c.get(Calendar.MILLISECOND));
        Log.d(TAG,user_join_date.toString());

        Long user_current_question = new Long(-1);
        String user_type = "user";
        String user_uid = uid;
        Boolean user_trial = true;
        Boolean ebook = true;

        mUserRef.child(uid).child("Current_Question").setValue(user_current_question);
        mUserRef.child(uid).child("Email").setValue(user_email);
        mUserRef.child(uid).child("Join_Date").setValue(user_join_date);
        mUserRef.child(uid).child("Name").setValue(user_name);
        mUserRef.child(uid).child("Trial").setValue(user_trial);
        mUserRef.child(uid).child("Type").setValue(user_type);
        mUserRef.child(uid).child("UID").setValue(user_uid);
        mUserRef.child(uid).child("Ebook").setValue(ebook);
    }
}
