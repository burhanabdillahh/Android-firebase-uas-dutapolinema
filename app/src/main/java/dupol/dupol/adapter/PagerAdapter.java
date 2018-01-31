package dupol.dupol.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import dupol.dupol.fragment.InfoFragment;
import dupol.dupol.fragment.PrestasiFragment;
import dupol.dupol.fragment.ProfilFragment;

/**
 * Created by Toshiba C55B on 1/28/2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int notabs;
    public PagerAdapter(FragmentManager fm, int ntabs) {
        super(fm);
        this.notabs = ntabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                ProfilFragment profilFragment = new ProfilFragment();
                return profilFragment;
            case 1 :
                InfoFragment infoFragment = new InfoFragment();
                return infoFragment;
            case 2 :
                PrestasiFragment prestasiFragment = new PrestasiFragment();
                return prestasiFragment;
                default:
                    return null;
        }

    }

    @Override
    public int getCount() {

        return notabs;
    }
}
