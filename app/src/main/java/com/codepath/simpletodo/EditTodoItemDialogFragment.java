package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class EditTodoItemDialogFragment extends DialogFragment {

    private EditText mEditText;
    private String mPriority;
    private Spinner mSpinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private int mYear;
    private int mMonth;
    private int mDay;

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
        bindSpinner(view);
        displayTextIfExist();
        setDueDate(view);
    }

    private void setDueDate(View view) {
        DatePicker date = (DatePicker) view.findViewById(R.id.datePicker);
        if (getArguments().getInt("year", -1) != -1) {
            mYear = getArguments().getInt("year");
            mMonth = getArguments().getInt("month");
            mDay = getArguments().getInt("day");
        } else {
            mYear = date.getYear();
            mMonth = date.getMonth();
            mDay = date.getDayOfMonth();
        }
        date.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
            };
        });
    }

    private void bindSpinner(View view) {
        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.priorities, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(spinnerAdapter);
        String existingPriority = getArguments().getString("priority");
        if (existingPriority != null) {
            mPriority = existingPriority;
            mSpinner.setSelection(spinnerAdapter.getPosition(mPriority));
        }
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                mPriority = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
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
        void onFinishEditDialog(
            String inputText,
            String priority,
            int pos,
            int year,
            int month,
            int day);
    }

    public void onUpdateItem(View v) {
        // Return input text back to activity through the implemented listener
        EditTodoItemDialogListener listener = (EditTodoItemDialogListener) getActivity();
        listener.onFinishEditDialog(
            mEditText.getText().toString(),
            mPriority,
            getArguments().getInt("itemIndex"),
            mYear,
            mMonth,
            mDay);
        // Close the dialog and return back to the parent activity
        dismiss();
    }
}
