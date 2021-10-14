/*Name: Nisha Ramrakhyani (Student Id: 801208678)
* Assignment: Midterm*/

package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentInterface, RegisterFragment.RegisterFragmentInterface, ForumListFragment.ForumListFragmentInterface, CreateForumFragment.NewForumFragmentInterface {
    DataServices.AuthResponse loggedInUser;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, new LoginFragment())
                .commit();
    }

    @Override
    public void CreateNewAcc() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,new RegisterFragment())
                .addToBackStack(null)
                .commit();
        }



    @Override
    public void login_details(DataServices.AuthResponse authResponse) {
        this.token = authResponse.getToken();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, ForumListFragment.newInstance(authResponse.getToken()))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void addNewUser(DataServices.AuthResponse authResponse) {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, ForumListFragment.newInstance(authResponse.getToken()))
                .commit();

    }

    @Override
    public void cancelbutton() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,new LoginFragment())
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void logout() {
        loggedInUser = null;
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,new LoginFragment())
                .commit();
    }

    @Override
    public void newForum(String token) {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, CreateForumFragment.newInstance(token))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void view_forum(String token, DataServices.Forum forum) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,ForumFragment.newInstance(token,forum))
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void new_forum_created(String token, DataServices.Forum forum) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, ForumListFragment.newInstance(token))
                .commit();

    }

    @Override
    public void cancel_new_forum_create() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, ForumListFragment.newInstance(this.token))
                .commit();


    }
}