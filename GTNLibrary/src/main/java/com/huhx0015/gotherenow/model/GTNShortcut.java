package com.huhx0015.gotherenow.model;

/**
 * -------------------------------------------------------------------------------------------------
 * [GTNShortcut] CLASS
 * PROGRAMMER: Michael Yoon Huh (HUHX0015)
 * DESCRIPTION: This is an object class that references the shortcut data, including the shortcut
 * name, address, type, image, and ID.
 * -------------------------------------------------------------------------------------------------
 */

public class GTNShortcut {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // SHORTCUT VARIABLES
    private long id; // Shortcut ID for the SQL database table.
    private long shortcut_image_id; // Shortcut image resource ID.
    private String shortcut_name; // Shortcut name.
    private String shortcut_address; // Shortcut address.
    private String shortcut_type; // Shortcut type.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // GTNShortcut(): Initializes the GTNShortcut object with default values.
    public GTNShortcut() {
        id = 0;
        shortcut_name = "";
        shortcut_address = "";
        shortcut_type = "d";
        shortcut_image_id = 0;
    }

    // GTNShortcut(): Initializes the GTNShortcut object with set parameters.
    public GTNShortcut(long Id, String name, String address, String type, int imageId) {
        super();
        id = Id;
        shortcut_name = name;
        shortcut_address = address;
        shortcut_type = type;
        shortcut_image_id = imageId;
    }

    /** SHORTCUT FUNCTIONALITY _________________________________________________________________ **/

    // toString(): This method is used by the ArrayAdapter in the ListView to return the name of the
    // shortcut.
    @Override
    public String toString() {
        return shortcut_name;
    }

    /** GET / SET FUNCTIONALITY ________________________________________________________________ **/

    // getId(): Returns the ID value for the table object.
    public long getId() { return id; }

    // getId(): Returns the shortcut image ID value.
    public long getShortcutImageId() { return shortcut_image_id; }

    // getShortcutName(): Returns the shortcut name.
    public String getShortcutName() { return shortcut_name; }

    // getShortcutAddress(): Returns the shortcut address.
    public String getShortcutAddress() { return shortcut_address; }

    // getShortcutType(): Returns the shortcut type.
    public String getShortcutType() { return shortcut_type; }

    // setId(): Sets the ID value for the table object.
    public void setId(long id) { this.id = id; }

    // setShortcutAddress(): Sets the shortcut address.
    public void setShortcutAddress(String address) { shortcut_address = address; }

    // setId(): Sets the shortcut image ID value.
    public void setShortcutImageId(long id) { shortcut_image_id = id; }

    // setShortcutName(): Sets the shortcut name.
    public void setShortcutName(String name) { shortcut_name = name; }

    // setShortcutType(): Sets the shortcut type.
    public void setShortcutType(String type) { shortcut_type = type; }
}