package com.example.slpt.SA22403292;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.slpt.R;

import java.util.ArrayList;
import java.util.List;

public class SelectLocationDialog extends DialogFragment {
    private OnItemSelectedListener listener;
    private ListView listView;

    private final Handler handler = new Handler(Looper.getMainLooper());


    // Interface to communicate the selected value back to the parent
    public interface OnItemSelectedListener {
        void onItemSelected(String selectedValue, boolean isStart);
    }

    public static SelectLocationDialog newInstance(List<String> lovValues, String label) {
        SelectLocationDialog fragment = new SelectLocationDialog();
        Bundle args = new Bundle();
        args.putStringArrayList("lovValues", new ArrayList<>(lovValues));
        args.putString("label", label);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        int parentWidth = getResources().getDisplayMetrics().widthPixels;
        int parentHeight = getResources().getDisplayMetrics().heightPixels;
        getDialog().getWindow().setLayout((int) (parentWidth * 0.8), parentHeight / 2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searchable_spinner, container, false);

        EditText editText = view.findViewById(R.id.edit_text);
        listView = view.findViewById(R.id.list_view);
        TextView emptyMessage = view.findViewById(R.id.empty_message);

        String label = null;
        List<String> lovValues = null;
        if (getArguments() != null) {
            label = getArguments().getString("label");
            lovValues = getArguments().getStringArrayList("lovValues");
        }
        if (lovValues == null) {
            lovValues = new ArrayList<>();
        }
        if (label == null) {
            label = "Select";
        }
        ((TextView) view.findViewById(R.id.select_label)).setText(label);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                lovValues
        );
        listView.setAdapter(adapter);

        if (lovValues.size() == 0) {
            listView.setVisibility(View.GONE);
            emptyMessage.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyMessage.setVisibility(View.GONE);
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {
                    if (listView.getCount() == 0) {
                        listView.setVisibility(View.GONE);
                        emptyMessage.setVisibility(View.VISIBLE);
                    } else {
                        listView.setVisibility(View.VISIBLE);
                        emptyMessage.setVisibility(View.GONE);
                    }
                }, 100);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        String finalLabel = label;
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedValue = (String) listView.getItemAtPosition(position);
            if (listener != null) {
                listener.onItemSelected(selectedValue, (finalLabel.contains("Start")));
            }
            dismiss();
        });

        return view;
    }

    // Set the listener when the fragment is attached
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException("Parent class must implement OnItemSelectedListener");
        }
    }

}
