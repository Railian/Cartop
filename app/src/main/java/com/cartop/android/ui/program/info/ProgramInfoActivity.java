package com.cartop.android.ui.program.info;

import android.Manifest;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.cartop.android.R;
import com.cartop.android.autoboot.BootService;
import com.cartop.android.core.api.ApiDelegate;
import com.cartop.android.core.api.ApiManager;
import com.cartop.android.core.connectivity.ConnectivityManager;
import com.cartop.android.core.connectivity.NetworkInfoListener;
import com.cartop.android.core.media.AdCallback;
import com.cartop.android.core.media.Category;
import com.cartop.android.core.models.Ad;
import com.cartop.android.core.models.Program;
import com.cartop.android.core.models.ProgramsPage;
import com.cartop.android.helpers.MediaHelper;
import com.cartop.android.helpers.PermissionHelper;
import com.cartop.android.settings.AppSettingsManager;
import com.cartop.android.ui.preview_image.PreviewImageActivity;
import com.cartop.android.ui.preview_video.PreviewVideoActivity;
import com.cartop.android.ui.program.preview.ProgramActivity;
import com.cartop.android.ui.progress.Orientation;
import com.cartop.android.ui.progress.ProgressFragment;
import com.cartop.android.ui.progress.ProgressTheme;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;


public class ProgramInfoActivity extends AppCompatActivity
        implements OnItemSelectedListener, View.OnClickListener,
        ProgramInfoAdViewHolder.OnAdClickListener, ProgressFragment.ActionListener,
        PopupMenu.OnMenuItemClickListener, PopupMenu.OnDismissListener, NetworkInfoListener {

    //region constants
    private static final String TAG = ProgramInfoActivity.class.getSimpleName();
    private static final String STATE_SELECTED_PROGRAM = "STATE_SELECTED_PROGRAM";
    private static final int API_REQUEST_GET_PROGRAMS = 1;
    //endregion

    //region widgets
    private Toolbar toolbar;
    private Spinner spinner;
    private RecyclerView rvProgramInfo;
    private FloatingActionButton fab;
    //endregion

    //region fields
    private ProgramsSpinnerAdapter spinnerAdapter;
    private Program selectedProgram;
    private ProgramInfoAdapter infoAdapter;
    private PermissionHelper permissionHelper;
    //endregion

    //region Life-Cycles
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_program_info);

        ApiManager.get().getDelegatesSet().addDelegate(TAG, apiDelegate);
        MediaHelper.get().getAdCallbacks().addDelegate(TAG, downloadFileCallback);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        spinner = (Spinner) findViewById(R.id.spinner);
        rvProgramInfo = (RecyclerView) findViewById(R.id.activity_program_info_rvProgramInfo);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        spinnerAdapter = new ProgramsSpinnerAdapter();
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        infoAdapter = new ProgramInfoAdapter();
        infoAdapter.setOnAdClickListener(this);
        rvProgramInfo.setAdapter(infoAdapter);

        if (savedInstanceState != null) {
            spinnerAdapter.restoreState(savedInstanceState);
            selectedProgram = savedInstanceState.getParcelable(STATE_SELECTED_PROGRAM);
        } else loadData(ConnectivityManager.get().isNetworkConnected());

        fab.setOnClickListener(this);
        ConnectivityManager.get().setNetworkInfoListener(this);

        permissionHelper = new PermissionHelper(this);
        permissionHelper.verifyPermission(0, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.program_info, menu);
        if (ConnectivityManager.get().isNetworkConnected()) {
            menu.findItem(R.id.action_network_connected).setVisible(true);
            menu.findItem(R.id.action_network_not_connected).setVisible(false);
        } else {
            menu.findItem(R.id.action_network_connected).setVisible(false);
            menu.findItem(R.id.action_network_not_connected).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_network_connected:
            case R.id.action_network_not_connected:
                break;
            case R.id.action_update_programs_list:
                showLoadingProgramsProgress();
                ApiManager.get().getPrograms(TAG, API_REQUEST_GET_PROGRAMS);
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_force_crash:
                throw new NullPointerException("Force Crash in Program Info Screen!");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        spinnerAdapter.saveState(outState);
        outState.putParcelable(STATE_SELECTED_PROGRAM, selectedProgram);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConnectivityManager.get().setNetworkInfoListener(null);
        ApiManager.get().getDelegatesSet().removeDelegate(apiDelegate);
        MediaHelper.get().getAdCallbacks().removeDelegate(downloadFileCallback);
    }
    //endregion

    //region Progress Tools
    private void showLoadingProgramsProgress() {
        //noinspection ConstantConditions
        getSupportActionBar().hide();
        fab.hide();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_activity_program_info_vProgressContainer, ProgressFragment.newInstance(ProgressTheme.LIGHT, Orientation.HORIZONTAL, "Loading programs..."), ProgressFragment.TAG)
                .commit();
    }

    private void showDownloadAdContentProgress(Ad ad) {
        //noinspection ConstantConditions
        getSupportActionBar().hide();
        fab.hide();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_activity_program_info_vProgressContainer, ProgressFragment.newInstance(ProgressTheme.DARK, Orientation.HORIZONTAL, String.format("Loading ad content...\nad title: %s\nad id: %s", ad.getBody().getTitle(), ad.getBody().getId()), "Dismiss"), ProgressFragment.TAG)
                .commit();
    }

    private void updateDownloadAdContentProgress(Ad ad, long fileSize, long fileSizeDownloaded) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_activity_program_info_vProgressContainer);
        if (fragment != null && fragment instanceof ProgressFragment)
            ((ProgressFragment) fragment).setMessage(String.format(Locale.getDefault(), "Loading ad content...\nad title: %s\nad id: %s\n%.2fMb of %.2fMb", ad.getBody().getTitle(), ad.getBody().getId(), fileSizeDownloaded / 1024 / 1024., fileSize / 1024 / 1024.));
    }

    private void hideProgress() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_activity_program_info_vProgressContainer);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        //noinspection ConstantConditions
        getSupportActionBar().show();
        fab.show();
    }
    //endregion

    //region Public Tools
    private void loadData(boolean networkConnected) {
        if (networkConnected) {
            showLoadingProgramsProgress();
            ApiManager.get().getPrograms(TAG, API_REQUEST_GET_PROGRAMS);
            selectedProgram = (Program) spinner.getSelectedItem();
        } else {
            ProgramsPage defaultProgramsPage = new ProgramsPage();
            ArrayList<Program> defaultData = new ArrayList<>();
            defaultData.add(Program.getDefault());
            defaultProgramsPage.setData(defaultData);
            spinnerAdapter.setProgramsPage(defaultProgramsPage);
            selectedProgram = (Program) spinner.getSelectedItem();
            infoAdapter.setProgram(selectedProgram);
            fab.show();
        }
    }
    //endregion

    //region OnItemSelectedListener Implementation
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedProgram = (Program) spinnerAdapter.getItem(position);
        infoAdapter.setProgram(selectedProgram);
        fab.show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selectedProgram = null;
        fab.hide();
    }
    //endregion

    //region OnClickListener Implementation
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Log.d(TAG, "onClick() called with: " + "selectedProgram = [" + selectedProgram + "]");
                AppSettingsManager.get().setRunningProgram(selectedProgram);
                BootService.setProgramIsRunning(this, true);
                ProgramActivity.startFromOutside(this);
                break;
        }
    }
    //endregion

    //region apiDelegate
    private ApiDelegate apiDelegate = new ApiDelegate.SimpleApiDelegate() {
        @Override
        public void onReceiveProgramsPage(int requestCode, ProgramsPage programsPage) {
            spinnerAdapter.setProgramsPage(programsPage);
            hideProgress();
            selectedProgram = (Program) spinner.getSelectedItem();
            infoAdapter.setProgram(selectedProgram);
            fab.show();

        }

        @Override
        public void onFailure(int requestCode, Throwable throwable) {
            if (requestCode == API_REQUEST_GET_PROGRAMS)
                ApiManager.get().getPrograms(TAG, API_REQUEST_GET_PROGRAMS);
        }
    };
    //endregion

    private Ad selectedAd;

    @Override
    public void onLoadContentClick(Ad ad, int adapterPosition) {
        MediaHelper.get().downloadAdFile(selectedAd = ad, TAG);
    }

    private AdCallback downloadFileCallback = new AdCallback() {
        @Override
        public void onDownloadStarted(Ad ad) {
            showDownloadAdContentProgress(ad);
        }

        @Override
        public void onDownloadProgressChanged(Ad ad, long fileSize, long fileSizeDownloaded) {
            updateDownloadAdContentProgress(ad, fileSize, fileSizeDownloaded);
        }

        @Override
        public void onDownloadFailed(Ad ad, Throwable throwable) {
            hideProgress();
        }

        @Override
        public void onDownloadCanceled(Ad ad) {
            hideProgress();
        }

        @Override
        public void onFileDownloaded(Ad ad, File file) {
            hideProgress();
            infoAdapter.notifyAdChanged(ad);
        }
    };

    @Override
    public void onActionClick(Fragment fragment) {
        MediaHelper.get().cancelDownload(selectedAd);
    }

    @Override
    public void onContentClick(Ad ad, int adapterPosition) {
        switch (Category.getFromContentType(ad.getBody().getContentType())) {
            case IMAGE: {
                Intent intent = new Intent(this, PreviewImageActivity.class);
                intent.putExtra(PreviewImageActivity.EXTRA_AD, ad);
                startActivity(intent);
                break;
            }
            case VIDEO: {
                Intent intent = new Intent(this, PreviewVideoActivity.class);
                intent.putExtra(PreviewVideoActivity.EXTRA_CONTENT_PATH, MediaHelper.get().getAdFile(ad).getPath());
                startActivity(intent);
                break;
            }
            case UNKNOWN:
                break;
        }
    }

    @Override
    public void onContentLongClick(View view, Ad ad, int adapterPosition) {
        selectedAd = ad;
        PopupMenu popup = new PopupMenu(this, view);
        popup.inflate(R.menu.program_info_ad_context);
        popup.setOnMenuItemClickListener(this);
        popup.setOnDismissListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update_content:
                //noinspection ResultOfMethodCallIgnored
                MediaHelper.get().deleteAdFile(selectedAd);
                infoAdapter.notifyAdChanged(selectedAd);
                MediaHelper.get().downloadAdFile(selectedAd, TAG);
                return true;
            case R.id.action_delete_content:
                //noinspection ResultOfMethodCallIgnored
                MediaHelper.get().deleteAdFile(selectedAd);
                infoAdapter.notifyAdChanged(selectedAd);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDismiss(PopupMenu menu) {
        selectedAd = null;
    }

    @Override
    public void onReceiveNetworkInfo(boolean connectionStateChanged, boolean networkConnected, NetworkInfo activeNetworkInfo) {
        if (connectionStateChanged) {
            invalidateOptionsMenu();
            loadData(networkConnected);
        }
    }
}
