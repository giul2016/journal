package fr.upem.journal;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import fr.upem.journal.tasks.FetchRSSFeedTask;

public class NewsFeedActivity extends AppCompatActivity {

    private NewsFeedFragment newsFeedFragment;
    private EditText urlEditText;
    private Button downloadButton;
    private FetchRSSFeedTask fetchRSSFeedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        FragmentManager fragmentManager = getFragmentManager();
        newsFeedFragment = (NewsFeedFragment) fragmentManager.findFragmentById(R.id.newsFeedFragment);
        fragmentManager.beginTransaction().replace(R.id.newsFeedFragment, newsFeedFragment).commit();

        urlEditText = (EditText) findViewById(R.id.urlEditText);
        downloadButton = (Button) findViewById(R.id.downloadButton);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fetchRSSFeedTask == null || fetchRSSFeedTask.isCancelled() || fetchRSSFeedTask.getStatus().equals
                        (AsyncTask.Status.FINISHED)) {
                    fetchRSSFeedTask = new FetchRSSFeedTask() {
                        @Override
                        protected void onPostExecute(List<Item> items) {
                            newsFeedFragment.updateItems(items);
                        }
                    };
                    fetchRSSFeedTask.execute(urlEditText.getText().toString());
                } else {
                    fetchRSSFeedTask.cancel(true);
                }
            }
        });
    }

}
