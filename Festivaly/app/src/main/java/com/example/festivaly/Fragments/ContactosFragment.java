package com.example.festivaly.Fragments;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.festivaly.Constantes;
import com.example.festivaly.Festivales.FestivalAdapter;
import com.example.festivaly.Peticion.Peticion;
import com.example.festivaly.Peticion.PeticionHolder;
import com.example.festivaly.R;
import com.example.festivaly.Usuario.ContactoAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactosFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDataBase;
    private LinearLayoutManager mLayoutManager;
    ArrayList<String> data;

    private RecyclerView rvContactos;
    private ContactoAdapter adapter;
    private GridLayoutManager glm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_contactos, container, false);
        getActivity().setTitle(Constantes.TAG_CONTACTOS);
        rvContactos = v.findViewById(R.id.list_contactos);

        descargarListaContactos();
        mostrarListaContactos();

        // Inflate the layout for this fragment
        return v;
    }

    private void mostrarListaContactos() {

        glm = new GridLayoutManager(getActivity(), 1);
        rvContactos.setLayoutManager(glm);

        adapter = new ContactoAdapter(getContext(),data);
        adapter.setOnItemClickListener(new ContactoAdapter.OnItemClickListener() {
            @Override
            public void onClickPerfil(int pos) {
                /*
                Uri addContactsUri = ContactsContract.Data.CONTENT_URI;

                // Add an empty contact and get the generated id.
                long rowContactId = getRawContactId();

                // Add contact name data.
                String displayName = adapter.getName();
                insertContactDisplayName(addContactsUri, rowContactId, displayName);

                // Add contact phone data.
                String phoneNumber = adapter.getPhone();
                insertContactPhoneNumber(addContactsUri, rowContactId, phoneNumber, "unknonw");

                Toast.makeText(getContext(),"New contact has been added, go back to previous page to see it in contacts list." , Toast.LENGTH_LONG).show();

                getActivity().finish();
                */
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                intent.putExtra(ContactsContract.Intents.Insert.NAME, adapter.getName());
                intent.putExtra(ContactsContract.Intents.Insert.PHONE,adapter.getPhone());
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL,adapter.getEmail());

                startActivity(intent);
            }

            @Override
            public void onClickBorrar(int pos) {

            }
        });

        rvContactos.setAdapter( adapter );

    }
    private long getRawContactId(){
        // Inser an empty contact.
        ContentValues contentValues = new ContentValues();
        Uri rawContactUri = getActivity().getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
        // Get the newly created contact raw id.
        long ret = ContentUris.parseId(rawContactUri);
        return ret;
    }


    private void insertContactDisplayName(Uri addContactsUri, long rawContactId, String displayName){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        // Put contact display name value.
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName);
        getActivity().getContentResolver().insert(addContactsUri, contentValues);
    }

    private void insertContactPhoneNumber(Uri addContactsUri, long rawContactId, String phoneNumber, String phoneTypeStr){
        // Create a ContentValues object.
        ContentValues contentValues = new ContentValues();
        // Each contact must has an id to avoid java.lang.IllegalArgumentException: raw_contact_id is required error.
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        // Put phone number value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        // Calculate phone type by user selection.
        int phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;

        if("home".equalsIgnoreCase(phoneTypeStr)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
        }else if("mobile".equalsIgnoreCase(phoneTypeStr)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        }else if("work".equalsIgnoreCase(phoneTypeStr)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
        }
        // Put phone type value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType);

        // Insert new contact data into phone contact list.
        getActivity().getContentResolver().insert(addContactsUri, contentValues);
    }

    private void descargarListaContactos() {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        data = new ArrayList<String>();

        mDataBase
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("contactos")
                .limitToFirst(10)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            data.add(ds.getKey());
                            Log.d("hm:" ,"["+ ds.getKey()+"] = "+ ds.getValue(Boolean.class));
                        }
                        mostrarListaContactos();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        //adapter();
    }
}
