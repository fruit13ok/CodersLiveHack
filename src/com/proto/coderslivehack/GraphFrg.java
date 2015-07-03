package com.proto.coderslivehack;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GraphFrg extends Fragment
{
	private int index;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		Bundle data = getArguments();
		index = data.getInt("idx");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.frg_graph, null);
		TextView tv = (TextView) v.findViewById(R.id.tvGraph);
		tv.setText("frg" + (index + 1));
		
		return v;
	}
}