package fr.upem.journal.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import fr.upem.journal.R;

public class AddCategoryDialogFragment extends DialogFragment {

    public interface AddCategoryDialogListener {
        void onDialogPositiveClick(String categoryTitle);

        void onDialogNegativeClick();
    }

    private AddCategoryDialogListener listener;
    private Context context;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.addCategory);

        final EditText categoryTitleEditText = new EditText(context);
        categoryTitleEditText.setHint(R.string.addCategoryDialogMessage);
        builder.setView(categoryTitleEditText);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String categoryTitle = categoryTitleEditText.getText().toString();
                listener.onDialogPositiveClick(categoryTitle);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogNegativeClick();
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;

        try {
            listener = (AddCategoryDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement AddCategoryDialogListener");
        }
    }
}
