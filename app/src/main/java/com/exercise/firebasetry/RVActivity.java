package com.exercise.firebasetry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.exercise.firebasetry.Adapter.EmployeeVH;
import com.exercise.firebasetry.Adapter.RVAdapter;
import com.exercise.firebasetry.data.DAOEmployee;
import com.exercise.firebasetry.data.Employee;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RVActivity extends AppCompatActivity
{
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    //RVAdapter adapter;
    DAOEmployee dao;
    FirebaseRecyclerAdapter adapter;
    boolean isLoading = false;
    String key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);

        swipeRefreshLayout = findViewById(R.id.swipe);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        //adapter = new RVAdapter(this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        //recyclerView.setAdapter(adapter);
        dao = new DAOEmployee();
        FirebaseRecyclerOptions<Employee> option = new FirebaseRecyclerOptions.Builder<Employee>()
                .setQuery(dao.get(), new SnapshotParser<Employee>()
                {
                    @NonNull
                    @NotNull
                    @Override
                    public Employee parseSnapshot(@NonNull @NotNull DataSnapshot snapshot)
                    {
                        Employee emp = snapshot.getValue(Employee.class);
                        emp.setKey(snapshot.getKey());
                        return emp;
                    }
                }).build();

        adapter = new FirebaseRecyclerAdapter(option) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position, @NonNull @NotNull Object model)
            {
                EmployeeVH vh = (EmployeeVH) holder;
                Employee emp = (Employee) model;
                vh.txt_name.setText(emp.getName());
                vh.txt_position.setText(emp.getPosition());
                vh.txt_option.setOnClickListener(v ->
                {
                    PopupMenu popupMenu = new PopupMenu(RVActivity.this, vh.txt_option);
                    popupMenu.inflate(R.menu.option_menu);
                    popupMenu.setOnMenuItemClickListener(item ->
                    {
                        switch (item.getItemId())
                        {
                            case R.id.menu_edit:
                                Intent intent = new Intent(RVActivity.this, MainActivity.class);
                                intent.putExtra("EDIT", emp);
                                RVActivity.this.startActivity(intent);
                                break;
                            case R.id.menu_remove:
                                DAOEmployee dao = new DAOEmployee();
                                dao.remove(emp.getKey()).addOnSuccessListener(suc->
                                {
                                    Toast.makeText(RVActivity.this, "Record is removed", Toast.LENGTH_SHORT).show();
                                    //notifyItemRemoved(position);
                                }).addOnFailureListener(er->
                                {
                                    Toast.makeText(RVActivity.this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                                break;
                        }
                        return false;
                    });
                    popupMenu.show();
                });
            }

            @NonNull
            @NotNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(RVActivity.this).inflate(R.layout.layout_item,parent,false);
                return new EmployeeVH(view);
            }

            @Override
            public void onDataChanged()
            {
                Toast.makeText(RVActivity.this, "Data changed",Toast.LENGTH_SHORT).show();
                super.onDataChanged();
            }
        };
        recyclerView.setAdapter(adapter);

        /*loadData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if(totalItem < lastVisible+3)
                {
                    if(!isLoading)
                    {
                        isLoading = true;
                        loadData();
                    }
                }
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    /*private void loadData()
    {
        swipeRefreshLayout.setRefreshing(true);
        dao.get(key).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
            {
                ArrayList<Employee> emps = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren())
                {
                    Employee emp = data.getValue(Employee.class);
                    emp.setKey(data.getKey());
                    emps.add(emp);

                    key = data.getKey();
                }
                adapter.setItems(emps);
                adapter.notifyDataSetChanged();

                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error)
            {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }*/
}