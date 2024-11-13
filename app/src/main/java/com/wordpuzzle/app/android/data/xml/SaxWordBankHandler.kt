package com.wordpuzzle.app.android.data.xml

import com.wordpuzzle.app.android.domain.model.Word
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler

/**
 * Created by abdularis on 31/07/17.
 */
internal class SaxWordBankHandler : DefaultHandler() {
    private var mWordList: MutableList<Word>? = null
    @Throws(SAXException::class)
    override fun startDocument() {
        mWordList = ArrayList()
    }

    @Throws(SAXException::class)
    override fun startElement(
        uri: String,
        localName: String,
        qName: String,
        attributes: Attributes
    ) {
        if (qName.equals(XML_ITEM_TAG_NAME, ignoreCase = true)) {
            val word = Word(mWordList!!.size, attributes.getValue(XML_STR_ATTRIB_NAME))
            mWordList!!.add(word)
        }
    }

    val words: List<Word>?
        get() = mWordList

    companion object {
        private const val XML_ITEM_TAG_NAME = "item"
        private const val XML_STR_ATTRIB_NAME = "str"
    }
}
