📄 Hospital Management System – Android Application Documentation

1. Project Overview

The Hospital Management System is an Android-based mobile application developed using Android Studio and Firebase. It allows medical staff to register, search, and manage patient information easily and efficiently.


---

2. Core Features

Login Page with background image.

Add New Patient Record – stores patient details in Firebase.

Search & Edit Patient Info – retrieve and update records by registration number.

Patient List View – displays all registered patients.

Firebase Integration for real-time data storage and retrieval.



---

3. Technologies Used

Frontend: Android XML Layouts, Java

Backend: Firebase Realtime Database

IDE: Android Studio

Languages: Java, XML

Fonts & Media: Custom fonts (e.g., Times New Roman), background images



---

4. Database Structure (Firebase Realtime DB)

patients
  └── <unique_id>
       ├── regNumber: "12345"
       ├── fullName: "Jane Doe"
       ├── symptoms: "Fever"
       ├── diagnosis: "Malaria"


---

5. Key Java Classes

a. Patient.java

Data model for each patient:

public class Patient {
    public String id;
    public String regNumber;
    public String fullName;
    public String symptoms;
    public String diagnosis;

    public Patient() {} // Firebase requirement

    public Patient(String id, String regNumber, String fullName, String symptoms, String diagnosis) {
        this.id = id;
        this.regNumber = regNumber;
        this.fullName = fullName;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
    }
}

b. FirebaseHandler.java (or inside MainActivity.java)

Handles create, read, update operations to/from Firebase.


---

6. UI Layouts

a. Login Screen

Background image via android:background="@drawable/your_image"

EditText fields for Email & Password


b. Patient List Screen

TextView title using custom font (@font/times_new_roman)

ListView showing all patients with color and size customization



---

7. How to Use the App

1. Launch the app and log in.


2. Click “Add New Patient” to input patient details.


3. Use “View/Search Patients” to retrieve or update data.


4. All records are synced in real-time with Firebase.




---

8. Future Enhancements (Optional)

Add video background using VideoView (in raw/ directory).

Implement user authentication (admin, nurse, doctor roles).

Export records as PDF or Excel.

Integrate cloud storage for prescriptions and reports.


