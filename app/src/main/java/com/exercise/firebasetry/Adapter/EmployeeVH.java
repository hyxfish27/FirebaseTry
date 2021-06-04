package com.exercise.firebasetry.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import com.exercise.firebasetry.R;

public class EmployeeVH extends RecyclerView.ViewHolder
{
    public TextView txt_name, txt_position, txt_option;
    public EmployeeVH(@NonNull @NotNull View itemView)
    {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_name);
        txt_position = itemView.findViewById(R.id.txt_position);
        txt_option = itemView.findViewById(R.id.txt_option);
    }
}
