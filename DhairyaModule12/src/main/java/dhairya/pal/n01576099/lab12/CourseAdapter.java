package dhairya.pal.n01576099.lab12;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{
    private ArrayList<CourseItemModel> courseItemModelArrayList;
    private Context context;

    public CourseAdapter(ArrayList<CourseItemModel> courseItemRVArrayList, Context context) {
        this.courseItemModelArrayList = courseItemRVArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseItemModel courseItemModel = courseItemModelArrayList.get(position);
        holder.courseNameTV.setText(courseItemModel.getCourseName());
        holder.courseDescriptionTV.setText(courseItemModel.getCourseDescription());
    }

    @Override
    public int getItemCount() {
        return courseItemModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView courseNameTV, courseDescriptionTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            courseNameTV = itemView.findViewById(R.id.dhaCourseName);
            courseDescriptionTV = itemView.findViewById(R.id.dhaCourseDescription);
        }
    }
}
