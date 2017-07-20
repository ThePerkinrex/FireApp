package perc.firebase.main;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import perc.firebase.models.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args){
        System.out.println("Im running!");
        System.out.println("Loading firebase");
        FirebaseApp fapp = null;
        try {
            FirebaseOptions.Builder options = new FirebaseOptions.Builder();
            options.setServiceAccount(new FileInputStream("res/snapapp-b6686-firebase-adminsdk-sj4rk-19c25cea12.json"));
            options.setDatabaseUrl("https://snapapp-b6686.firebaseio.com");
            options.build();
            fapp = FirebaseApp.initializeApp(options.build());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(fapp!=null) {
            System.out.println("Firebase loaded!");
            System.out.println(fapp.getName());  // "[DEFAULT]"
            FirebaseDatabase db = FirebaseDatabase.getInstance(fapp);
            System.out.println("Database loaded");
            DatabaseReference dbref = db.getReference("users");
            System.out.println("Database reference loaded");
            System.out.println(dbref);
            System.out.println("Retrieving UUIDs");
            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    System.out.println(user);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
            System.out.println("Adding a user");
            Map<String, User> users = new HashMap<String, User>();
            String uuid = UUID.randomUUID().toString();
            users.put(uuid, new User("ThePerkinrex","1234",uuid));
            dbref.setValue(users);

        }else{
            System.out.println("Firebase didn't load, exiting");
        }
    }
}
