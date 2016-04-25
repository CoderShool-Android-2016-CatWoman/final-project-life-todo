package com.catwoman.lifetodo.fragments;

/**
 * Created by HUONGVU on 4/26/2016.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.dbs.TodoItemDb;
import com.catwoman.lifetodo.models.Category;

/**
 * Created by annt on 4/9/16.
 */
public class AddPhotoGalleryFragment extends DialogFragment {
    private EditText mEditText;
    private Button mBtnSave;
    private static String imagePath;


    public AddPhotoGalleryFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddPhotoGalleryFragment newInstance(String title,String imageFolder, Category category) {
        AddPhotoGalleryFragment frag = new AddPhotoGalleryFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putParcelable("category", category);
        frag.setArguments(args);
        imagePath = imageFolder;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_camera_add, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.etItemName);
        mBtnSave = (Button) view.findViewById(R.id.btnSaveItem);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Item Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = mEditText.getText().toString();
                String itemThumb = "";
                if(!imagePath.equals("")){
                    itemThumb = imagePath;
                }
                TodoItemDb.getInstance().addOrUpdateItem(0, String.valueOf(itemName), itemThumb,
                        "InProgress", "", "", "", 0, 0,
                        (Category) getArguments().getParcelable("category")
                );

                dismiss();
            }
        });

    }
}
