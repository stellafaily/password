package com.example.encryptionmemo.common

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.encryptionmemo.MyApplication
import com.example.encryptionmemo.R
import java.security.DigestException
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Utils {

    @JvmStatic
    fun getRandomResourceId(): Int{
        MyApplication.prefs.noteColorIndex += 1
        return when(MyApplication.prefs.noteColorIndex%7){
            0 -> R.drawable.bg_round10_banana
            1 -> R.drawable.bg_round10_vanillacream
            2 -> R.drawable.bg_round10_pear2
            3 -> R.drawable.bg_round10_pear3
            4 -> R.drawable.bg_round10_greenapple
            5 -> R.drawable.bg_round10_crayolapeach
            else -> R.drawable.bg_round10_pear
        }
    }

    @BindingAdapter("randomDrawable")
    @JvmStatic
    fun randomDrawable(view : View, resourceId: Int){
        view.setBackgroundResource(resourceId)
    }

    @JvmStatic
    fun getLockString(value: Boolean): Int {
        return if (value) { R.string.this_memo_lock_save } else { R.string.this_memo_unlock_save }
    }

    @BindingAdapter("encDrawable")
    @JvmStatic
    fun lockDrawable(view : ImageView, hasEnc: Boolean){
        if(hasEnc) {
            view.setImageResource(R.drawable.ic_addmemo_lock)
        }else{
            view.setImageResource(R.drawable.ic_addmemo_unlock)
        }
    }

    @JvmStatic
    fun falseIsVisible(value: Boolean): Int {
        return if (value) { View.GONE } else { View.VISIBLE }
    }

    @JvmStatic
    fun trueIsVisible(value: Boolean): Int {
        return if (value) { View.VISIBLE } else { View.GONE }
    }

    @JvmStatic
    fun editedAtString(time: Long): String{
        return if(hasKorean()) {
            SimpleDateFormat("yyyy.M.d a h:m").format(Date(time))
        }else{
            SimpleDateFormat("MM/dd/yy a h:m").format(Date(time))
        }
    }

    @JvmStatic
    fun hasKorean(): Boolean{
        return Locale.getDefault().language == "ko"
    }

    @JvmStatic
    fun decData(encKey: String, encData: ByteArray): String {
        var iv = ByteArray(16)
        val keySpec = SecretKeySpec(hashSHA256(encKey), "AES")
        val cipher_dec = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher_dec.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv))
        val byteDecryptedText = cipher_dec.doFinal(encData)
        return String(byteDecryptedText)
    } // 데이터 복호화
    @JvmStatic
    fun decData2(encKey: String, encData2: ByteArray): String {
        var iv = ByteArray(16)
        val keySpec = SecretKeySpec(hashSHA256(encKey), "AES")
        val cipher_dec = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher_dec.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv))
        val byteDecryptedText = cipher_dec.doFinal(encData2)
        return String(byteDecryptedText)
    }


    @JvmStatic
    fun encData(encKey: String, data: String,): ByteArray {
        var iv = ByteArray(16)
        val keySpec = SecretKeySpec(hashSHA256(encKey), "AES")
        val cipher_enc = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher_enc.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv))
        return cipher_enc.doFinal(data.toByteArray())
    } // 데이터 암호화
    @JvmStatic
    fun encData2(encKey: String, data2: String,): ByteArray {
        var iv = ByteArray(16)
        val keySpec = SecretKeySpec(hashSHA256(encKey), "AES")
        val cipher_enc = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher_enc.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv))
        return cipher_enc.doFinal(data2.toByteArray())
    }


    @JvmStatic
    private fun hashSHA256(msg: String): ByteArray {
        val hash: ByteArray
        try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(msg.toByteArray())
            hash = md.digest()
        } catch (e: CloneNotSupportedException) {
            throw DigestException("couldn't make digest of partial content")
        }
        return hash
    }
}