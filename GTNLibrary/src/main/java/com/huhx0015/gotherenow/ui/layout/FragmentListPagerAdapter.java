package com.huhx0015.gotherenow.ui.layout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.List;

/** ------------------------------------------------------------------------------------------------
 *  [FragmentListPagerAdapter] CLASS
 *  DESCRIPTION: A class that extends upon the FragmentStatePagerAdapter class object, granting the
 *  ability to load slides from a List of Fragments.
 *  ------------------------------------------------------------------------------------------------
 */

public class FragmentListPagerAdapter extends FragmentStatePagerAdapter {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private final List<Fragment> fragments; // Used to store the List of Fragment objects.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // FragmentListPagerAdapter(): Constructor method for the FragmentListPagerAdapter subclass.
    public FragmentListPagerAdapter(final android.support.v4.app.FragmentManager fragmentManager, final List<Fragment> fragments) {
        super(fragmentManager);
        this.fragments = fragments;
    }

    /** PAGER ADAPTER FUNCTIONALITY ____________________________________________________________ **/

    // getCount(): Returns the number of fragments in the PagerAdapter object.
    @Override
    public int getCount() {
        return fragments.size();
    }

    // getItem(): Returns the fragment position in the PagerAdapter object.
    @Override
    public Fragment getItem(final int position) {
        return fragments.get(position);
    }

    // getItemPosition(): Returns the item position in the PagerAdapter object.
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}