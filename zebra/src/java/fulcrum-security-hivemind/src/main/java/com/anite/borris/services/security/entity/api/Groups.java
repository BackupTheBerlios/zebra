/**
 * User: Raymond.Offiah
 * Date: 18-May-2005
 * Time: 15:51:22
 */
package com.anite.borris.services.security.entity.api;

public class Groups {

    
//    public static final Groups ADMINISTRATORS = new Groups("administrators");

    public static final Groups TPR_ADMIN = new Groups("tpr_admin");
    
    public static final Groups TPR_USER = new Groups("tpr_user");
    
    /**
     * data entry admin group to add, delete users etch
     */
    public static final Groups DATA_ENTRY_ADMIN = new Groups("data_entry_admin");
    
    /**
     * Group for Data Entry they can edit all pages but do no validation
     */ 
    public static final Groups DATA_ENTRY = new Groups("data_entry");
    
    public static final Groups DATA_VALIDATOR = new Groups("data_validator");

    public static final Groups PENSIONSCHEME_ADMIN = new Groups("pensionscheme_admin");

    public static final Groups PENSIONSCHEME_USER = new Groups("pensionscheme_user");

    public static final Groups ADMINISTRATOR = new Groups("administrator");
    
    public static final Groups TEMP_USER = new Groups("temp_user");
     

    /**
     * Data validation users for the call centre
     */
    public static final Groups DATA_VALIDATION = new Groups("data_validation");
    
    public static final Groups VALIDATION_CHECKER = new Groups("validation_checker");

    private final String myName; // for debug only

    private Groups(String name) {
        myName = name;
    }

    public String toString() {
        return myName;
    }

}