package com.example.bryan.androidcontrol;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.EditText;

/**
 * Created by NTU on 29-Jan-18.
 */

public class DirectionFragment extends Fragment {

    private static ImageButton upBtn;
    private static ImageButton downBtn;
    private static ImageButton leftBtn;
    private static ImageButton rightBtn;
    private static EditText robotStatusEditText;

    private OnFragmentInteractionListener listener;

    public DirectionFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_distance, container, false);

        robotStatusEditText = view.findViewById(R.id.robotStatusEditText);

        upBtn = view.findViewById(R.id.upBtn);
        upBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String message = ("f"); //
                if (message != null) {
                    sendMessage(message);
                    robotStatusEditText.setText("Forward");
                }
            }
        });

        downBtn = view.findViewById(R.id.downBtn);
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = ("r");
                if (message != null) {
                    sendMessage(message);
                    robotStatusEditText.setText("Reverse");
                }
            }
        });

        rightBtn= view.findViewById(R.id.rightBtn);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = ("tr");
                if (message != null) {
                    sendMessage(message);
                    robotStatusEditText.setText("Right");
                }
            }
        });

        leftBtn = view.findViewById(R.id.leftBtn);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = ("tl");
                if (message != null) {
                    sendMessage(message);
                    robotStatusEditText.setText("Left");
                }
            }
        });

    return view;
    }

    public void sendMessage(String message) {
        MainActivity a = (MainActivity) getContext();
        a.sendMessage(message);

    }

    public interface OnFragmentInteractionListener {

        void sendMessage(String message);
    }
}
