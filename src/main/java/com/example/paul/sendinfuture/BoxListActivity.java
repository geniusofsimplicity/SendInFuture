package com.example.paul.sendinfuture;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;


public class BoxListActivity extends SingleFragmentActivity
implements BoxListFragment.Callbacks{


    @Override
    protected Fragment createFragment() {
        return BoxListFragment.newInstance();
    }

    @Override
    public void onBoxSelected(Box box, ActivityOptionsCompat options) {
        Intent intent = BoxActivity.newIntent(this, box.getId());
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
}
