package com.rohitsuratekar.NCBSinfo.activities.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.Dashboard;
import com.rohitsuratekar.NCBSinfo.activities.login.Login;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportMethods;
import com.rohitsuratekar.NCBSinfo.activities.transport.edit.TransportEdit;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.Route;
import com.rohitsuratekar.NCBSinfo.background.CurrentSession;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;
import com.secretbiology.helpers.general.listeners.OnClickGuard;
import com.secretbiology.helpers.general.listeners.OnSwipeTouchListener;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Home Page
 * <p>
 * This page is the first page when user opens app.
 * Details about favorite route will be shown at first. Users will be also shown few tips and suggestions based on
 * preference matrix and database. Most of the UI updating operations are done in background {@link #setUpItems()}.
 * This activity also implements some custom made functions like ClickGuard and OnSwipeTouchListener.
 *
 * @see OnClickGuard : Avoids taking fast button clicks by user
 * @see OnSwipeTouchListener : Detects left, right, up and down swipe moments
 */
public class Home extends BaseActivity {

    @BindView(R.id.hm_tx_above_transport)
    TextView aboveTransport;
    @BindView(R.id.hm_tx_next_transport)
    TextView nextTransport;
    @BindView(R.id.hm_tx_sugg1)
    TextView transportSuggestion1;
    @BindView(R.id.hm_tx_sugg2)
    TextView transportSuggestion2;
    @BindView(R.id.hm_tx_sugg3)
    TextView transportSuggestion3;
    @BindView(R.id.hm_tx_next_place)
    TextView currentPlace;
    @BindView(R.id.hm_tx_weekday)
    TextView currentDay;
    @BindView(R.id.hm_tx_date)
    TextView currentDate;
    @BindView(R.id.hm_tx_type)
    TextView currentType;
    @BindView(R.id.hm_tx_seats)
    TextView currentSeats;
    @BindView(R.id.hm_tx_sugg_note)
    TextView suggestionNote;

    @BindView(R.id.hm_im_back_image)
    ImageView backImage;
    @BindView(R.id.hm_im_fav)
    ImageView favorite;
    @BindView(R.id.hm_im_map)
    ImageView map;
    @BindView(R.id.hm_im_top_back)
    ImageView topBack;
    @BindView(R.id.hm_im_top_dark)
    ImageView topDark;

    @BindView(R.id.hm_bt_left)
    ImageButton leftBtn;
    @BindView(R.id.hm_bt_right)
    ImageButton rightBtn;
    @BindView(R.id.hm_bt_view_all)
    Button allBtn;

    @BindView(R.id.hm_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.home_base_layout)
    ConstraintLayout layout;

    private CurrentSession session = CurrentSession.getInstance();
    private Calendar currentCalendar = Calendar.getInstance();

    private int currentIndex;
    private Route currentRoute;
    private List<String> nextList;
    private AppPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        prefs = new AppPrefs(getBaseContext());
        currentIndex = session.getCurrentIndex();
        currentRoute = session.getCurrentRoute();
        nextList = session.getNextList();
        final List<SuggestionModel> allSuggestions = new Suggestions(getBaseContext()).get();
        HomeAdapter adapter = new HomeAdapter(allSuggestions);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new HomeAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                showSuggestions(allSuggestions.get(position));
            }
        });


        layout.setOnTouchListener(new OnSwipeTouchListener(getBaseContext()) {
            @Override
            protected void onSwipeRight() {
                goLeft();
            }

            @Override
            protected void onSwipeLeft() {
                goRight();
            }

            @Override
            protected void onSwipeTop() {
            }

            @Override
            protected void onSwipeBottom() {
            }
        });

        leftBtn.setOnClickListener(new OnClickGuard(200) {
            @Override
            public void guard(View v) {
                goLeft();
            }
        });
        rightBtn.setOnClickListener(new OnClickGuard(200) {
            @Override
            public void guard(View v) {
                goRight();
            }
        });

        //When user clicks on favorite icon
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.setFavoriteRoute(currentRoute.getRouteNo());
                prefs.setFavoriteOrigin(currentRoute.getOrigin());
                prefs.setFavoriteDestination(currentRoute.getDestination());
                prefs.setFavoriteType(currentRoute.getType());
                General.makeShortToast(getBaseContext(), "Default route changed");
                favorite.setImageResource(R.drawable.icon_favorite);
            }
        });

        setUpItems();
    }

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.HOME;
    }

    /**
     * Opens {@link Transport} activity with whichever current route is on the home screen
     */
    @OnClick(R.id.hm_bt_view_all)
    public void showAllTrips() {
        Intent intent = new Intent(Home.this, Transport.class);
        startActivity(intent);
        animateTransition();
    }

    /**
     * Shows next route trip details
     *
     * @see CurrentSession
     */
    private void goRight() {
        currentIndex++;
        if (currentIndex == session.getAllRoutes().size()) {
            currentIndex = 0;
        }
        currentRoute = session.getAllRoutes().get(currentIndex);
        new setUpLayout().execute();
    }

    /**
     * Shows previous route trip details
     *
     * @see CurrentSession
     */

    private void goLeft() {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = session.getAllRoutes().size() - 1;
        }
        currentRoute = session.getAllRoutes().get(currentIndex);
        new setUpLayout().execute();
    }

    /**
     * Background operation to update all home page UI.
     * Basically changes all {@link CurrentSession} parameters and then calls {@link #setUpItems()}
     */
    private class setUpLayout extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            nextList = new TransportMethods().nextTransport(currentCalendar, currentRoute);
            session.setNextList(nextList);
            session.setCurrentIndex(currentIndex);
            session.setCurrentRoute(currentRoute);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setUpItems();
        }
    }

    /**
     * Main method to update UI elements when user navigates through buttons or touch inputs
     *
     * @see #goLeft()
     * @see #goRight()
     */
    private void setUpItems() {
        if (currentRoute != null) { //Sanity check for very slow devices
            currentPlace.setText(getString(R.string.home_current_place,
                    currentRoute.getOrigin().toUpperCase(), currentRoute.getDestination().toUpperCase()));
            nextTransport.setText(formatString(nextList.get(0)));
            transportSuggestion1.setText(formatString(nextList.get(1)));
            transportSuggestion2.setText(formatString(nextList.get(2)));
            transportSuggestion3.setText(formatString(nextList.get(3)));
            currentDate.setText(DateConverter.convertToString(currentCalendar, "dd MMM").toUpperCase());
            currentDay.setText(DateConverter.convertToString(currentCalendar, "EEE").toUpperCase());
            currentType.setText(currentRoute.getType().toString());
            aboveTransport.setText(getString(R.string.home_above_transport, currentRoute.getType().toString().toLowerCase()));
            currentSeats.setText(getString(R.string.home_seats, currentRoute.getType().getSeats()));

            // To Avoid memory out of bound error for lower APIs
            if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                backImage.setImageResource(getIcon());
                //Picasso.with(Home.this).load(getIcon()).placeholder(getIcon()).into(backImage);
            }
            if (prefs.getFavoriteRoute() == currentRoute.getRouteNo()) {
                favorite.setImageResource(R.drawable.icon_favorite);
                //Picasso.with(Home.this).load(R.drawable.icon_favorite).placeholder(R.drawable.icon_favorite).into(favorite);
            } else {
                favorite.setImageResource(R.drawable.icon_favorite_border);
                //Picasso.with(Home.this).load(R.drawable.icon_favorite_border).placeholder(R.drawable.icon_favorite_border).into(favorite);
            }
        } else {
            General.makeShortToast(getBaseContext(), "Something is wrong!");
        }
    }

    /**
     * Converts "HH:mm" timing format to "hh:mm a"
     *
     * @param string : Input string
     * @return : formatted string
     * @see DateConverter
     */
    private String formatString(String string) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar = DateConverter.convertToCalender(ConverterMode.DATE_FIRST, string);
        } catch (ParseException e) {
            Log.error("Unable to parse " + string);
        }
        return DateConverter.convertToString(calendar, "hh:mm a").toUpperCase();
    }

    /**
     * Selects icon/illustration of destination buildings on the home screen
     *
     * @return : Resource IDs (default "unknown")
     */
    private int getIcon() {
        switch (currentRoute.getDestination().toLowerCase().trim()) {
            case "ncbs":
                return R.drawable.ncbs;
            case "mandara":
                return R.drawable.mandara;
            case "iisc":
                return R.drawable.iisc;
            case "icts":
                return R.drawable.icts;
            default:
                return R.drawable.unknown;
        }
    }

    /**
     * Shows dialog with tips and suggestions based on user database and preferences
     * //TODO implement dynamic database and with preference matrix
     *
     * @param model : Suggestion Model
     * @see SuggestionModel
     */
    private void showSuggestions(final SuggestionModel model) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setIcon(model.getIcon())
                .setTitle(getString(R.string.did_you_know))
                .setMessage(model.getDetails())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        if (model.getAction() != 0) {
            dialog.setNegativeButton("Check Out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (takeAction(model.getAction())) {
                        animateTransition();
                    }
                }
            });
        }
        dialog.show();
    }

    boolean takeAction(int action) {
        switch (action) {
            case Suggestions.EDIT_TRANSPORT:
                startActivity(new Intent(Home.this, TransportEdit.class));
                break;
            case Suggestions.LOGIN:
                if (prefs.isUserLoggedIn()) {
                    startActivity(new Intent(Home.this, Dashboard.class));
                } else {
                    startActivity(new Intent(Home.this, Login.class));
                }
                break;
        }
        return true;
    }


}
