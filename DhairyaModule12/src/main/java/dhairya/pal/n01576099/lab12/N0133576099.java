// Dhairya Pal N01576099

package dhairya.pal.n01576099.lab12;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class N0133576099 extends Fragment {
    private int counter = 0;

    public N0133576099() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_n0133576099, container, false);

        WebView webView = view.findViewById(R.id.dhaWebview);
        Spinner spinner = view.findViewById(R.id.dhaSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                webView.loadUrl(getResources().getStringArray(R.array.website_urls)[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        
        //AdView setup
        AdView adview = view.findViewById(R.id.dhaAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);
        //Counter implementation on AdView
        adview.setOnClickListener(view1 -> {
            counter++;
            Toast.makeText(getContext(), getString(R.string.dhairya_pal) + counter, Toast.LENGTH_SHORT).show();
        });
        
        return view;
    }
}