package com.dinodevs.pacecalendarwidget;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URL;

public class FilesUtil {

    public static String getiCalURL(Context context) {

        String line = null;
        File file = new File (context.getExternalFilesDir(null), Constants.CALENDAR_CONFIG_FILE);
        if (file.exists ()) {
            Log.d(Constants.TAG, String.format("FilesUtil getiCalURL file: %s found", file.getName()));
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                line = reader.readLine();
                line = line.trim();
                Log.d(Constants.TAG, String.format("FilesUtil getiCalURL line: %s", line));

                String test = line.toLowerCase();
                boolean isICSFile = !test.isEmpty() && (test.startsWith("http://") || test.startsWith("https://")) && test.endsWith("ics");
                if (!isICSFile)
                    line = null;

                reader.close();
            }
            catch (Exception e) {
                Log.e(Constants.TAG, e.getLocalizedMessage(), e);

            }
        }
        return line;
    }

    public static class urlToFile extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {

            String url = strings[0];
            String saveDir = strings[1];
            String file = strings[2];
            Log.d(Constants.TAG, "FilesUtil urlToFile url: " + url + " saveDir: " + saveDir + " file: " + file);

            BufferedInputStream in = null;
            long length = 0;
            try {
                in = new BufferedInputStream(new URL(url).openStream());
                FileOutputStream out = new FileOutputStream(saveDir + File.separator + file);
                byte[] buffer = new byte[1024];
                int c;
                while ((c = in.read(buffer)) != -1) {
                    out.write(buffer, 0, c);
                    length += c;
                }
                out.close();
                in.close();
            } catch (Exception e) {
                Log.e(Constants.TAG, e.getLocalizedMessage(), e);
                return false;
            } finally {
                Log.d(Constants.TAG, "FilesUtil urlToFile length: " + length);
            }
            return true;
        }
    }
}
