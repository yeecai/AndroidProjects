package com.exampe.nerdlauncher;

import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class NerdLaucherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return NerdLaucherFragment.newInstance();
    }

}
