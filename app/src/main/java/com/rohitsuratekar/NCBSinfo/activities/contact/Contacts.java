package com.rohitsuratekar.NCBSinfo.activities.contact;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.Helper;
import com.rohitsuratekar.NCBSinfo.database.models.ContactModel;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.secretbiology.helpers.general.General;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Contacts extends BaseActivity {

    @BindView(R.id.con_txt_main_text)
    TextView mainText;
    @BindView(R.id.con_txt_sub_text)
    TextView subText;

    @BindView(R.id.contact_recycler)
    RecyclerView recycler;
    @BindView(R.id.con_bottom_sheet)
    BottomSheetLayout bottomSheet;

    private ContactAdapter adapter;
    private List<ContactModel> allContacts = new ArrayList<>();

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private ContactModel currentContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        allContacts = new ContactList().getFront();

        adapter = new ContactAdapter(allContacts);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getBaseContext());
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new ContactAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                openSheet(position);
            }
        });
    }

    private void openSheet(final int position) {
        final ContactModel entry = allContacts.get(position);
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        int height = size.y;
        // bottomSheet.setPeekSheetTranslation(height / 2);
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.contacts_bottom_sheet, bottomSheet, false));
        TextView bs_title = (TextView) findViewById(R.id.con_bs_title);
        TextView bs_number = (TextView) findViewById(R.id.con_bs_number);
        TextView bs_email = (TextView) findViewById(R.id.con_bs_email);
        ImageView bs_icon = (ImageView) findViewById(R.id.con_bs_icon);
        ImageView bs_back = (ImageView) findViewById(R.id.con_bs_back);
        ImageView bs_copy = (ImageView) findViewById(R.id.con_bs_copy);
        ImageView bs_call = (ImageView) findViewById(R.id.con_bs_call);

        bs_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyText(entry.getEmail());
            }
        });

        bs_number.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyText(entry.getNumber());
                return true;
            }
        });


        bs_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ActivityCompat.checkSelfPermission
                        (Contacts.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    currentContact = entry;
                    ActivityCompat.requestPermissions(Contacts.this,
                            new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);

                } else {

                    String uri = "tel:" + entry.getNumber().trim();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }

            }
        });


        bottomSheet.addOnSheetStateChangeListener(new BottomSheetLayout.OnSheetStateChangeListener() {
            @Override
            public void onSheetStateChanged(BottomSheetLayout.State state) {
                if (state.equals(BottomSheetLayout.State.EXPANDED)) {
                    new Helper().changeDarkColor(Contacts.this, entry.getDarkColor());
                    toolbar.setBackgroundColor(General.getColor(getBaseContext(), entry.getColor()));
                } else {
                    new Helper().changeDarkColor(Contacts.this, R.color.colorPrimaryDark);
                    toolbar.setBackgroundColor(General.getColor(getBaseContext(), R.color.colorPrimary));
                }
            }
        });


        if (allContacts.get(position).getColor() > 0) {
            int col = entry.getColor();
            bs_back.setImageResource(col);
            bs_icon.setColorFilter(General.getColor(getBaseContext(), entry.getDarkColor()));

        } else {
            bs_back.setImageResource(General.getColor(getBaseContext(), R.color.colorPrimary));
            bs_icon.setColorFilter(General.getColor(getBaseContext(), R.color.colorPrimaryDark));
        }


        bs_email.setText(entry.getEmail());
        bs_icon.setImageResource(entry.getIcon());
        bs_title.setText(entry.getName());
        bs_number.setText(entry.getNumber());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String uri = "tel:" + currentContact.getNumber().trim();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);

                } else {
                    General.makeLongToast(getBaseContext(), "Unable to make call because of permission issue");
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.CONTACTS;
    }

    private void copyText(String s) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(s);
            General.makeShortToast(getBaseContext(), s + " Copied to clipboard");
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("NCBSinfo copied text", s);
            clipboard.setPrimaryClip(clip);
            General.makeShortToast(getBaseContext(), s + " Copied to clipboard");
        }
    }
}
