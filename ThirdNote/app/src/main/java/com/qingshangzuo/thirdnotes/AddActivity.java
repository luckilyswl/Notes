package com.qingshangzuo.thirdnotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.qingshangzuo.thirdnotes.db.NoteDb;
import com.qingshangzuo.thirdnotes.util.HelpUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int LOCAL_IMAGE_CODE = 1;
    private static final int CAMERA_IMAGE_CODE = 2;
    private static final String IMAGE_TYPE = "image/*";
    private String rootUrl = null;
    private String curFormatDateStr = null;

    private Button btnPic,btnVideo;
    private ImageView showImageIv;

    private EditText mEt;
    private NoteDb mDb;
    private SQLiteDatabase mSqldb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        findById();
        initData();

        mEt = findViewById(R.id.edt_text);
        mDb = new NoteDb(this);
        mSqldb = mDb.getWritableDatabase();
    }

    private void initData() {
        rootUrl = Environment.getExternalStorageDirectory().getPath();
    }

    private void findById() {

        btnPic = findViewById(R.id.btn_pic);
        btnVideo = findViewById(R.id.btn_video);
        showImageIv = findViewById(R.id.id_image_iv);


        btnPic.setOnClickListener(this);
        btnVideo.setOnClickListener(this);
    }

    public void save(View v){
        DbAdd();
        finish();
    }

    public void cancle(View v) {
        mEt.setText("");
        finish();
    }

    public void DbAdd() {
        ContentValues cv = new ContentValues();
        cv.put(NoteDb.CONTENT,mEt.getText().toString());
        cv.put(NoteDb.TIME,getTime());
        mSqldb.insert(NoteDb.TABLE_NAME,null,cv);
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();
        String str = sdf.format(date);
        return str;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pic:
                processLocal();
                break;
            case R.id.btn_video:
                processCamera();
                break;
        }
    }

    private void processLocal() {
        Intent intent = new Intent();
        /* 开启Pictures画面Type设定为image */
        intent.setType(IMAGE_TYPE);
        /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
        /* 取得相片后返回本画面 */
        startActivityForResult(intent, LOCAL_IMAGE_CODE);
    }

    private void processCamera() {
        curFormatDateStr = HelpUtil.getDateFormatString(Calendar.getInstance()
                .getTime());
        String fileName = "IMG_" + curFormatDateStr + ".png";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(rootUrl, fileName)));
        intent.putExtra("fileName", fileName);
        startActivityForResult(intent, CAMERA_IMAGE_CODE);
    }

    /**
     * 处理Activity跳转后返回事件
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String url = "";
            Bitmap bitmap = null;
            if (requestCode == LOCAL_IMAGE_CODE) {
                Uri uri = data.getData();
                url = uri.toString().substring(
                        uri.toString().indexOf("///") + 2);
                Log.e("uri", uri.toString());
                if (url.contains(".jpg") && url.contains(".png")) {
                    Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                bitmap = HelpUtil.getBitmapByUrl(url);
                showImageIv.setImageBitmap(HelpUtil.getBitmapByUrl(url));

                /**
                 * 获取bitmap还有一种方法
                 *
                 * ContentResolver cr = this.getContentResolver(); bitmap =
                 * HelpUtil.getBitmapByUri(uri, cr);
                 */

            } else if (requestCode == CAMERA_IMAGE_CODE) {
                url = rootUrl + "/" + "IMG_" + curFormatDateStr + ".png";
                bitmap = HelpUtil.getBitmapByUrl(url);
                showImageIv.setImageBitmap(HelpUtil.createRotateBitmap(bitmap));

                /**
                 * 获取bitmap还有一种方法
                 *
                 * File picture = new File(url);
                 * Uri uri = Uri.fromFile(picture);
                 * ContentResolver cr = this.getContentResolver();
                 * bitmap = HelpUtil.getBitmapByUri(uri, cr);
                 */
            }
        } else {
            Toast.makeText(this, "没有加入图片", Toast.LENGTH_SHORT).show();
        }

    }
}
