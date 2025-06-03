package com.example.studentmanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    EditText editSearchReg;
    Button btnSearch;
    ListView listViewStudents;

    DatabaseReference databaseReference;

    List<Student> studentList;
    ArrayAdapter<String> adapter;
    List<String> studentDisplayList; // to show in ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_student_list);

        editSearchReg = findViewById(R.id.editSearchReg);
        btnSearch = findViewById(R.id.btnSearch);
        listViewStudents = findViewById(R.id.listViewStudents);

        databaseReference = FirebaseDatabase.getInstance().getReference("Students");

        studentList = new ArrayList<>();
        studentDisplayList = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentDisplayList);
        listViewStudents.setAdapter(adapter);

        loadAllStudents();

        btnSearch.setOnClickListener(v -> {
            String reg = editSearchReg.getText().toString().trim();
            if (TextUtils.isEmpty(reg)) {
                Toast.makeText(this, "Enter registration number to search", Toast.LENGTH_SHORT).show();
            } else {
                searchStudent(reg);
            }
        });

        listViewStudents.setOnItemClickListener((parent, view, position, id) -> {
            Student selectedStudent = studentList.get(position);
            showEditDialog(selectedStudent);
        });
    }

    private void loadAllStudents() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();
                studentDisplayList.clear();

                for (DataSnapshot studentSnap : snapshot.getChildren()) {
                    Student student = studentSnap.getValue(Student.class);
                    if (student != null) {
                        studentList.add(student);
                        studentDisplayList.add(student.regNumber + " - " + student.fullName);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentListActivity.this, "Failed to load students", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchStudent(String regNumber) {
        databaseReference.child(regNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();
                studentDisplayList.clear();

                Student student = snapshot.getValue(Student.class);
                if (student != null) {
                    studentList.add(student);
                    studentDisplayList.add(student.regNumber + " - " + student.fullName);
                } else {
                    Toast.makeText(StudentListActivity.this, "No student found with Reg: " + regNumber, Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentListActivity.this, "Search failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDialog(Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Student");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_edit_student, null);

        final EditText inputFullName = viewInflated.findViewById(R.id.editFullNameDialog);
        final EditText inputCatMarks = viewInflated.findViewById(R.id.editCatMarksDialog);
        final EditText inputExamMarks = viewInflated.findViewById(R.id.editExamMarksDialog);

        inputFullName.setText(student.fullName);
        inputCatMarks.setText(String.valueOf(student.catMarks));
        inputExamMarks.setText(String.valueOf(student.examMarks));

        builder.setView(viewInflated);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newFullName = inputFullName.getText().toString().trim();
            String catStr = inputCatMarks.getText().toString().trim();
            String examStr = inputExamMarks.getText().toString().trim();

            if (TextUtils.isEmpty(newFullName) || TextUtils.isEmpty(catStr) || TextUtils.isEmpty(examStr)) {
                Toast.makeText(StudentListActivity.this, "All fields required", Toast.LENGTH_SHORT).show();
                return;
            }

            int catMarks = Integer.parseInt(catStr);
            int examMarks = Integer.parseInt(examStr);
            int totalMarks = catMarks + examMarks;
            String grade = calculateGrade(totalMarks);

            // Update student object
            student.fullName = newFullName;
            student.catMarks = catMarks;
            student.examMarks = examMarks;
            student.totalMarks = totalMarks;
            student.grade = grade;

            // Update in Firebase
            databaseReference.child(student.regNumber).setValue(student)
                    .addOnSuccessListener(aVoid -> Toast.makeText(StudentListActivity.this, "Student updated", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(StudentListActivity.this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());

            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private String calculateGrade(int total) {
        if (total >= 70) return "A";
        else if (total >= 60) return "B";
        else if (total >= 50) return "C";
        else if (total >= 40) return "D";
        else return "F";
    }
}