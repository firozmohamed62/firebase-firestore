package com.example.firebaserecycler;

public class Note {
    private String title;
    private String description;


    // firestore always need an empty constructor , else it will crash

    public Note(){
        //public no -arg constructor needed
    }



    public Note(String title,String description){
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
