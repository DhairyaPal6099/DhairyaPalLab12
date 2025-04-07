// Dhairya Pal N01576099

package dhairya.pal.n01576099.lab12;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;


import java.util.ArrayList;

public class PalActivity12 extends AppCompatActivity {

    Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dhaMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.dhaToolbar);
        setSupportActionBar(toolbar);

        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new Dh11airya());
        fragmentList.add(new Pa22l());
        fragmentList.add(new N0133576099());
        fragmentList.add(new DP44());

        TabLayout tabLayout = findViewById(R.id.dhaTabsLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedFragment = fragmentList.get(tab.getPosition());
                getSupportFragmentManager().beginTransaction().replace(R.id.dhaFragmentContainerView, selectedFragment).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}