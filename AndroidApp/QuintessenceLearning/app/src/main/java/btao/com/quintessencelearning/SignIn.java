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

public class SignIn extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private static final String TAG = "SignIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_in);





    }

    public void signIn(View view){
        inputEmail = (EditText) findViewById(R.id.text_email);
        inputPassword = (EditText) findViewById(R.id.text_password);

        auth = FirebaseAuth.getInstance();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (task.isSuccessful()){
                            Log.d(TAG,"signin: success");
                            Toast.makeText(SignIn.this,"Logged In",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignIn.this,SubmitActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w(TAG,"signin failure", task.getException());
                            Toast.makeText(SignIn.this,"Log in Failure",Toast.LENGTH_SHORT).show();
                        }



                    }
                });
    }

    public void signUp(View view){
        Intent intent = new Intent(this,SignUp.class);
        startActivity(intent);
    }
}