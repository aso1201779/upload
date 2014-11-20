package com.example.upload;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
     * 画像用POSTリクエスト
     * @param url リクエスト先
     * @param image 送信する画像(バイト配列)
     * @param params 一緒に送るパラメータ
     * @return
     */

    public String postMultipart(String url, byte[] image, String params) {
        HttpClient client = new DefaultHttpClient();
        String str = "";

        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entity.setCharset(Charset.forName("UTF-8"));
        try {
            // 画像をセット
            // 第一引数：パラメータ名
            // 第二引数：画像データ
            // 第三引数：画像のタイプ。jpegかpngかは自由
            // 第四引数：画像ファイル名。ホントはContentProvider経由とかで取って来るべきなんだろうけど、今回は見えない部分なのでパス
            entity.addBinaryBody("avater", image, ContentType.create("image/png"), "hoge.png");
            url = "http://example.com/image.json";

            // 画像以外のデータを送る場合はaddTextBodyを使う
            ContentType textContentType = ContentType.create("application/json","UTF-8");
            entity.addTextBody("auth_token", params, textContentType);

            HttpPost post = new HttpPost(url);
            post.setEntity(entity.build());

            HttpResponse httpResponse = client.execute(post);
            str = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            Log.i("HTTP status Line", httpResponse.getStatusLine().toString());
            Log.i("HTTP response", new String(str));
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return str;
    }

}
