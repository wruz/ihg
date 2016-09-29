package com.wruzjan.ihg;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MeasurementDetailsFragment extends Fragment {

    TextView mtext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Intent launchingIntent = getActivity().getIntent();
        String color = launchingIntent.getStringExtra("COLOR_TYPE");
        View viewer = (View) inflater.inflate(R.layout.measurement, container, false);
        mtext  = (TextView) viewer.findViewById(R.id.textSample);
        updateColor(color);
        return viewer;
    }

    /**
     * for updating the color from the given input
     * @param color
     */
    public void updateColor(String color)
    {
        if(color != null)
            if(color.equals("RED"))
                mtext.setBackgroundColor(Color.RED);
            else if(color.equals("BLUE"))
                mtext.setBackgroundColor(Color.BLUE);
            else if(color.equals("GREEN"))
                mtext.setBackgroundColor(Color.GREEN);
    }
}