package fr.upem.journal.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.LinearLayout;

import fr.upem.journal.R;

public class AddNewsFeedDialogFragment extends DialogFragment {

    public interface AddNewsFeedDialogListener {
        void onDialogPositiveClick(String label, String link);

        void onDialogNegativeClick();
    }

    private AddNewsFeedDialogListener listener;
    private Context context;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.addNewsFeed);

        final EditText newsFeedLabelEditText = new EditText(context);
        newsFeedLabelEditText.setHint(R.string.addNewsFeedLabelEditText);
        final EditText newsFeedLinkEditText = new EditText(context);
        newsFeedLinkEditText.setHint(R.string.addNewsFeedLinkEditText);

        final LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(newsFeedLabelEditText);
        layout.addView(newsFeedLinkEditText);

        builder.setView(layout);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String label = newsFeedLabelEditText.getText().toString();
                String link = newsFeedLinkEditText.getText().toString();
                listener.onDialogPositiveClick(label, link);
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
            listener = (AddNewsFeedDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement AddNewsFeedDialogListener");
        }
    }
}
