package edu.utdallas.dxp141030.contactmanager.util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class to help in data manipulations.
 * @author Durga Sai Preetham Palagummi
 * NetID dxp141030
 * Date 17th March 2015
*/


public class DataUtil {
    
    private ArrayList<Contact> contactsList;
    
    public DataUtil(ArrayList<Contact> contactsList) {
        this.contactsList = contactsList;
    }
    
    /*Update record in File*/
    public String updateRecord(Contact oldContact, Contact newContact){
      StringBuilder strBuilder = new StringBuilder();
      Iterator<Contact> iterator = contactsList.iterator();
      Contact contact;
      
      while(iterator.hasNext()){
        contact = (Contact) iterator.next();
        if(contact.equals(oldContact)){
          contact = newContact;
        }
        strBuilder.append(contact.constructString());
      }
      return strBuilder.toString();
    }
    
    /*Remove Record from File*/
    public String removeRecord(Contact contactToRemove) {
      StringBuilder strBuilder = new StringBuilder();
      Iterator<Contact> iterator = contactsList.iterator();
      Contact contact;
      
      while(iterator.hasNext()){
        contact = (Contact) iterator.next();
        if(contact.equals(contactToRemove)){
          iterator.remove();
          continue;
        }
        strBuilder.append(contact.constructString());
      }
      
      return strBuilder.toString();
    }
}
