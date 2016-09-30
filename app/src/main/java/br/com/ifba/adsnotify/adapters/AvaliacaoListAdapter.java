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
import java.util.ArrayList;
import java.util.List;
import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.model.OpcaoResposta;
import br.com.ifba.adsnotify.model.Pergunta;
import br.com.ifba.adsnotify.model.Resposta;

/**
 * Classe adapter usada para mostragem de avaliação na view
 * @Author Robson Coutinho
 * @version 1.0
 * @since 27/05/2016.
 */

public class AvaliacaoListAdapter extends BaseAdapter {
    private static final String TAG = AvaliacaoListAdapter.class.getSimpleName();
    private Activity activity;
    private List<Pergunta> perguntas;
    private LayoutInflater inflater;
    private List<OpcaoResposta> listOpcaoRespostas;
    private RadioButton radioButtons[];
    private List<RadioButton> listRadioButtons;
    private static final int PERGUNTA_ABERTA = 0;
    private LinearLayout ll;
    private RadioGroup radioGroup;
    private EditText editText;
    private Context context;


    public AvaliacaoListAdapter(Activity activity, List<Pergunta> perguntas,
                                List<OpcaoResposta> listOpcaoRespostas) {
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

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_avaliacao, null);
        context = convertView.getContext();

        TextView enunciado = (TextView) convertView.findViewById(R.id.idEnunciado);

        final Pergunta pergunta = perguntas.get(position);

        enunciado.setText(pergunta.getEnunciado());
        int tamanhoOpcaoResposta = listOpcaoRespostas.size();

        radioButtons = new RadioButton[tamanhoOpcaoResposta];

        ll = (LinearLayout) convertView.findViewById(R.id.btnLay);


        final int idPergunta = pergunta.getIdPergunta();
        Log.d("ID Pergunta: ", String.valueOf(idPergunta));
        int tipoPergunta = pergunta.getTipoPergunta();

        Log.d("TIPO PERGUNTA", String.valueOf(tipoPergunta));


        radioGroup = new RadioGroup(context);
        radioGroup.setOrientation(LinearLayout.VERTICAL);

        listRadioButtons = new ArrayList<>();

        /*
        * Se pergunta for fechada, geração de RadioButtons dinamicamente, associando pergunta e
        * suas respostas
        * */
        if (listOpcaoRespostas != null) {

            for (int i = 0; i < listOpcaoRespostas.size(); i++) {
                if (listOpcaoRespostas.get(i).getIdPergunta() == idPergunta) {
                    Log.d(TAG, "ID" + idPergunta + " É = " + listOpcaoRespostas.get(i).getIdPergunta());
                    radioButtons[i] = new RadioButton(context);
                    radioButtons[i].setText(listOpcaoRespostas.get(i).getResposta());
                    radioButtons[i].setId(radioButtons[i].hashCode());
                    radioButtons[i].setChecked(false);
                    listRadioButtons.add(radioButtons[i]);
                }


            }
            for (int i = 0; i < listRadioButtons.size(); i++) {
                radioGroup.addView(listRadioButtons.get(i));
                if (listRadioButtons.get(i).isChecked()) {
                    String text = listRadioButtons.get(i).getText().toString();
                    // Log.d("VALOR BUTTON: ", text);
                }

                if (i == 0) {
                    setData(false, null);
                }
            }

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                    if (null != rb && checkedId > -1) {
                        //Toast.makeText(context, rb.getText(), Toast.LENGTH_SHORT).show();
                        String respostaCampo = (String) rb.getText();
                        if (respostaCampo != null) {
                            Resposta resp = new Resposta();
                            resp.setIdPerguntaRespondida(idPergunta);
                            resp.setRespostaUsuário(respostaCampo);
                            resp.setIdentificador(String.valueOf(rb.getId()));
                            setData(true, resp);
                        }
                    }
                }
            });
            ll.addView(radioGroup);
        }
        /*****************************************************************************************/

        /*
        * Condicional que verifica se pergunta é aberta,
        * se for, gero um EditText para a pergunta e aguardo resposta dada pelo usuário
        * */

        if (pergunta.getTipoPergunta() == PERGUNTA_ABERTA) {
            final Resposta resposta = new Resposta();

           editText= (EditText) convertView.findViewById(R.id.editDinamico);
           editText.setVisibility(View.VISIBLE);

            editText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                @Override
                public void onFocusChange(View v, boolean hasFocus){
                    String entrada;
                    if(!hasFocus){
                        editText= (EditText) v;
                        entrada= editText.getText().toString();
                        resposta.setRespostaUsuário(editText.getText().toString());
                        resposta.setIdentificador(String.valueOf(resposta.hashCode()));
                        resposta.setIdPerguntaRespondida(pergunta.getIdPergunta());
                        Log.d("RESPOSTA", entrada);

                        if (resposta.getRespostaUsuário() == null || resposta.getRespostaUsuário().isEmpty()) {
                            setData(false, null);
                        } else {
                            setData(true, resposta);
                        }
                    }
                    }
                });


            if (editText == null || editText.length()<2) {
                setData(false, null);
            }
        }
        return convertView;
    }

    /*
    * Metodo usada para passagem de parametros para @AvaliacaoActivity
    * Passa respostas em tempo de execuução
    * */
    public void setData(boolean data, Resposta resposta) {
    }
}





