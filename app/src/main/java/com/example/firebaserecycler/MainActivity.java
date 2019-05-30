package com.example.firebaserecycler;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";//used for log msg
    private static final String KEY_TITLE="title";
    private static final String KEY_DESCRIPTION="description";


    private EditText editTextTitle;
    private EditText editTextDescription;
     Button sendDataButton;
     Button loadNoteBtn;
     Button updateDescBtn;
     Button deleteDescBtn;
     Button deleteNoteBtn;
     private TextView textViewData;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private DocumentReference noteRef=db.collection("Notebook").document("My First Note");
    //or we can call db.document("Notebook/My First Note");


    private ListenerRegistration  noteListener;


    @Override
    protected void onStart() {
        super.onStart();
        noteListener=noteRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                if (e != null){
                    Toast.makeText(MainActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (documentSnapshot.exists()){
                    Note note= documentSnapshot.toObject(Note.class);
                    String title= note.getTitle();
                    String description= note.getDescription();

                    textViewData.setText("Title:" + title + "\n" + "Description:" + description);



                }

                //used when note is deleted




                else {
                    textViewData.setText("");
                }

            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        noteListener.remove();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        editTextTitle=(EditText)findViewById(R.id.edit_text_title);
        editTextDescription=(EditText)findViewById(R.id.edit_text_description);
        sendDataButton=(Button)findViewById(R.id.sendDataBtn);
        loadNoteBtn=(Button)findViewById(R.id.loadNoteBtn);
        updateDescBtn=(Button)findViewById(R.id.updateDescBtn);
        deleteDescBtn=(Button)findViewById(R.id.deleteDescBtn);
        deleteNoteBtn=(Button)findViewById(R.id.deleteNoteBtn);
        textViewData=(TextView)findViewById(R.id.text_view_data);











        sendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title= editTextTitle.getText().toString();
                String description= editTextDescription.getText().toString();

                //we need a container to safely move data to database
                //for this we can use java map or java object

           //commenting out map to use class as an update


                Note note=new Note(title,description);

           /*

                Map<String,Object> note= new HashMap<>();

                note.put(KEY_TITLE,title);
                note.put(KEY_DESCRIPTION,description);


                */

                //reference to first collection

                //changed                 db.collection("Notebook").document("My First Note").set(note)
                noteRef.set(note)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "note saved", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "error!", Toast.LENGTH_SHORT).show();

                            }
                        });


            }



        });







        updateDescBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description=editTextDescription.getText().toString();

               /* ---- */
                Map<String,Object> note = new HashMap<>();
                note.put(KEY_DESCRIPTION,description);
                noteRef.set(note, SetOptions.merge());
                /* ----- */

                /* or we can use just
                noteRef.update(KEY_DESCRIPTION,description);
                // if content already there ,it will get updated ,else nothing happens
                 */
            }
        });






        deleteDescBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> note = new HashMap<>();
                note.put(KEY_DESCRIPTION, FieldValue.delete());



                noteRef.update(note);

                //or use
                //noteRef.update(KEY_DESCRIPTION,FieldValue.delete());
                //can add onsuccesslistener or failurelistener
            }
        });



        deleteNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteRef.delete();
            }
        });





        loadNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                //document snapshot here contains all our data

                                if (documentSnapshot.exists()){
                                /*    String title=documentSnapshot.getString(KEY_TITLE);
                                    String description= documentSnapshot.getString(KEY_DESCRIPTION);

                                    */


                                Note note= documentSnapshot.toObject(Note.class);
                                String title= note.getTitle();
                                String description= note.getDescription();

                                    //Map<String,Object> note=documentSnapshot.getData();
                                    textViewData.setText("Title:" + title + "\n" + "Description:" + description);



                                }else {
                                    Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                                }



                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });





    }



}