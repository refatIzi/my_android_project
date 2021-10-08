package com.example.testedit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Storege extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private List<Model> List = new ArrayList<>();
    ListAdapter Adapter;
    private ListView ListView;
    String  sesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storege);
        ListView=findViewById(R.id.listStoreg);
        ListView.setOnItemClickListener(this);

        sesion = String.valueOf(Environment.getExternalStorageDirectory());
        browseTo( sesion);
    }
    private void browseTo(String aDirectory) {


        List.clear();
        File dir = new File(aDirectory);
        // если объект представляет каталог
        final String[] sDirList = dir.list();
        // упаковываем данные в понятную для адаптера структуру
        for (int i = 0; i < sDirList.length; i++) {

            File f1 = new File(aDirectory + File.separator + sDirList[i]);
            if (f1.isFile()) {
                String filenameArray[] = sDirList[i].split("\\.");
                String extension = filenameArray[filenameArray.length - 1];
                // сравниваем с помощью метода .equals("jpg")
                if (extension.equals("py"))
                {
                    List.add(new Model(sDirList[i], "no information",
                            "here will be information about the file or folder", R.drawable.ic_filepython, false));
                }
               else if (extension.equals("zip")||extension.equals("7z")||extension.equals("tar")||extension.equals("gz")
                        ||extension.equals("tar"))

                {
                   List.add(new Model(sDirList[i], "no information"+ "",
                           "here will be information about the file or folder", R.drawable.ic_archive, false));
                }
               else if (extension.equals("jpg")||extension.equals("png")||extension.equals("gif"))// "jpg" или "png"
                {
                    List.add(new Model(sDirList[i], "no information"+ "",
                            "here will be information about the file or folder", R.drawable.ic_image, false));
                }
                else if (extension.equals("apk"))// "jpg" или "png"
                {
                    List.add(new Model(sDirList[i], "no information"+ "",
                            "here will be information about the file or folder", R.drawable.ic_application, false));
                }
                else if (extension.equals("xz"))// "jpg" или "png"
                {
                    List.add(new Model(sDirList[i], "no information"+ "",
                            "here will be information about the file or folder", R.drawable.ic_arkchiv_conteyner, false));
                }
                else if (extension.equals("xml")||extension.equals("html"))// "jpg" или "png"
                {
                    List.add(new Model(sDirList[i], "no information"+ "",
                            "here will be information about the file or folder", R.drawable.ic_xml_file, false));
                }
                else if (extension.equals("mp4")||extension.equals("m4v")||extension.equals("avi")||extension.equals("mov")
                        ||extension.equals("mpg")||extension.equals("mpeg"))// "jpg" или "png"
                {
                    List.add(new Model(sDirList[i], "no information"+ "",
                            "here will be information about the file or folder", R.drawable.ic_playlist, false));
                }
                else  if(extension.equals("mp3")||extension.equals("wav")||extension.equals("wma"))// "jpg" или "png"
                // ссылка сравнения строки https://www.internet-technologies.ru/articles/sravnenie-strok-v-java.html
                {
                    List.add(new Model(sDirList[i],"no information"+ "",
                            "here will be information about the file or folder", R.drawable.ic_audiofile,false));
                }
                else  if(extension.equals("txt"))// "jpg" или "png"
                // ссылка сравнения строки https://www.internet-technologies.ru/articles/sravnenie-strok-v-java.html
                {
                    List.add(new Model(sDirList[i],"no information"+ "",
                            "here will be information about the file or folder", R.drawable.ic_textxfile,false));
                }
                else  if(extension.equals("pdf"))// "jpg" или "png"
                // ссылка сравнения строки https://www.internet-technologies.ru/articles/sravnenie-strok-v-java.html
                {
                    List.add(new Model(sDirList[i],"no information"+ "",
                            "here will be information about the file or folder", R.drawable.ic_pdfile,false));
                }
                else  if(extension.equals("exe"))// "jpg" или "png"
                // ссылка сравнения строки https://www.internet-technologies.ru/articles/sravnenie-strok-v-java.html
                {
                    List.add(new Model(sDirList[i],"no information"+ "",
                            "here will be information about the file or folder", R.drawable.ic_applicationxdesigner,false));
                }
                else  if(extension.equals("PPTX")||extension.equals("PPTM")||extension.equals("PPTM"))// "jpg" или "png"
                // ссылка сравнения строки https://www.internet-technologies.ru/articles/sravnenie-strok-v-java.html
                {
                    List.add(new Model(sDirList[i],"no information"+ "",
                            "here will be information about the file or folder", R.drawable.ic_officepresentation,false));
                }
                else  if(extension.equals("doc")||extension.equals("docx"))// "jpg" или "png"
                // ссылка сравнения строки https://www.internet-technologies.ru/articles/sravnenie-strok-v-java.html
                {
                    List.add(new Model(sDirList[i],"no information"+ "",
                            "here will be information about the file or folder", R.drawable.ic_officedocument,false));
                }
                else  if(extension.equals("XLSX")||extension.equals("XLSM")||extension.equals("XLTX")||extension.equals("XLTM")
                        ||extension.equals("XLS") ||extension.equals("XLT")||extension.equals("XLS")||extension.equals("CSV"))// "jpg" или "png"
                // ссылка сравнения строки https://www.internet-technologies.ru/articles/sravnenie-strok-v-java.html
                {
                    List.add(new Model(sDirList[i],"no information"+ "",
                            "here will be information about the file or folder", R.drawable.ic_officeexel,false));
                }
               else
                {
                    List.add(new Model(sDirList[i],  "no information",
                            "here will be information about the file or folder", R.drawable.ic_file_other, false));
                }
            }
            else
            {
                List.add(new Model(sDirList[i], "no information",
                        "here will be information about the file or folder", R.drawable.ic_folder_f, false));
            //https://icon-icons.com/ru/pack/Papirus-Places/1379&page=5
            }


        }
        Adapter = new ListAdapter(Storege.this, R.layout.iteam_row, List);
        ListView.setAdapter(Adapter);
    }

   /**Обработчик нажатия на ListView*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final File aDirectory = new File(sesion+"/"+Adapter.getItem(position).getNomber());
        if(aDirectory.isFile())
        {
            //https://android-tools.ru/coding/delimsya-fajlami-v-android-s-pomoshhyu-fileprovider/
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // устанавливаем флаг для того, чтобы дать внешнему приложению пользоваться нашим FileProvider
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // генерируем URI, я определил полномочие как ID приложения в манифесте, последний параметр это файл, который я хочу открыть
            Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, new File(sesion + "/" + Adapter.getItem(position).getNomber() + ""));
            // я открываю PDF-файл, поэтому я даю ему действительный тип MIME
            intent.setDataAndType(uri, comparision(Adapter.getItem(position).getNomber()));
            //    Activity activity= new Activity();
            // подтвердите, что устройство может открыть этот файл!
           /*    PackageManager pm = activity.getPackageManager();
            if (intent.resolveActivity(pm) != null) {
                startActivity(intent);
            }*/
            startActivity(intent);
        }
        else
        {
            sesion=sesion+ "/" + Adapter.getItem(position).getNomber()+"";
            String[] parts = sesion.split("/");
            setTitle(""+parts[parts.length-1]);
            browseTo(sesion);
        }
    }

    /**Метод с помошью которого мы прослеживаем формат файла и выбераем чем его открыть*/
    private String comparision(String fileName)
    {
        String filenameArray[] = fileName.split("\\.");
        String extension = filenameArray[filenameArray.length-1];
        if(extension.equals("jpg")||extension.equals("png")||extension.equals("gif"))// "jpg" или "png"

        {
            return "image/*";
        }
        else  if(extension.equals("mp3")||extension.equals("wav")||extension.equals("wma"))// "jpg" или "png"

        {
            return "audio/*";
        }
        else  if(extension.equals("mp4")||extension.equals("m4v")||extension.equals("avi")||extension.equals("mov")||extension.equals("mpg")||extension.equals("mpeg"))// "jpg" или "png"

        {
            return "video/*";
        }
        else return "*/*";
    }
    //метот обработки функциональной кнопки назад https://overcoder.net/q/380976/%D0%BC%D0%BD%D0%B5-%D0%BD%D1%83%D0%B6%D0%BD%D0%BE-%D1%87%D1%82%D0%BE%D0%B1%D1%8B-%D1%81%D0%B2%D0%B5%D1%80%D0%BD%D1%83%D1%82%D1%8C-%D0%BF%D1%80%D0%B8%D0%BB%D0%BE%D0%B6%D0%B5%D0%BD%D0%B8%D0%B5-android-%D0%BF%D1%80%D0%B8-%D0%BD%D0%B0%D0%B6%D0%B0%D1%82%D0%B8%D0%B8-%D0%BA%D0%BD%D0%BE%D0%BF%D0%BA%D0%B8-%D0%BD%D0%B0%D0%B7%D0%B0%D0%B4
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
                //  Toast.makeText(getApplicationContext(), "buss " + buss, Toast.LENGTH_LONG).show();
                Sesion();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void Sesion()
    {
        String Directory="";
        String[] parts = sesion.split("/");
        setTitle(""+parts[parts.length-2]);
        for(int i=0;i!=parts.length-1;i++)
        {
            // была проблема в которой  без условия накалдывалась //// слещ впереди директорий
            // чтобы это избежать было установлено условие: что если i == 0 то без слеща (/)
            // если иначе то со слешои
            if(i==0)
            {
                Directory=Directory+parts[i];
            }
            else
            {
                Directory=Directory+"/"+parts[i];
            }
        }
        sesion=Directory;
        if( sesion.equals("/storage/emulated"))
        {
            finish();//Закрыаем коно!
        }
        else {
            browseTo( sesion);
        }
    }
}