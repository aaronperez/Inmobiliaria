package com.aaron.inmobiliaria;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class Detalle extends Fragment {

    private View v;
    private TextView tv;
    private ArrayList<File> fotos;
    private ListView lvF;
    private AdaptadorFotos adF;
    private String id;
    static final int REQUEST_TAKE_PHOTO = 1;



    public Detalle() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_detalle, container, false);
        tv=(TextView)v.findViewById(R.id.tvIdDetalle);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK) {
            tostada(R.string.archivoGuardado);
            setText(id);
        }
    }

    public void setText(String s){
        id=s;
        tv.setText(id);
        fotos=new ArrayList<File>();
        try {
            iniciarFotos(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        hacerFoto();
        adF = new AdaptadorFotos(getActivity(), R.layout.elementodetalle, fotos);
        lvF = (ListView)v.findViewById(R.id.lvFotos);
        lvF.setAdapter(adF);
        registerForContextMenu(lvF);
    }

    public void iniciarFotos(String id) throws IOException {
        //File [] array = getActivity().getExternalFilesDir(null).listFiles();
        File [] array = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).listFiles();
        if(array != null && array.length >0){
            for(File a : array){
                Log.v("file+file[]",getString(R.string.inmueble)+"_"+id+a.getPath() );
                if(a.getPath().contains(getString(R.string.inmueble)+"_"+id)){
                    fotos.add(a);
                }
            }
        }
        else{
            tostada(R.string.mensajeError);
        }
    }

    public void hacerFoto(){
        ImageButton button = (ImageButton)v.findViewById(R.id.ibFoto);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Asegura que se puede realizar una foto
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Crea el archivo donde irá la foto
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        tostada(R.string.archivoNoGuardado);;
                    }
                    // Continua si el archivo ha sido creado
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }
            }
        });
    }

    private File createImageFile() throws IOException {
        // Crear un nombre de archivo de imagen
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
        String imageFileName = getText(R.string.inmueble) + "_" + tv.getText() + "_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public void tostada(int s){
        Toast.makeText(getActivity(), getText(s).toString(), Toast.LENGTH_SHORT).show();
    }

}
