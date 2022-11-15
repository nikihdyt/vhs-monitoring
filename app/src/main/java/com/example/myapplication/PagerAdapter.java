package com.example.myapplication;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new Fragment1();
            case 1: return new Fragment2();
            case 2: return new Fragment3();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

//
//    AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(MainActivity.this);
//        myAlertBuilder.setTitle("Login");
//                myAlertBuilder.setMessage("Please insert your credentials");
//
//                myAlertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//public void onClick(DialogInterface dialog, int which) {
//        Toast.makeText(getApplicationContext(), R.string.pressed_ok, Toast.LENGTH_SHORT).show();
//        }
//        });
//
//        myAlertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//public void onClick(DialogInterface dialog, int which) {
//        Toast.makeText(getApplicationContext(), R.string.pressed_cancel, Toast.LENGTH_SHORT).show();
//        }
//        });
//
//        myAlertBuilder.show();