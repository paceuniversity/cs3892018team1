package com.example.yevgeniyshatrovskiy.steepr.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.yevgeniyshatrovskiy.steepr.Objects.Recipe;
import com.example.yevgeniyshatrovskiy.steepr.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button submitButton;
    private ArrayList<String> categories;

    private OnFragmentInteractionListener mListener;

    public CustomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomFragment newInstance(String param1, String param2) {
        CustomFragment fragment = new CustomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            categories = getArguments().getStringArrayList("categories");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_custom, container, false);

        final Spinner spinner = view.findViewById(R.id.spinner);
        final Spinner tempSpinner = view.findViewById(R.id.tempSpinner);
        ArrayList<String> tempList = new ArrayList<>();
        tempList.add("F\u00B0");
        tempList.add("C\u00B0");


        ArrayAdapter<String> tempAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, tempList);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, categories);

        spinner.setAdapter(dataAdapter);
        tempSpinner.setAdapter(tempAdapter);


        //Input field
        final EditText nameInput = view.findViewById(R.id.nameInput);
        final EditText tempInput = view.findViewById(R.id.tempInput);
        final EditText minInput = view.findViewById(R.id.minInput);
        final EditText secInput = view.findViewById(R.id.secInput);
        final EditText desInput = view.findViewById(R.id.desInput);


        //Submit Form
        submitButton = view.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction().remove();
                try{

                    Recipe newRecipe = new Recipe();

                    newRecipe.setCategory("Favorite");
                    newRecipe.setName(nameInput.getText().toString());
                    newRecipe.setTemperature(Integer.parseInt(tempInput.getText().toString()));
                    int steepTime = ((Integer.parseInt(minInput.getText().toString())) * 60) +
                            (Integer.parseInt(secInput.getText().toString()));
                    newRecipe.setSecondsToSteep(steepTime);
                    newRecipe.setDescription(desInput.getText().toString());
                    newRecipe = determineImage(spinner.getSelectedItem().toString(),newRecipe);
                    newRecipe.setChineseCategory("茶");
                    newRecipe.setChineseName("茶");

                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference myRef = database.child("users");
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    myRef.child(firebaseUser.getUid()).child("Favorites").child(newRecipe.getName()).setValue(newRecipe);
                    getActivity().onBackPressed();

                }catch (Exception e){
                    Toast.makeText(getActivity(), "Please Fill All Fields",
                            Toast.LENGTH_SHORT).show();
//                    getActivity().onBackPressed();
                }



            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public Recipe determineImage(String name, Recipe rep){
        Recipe recipe = new Recipe();
        recipe = rep;
        switch (name){
            case "Black":
                rep.setBackGroundImage("herbaltea");
                rep.setBackGroundColor("#ffffff");
                rep.setTextColor("#4A453F");
                return rep;
            case "White":
                rep.setBackGroundImage("whitetea");
                rep.setBackGroundColor("#ffffff");
                rep.setTextColor("#AB9D85");
                return rep;
            case "Herbal":
                rep.setBackGroundImage("herbaltea");
                rep.setBackGroundColor("#ffffff");
                rep.setTextColor("#008000");
                return rep;
            case "Oolong":
                rep.setBackGroundImage("oolongtea");
                rep.setBackGroundColor("#ffffff");
                rep.setTextColor("#BD8300");
                return rep;
            case "Green":
                rep.setBackGroundImage("greentea");
                rep.setBackGroundColor("#f6F6F6");
                rep.setTextColor("#008000");
                return rep;
            case "Yellow":
                rep.setBackGroundImage("yellowtea");
                rep.setBackGroundColor("#ffffff");
                rep.setTextColor("#BEB71E");
                return rep;
            case "Matcha":
                rep.setBackGroundImage("greentea");
                rep.setBackGroundColor("#ffffff");
                rep.setTextColor("#008000");
                return rep;
            case "Pu'er":
                rep.setBackGroundImage("puerhtea");
                rep.setBackGroundColor("#ffffff");
                rep.setTextColor("#352045");
                return rep;
            default:
                rep.setBackGroundImage("greentea");
                rep.setBackGroundColor("#ffffff");
                rep.setTextColor("#008000");
                return rep;
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
