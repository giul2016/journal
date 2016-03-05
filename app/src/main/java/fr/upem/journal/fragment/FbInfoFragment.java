package fr.upem.journal.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.upem.journal.R;
import fr.upem.journal.adapter.FbPagerAdapter;

/**
 * Created by TTTH on 2/22/2016.
 */
public class FbInfoFragment extends android.support.v4.app.Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private CallbackManager callbackManager;
    private LoginButton login_btn;
    private TextView name_tv;
    private ProfilePictureView pictureView;
    private String[] permissions;
    private ViewPager viewPager;
    private FbPagerAdapter mAdapter;
    private ActionBar actionBar;
    private ShareButton post_photo_bttn;
    private ShareButton post_video_bttn;
    private ShareButton post_link_bttn;
    private LoginManager manager;
    private ShareDialog shareDialog;
    private ImageView user_cover_photo;
    private ProfilePictureView profilePictureView;

    public static FbInfoFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FbInfoFragment fragment = new FbInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getContext());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        //region check photo, video
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 10) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getRealPathFromURI(selectedImageUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    publishImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 20) {
                Uri selectedVideoUri = data.getData();
                String selectedVideoPath = getRealPathFromURI(selectedVideoUri);
                publishVideo(selectedVideoPath.toString());
            }
        }

        //endregion check photo, video
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fb_info, container, false);

        profilePictureView = (ProfilePictureView) view.findViewById(R.id.picture);

        //region Init

        login_btn = (LoginButton) view.findViewById(R.id.login_button);
        name_tv = (TextView) view.findViewById(R.id.name);
        pictureView = (ProfilePictureView) view.findViewById(R.id.picture);
        permissions = new String[]{"user_friends", "user_about_me", "user_actions.music", "user_birthday",
                "user_likes", "user_friends", "user_photos", "user_relationships",
                "user_tagged_places", "user_work_history", "user_actions.books", "user_actions.news",
                "user_education_history", "user_games_activity", "user_location",
                "user_posts", "user_religion_politics", "user_videos", "user_actions.fitness",
                "user_actions.video", "user_events", "user_hometown", "user_managed_groups",
                "user_relationship_details", "user_status", "user_website"};
        post_photo_bttn = (ShareButton) view.findViewById(R.id.post_photo_bttn);
        post_video_bttn = (ShareButton) view.findViewById(R.id.post_video_bttn);
        post_link_bttn = (ShareButton) view.findViewById(R.id.post_link_bttn);
        shareDialog = new ShareDialog(this);

        if (AccessToken.getCurrentAccessToken() == null) {
            name_tv.setText("You are not logged in. Please log in!");
            post_photo_bttn.setVisibility(View.INVISIBLE);
        } else {
            init_info();
        }

        //endregion Init

        //region share image
        post_photo_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT <= 19) {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(i, 10);
                } else if (Build.VERSION.SDK_INT > 19) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 10);
                }
            }
        });

        //endregion share image


        //region share video
        post_video_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT <= 19) {
                    Intent i = new Intent();
                    i.setType("video/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(i, 20);
                } else if (Build.VERSION.SDK_INT > 19) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 20);
                }
            }
        });

        //endregion share video


        //region share link
        post_link_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentTitle("Title Description")
                        .setContentDescription("Description")
                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                        .build();
                post_link_bttn.setShareContent(content);
                shareDialog.show(content);
            }
        });

        // endregion share link

        //region Log in


        login_btn.setReadPermissions(permissions);
        login_btn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                LoginManager.getInstance().logInWithPublishPermissions(getActivity(),Arrays.asList("publish_actions"));
                                String id = object.optString("id");
                                String name = object.optString("name");
                                name_tv.setText("Hi " + name + "!");
                                pictureView.setProfileId(id);
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

        //endregion Log in

        return view;
    }


    //region Functions

    //region post photo
    private void publishImage(Bitmap img) {
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(img)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        Log.e("abc", content.toString());
        shareDialog.show(content);
    }

    //endregion post photo

    //region post video
    private void publishVideo(String videoUrl) {
        Uri videoFileUri = Uri.parse("file://" + videoUrl);
        ShareVideo video = new ShareVideo.Builder()
                .setLocalUrl(videoFileUri)
                .build();

        ShareVideoContent content = new ShareVideoContent.Builder()
                .setVideo(video)
                .build();
        shareDialog.show(content);
    }

    //endregion post video

    //region init
    public void init_info() {
        if (AccessToken.getCurrentAccessToken() == null) {
            name_tv.setText("You are not logged in. Please log in!");
            post_photo_bttn.setVisibility(View.INVISIBLE);
            post_video_bttn.setVisibility(View.INVISIBLE);
            post_link_bttn.setVisibility(View.INVISIBLE);
        } else {
            post_photo_bttn.setVisibility(View.VISIBLE);
            post_video_bttn.setVisibility(View.VISIBLE);
            post_link_bttn.setVisibility(View.VISIBLE);
        }
        Log.e("+++ init info", "ok");
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        object = response.getJSONObject();
                        if (object != null) {
                            String id = object.optString("id");
                            String name = object.optString("name");
                            String birthday = object.optString("birthday");

                            profilePictureView.setProfileId(id);
                            name_tv.setText("Hi " + name);
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,bio,photos{link}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    //endregion init

    public String getRealPathFromURI(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

//endregion functions

    @Override
    public void onResume() {
        super.onResume();
        init_info();
    }

}



