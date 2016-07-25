package com.example.paul.sendinfuture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by Paul on 10.07.2016.
 */
public class BoxActivity extends SingleFragmentActivity {

    private static final String EXTRA_BOX_ID = "com.example.paul.sendInFuture.boxId";

    public static Intent newIntent(Context packageContext, UUID boxId) {
        Intent intent = new Intent(packageContext, BoxActivity.class);
        intent.putExtra(EXTRA_BOX_ID, boxId);
        return intent;
    }

    @Override
    protected Fragment createFragment(){
        UUID boxId = (UUID)getIntent().getSerializableExtra(EXTRA_BOX_ID);
        Fragment boxFragment = BoxFragment.newInstance(boxId);
        return boxFragment;
    }
}
