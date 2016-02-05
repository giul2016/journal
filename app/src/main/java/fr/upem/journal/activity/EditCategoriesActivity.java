package fr.upem.journal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import fr.upem.journal.R;
import fr.upem.journal.fragment.EditCategoriesFragment;

public class EditCategoriesActivity extends AppCompatActivity implements EditCategoriesFragment.OnItemSelectedListener {

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_categories);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.editCategoriesFragment, new EditCategoriesFragment
                ()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_categories_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionAddCategory:
                Toast.makeText(getApplicationContext(), "Add Category", Toast.LENGTH_LONG).show();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String categoryTitle) {
        Intent intent = new Intent(this, EditNewsFeedsActivity.class);
        intent.putExtra("title", categoryTitle);
        startActivity(intent);
    }
}
