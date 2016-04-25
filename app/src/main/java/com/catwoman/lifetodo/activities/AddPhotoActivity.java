package com.catwoman.lifetodo.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.fragments.AddPhotoGalleryFragment;
import com.catwoman.lifetodo.models.Category;

import io.realm.Realm;

public class AddPhotoActivity extends AppCompatActivity {

    private static int SELECT_FILE = 1;
    private ImageView ivImage;
    private Button btnDone;
    private AddPhotoGalleryFragment editPhotoNameDialogFragment;
    private Realm realm;
    private Category category;
    private String selectedImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        btnDone = (Button)findViewById(R.id.btnSelectPhoto);
        int categoryId = getIntent().getIntExtra("categoryId",0);
        realm = Realm.getDefaultInstance();

        category = realm.where(Category.class).equalTo("id", categoryId).findFirst();

        selectImage();


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPhotoActivity.this, TodoItemsActivity.class);
                intent.putExtra("categoryId", category.getId());
                startActivity(intent);
            }
        });

    }

    private void selectImage() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                selectedImagePath = cursor.getString(column_index);

                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);

                ivImage.setImageBitmap(bm);

                showEditDialog();
            }
        }
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        editPhotoNameDialogFragment = AddPhotoGalleryFragment.newInstance("",selectedImagePath,category);
        editPhotoNameDialogFragment.show(fm, "fragment_edit_name");
    }

}
