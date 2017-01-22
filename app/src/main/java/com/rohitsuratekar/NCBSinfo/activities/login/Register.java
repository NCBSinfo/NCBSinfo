package com.rohitsuratekar.NCBSinfo.activities.login;

import android.os.Bundle;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.secretbiology.helpers.general.views.InputView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends BaseActivity {

    @BindView(R.id.reg_in_name)
    InputView name;
    @BindView(R.id.reg_in_email)
    InputView email;
    @BindView(R.id.reg_in_pass)
    InputView pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.reg_btn_submit)
    public void submitDetails() {

    }


    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.REGISTER;
    }
}
