// Dhairya Pal N01576099

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
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(CourseItemModel courseItem, int position);
    }

    public CourseAdapter(ArrayList<CourseItemModel> courseItemRVArrayList, Context context, OnItemLongClickListener onItemLongClickListener) {
        this.courseItemModelArrayList = courseItemRVArrayList;
        this.context = context;
        this.onItemLongClickListener = onItemLongClickListener;
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
        holder.itemView.setOnLongClickListener(view -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(courseItemModel, position);
            }
            return true;
        });
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
