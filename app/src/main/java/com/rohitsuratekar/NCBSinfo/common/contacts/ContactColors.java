package com.rohitsuratekar.NCBSinfo.common.contacts;


import android.content.Context;

import com.rohitsuratekar.NCBSinfo.R;

public class ContactColors {

    int colorID;
    String letter;
    Context context;

    public ContactColors(Context context, String letter) {
        this.context = context;
        this.letter = letter;
        this.colorID = getColor();

    }

    private int getColor() {
        switch (letter) {
            case "a":
                return context.getResources().getColor(R.color.a);
            case "b":
                return context.getResources().getColor(R.color.b);
            case "c":
                return context.getResources().getColor(R.color.c);
            case "d":
                return context.getResources().getColor(R.color.d);
            case "e":
                return context.getResources().getColor(R.color.e);
            case "f":
                return context.getResources().getColor(R.color.f);
            case "g":
                return context.getResources().getColor(R.color.g);
            case "h":
                return context.getResources().getColor(R.color.h);
            case "i":
                return context.getResources().getColor(R.color.i);
            case "j":
                return context.getResources().getColor(R.color.j);
            case "k":
                return context.getResources().getColor(R.color.k);
            case "l":
                return context.getResources().getColor(R.color.l);
            case "m":
                return context.getResources().getColor(R.color.m);
            case "n":
                return context.getResources().getColor(R.color.n);
            case "o":
                return context.getResources().getColor(R.color.o);
            case "p":
                return context.getResources().getColor(R.color.p);
            case "q":
                return context.getResources().getColor(R.color.q);
            case "r":
                return context.getResources().getColor(R.color.r);
            case "s":
                return context.getResources().getColor(R.color.s);
            case "t":
                return context.getResources().getColor(R.color.t);
            case "u":
                return context.getResources().getColor(R.color.u);
            case "v":
                return context.getResources().getColor(R.color.v);
            case "w":
                return context.getResources().getColor(R.color.w);
            case "x":
                return context.getResources().getColor(R.color.x);
            case "y":
                return context.getResources().getColor(R.color.y);
            case "z":
                return context.getResources().getColor(R.color.z);
            default:
                return context.getResources().getColor(R.color.r);
        }
    }
}
