package com.example.zakaygoldv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText editWeight, editValue;
    RadioGroup radioGroupType;
    RadioButton radioKeep, radioWear;
    Button btnCalculate, btnReset;
    TextView txtTotalValue, txtPayable, txtZakat, txtGoldZakat;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Handle window insets for notch/status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editWeight = findViewById(R.id.editWeight);
        editValue = findViewById(R.id.editValue);
        radioGroupType = findViewById(R.id.radioGroupType);
        radioKeep = findViewById(R.id.radioKeep);
        radioWear = findViewById(R.id.radioWear);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.buttonReset);
        txtTotalValue = findViewById(R.id.txtTotalValue);
        txtPayable = findViewById(R.id.txtPayable);
        txtZakat = findViewById(R.id.txtZakat);
        txtGoldZakat = findViewById(R.id.txtGoldZakat);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateZakat();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(item.getItemId() == R.id.action_share){
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "Download my app: https://github.com/syamilshawn/Zakat-GoldV2");
            startActivity(Intent.createChooser(shareIntent,"Share"));
            return true;

        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void calculateZakat() {
        String weightStr = editWeight.getText().toString();
        String valueStr = editValue.getText().toString();

        if (weightStr.isEmpty() || valueStr.isEmpty()) {
            Toast.makeText(this, "Please fill all inputs", Toast.LENGTH_SHORT).show();
            return;
        }

        if (radioGroupType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select gold type", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            double value = Double.parseDouble(valueStr);

            double uruf = 0;
            if (radioKeep.isChecked()) {
                uruf = 85;
            } else if (radioWear.isChecked()) {
                uruf = 200;
            }

            double totalGoldValue = weight * value;
            double payableWeight = weight - uruf;

            if (payableWeight < 0) {
                payableWeight = 0;
            }

            double zakatPayable = payableWeight * value;
            double totalZakat = zakatPayable * 0.025;

            // Display results in requested order: Total Value -> Payable Value -> Gold Amount -> Total Zakat
            txtTotalValue.setText(String.format(Locale.getDefault(), "Total Gold Value: RM %.2f", totalGoldValue));
            txtPayable.setText(String.format(Locale.getDefault(), "Zakat payable gold value: RM %.2f", zakatPayable));
            txtGoldZakat.setText(String.format(Locale.getDefault(), "Zakat payable gold weight: %.2fg", payableWeight));
            txtZakat.setText(String.format(Locale.getDefault(), "Total Zakat: RM %.2f", totalZakat));

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input format", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetFields() {
        editWeight.setText("");
        editValue.setText("");
        radioGroupType.clearCheck();
        txtTotalValue.setText("");
        txtGoldZakat.setText("");
        txtPayable.setText("");
        txtZakat.setText("");
    }


}
