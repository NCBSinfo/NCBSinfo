package com.rohitsuratekar.NCBSinfo;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.activity.Activity_Contact;
import com.rohitsuratekar.NCBSinfo.activity.Activity_Extra;
import com.rohitsuratekar.NCBSinfo.activity.Activity_GCMModeration;
import com.rohitsuratekar.NCBSinfo.activity.Activity_GCMRegistration;
import com.rohitsuratekar.NCBSinfo.activity.Activity_LectureHalls;
import com.rohitsuratekar.NCBSinfo.activity.Activity_NotificationReceiver;
import com.rohitsuratekar.NCBSinfo.activity.Activity_Shuttles;
import com.rohitsuratekar.NCBSinfo.adapters.adapeters_home_GoSpinner;
import com.rohitsuratekar.NCBSinfo.constants.DatabaseConstants;
import com.rohitsuratekar.NCBSinfo.constants.GCMConstants;
import com.rohitsuratekar.NCBSinfo.helper.helper_shuttles;
import com.rohitsuratekar.NCBSinfo.models.models_userNotifications;
import com.rohitsuratekar.NCBSinfo.retro.gplus_response.Image;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Home extends AppCompatActivity {

    Animation alphaAnimation, exitAnimation;
    ImageButton shuttleButton, ContactButton,updateButton, otherButton;
    RelativeLayout aniShuttle, targetShuttle, originalShuttle,aniContact,targetContact,originalContact;
    RelativeLayout aniUpdate, targetUpdate,originalUpdate,aniOther,targetOther,originalOther;
    int haveUpdates, updateRepeat, updateTime, tempCounter, tempListItem, isBuggy;
    TextView home_next,nextShuttleText, timeleft, updateText, shuttleFromText, shuttleToText;
    List<models_userNotifications> entrylist;
    String transportFROM, transportTO;
    int spinnerVariable=0,spinnerVariable2=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.home_section_fade);
        exitAnimation = AnimationUtils.loadAnimation(this,R.anim.home_section_exit);

        //Delete Old databases
        if(doesDatabaseExist(this,"ContactStore2")){
            this.deleteDatabase("ContactStore2");
        }

        //Get Height of Device Screen
        DisplayMetrics matrix = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(matrix);
        final int screen_H = matrix.heightPixels;

        //Give warning to users if Android version is lower than 5.0
        if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(DatabaseConstants.ANDROID_VERSION_WARNING, true)) {

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

                final AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                alertDialog.setTitle("Compatibility mode");
                alertDialog.setMessage("This app is best suited for Android Lollipop (21) and above. Your current android version is " + android.os.Build.VERSION.SDK_INT + " . Some animations and functions might not work properly. ");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(DatabaseConstants.ANDROID_VERSION_WARNING, false).apply();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();

            }
        }

        //Check default values
        int route =  PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(DatabaseConstants.HOME_DEFAULT_ROUTE, 0);
        transportFROM = new helper_shuttles().getRouteName(route)[0];
        transportTO = new helper_shuttles().getRouteName(route)[1];
        isBuggy = Integer.valueOf(new helper_shuttles().getRouteName(route)[2]);

        //Find and assign all section Components
        //Shuttle Section
        shuttleButton = (ImageButton)findViewById(R.id.Button_home_shuttle);
        aniShuttle = (RelativeLayout)findViewById(R.id.ShuttleAnimation);
        targetShuttle = (RelativeLayout)findViewById(R.id.ShuttleOptions);
        originalShuttle = (RelativeLayout)findViewById(R.id.ShuttleContent);
        //Contact Section
        ContactButton = (ImageButton)findViewById(R.id.Button_home_contact);
        aniContact = (RelativeLayout)findViewById(R.id.ContactAnimation);
        targetContact = (RelativeLayout)findViewById(R.id.ContactOptions);
        originalContact = (RelativeLayout)findViewById(R.id.ContactContent);
        //Update Section
        updateButton = (ImageButton)findViewById(R.id.Button_home_updates);
        aniUpdate = (RelativeLayout)findViewById(R.id.UpdateAnimation);
        targetUpdate = (RelativeLayout)findViewById(R.id.UpdateOptions);
        originalUpdate = (RelativeLayout)findViewById(R.id.UpdateContent);
        //Other section
        otherButton = (ImageButton)findViewById(R.id.Button_home_other);
        aniOther= (RelativeLayout)findViewById(R.id.OtherAnimation);
        targetOther = (RelativeLayout)findViewById(R.id.OtherOptions);
        originalOther = (RelativeLayout)findViewById(R.id.OtherContent);
        //Set all layouts of default view
        setAllDefaultview(0); //0 is for default

        LinearLayout l1 = (LinearLayout) findViewById(R.id.Home_pre_update);
        LinearLayout l2 = (LinearLayout) findViewById(R.id.Home_post_update);
        final LinearLayout l3 = (LinearLayout) findViewById(R.id.Home_sh_firsttime);
        final LinearLayout l4 = (LinearLayout) findViewById(R.id.Home_sh_timeleftLayout);
        final LinearLayout l5 = (LinearLayout) findViewById(R.id.Home_sh_setDefault);
        updateText = (TextView)findViewById(R.id.home_updateTitle);
        shuttleFromText = (TextView)findViewById(R.id.home_sh_from);
        shuttleToText = (TextView)findViewById(R.id.home_sh_totext);


        //All animation OnCLick events
        //Shuttle Section
        shuttleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(DatabaseConstants.HOME_SETSHUTTLE_DEFAULT, true)) {

                    int x = shuttleButton.getRight();
                    int y = shuttleButton.getBottom();
                    FillAnimation(originalShuttle, aniShuttle, targetShuttle, x, y, 0, screen_H, true);  //Flag will determine whether section is under selection of deselection
                }
                else {

                    l3.setVisibility(View.GONE);
                    l4.setVisibility(View.GONE);
                    l5.setVisibility(View.VISIBLE);

                }
                setAllDefaultview(1); //Change view of other selected sections
            }
        });
        final ImageButton HighlightshuttleButton = (ImageButton)findViewById(R.id.Button_home_shuttle_highlighted);
        HighlightshuttleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = HighlightshuttleButton.getRight();
                int y = HighlightshuttleButton.getBottom();
                FillAnimation(originalShuttle, aniShuttle, targetShuttle, x, y, 0, screen_H, false);

            }
        });
        //ContactSection
        ContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = ContactButton.getRight();
                int y = ContactButton.getBottom();
                FillAnimation(originalContact,aniContact,targetContact,x,y,0,screen_H, true);
                setAllDefaultview(2);
            }
        });
        final ImageButton HighlightContactButton = (ImageButton)findViewById(R.id.Button_home_contact_highlighted);
        HighlightContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = HighlightContactButton.getRight();
                int y = HighlightContactButton.getBottom();
                FillAnimation(originalContact,aniContact,targetContact,x,y,0,screen_H, false);
            }
        });
        //UpdateSection
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = updateButton.getRight();
                int y = updateButton.getBottom();
                FillAnimation(originalUpdate,aniUpdate,targetUpdate,x,y,0,screen_H, true);
                setAllDefaultview(3);
            }
        });
        final ImageButton HighlightUpdateButton = (ImageButton)findViewById(R.id.Button_home_update_highlighted);
        HighlightUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = HighlightUpdateButton.getRight();
                int y = HighlightUpdateButton.getBottom();
                FillAnimation(originalUpdate,aniUpdate,targetUpdate,x,y,0,screen_H, false);
            }
        });
        //OtherSection
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = otherButton.getRight();
                int y = otherButton.getBottom();
                FillAnimation(originalOther,aniOther,targetOther,x,y,0,screen_H, true);
                setAllDefaultview(4);
            }
        });
        final ImageButton HighlightOtherButton = (ImageButton)findViewById(R.id.Button_home_other_highlighted);
        HighlightOtherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = HighlightOtherButton.getRight();
                int y = HighlightOtherButton.getBottom();
                FillAnimation(originalOther, aniOther, targetOther, x, y, 0, screen_H, false);
            }
        });

        ImageButton extra = (ImageButton)findViewById(R.id.ExtraButton);
        extra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Activity_Extra.class);
                startActivity(intent);
            }
        });

        //Popup menu on From-To text


        shuttleToText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(Home.this, shuttleFromText);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.home_shuttle_to_fram_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int changeRoute =0;
                        if (item.getItemId() == R.id.route0) { changeRoute=0; }
                        else if (item.getItemId() == R.id.route1) {changeRoute=1; }
                        else if (item.getItemId() == R.id.route2) { changeRoute=2; }
                        else if (item.getItemId() == R.id.route3) { changeRoute=3; }
                        else if (item.getItemId() == R.id.route4) { changeRoute=4; }
                        else if (item.getItemId() == R.id.route5) { changeRoute=5; }
                        else if (item.getItemId() == R.id.route6) { changeRoute=6; }
                        else if (item.getItemId() == R.id.route7) { changeRoute=7; }
                        else if (item.getItemId() == R.id.route8) { changeRoute=8; }

                        transportFROM = new helper_shuttles().getRouteName(changeRoute)[0];
                        transportTO = new helper_shuttles().getRouteName(changeRoute)[1];
                        isBuggy = Integer.valueOf(new helper_shuttles().getRouteName(changeRoute)[2]);
                        changeShuttleText();

                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method

        shuttleFromText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(Home.this, shuttleFromText);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.home_shuttle_to_fram_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int changeRoute =0;
                        if (item.getItemId() == R.id.route0) { changeRoute=0; }
                        else if (item.getItemId() == R.id.route1) {changeRoute=1; }
                        else if (item.getItemId() == R.id.route2) { changeRoute=2; }
                        else if (item.getItemId() == R.id.route3) { changeRoute=3; }
                        else if (item.getItemId() == R.id.route4) { changeRoute=4; }
                        else if (item.getItemId() == R.id.route5) { changeRoute=5; }
                        else if (item.getItemId() == R.id.route6) { changeRoute=6; }
                        else if (item.getItemId() == R.id.route7) { changeRoute=7; }
                        else if (item.getItemId() == R.id.route8) { changeRoute=8; }

                        transportFROM = new helper_shuttles().getRouteName(changeRoute)[0];
                        transportTO = new helper_shuttles().getRouteName(changeRoute)[1];
                        isBuggy = Integer.valueOf(new helper_shuttles().getRouteName(changeRoute)[2]);
                        changeShuttleText();

                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method

        //First Time use layouts

        Spinner spinHome = (Spinner) findViewById(R.id.Home_sh_spinner);
        Spinner shuttleGoSpinner = (Spinner) findViewById(R.id.home_shuttle_go_spinner);
        String[] spinnerItems = getResources().getStringArray(R.array.home_spinner_items);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),R.layout.home_spinner_items,spinnerItems);
        spinHome.setAdapter(adapter);
        spinHome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerVariable > 0) {
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt(DatabaseConstants.HOME_DEFAULT_ROUTE, position).apply();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(DatabaseConstants.HOME_SETSHUTTLE_DEFAULT, false).apply();
                    transportFROM = new helper_shuttles().getRouteName(position)[0];
                    transportTO = new helper_shuttles().getRouteName(position)[1];
                    isBuggy = Integer.valueOf(new helper_shuttles().getRouteName(position)[2]);
                    l3.setVisibility(View.GONE);
                    l4.setVisibility(View.VISIBLE);
                    l5.setVisibility(View.GONE);
                    changeShuttleText();
                }
                spinnerVariable++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapeters_home_GoSpinner customAdapetr1 = new adapeters_home_GoSpinner(Home.this,spinnerItems);
        shuttleGoSpinner.setAdapter(customAdapetr1);
        shuttleGoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerVariable2>0){
                    Intent intent = new Intent(Home.this, Activity_Shuttles.class);
                    intent.putExtra("switch",String.valueOf(position));
                    startActivity(intent);
                }
                spinnerVariable2++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(DatabaseConstants.HOME_SETSHUTTLE_DEFAULT, true)){
            l3.setVisibility(View.VISIBLE);
            l4.setVisibility(View.GONE);
            l5.setVisibility(View.GONE);
        }
        else{

            l3.setVisibility(View.GONE);
            l4.setVisibility(View.VISIBLE);
            l5.setVisibility(View.GONE);
        }






        DatabaseHelper db = new DatabaseHelper(getBaseContext());
        entrylist = db.getAllEntries();

        //Check if you have any recent updates
        if (entrylist.size()>0){
            haveUpdates=1;
            l1.setVisibility(View.GONE);
            l2.setVisibility(View.VISIBLE);
            updateText.setText( entrylist.get(entrylist.size()-1).getNotificationTitle());
            //Set how many updates will be repeated
            if (entrylist.size()>5){ updateRepeat = 5;}
            else {updateRepeat=entrylist.size();}

        }
        else {
            haveUpdates=0;
            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.GONE);
         }

        //Elements to change
        home_next = (TextView)findViewById(R.id.home_shuttleSection_nextText);
        nextShuttleText = (TextView)findViewById(R.id.home_sh_NextShuttle);
        timeleft = (TextView)findViewById(R.id.home_sh_timing);

        //Timer
        updateTime = 3;
        tempCounter = 0;
        tempListItem = 0;
        Timer timeLeft = new Timer();
        timeLeft.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 1000); //1000


    } //OnCreate close


    // Animation to fill section
      public void FillAnimation(final View currentView, final View animatedWindows, final View targetView, int x, int y,int startRadius, int endRadius, boolean flag) {

          Animator anim = null;

          //Animation will work only of android 21 and above
          if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

          if (flag) {

              anim = ViewAnimationUtils.createCircularReveal(animatedWindows, x, y, startRadius, endRadius);
            anim.setDuration(500);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    targetView.setVisibility(View.VISIBLE);
                    targetView.startAnimation(alphaAnimation);
                }
                @Override
                public void onAnimationEnd(Animator animator) {
                   // targetView.setVisibility(View.VISIBLE);
                   // targetView.startAnimation(alphaAnimation);
                }
                @Override
                public void onAnimationCancel(Animator animator) {
                }
                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            animatedWindows.setVisibility(View.VISIBLE);
            anim.start();
            flag = false;
        }
        else{
              anim = ViewAnimationUtils.createCircularReveal(animatedWindows, x, y, endRadius, startRadius);
            anim.setDuration(400);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    targetView.setVisibility(View.GONE);
                    targetView.startAnimation(exitAnimation);

                }
                @Override
                public void onAnimationEnd(Animator animator) {
                    animatedWindows.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationCancel(Animator animator) {
                }
                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            anim.start();
            flag = true;

        }
          }
          //COMPATIBILITY CHECK
          //Change for devices with lower android version
          else{
              if (flag) {
                targetView.setVisibility(View.VISIBLE);
                animatedWindows.setVisibility(View.VISIBLE);
              }
              else{
                    targetView.setVisibility(View.GONE);
                    animatedWindows.setVisibility(View.GONE);
                   flag = true;
              }

          }
    }


    //Function to set section view to default except current selected section
    public void setAllDefaultview (int section){  //Section will be currently selected section
        if(section!=1){
        aniShuttle.setVisibility(View.GONE);
        targetShuttle.setVisibility(View.GONE);
        originalShuttle.setVisibility(View.VISIBLE);
        }
        if (section!=2) {
            aniContact.setVisibility(View.GONE);
            targetContact.setVisibility(View.GONE);
            originalContact.setVisibility(View.VISIBLE);
        }
        if(section!=3){
            aniUpdate.setVisibility(View.GONE);
            targetUpdate.setVisibility(View.GONE);
            originalUpdate.setVisibility(View.VISIBLE);

        }
        if(section!=4){
            aniOther.setVisibility(View.GONE);
            targetOther.setVisibility(View.GONE);
            originalOther.setVisibility(View.VISIBLE);
        }

        //Contact Section onCLick Events

        ImageButton im1 = (ImageButton)findViewById(R.id.home_contact_favButton);
        ImageButton im2 = (ImageButton)findViewById(R.id.home_contactListButton);
        ImageButton im3 = (ImageButton)findViewById(R.id.home_contact_emergency);
        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Home.this,Activity_Contact.class);
                intent.putExtra("switch","1");
                startActivity(intent);
            }
        });

        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Home.this,Activity_Contact.class);
                intent.putExtra("switch","0");
                startActivity(intent);
            }
        });

        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "080-2366-6666"));
                startActivity(intent);
            }
        });
    }


    public void gotoRegistration(View arg0){
        Intent intent = new Intent(Home.this, Activity_GCMRegistration.class);
        startActivity(intent);
    }
    public void gotoModerator(View arg0){
        Intent intent = new Intent(Home.this, Activity_GCMModeration.class);
        startActivity(intent);
    }

    public void gotoNotificationLog(View arg0){
       // DatabaseHelper db = new DatabaseHelper(getBaseContext());
        //db.close();
        Intent intent = new Intent(Home.this, Activity_NotificationReceiver.class);
        startActivity(intent);
    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    //Timer functions

    private void TimerMethod() {
        this.runOnUiThread(Timer_Tick);
    }
    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            //Content Here
            //TODO
            changeShuttleText();

        }
    };

    public void changeShuttleText(){
        String tempText;
        if (isBuggy==1){tempText="Next Buggy";}
        else {tempText="Next Shuttle";}
        home_next.setText(tempText);
        Calendar c2 = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        Calendar nextDate = new helper_shuttles().NextTransport(transportFROM, transportTO, format.format(c2.getTime()), isBuggy);
        SimpleDateFormat dformat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        nextShuttleText.setText(dformat.format(nextDate.getTime()));
        shuttleFromText.setText(transportFROM.toUpperCase());
        shuttleToText.setText(transportTO.toUpperCase());

        float[] Difference = new helper_shuttles().TimeLeft(format.format(c2.getTime()), format.format(nextDate.getTime()));

        timeleft.setText(""+ ((int) Difference[2])+":"+ ((int) Difference[1])+":"+ ((int) Difference[0]));

        if(haveUpdates==1){

         if (tempCounter==updateTime){

             if (tempListItem < updateRepeat){
             updateText.setText( entrylist.get(entrylist.size()-tempListItem-1).getNotificationTitle());
             tempListItem++;}
             else{
                 tempListItem=0;
             }
             tempCounter=0;

         }
            else
         {tempCounter++;}

        }

    }


}
