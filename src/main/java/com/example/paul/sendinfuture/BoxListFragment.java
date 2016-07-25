package com.example.paul.sendinfuture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Paul on 10.07.2016.
 */
public class BoxListFragment extends Fragment {

    private RecyclerView mBoxRecyclerView;
    private BoxAdapter mAdapter;
    private Callbacks mCallbacks;




    /*
    * Required interface for hosting activities.
    * */

    public interface Callbacks{
        void onBoxSelected(Box box, ActivityOptionsCompat options);
    }


    public static BoxListFragment newInstance(){
        return new BoxListFragment();
    }

    private class BoxHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private EditText mBoxTitle;
        private Button mDeliveryDateButton;
        private Button mDeliveryTimeButton;
        private Box mBox;
        private CardView mCardView;

        public BoxHolder(final View itemView) {
            super(itemView);


            mCardView = (CardView)itemView;
            mBoxTitle = (EditText)itemView.findViewById(R.id.box_title);
            mBoxTitle.setOnClickListener(this);
            View.OnTouchListener otl = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            };
            mBoxTitle.setOnTouchListener(otl);
            mBoxTitle.setInputType(InputType.TYPE_NULL);
            mDeliveryDateButton = (Button)itemView.findViewById(R.id.button_set_date);
            mDeliveryDateButton.setOnClickListener(this);
            mDeliveryTimeButton = (Button)itemView.findViewById(R.id.button_set_time);
            mDeliveryTimeButton.setOnClickListener(this);

            itemView.setOnClickListener(this);
        }

        public void bindBox(Box box){
            mBox = box;
            String title = mBox.getTitle();
            if(title != null){
                mBoxTitle.setText(title);
            }else{
                mBoxTitle.setHint("");
            }


            mDeliveryDateButton.setText(mBox.getDateString());
            mDeliveryTimeButton.setText(mBox.getTimeString());
            mCardView.setBackground(CardViewUtils.getCardBackground(getActivity(), mBox.getCalendarInMillis(), mBox.isInFuture()));
            boolean isEnabled = CardViewUtils.isCardEnabled(mBox.getCalendarInMillis(), mBox.isInFuture());
            mCardView.setEnabled(isEnabled);
            mBoxTitle.setEnabled(isEnabled);
            mDeliveryDateButton.setEnabled(isEnabled);
            mDeliveryTimeButton.setEnabled(isEnabled);
        }

        @Override
        public void onClick(View view) {
            View view1 = mCardView;
//            Pair<View, String> holderPair = Pair.create(view1, getResources().getString(R.string.cardview_box_transition_name));
            String transitionName = getResources().getString(R.string.cardview_box_transition_name);
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(getActivity(), view1, transitionName);

            mCallbacks.onBoxSelected(mBox, options);

//            Intent intent = new Intent(getActivity(), BoxActivity.class);
//            intent.putExtra("testBoxId", mBox.getId());

        }
    }

    private class BoxAdapter extends RecyclerView.Adapter<BoxHolder>{

        private List<Box> mBoxes;

        public BoxAdapter(List<Box> boxes){
            mBoxes = boxes;
        }

        @Override
        public BoxHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.cardview_title_and_date, parent, false);
            return new BoxHolder(view);
        }

        @Override
        public void onBindViewHolder(BoxHolder holder, int position) {
            Box box = mBoxes.get(position);
            holder.bindBox(box);

        }

        @Override
        public int getItemCount() {
            return mBoxes.size();
        }

        public void setBoxes(List<Box> boxes){
            mBoxes = boxes;
        }
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume(){
        super.onResume();

        updateUI();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedStateInstance){
        View view = inflater.inflate(R.layout.fragment_box_list, container, false);

        mBoxRecyclerView = (RecyclerView)view.findViewById(R.id.box_recycler_view);
        mBoxRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_box_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_new_box:
                Box box = new Box();
                BoxOffice.get(getActivity()).addBox(box);
                updateUI();
                return true;
            case R.id.menu_item_remove_delivered_boxes:
                BoxOffice.get(getActivity()).removeDeliveredBoxes();
                updateUI();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }


    private void updateUI(){
        BoxOffice boxOffice = BoxOffice.get(getActivity());
        List<Box> boxes = boxOffice.getBoxes();

        if(mAdapter == null){
            mAdapter = new BoxAdapter(boxes);
            mBoxRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setBoxes(boxes);
            mAdapter.notifyDataSetChanged();
        }
    }
}
