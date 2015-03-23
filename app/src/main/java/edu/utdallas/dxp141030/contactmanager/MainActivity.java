package edu.utdallas.dxp141030.contactmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.utdallas.dxp141030.contactmanager.adapters.CustomAdapter;
import edu.utdallas.dxp141030.contactmanager.util.Contact;
import edu.utdallas.dxp141030.contactmanager.util.FileUtil;

/**
 * This will be the main launcher activity that will get invoked first time the user clicks on the application.
 *
 * @author Durga Sai Preetham Palagummi
 * NetID dxp141030
 * Date 17th March 2015
 * Modified on 23rd March 2015
 */

public class MainActivity extends Activity implements OnItemClickListener {

    /*Views*/
    private ListView listViewContacts;
    private ProgressBar progressBarLoading;
    private TextView textViewNoItems;

    /*Variables*/
    private ArrayList<Contact> contactsList;
    private CustomAdapter customAdapter;
    private FileUtil fileUtil;
    private int selectedContactIndex = 0;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set Layout
        setContentView(R.layout.activity_main);

        /*Reference All views from xml using R.id*/
        listViewContacts = (ListView) this.findViewById(R.id.listView_contacts);
        textViewNoItems = (TextView) this.findViewById(R.id.textViewNoItems);
        progressBarLoading = (ProgressBar) this.findViewById(R.id.progressBarLoading);
        getActionBar().setIcon(R.drawable.ic_action_action_account_box);

        /*Initialize Variables*/
        contactsList = new ArrayList<Contact>();
        fileUtil = new FileUtil();

        /*OnItemClick event handler*/
        listViewContacts.setOnItemClickListener(this);

        /*AsyncTask to fetch Data from file
        * The reason to use AsyncTask is that we can extend the storage online in the future*/
        new FetchData().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu thus adding the items to action bar if exists
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Handle action bar item clicks here. The action bar will
         * automatically handle clicks on the Home/Up button, so long
         * as you specify a parent activity in AndroidManifest.xml.
        */
        int id = item.getItemId();
        if (id == R.id.menu_add) {
            /*Use Intent to navigate to NewContactActivity*/
            Intent newContactIntent = new Intent(this, NewContactActivity.class);
            /*startActivity NewContactActivity*/
            startActivity(newContactIntent);
            /*Finish Current Activity to prevent triggering multiple instances*/
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /*AsyncTask to fetch Data from file*/
    public class FetchData extends AsyncTask<String, Integer, String> {

        /*Does work in Background Thread*/
        @Override
        protected String doInBackground(String... arg0) {
            /*Read from file, parse it 
             * and populate in ArrayList<Contact>*/
            ArrayList<String> contactsString = fileUtil.readFile();
            Contact contact;
            for (String c : contactsString) {
                try {
                    String[] contactItem = c.split(":");
                    contact = new Contact(contactItem[0], contactItem[1], contactItem[2],
                            contactItem[3]);
                    contactsList.add(contact); //Add Item to ArrayList<Contact>
                } catch (Exception e) {
                    //Will not use logger file as of now. Wil implement logger file if time permits
                    e.printStackTrace();
                }
            }
            return null;
        }

        /*After background Thread doInBackground executes*/
        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            /*Check if contactsList size is zero*/
            if (contactsList.size() == 0) {
                textViewNoItems.setVisibility(View.VISIBLE);
            } else {
                /*Set contactsList to Custom Base Adapter*/
                customAdapter = new CustomAdapter(MainActivity.this, contactsList);

                /**
                 * One of the requirement is to sort the list with respect to the first name.
                 * Implement a TreeSet for this purpose.
                 */
                Map<String, Contact> mapForSorting = new TreeMap<String, Contact>();
                for (Contact x : contactsList) {
                    mapForSorting.put(x.getFirstName(), x);
                }
                SortedSet<String> keys = new TreeSet<String>(mapForSorting.keySet());
                contactsList.clear(); // Clear the contact list before adding it to the list to avoid duplicates
                for (String key : keys) {
                    Contact value = mapForSorting.get(key);
                    contactsList.add(value);
                }

                listViewContacts.setAdapter(customAdapter); //Set Adapter to ListView
            }

            progressBarLoading.setVisibility(View.INVISIBLE); //Hide progressBar After setting List items
        }

    }

    /*Handle List Item Click*/
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                            long arg3) {
        selectedContactIndex = arg2;
        actionMode = startActionMode(callbackMenu); //Start Action Mode for ActionBar buttons
        arg0.setSelected(true);
    }

    /*Callback for ActionBar icons click*/
    private ActionMode.Callback callbackMenu = new ActionMode.Callback() {

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //Inflate context menu in ActionBar
            mode.getMenuInflater().inflate(R.menu.context, menu);
            return true;
        }

        /*Handle onActionItemsClicked Event*/
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.menu_edit:
                /*Put isEditMode flag ,contactsList and Index in Bundle 
                 * and send to NewContactActivity to enable edit mode*/
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isEditMode", true);
                    bundle.putInt("contactIndex", selectedContactIndex);
                    bundle.putSerializable("contactsList", contactsList);
                    Intent newContactIntent = new Intent(MainActivity.this, NewContactActivity.class);
                    newContactIntent.putExtras(bundle);
                    startActivity(newContactIntent);
                    break;
                case R.id.menu_delete:
                
                /*Show Alert Dialog to Confirm Delete*/
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Delete Contact") //Set Title
                            .setMessage("Delete this contact?") //Set Message
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //If clicked yes
                                    contactsList.remove(selectedContactIndex);
                                    StringBuilder strBuilder = new StringBuilder();
                                    for (Contact c : contactsList) {
                                        strBuilder.append(c.constructString());
                                    }
                        /*Write String to file*/
                                    boolean isFileWritten = fileUtil.writeToFile(strBuilder.toString(), false);
                                    if (isFileWritten) {
                                        customAdapter.notifyDataSetChanged();
                                        actionMode.finish();
                                        if (contactsList.size() == 0) {
                                            textViewNoItems.setVisibility(View.VISIBLE); //Show NoItems textView if contactsList is empty
                                        }
                                        Toast.makeText(getApplicationContext(), "Deleted Contact", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // If clicked No
                                    actionMode.finish();
                                }
                            }).show();
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onPause() {
        finish(); //Finish Activity onPause
        super.onPause();
    }

}