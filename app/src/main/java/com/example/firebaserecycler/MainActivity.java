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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
     Button addDataBtn;
     Button loadNoteBtn;

     private TextView textViewData;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private DocumentReference noteRef=db.collection("Notebook").document("My First Note");
    //or we can call db.document("Notebook/My First Note");

    private CollectionReference notebookRef=db.collection("Notebook");

    private ListenerRegistration  noteListener;


    @Override
    protected void onStart() {
        super.onStart();

        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if (e != null){
                    return;
                }


                String data="";



                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {

                    Note note=documentSnapshot.toObject(Note.class);


                    note.setDocumentId(documentSnapshot.getId());



                    String documentId = note.getDocumentId();
                    String title=note.getTitle();
                    String description= note.getDescription();


                    data += "ID:" +documentId +"\n Title:" + title + "\nDescription:" + description  + "\n\n";



                }



                textViewData.setText(data);
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
        addDataBtn=(Button)findViewById(R.id.addDataBtn);
        loadNoteBtn=(Button)findViewById(R.id.loadNoteBtn);

        textViewData=(TextView)findViewById(R.id.text_view_data);











        addDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();

                //we need a container to safely move data to database
                //for this we can use java map or java object

                //commenting out map to use class as an update


                Note note = new Note(title, description);

           notebookRef.add(note);//can  add successlistener and failure listener




            }
        });










        loadNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notebookRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //query snapshot has multiple document snapshots

                        String data ="";

                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){

                            Note note=documentSnapshot.toObject(Note.class);


                            note.setDocumentId(documentSnapshot.getId());



                            String documentId = note.getDocumentId();
                            String title=note.getTitle();
                            String description= note.getDescription();


                            data += "ID:" +documentId +"\n Title:" + title + "\nDescription:" + description  + "\n\n";



                        }
                        textViewData.setText(data);

                    }
                });
            }
        });





    }



}