package com.wruzjan.ihg;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MeasurementListFragment extends ListFragment {

    private final String[] tutorialList = {"RED","GREEN","BLUE",};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter(getActivity().
                getApplicationContext(), android.R.layout.simple_list_item_activated_1, tutorialList));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        MeasurementDetailsFragment viewer = (MeasurementDetailsFragment) getFragmentManager().findFragmentById(R.id.viewers);
        if (viewer == null || !viewer.isInLayout()) {
            Intent launchingIntent = new Intent(getActivity(),MeasurementDetailsActivity.class);
            launchingIntent.putExtra("COLOR_TYPE", tutorialList[position]);
            startActivity(launchingIntent);
        } else {
            viewer.updateColor(tutorialList[position]);
        }
    }
}