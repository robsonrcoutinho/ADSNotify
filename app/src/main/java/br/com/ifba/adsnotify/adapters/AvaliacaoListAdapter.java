package br.com.ifba.adsnotify.adapters;

import android.app.Activity;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.ifba.adsnotify.R;
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
    private List<RadioButton> listRadioButtons;
    private static final int PERGUNTA_FECHADA = 1;
    private static final int PERGUNTA_ABERTA = 0;
    private LinearLayout ll;
    private RadioGroup radioGroup;
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

        ll = (LinearLayout)convertView.findViewById(R.id.btnLay);


        long idPergunta = pergunta.getIdPergunta();
        int tipoPergunta = pergunta.getTipoPergunta();

        Log.d("TIPOPERGUNTA", String.valueOf(tipoPergunta));

        if(pergunta.getTipoPergunta() == PERGUNTA_ABERTA){
            editText = new EditText(context);
            editText.setLines(1);
            ll.addView(editText);
        }

        radioGroup = new RadioGroup(context);
        radioGroup.setOrientation(LinearLayout.VERTICAL);

        listRadioButtons = new ArrayList<>();

        for (int i = 0; i < listOpcaoRespostas.size(); i++) {
                if (listOpcaoRespostas.get(i).getIdPergunta() == idPergunta) {
                    Log.d(TAG, "IDs SÂO IGUAIS");
                    Log.d(TAG, "ID" + idPergunta + "É = " + listOpcaoRespostas.get(i).getIdPergunta());

                    radioButtons[i] = new RadioButton(context);
                    radioButtons[i].setText(listOpcaoRespostas.get(i).getResposta());
                    radioButtons[i].setId(radioButtons[i].hashCode());
                    radioButtons[i].setChecked(false);
                    listRadioButtons.add(radioButtons[i]);
                }


            }
        for(int i =0; i<listRadioButtons.size(); i++){
            radioGroup.addView(listRadioButtons.get(i));
            if(listRadioButtons.get(i).isChecked()){
                String text = listRadioButtons.get(i).getText().toString();
                Log.d("VALOR BUTTON: ",text);
            }
            Log.d("VALOR BUTTON FORA: ",listRadioButtons.get(i).getText().toString());
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);

                if (null != rb && checkedId > -1) {
                    Toast.makeText(context, rb.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        ll.addView(radioGroup);

        return convertView;
    }


}
