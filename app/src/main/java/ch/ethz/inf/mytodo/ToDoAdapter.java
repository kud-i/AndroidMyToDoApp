package ch.ethz.inf.mytodo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ch.ethz.inf.mytodo.io.IO;
import ch.ethz.inf.mytodo.model.Category;
import ch.ethz.inf.mytodo.model.ToDo;

/**
 * Created by m on 23/11/15.
 */
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    final ArrayList mDataset;
    private final Activity mAdapterContext;
    private final Category mCategory;

         // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            // each data item is just a string in this case
            private TextView mTextTitle;
            private TextView mTextDate;
            private CheckBox mCheckBox;
            private Context mContext;
            public  View itemView;

            public ViewHolder(final Context context, View itemView)  {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);
                mContext = context;
                mTextTitle = (TextView) itemView.findViewById(R.id.textTitle);
                mTextDate = (TextView) itemView.findViewById(R.id.textDate);
                mCheckBox = (CheckBox) itemView.findViewById(R.id.checkBox);
                this.itemView = itemView;

                itemView.setOnClickListener(this);

                //Checkbox Clicklistener
                mCheckBox.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int position = getLayoutPosition();

                        ToDo toDo = (ToDo) mDataset.get(position);
                        toDo.setDone(mCheckBox.isChecked());

                        mDataset.set(position,toDo);

                        IO io = new IO();
                        io.WriteData(mDataset,mContext,mCategory);


                    }
                });

            }

            //On click listener for edit
            @Override
            public void onClick(View view) {
                int position = getLayoutPosition(); // gets item position
                ToDo toDo = (ToDo) mDataset.get(position);

                //New Intent with parameter
                Intent intent = new Intent(mContext, ToDoActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("title",toDo.getTitle());
                intent.putExtra("date", toDo.getDueDate());
                intent.putExtra("done", toDo.getDone());

                mAdapterContext.startActivityForResult(intent, 1);

            }

        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public ToDoAdapter(ArrayList myDataset, Activity context, Category category) {
            this.mAdapterContext = context;
            mDataset = myDataset;
            this.mCategory = category;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View toDoView = inflater.inflate(R.layout.row_item, parent, false);

            // Return a new holder instance
            return new ViewHolder(context,toDoView);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            ToDo toDo = (ToDo) mDataset.get(position);
            holder.mTextTitle.setText(toDo.getTitle());

            //Parse and format the date in nicer format.
            DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date date = null;
            try {
                date = formatter.parse(toDo.getDueDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DateFormat frontendFormatter = new SimpleDateFormat("E. dd.MM.yy");

            holder.mTextDate.setText(frontendFormatter.format(date));

            if (toDo.getDone()) {
                holder.mCheckBox.setChecked(true);
            }else{
                holder.mCheckBox.setChecked(false);
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }

}

