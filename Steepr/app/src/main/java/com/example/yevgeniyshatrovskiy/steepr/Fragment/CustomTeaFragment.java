package com.example.yevgeniyshatrovskiy.steepr.Fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yevgeniyshatrovskiy.steepr.Activities.MainActivity;
import com.example.yevgeniyshatrovskiy.steepr.Objects.Recipe;
import com.example.yevgeniyshatrovskiy.steepr.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link CustomTeaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomTeaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomTeaFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button mPlusOneButton;
    private String userID;


    private OnFragmentInteractionListener mListener;

    public CustomTeaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomTeaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomTeaFragment newInstance(String param1, String param2) {
        CustomTeaFragment fragment = new CustomTeaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static CustomTeaFragment newInstance() {
        CustomTeaFragment fragment = new CustomTeaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }



    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int width = metrics.widthPixels;
//        int height = metrics.heightPixels;
//
//        dialog.getWindow().setLayout((6 * width)/7, (4 * height)/5);
        dialog.setTitle("Custom");
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userID = getArguments().getString("userID");
        if(userID != null)
            Log.v("FRAG", userID);
        else
            Log.v("FRAG", "ITS NULL");


        // Inflate the layout for this fragment

//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int width = metrics.widthPixels;
//        int height = metrics.heightPixels;
//
//        Dialog yourDialog = getDialog();
//        yourDialog.getWindow().setLayout((6 * width)/7, (4 * height)/5);

        View view = inflater.inflate(R.layout.fragment_custom_tea, container, false);


        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //Find the +1 button
        mPlusOneButton = view.findViewById(R.id.plus_one_button);
        mPlusOneButton.setHint("Test");
        mPlusOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                DatabaseReference myRef = database.child("users");
                Recipe rep = new Recipe("Awesome", "Weird", 60, null, "greentea","Favorite", "#ffffff","greentea",
                        "#BD8300","茶","茶");

                Recipe rep2 = new Recipe("Awesome2", "Weird", 60, null,
                        "greentea","Favorite", "#ffffff","greentea",
                        "#BD8300","茶","茶");

                myRef.child(userID).child("Favorites").child(rep.getName()).setValue(rep);
                myRef.child(userID).child("Favorites").child(rep2.getName()).setValue(rep2);
//                myRef.child(userID).child("Favorites").child(rep.getName()).removeValue();
                ((MainActivity)getActivity()).restartListener();
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
