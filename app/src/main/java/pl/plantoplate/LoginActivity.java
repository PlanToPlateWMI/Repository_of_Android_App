package pl.plantoplate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import pl.plantoplate.databinding.LoginPageBinding;
import pl.plantoplate.ui.registration.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private LoginPageBinding login_view;

    private TextInputEditText email_input;
    private TextInputEditText password_input;
    private Button remind_password_button;
    private Button signin_button;
    private Button create_account_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login_view = LoginPageBinding.inflate(getLayoutInflater());
        setContentView(login_view.getRoot());

        // define ui elements
        create_account_button = login_view.buttonZalozKonto;

        // set listeners
        create_account_button.setOnClickListener(v -> createAccount());

    }

    public void createAccount() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}

