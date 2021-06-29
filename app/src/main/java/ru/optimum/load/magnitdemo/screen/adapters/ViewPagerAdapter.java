package ru.optimum.load.magnitdemo.screen.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ru.optimum.load.magnitdemo.screen.main.details.fragments.MonitoringFragment;
import ru.optimum.load.magnitdemo.screen.main.details.fragments.ReportsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return MonitoringFragment.newInstance();
            case 1:
                return ReportsFragment.newInstance();
        }
        return MonitoringFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
