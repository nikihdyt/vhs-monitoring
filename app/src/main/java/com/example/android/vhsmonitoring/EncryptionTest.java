package com.example.android.vhsmonitoring;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EncryptionTest extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption_test);

        Button btn_encrypt = (Button) findViewById(R.id.encryptButton);
        EditText et_dataInput = (EditText) findViewById(R.id.dataInput);
        TextView tv_dataEncrypted = (TextView) findViewById(R.id.dataEncrypted);
        TextView tv_dataDecrypted = (TextView) findViewById(R.id.dataDecrypted);

        btn_encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Encryption dataEncrypt = new Encryption();

                    // encode data
                    String data1 = dataEncrypt.encrypt(et_dataInput.getText().toString());

                    // decode data
                    String data2 = dataEncrypt.decrypt(data1);

                    // update textview
                    tv_dataEncrypted.setText(data1);
                    tv_dataDecrypted.setText(data2);
                }
            }
        });
    }
}
