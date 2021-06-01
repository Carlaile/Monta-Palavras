package com.example.montapalavras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btnOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOk = (Button) findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manipularActivity();
            }
        });
    }

    private void manipularActivity() {
        if(validateFields()) {
            //altera visibilidade na tela
            findViewById(R.id.txtDigLet).setVisibility(View.INVISIBLE);
            findViewById(R.id.txtPalPts).setVisibility(View.VISIBLE);
            findViewById(R.id.txtPalRet).setVisibility(View.VISIBLE);
            findViewById(R.id.txtSobTxt).setVisibility(View.VISIBLE);
            findViewById(R.id.txtLetSob).setVisibility(View.VISIBLE);

            calcularPalavras();
        }
    }

    private void calcularPalavras() {
        EditText txtLetDig = (EditText)findViewById(R.id.txtLetDig);

        TextView txtPalPts = (TextView)findViewById(R.id.txtPalPts);
        TextView txtPalRet = (TextView)findViewById(R.id.txtPalRet);
        TextView txtLetSob = (TextView)findViewById(R.id.txtLetSob);

        //tabela de pontuaçao de caracteres
        char umPonto[] = {'E', 'A', 'I', 'O', 'N', 'R', 'T', 'L', 'S', 'U'};
        char doisPontos[] = {'W', 'D', 'G'};
        char tresPontos[] = {'B', 'C', 'M', 'P'};
        char quatroPontos[] = {'F', 'H', 'V'};
        char oitoPontos[] = {'J', 'X'};
        char dezPontos[] = {'Q', 'Z'};

        //banco de dados de palavras
        String[] dbPalavras = {"Abacaxi", "Manada", "mandar", "porta", "mesa", "Dado", "Mangas", "Ja", "coisas", "radiografia",
                "matematica", "Drogas", "predios", "implementaçao", "computador", "balao", "Xicara", "Tedio",
                "faixa", "Livro", "deixar", "superior", "Profissao", "Reuniao", "Predios", "Montanha", "Botanica",
                "Banheiro", "Caixas", "Xingamento", "Infestaçao", "Cupim", "Premiada", "empanada", "Ratos",
                "Ruido", "Antecedente", "Empresa", "Emissario", "Folga", "Fratura", "Goiaba", "Gratuito",
                "Hidrico", "Homem", "Jantar", "Jogos", "Montagem", "Manual", "Nuvem", "Neve", "Operaçao",
                "Ontem", "Pato", "Pe", "viagem", "Queijo", "Quarto", "Quintal", "Solto", "rota", "Selva",
                "Tatuagem", "Tigre", "Uva", "Ultimo", "Vituperio", "Voltagem", "Zangado", "Zombaria", "Dor"};

        String palavraAnalisada, caracteresNao, caracteresSim;
        String letrasDigitadasAux = txtLetDig.getText().toString().trim();
        String letrasDigitadas = letrasDigitadasAux.toUpperCase();
        String palavraMaxPts = "";
        String sobraMaxPts = "";
        int indicesInvalidos[] = new int[30];
        int indiceMaxPts = 0;
        int ptsPalavra = 0;
        int existeIndice;

        //percorre o dicionario de palavras uma a uma
        for(int indicePalavra = 0; indicePalavra < dbPalavras.length; indicePalavra++){
            palavraAnalisada = dbPalavras[indicePalavra]; //pega a palavra que será analisada
            palavraAnalisada = palavraAnalisada.toUpperCase(); //trata diferença de letras minusculas/maiusculas

            caracteresNao = ""; //limpa caracteres que nao deram match
            caracteresSim = ""; //limpa caracteres que deram match

            //limpa a string que armazena as letras que foram utilizadas para evitar aceitar a mesma letra 2 vezes
            for(int indiceInvZerar = 0; indiceInvZerar < indicesInvalidos.length; indiceInvZerar++){
                indicesInvalidos[indiceInvZerar] = -1;
            }

            //percorre as letras digitadas pelo usuário que foram recebidas
            for(int indice = 0; indice < letrasDigitadas.length(); indice++) {

                //percorre as letras da palavra atual do banco de dados para dar match
                for(int indiceLetra = 0; indiceLetra < palavraAnalisada.length(); indiceLetra++){

                    //verifica se as letras deram match
                    if(palavraAnalisada.charAt(indiceLetra) == letrasDigitadas.charAt(indice)){

                        //verificar se indiceLetra existe nos indicesInvalidos
                        existeIndice = 0;

                        //percorre indices invalidos
                        for(int indiceInv = 0; indiceInv < indicesInvalidos.length; indiceInv++){
                            if(indicesInvalidos[indiceInv] == -1){ //indice no fim, não precisa percorrer mais
                                break;
                            }
                            else if(indicesInvalidos[indiceInv] == indiceLetra){ //a letra já está no indice de utilizadas
                                existeIndice = 1;
                                break;
                            }

                        }
                        //letra igual e nao existe nao existe no indice de letras já utilizadas
                        if(existeIndice == 0){
                            //adiciona no indice na ultima posiçao valida
                            for(int indiceInvSel = 0; indiceInvSel < indicesInvalidos.length; indiceInvSel++){
                                if(indicesInvalidos[indiceInvSel] == -1){
                                    indicesInvalidos[indiceInvSel] = indiceLetra;
                                    break;
                                }
                            }

                            caracteresSim = caracteresSim + palavraAnalisada.charAt(indiceLetra); //grava os caracteres que deram match
                            break;
                        }
                    }
                    //grava o caractere que nao deu match
                    if(indiceLetra == palavraAnalisada.length() - 1){
                        caracteresNao = caracteresNao + letrasDigitadas.charAt(indice);
                    }
                }
            }
            ptsPalavra = 0;
            //pontuar palavra caso esteja ok
            if(caracteresSim.length() == palavraAnalisada.length()){ //verifica se a palavra foi completada
                //percorre a palavra analisada
                for(int indicePontuacao = 0; indicePontuacao < palavraAnalisada.length(); indicePontuacao++){
                    //verifica a tabela de 1 pt
                    for(int indiceTabela = 0; indiceTabela < umPonto.length; indiceTabela++){
                        if(umPonto[indiceTabela] == palavraAnalisada.charAt(indicePontuacao)){
                            ptsPalavra += 1;
                            break;
                        }
                    }
                    //verifica a tabela de 2 pts
                    for(int indiceTabela = 0; indiceTabela < doisPontos.length; indiceTabela++){
                        if(doisPontos[indiceTabela] == palavraAnalisada.charAt(indicePontuacao)){
                            ptsPalavra += 2;
                            break;
                        }
                    }
                    //verifica a tabela de 3 pts
                    for(int indiceTabela = 0; indiceTabela < tresPontos.length; indiceTabela++){
                        if(tresPontos[indiceTabela] == palavraAnalisada.charAt(indicePontuacao)){
                            ptsPalavra += 3;
                            break;
                        }
                    }
                    //verifica a tabela de 4 pts
                    for(int indiceTabela = 0; indiceTabela < quatroPontos.length; indiceTabela++){
                        if(quatroPontos[indiceTabela] == palavraAnalisada.charAt(indicePontuacao)){
                            ptsPalavra += 4;
                            break;
                        }
                    }
                    //verifica a tabela de 8 pts
                    for(int indiceTabela = 0; indiceTabela < oitoPontos.length; indiceTabela++){
                        if(oitoPontos[indiceTabela] == palavraAnalisada.charAt(indicePontuacao)){
                            ptsPalavra += 8;
                            break;
                        }
                    }
                    //verifica a tabela de 10 pts
                    for(int indiceTabela = 0; indiceTabela < dezPontos.length; indiceTabela++){
                        if(dezPontos[indiceTabela] == palavraAnalisada.charAt(indicePontuacao)){
                            ptsPalavra += 10;
                            break;
                        }
                    }
                }
            }

            //verifica se foi a palavra com maior pontuacao para salvar
            if(ptsPalavra > indiceMaxPts) {
                indiceMaxPts = ptsPalavra;
                palavraMaxPts = palavraAnalisada;
                sobraMaxPts = caracteresNao;
            }
        }
        //retorna o resultado para tela
        txtPalPts.setText("Palavra de " + indiceMaxPts + " pontos:");
        txtPalRet.setText(palavraMaxPts);
        txtLetSob.setText(sobraMaxPts);
        txtLetDig.setText("");
    }

    private boolean validateFields() { //somente para garantir que foram digitados caracteres antes de clicar ok
        String field_value;
        boolean is_ok = true;

        EditText txtLetDig = (EditText)findViewById(R.id.txtLetDig);

        field_value = txtLetDig.getText().toString().trim();
        if(field_value.isEmpty()){
            txtLetDig.setError("Digite as letras da jogada");
            is_ok = false;
        }

        return is_ok;
    }
}