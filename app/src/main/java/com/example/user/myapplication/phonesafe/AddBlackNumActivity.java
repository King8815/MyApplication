package com.example.user.myapplication.phonesafe;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.R;
import com.example.user.myapplication.phonesafe.phonesafe_database.BlackNumberOperator;
import com.example.user.myapplication.phonesafe.phonesafe_entity.BlackContactInfo;

import java.net.URI;

public class AddBlackNumActivity extends Activity {
    private CheckBox mTelBox;
    private CheckBox mMsgBox;
    private EditText blackphone_edit;
    private EditText blackname_edit;
    private BlackNumberOperator operator;
    // 声明姓名，电话
    private String username = null;
    private String usernumber = null;
    private String number, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_black_num);
        operator = new BlackNumberOperator(this);
        initView();
    }

    private void initView() {
        //对嵌入的标题进行设置
        findViewById(R.id.rl_title).setBackgroundResource(R.color.purple);//对包括的title设置背景色
        ImageView mLeftView = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftView.setImageResource(R.mipmap.back);
        TextView mText = (TextView) findViewById(R.id.tv_title);
        mText.setText("添加黑名单");

        //设置本界面的控件属性
        blackphone_edit = (EditText) findViewById(R.id.blackphone_edit);
        blackname_edit = (EditText) findViewById(R.id.blackname_edit);
        mTelBox = (CheckBox) findViewById(R.id.phone_intercept);
        mMsgBox = (CheckBox) findViewById(R.id.msg_intercept);
    }

    /*
    * 函数用来执行控件的监听函数
    */
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.add_black:
                number = blackphone_edit.getText().toString().trim();
                name = blackname_edit.getText().toString().trim();
                if ("".equals(number) || "".equals(name)) {
                    Toast.makeText(this, "电话号码和手机号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //电话号码和名称都不为空
                    BlackContactInfo blackContactInfo = new BlackContactInfo();
                    blackContactInfo.phoneNumber = number;
                    blackContactInfo.contactName = name;
                    if (!mMsgBox.isChecked() & mTelBox.isChecked()) {
                        //电话拦截
                        blackContactInfo.mode = 1;
                    } else if (mMsgBox.isChecked() & !mTelBox.isChecked()) {
                        //短信拦截
                        blackContactInfo.mode = 2;
                    } else if (mMsgBox.isChecked() & mTelBox.isChecked()) {
                        //两种模式都选择
                        blackContactInfo.mode = 3;
                    } else {
                        Toast.makeText(this, "请选择拦截模式", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!operator.IsNumberExist(blackContactInfo.phoneNumber)) {
                        operator.add(blackContactInfo);
                    } else {
                        Toast.makeText(this, "该号码已经被添加至黑名单", Toast.LENGTH_SHORT).show();
                    }
//                    startActivity(new Intent(this,SecurityPhoneActivity.class));
                    finish();
                }
                break;
            case R.id.add_from_contact:
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI), 0);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (resultCode == Activity.RESULT_OK) {
            // ContentProvider展示数据类似一个单个数据库表,是Android提供的一个供Android多个应用程序数据共享的技术
            // ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
            ContentResolver resolver = getContentResolver();
            // URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
            Uri uri = data.getData();
            Cursor cursor = managedQuery(uri, null, null, null, null);
            cursor.moveToFirst();
            //获得DATA表中的名字
            username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            // 条件为联系人ID
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
            Cursor phone = resolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + contactId, null, null);
            while (phone.moveToNext()) {
                usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                blackphone_edit.setText(usernumber);
                blackname_edit.setText(username);
            }

        }*/
        if (resultCode == Activity.RESULT_OK) {
            // ContentProvider展示数据类似一个单个数据库表
            // ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
            ContentResolver reContentResolverol = getContentResolver();
            // URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
            Uri contactData = data.getData();
            Log.i("tag", contactData.toString());
            // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            Log.i("tag", cursor.toString());
            while (cursor.moveToFirst()){
                // 获得DATA表中的名字
                username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Log.i("tag", username);
                // 条件为联系人ID
                String contactId = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.Contacts._ID));
                // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
                Cursor phone = reContentResolverol.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null,
                        null);
                while (phone.moveToNext()) {
                    usernumber = phone
                            .getString(phone
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.i("tag", usernumber);
                    blackphone_edit.setText(usernumber);
                    blackname_edit.setText(username);
                }

            }
        }
    }
}
