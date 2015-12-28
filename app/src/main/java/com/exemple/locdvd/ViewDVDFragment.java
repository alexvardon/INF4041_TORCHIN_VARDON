package com.exemple.locdvd;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewDVDFragment extends Fragment{

    TextView txtTitreDVD;
    TextView txtAnneeDVD;
    TextView txtResumeFilm;
    LinearLayout layoutActeurs;
    TextView txtDateDernierVisionnage;
    Button setDateVisionnage;
    DVD dvd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // affectation du fichier de layout
        View view = inflater.inflate(R.layout.activity_viewdvd, null);

        // Obtention des références sur les composants
        txtTitreDVD = (TextView)view.findViewById(R.id.titreDVD);
        txtAnneeDVD= (TextView)view.findViewById(R.id.anneeDVD);
        txtResumeFilm= (TextView)view.findViewById(R.id.resumeFilm);
        layoutActeurs = (LinearLayout)view.findViewById(R.id.layoutActeurs);
        setDateVisionnage = (Button)view.findViewById(R.id.setDateVisionnage);
        txtDateDernierVisionnage =(TextView)view.findViewById(R.id.dateVisionnage);

        long dvdId = getArguments().getLong("dvdId",-1);

        dvd = DVD.getDVD(getActivity(), dvdId);

        setDateVisionnage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        return view;
    }

    private void showDatePicker()  {
        DatePickerDialog datePickerDialog;

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                dvd.setDateVisionnage(calendar.getTimeInMillis());
                dvd.update(getActivity());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String dateValue = String.format(getString(R.string.date_dernier_visionnage_avec_valeur), simpleDateFormat.format(calendar.getTime()));
                txtDateDernierVisionnage.setText( dateValue);
            }
        };

        Calendar calendar = Calendar.getInstance();
        if(dvd.dateVisionnage>0) {
            calendar.setTimeInMillis(dvd.dateVisionnage);
        }

        datePickerDialog = new DatePickerDialog(getActivity(),onDateSetListener, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.show();
    }




    @Override
    public void onResume() {
        super.onResume();

        txtTitreDVD.setText(dvd.getTitre());
        txtAnneeDVD.setText(
                String.format(getString(R.string.annee_de_sortie), dvd.getAnnee()));

        for(String acteur : dvd.getActeurs()) {
            TextView textView = new TextView(getActivity());
            textView.setText(acteur);
            layoutActeurs.addView(textView);
        }
        txtResumeFilm.setText(dvd.getResume());
    }

}
