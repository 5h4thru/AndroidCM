package edu.utdallas.dxp141030.contactmanager.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.utdallas.dxp141030.contactmanager.R;
import edu.utdallas.dxp141030.contactmanager.util.Contact;

/**
 * Custom adapter class that aids in inflating the list items.
 * @author Durga Sai Preetham Palagummi
 * NetID dxp141030
 * Date 17th March 2015
 * Modified on 22nd March 2015
*/


/*CustomAdapter class extends BaseAdapter*/
public class CustomAdapter extends BaseAdapter {

    /*Variables*/
    private ArrayList<Contact> contactsList;
    private LayoutInflater inflater;
    
    public CustomAdapter(Activity activity, ArrayList<Contact> contactsList) {
       /*Initialize*/
       this.contactsList = contactsList;
       inflater = (LayoutInflater) activity
               .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return contactsList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    /*Class with various views of the listItem*/
    public static class ViewHolder {
        public TextView textViewFirstName, textViewLastName, 
            textViewPhoneNumber, textViewEmail;
    }
    
    @SuppressLint("InflateParams")
    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        View vi = arg1;
        final ViewHolder holder;
        
        if (arg1 == null) {
            vi = inflater.inflate(R.layout.list_item, null); //Inflate Item with Custom layout
            holder = new ViewHolder();
            //Initialize Views
            holder.textViewFirstName = (TextView) vi
                    .findViewById(R.id.textViewFirstName);
            holder.textViewLastName = (TextView) vi.findViewById(R.id.textViewLastName);
            holder.textViewPhoneNumber = (TextView) vi.findViewById(R.id.textViewPhoneNumber);
            holder.textViewEmail = (TextView) vi.findViewById(R.id.textViewEmail);
            
            vi.setTag(holder); //Set Tag to holder for each ListItem view
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        
        /*Set Values to Views*/
        holder.textViewFirstName.setText(contactsList.get(arg0).getFirstName());
        holder.textViewLastName.setText(contactsList.get(arg0).getLastName());
        try {
            /*Format Phone Number using PhoneNumberUtils*/
            holder.textViewPhoneNumber.setText(PhoneNumberUtils.formatNumber(contactsList.get(arg0).getPhoneNumber()));
        } catch (Exception e) {
            //No time for a logger file. Will implement later if time permits
            e.printStackTrace();
        }
        holder.textViewEmail.setText(contactsList.get(arg0).getEmailAddress());

        return vi;
    }
}
