package com.example.paul.sendinfuture;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Paul on 24.07.2016.
 */
public class CardViewUtils {

    public enum BoxState{
        SEALED, DELIVERED, INDEVELOPMENT;
    }

    public static Drawable getCardBackground(Context context, long deliveryDateTime, boolean isInFuture){
        switch (getBoxState(deliveryDateTime, isInFuture)){
            case INDEVELOPMENT:
                return context.getResources().getDrawable(R.drawable.wood_background);
            case SEALED:
                return context.getResources().getDrawable(R.drawable.icy_pattern);
            case DELIVERED:
                return context.getResources().getDrawable(R.drawable.green_flowers_pattern);
            default:
                return context.getResources().getDrawable(R.drawable.green_flowers_pattern);
        }
    }

    private static BoxState getBoxState(long deliveryDateTime, boolean isInFuture){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        long now = calendar.getTimeInMillis();
        if(deliveryDateTime > now){
            if(isInFuture){
                return BoxState.SEALED;
            }else {
                return BoxState.INDEVELOPMENT;
            }
        }else{
            if(isInFuture){
                return BoxState.DELIVERED;
            }else{
                return BoxState.INDEVELOPMENT;
            }
        }
    }

    public static boolean isCardEnabled(long deliveryDateTime, boolean isInFuture){
        switch (getBoxState(deliveryDateTime, isInFuture)){
            case INDEVELOPMENT:
                return true;
            case SEALED:
                return false;
            case DELIVERED:
                return true;
            default:
                return true;
        }
    }
}
