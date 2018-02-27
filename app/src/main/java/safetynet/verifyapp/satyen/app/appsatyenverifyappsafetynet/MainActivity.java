package safetynet.verifyapp.satyen.app.appsatyenverifyappsafetynet;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.safetynet.HarmfulAppsData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import safetynet.verifyapp.satyen.app.appsatyenverifyappsafetynet.model.AttestationResponseData;
import safetynet.verifyapp.satyen.app.appsatyenverifyappsafetynet.util.AdmobUtil;
import safetynet.verifyapp.satyen.app.appsatyenverifyappsafetynet.util.AttestationModelFactory;

public class MainActivity extends AppCompatActivity implements AttestationModel.AttestationModelListener {

    @BindView(R.id.tvResult)
    TextView tvResult;
    @BindView(R.id.btAtteststation)
    Button btAttestation;

    @BindView(R.id.btVerifyApps)
    Button btVerifyApps;

    AttestationModel mViewModel;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mViewModel = ViewModelProviders.of(this, new AttestationModelFactory(this)).get(AttestationModel.class);
        if (BuildConfig.DEBUG) {
            MobileAds.initialize(this,
                    "ca-app-pub-3940256099942544~3347511713");
        } else {
            MobileAds.initialize(this, Constants.APP_ID);
        }
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = null;

        if (BuildConfig.DEBUG) {
            String deviceId = AdmobUtil.getDeviceId(this);
            if (deviceId != null) {
                adRequest = new AdRequest.Builder()
                        .addTestDevice(deviceId)
                        .build();
            }


        } else {
            adRequest = new AdRequest.Builder()
                    .build();
        }
        if (adRequest != null) {
            mAdView.loadAd(adRequest);
        }
        createChooserDialog();

    }


    @OnClick(R.id.btAtteststation)
    public void getAttestationResult() {
        mViewModel.getAttestationResult();
    }


    @Override
    public void onAttestationResult(AttestationResponseData data) {
        tvResult.setText("Basic Integrity: " + data.getBasicIntegrity());
    }

    @Override
    public void onHarmfulAppResponse(List<HarmfulAppsData> appList) {
        Toast.makeText(this, "Got Response ", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btVerifyApps)
    public void verifyApps() {
        mViewModel.verfiyApps();
    }


    public void createChooserDialog() {
        String items[] = {"Attestation Api", "VerifyApps"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.chooserdialog)
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, R.string.attestation_api, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            case 1:
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // throw new RuntimeException();
                                    }
                                });
                                break;
                            case 2:
                                break;
                        }
                    }
                });
        builder.create().show();
    }


}
