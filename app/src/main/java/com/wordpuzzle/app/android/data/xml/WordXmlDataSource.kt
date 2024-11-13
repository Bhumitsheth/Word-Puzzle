package com.wordpuzzle.app.android.data.xml

import android.content.Context
import android.content.res.AssetManager
import com.wordpuzzle.app.android.domain.data.source.WordDataSource
import com.wordpuzzle.app.android.domain.model.Word
import com.wordpuzzle.app.android.service.main.ServerApi
import com.wordpuzzle.app.android.utils.StoreSingletonData
import org.xml.sax.InputSource
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

/**
 * Created by abdularis on 31/07/17.
 */
class WordXmlDataSource @Inject constructor(context: Context, serverApi: ServerApi) : WordDataSource {
    private val mAssetManager: AssetManager
    lateinit var storeSingletonData: StoreSingletonData
    private lateinit var wordsList: List<String>
    private var currentPosition = 0
    private var mWordList: MutableList<Word>? = null

    init {
        mAssetManager = context.assets
        storeSingletonData = StoreSingletonData
    }

    override fun getWords(callback: WordDataSource.Callback?) {
        try {
//            val reader = SAXParserFactory.newInstance().newSAXParser().xmlReader
//            val wordBankHandler = SaxWordBankHandler()
//            reader.contentHandler = wordBankHandler
//            reader.parse(xmlInputSource)

            // Initialize your words list (you can replace this with your actual data)
            wordsList = storeSingletonData.getWordListKey()

            val strList = updateList()
            storeSingletonData.setStoreWordListKey(strList)

            val wordList: MutableList<Word> = ArrayList()
            if (strList.size >= 5) {
                for (i in strList.indices) {
                    wordList.add(Word(i + 1, strList[i].uppercase(Locale.getDefault())))
                }
            }

            // return result
            callback!!.onWordsLoaded(wordList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @get:Throws(IOException::class)
    private val xmlInputSource: InputSource
        get() = InputSource(mAssetManager.open(ASSET_WORD_BANK_FILE))

    companion object {
        private const val ASSET_WORD_BANK_FILE = "words.xml"
    }

    private fun getWordsList(): List<String> {
        // Here you would retrieve your list of words from the JSON response
        // For demonstration purposes, I'll return a static list
        return mutableListOf("ante", "libero", "integer", "diam", "nullam", "blandit", "varius", "dolor", "tellus", "luctus",
            "cursus", "class", "sapien", "morbi", "enim", "nostra", "orci", "felis", "Mauris", "sit",
            "sem", "purus", "auctor", "Column", "mollis", "velit", "Fusce", "per", "semper", "justo")
    }

    private fun updateList() : List<String>{
        var sublist: List<String> // Declare sublist variable

        // Initialize sublist with an empty list as default value
        sublist = emptyList()

        if (currentPosition < wordsList.size) {
            val endIndex = currentPosition + 10
            if (endIndex <= wordsList.size) {
                sublist = wordsList.subList(currentPosition, endIndex)
            } else {
                if (wordsList.size >= 5) {
                    sublist = wordsList.subList(currentPosition, wordsList.size)
                }
            }
            // Update UI or perform any actions with the sublist
            // For demonstration, let's print the sublist
            println(sublist)
            // Update current position
            currentPosition = endIndex
        }
        return sublist
    }
}
