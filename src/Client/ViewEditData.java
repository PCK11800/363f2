import javax.swing.*;

public class ViewEditData {

    public static void main(String[] args) {
        // Example on how to feed it data (just un-comment it):

        // populateFields("DUMMY DATA", "DUMMY DATA", "DUMMY DATA", "DUMMY DATA", "DUMMY DATA", "DUMMY DATA", "DUMMY DATA");
    }

    public static void populateFields(String height, String weight, String doctorNotes, String pastConditions, String fullName, String dateOfBirth, String address)
    {
        JFrame frame = new JFrame("Records data");
        frame.setSize(620,380);

        // PATIENT'S PERSONAL INFO:

        JLabel personalTitle;
        personalTitle = new JLabel();
        personalTitle.setText("Patient personal information");
        personalTitle.setBounds(20,20,200, 30);
        frame.add(personalTitle);


        JLabel heightLabel = new JLabel();
        heightLabel.setText("Height");
        heightLabel.setBounds(20, 30, 100, 110);
        frame.add(heightLabel);

        JTextField heightField = new JTextField();
        heightField.setBounds(120, 70, 130, 30);
        heightField.setText(height);
        frame.add(heightField);


        JLabel weightLabel = new JLabel();
        weightLabel.setText("Weight");
        weightLabel.setBounds(20, 70, 100, 110);
        frame.add(weightLabel);

        JTextField weightField = new JTextField();
        weightField.setBounds(120, 110, 130, 30);
        weightField.setText(weight);
        frame.add(weightField);


        JLabel notesLabel = new JLabel();
        notesLabel.setText("Doctor notes");
        notesLabel.setBounds(20, 110, 100, 110);
        frame.add(notesLabel);

        JTextField notesField = new JTextField();
        notesField.setBounds(120, 150, 130, 30);
        notesField.setText(doctorNotes);
        frame.add(notesField);


        JLabel conditionsLabel = new JLabel();
        conditionsLabel.setText("Past conditions");
        conditionsLabel.setBounds(20, 150, 100, 110);
        frame.add(conditionsLabel);

        JTextField conditionsField = new JTextField();
        conditionsField.setBounds(120, 190, 130, 30);
        conditionsField.setText(pastConditions);
        frame.add(conditionsField);


        JButton updateButton = new JButton("Save changes");
        updateButton.setBounds(20,250,140, 40);
        frame.add(updateButton);


        // PATIENT'S MEDICAL INFO:

        JLabel medicalTitle;
        medicalTitle = new JLabel();
        medicalTitle.setText("Patient medical data");
        medicalTitle.setBounds(300,20,200, 30);
        frame.add(medicalTitle);


        JLabel nameLabel = new JLabel();
        nameLabel.setText("Full name");
        nameLabel.setBounds(300, 30, 100, 110);
        frame.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(400, 70, 130, 30);
        nameField.setText(fullName);
        frame.add(nameField);


        JLabel birthLabel = new JLabel();
        birthLabel.setText("Date of birth");
        birthLabel.setBounds(300, 70, 100, 110);
        frame.add(birthLabel);

        JTextField birthField = new JTextField();
        birthField.setBounds(400, 110, 130, 30);
        birthField.setText(dateOfBirth);
        frame.add(birthField);


        JLabel addressLabel = new JLabel();
        addressLabel.setText("Address");
        addressLabel.setBounds(300, 110, 100, 110);
        frame.add(addressLabel);

        JTextField addressField = new JTextField();
        addressField.setBounds(400, 150, 130, 30);
        addressField.setText(dateOfBirth);
        frame.add(addressField);


        JLabel emailLabel = new JLabel();
        emailLabel.setText("Email address");
        emailLabel.setBounds(300, 150, 100, 110);
        frame.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(400, 190, 130, 30);
        emailField.setText(address);
        frame.add(emailField);


        JTextArea informUser = new JTextArea();
        informUser.setBounds(0, 0, 100, 100);
        frame.add(informUser);

        informUser.setOpaque(false);
        informUser.setEditable(false);

        updateButton.addActionListener(actionEvent -> {
            informUser.setText("Data updated!");

            String newHeight = heightField.getText();
            String newWeight = weightField.getText();
            String newDoctorNotes = notesField.getText();
            String newPastConditions = conditionsField.getText();
            String newFullName = nameField.getText();
            String newDateOfBirth = birthField.getText();
            String newAddress = addressField.getText();

            // ^ Do anything with this new data...
        });

        frame.setVisible(true);
    }
}