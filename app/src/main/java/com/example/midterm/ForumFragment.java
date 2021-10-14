package com.example.midterm;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;


public class ForumFragment extends Fragment implements ForumFragmentViewAdapter.ForumFragmentListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String token = "token";

    TextView textViewForumTitleDetails, textviewForumAuthor, textviewForumDesc, textViewCommenttotal;
    EditText editTextwritecomment;
    ForumFragmentViewAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    DataServices.Account currentAccount;

    // TODO: Rename and change types of parameters
    private DataServices.Forum mParam1;
    private String mParamToken;

    public ForumFragment() {
        // Required empty public constructor
    }

    public static ForumFragment newInstance(String t, DataServices.Forum data) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, data);
        args.putString(token, t);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (DataServices.Forum) getArguments().getSerializable(ARG_PARAM1);
            mParamToken = getArguments().getString(token);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        getActivity().setTitle(getResources().getString(R.string.forum_details_title));
        textViewForumTitleDetails = view.findViewById(R.id.textViewForumTitleDetails);
        textviewForumAuthor = view.findViewById(R.id.textviewForumAuthor);
        textViewCommenttotal = view.findViewById(R.id.textViewCommenttotal);
        textviewForumDesc = view.findViewById(R.id.textviewForumDesc);
        editTextwritecomment = view.findViewById(R.id.editTextwritecomment);
        recyclerView = view.findViewById(R.id.recyclerView_detail);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        DataServices.Account acc = mParam1.getCreatedBy();
        textviewForumDesc.setText(mParam1.getDescription());
        textviewForumDesc.setMovementMethod(new ScrollingMovementMethod());
        textviewForumAuthor.setText(acc.getName());
        textViewForumTitleDetails.setText(mParam1.getTitle());



        // call getAccountAsync
        new GetAccountAsync().execute(mParamToken);
        view.findViewById(R.id.buttonPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextwritecomment.getText().toString().isEmpty()) {
                    new addCommentAsyncFunction().execute(mParamToken, String.valueOf(mParam1.getForumId()), editTextwritecomment.getText().toString());
                }
            }
        });

        return view;
    }

    class GetAccountAsync extends AsyncTask<String,String, DataServices.Account>{
        @Override
        protected void onPostExecute(DataServices.Account account) {
            if(account!=null){
                currentAccount = account;
                new getCommentAsync().execute(mParamToken, String.valueOf(mParam1.getForumId()));
            } else{
                Log.d("TAG", "onPostExecute: ");
            }
        }

        @Override
        protected DataServices.Account doInBackground(String... strings) {
            try{
                return DataServices.getAccount(strings[0]);
            }
            catch (DataServices.RequestException e){

            }
            return null;
        }
    }



    class addCommentAsyncFunction extends AsyncTask<String, String, String[]> {

        @Override
        protected void onPostExecute(String[] s) {
            if (s != null) {
                new getCommentAsync().execute(s[0], s[1]);
            } else
                Toast.makeText(getActivity(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String[] doInBackground(String... strings) {
            try {
                DataServices.createComment(strings[0], parseInt(strings[1]), strings[2]); //token, forumId, text
                String[] s = new String[2];
                s[0] = strings[0];
                s[1] = strings[1];
                return s;
            } catch (DataServices.RequestException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    class getCommentAsync extends AsyncTask<String, String, ArrayList<DataServices.Comment>> {

        @Override
        protected void onPostExecute(ArrayList<DataServices.Comment> s) {
            if (s != null) {
                textViewCommenttotal.setText(String.valueOf(s.size()));
                adapter = new ForumFragmentViewAdapter(s, mParamToken, currentAccount, ForumFragment.this);
                recyclerView.setAdapter(adapter);
            }
        }

        @Override
        protected ArrayList<DataServices.Comment> doInBackground(String... comments) {
            try {
                return DataServices.getForumComments(comments[0], parseInt(comments[1]));
            } catch (DataServices.RequestException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    @Override
    public void deleteComment(String commentId) {
        new deleteCommentAsync().execute(mParamToken, String.valueOf(mParam1.getForumId()), commentId);
    }

    class deleteCommentAsync extends AsyncTask<String, String, String[]> {

        @Override
        protected void onPostExecute(String[] string) {
            if (string != null)
                new getCommentAsync().execute(string[0], string[1]);
        }

        @Override
        protected String[] doInBackground(String... comments) {
            try {
                DataServices.deleteComment(comments[0], parseInt(comments[1]), parseInt(comments[2]));
                String[] string = new String[2];
                string[0] = comments[0];
                string[1] = comments[1];
                return string;
            } catch (DataServices.RequestException e) {
                e.printStackTrace();
                return null;
            }

        }
    }


}