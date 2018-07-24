package aspace.trya;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Begin the transaction
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.main_container, new PhoneLoginFragment());
        ft.commit();
    }
}
