package com.example.paul.sendinfuture;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.zip.Inflater;

/**
 * Created by Paul on 11.07.2016.
 */
public class BoxFragment extends Fragment {

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogDate";

    private static final String TAG = "BoxFragment";

    private static final int MESSAGE_OPEN_BOX = 0;

    private EditText mTitleField;
    private EditText mLetterEditText;
    private Button mDateButton;
    private Button mTimeButton;
    private ImageButton mTextButton;
    private Button mVideoButton;
    private Button mPictureButton;
    private ImageButton mSealButton;
    private ImageButton mPutIntoBoxButton;
    private EditText mBoxNote;
    private Box mBox;
    private CardView mCardView;

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;


    private static final String ARG_BOX_ID = "boxId";


    public static BoxFragment newInstance(UUID boxId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_BOX_ID, boxId);

        BoxFragment fragment = new BoxFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID boxId = (UUID)getArguments().getSerializable(ARG_BOX_ID);
        mBox = BoxOffice.get(getActivity()).getBox(boxId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_box, container, false);

//        RelativeLayout rl = (RelativeLayout)view.findViewById(R.id.fragment_box_rl);
//        final BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//        bitmapOptions.inTargetDensity = 1;
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.mechanism_pattern, bitmapOptions);
//        bmp.setDensity(Bitmap.DENSITY_NONE);
//        bmp.createBitmap(bmp, 0, 0, rl.getWidth(), bmp.getHeight());

        mCardView = (CardView)view.findViewById(R.id.fragment_box_card_view);
        mCardView.setBackground(CardViewUtils.getCardBackground(getActivity(), mBox.getCalendarInMillis(), mBox.isInFuture()));

        mTitleField = (EditText)view.findViewById(R.id.box_title);
        mTitleField.setText(mBox.getTitle());
        mTitleField.setEnabled(true);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int befor, int count) {
                mBox.setTitle(s.toString());
                updateBox();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mBoxNote = (EditText)view.findViewById(R.id.fragment_box_editText);
//        mChestView = (ImageView)view.findViewById(R.id.imageView);

        mDateButton = (Button)view.findViewById(R.id.button_set_date);
        mDateButton.setEnabled(true);
        if(mBox.getDate() == null) {
            mDateButton.setText(getResources().getString(R.string.button_set_date));
        }else{
            mDateButton.setText(mBox.getDateString());
        }
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mBox.getDate());
                dialog.setTargetFragment(BoxFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mTimeButton = (Button)view.findViewById(R.id.button_set_time);
        mTimeButton.setEnabled(true);
        if(mBox.getTimeString() == ""){
            mTimeButton.setText(getResources().getString(R.string.button_set_time));
        }else{
            mTimeButton.setText(mBox.getTimeString());
        }
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mBox.getHour(), mBox.getMinute());
                dialog.setTargetFragment(BoxFragment.this, REQUEST_TIME);
                dialog.show(fm, DIALOG_TIME);
            }
        });

        mLetterEditText = (EditText)view.findViewById(R.id.fragment_box_editText);

//        mTextButton = (Button)view.findViewById(R.id.button_add_text);
        mTextButton = (ImageButton)view.findViewById(R.id.button_add_text);
        mTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                performZoomViewAnimation(mTextButton, mLetterEditText);
                mLetterEditText.setText(mBox.getMessage());
//                performRevealAnimation(mBoxNote);
                mPutIntoBoxButton.setVisibility(View.VISIBLE);
                mSealButton.setVisibility(View.INVISIBLE);

            }
        });

        mSealButton = (ImageButton)view.findViewById(R.id.fragment_box_button_seal);
        mSealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBox.getTitle() == null){
                    CharSequence toastText = getActivity().getResources().getString(R.string.button_seal_warning);
                    Toast toast = Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

//                mPutIntoBoxButton.setVisibility(View.VISIBLE);
//                mSealButton.setVisibility(View.GONE);
                sealBox();
            }
        });

        mPutIntoBoxButton = (ImageButton)view.findViewById(R.id.fragment_box_button_add_to_box);
        mPutIntoBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPutIntoBoxButton.setVisibility(View.INVISIBLE);
                mSealButton.setVisibility(View.VISIBLE);
                animatePutViewIntoBox(mBoxNote);
                saveTextMessage();
            }
        });

        mShortAnimationDuration = (int) Math.round(getResources().getInteger(android.R.integer.config_longAnimTime) * 1.3);

        return view;
    }

    private void sealBox(){
        mBox.setIsInFuture(true);
        updateBox();
        getActivity().finish();
    }


    private void saveTextMessage(){
        String message = mLetterEditText.getText().toString();
        mBox.setMessage(message);
        updateBox();
    }

    private void performZoomViewAnimation(final View startView, final View finalView){
        if(mCurrentAnimator != null){
            mCurrentAnimator.cancel();
        }

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        startView.getGlobalVisibleRect(startBounds);

        getActivity().findViewById(R.id.fragment_box_chest_backgroung).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()){
            startScale = (float) startBounds.height() / finalBounds.height();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }else{
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        finalView.setVisibility(View.VISIBLE);

        finalView.setPivotX(finalView.getRight() / 2);
        finalView.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(finalView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(finalView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(finalView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(finalView, View.SCALE_Y,
                        startScale, 1f));

        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });

        set.start();
        mCurrentAnimator = set;

    }

    private void animatePutViewIntoBox(final View view){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int x = (view.getLeft() + view.getRight()) / 2;
            int y = view.getHeight() / 3;

            int initialRadius = calcDiagonal(view.getWidth() / 2, 2 * view.getHeight() / 3);

            Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, initialRadius, 70);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
//                    mChestView.setVisibility(View.VISIBLE);
                }
            });

            anim.setDuration(1000);
            anim.start();
        }else{
            view.setVisibility(View.VISIBLE);
        }

    }

    private void performRevealAnimation(final View view){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int x = view.getLeft();
            int y = view.getTop();

            int finalRadius = calcDiagonal(view.getWidth(), view.getHeight());
            Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, 0, finalRadius);
            anim.setDuration(1000);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    view.setVisibility(View.VISIBLE);
                }
            });
            anim.start();
        }else{
            view.setVisibility(View.VISIBLE);
        }
    }

    private int calcDiagonal(int x, int y){
        return (int) Math.round(Math.sqrt(x * x + y * y));
    }

    private void performHideAnimation(final View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int x = view.getRight();
            int y = view.getBottom();

            int initialRadius = calcDiagonal(view.getWidth(), view.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, initialRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            });

            anim.setDuration(1000);
            anim.start();
        }else{
            view.setVisibility(View.INVISIBLE);
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            GregorianCalendar calendar = (GregorianCalendar)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mBox.setDate(calendar);
            updateDate();
            updateBox();
        }
        if(requestCode == REQUEST_TIME){
            GregorianCalendar time = (GregorianCalendar)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mBox.setTime(time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
            updateTime();
            updateBox();
        }
    }

    private void updateDate(){
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());
        mDateButton.setText(dateFormat.format(mBox.getDate()));
    }

    private void updateTime(){
        mTimeButton.setText(mBox.getTimeString());
    }

    private void updateBox(){
        BoxOffice.get(getActivity()).updateBox(mBox);
    }


}
