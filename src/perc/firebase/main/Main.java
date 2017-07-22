package perc.firebase.main;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import perc.firebase.models.User;
import processing.core.PApplet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Main extends PApplet{

    ArrayList<User> users;
    boolean hasSentMessage;

    public static void main(String[] args){
        PApplet.main("perc.firebase.main.Main");
    }

    public void settings(){
        System.out.println("Settings");
    }

    public void setup(){
        System.out.println("Setup");
        System.out.println("Im running!");
        System.out.println("Loading firebase");
        FirebaseApp fapp = null;
        users = new ArrayList<>();
        hasSentMessage = false;
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
                    System.out.println("Child count: " + dataSnapshot.getChildrenCount());
                    for(DataSnapshot child:dataSnapshot.getChildren()){
                        if(!child.getKey().equals("updateNode")) {
                            System.out.println("A user");
                            System.out.println("UUID: " + child.getKey());
                            String name=null;
                            String pass=null;
                            String uuid=child.getKey();
                            for(DataSnapshot data:child.getChildren()) {
                                if(data.getKey().equals("name")){
                                    name = data.getValue(String.class);
                                }else if(data.getKey().equals("pass")){
                                    pass = data.getValue(String.class);
                                }else if(data.getKey().equals("uuid")){
                                    uuid = data.getValue(String.class);
                                }
                            }
                            User foundUser = new User(name,pass,uuid);
                            System.out.println(foundUser.toString());
                            users.add(foundUser);
                        }else{
                            System.out.println("Update node found");
                        }
                    }
                    System.out.println("Children loop ended");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("An error ocured: " + databaseError.getCode());
                }
            });
            dbref.child("updateNode").setValue("update");
            //System.out.println("Adding a user");
            //Map<String, User> users = new HashMap<String, User>();
            //String uuid = UUID.randomUUID().toString();
            //users.put(uuid, new User("ThePerkinrex","1234",uuid));
            //dbref.setValue(users);

        }else{
            System.out.println("Firebase didn't load, exiting");
            exit();
        }
    }

    public void draw(){
        if(!hasSentMessage) {
            if (!users.isEmpty()) {
                for (User user : users) {
                    System.out.println("A user " + user.toString() + " made it to the main loop");
                }
                background(color(255, 255, 0));
                hasSentMessage = true;
            } else {
                System.out.println("Checking");
            }
        }
    }
}
