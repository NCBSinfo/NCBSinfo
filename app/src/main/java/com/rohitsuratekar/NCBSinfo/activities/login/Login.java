package com.rohitsuratekar.NCBSinfo.activities.login;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

public class Login extends BaseActivity {
    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.LOGIN;
    }

    ImageButton nextButton, previousButton;
    LinearLayout revealView, passwordLayout, userLayout;
    Animation alphaAnimation;
    float pixelDensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pixelDensity = getResources().getDisplayMetrics().density;

        userLayout = (LinearLayout) findViewById(R.id.login_userLayout);
        nextButton = (ImageButton) findViewById(R.id.login_nextButton);
        previousButton = (ImageButton) findViewById(R.id.login_goBackButton);
        revealView = (LinearLayout) findViewById(R.id.linearView);
        passwordLayout = (LinearLayout) findViewById(R.id.login_passwordLayout);
        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    gotoPassWithReveal();
                }
                else {
                    passwordLayout.setVisibility(View.VISIBLE);
                    revealView.setVisibility(View.VISIBLE);
                    userLayout.setVisibility(View.GONE);
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    gotoBackWithReveal();
                }
                else {
                    passwordLayout.setVisibility(View.GONE);
                    revealView.setVisibility(View.GONE);
                    userLayout.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void gotoPassWithReveal() {

        int x = userLayout.getRight();
        int y = userLayout.getBottom();
        x -= ((28 * pixelDensity) + (16 * pixelDensity));

        int hypotenuse = (int) Math.hypot(userLayout.getWidth(), userLayout.getHeight());

        revealView.setBackgroundColor(getResources().getColor(R.color.login_input_activated));

        FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams)
                revealView.getLayoutParams();
        parameters.height = userLayout.getHeight();
        revealView.setLayoutParams(parameters);

        Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, 0, hypotenuse);
        anim.setDuration(400);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                previousButton.setVisibility(View.VISIBLE);
                //  previousButton.startAnimation(alphaAnimation);


            }

            @Override
            public void onAnimationEnd(Animator animator) {
                passwordLayout.setVisibility(View.VISIBLE);
                passwordLayout.startAnimation(alphaAnimation);
                userLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        revealView.setVisibility(View.VISIBLE);

        anim.start();


    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void gotoBackWithReveal() {

        int x = userLayout.getRight();
        int y = userLayout.getBottom();
        x -= ((28 * pixelDensity) + (16 * pixelDensity));

        int hypotenuse = (int) Math.hypot(userLayout.getWidth(), userLayout.getHeight());

        revealView.setBackgroundColor(getResources().getColor(R.color.login_input_activated));

        previousButton.setVisibility(View.GONE);

        Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, hypotenuse, 0);
        anim.setDuration(400);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                userLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                revealView.setVisibility(View.GONE);
                passwordLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        anim.start();

    }


}
