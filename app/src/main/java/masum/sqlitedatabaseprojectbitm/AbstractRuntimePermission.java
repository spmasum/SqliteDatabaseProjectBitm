package masum.sqlitedatabaseprojectbitm;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.View;

/**
 * Created by spmas on 29-May-18.
 */

public abstract class AbstractRuntimePermission extends AppCompatActivity {
    private SparseIntArray mErrorString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mErrorString = new SparseIntArray();


    }

    public abstract void onPermissionGranted(int requestCode);

    public void requestAppPermissions(final String[] requestedPermission, final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean showRequestPermission = false;

        for (String permission : requestedPermission) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            showRequestPermission = showRequestPermission || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);

        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (showRequestPermission) {
                Snackbar.make(findViewById(android.R.id.content), stringId, Snackbar.LENGTH_INDEFINITE).setAction("GRANT", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(AbstractRuntimePermission.this, requestedPermission, requestCode);
                    }
                }).show();

            }else {
                ActivityCompat.requestPermissions(this,requestedPermission,requestCode);
            }

        }else {
            onPermissionGranted(requestCode);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck=PackageManager.PERMISSION_GRANTED;
        for (int permission:grantResults){
            permissionCheck=permissionCheck+permission;
        }
        if (grantResults.length>0&&PackageManager.PERMISSION_GRANTED==permissionCheck){
            onPermissionGranted(requestCode);
        }else {
            //Display msg when some dangerous permission not accept
            Snackbar.make(findViewById(android.R.id.content),mErrorString.get(requestCode),Snackbar.LENGTH_INDEFINITE).setAction("Enable", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:"+getPackageName()));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intent);
                }
            }).show();
        }
    }
}