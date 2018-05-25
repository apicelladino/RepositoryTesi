package io.github.aqibsaeed.activityrecognition;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConnection {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");



public void writeDatabase(){
    myRef.setValue("Hello, Database");
}
}
