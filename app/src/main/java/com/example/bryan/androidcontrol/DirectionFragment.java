package com.example.bryan.androidcontrol;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;


/**
 * Created by NTU on 29-Jan-18.
 */

public class DirectionFragment extends Fragment {

    private static ImageButton upBtn;
    private static ImageButton downBtn;
    private static ImageButton leftBtn;
    private static ImageButton rightBtn;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_distance, container, false);

        upBtn = view.findViewById(R.id.upBtn);
        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void OnClick(View view) {
                //move the robot up
                //must have connection with the robot
                //update the map as robot moves up
            }
        });

        downBtn = view.findViewById(R.id.downBtn);
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void OnClick(View view) {
                //move the robot down
                //must have connection with the robot
                //update the map as the robot moves down
            }
        });


        leftBtn = view.findViewById(R.id.leftBtn);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void OnClick(View view) {
                //move the robot left
                //must have connection with the robot
                //update the map as the robot moves left
            }
        });


        rightBtn = view.findViewById(R.id.rightBtn);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void OnClick(View view) {
                //move the robot right
                //must have connection with the robot
                //update the map as the robot moves right
            }
        });

    return view;
    }


}
