package com.example.tfgviravidam.features.app.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tfgviravidam.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PasswordFragment extends Fragment {

    Button btn;
    TextView textView;
    TextView txtError;

    String nombre,fecha,phone,mail;
    EditText txtPass;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password, container, false);
        btn = view.findViewById(R.id.btnPass);
        textView = view.findViewById(R.id.txtPass);
        txtError=view.findViewById(R.id.txtError);

        textView.requestFocus();
        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        textView.addTextChangedListener(textWatcher);
        txtPass=view.findViewById(R.id.txtPass);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle datosRecuperados = getArguments();
                nombre = datosRecuperados.getString("nombre");
                fecha = datosRecuperados.getString("fecha");
                phone = datosRecuperados.getString("phone");
                mail = datosRecuperados.getString("mail");

                Animation fuera = AnimationUtils.loadAnimation(getContext(),R.anim.to_left);
                Animation dentro = AnimationUtils.loadAnimation(getContext(),R.anim.to_rigth);

                Bundle datosAEnviar = new Bundle();
                datosAEnviar.putString("nombre",nombre);
                datosAEnviar.putString("fecha",fecha);
                datosAEnviar.putString("phone",phone);
                datosAEnviar.putString("mail",mail);
                datosAEnviar.putString("pass",txtPass.getText().toString().trim());

                Fragment fragmento = new UsernameFragment();
                fragmento.setArguments(datosAEnviar);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.to_rigth,R.anim.to_left);
                fragmentTransaction.replace(R.id.fragmentContainerView, fragmento);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //Navigation.findNavController(view).navigate(R.id.action_passwordFragment_to_usernameFragment);
            }
        });
        return view;
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String text = textView.getText().toString().trim();

            String regex = "^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,16}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            boolean isMatch = matcher.matches();
            if(isMatch){
                btn.setEnabled(!text.isEmpty());
                txtError.setText("");
            }else{
                txtError.setText("Introduce una contraseña válida");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}