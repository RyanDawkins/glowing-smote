package com.ryanddawkins.glowing_smote;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsFragment extends Fragment {

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState)
    {

        Button saveButton = (Button)(getActivity().findViewById(R.id.save_button));
        final Settings settings = new Settings(getActivity());
        final EditText ipText = (EditText)(getActivity().findViewById(R.id.ip_address));
        final EditText portText = (EditText)(getActivity().findViewById(R.id.port_number));

        ipText.setText(settings.getIpAddress());
        portText.setText(""+settings.getPortNumber());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                EditText ipText = (EditText)(getActivity().findViewById(R.id.ip_address));
//                EditText portText = (EditText)(getActivity().findViewById(R.id.port_number));
//                Settings settings = new Settings(getActivity());
                String ipAddress = ipText.getText().toString();
                int portNumber;
                if (!ipAddress.equals("")) {
                    try {
                        portNumber = Integer.parseInt(portText.getText().toString());
                    } catch (NumberFormatException e) {
                        portNumber = Settings.DEFAULT_PORT;
                        Context context = getActivity();
                        CharSequence text = getActivity().getString(R.string.bad_port);
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    Log.d("ipaddress-settings", ipAddress);
                    settings.setIpAddress(ipAddress);
                    settings.setPortNumber(portNumber);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new MoviesFragment())
                            .addToBackStack(null)
                            .commit();

                    try{
                        InputMethodManager inputManager =
                                (InputMethodManager) getActivity().
                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(
                                getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    } catch(NullPointerException e){}

                } else {
                    Context context = getActivity();
                    CharSequence text = getActivity().getString(R.string.no_ip_given);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

}