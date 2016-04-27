package com.catwoman.lifetodo.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.adapters.TodoItemsAdapter;
import com.catwoman.lifetodo.dbs.CategoryDb;
import com.catwoman.lifetodo.dbs.TodoItemDb;
import com.catwoman.lifetodo.fragments.AddTextFragment;
import com.catwoman.lifetodo.models.Category;
import com.catwoman.lifetodo.models.TodoItem;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class TodoItemsActivity extends AppCompatActivity {
    private static final String TAG = "TodoItemsActivity";
    private static final String APP_TAG = "LifeTodo";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int CAPTURE_IMAGE_REQUEST_CODE = 2;
    private static final int PICK_IMAGE_REQUEST_CODE = 3;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rvItems)
    RecyclerView rvItems;
    @Bind(R.id.tvMessage)
    TextView tvMessage;
    @Bind(R.id.vOverlay)
    View vOverlay;
    @Bind(R.id.famAdd)
    FloatingActionsMenu famAdd;
    @Bind(R.id.fabAddText)
    FloatingActionButton fabAddText;
    @Bind(R.id.fabAddPhoto)
    FloatingActionButton fabAddPhoto;
    @Bind(R.id.fabAddCamera)
    FloatingActionButton fabAddCamera;
    @Bind(R.id.fabAddLocation)
    FloatingActionButton fabAddLocation;
    private RealmResults<TodoItem> todoItems;
    private Category category;
    private CategoryDb categoryDb;
    private TodoItemDb todoItemDb;
    private TodoItemsAdapter todoItemsAdapter;
    private StaggeredGridLayoutManager layoutManager;
    private RealmChangeListener changeListener;
    private String photoFileName;
    private TodoItem pendingEditItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_items);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoryDb = CategoryDb.getInstance();
        todoItemDb = TodoItemDb.getInstance();

        int categoryId = getIntent().getIntExtra("categoryId", 0);
        category = categoryDb.getCategory(categoryId);
        getSupportActionBar().setTitle(category.getTitle());

        if (category.getName().equals("place")) {
            fabAddLocation.setVisibility(View.VISIBLE);
        }

        populateViews();
        setListeners();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Saving variables
        savedInstanceState.putString("photoFileName", photoFileName);

        // Call at the end
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call at the start
        super.onRestoreInstanceState(savedInstanceState);

        // Retrieve variables
        photoFileName = savedInstanceState.getString("photoFileName");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (null != pendingEditItem) {
            AddTextFragment fragment = AddTextFragment.newInstance(pendingEditItem.getCategory(), pendingEditItem);
            fragment.show(getSupportFragmentManager(), "fragment_add_text");
            pendingEditItem = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.toString());
                addPlace(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                addCapturedImage();
            } else { // Result was a failure
                Log.i(TAG, "Picture wasn't taken!");
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                addPickedImage(data);
            } else { // Result was a failure
                Log.i(TAG, "Image wasn't picked!");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        TodoItemsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        todoItems.removeChangeListener(changeListener);
    }

    private void populateViews() {
        todoItems = todoItemDb.getItems(category);
        todoItemsAdapter = new TodoItemsAdapter(todoItems);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvItems.setAdapter(todoItemsAdapter);
        rvItems.setLayoutManager(layoutManager);

        String message = getString(R.string.message_no_item, category.getTitle().toLowerCase());
        tvMessage.setText(message);
        toggleMessage();
    }

    private void setListeners() {
        changeListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                todoItemsAdapter.notifyDataSetChanged();
                toggleMessage();
            }
        };
        todoItems.addChangeListener(changeListener);

        famAdd.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                vOverlay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                vOverlay.setVisibility(View.GONE);
            }
        });

        fabAddCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoItemsActivityPermissionsDispatcher.showCameraWithCheck(TodoItemsActivity.this);
            }
        });
    }

    private void toggleMessage() {
        tvMessage.setVisibility(0 == todoItems.size() ? View.VISIBLE : View.GONE);
    }

    public void collapseFAM(View v) {
        famAdd.collapse();
    }

    public void showAddText(View v) {
        famAdd.collapseImmediately();

        AddTextFragment fragment = AddTextFragment.newInstance(category, null);
        fragment.show(getSupportFragmentManager(), "fragment_add_text");
    }

    public void showAddLocation(View v) {
        famAdd.collapseImmediately();

        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    private void addPlace(Place place) {
        todoItemDb.addItem(String.valueOf(place.getName()), String.valueOf(place.getAddress()),
                place.getLatLng().latitude, place.getLatLng().longitude, category);
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showCamera() {
        famAdd.collapseImmediately();

        // Renew file name
        photoFileName = newPhotoFileName();

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName)); // set the image file name

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForCamera(PermissionRequest request) {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
        showRationaleDialog(R.string.permission_camera_rationale, request);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onCameraDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onCameraNeverAskAgain() {
        Toast.makeText(this, R.string.permission_camera_never_askagain, Toast.LENGTH_SHORT).show();
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    private String newPhotoFileName() {
        String photoFileName = "img_" + Build.MANUFACTURER + "_" + Build.PRODUCT
                + "_" + new SimpleDateFormat("yyyyMMdd'-'HHmmss").format(new Date()) + ".jpg";
        return photoFileName.replaceAll(" ", "_");
    }

    private void addCapturedImage() {
        Uri takenPhotoUri = getPhotoFileUri(photoFileName);
        String thumbUrl = takenPhotoUri.toString();

        pendingEditItem = TodoItemDb.getInstance().addItem(thumbUrl, category);
    }

    public void onPickImage(View v) {
        famAdd.collapseImmediately();

        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Photo"), PICK_IMAGE_REQUEST_CODE);
    }

    private void addPickedImage(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String thumbUrl = cursor.getString(column_index);
        pendingEditItem = TodoItemDb.getInstance().addItem(thumbUrl, category);
    }
}
