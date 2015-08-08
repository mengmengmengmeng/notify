package me.notify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

/**
 * Created by David Rommel, B. on 8/8/15.
 */
public class RegistrationFragment extends Fragment {
    EditText first_name, last_name, age, mobile_number, email, password;
    FrameLayout frameLsubmit;
    SharedPreferences preferences;
    TaskRegister taskRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registration, container, false);
        preferences = getActivity().getSharedPreferences("tokens", Context.MODE_PRIVATE);
        first_name = (EditText) v.findViewById(R.id.first_name);
        last_name = (EditText) v.findViewById(R.id.last_name);
        age = (EditText) v.findViewById(R.id.age);
        mobile_number = (EditText) v.findViewById(R.id.mobile_number);
        email = (EditText) v.findViewById(R.id.email);
        password = (EditText) v.findViewById(R.id.password);
        frameLsubmit = (FrameLayout) v.findViewById(R.id.frameLSubmit);

        frameLsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               taskRegister = new TaskRegister(getActivity(), preferences,
                        first_name.getText().toString(), last_name.getText().toString(), age.getText().toString(), mobile_number.getText().toString(),
                        email.getText().toString(), password.getText().toString());

                taskRegister.executeRegistration();
            }
        });

        return v;
    }
}
