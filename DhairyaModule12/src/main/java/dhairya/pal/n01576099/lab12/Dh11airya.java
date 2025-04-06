// Dhairya Pal N01576099

package dhairya.pal.n01576099.lab12;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Dh11airya extends Fragment {
    private int uniqueRecordId;
    private RecyclerView courseRV;
    private CourseAdapter adapter;
    private ArrayList<CourseItemModel> courseItemRVArrayList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    public Dh11airya() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_dh11airya, container, false);

        myRef = database.getReference(getString(R.string.coursesindatabase));
        courseRV = view.findViewById(R.id.dhaRecyclerView);
        EditText courseNameEditText = view.findViewById(R.id.dhaCourseNameEditText);
        EditText courseDescriptionEditText = view.findViewById(R.id.dhaCourseDescriptionEditText);
        Button addButton = view.findViewById(R.id.dhaAddButton);
        Button deleteButton = view.findViewById(R.id.dhaDeleteButton);
        courseItemRVArrayList = new ArrayList<>();

        //Setting the unique record id to the maximum course id + 1
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int maxId = -1;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CourseItemModel courseItem = dataSnapshot.getValue(CourseItemModel.class);
                    if (courseItem != null && courseItem.getCourseId() > maxId) {
                        maxId = courseItem.getCourseId();
                    }
                }
                uniqueRecordId = maxId + 1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CourseItemModel courseItem = snapshot.getValue(CourseItemModel.class);
                if (courseItem != null) {
                    courseItemRVArrayList.add(courseItem);
                    adapter.notifyItemInserted(courseItemRVArrayList.size() - 1);
                }
                uniqueRecordId++;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CourseItemModel updatedItem = snapshot.getValue(CourseItemModel.class);
                int indexToUpdate = -1;

                for (int i = 0; i < courseItemRVArrayList.size(); i++) {
                    if (courseItemRVArrayList.get(i).getCourseId() == updatedItem.getCourseId()) {
                        indexToUpdate = i;
                        break;
                    }
                }

                if (indexToUpdate != -1) {
                    courseItemRVArrayList.set(indexToUpdate, updatedItem);
                    adapter.notifyItemChanged(indexToUpdate);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                CourseItemModel removedItem = snapshot.getValue(CourseItemModel.class);
                int indexToUpdate = -1;

                for (int i = 0; i < courseItemRVArrayList.size(); i++) {
                    if (courseItemRVArrayList.get(i).getCourseId() == removedItem.getCourseId()) {
                        indexToUpdate = i;
                        courseItemRVArrayList.remove(indexToUpdate);
                        adapter.notifyItemRemoved(indexToUpdate);
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

        addButton.setOnClickListener(vieww -> {
            Pattern pattern = Pattern.compile(getString(R.string.regex));
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
                //Storing a new course record to 'coursesInDatabase' in Firebase
                String key = String.valueOf(uniqueRecordId);
                myRef.child(key).setValue(new CourseItemModel(uniqueRecordId, courseNameEditText.getText().toString(), courseDescriptionEditText.getText().toString()));
                courseNameEditText.setText(getString(R.string.empty_string));
                courseDescriptionEditText.setText(getString(R.string.empty_string));
            }
        });

        deleteButton.setOnClickListener(vieww -> {
            if (courseItemRVArrayList.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.no_data_to_delete), Toast.LENGTH_SHORT).show();
            } else {
                myRef.removeValue();
            }
            uniqueRecordId = 0;
        });

        return view;
    }

    private void buildRecyclerView() {
        //The third argument is a method I made up in CourseAdapter
        adapter = new CourseAdapter(courseItemRVArrayList, getContext(), new CourseAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(CourseItemModel courseItem, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setPositiveButton(R.string.delete, (dialogInterface, i) -> {
                    myRef.child(String.valueOf(courseItem.getCourseId())).removeValue();
                    dialogInterface.dismiss();
                });
                builder.setTitle(R.string.delete_this_record);
                builder.create().show();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        courseRV.setHasFixedSize(true);
        courseRV.setLayoutManager(manager);
        courseRV.setAdapter(adapter);
    }
}