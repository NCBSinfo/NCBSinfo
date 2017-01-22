package com.rohitsuratekar.NCBSinfo.activities.contact;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.database.models.ContactModel;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        allContacts.add(new ContactModel("Reception", "080-2366-6001", R.drawable.icon_favorite));
        allContacts.add(new ContactModel("Medical Centre", "080-2366-6450", R.drawable.icon_authenticated));
        allContacts.add(new ContactModel("Substation", "080-2366-6425", R.drawable.icon_contacts));
        allContacts.add(new ContactModel("Reception 2", "080-2366-6018", R.drawable.icon_favorite));

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

    private void openSheet(int position) {

        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        int height = size.y;
        bottomSheet.setPeekSheetTranslation(height / 2);
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.contacts_bottom_sheet, bottomSheet, false));
        TextView bs_title = (TextView) findViewById(R.id.con_bs_title);
        TextView bs_number1 = (TextView) findViewById(R.id.con_bis_number1);
        ImageView bs_icon = (ImageView) findViewById(R.id.con_bs_icon);


        bs_icon.setImageResource(allContacts.get(position).getIcon());
        bs_title.setText(allContacts.get(position).getName());
        bs_number1.setText(allContacts.get(position).getNumber());

        bottomSheet.addOnSheetStateChangeListener(new BottomSheetLayout.OnSheetStateChangeListener() {
            @Override
            public void onSheetStateChanged(BottomSheetLayout.State state) {
                if (state.equals(BottomSheetLayout.State.EXPANDED)) {
                    bottomSheet.peekSheet();
                }
            }
        });
    }

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.CONTACTS;
    }
}
