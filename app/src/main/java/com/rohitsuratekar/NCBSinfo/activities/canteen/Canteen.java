package com.rohitsuratekar.NCBSinfo.activities.canteen;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.rohitsuratekar.NCBSinfo.utilities.General;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 05-07-16.
 */
public class Canteen extends BaseActivity implements View.OnClickListener, AppConstants {


    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.CANTEEN;
    }


    @BindView(R.id.canteen_breakfastIcon)
    ImageView breakfastIcon;
    @BindView(R.id.canteen_morningTeaIcon)
    ImageView morningTeaIcon;
    @BindView(R.id.canteen_lunchIcon)
    ImageView lunchIcon;
    @BindView(R.id.canteen_eveningTeaIcon)
    ImageView eveningTeaIcon;
    @BindView(R.id.canteen_dinnerIcon)
    ImageView dinnerIcon;
    @BindView(R.id.canteen_breakfastText)
    TextView breakfastText;
    @BindView(R.id.canteen_morningTeaText)
    TextView morningTeaText;
    @BindView(R.id.canteen_lunchText)
    TextView lunchText;
    @BindView(R.id.canteen_eveningTeaText)
    TextView eveningTeaText;
    @BindView(R.id.canteen_dinnerText)
    TextView dinnerText;
    @BindView(R.id.canteen_welcome_text)
    TextView welcomeText;
    @BindView(R.id.canteen_message)
    TextView secondMessage;
    @BindView(R.id.canteen_check_menu)
    Button checkMenu;

    Context context;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        context = getBaseContext();
        calendar = Calendar.getInstance();

        setIcons();
        breakfastIcon.setOnClickListener(this);
        morningTeaIcon.setOnClickListener(this);
        lunchIcon.setOnClickListener(this);
        eveningTeaIcon.setOnClickListener(this);
        dinnerIcon.setOnClickListener(this);
        checkMenu.setOnClickListener(this);

        if(!new CanteenData(calendar).isFoodAvailable()){
            welcomeText.setText(getResources().getString(R.string.canteen_welcome_empty));
            secondMessage.setText("");
        }



    }

    @Override
    public void onClick(View view) {
        CanteenData canteen = new CanteenData(calendar);
        switch (view.getId()) {
            case R.id.canteen_breakfastIcon:
                if (canteen.isBreakfast()) {
                    showDialog(getString(R.string.canteen_breakfast), canteen.getAllBreakfastLocations(), breakfastIcon);
                }
                break;
            case R.id.canteen_morningTeaIcon:
                if (canteen.isMidMorningTea()) {
                    showDialog(getString(R.string.canteen_morningTea), canteen.getAllMorningTeaLocations(), morningTeaIcon);
                }
                break;
            case R.id.canteen_lunchIcon:
                if (canteen.isLunch()) {
                    showDialog(getString(R.string.canteen_lunch), canteen.getAllLunchLocations(), lunchIcon);
                }
                break;
            case R.id.canteen_eveningTeaIcon:
                if (canteen.isMidEveningTea()) {
                    showDialog(getString(R.string.canteen_eveningTea), canteen.getAllEveningTeaLocations(), eveningTeaIcon);
                }
                break;
            case R.id.canteen_dinnerIcon:
                if (canteen.isDinner()) {
                    showDialog(getString(R.string.canteen_dinner), canteen.getAllDinnerLocations(), dinnerIcon);
                }
                break;
            case R.id.canteen_check_menu:
                Toast.makeText(getBaseContext(), "Working on this!", Toast.LENGTH_SHORT).show();
                break;

        }

    }


    private void setIcons() {

        CanteenData canteen = new CanteenData(calendar);


        setColors(breakfastIcon, breakfastText, canteen.isBreakfast());
        setColors(morningTeaIcon, morningTeaText, canteen.isMidMorningTea());
        setColors(lunchIcon, lunchText, canteen.isLunch());
        setColors(eveningTeaIcon, eveningTeaText, canteen.isMidEveningTea());
        setColors(dinnerIcon, dinnerText, canteen.isDinner());

    }

    private void setColors(ImageView view, TextView text, boolean isActive) {
        if (isActive) {
            new General().setColorToIcon(context, view, R.color.canteen_active);
            new General().setColorToText(context, text, R.color.canteen_active);
        } else {
            new General().setColorToIcon(context, view, R.color.canteen_inactive);
            new General().setColorToText(context, text, R.color.canteen_inactive);
        }
    }

    private void showDialog(String location, List<canteens> places, ImageView view) {

        String locationString = "Available at following location(s)\n\n";
        for (canteens c : places) {
            locationString = locationString + getString(c.getNameID()) + "\n";
        }
        new AlertDialog.Builder(Canteen.this)
                .setTitle(location)
                .setMessage(locationString)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(view.getDrawable())
                .show();
    }


}
