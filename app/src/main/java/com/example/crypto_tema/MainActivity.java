package com.example.crypto_tema;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;

import javax.crypto.Cipher;

public class MainActivity extends AppCompatActivity {

    private void exempluCriptareAsimetricaFolosindRSA(){
        // https://www.developer.com/mobile/android/android-encryption-with-the-android-cryptography-api/
        // https://developer.android.com/reference/javax/crypto/package-summary

        // textul in clar
        String plainMessage = "Acesta este mesajul necriptat";

        // se genereaza o pereche de chei (publica/privata) pe 1024-biti ce vor fi utilizate de
        // algoritmul RSA
        Key publicKey;
        Key privateKey;
        try {
            // se preia instanta de 'generator de perechi de chei' si se seteaza marimea cheii (1024 biti)
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);

            // se preia instanta de 'pereche de chei' si se genereaza o cheie publica si una privata
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        } catch (Exception e) {
            Log.e("[ERROR]", "Nu a reusit generarea perechii de chei pentru algoritmul RSA");
            return;
        }

        // generarea perechii de chei a reusit

        // se cripteaza mesajul in clar folosindu-se cheia publica si algoritmul RSA
        byte[] encodedMessage;
        try {
            // se preia instanta 'cifrului de tip RSA' si se seteaza modul de criptare cu cheia publica
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // se cripteaza mesajul clar
            encodedMessage = cipher.doFinal(plainMessage.getBytes());

        } catch (Exception e) {
            Log.e("[ERROR]", "Nu a reusit criptarea mesajului clar");
            return;
        }

        // se decripteaza mesajul criptat folosindu-se cheia privata si algoritmul RSA
        String decodedMessage;
        try {
            // se preia instanta cifrului de tip RSA si se seteaza modul de decriptare cu cheia privata
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // se decripteaza mesajul criptat
            decodedMessage = new String(cipher.doFinal(encodedMessage), StandardCharsets.UTF_8);

        } catch (Exception e) {
            Log.e("[ERROR]", "Nu a reusit decriptarea mesajului criptat");
            return;
        }

        // afisare in UI
        TextView tvPrivateKey = findViewById(R.id.tvPrivateKey);
        TextView tvPublicKey = findViewById(R.id.tvPublicKey);
        TextView tvPlainMessage = findViewById(R.id.tvPlainMessage);
        TextView tvEncryptedMessage = findViewById(R.id.tvEncryptedMessage);
        TextView tvDecryptedMessage = findViewById(R.id.tvDecryptedMessage);

        tvPrivateKey.setText(privateKey.toString());
        tvPublicKey.setText(publicKey.toString());
        tvPlainMessage.setText(plainMessage);
        //tvEncryptedMessage.setText(new String(encodedMessage, StandardCharsets.UTF_8));
        tvEncryptedMessage.setText(Base64.encodeToString(encodedMessage, Base64.DEFAULT));
        tvDecryptedMessage.setText(decodedMessage);
    }

    private void exempluHashFolosindSHA256(){
        // https://developer.android.com/reference/java/security/MessageDigest
        // https://developer.android.com/guide/topics/security/cryptography#java

        try {
            String clearPassword = "aceasta-este-o-parola-in-clar";

            // Se preia instanta algoritmului 'SHA-256' si se apeleaza metoda 'digest' cu parola in
            // clar, iar outputul metodei va fi hash-ul generat de algoritm.
            byte[] hashedPassword = MessageDigest.getInstance("SHA-256")
                    .digest(clearPassword.getBytes(StandardCharsets.UTF_8));

            // afisare in UI
            TextView tvClearPassword = findViewById(R.id.tvClearPassword);
            tvClearPassword.setText(clearPassword);

            TextView tvHashedPassword = findViewById(R.id.tvHashedPassword);
            //tvHashedPassword.setText(new String(hashedPassword, StandardCharsets.UTF_8));
            tvHashedPassword.setText(Base64.encodeToString(hashedPassword, Base64.DEFAULT));

        } catch (Exception e) {
            Log.e("[ERROR]", "Nu a reusit aplicarea algoritmului de hash SHA-256 parolei");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_criptare_asimetrica);
        exempluCriptareAsimetricaFolosindRSA();

        //setContentView(R.layout.layout_aplicare_hash);
        //exempluHashFolosindSHA256();

    }
}