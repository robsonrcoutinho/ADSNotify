package br.com.ifba.adsnotify.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.model.Avaliacao;
import br.com.ifba.adsnotify.model.Disciplina;
import br.com.ifba.adsnotify.model.OpcaoResposta;
import br.com.ifba.adsnotify.model.Pergunta;

/**
 * Created by Robson on 27/05/2016.
 */
public class AvaliacaoListAdapter extends BaseAdapter {
    private static final String TAG = AvaliacaoListAdapter.class.getSimpleName();
    private Activity activity;
    private List<Pergunta> perguntas;
    private LayoutInflater inflater;
    private List<OpcaoResposta> listOpcaoRespostas ;
    private RadioButton radioButtons[];
    private static final int PERGUNTA_FECHADA = 1;
    private static final int PERGUNTA_ABERTA = 0;
    private RadioGroup radioGroup;
    private int opcao;
    private EditText editText;


    public AvaliacaoListAdapter(Activity activity, List<Pergunta> perguntas,
                                List<OpcaoResposta> listOpcaoRespostas){
        this.activity = activity;
        this.perguntas = perguntas;
        this.listOpcaoRespostas = listOpcaoRespostas;
    }

    @Override
    public int getCount() {
        return perguntas.size();
    }

    @Override
    public Object getItem(int position) {
        return perguntas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
            convertView = inflater.inflate(R.layout.list_row_avaliacao, null);
            final Context context = convertView.getContext();

        TextView enunciado = (TextView) convertView.findViewById(R.id.idEnunciado);

        Pergunta pergunta = perguntas.get(position);

        enunciado.setText(pergunta.getEnunciado());
        int tamanhoOpcaoResposta = listOpcaoRespostas.size();

        radioButtons = new RadioButton[tamanhoOpcaoResposta];

        LinearLayout ll = (LinearLayout)convertView.findViewById(R.id.btnLay);
        /*LinearLayout.LayoutParams lp = new
        LinearLayout.LayoutParams(GridLayout.LayoutParams. WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT);*/


        long idPergunta = pergunta.getIdPergunta();
        int tipoPergunta = pergunta.getTipoPergunta();

        Log.d("TIPOPERGUNTA", String.valueOf(tipoPergunta));

        if(pergunta.getTipoPergunta() == PERGUNTA_ABERTA){
            editText = new EditText(context);
            editText.setLines(1);
            //editText.setMinLines(5);
            ll.addView(editText);
        }

        for (int i = 0; i < listOpcaoRespostas.size(); i++) {
                if (listOpcaoRespostas.get(i).getIdPergunta().equals(idPergunta)) {
                    Log.d(TAG,"IDs SÂO IGUAIS");
                    Log.d(TAG,"ID"+ idPergunta +"É = " +listOpcaoRespostas.get(i).getIdPergunta());

                    radioGroup = new RadioGroup(context);
                    radioGroup.setOrientation(LinearLayout.VERTICAL);

                    radioButtons[i] = new RadioButton(context);

                    radioGroup.addView(radioButtons[i]);

                    radioButtons[i].setText(listOpcaoRespostas.get(i).getResposta());
                    radioButtons[i].setChecked(false);
                    radioButtons[i].setChecked(false);

                    radioButtons[i].setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            opcao = radioGroup.getCheckedRadioButtonId();
                            Toast.makeText(context, "Clique na linhaa" + opcao, Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });
                    //ll.addView(radioButtons[i], lp);
                    ll.addView(radioGroup);
                }

            }
       /* convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Clique",Toast.LENGTH_SHORT).show();
            }
        });*/
        return convertView;
    }


}
