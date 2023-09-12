package com.example.tfgviravidam.features.app.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tfgviravidam.R;
import com.example.tfgviravidam.databinding.FragmentBirthdayBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BirthdayFragment extends Fragment {

    EditText day,month,year;
    Button btnNext;
    TextView txtError;
    String nombre;

    private FragmentBirthdayBinding binding;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We set the listener on the child fragmentManager

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_birthday, container, false);
        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        Bundle datosRecuperados = getArguments();
        nombre = datosRecuperados.getString("nombre");
        day=view.findViewById(R.id.txtDay);
        month=view.findViewById(R.id.txtMonth);
        year=view.findViewById(R.id.txtYear);
        btnNext=view.findViewById(R.id.btnBirth);
        txtError=view.findViewById(R.id.txtError);

        day.requestFocus();

        day.addTextChangedListener(textWatcherDay);
        month.addTextChangedListener(textWatcherMonth);
        year.addTextChangedListener(textWatcherYear);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle datosRecuperados = getArguments();
                nombre = datosRecuperados.getString("nombre");

                String fecha= day.getText()+"-"+month.getText()+"-"+year.getText();

                Bundle datosAEnviar = new Bundle();
                datosAEnviar.putString("nombre",nombre);
                datosAEnviar.putString("fecha",fecha);
                Fragment fragmento = new PhoneFragment();
                fragmento.setArguments(datosAEnviar);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.to_rigth,R.anim.to_left);
                fragmentTransaction.replace(R.id.fragmentContainerView, fragmento);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                /*Navigation.findNavController(view).navigate(R.id.action_birthdayFragment_to_phoneFragment);
                im.hideSoftInputFromWindow(textView.getWindowToken(), 0);*/

            }
        });
        return view;

    }

    private TextWatcher textWatcherDay = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(day.getText().toString().length()==2){
                month.requestFocus();
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    private TextWatcher textWatcherMonth = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(month.getText().toString().length()==2){
                year.requestFocus();
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {


        }
    };
    private TextWatcher textWatcherYear = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if(year.getText().toString().length()==4){
                String fecha= day.getText()+"-"+month.getText()+"-"+year.getText();
                String regex = "^(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-(19|20)\\d{2}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(fecha);
                boolean isMatch = matcher.matches();
                if (isMatch){
                    btnNext.setEnabled(true);
                }else {
                    txtError.setText("Introduce una fecha de nacimiento valida");
                }

            }




        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}