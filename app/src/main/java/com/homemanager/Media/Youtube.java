package com.homemanager.Media;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.homemanager.R;
import com.homemanager.Task.Action.PlayTask;
import com.homemanager.Task.Action.StatusMessage;
import com.homemanager.Task.Action.VideoShareTask;
import com.homemanager.Task.Youtube.YoutubeObject;
import com.homemanager.TaskConnector;

import java.io.InputStream;
import java.util.ArrayList;

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            //Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

public class Youtube  extends Activity {
    ArrayList<YoutubeObject> youtubeVideosList;

    public Youtube(ArrayList<YoutubeObject> youtubeVideosList){
        this.youtubeVideosList = youtubeVideosList;
    }

    public void YoutubeUpdateList(View view, final StatusMessage statusMessage, final TaskConnector taskConnector) {
        TableLayout tl;

        tl = (TableLayout) view.findViewById(R.id.ytTableLayout);
        // Stuff that updates the UI
        tl.removeAllViews();
        for (final YoutubeObject obj : youtubeVideosList) {
            //obj.get
            try {
                TableRow tr1 = new TableRow(view.getContext());

                tr1.setLayoutParams(new TableRow.LayoutParams(tl.getWidth(),//TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                TextView textview = new TextView(view.getContext());
                textview.setText(obj.getTitle() + " " + obj.getDuration());
                textview.setTextColor(Color.BLACK);
                textview.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                textview.setHorizontallyScrolling(false);

                textview.setSingleLine(false);
                textview.setMaxLines(3);
                textview.setMinLines(1);
                textview.setMaxWidth((int) (tl.getWidth() / 2));

                ImageView image = new ImageView(view.getContext());
                image.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        taskConnector.putNewTask(new VideoShareTask(statusMessage, obj.getVideoLink()));
                        statusMessage.displayHint(R.string.HintWaitForData);
                    }
                });
                new DownloadImageTask((ImageView) image)
                        .execute(obj.getIconLink());

                //image.setImageDrawable(getResources().getDrawable(description.getIcon()));

                tr1.addView(image);
                tr1.addView(textview);

                LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) textview.getLayoutParams();
                p.rightMargin = 10;
                p.leftMargin = 20;
                p.gravity = Gravity.CENTER | Gravity.LEFT;


                image.getLayoutParams().height = 150;
                image.getLayoutParams().width = 200;

                tl.addView(tr1, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
            } catch (Exception e) {
            }
        }
    }

/*
    runOnUiThread(new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {
            if (connectionChecker.isConnectionErrorAppear() ||
                    connectionChecker.isConnectionEstablishInProgress()){
                return;
            }

            tl = (TableLayout) findViewById(R.id.News);
            // Stuff that updates the UI
            tl.removeAllViews();

            for (TaskDescription description : taskDesc) {

                try {
                    TableRow tr1 = new TableRow(getApplicationContext());

                    tr1.setLayoutParams(new TableRow.LayoutParams(tl.getWidth(),//TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    TextView textview = new TextView(getApplicationContext());
                    textview.setText(description.getDescription() + " " + description.getDate());
                    textview.setTextColor(Color.BLACK);
                    textview.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    textview.setHorizontallyScrolling(false);

                    textview.setSingleLine(false);
                    textview.setMaxLines(3);
                    textview.setMinLines(1);
                    textview.setMaxWidth((int) (tl.getWidth() / 2));

                    ImageView image = new ImageView(getApplicationContext());
                    image.setImageDrawable(getResources().getDrawable(description.getIcon()));

                    TextView textview_date = new TextView(getApplicationContext());
                    textview_date.setText(description.getDate());
                    textview_date.setTextColor(Color.BLACK);

                    tr1.addView(image);
                    tr1.addView(textview);
                    //tr1.addView(textview_date);

                    LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) textview.getLayoutParams();
                    p.rightMargin = 10;
                    p.leftMargin = 20;
                    p.gravity = Gravity.CENTER | Gravity.LEFT;


                    image.getLayoutParams().height = 60;
                    image.getLayoutParams().width = 60;

                    //textview_date.getLayoutParams().width = tl.getWidth() - 60 - textview.getMaxWidth();

                    tl.addView(tr1, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                }
                catch (Exception e){}
            }

        }
    });*/

}
