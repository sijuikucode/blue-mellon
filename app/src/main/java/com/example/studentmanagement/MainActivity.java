package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText editRegNumber, editFullName, editCatMarks, editExamMarks;
    Button btnSave, btnGoToList;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editRegNumber = findViewById(R.id.editRegNumber);
        editFullName = findViewById(R.id.editFullName);
        editCatMarks = findViewById(R.id.editCatMarks);
        editExamMarks = findViewById(R.id.editExamMarks);
        btnSave = findViewById(R.id.btnSave);
        btnGoToList = findViewById(R.id.btnGoToList);

        databaseReference = FirebaseDatabase.getInstance().getReference("Students");

        btnSave.setOnClickListener(v -> saveStudent());
        btnGoToList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StudentListActivity.class);
            startActivity(intent);
        });
    }

    private void saveStudent() {
        String regNumber = editRegNumber.getText().toString().trim();
        String fullName = editFullName.getText().toString().trim();
        String catMarksStr = editCatMarks.getText().toString().trim();
        String examMarksStr = editExamMarks.getText().toString().trim();

        if (TextUtils.isEmpty(regNumber) || TextUtils.isEmpty(fullName)
                || TextUtils.isEmpty(catMarksStr) || TextUtils.isEmpty(examMarksStr)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int catMarks = Integer.parseInt(catMarksStr);
        int examMarks = Integer.parseInt(examMarksStr);
        int totalMarks = catMarks + examMarks;
        String grade = calculateGrade(totalMarks);

        Student student = new Student(regNumber, fullName, catMarks, examMarks, totalMarks, grade);

        // Save using regNumber as key
        databaseReference.child(regNumber).setValue(student)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MainActivity.this, "Student saved", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private String calculateGrade(int total) {
        if (total >= 70) return "A";
        else if (total >= 60) return "B";
        else if (total >= 50) return "C";
        else if (total >= 40) return "D";
        else return "F";
    }

    private void clearFields() {
        editRegNumber.setText("");
        editFullName.setText("");
        editCatMarks.setText("");
        editExamMarks.setText("");
    }
}