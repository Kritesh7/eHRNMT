package in.co.cfcs.ehrnmt.Source;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Admin on 31-10-2017.
 */

public class DownloadTask
{
    private static final String TAG = "Download Task";
    private Context context;
    private ProgressDialog pDialog;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    private String downloadUrl = "", downloadFileName = "";
    private Activity activity;
    public String checkNavigate ;

    public DownloadTask(Context context, String downloadUrl, String checkNavigate) {
        this.context = context;
        this.downloadUrl = downloadUrl;
        this.checkNavigate = checkNavigate;

        if (checkNavigate.equalsIgnoreCase("Appreciation")) {

            downloadFileName = downloadUrl.replace(SettingConstant.DownloadUrl + "Images/AppreceationFile/", "");//Create file name by picking download file name from URL

        }else if (checkNavigate.equalsIgnoreCase("Warning"))
        {
            downloadFileName = downloadUrl.replace(SettingConstant.DownloadUrl + "Images/WarningFile/", "");//Create file name by picking download file name from URL

        }else if (checkNavigate.equalsIgnoreCase("OfficealDocs"))
        {

            downloadFileName = downloadUrl.replace(SettingConstant.DownloadUrl + "Images/NMT_Document/", "");//Create file name by picking download file name from URL


        }else if (checkNavigate.equalsIgnoreCase("MedialAnssurance"))
        {
            downloadFileName = downloadUrl.replace(SettingConstant.DownloadUrl + "Images/MedicalPolicyDocument/", "");//Create file name by picking download file name from URL

        }
        Log.e("Download Url", downloadFileName);

        //Start Downloading Task
        new DownloadingTask().execute();
    }



    /**
     * Background Async Task to download file
     * */

     private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(context); //show Dialog when download started
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    pDialog.dismiss();//If Download completed then change button text
                    //First check if SD Card is present or not
                    openDownloadedFolder();

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs
                Toast.makeText(context, "Download Failed with Exception - " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

               // Log.e(TAG, );

            }


            super.onPostExecute(result);
        }



        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                // getting file length
                int lenghtOfFile = c.getContentLength();

                Log.e("check file lenth",lenghtOfFile + "");

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }


                //Get File if SD card is present
                if (new CheckForSDCard().isSDCardPresent()) {

                    apkStorage = new File(
                            Environment.getExternalStorageDirectory() + "/"
                                    + UtilsMethods.downloadDirectory);
                } else
                    Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage,downloadFileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e(TAG, "File Created");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {

                    fos.write(buffer, 0, len1);//Write new file


                }

                //Close all connection after doing task
                fos.close();
                is.close();


            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }

    //Open downloaded folder
    private void openDownloadedFolder() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
          alertDialog.setTitle("Alert");
        // Setting Dialog Message
        alertDialog.setMessage("You want to open this file");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                //Get Download Directory File
                File apkStorage = new File(
                        Environment.getExternalStorageDirectory() + "/"
                                + UtilsMethods.downloadDirectory + "/" + downloadFileName);


                if (!apkStorage.exists())
                    Toast.makeText(context, "Right now there is no directory. Please download some file first.", Toast.LENGTH_SHORT).show();

                else {

                    //If directory is present Open Folder

                    /** Note: Directory will open only if there is a app to open directory like File Manager, etc.  **/

                    //
                    // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    //Intent intent = new Intent(Intent.ACTION_VIEW);
                    // Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                    //         + "/"+UtilsMethods.downloadDirectory+"/");
                    // intent.setDataAndType(uri,"*/*");
                    //  context.startActivity(Intent.createChooser(intent, "Open Download Folder"));


                    //Uri uri= FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider", apkStorage);
                    // Uri uri = Uri.fromFile(apkStorage);

                    Uri uri;

                    //  Intent intent = new Intent();
                    //  intent.setAction(Intent.ACTION_VIEW);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        // intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        uri = FileProvider.getUriForFile(context, "in.co.cfcs.ehrnmt.provider", apkStorage);
                        //intent.setDataAndType(contentUri, type);
                    }else
                    {
                        uri = Uri.fromFile(apkStorage);
                    }



                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if (apkStorage.toString().contains(".doc") || apkStorage.toString().contains(".docx")) {
                        // Word document
                        intent.setDataAndType(uri, "application/msword");
                    } else if (apkStorage.toString().contains(".pdf")) {
                        // PDF file
                        intent.setDataAndType(uri, "application/pdf");
                    } else if (apkStorage.toString().contains(".ppt") || apkStorage.toString().contains(".pptx")) {
                        // Powerpoint file
                        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                    } else if (apkStorage.toString().contains(".xls") || apkStorage.toString().contains(".xlsx")) {
                        // Excel file
                        intent.setDataAndType(uri, "application/vnd.ms-excel");
                    } else if (apkStorage.toString().contains(".zip") || apkStorage.toString().contains(".rar")) {
                        // WAV audio file
                        intent.setDataAndType(uri, "application/x-wav");
                    } else if (apkStorage.toString().contains(".rtf")) {
                        // RTF file
                        intent.setDataAndType(uri, "application/rtf");
                    } else if (apkStorage.toString().contains(".wav") || apkStorage.toString().contains(".mp3")) {
                        // WAV audio file
                        intent.setDataAndType(uri, "audio/x-wav");
                    } else if (apkStorage.toString().contains(".gif")) {
                        // GIF file
                        intent.setDataAndType(uri, "image/gif");
                    } else if (apkStorage.toString().contains(".jpg") || apkStorage.toString().contains(".jpeg") || apkStorage.toString().contains(".png")) {
                        // JPG file
                        intent.setDataAndType(uri, "image/jpeg");
                    } else if (apkStorage.toString().contains(".txt")) {
                        // Text file
                        intent.setDataAndType(uri, "text/plain");

                    } else if (apkStorage.toString().contains(".3gp") || apkStorage.toString().contains(".mpg") ||
                            apkStorage.toString().contains(".mpeg") || apkStorage.toString().contains(".mpe") || apkStorage.toString().contains(".mp4") || apkStorage.toString().contains(".avi")) {
                        // Video files
                        intent.setDataAndType(uri, "video/*");
                    } else {
                        intent.setDataAndType(uri, "*/*");
                    }

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {

                        context.startActivity(intent);

                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "This File Foramt is wrong", Toast.LENGTH_SHORT).show();
                    }
                }


               /* Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
                context.startActivity(browserIntent);
                dialog.dismiss();*/



            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();




    }

    public void old ()
    {
        //First check if SD Card is present or not
        if (new CheckForSDCard().isSDCardPresent()) {

            //Get Download Directory File
            File apkStorage = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + UtilsMethods.downloadDirectory);

            Log.e("file Path", apkStorage.getPath() + "Null");

            //If file is not present then display Toast
            if (!apkStorage.exists())
                Toast.makeText(context, "Right now there is no directory. Please download some file first.", Toast.LENGTH_SHORT).show();

            else {
//
                //If directory is present Open Folder

                /** Note: Directory will open only if there is a app to open directory like File Manager, etc.  **/

                //
                // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //Intent intent = new Intent(Intent.ACTION_VIEW);
                // Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                //         + "/"+UtilsMethods.downloadDirectory+"/");
                // intent.setDataAndType(uri,"*/*");
                //  context.startActivity(Intent.createChooser(intent, "Open Download Folder"));


                //Uri uri= FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider", apkStorage);
                // Uri uri = Uri.fromFile(apkStorage);

                Uri uri;

                //  Intent intent = new Intent();
                //  intent.setAction(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    uri = FileProvider.getUriForFile(context, "in.co.cfcs.ehrnmt.provider", apkStorage);
                    //intent.setDataAndType(contentUri, type);
                }else
                {
                    uri = Uri.fromFile(apkStorage);
                }



                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (apkStorage.toString().contains(".doc") || apkStorage.toString().contains(".docx")) {
                    // Word document
                    intent.setDataAndType(uri, "application/msword");
                } else if (apkStorage.toString().contains(".pdf")) {
                    // PDF file
                    intent.setDataAndType(uri, "application/pdf");
                } else if (apkStorage.toString().contains(".ppt") || apkStorage.toString().contains(".pptx")) {
                    // Powerpoint file
                    intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                } else if (apkStorage.toString().contains(".xls") || apkStorage.toString().contains(".xlsx")) {
                    // Excel file
                    intent.setDataAndType(uri, "application/vnd.ms-excel");
                } else if (apkStorage.toString().contains(".zip") || apkStorage.toString().contains(".rar")) {
                    // WAV audio file
                    intent.setDataAndType(uri, "application/x-wav");
                } else if (apkStorage.toString().contains(".rtf")) {
                    // RTF file
                    intent.setDataAndType(uri, "application/rtf");
                } else if (apkStorage.toString().contains(".wav") || apkStorage.toString().contains(".mp3")) {
                    // WAV audio file
                    intent.setDataAndType(uri, "audio/x-wav");
                } else if (apkStorage.toString().contains(".gif")) {
                    // GIF file
                    intent.setDataAndType(uri, "image/gif");
                } else if (apkStorage.toString().contains(".jpg") || apkStorage.toString().contains(".jpeg") || apkStorage.toString().contains(".png")) {
                    // JPG file
                    intent.setDataAndType(uri, "image/jpeg");
                } else if (apkStorage.toString().contains(".txt")) {
                    // Text file
                    intent.setDataAndType(uri, "text/plain");

                } else if (apkStorage.toString().contains(".3gp") || apkStorage.toString().contains(".mpg") ||
                        apkStorage.toString().contains(".mpeg") || apkStorage.toString().contains(".mpe") || apkStorage.toString().contains(".mp4") || apkStorage.toString().contains(".avi")) {
                    // Video files
                    intent.setDataAndType(uri, "video/*");
                } else {
                    intent.setDataAndType(uri, "*/*");
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

        } else {
            Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();
        }
    }

}
