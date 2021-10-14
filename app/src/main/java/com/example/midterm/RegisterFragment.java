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

public class RegisterFragment extends Fragment {

    EditText editTextRegisterName;
    EditText editTextRegisterEmail;
    EditText editTextRegisterPassword;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        getActivity().setTitle(getResources().getString(R.string.new_account_title));
        editTextRegisterName = view.findViewById(R.id.editTextRegisterName);
        editTextRegisterEmail = view.findViewById(R.id.editTextRegisterEmail);
        editTextRegisterPassword = view.findViewById(R.id.editTextRegisterPassword);


        view.findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextRegisterName.getText().toString();
                String email = editTextRegisterEmail.getText().toString();
                String password = editTextRegisterPassword.getText().toString();
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_LONG).show();
                } else {
                    new RegisterAsync().execute(name, email, password);
                }
            }
        });
        view.findViewById(R.id.textViewCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rListener.cancelbutton();
            }
        });


        return view;
    }

    RegisterFragmentInterface rListener;

    public interface RegisterFragmentInterface {
        void addNewUser(DataServices.AuthResponse authResponse);

        void cancelbutton();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RegisterFragment.RegisterFragmentInterface) {
            rListener = (RegisterFragment.RegisterFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement IListener");
        }
    }

    class RegisterAsync extends AsyncTask<String, String, DataServices.AuthResponse> {
        @Override
        protected DataServices.AuthResponse doInBackground(String... inputs) {
            try {
                return DataServices.register(inputs[0], inputs[1], inputs[2]);
            } catch (DataServices.RequestException e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(DataServices.AuthResponse authResponse) {
            if (authResponse != null) {
                rListener.addNewUser(authResponse);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.email_exists), Toast.LENGTH_SHORT).show();
            }

        }

    }
}