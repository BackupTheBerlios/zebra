/**
 * User: Raymond.Offiah
 * Date: 18-May-2005
 * Time: 15:30:31
 */
package com.anite.borris.services.security.entity.api;

public class Roles {

    public static final Roles ADMINISTRATOR = new Roles("administrator");

    /* Data entry User */

    public static final Roles DATA_ENTRY_USER_ADMIN = new Roles("data_entry_user_admin");

    public static final Roles DATA_ENTRY = new Roles("data_entry");
    
    public static final Roles READ = new Roles("read");

    public static final Roles WRITE = new Roles("write");

    public static final Roles SEARCH = new Roles("search");
    
    public static final Roles SCHEME_COLLECTION = new Roles("scheme_collection");

    public static final Roles DATA_ENTRY_SUBMIT = new Roles("data_entry_submit");

    /* Data Validator */

    public static final Roles DATA_VALIDATOR = new Roles("data_validator");

    /* Pension User */
    
    // ??? PENSION_SCHEME_USER
    public static final Roles PENSION_SCHEME_USER_ADMIN = new Roles("pension_scheme_user_admin");

    public static final Roles PENSION_SCHEME_USER = new Roles("pension_scheme_user");

    //  Role for a user to test the validation works
    public static final Roles VALIDATION_CHECKER = new Roles("validation_checker");

    private final String myName; // for debug only

    private Roles(String name) {
        myName = name;
    }

    public String toString() {
        return myName;
    }
}
