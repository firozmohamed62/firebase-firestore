package com.example.firebaserecycler;

import com.google.firebase.firestore.Exclude;

public class Note {
    private String documentId;
    private String title;
    private String description;


    // firestore always need an empty constructor , else it will crash

    public Note(){
        //public no -arg constructor needed
    }



    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Note(String title, String description){
        this.title=title;
        this.description=description;

    }



    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

}
