package plantopia.sungshin.plantopia.Diray;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import plantopia.sungshin.plantopia.R;
import plantopia.sungshin.plantopia.User.AutoLoginManager;
import plantopia.sungshin.plantopia.User.ServerURL;
import plantopia.sungshin.plantopia.User.UserData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDiaryActivity extends AppCompatActivity {
    final static int EDIT_DIARY = 13;

    @BindView(R.id.diary_img)
    ImageView diaryImg;
    @BindView(R.id.diary_text)
    TextView diaryText;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_diary);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        Glide.with(this).load(intent.getStringExtra("imgPath")).into(diaryImg);
        diaryText.setText(intent.getStringExtra("content"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_diary_show, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_edit:
                Intent edit = new Intent(ShowDiaryActivity.this, EditDiaryActivity.class);
                edit.putExtra("content", diaryText.getText().toString());
                edit.putExtra("imgPath", getIntent().getStringExtra("imgPath"));
                edit.putExtra("id", getIntent().getStringExtra("id"));
                startActivityForResult(edit, EDIT_DIARY);
                break;

            case R.id.menu_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowDiaryActivity.this);
                builder.setTitle("다이어리 삭제")
                        .setMessage("다이어리를 삭제하시겠습니까? 삭제한 다이어리의 기록은 복구가 불가능합니다.")
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(RESULT_OK);
                                finish();
                            }
                        }).show();

                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_DIARY && resultCode == RESULT_OK) {
            Glide.with(this).load(data.getStringExtra("imgPath")).into(diaryImg);
            diaryText.setText(data.getStringExtra("content"));
        }
    }
}