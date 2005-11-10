/**
 * User: Raymond.Offiah
 * Date: 13-May-2005
 * Time: 09:35:20
 */
package com.anite.borris.services.security.entity.api.permissions;

public class Permissions {

    public static final Permissions ADMINISTRATE = new Permissions("administrate");

    public static final Permissions CREATE_USER = new Permissions("create_user");

    public static final Permissions ABOUT_SCHEME_READ = new Permissions("about_scheme_read");

    public static final Permissions ABOUT_SCHEME_WRITE = new Permissions("about_scheme_write");

    public static final Permissions SCHEME_DETAILS_READ = new Permissions("scheme_details_read");

    public static final Permissions SCHEME_DETAILS_WRITE = new Permissions("scheme_details_write");

    public static final Permissions SCHEME_TRUSTEES_READ = new Permissions("scheme_trustees_read");

    public static final Permissions SCHEME_TRUSTEES_WRITE = new Permissions("scheme_trustees_write");

    public static final Permissions FINANCIAL_INFORMATION_READ = new Permissions("financial_information_read");

    public static final Permissions FINANCIAL_INFORMATION_WRITE = new Permissions("financial_information_write");

    public static final Permissions EMPLOYER_DETAILS_READ = new Permissions("employer_details_read");

    public static final Permissions EMPLOYER_DETAILS_WRITE = new Permissions("employer_details_write");

    public static final Permissions PARTICIPATING_EMPLOYERS_READ = new Permissions("participating_employers_read");

    public static final Permissions PARTICIPATING_EMPLOYERS_WRITE = new Permissions("participating_employers_write");

    public static final Permissions DECLARATION_READ = new Permissions("declaration_read");

    public static final Permissions DECLARATION_WRITE = new Permissions("declaration_write");

    public static final Permissions FINAL_SUBMISSION_READ = new Permissions("final_submission_read");

    public static final Permissions FINAL_SUBMISSION_WRITE = new Permissions("final_submission_write");

    public static final Permissions INVALIDATE_FIELD = new Permissions("invalidate_field");

    /**
     * Permission for Data Entry person to always receive validation messages when every page is viewed
     */
    public static final Permissions ALWAYS_VALIDATE = new Permissions("always_validate");

    /**
     *  Permission to run the validation in the web app
     */
    public static final Permissions VALIDATE_DATA = new Permissions("no_validation");

    // Permission for Data Entry person to submit form after initial data entry
    public static final Permissions SUBMIT_FIRST = new Permissions("submit_first");

    // Permission for Data Entry person to submit form after checking initial data entry
    public static final Permissions SUBMIT_FINAL = new Permissions("submit_final");

    // Permission for Data Entry person to save and send form
    public static final Permissions SAVE_AND_SEND = new Permissions("save_and_send");

    //Permission for Data Entry person that prevents some controls being rendered when in role
    public static final Permissions SAVE_CONTINUE_BUTTON = new Permissions("save_continue_button");
    
    // Permission for Data Entry person to save and send form for reprint
    public static final Permissions SAVE_AND_SEND_FOR_REPRINT = new Permissions("save_and_send_for_reprint");

    // Permission for SEARCH
    public static final Permissions SEARCH = new Permissions("search");

    //  Permission for Data Entry person to set or not the enabling / disabling of components 
    public static final Permissions ENABLE_DISABLE_ACTIVE = new Permissions("enable_disable_active");

    public static final Permissions DATA_ENTRY = new Permissions("data_entry");
    
    /* Permisions for the PSR Admin User */
    
    public static final Permissions CREATE_PS_USER = new Permissions("create_ps_user");
    
    public static final Permissions CHANGE_PERMISSION = new Permissions("change_permisions");
    
    public static final Permissions CHANGE_PASSWORD = new Permissions ("change_password");
    
    public static final Permissions SHOW_ADMIN_LINK	= new Permissions ("show_admin_link");
    
    public static final Permissions DATA_ENTRY_CREATE_USER = new Permissions("data_entry_create_user");
    
    public static final Permissions SAVE_PROGRESS_BUTTON = new Permissions("save_progress_button");
    
    //public static final Permissions NO_VALIDATION = new Permissions("no_Validation");
    
    
    /**
     * Permission to allow users to view the scheme collection page
     */
    public static final Permissions SCHEME_COLLECTION_VIEW = new Permissions("scheme_collection_view");
    
    private final String myName;

    private Permissions(String name) {
        myName = name;
    }

    public String toString() {
        return myName;
    }
}
