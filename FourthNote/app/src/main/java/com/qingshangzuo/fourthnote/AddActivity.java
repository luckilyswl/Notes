package com.qingshangzuo.fourthnote;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.qingshangzuo.fourthnote.db.NoteDb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 1;
    private ImageView ivBack, ivUp, ivDown, ivHuanhang, ivPic, ivTakePic, ivText, ivPaint, ivPlay, imageView,ivSave;
    private EditText editText;
    private LinearLayout layoutEdither;
    private VideoView videoView;

    private String rootUrl = null;
    private NoteDb mDb;
    private SQLiteDatabase mSqldb;
    private final int CODE_PERMISSION = 4;
    private boolean FLAG_PERMISSION = false;
    private List<String> list;

    private final String TAG = getClass().getSimpleName();
    private File imageFile;
    private Uri mImageUri, mImageUriFromFile;
    private static final int TAKE_PHOTO = 189;
    private static final int CHOOSE_PHOTO = 385;
    private static final String FILE_PROVIDER_AUTHORITY = "com.qingshangzuo.fourthnote.fileprovider";

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        layoutEdither = findViewById(R.id.layout_editer);
        ivBack = findViewById(R.id.iv_back);
        ivUp = findViewById(R.id.iv_up);
        ivDown = findViewById(R.id.iv_down);
        ivHuanhang = findViewById(R.id.iv_huanhang);
        ivTakePic = findViewById(R.id.iv_take_pic);
        ivText = findViewById(R.id.iv_text);
        ivPic = findViewById(R.id.iv_pic);
        ivPaint = findViewById(R.id.iv_paint);
        ivPlay = findViewById(R.id.iv_play);
        imageView = findViewById(R.id.imageview);
        editText = findViewById(R.id.edt_text);
        videoView = findViewById(R.id.videoview);
        ivSave = findViewById(R.id.iv_save);

        ivBack.setOnClickListener(this);
        ivUp.setOnClickListener(this);
        ivDown.setOnClickListener(this);
        ivHuanhang.setOnClickListener(this);
        ivTakePic.setOnClickListener(this);
        ivText.setOnClickListener(this);
        ivPic.setOnClickListener(this);
        ivPaint.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        imageView.setOnClickListener(this);
        editText.setOnClickListener(this);
        ivSave.setOnClickListener(this);

        rootUrl = Environment.getExternalStorageDirectory().getPath();
        mDb = new NoteDb(this);
        mSqldb = mDb.getWritableDatabase();

        //获取图片权限
        PicPermission();
    }

    private void PicPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            list = new ArrayList<>();
            if(checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
            {
                list.add(Manifest.permission.CAMERA);
            }
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if(list.size()!=0)
                requestPermissions(list.toArray(new String[list.size()]),CODE_PERMISSION);
        }
        else {
            FLAG_PERMISSION = true;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                editText.setText("");
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.iv_up:
                //返回上一次步骤
                break;

            case R.id.iv_down:
                //返回下一次步骤
                break;

            case R.id.iv_huanhang:
                //换行

                break;

            case R.id.iv_pic:
                //添加图片
                if(ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSIONS_REQUEST_CODE);
                }else {
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, CHOOSE_PHOTO);//打开相册
                }
                imageView.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_text:
                //更该文字类型
                break;

            case R.id.iv_take_pic:
                //拍照并保存图片
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
                if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
                    imageFile = createImageFile();//创建用来保存照片的文件
                    mImageUriFromFile = Uri.fromFile(imageFile);
                    Log.i(TAG, "takePhoto: uriFromFile " + mImageUriFromFile);
                    if (imageFile != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            /*7.0以上要通过FileProvider将File转化为Uri*/
                            mImageUri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, imageFile);
                        } else {
                            /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                            mImageUri = Uri.fromFile(imageFile);
                        }
                        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//将用于输出的文件Uri传递给相机
                        startActivityForResult(takePhotoIntent, TAKE_PHOTO);//打开相机
                    }
                }
                imageView.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_paint:
                //画板
                break;

            case R.id.iv_play:
                // 录视频
                Intent video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                try{
                    fileUri= FileProvider.getUriForFile(AddActivity.this,getApplicationContext().getPackageName() + ".provider",createMediaFile());
                }catch (IOException e){
                    e.printStackTrace();
                }
                video.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
                video.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);

                startActivityForResult(video,1);
                videoView.setVisibility(View.VISIBLE);
                break;

            case R.id.edt_text:
                //添加文本
                layoutEdither.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_save:
                //保存
                ContentValues cv = new ContentValues();
                cv.put(NoteDb.CONTENT,editText.getText().toString());
                cv.put(NoteDb.TIME,getTime());
                mSqldb.insert(NoteDb.TABLE_NAME,null,cv);
                finish();

                break;

            default:
                break;
        }
    }

    private File createMediaFile() throws IOException {

        //if(Utils.checkSDCardAvaliable()) {
            if ((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
                // 选择自己的文件夹
                String path = Environment.getExternalStorageDirectory().getPath() + "/myvideo/";
                // Constants.video_url 是一个常量，代表存放视频的文件夹
                File mediaStorageDir = new File(path);
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.e("TAG", "文件夹创建失败");
                        return null;
                    }
                }

                // 文件根据当前的毫秒数给自己命名
                String timeStamp = String.valueOf(System.currentTimeMillis());
                timeStamp = timeStamp.substring(7);
                String imageFileName = "V" + timeStamp;
                String suffix = ".mp4";
                File mediaFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
                return mediaFile;
            }
        //}
        return null;
    }


    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    private String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();
        String str = sdf.format(date);
        return str;
    }

    /*申请权限的回调*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onRequestPermissionsResult: permission granted");
        } else {
            Log.i(TAG, "onRequestPermissionsResult: permission denied");
            Toast.makeText(this, "You Denied Permission", Toast.LENGTH_SHORT).show();
        }
    }

    /*相机或者相册返回来的数据*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode==RESULT_OK){
                Toast.makeText(this, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
                videoView.setVideoURI(fileUri);
                videoView.requestFocus();
            }
        }

        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        /*如果拍照成功，将Uri用BitmapFactory的decodeStream方法转为Bitmap*/
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                        Log.i(TAG, "onActivityResult: imageUri " + mImageUri);
                        galleryAddPic(mImageUriFromFile);
                        imageView.setImageBitmap(bitmap);//显示到ImageView上
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (data == null) {//如果没有拍照或没有选取照片，则直接返回
                    return;
                }
                Log.i(TAG, "onActivityResult: ImageUriFromAlbum: " + data.getData());
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        handleImageOnKitKat(data);//4.4之后图片解析
                    } else {
                        handleImageBeforeKitKat(data);//4.4之前图片解析
                    }
                }
                break;
            default:
                break;
        }
    }

    private void galleryAddPic(Uri uri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        sendBroadcast(mediaScanIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri，则提供document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则进行普通处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，则直接获取路径
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
