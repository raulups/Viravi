package com.example.tfgviravidam.features.app.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfgviravidam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class PassLoginFragment extends Fragment {

    TextView passwordUsuario;
    String emailUsuario;
    Button btn;

    FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pass_login, container, false);
        btn = view.findViewById(R.id.btnPassLogin);
        passwordUsuario = view.findViewById(R.id.txtPassLogin);
        passwordUsuario.requestFocus();

        firebaseAuth= FirebaseAuth.getInstance();
        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        passwordUsuario.addTextChangedListener(textWatcher);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle datosRecuperados = getArguments();
                emailUsuario = datosRecuperados.getString("email");

                firebaseAuth.signInWithEmailAndPassword(emailUsuario,passwordUsuario.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getActivity(), ViraviActivity.class));
                        }else{
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            dameToastDeError(errorCode);
                        }
                    }
                });
                im.hideSoftInputFromWindow(passwordUsuario.getWindowToken(), 0);
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

            String text = passwordUsuario.getText().toString().trim();
            btn.setEnabled(!text.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void dameToastDeError(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(getActivity(), "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(getActivity(), "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(getActivity(), "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(getActivity(), "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                error("La dirección de correo electrónico está mal formateada.");
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(getActivity(), "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                passwordUsuario.setError("La contraseña es incorrecta ");
                passwordUsuario.requestFocus();
                passwordUsuario.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(getActivity(), "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente...", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(getActivity(),"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(getActivity(), "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(getActivity(), "La dirección de correo electrónico ya está siendo utilizada por otra cuenta...", Toast.LENGTH_LONG).show();
                error("La dirección de correo electrónico ya está siendo utilizada por otra cuenta...");
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(getActivity(), "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(getActivity(), "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(getActivity(), "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(getActivity(), "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                error("No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.");
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(getActivity(), "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(getActivity(), "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(getActivity(), "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                passwordUsuario.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                passwordUsuario.requestFocus();
                break;

        }

    }

    public void error(String error) {

        Fragment fragmento = new UserLoginFragment();
        Bundle datosAEnviar = new Bundle();
        datosAEnviar.putString("emailerror", error);
        fragmento.setArguments(datosAEnviar);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.to_rigth,R.anim.to_left);
        fragmentTransaction.replace(R.id.fragmentContainerView, fragmento);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}