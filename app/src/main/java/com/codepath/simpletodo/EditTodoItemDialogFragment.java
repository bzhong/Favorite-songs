package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class EditTodoItemDialogFragment extends DialogFragment {

    private EditText mEditText;

    public EditTodoItemDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditTodoItemDialogFragment newInstance(String title) {
        EditTodoItemDialogFragment frag = new EditTodoItemDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_edit, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Todo Item");
        getDialog().setTitle(title);

        Button saveButton = (Button) view.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateItem(v);
            }
        });

        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.updateText);
        displayTextIfExist();
    }

    private void displayTextIfExist() {
        String text = getArguments().getString("itemText");
        if (text != null) {
            mEditText.setText(text);
            mEditText.setSelection(text.length());
        }
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    public interface EditTodoItemDialogListener {
        void onFinishEditDialog(String inputText, int pos);
    }

    public void onUpdateItem(View v) {
        // Return input text back to activity through the implemented listener
        EditTodoItemDialogListener listener = (EditTodoItemDialogListener) getActivity();
        listener.onFinishEditDialog(mEditText.getText().toString(), getArguments().getInt("itemIndex"));
        // Close the dialog and return back to the parent activity
        dismiss();
    }
}
