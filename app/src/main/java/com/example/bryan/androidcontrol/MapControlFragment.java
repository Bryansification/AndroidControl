package com.example.bryan.androidcontrol;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by Bryan on 2/2/2018.
 */

public class MapControlFragment extends Fragment{
    private static Button manualUpdateBtn;
    private static ToggleButton autoUpdateToggleBtn;

    private static Switch waypointToggleBtn;
    private static TextView waypointTextView;

    private static Button sendWaypointBtn;

    private static Switch robotToggleBtn;
    private static TextView robotTextView;

    private OnFragmentInteractionListener onFragmentInteractionListener;

    public MapControlFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mapcontrol, container, false);

        manualUpdateBtn = view.findViewById(R.id.manualUpdateBtn);
        manualUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manualUpdate();
            }
        });

        autoUpdateToggleBtn = view.findViewById(R.id.autoUpdateToggleBtn);
        autoUpdateToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggleAutoUpdate(compoundButton, b);
            }
        });

        waypointToggleBtn = view.findViewById(R.id.waypointToggleBtn);
        waypointToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                toggleWaypoint(compoundButton, isChecked);
                robotToggleBtn.setChecked(false);

            }
        });

        waypointTextView = view.findViewById(R.id.waypointTextView);

        sendWaypointBtn = view.findViewById(R.id.sendWaypointBtn);
        sendWaypointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendWaypoint();
            }
        });

        robotToggleBtn = view.findViewById(R.id.robotToggleBtn);
        robotToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                toggleRobot(compoundButton, isChecked);
                waypointToggleBtn.setChecked(false);
            }
        });

        robotTextView = view.findViewById(R.id.robotTextView);

        return view;
    }

    public void setWaypointTextView(int[] waypoint) {
        if (waypoint[0] < 0) {
            waypointTextView.setText("x:-- y:--");
        } else {
            waypointTextView.setText("x:" + waypoint[0] + " y:" + waypoint[1]);
        }
    }

    public void setRobotTextView(int[] robot) {
        if (robot[0] < 0) {
            robotTextView.setText("x:-- y:--");
        } else {
            robotTextView.setText("x:" + robot[0] + " y:" + robot[1]);
        }

    }

    public void manualUpdate() {
        if (onFragmentInteractionListener != null) {
            onFragmentInteractionListener.manualUpdate();
        }
    }

    public void toggleAutoUpdate(CompoundButton compoundButton, boolean isChecked) {
        if (onFragmentInteractionListener != null) {
            onFragmentInteractionListener.toggleAutoUpdate(isChecked);
        }
    }

    public void toggleWaypoint(CompoundButton compoundButton, boolean isChecked) {
        if (onFragmentInteractionListener != null) {
            onFragmentInteractionListener.toggleWaypoint(isChecked);
        }
    }

    public void sendWaypoint() {
        if (onFragmentInteractionListener != null) {
            onFragmentInteractionListener.sendWaypoint();
        }
    }

    public void toggleRobot(CompoundButton compoundButton, boolean isChecked) {
        if (onFragmentInteractionListener != null) {
            onFragmentInteractionListener.toggleRobot(isChecked);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;

    }

    public interface OnFragmentInteractionListener {

        void manualUpdate();
        void toggleAutoUpdate(boolean isChecked);
        void toggleWaypoint(boolean isChecked);
        void toggleRobot(boolean isChecked);
        void sendWaypoint();
    }

}

