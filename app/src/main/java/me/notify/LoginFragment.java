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
 * Created by David Rommel, B. on 8/9/15.
 */
public class LoginFragment extends Fragment {
    EditText email, password;
    FrameLayout frameLsubmit;
    SharedPreferences preferences;
    TaskRegisterAndLogin taskRegisterAndLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        preferences = getActivity().getSharedPreferences("tokens", Context.MODE_PRIVATE);
        email = (EditText) v.findViewById(R.id.email);
        password = (EditText) v.findViewById(R.id.password);
        frameLsubmit = (FrameLayout) v.findViewById(R.id.frameLSubmit);

        frameLsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(last_name.length() == 0 || first_name.length() == 0 ||
                //        age.length() == 0 || mobile_number.length() == 0 ||
                //        email.length() == 0 || password.length() == 0){

                //    Toast.makeText(getActivity(), "Please fill all values! :)", Toast.LENGTH_LONG).show();

                //}else if(password.length() < 8){
                //    Toast.makeText(getActivity(), "Password is too short (minimum is 8 characters)! :)", Toast.LENGTH_LONG).show();
                //}else{
                    taskRegisterAndLogin = new TaskRegisterAndLogin(getActivity(), preferences,
                            "", "", "", "",
                            email.getText().toString(), password.getText().toString(), TaskRegisterAndLogin.LOGIN);

                    taskRegisterAndLogin.executeRegistration();
                //}
            }
        });

        return v;
    }
}