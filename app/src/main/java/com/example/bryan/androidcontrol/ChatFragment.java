package com.example.bryan.androidcontrol;

/**
 * Created by Bryan on 1/30/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ChatFragment extends Fragment {

    public static ArrayAdapter<String> chatArrayAdapter;

    private static Button f1Btn;
    private static Button f2Btn;
    private static Button reconfigureBtn;
    private static Button reconfigf1btn;
    private static Button reconfigf2btn;
    private static ListView chatListView;
    private static EditText inputEditText;
    private static EditText inputReconfigF1Text;
    private static EditText inputReconfigF2Text;

    private static Button sendBtn;
    private static boolean visibility =false;

    private SharedPreferences prefs;

    private OnFragmentInteractionListener listener;

    public ChatFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        f1Btn = view.findViewById(R.id.f1Btn);
        f1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = prefs.getString("f1Key", "");
                if (message != null) {
                    sendMessage(message);
                }
            }
        });
        f1Btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String message = inputEditText.getText().toString();
                prefs.edit().putString("f1Key", message).apply();
                Toast.makeText(getContext(), "Command successfully assigned to F1",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        f2Btn = view.findViewById(R.id.f2Btn);
        f2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = prefs.getString("f2Key", "");
                if (message != null) {
                    sendMessage(message);
                }
            }
        });
        f2Btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String message = inputEditText.getText().toString();
                prefs.edit().putString("f2Key", message).apply();
                Toast.makeText(getContext(), "Command successfully assigned to F2",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        inputReconfigF1Text = view.findViewById(R.id.inputReconfigF1Text);
        inputReconfigF2Text = view.findViewById(R.id.inputReconfigF2Text);
        reconfigf1btn = view.findViewById(R.id.reconfigf1btn);
        reconfigf2btn = view.findViewById(R.id.reconfigf2btn);
        reconfigureBtn = view.findViewById(R.id.reconfigureBtn);
        reconfigureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!visibility){
                    visibility = true;
                    reconfigf1btn.setVisibility(View.VISIBLE);
                    reconfigf2btn.setVisibility(View.VISIBLE);
                    inputReconfigF1Text.setVisibility(View.VISIBLE);
                    inputReconfigF2Text.setVisibility(View.VISIBLE);
                    reconfigf1btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String message = inputReconfigF1Text.getText().toString();
                            prefs.edit().putString("f1Key", message).apply();
                            Toast.makeText(getContext(), "Command successfully assigned to F1",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    reconfigf2btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String message = inputReconfigF2Text.getText().toString();
                            prefs.edit().putString("f2Key", message).apply();
                            Toast.makeText(getContext(), "Command successfully assigned to F2",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    visibility = false;
                    reconfigf1btn.setVisibility(View.INVISIBLE);
                    reconfigf2btn.setVisibility(View.INVISIBLE);
                    inputReconfigF1Text.setVisibility(View.INVISIBLE);
                    inputReconfigF2Text.setVisibility(View.INVISIBLE);
                }


            }
        });

        inputEditText = view.findViewById(R.id.inputEditText);

        // Initialize the array adapter for the conversation thread //
        chatArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.display_text);

        chatListView = view.findViewById(R.id.chatListView);
        chatListView.setAdapter(chatArrayAdapter);

        sendBtn = view.findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputEditText.getText().toString();
                sendMessage(message);
                ChatFragment.inputEditText.setText("");
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void sendMessage(String message) {
        if (listener != null) {
            listener.sendMessage(message);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {

        void sendMessage(String message);
    }
}
