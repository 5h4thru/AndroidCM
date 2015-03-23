package edu.utdallas.dxp141030.contactmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import edu.utdallas.dxp141030.contactmanager.util.Contact;
import edu.utdallas.dxp141030.contactmanager.util.DataUtil;
import edu.utdallas.dxp141030.contactmanager.util.FileUtil;

/**
 * Activity to handle new contacts. Will be invoked when user clicks on add icon in the main activity.
 * @author Durga Sai Preetham Palagummi
 * NetID dxp141030
 * Date 17th March 2015
 * Modified on 23rd March 2015
*/

public class NewContactActivity extends Activity {

    /*Views*/
    private EditText editTextFirstName, editTextLastName,
    editTextPhoneNumber, editTextEmail;
    
    /*Variables*/
    private FileUtil fileUtil;
    private Boolean isEditMode = false;

    private DataUtil dataUtil;
    private Contact oldContact;
    private ArrayList<Contact> contactsList;
    
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        /*Set Content View*/ 
        setContentView(R.layout.activity_newcontact);

        /*Reference Views using Id*/
        editTextFirstName = (EditText) this.findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) this.findViewById(R.id.editTextLastName);
        editTextPhoneNumber = (EditText) this.findViewById(R.id.editTextPhoneNumber);
        editTextEmail = (EditText) this.findViewById(R.id.editTextEmail);
        getActionBar().setIcon(R.drawable.ic_action_action_account_box); //Set Icon in ActionBar
        
        /*Check EditMode to set Variables from Intent Extras*/
        try {
            isEditMode = getIntent().getExtras().getBoolean("isEditMode", false);
            if(isEditMode){
                contactsList = (ArrayList<Contact>) getIntent().getExtras().getSerializable("contactsList");
                int contactIndex = getIntent().getExtras().getInt("contactIndex");
                oldContact = contactsList.get(contactIndex);
                populateFields(oldContact);
                dataUtil = new DataUtil(contactsList);
            }
        } catch(NullPointerException e) {
            isEditMode = false;
            contactsList = new ArrayList<Contact>();
            e.printStackTrace();
        }

        fileUtil = new FileUtil();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Handle action bar item clicks here. The action bar will
         * automatically handle clicks on the Home/Up button, so long
         * as you specify a parent activity in AndroidManifest.xml.
        */
        int id = item.getItemId();
        if (id == R.id.menu_save) {
            /*Validate firstName alone, 
             * check if other fields are present otherwise assign default values to write to file (N/A)
             * Create New Contact 
             * Check If in editMode
             * Write to File*/
            if(editTextFirstName.getText().toString().trim().length() > 0) {
                
                String lastName = " ", phoneNumber = "N/A", email = "N/A";
               
                if(!editTextLastName.getText().toString().trim().equals("")) {
                    lastName = editTextLastName.getText().toString();
                }
                
                if(!editTextPhoneNumber.getText().toString().trim().equals("")) {
                    phoneNumber = editTextPhoneNumber.getText().toString();
                }
                
                if(!editTextEmail.getText().toString().trim().equals("")) {
                    email = editTextEmail.getText().toString();
                }
                
                Contact newContact = new Contact(editTextFirstName.getText().toString(), lastName,
                        phoneNumber, email);
                if(isEditMode) {
                    boolean isFileWritten = fileUtil.writeToFile(dataUtil.updateRecord(oldContact, newContact), false);
                    if(isFileWritten){
                        Toast.makeText(getApplicationContext(), "Updated Contact", Toast.LENGTH_SHORT).show();
                    }
                    
                } else {
                    boolean isFileWritten = fileUtil.writeToFile(newContact.constructString(), true);
                    if(isFileWritten){
                        Toast.makeText(getApplicationContext(), "Created new Contact", Toast.LENGTH_SHORT).show();
                    }
                }
                
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
            } else {
                Toast.makeText(getApplicationContext(), "First Name is mandatory", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    
    /*Populate the Views with values from Intent Extras*/
    private void populateFields(Contact existingContact) {

        editTextFirstName.setText(existingContact.getFirstName());
        
        if(existingContact.getLastName().equals(" ")) {
            editTextLastName.setText("");
        } else {
            editTextLastName.setText(existingContact.getLastName());
        }
        
        if(existingContact.getPhoneNumber().equals("N/A")) {
            editTextPhoneNumber.setText("");
        } else {
            editTextPhoneNumber.setText(existingContact.getPhoneNumber());
        }
        
        if(existingContact.getEmailAddress().equals("N/A")) {
            editTextEmail.setText("");
        } else {
            editTextEmail.setText(existingContact.getEmailAddress());
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        /*Go Back to MainActivity on Back key*/
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    @Override
    protected void onPause() {

        super.onPause();
        finish(); //Finish on Pause
    }


}
