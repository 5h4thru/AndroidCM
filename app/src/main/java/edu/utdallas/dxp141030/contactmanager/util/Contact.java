package edu.utdallas.dxp141030.contactmanager.util;

import java.io.Serializable;

/**
* This is the Model class for each contact that will be saved in the application.
 * @author Durga Sai Preetham Palagummi
 * NetID dxp141030
 * Date 17th March 2015
*/


/* Extends serializable class */
public class Contact implements Serializable {
    
    /**
     * Long variable to store unique IDs
     */
    private static final long serialVersionUID = 1L;
    
    /*Variables for each field that will be entered.
    * For simplicity let all the variables be String type.
    */
    private String firstName, lastName, phoneNumber, emailAddress;
    
    /*Constructor*/
    public Contact(String firstName, String lastName, String phoneNumber,
            String emailAddress) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    /*Getters and Setters*/
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    /*Construct String from Values to write to file*/
    public String constructString() {
        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append(firstName + ":")
        .append(lastName + ":")
        .append(phoneNumber + ":")
        .append(emailAddress + ":")
        .append(System.lineSeparator());
        
        return stringBuilder.toString();
    }
}
