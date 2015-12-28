package com.exemple.locdvd;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class ListDVDFragment extends Fragment {


    public interface OnDVDSelectedListener {
        public void onDVDSelected(long dvdId);
    }


    ListView list;

    OnDVDSelectedListener onDVDSelectedListener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listdvd, null);

        list = (ListView)view.findViewById(R.id.main_List);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onDVDSelectedListener!=null )
                    onDVDSelectedListener.onDVDSelected(id);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDVDList();
    }

    @Override
    public void onAttach(Activity activity)  {
        super.onAttach(activity);
        onDVDSelectedListener = (OnDVDSelectedListener)activity;
    }

    public void updateDVDList() {
        ArrayList<DVD> dvdList = DVD.getDVDList(getActivity());
        DVDAdapter dvdAdapter = new DVDAdapter(getActivity(), dvdList);
        list.setAdapter(dvdAdapter);
    }

}
