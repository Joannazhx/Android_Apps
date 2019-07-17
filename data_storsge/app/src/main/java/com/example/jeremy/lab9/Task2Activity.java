package com.example.jeremy.lab9;

import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;

public class Task2Activity extends AppCompatActivity {
    TextView txtStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_task2 );
        txtStorage = findViewById( R.id.txtStorage );
        txtStorage.setText( "" );


        if (  externalMemoryAvailable()){
            String avalaibleSpace = getAvailableInternalMemorySize();
            String ava = getSDFreeSize();
            txtStorage.setText( "Ext. Storage Available " +  ava +"MB\n"
                                +"internal :"+avalaibleSpace
            ) ;

        } else {
            txtStorage.setText( "Ext. Storage Not Available" );
        }


    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        Log.e("dcv",state);
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String secStore = System.getenv("SECONDARY_STORAGE");
        File f_secs = new File(secStore);
        f_secs.canRead();
        String state = Environment.getExternalStorageState();
        Log.e("dcv",state);
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }



    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return String.valueOf( (availableBlocks * blockSize)/ 1048576 );
    }

    public static boolean externalMemoryAvailable() {
        //get sd card status
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public String getSDFreeSize() {

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getBlockCount();
        long megAvailable = bytesAvailable / 1048576;
        return String.valueOf(megAvailable);
        //取得SD卡文件路径

        //File path = Environment.getExternalStorageDirectory();

        //StatFs sf = new StatFs(path.getPath());

        //获取单个数据块的大小(Byte)

        //long blockSize = sf.getBlockSize();

        //空闲的数据块的数量

        //long freeBlocks = sf.getAvailableBlocks();

        //返回SD卡空闲大小

        //return freeBlocks * blockSize;  //单位Byte

        //return (freeBlocks * blockSize)/1024;   //单位KB

        //return String.valueOf((freeBlocks * blockSize) / 1024 / 1024); //单位MB
    }


}