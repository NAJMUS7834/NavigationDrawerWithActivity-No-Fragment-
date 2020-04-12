package getways.solution.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    Button button;
    EditText EditText;
    CheckBox checkBox;
    Spinner spinner;
    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        button = findViewById(R.id.button);
        EditText = findViewById(R.id.EditText);
        checkBox = findViewById(R.id.checkBox);
        spinner = findViewById(R.id.spinner);

        populateSpinner();

        db = FirebaseFirestore.getInstance();
        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();







        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId()
                                        + " => " + document.getData());
                                EditText.setText((CharSequence) document.get("name"));
                                EditText.setSelection(EditText.getText().length());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });














        if(db.collection("users").document("userId").get() != null){

        }


    }

    private void populateSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    public void SbtHandle(View view) {

        writeData();
    }

    private void writeData() {

        String username = EditText.getText().toString();
        String category = spinner.getSelectedItem().toString();
        String status = new String();
        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(checkBox.isChecked()){
            status =checkBox.getText().toString();
        }
        if(!checkBox.isChecked()){
            status ="Not Available";
        }
        DocumentReference documentReference = db.collection("users").document(userId);
        Map<String, String> userMap = new HashMap<>();
        userMap.put("name",username);
        userMap.put("category",category);
        userMap.put("status",status);
        documentReference.set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ProfileActivity.this,"Success",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this,"eRROR"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }

}
