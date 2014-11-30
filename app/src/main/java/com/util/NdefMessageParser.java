package com.util;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanaiyathacker on 30/11/2014.
 */
public class NdefMessageParser {
    // Utility class
    private NdefMessageParser() {

    }

    /** Parse an NdefMessage */
    public static List<ParsedNdefRecord> parse(NdefMessage message) {
        try {
            return getRecords(message.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ParsedNdefRecord> getRecords(NdefRecord[] records) throws Exception {
        List<ParsedNdefRecord> elements = new ArrayList<ParsedNdefRecord>();
        for (final NdefRecord record : records) {
            byte[] payload = record.getPayload();
            String str = new String(payload);
            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
            int languageCodeLength = payload[0] & 0077;
            String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            String text =
                    new String(payload, languageCodeLength + 1,
                            payload.length - languageCodeLength - 1, textEncoding);
            System.out.println(str);
        }
//        for (final NdefRecord record : records) {
//            if (UriRecord.isUri(record)) {
//                elements.add(UriRecord.parse(record));
//            } else if (TextRecord.isText(record)) {
//                elements.add(TextRecord.parse(record));
//            } else if (SmartPoster.isPoster(record)) {
//                elements.add(SmartPoster.parse(record));
//            } else {
//                elements.add(new ParsedNdefRecord() {
//                    @Override
//                    public View getView(Activity activity, LayoutInflater inflater, ViewGroup parent, int offset) {
//                        TextView text = (TextView) inflater.inflate(R.layout.tag_text, parent, false);
//                        text.setText(new String(record.getPayload()));
//                        return text;
//                    }
//
//                });
//            }
//        }
        return elements;
    }
}
