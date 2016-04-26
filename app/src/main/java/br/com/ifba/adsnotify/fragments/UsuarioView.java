package br.com.ifba.adsnotify.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.ifba.adsnotify.R;

/**
 * Created by Robson on 22/04/2016.
 */
public class UsuarioView extends Fragment {
    public UsuarioView() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.usuario_layout, container, false);
    }
}
