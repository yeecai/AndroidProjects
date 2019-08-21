package com.example.draganddrawactivity;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;

public class DragAndDrawActivity extends SingleFragmentActivity {

   // ​​p​r​o​t​e​c​t​e​d​ ​P​a​r​c​e​l​a​b​l​e​ ​o​n​S​a​v​e​I​n​s​t​a​n​c​e​S​t​a​t​e​(​);
  //  ​p​r​o​t​e​c​t​e​d​ ​v​o​i​d​ ​o​n​R​e​s​t​o​r​e​I​n​s​t​a​n​c​e​S​t​a​t​e​(​P​a​r​c​e​l​a​b​l​e​ ​s​t​a​t​e​)​
    @Override
    protected Fragment createFragment() {
        return DragAndDrawFragment.newInstance();
    }

       // Parcelable state = onSaveInstanceState();
    /*@Override
    protected Parcelable onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }*/
}
