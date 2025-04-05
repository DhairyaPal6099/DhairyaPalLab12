package dhairya.pal.n01576099.lab12;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Dh11airya extends Fragment {

    private RecyclerView courseRV;
    private CourseAdapter adapter;
    private ArrayList<CourseItemModel> courseItemRVArrayList;
    FirebaseDatabase database;

    public Dh11airya() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dh11airya, container, false);

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(getString(R.string.coursesindatabase));
        courseItemRVArrayList = new ArrayList<>();
        courseRV = view.findViewById(R.id.dhaRecyclerView);
        EditText courseNameEditText = view.findViewById(R.id.dhaCourseNameEditText);
        EditText courseDescriptionEditText = view.findViewById(R.id.dhaCourseDescriptionEditText);
        Button addButton = view.findViewById(R.id.dhaAddButton);
        Button deleteButton = view.findViewById(R.id.dhaDeleteButton);

        //courseItemRVArrayList = new ArrayList<>();

        //loadData();
        buildRecyclerView();

        //Changing lowercase input from course name edit text to uppercase
        courseNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String uppercaseEditable = editable.toString().toUpperCase();
                if (!editable.toString().equals(uppercaseEditable)) {
                    courseNameEditText.setText(uppercaseEditable);
                    courseNameEditText.setSelection(uppercaseEditable.length());
                }
            }
        });

        //Add button logic
        addButton.setOnClickListener(vieww -> {
            Pattern pattern = Pattern.compile(getString(R.string.regex_for_coursename));
            if (courseNameEditText.getText().toString().isEmpty()) {
                courseNameEditText.setError(getString(R.string.empty_not_allowed));
            }
            else if (courseDescriptionEditText.getText().toString().isEmpty()) {
                courseDescriptionEditText.setError(getString(R.string.empty_not_allowed));
            }
            else if (!pattern.matcher(courseNameEditText.getText().toString()).matches()) {
                courseNameEditText.setError(getString(R.string.invalid_input));
            }
            else {
                courseItemRVArrayList.add(new CourseItemModel(courseNameEditText.getText().toString(), courseDescriptionEditText.getText().toString()));
                adapter.notifyItemInserted(courseItemRVArrayList.size());
                myRef.setValue(courseItemRVArrayList); //Storing the data in Firebase
                courseNameEditText.setText(R.string.empty_string);
                courseDescriptionEditText.setText(getString(R.string.empty_string));
            }
        });

        deleteButton.setOnClickListener(vieww -> {
            if (courseItemRVArrayList.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.no_data_to_delete), Toast.LENGTH_SHORT).show();
            } else {
                courseItemRVArrayList.clear();
                buildRecyclerView();
                myRef.removeValue();
            }
        });

        return view;
    }

    public void buildRecyclerView() {
        adapter = new CourseAdapter(courseItemRVArrayList, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        courseRV.setHasFixedSize(true);
        courseRV.setLayoutManager(manager);
        courseRV.setAdapter(adapter);
    }
}