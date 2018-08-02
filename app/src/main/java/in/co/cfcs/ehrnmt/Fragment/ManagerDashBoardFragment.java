package in.co.cfcs.ehrnmt.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import in.co.cfcs.ehrnmt.Manager.ManagerActivity.ManagerEmployeeDataDashboardActivity;
import in.co.cfcs.ehrnmt.Manager.ManagerActivity.ManagerFilterActivity;
import in.co.cfcs.ehrnmt.Manager.ManagerActivity.ManagerLeaveMangementActivity;
import in.co.cfcs.ehrnmt.Manager.ManagerActivity.ManagerProceedRequestActivity;
import in.co.cfcs.ehrnmt.Manager.ManagerActivity.ManagerReportDashboardActivity;
import in.co.cfcs.ehrnmt.Manager.ManagerActivity.ManagerRequestToApproveActivity;
import in.co.cfcs.ehrnmt.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManagerDashBoardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManagerDashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManagerDashBoardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public LinearLayout requaestApprovedLay, proceedLay, sixthTilesLay, thirdTilesLay, fourthLay, fivthLay;

    private OnFragmentInteractionListener mListener;

    public ManagerDashBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManagerDashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManagerDashBoardFragment newInstance(String param1, String param2) {
        ManagerDashBoardFragment fragment = new ManagerDashBoardFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_manager_dash_board, container, false);

        String strtext = getArguments().getString("Count");
        Log.e("checking count",strtext + " null");

        mListener.onFragmentInteraction(strtext);

        requaestApprovedLay = (LinearLayout)rootView. findViewById(R.id.requesttoapprovetxt);
        proceedLay = (LinearLayout)rootView. findViewById(R.id.proceedlay);
        sixthTilesLay = (LinearLayout)rootView. findViewById(R.id.sixthtiles);
        thirdTilesLay = (LinearLayout)rootView. findViewById(R.id.thirdTiles);
        fourthLay = (LinearLayout) rootView.findViewById(R.id.fourthtile);
        fivthLay = (LinearLayout) rootView.findViewById(R.id.fivthtiles);

        requaestApprovedLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(getActivity(),ManagerRequestToApproveActivity.class);
                ik.putExtra("BackValue","1");
                startActivity(ik);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        proceedLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(getActivity(),ManagerProceedRequestActivity.class);
                startActivity(ik);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

            }
        });

        sixthTilesLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(getActivity(),ManagerFilterActivity.class);
                ik.putExtra("CheckingTheActivity","Asset Details");
                startActivity(ik);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        thirdTilesLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent ik = new Intent(getActivity(),ManagerLeaveMangementActivity.class);
                startActivity(ik);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        fourthLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(getActivity(),ManagerReportDashboardActivity.class);
                startActivity(ik);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        fivthLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(getActivity(),ManagerEmployeeDataDashboardActivity.class);
                startActivity(ik);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
   /* public void onButtonPressed(Uri uri) {
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
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
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
        void onFragmentInteraction(String count);
    }
}
