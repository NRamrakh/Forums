package com.example.midterm;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    EditText editTextLoginEmail;
    EditText editTextLoginPassword;
    Button loginButton;
    TextView textViewCreateAccount;
    String emailId, password;
    DataServices.Account account;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().setTitle(getResources().getString(R.string.login_title));
        editTextLoginEmail = view.findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = view.findViewById(R.id.editTextLoginPassword);

        view.findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailId = editTextLoginEmail.getText().toString();
                password = editTextLoginPassword.getText().toString();
                if(emailId.isEmpty() || password.isEmpty()){
                    Toast.makeText(getActivity(),getResources().getString(R.string.mandatory_fields), Toast.LENGTH_LONG).show();
                }
                else{
                    new LoginAsync().execute(emailId, password);
                }
            }
        });
        view.findViewById(R.id.textViewCreateAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lListener.CreateNewAcc();
            }
        });
        return view;
    }

    LoginFragmentInterface lListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragmentInterface) {
            lListener = (LoginFragmentInterface)context;
        } else {
            throw new RuntimeException(context.toString() + "must implement IListener");
        }
    }

    public interface LoginFragmentInterface{
        void CreateNewAcc();
        void login_details(DataServices.AuthResponse authResponse);
    }

    class LoginAsync extends AsyncTask<String, Integer,DataServices.AuthResponse>{

        @Override
        protected DataServices.AuthResponse doInBackground(String... inputs) {
            try {
                return DataServices.login(inputs[0], inputs[1]);
            } catch (DataServices.RequestException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(DataServices.AuthResponse authResponse) {
            if(authResponse!=null){
                lListener.login_details(authResponse);
            }
            else{
                Toast.makeText(getActivity(), getResources().getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show();
            }
        }
    }
}