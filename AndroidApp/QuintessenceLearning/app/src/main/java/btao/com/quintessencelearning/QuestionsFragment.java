package btao.com.quintessencelearning;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionsFragment extends Fragment {
    private TextView mTextMessage;
    private FirebaseAuth auth;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mQuestionRef;
    private DatabaseReference mUserRef;
    private DatabaseReference mQuestion;
    private DatabaseReference mUser;
    Long currentQuestion;

    private final String TAG = "MainActivity";



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public QuestionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionsFragment newInstance(String param1, String param2) {
        QuestionsFragment fragment = new QuestionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //questionNav();
        return inflater.inflate(R.layout.fragment_questions, container, false);


    }
    public void setQuestion(String text){
        TextView question = (TextView) getView().findViewById(R.id.text_message);
        question.setText(text);
    }

    public void questionNav(){
        mTextMessage = (TextView) getView().findViewById(R.id.text_message);
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(getActivity(), SignIn.class);
            startActivity(intent);
            getActivity().finish();
        }
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mQuestionRef = mDatabaseRef.child("Questions");
        mUserRef = mDatabaseRef.child("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userUID = user.getUid();
        mUser = mUserRef.child(userUID);


        final ValueEventListener questionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d(TAG, "Inside class");
                    String text = (String) child.child("Text").getValue();
                    Long count = (Long) child.child("count").getValue();
                    if (count==currentQuestion) {
                        mTextMessage.setText(text);
                        Log.d(TAG, text);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(getActivity(),databaseError.toString(),Toast.LENGTH_SHORT).show();
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentQuestion = (Long) dataSnapshot.child("Current_Question").getValue();
                mQuestionRef.addListenerForSingleValueEvent(questionListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"Couldn't get user ref");
            }
        });

    }




}