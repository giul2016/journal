package fr.upem.journal.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import fr.upem.journal.R;

public class FacebookActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerList;
    private final String[] drawerItems = {"News", "Facebook", "Twitter", "Settings"};

    private CallbackManager callbackManager;
    private LoginButton login_btn;
    private TextView name_tv;
    private ProfilePictureView pictureView;
    private String[] permissions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_facebook);

        // region Left toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawerOpen, R.string.drawerClose) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);

        drawerList = (ListView) findViewById(R.id.leftDrawer);
        drawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawerItems));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(FacebookActivity.this, NewsFeedActivity.class);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(FacebookActivity.this, TwitterActivity.class);
                    startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(FacebookActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // endregion left toolbar

        //region Init

        login_btn = (LoginButton)findViewById(R.id.login_button);
        name_tv = (TextView)findViewById(R.id.name);
        pictureView = (ProfilePictureView) findViewById(R.id.picture);
        permissions = new String[]{"user_friends","user_about_me","user_actions.music","user_birthday",
                                    "user_likes","user_friends","user_photos","user_relationships",
                                    "user_tagged_places","user_work_history","user_actions.books","user_actions.news",
                                    "user_education_history","user_games_activity","user_location",
                                    "user_posts","user_religion_politics","user_videos","user_actions.fitness",
                                    "user_actions.video","user_events","user_hometown","user_managed_groups",
                                    "user_relationship_details","user_status","user_website"};

        if(AccessToken.getCurrentAccessToken()==null){
            name_tv.setText("You are not logged in. Please log in!");
        }
        else {
            init_info();
        }

        //endregion Init



        //region -----Login-----
        login_btn.setReadPermissions(permissions);
        login_btn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                String id = object.optString("id");
                                String name = object.optString("name");
                                name_tv.setText("Hi " + name + "!");
                                pictureView.setProfileId(id);
                                getLikedPages();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                name_tv.setText("Login canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                name_tv.setText("Login failed.");
            }
        });


        //endregion




    }


    //region Config

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.actionSettings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
            case R.id.actionEditCategories:
                Intent intentEdit = new Intent(this, EditCategoriesActivity.class);
                startActivity(intentEdit);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //endregion Config

    //region function

    public void init_info(){
        Log.e("+++ init info", "ok");
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
//                        Log.e("GraphResponse", response.toString());

                                        object = response.getJSONObject();
                                        String id = object.optString("id");
                                        String name = object.optString("name");
                                        String birthday = object.optString("birthday");
                                        Log.d("++respons ",response.toString());
                                        name_tv = (TextView)findViewById(R.id.name);
                                        name_tv.setText("Hi " + name +birthday);
                                        ProfilePictureView profilePictureView = (ProfilePictureView)findViewById(R.id.picture);
                                        profilePictureView.setProfileId(id);

                                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,bio,photos{link}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getLikedPages(){
        Log.e("+++ get pages", "ok");
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(final JSONObject object, GraphResponse response) {
                        // Application code
//                        Log.e("GraphResponse", response.toString());

                        GraphRequest likeRequest = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(),
                                "/me/likes/",
                                new GraphRequest.Callback() {
                                    @Override
                                    public void onCompleted(GraphResponse response) {

                                        try {
//                                            Intent intent = new Intent(MainActivity.this,PagesList.class);
                                            Log.d("GraphLikeResponse", "Likes:- " + response.toString());
                                            JSONObject object = response.getJSONObject();
                                            JSONArray array = object.getJSONArray("data");
//                                            intent.putExtra("jsonPagesData",array.toString());
//                                            startActivity(intent);
                                            Log.e("GraphObject", "Likes:- " + array.toString());
                                            ArrayList<String> pages_list = new ArrayList<String>();
                                            for (int i=0; i<array.length();i++){
                                                Log.e("+ name : ", ((JSONObject) array.get(i)).optString("name"));
                                                pages_list.add(((JSONObject) array.get(i)).optString("name"));
                                                ArrayAdapter adapter = new ArrayAdapter<String>(FacebookActivity.this, android.R.layout.simple_list_item_1, pages_list);
                                                ListView listView = (ListView)findViewById(R.id.listView);
                                                listView.setAdapter(adapter);

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                        likeRequest.executeAsync();

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,bio,photos{link}");
        request.setParameters(parameters);
        request.executeAsync();

    }

    //endregion function

}
