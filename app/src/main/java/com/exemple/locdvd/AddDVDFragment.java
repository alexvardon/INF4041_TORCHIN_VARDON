package com.exemple.locdvd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AddDVDFragment extends Fragment{

    EditText editTitreFilm;
    EditText editAnnee;
    EditText editResume;
    Button btnAddActeur;
    Button btnOk;
    LinearLayout addActeursLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // affectation du fichier de layout
        View view = inflater.inflate(R.layout.activity_adddvd, null);

        // Obtention des références sur les composants
        editTitreFilm = (EditText)view.findViewById(R.id.addDVD_titre);
        editAnnee= (EditText)view.findViewById(R.id.addDVD_annee);
        editResume= (EditText)view.findViewById(R.id.addDVD_resume);
        btnAddActeur = (Button)view.findViewById(R.id.addDVD_addActeur);
        btnOk = (Button)view.findViewById(R.id.addDVD_ok);

        addActeursLayout =
                (LinearLayout)view.findViewById(R.id.addDVD_addActeurLayout);

        btnAddActeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addActeur(null);
            }
        });

        // Est-ce une re création suite à une rotation de l'écran ?
        if(savedInstanceState!=null) {
            String [] acteurs = savedInstanceState.getStringArray("acteurs");
            for(String s : acteurs) {
                addActeur(s);
            }
        }
        else {
            // Aucun acteur saisi, on affiche un composant editText vide
            addActeur(null);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        String[] acteurs = new String[addActeursLayout.getChildCount()];
        for(int i=0;i<addActeursLayout.getChildCount();i++) {
            View child = addActeursLayout.getChildAt(i);
            if(child instanceof EditText)
                acteurs[i] = ((EditText)child).getText().toString();
        }
        savedInstanceState.putStringArray("acteurs",acteurs);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void addActeur(String content) {
        EditText editNewActeur = new EditText(getActivity());
        editNewActeur.
                setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                        | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        if(content!=null)
            editNewActeur.setText(content);
        addActeursLayout.addView(editNewActeur);
    }
}
