package com.exercise.firebasetry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.exercise.firebasetry.data.DAOEmployee;
import com.exercise.firebasetry.data.Employee;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText edit_name = findViewById(R.id.edit_name);
        final EditText edit_position = findViewById(R.id.edit_position);
        Button btn_submit = findViewById(R.id.btn_submit);
        Button btn_open = findViewById(R.id.btn_open);
        DAOEmployee dao = new DAOEmployee();
        Employee emp_edit = (Employee) getIntent().getSerializableExtra("EDIT");

        if(emp_edit != null)
        {
            btn_submit.setText("Update");
            edit_name.setText(emp_edit.getName());
            edit_position.setText(emp_edit.getPosition());
            btn_open.setVisibility(View.GONE);
        }
        else
        {
            btn_submit.setText("Submit");
            btn_open.setVisibility(View.VISIBLE);
        }

        btn_submit.setOnClickListener(v ->
        {
            Employee emp = new Employee(edit_name.getText().toString(),edit_position.getText().toString());

            if(emp_edit == null)
            {
                dao.add(emp).addOnSuccessListener(suc->
                {
                    Toast.makeText(this, "Record is inserted", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er->
                {
                    Toast.makeText(this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
            else
            {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("name",edit_name.getText().toString());
                hashMap.put("position",edit_position.getText().toString());
                dao.update(emp_edit.getKey(), hashMap).addOnSuccessListener(suc->
                {
                    Toast.makeText(this, "Record is updated", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er->
                {
                    Toast.makeText(this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
            edit_name.setText("");
            edit_position.setText("");
        });

        btn_open.setOnClickListener(v ->
        {
            Intent intent = new Intent(MainActivity.this, RVActivity.class);
            startActivity(intent);
        });
    }
}